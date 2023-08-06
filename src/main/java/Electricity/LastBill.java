package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LastBill extends JFrame implements ActionListener {
    private JLabel titleLabel;
    private JTextArea billTextArea;
    private JButton generateButton;
    private JComboBox<String> meterComboBox;
    private JPanel panel;

    public LastBill() {
        setSize(500, 900);
        setLayout(new BorderLayout());
        panel = new JPanel();
        titleLabel = new JLabel("Generate Bill");
        meterComboBox = new JComboBox<>();

        billTextArea = new JTextArea(50, 15);
        JScrollPane scrollPane = new JScrollPane(billTextArea);
        billTextArea.setFont(new Font("Senserif", Font.ITALIC, 18));

        generateButton = new JButton("Generate Bill");
        panel.add(titleLabel);
        panel.add(meterComboBox);
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(generateButton, BorderLayout.SOUTH);

        generateButton.addActionListener(this);
        setLocation(350, 40);

        populateMeterComboBox();
    }

    private void populateMeterComboBox() {
        try (DbConnection connection = new DbConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT meter FROM emp");
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String meter = rs.getString("meter");
                meterComboBox.addItem(meter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving meter data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedMeter = (String) meterComboBox.getSelectedItem();
        if (selectedMeter != null) {
            try (DbConnection connection = new DbConnection();
                 PreparedStatement customerStatement = connection.prepareStatement("SELECT * FROM emp WHERE meter_number = ?");
                 PreparedStatement billStatement = connection.prepareStatement("SELECT * FROM bill WHERE meter_number = ? ORDER BY month DESC")) {

                customerStatement.setString(1, selectedMeter);
                billStatement.setString(1, selectedMeter);

                ResultSet rsCustomer = customerStatement.executeQuery();
                ResultSet rsBill = billStatement.executeQuery();

                billTextArea.setText(""); // Clear the previous text

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

                billTextArea.append("Details of the Last Bills\n\n\n");
                while (rsBill.next()) {
                    billTextArea.append("         " + rsBill.getString("month") + "           " + rsBill.getString("amount") + "\n");
                }

                if (!rsBill.first()) {
                    JOptionPane.showMessageDialog(this, "No billing data found for the selected meter.", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving billing data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LastBill().setVisible(true);
        });
    }
}
