package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateInformation extends JFrame implements ActionListener {
    private JTextField addressTextField, cityTextField, stateTextField, emailTextField, phoneTextField;
    private JLabel nameLabel, meterNumberLabel;
    private JButton updateButton, backButton;
    private String meter;

    public UpdateInformation(String meter) {
        this.meter = meter;

        setBounds(500, 220, 1050, 450);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("UPDATE CUSTOMER INFORMATION");
        titleLabel.setBounds(110, 0, 400, 30);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(titleLabel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(30, 70, 100, 20);
        add(nameLabel);

        this.nameLabel = new JLabel();
        this.nameLabel.setBounds(230, 70, 100, 20);
        add(this.nameLabel);

        JLabel meterNumberLabel = new JLabel("Meter Number");
        meterNumberLabel.setBounds(30, 110, 100, 20);
        add(meterNumberLabel);

        this.meterNumberLabel = new JLabel();
        this.meterNumberLabel.setBounds(230, 110, 200, 20);
        add(this.meterNumberLabel);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(30, 150, 100, 20);
        add(addressLabel);

        addressTextField = new JTextField();
        addressTextField.setBounds(230, 150, 200, 20);
        add(addressTextField);

        JLabel cityLabel = new JLabel("City");
        cityLabel.setBounds(30, 190, 100, 20);
        add(cityLabel);

        cityTextField = new JTextField();
        cityTextField.setBounds(230, 190, 200, 20);
        add(cityTextField);

        JLabel stateLabel = new JLabel("State");
        stateLabel.setBounds(30, 230, 100, 20);
        add(stateLabel);

        stateTextField = new JTextField();
        stateTextField.setBounds(230, 230, 200, 20);
        add(stateTextField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(30, 270, 100, 20);
        add(emailLabel);

        emailTextField = new JTextField();
        emailTextField.setBounds(230, 270, 200, 20);
        add(emailTextField);

        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(30, 310, 100, 20);
        add(phoneLabel);

        phoneTextField = new JTextField();
        phoneTextField.setBounds(230, 310, 200, 20);
        add(phoneTextField);

        updateButton = new JButton("Update");
        updateButton.setBackground(Color.BLACK);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBounds(70, 360, 100, 25);
        updateButton.addActionListener(this);
        add(updateButton);

        backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(230, 360, 100, 25);
        backButton.addActionListener(this);
        add(backButton);

        try (DbConnection dbConnection = new DbConnection()) {
            String query = "SELECT * FROM customer WHERE meter = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, meter);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nameLabel.setText(rs.getString("name"));
                meterNumberLabel.setText(rs.getString("meter"));
                addressTextField.setText(rs.getString("address"));
                cityTextField.setText(rs.getString("city"));
                stateTextField.setText(rs.getString("state"));
                emailTextField.setText(rs.getString("email"));
                phoneTextField.setText(rs.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(this, "Meter number not found.", "Error", JOptionPane.ERROR_MESSAGE);
                this.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details.", "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
        }

        ImageIcon updateImageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/update.jpg"));
        Image updateImage = updateImageIcon.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT);
        ImageIcon updateImageResized = new ImageIcon(updateImage);
        JLabel updateImageLabel = new JLabel(updateImageResized);
        updateImageLabel.setBounds(550, 50, 400, 300);
        add(updateImageLabel);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == updateButton) {
            String address = addressTextField.getText();
            String city = cityTextField.getText();
            String state = stateTextField.getText();
            String email = emailTextField.getText();
            String phone = phoneTextField.getText();

            try (DbConnection dbConnection = new DbConnection()) {
                String query = "UPDATE customer SET address = ?, city = ?, state = ?, email = ?, phone = ? WHERE meter = ?";
                PreparedStatement pstmt = dbConnection.prepareStatement(query);
                pstmt.setString(1, address);
                pstmt.setString(2, city);
                pstmt.setString(3, state);
                pstmt.setString(4, email);
                pstmt.setString(5, phone);
                pstmt.setString(6, meter);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Details Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update customer details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating customer details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == backButton) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateInformation("").setVisible(true);
    }
}
