package Electricity;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepositDetails extends JFrame implements ActionListener {

    private JTable table;
    private JButton searchButton, printButton;
    private JLabel meterLabel, monthLabel;
    private JComboBox<String> meterComboBox, monthComboBox;
    private String[] columnNames = {"Meter Number", "Month", "Total Bill", "Status"};
    private DefaultTableModel tableModel;
    private HikariDataSource dataSource;

    public DepositDetails() {
        super("Deposit Details");
        setSize(700, 750);
        setLocation(600, 150);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        initConnectionPool();

        meterLabel = new JLabel("Sort by Meter Number");
        meterLabel.setBounds(20, 20, 150, 20);
        add(meterLabel);

        meterComboBox = new JComboBox<>();
        meterComboBox.setBounds(180, 20, 150, 20);
        add(meterComboBox);

        monthLabel = new JLabel("Sort by Month");
        monthLabel.setBounds(400, 20, 100, 20);
        add(monthLabel);

        monthComboBox = new JComboBox<>();
        monthComboBox.setBounds(520, 20, 150, 20);
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
        add(monthComboBox);

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0, 100, 700, 650);
        add(scrollPane);

        searchButton = new JButton("Search");
        searchButton.setBounds(20, 70, 80, 20);
        searchButton.addActionListener(this);
        add(searchButton);

        printButton = new JButton("Print");
        printButton.setBounds(120, 70, 80, 20);
        printButton.addActionListener(this);
        add(printButton);

        try {
            populateMeterComboBox();
            populateTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private void populateMeterComboBox() throws SQLException {
        String query = "SELECT meter FROM customer";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                meterComboBox.addItem(resultSet.getString("meter"));
            }
        }
    }

    private void populateTable() throws SQLException {
        String query = "SELECT * FROM bill";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String meterNumber = resultSet.getString("meter");
                String month = resultSet.getString("month");
                String totalBill = resultSet.getString("total_bill");
                String status = resultSet.getString("status");

                tableModel.addRow(new String[]{meterNumber, month, totalBill, status});
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == searchButton) {
            String meter = (String) meterComboBox.getSelectedItem();
            String month = (String) monthComboBox.getSelectedItem();
            String query = "SELECT * FROM bill WHERE meter = ? AND month = ?";

            try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {

                statement.setString(1, meter);
                statement.setString(2, month);
                ResultSet resultSet = statement.executeQuery();

                tableModel.setRowCount(0);
                while (resultSet.next()) {
                    String meterNumber = resultSet.getString("meter");
                    String mth = resultSet.getString("month");
                    String totalBill = resultSet.getString("total_bill");
                    String status = resultSet.getString("status");

                    tableModel.addRow(new String[]{meterNumber, mth, totalBill, status});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == printButton) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DepositDetails().setVisible(true);
        });
    }
}
