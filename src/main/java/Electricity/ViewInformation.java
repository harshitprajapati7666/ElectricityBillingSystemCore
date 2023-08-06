package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewInformation extends JFrame implements ActionListener {
    private JButton backButton;

    public ViewInformation(String meter) {
        setBounds(600, 250, 850, 650);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel titleLabel = new JLabel("VIEW CUSTOMER INFORMATION");
        titleLabel.setBounds(250, 0, 500, 40);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(titleLabel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(70, 80, 100, 20);
        add(nameLabel);

        JLabel nameValueLabel = new JLabel();
        nameValueLabel.setBounds(250, 80, 150, 20);
        add(nameValueLabel);

        JLabel meterNumberLabel = new JLabel("Meter Number");
        meterNumberLabel.setBounds(70, 140, 100, 20);
        add(meterNumberLabel);

        JLabel meterNumberValueLabel = new JLabel();
        meterNumberValueLabel.setBounds(250, 140, 150, 20);
        add(meterNumberValueLabel);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(70, 200, 100, 20);
        add(addressLabel);

        JLabel addressValueLabel = new JLabel();
        addressValueLabel.setBounds(250, 200, 200, 20);
        add(addressValueLabel);

        JLabel cityLabel = new JLabel("City");
        cityLabel.setBounds(70, 260, 100, 20);
        add(cityLabel);

        JLabel cityValueLabel = new JLabel();
        cityValueLabel.setBounds(250, 260, 150, 20);
        add(cityValueLabel);

        JLabel stateLabel = new JLabel("State");
        stateLabel.setBounds(500, 80, 100, 20);
        add(stateLabel);

        JLabel stateValueLabel = new JLabel();
        stateValueLabel.setBounds(650, 80, 100, 20);
        add(stateValueLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(500, 140, 100, 20);
        add(emailLabel);

        JLabel emailValueLabel = new JLabel();
        emailValueLabel.setBounds(650, 140, 200, 20);
        add(emailValueLabel);

        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(500, 200, 100, 20);
        add(phoneLabel);

        JLabel phoneValueLabel = new JLabel();
        phoneValueLabel.setBounds(650, 200, 150, 20);
        add(phoneValueLabel);

        try (DbConnection dbConnection = new DbConnection()) {
            String query = "SELECT * FROM customer WHERE meter = ?";
            PreparedStatement pstmt = dbConnection.prepareStatement(query);
            pstmt.setString(1, meter);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nameValueLabel.setText(rs.getString("name"));
                meterNumberValueLabel.setText(rs.getString("meter"));
                addressValueLabel.setText(rs.getString("address"));
                cityValueLabel.setText(rs.getString("city"));
                stateValueLabel.setText(rs.getString("state"));
                emailValueLabel.setText(rs.getString("email"));
                phoneValueLabel.setText(rs.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(this, "Meter number not found.", "Error", JOptionPane.ERROR_MESSAGE);
                this.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details.", "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
        }

        backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setBounds(350, 340, 100, 25);
        backButton.addActionListener(this);
        add(backButton);

        ImageIcon viewCustomerIcon = new ImageIcon(ClassLoader.getSystemResource("icon/viewcustomer.jpg"));
        Image viewCustomerImage = viewCustomerIcon.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
        ImageIcon viewCustomerResizedIcon = new ImageIcon(viewCustomerImage);
        JLabel viewCustomerImageLabel = new JLabel(viewCustomerResizedIcon);
        viewCustomerImageLabel.setBounds(20, 350, 600, 300);
        add(viewCustomerImageLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        new ViewInformation("").setVisible(true);
    }
}
