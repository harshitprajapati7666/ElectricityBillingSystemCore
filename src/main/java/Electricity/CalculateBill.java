package Electricity;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CalculateBill extends JFrame implements ActionListener {
    private JLabel titleLabel, meterLabel, nameLabel, addressLabel, unitsLabel, monthLabel;
    private JTextField unitsTextField;
    private JComboBox<String> meterComboBox, monthComboBox;
    private JButton submitButton, cancelButton;
    private HikariDataSource dataSource;

    public CalculateBill() {
        setTitle("Calculate Electricity Bill");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create the main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230));
        setContentPane(mainPanel);

        // Create the top panel for the title
        JPanel topPanel = new JPanel();
        titleLabel = new JLabel("Calculate Electricity Bill");
        titleLabel.setFont(new Font("Senserif", Font.PLAIN, 26));
        topPanel.add(titleLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create the center panel for input fields
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(173, 216, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        meterLabel = new JLabel("Meter No");
        centerPanel.add(meterLabel, gbc);

        meterComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        centerPanel.add(meterComboBox, gbc);

        nameLabel = new JLabel("Name");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        centerPanel.add(nameLabel, gbc);

        JLabel nameValueLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        centerPanel.add(nameValueLabel, gbc);

        addressLabel = new JLabel("Address");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        centerPanel.add(addressLabel, gbc);

        JLabel addressValueLabel = new JLabel();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        centerPanel.add(addressValueLabel, gbc);

        unitsLabel = new JLabel("Units Consumed");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        centerPanel.add(unitsLabel, gbc);

        unitsTextField = new JTextField(10);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        centerPanel.add(unitsTextField, gbc);

        monthLabel = new JLabel("Month");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        centerPanel.add(monthLabel, gbc);

        monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        centerPanel.add(monthComboBox, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create the bottom panel for buttons
        JPanel bottomPanel = new JPanel();
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        bottomPanel.add(submitButton);
        bottomPanel.add(cancelButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);

        // Set the size and make the frame visible
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        initConnectionPool();
        populateMeterComboBox();
    }

    private void initConnectionPool() {
        // Configuration for HikariCP connection pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DbConfig.DB_URL);
        config.setUsername(DbConfig.DB_USER);
        config.setPassword(DbConfig.DB_PASSWORD);

        // Create the data source
        dataSource = new HikariDataSource(config);
    }

    private void populateMeterComboBox() {
        String query = "SELECT meter FROM customer";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                meterComboBox.addItem(resultSet.getString("meter"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching the meter numbers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            String meterNo = (String) meterComboBox.getSelectedItem();
            String units = unitsTextField.getText();
            String month = (String) monthComboBox.getSelectedItem();

            if (meterNo.isEmpty() || units.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO bill VALUES (?, ?, ?, ?, 'Not Paid')")) {

                int unitsConsumed = Integer.parseInt(units);

                // Retrieve the tariff details
                String tariffQuery = "SELECT * FROM tax";
                try (PreparedStatement tariffStatement = connection.prepareStatement(tariffQuery);
                     ResultSet rs = tariffStatement.executeQuery()) {

                    int totalBill = 0;
                    if (rs.next()) {
                        totalBill = unitsConsumed * rs.getInt("cost_per_unit");
                        totalBill += rs.getInt("meter_rent");
                        totalBill += rs.getInt("service_charge");
                        totalBill += rs.getInt("service_tax");
                        totalBill += rs.getInt("swachh_bharat_cess");
                        totalBill += rs.getInt("fixed_tax");
                    }

                    // Insert the bill details into the database
                    statement.setString(1, meterNo);
                    statement.setString(2, month);
                    statement.setString(3, units);
                    statement.setInt(4, totalBill);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Customer Bill Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == cancelButton) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculateBill());
    }
}
