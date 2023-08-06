package Electricity;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetails extends JFrame implements ActionListener {

    private JTable table;
    private JButton printButton, prevButton, nextButton;
    private String[] columnNames = {"Customer Name", "Meter Number", "Address", "City", "State", "Email", "Phone"};
    private List<String[]> data;
    private int currentPage;
    private int pageSize = 10;
    private int totalRecords;

    public CustomerDetails() {
        super("Customer Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 650);
        setLocationRelativeTo(null);

        data = new ArrayList<>();
        currentPage = 1;

        loadDataFromDatabase();

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);

        displayDataInTable(model);

        table = new JTable(model);
        table.setAutoCreateRowSorter(true); // Enable sorting on table columns

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel headerPanel = new JPanel(new GridLayout(1, columnNames.length));
        for (String columnName : columnNames) {
            JLabel headerLabel = new JLabel(columnName);
            headerPanel.add(headerLabel);
        }

        JPanel paginationPanel = new JPanel();
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        prevButton.addActionListener(this);
        nextButton.addActionListener(this);
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);

        JPanel buttonPanel = new JPanel();
        printButton = new JButton("Print");
        printButton.addActionListener(this);
        buttonPanel.add(printButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(paginationPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadDataFromDatabase() {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS total FROM customer");
             PreparedStatement dataStatement = connection.prepareStatement("SELECT * FROM customer LIMIT ?, ?")) {

            // Get the total number of records in the database
            try (ResultSet countResultSet = countStatement.executeQuery()) {
                if (countResultSet.next()) {
                    totalRecords = countResultSet.getInt("total");
                }
            }

            // Calculate the total number of pages based on the page size
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            // Validate the current page number
            if (currentPage < 1) {
                currentPage = 1;
            } else if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            // Set the start index for the query
            int startIndex = (currentPage - 1) * pageSize;
            dataStatement.setInt(1, startIndex);
            dataStatement.setInt(2, pageSize);

            // Fetch data from the database for the current page
            try (ResultSet resultSet = dataStatement.executeQuery()) {
                while (resultSet.next()) {
                    String[] row = new String[7];
                    row[0] = resultSet.getString("name");
                    row[1] = resultSet.getString("meter");
                    row[2] = resultSet.getString("address");
                    row[3] = resultSet.getString("city");
                    row[4] = resultSet.getString("state");
                    row[5] = resultSet.getString("email");
                    row[6] = resultSet.getString("phone");
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayDataInTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == printButton) {
            try {
                table.print();
                JOptionPane.showMessageDialog(this, "Printing initiated successfully.", "Print", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while initiating printing.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == prevButton) {
            // Move to the previous page
            if (currentPage > 1) {
                currentPage--;
                data.clear();
                loadDataFromDatabase();
                ((DefaultTableModel) table.getModel()).fireTableDataChanged();
            }
        } else if (ae.getSource() == nextButton) {
            // Move to the next page
            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
            if (currentPage < totalPages) {
                currentPage++;
                data.clear();
                loadDataFromDatabase();
                ((DefaultTableModel) table.getModel()).fireTableDataChanged();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerDetails().setVisible(true);
        });
    }
}
