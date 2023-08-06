package Electricity;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillDetailsFrame extends JFrame {
    private JTable table;
    private String[] columnNames = {"Meter Number", "Month", "Total Bill", "Status"};
    private DefaultTableModel tableModel;
    private HikariDataSource dataSource;

    public BillDetailsFrame(String meter) {
        super("Bill Details");
        setSize(700, 650);
        setLocation(600, 150);
        getContentPane().setBackground(Color.WHITE);

        initConnectionPool();

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a new JPanel and use GridBagLayout for the frame's content pane
        JPanel contentPane = new JPanel(new GridBagLayout());
        setContentPane(contentPane);

        // Create a new GridBagConstraints object
        GridBagConstraints gbc = new GridBagConstraints();

        // Set constraints for the scroll pane
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        contentPane.add(scrollPane, gbc);

        try {
            populateTable(meter);
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

    private void populateTable(String meter) throws SQLException {
        String query = "SELECT * FROM bill WHERE meter = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, meter);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String meterNumber = resultSet.getString("meter");
                    String month = resultSet.getString("month");
                    String totalBill = resultSet.getString("total_bill");
                    String status = resultSet.getString("status");

                    tableModel.addRow(new String[]{meterNumber, month, totalBill, status});
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BillDetailsFrame("").setVisible(true);
        });
    }
}
