package Electricity;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.Random;

public class NewCustomer extends JFrame implements ActionListener {
    private JLabel nameLabel, meterNoLabel, addressLabel, cityLabel, stateLabel, emailLabel, phoneLabel, meterNoValueLabel;
    private JTextField nameTextField, addressTextField, cityTextField, emailTextField, phoneTextField;
    private JComboBox<String> stateComboBox;
    private JButton nextButton, cancelButton;

    public NewCustomer() {
        setLocation(600, 200);
        setSize(700, 500);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("New Customer");
        titleLabel.setBounds(180, 10, 200, 26);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        panel.add(titleLabel);

        nameLabel = new JLabel("Customer Name");
        nameLabel.setBounds(100, 80, 100, 20);

        nameTextField = new JTextField();
        nameTextField.setBounds(240, 80, 200, 20);
        panel.add(nameLabel);
        panel.add(nameTextField);

        meterNoLabel = new JLabel("Meter No.");
        meterNoLabel.setBounds(100, 120, 100, 20);
        meterNoValueLabel = new JLabel();
        meterNoValueLabel.setBounds(240, 120, 200, 20);
        panel.add(meterNoLabel);
        panel.add(meterNoValueLabel);

        addressLabel = new JLabel("Address");
        addressLabel.setBounds(100, 160, 100, 20);
        addressTextField = new JTextField();
        addressTextField.setBounds(240, 160, 200, 20);
        panel.add(addressLabel);
        panel.add(addressTextField);

        cityLabel = new JLabel("City");
        cityLabel.setBounds(100, 200, 100, 20);
        cityTextField = new JTextField();
        cityTextField.setBounds(240, 200, 200, 20);
        panel.add(cityLabel);
        panel.add(cityTextField);

        stateLabel = new JLabel("State");
        stateLabel.setBounds(100, 240, 100, 20);
        stateComboBox = new JComboBox<>(new String[]{"", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"});
        stateComboBox.setBounds(240, 240, 200, 20);
        panel.add(stateLabel);
        panel.add(stateComboBox);

        emailLabel = new JLabel("Email");
        emailLabel.setBounds(100, 280, 100, 20);
        emailTextField = new JTextField();
        emailTextField.setBounds(240, 280, 200, 20);
        panel.add(emailLabel);
        panel.add(emailTextField);

        phoneLabel = new JLabel("Phone Number");
        phoneLabel.setBounds(100, 320, 100, 20);
        phoneTextField = new JTextField();
        phoneTextField.setBounds(240, 320, 100, 25);
        panel.add(phoneLabel);
        panel.add(phoneTextField);

        nextButton = new JButton("Next");
        nextButton.setBounds(120, 390, 100, 25);
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(250, 390, 100, 25);

        nextButton.setBackground(Color.BLACK);
        nextButton.setForeground(Color.WHITE);

        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);

        panel.add(nextButton);
        panel.add(cancelButton);
        setLayout(new BorderLayout());

        add(panel, BorderLayout.CENTER);

        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.png"));
        Image image = imageIcon.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
        ImageIcon resizedImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedImageIcon);
        add(imageLabel, BorderLayout.WEST);

        getContentPane().setBackground(Color.WHITE);

        nextButton.addActionListener(this);
        cancelButton.addActionListener(this);

        Random random = new Random();
        long meterNo = Math.abs(random.nextLong()) % 1000000;
        meterNoValueLabel.setText(String.valueOf(meterNo));
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == nextButton) {
            String name = nameTextField.getText();
            String meter = meterNoValueLabel.getText();
            String address = addressTextField.getText();
            String city = cityTextField.getText();
            String state = (String) stateComboBox.getSelectedItem();
            String email = emailTextField.getText();
            String phone = phoneTextField.getText();

            String q1 = "INSERT INTO customer (name, meter, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String q2 = "INSERT INTO login (username, password, user, meter_no, last_login) VALUES (?, ?, ?, ?, ?)";
            try (DbConnection c1 = new DbConnection();
                 PreparedStatement statement1 = c1.prepareStatement(q1);
                 PreparedStatement statement2 = c1.prepareStatement(q2)) {

                // Insert customer details
                statement1.setString(1, name);
                statement1.setString(2, meter);
                statement1.setString(3, address);
                statement1.setString(4, city);
                statement1.setString(5, state);
                statement1.setString(6, email);
                statement1.setString(7, phone);
                statement1.executeUpdate();

                // Insert login details with an empty password (to be set during registration)
                String hashedPassword = BCrypt.hashpw("", BCrypt.gensalt()); // Set an empty password, which will be updated during registration
                String userType = "Customer";
                statement2.setString(1, meter);
                statement2.setString(2, hashedPassword);
                statement2.setString(3, userType);
                statement2.setString(4, meter);
                statement2.setNull(5, java.sql.Types.TIMESTAMP);
                statement2.executeUpdate();

                JOptionPane.showMessageDialog(null, "Customer Details Added Successfully");
                this.setVisible(false);
                new MeterInfo(meter).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (ae.getSource() == cancelButton) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NewCustomer().setVisible(true);
        });
    }
}
