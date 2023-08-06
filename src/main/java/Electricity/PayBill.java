package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PayBill extends JFrame implements ActionListener {
    private JLabel meterNoLabel, nameLabel, unitsLabel, totalBillLabel, statusLabel;
    private JLabel meterNoValueLabel, nameLabelValueLabel, unitsValueLabel, totalBillValueLabel, statusValueLabel;
    private JComboBox<String> monthComboBox;
    private JButton payButton, backButton;
    private String meter;

    public PayBill(String meter) {
        this.meter = meter;
        setTitle("Electricity Bill Payment");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Electricity Bill");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        meterNoLabel = new JLabel("Meter No:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(meterNoLabel, gbc);

        meterNoValueLabel = new JLabel(meter);
        gbc.gridx = 1;
        panel.add(meterNoValueLabel, gbc);

        nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(nameLabel, gbc);

        nameLabelValueLabel = new JLabel();
        gbc.gridx = 1;
        panel.add(nameLabelValueLabel, gbc);

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Select Month:"), gbc);
        gbc.gridx = 1;
        panel.add(monthComboBox, gbc);

        unitsLabel = new JLabel("Units:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(unitsLabel, gbc);

        unitsValueLabel = new JLabel();
        gbc.gridx = 1;
        panel.add(unitsValueLabel, gbc);

        totalBillLabel = new JLabel("Total Bill:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(totalBillLabel, gbc);

        totalBillValueLabel = new JLabel();
        gbc.gridx = 1;
        panel.add(totalBillValueLabel, gbc);

        statusLabel = new JLabel("Status:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(statusLabel, gbc);

        statusValueLabel = new JLabel();
        statusValueLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        panel.add(statusValueLabel, gbc);

        payButton = new JButton("Pay");
        payButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(payButton, gbc);

        backButton = new JButton("Back");
        backButton.addActionListener(this);
        gbc.gridx = 1;
        panel.add(backButton, gbc);

        loadBillDataForSelectedMonth((String) monthComboBox.getSelectedItem());

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    private void loadBillDataForSelectedMonth(String selectedMonth) {
        try (DbConnection c = new DbConnection();
             PreparedStatement stmt1 = c.prepareStatement("SELECT * FROM customer WHERE meter = ?");
             PreparedStatement stmt2 = c.prepareStatement("SELECT * FROM bill WHERE meter = ? AND month = ?")) {

            stmt1.setString(1, meter);
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next()) {
                nameLabelValueLabel.setText(rs1.getString("name"));
            }

            stmt2.setString(1, meter);
            stmt2.setString(2, selectedMonth);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) {
                unitsValueLabel.setText(rs2.getString("units"));
                totalBillValueLabel.setText(rs2.getString("total_bill"));
                statusValueLabel.setText(rs2.getString("status"));
            } else {
                unitsValueLabel.setText("");
                totalBillValueLabel.setText("");
                statusValueLabel.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == monthComboBox) {
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            loadBillDataForSelectedMonth(selectedMonth);
        } else if (ae.getSource() == payButton) {
            try (DbConnection c = new DbConnection();
                 PreparedStatement stmt = c.prepareStatement("UPDATE bill SET status = 'Paid' WHERE meter = ? AND month = ?")) {
                String selectedMonth = (String) monthComboBox.getSelectedItem();
                stmt.setString(1, meter);
                stmt.setString(2, selectedMonth);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.setVisible(false);
            new Paytm(meter).setVisible(true);
        } else if (ae.getSource() == backButton) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PayBill("").setVisible(true);
        });
    }
}
