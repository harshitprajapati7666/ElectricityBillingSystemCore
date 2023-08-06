package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenerateBill extends JFrame implements ActionListener {

    private JLabel titleLabel, meterLabel;
    private JTextArea billTextArea;
    private JButton generateButton;
    private JComboBox<String> monthComboBox;
    private JPanel panel;
    private String meter;

    public GenerateBill(String meter) {
        this.meter = meter;
        setSize(500, 900);
        setLayout(new BorderLayout());

        panel = new JPanel(new GridBagLayout());
        titleLabel = new JLabel("Generate Bill");
        meterLabel = new JLabel(meter);
        monthComboBox = new JComboBox<>();

        monthComboBox.addItem("January");
        monthComboBox.addItem("February");
        monthComboBox.addItem("March");
        monthComboBox.addItem("April");
        monthComboBox.addItem("May");
        monthComboBox.addItem("June");
        monthComboBox.addItem("July");
        monthComboBox.addItem("August");
        monthComboBox.addItem("September");
        monthComboBox.addItem("October");
        monthComboBox.addItem("November");
        monthComboBox.addItem("December");

        billTextArea = new JTextArea(50, 15);
        billTextArea.setText("\n\n\t------- Click on the -------\n\t Generate Bill Button to get\n\tthe bill of the Selected Month\n\n");
        JScrollPane scrollPane = new JScrollPane(billTextArea);
        billTextArea.setFont(new Font("Senserif", Font.ITALIC, 18));

        generateButton = new JButton("Generate Bill");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(meterLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(monthComboBox, gbc);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(generateButton, BorderLayout.SOUTH);

        generateButton.addActionListener(this);
        setLocation(750, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (DbConnection connection = new DbConnection();
             PreparedStatement customerStatement = connection.prepareStatement("SELECT * FROM customer WHERE meter = ?");
             PreparedStatement meterInfoStatement = connection.prepareStatement("SELECT * FROM meter_info WHERE meter_number = ?");
             PreparedStatement taxStatement = connection.prepareStatement("SELECT * FROM tax");
             PreparedStatement billStatement = connection.prepareStatement("SELECT * FROM bill WHERE meter = ? AND month = ?")) {

            customerStatement.setString(1, meter);
            ResultSet rsCustomer = customerStatement.executeQuery();

            meterInfoStatement.setString(1, meter);
            ResultSet rsMeterInfo = meterInfoStatement.executeQuery();

            ResultSet rsTax = taxStatement.executeQuery();

            billStatement.setString(1, meter);
            billStatement.setString(2, (String) monthComboBox.getSelectedItem());
            ResultSet rsBill = billStatement.executeQuery();

            billTextArea.setText("\tHarshit Power Limited\nELECTRICITY BILL FOR THE MONTH OF " + monthComboBox.getSelectedItem() + ", 2023\n\n\n");

            if (rsCustomer.next()) {
                billTextArea.append("\n   Customer Name: " + rsCustomer.getString("name"));
                billTextArea.append("\n   Meter Number:   " + rsCustomer.getString("meter"));
                billTextArea.append("\n   Address:            " + rsCustomer.getString("address"));
                billTextArea.append("\n   State:                  " + rsCustomer.getString("state"));
                billTextArea.append("\n   City:                   " + rsCustomer.getString("city"));
                billTextArea.append("\n   Email:              " + rsCustomer.getString("email"));
                billTextArea.append("\n   Phone Number:   " + rsCustomer.getString("phone"));
                billTextArea.append("\n----------------------------------------------------------------");
                billTextArea.append("\n");
            }

            if (rsMeterInfo.next()) {
                billTextArea.append("\n   Meter Location: " + rsMeterInfo.getString("meter_location"));
                billTextArea.append("\n   Meter Type:       " + rsMeterInfo.getString("meter_type"));
                billTextArea.append("\n   Phase Code:    " + rsMeterInfo.getString("phase_code"));
                billTextArea.append("\n   Bill Type:          " + rsMeterInfo.getString("bill_type"));
                billTextArea.append("\n   Days:               " + rsMeterInfo.getString("days"));
                billTextArea.append("\n");
            }

            if (rsTax.next()) {
                billTextArea.append("------------------------------------------------------------------");
                billTextArea.append("\n\n");
                billTextArea.append("\n Cost per Unit: ₹              " + rsTax.getString("cost_per_unit"));
                billTextArea.append("\n Meter Rent: ₹                 " + rsTax.getString("meter_rent"));
                billTextArea.append("\n Service Charge: ₹             " + rsTax.getString("service_charge"));
                billTextArea.append("\n Service Tax: ₹                " + rsTax.getString("service_tax"));
                billTextArea.append("\n Swachh Bharat Cess: ₹         " + rsTax.getString("swachh_bharat_cess"));
                billTextArea.append("\n Fixed Tax: ₹                  " + rsTax.getString("fixed_tax"));
                billTextArea.append("\n");
            }

            if (rsBill.next()) {
                billTextArea.append("\n       Current Month:  \t" + rsBill.getString("month"));
                billTextArea.append("\n       Units Consumed: \t" + rsBill.getString("units"));
                billTextArea.append("\n       Total Charges:  \t" + rsBill.getString("total_bill"));
                billTextArea.append("\n-----------------------------------------------------------------");
                billTextArea.append("\n       TOTAL PAYABLE:  \t" + rsBill.getString("total_bill"));
            }

            // Close result sets
            rsCustomer.close();
            rsMeterInfo.close();
            rsTax.close();
            rsBill.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while generating the bill.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GenerateBill("").setVisible(true);
        });
    }
}
