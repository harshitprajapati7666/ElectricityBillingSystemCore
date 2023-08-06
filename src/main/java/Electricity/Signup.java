package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Signup extends JFrame implements ActionListener {
    private JPanel signupPanel;
    private JTextField usernameTextField, nameTextField, passwordTextField, meterNumberTextField;
    private JComboBox<String> accountTypeComboBox;
    private JButton createButton, backButton;

    public Signup() {
        setTitle("Create Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        signupPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        signupPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        signupPanel.setBackground(Color.WHITE);
        add(signupPanel);

        JLabel usernameLabel = new JLabel("Username:");
        signupPanel.add(usernameLabel);

        usernameTextField = new JTextField();
        signupPanel.add(usernameTextField);

        JLabel nameLabel = new JLabel("Name:");
        signupPanel.add(nameLabel);

        nameTextField = new JTextField();
        signupPanel.add(nameTextField);

        JLabel passwordLabel = new JLabel("Password:");
        signupPanel.add(passwordLabel);

        passwordTextField = new JPasswordField();
        signupPanel.add(passwordTextField);

        JLabel accountTypeLabel = new JLabel("Account Type:");
        signupPanel.add(accountTypeLabel);

        accountTypeComboBox = new JComboBox<>();
        accountTypeComboBox.addItem("Admin");
        accountTypeComboBox.addItem("Customer");
        signupPanel.add(accountTypeComboBox);

        JLabel meterNumberLabel = new JLabel("Meter Number:");
        meterNumberLabel.setVisible(false);
        signupPanel.add(meterNumberLabel);

        meterNumberTextField = new JTextField();
        meterNumberTextField.setVisible(false);
        signupPanel.add(meterNumberTextField);

        accountTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAccountType = (String) accountTypeComboBox.getSelectedItem();
                meterNumberLabel.setVisible(selectedAccountType.equals("Customer"));
                meterNumberTextField.setVisible(selectedAccountType.equals("Customer"));
            }
        });

        createButton = new JButton("Create Account");
        createButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        createButton.setBackground(Color.BLACK);
        createButton.setForeground(Color.WHITE);
        createButton.addActionListener(this);
        signupPanel.add(createButton);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        signupPanel.add(backButton);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == createButton) {
            String username = usernameTextField.getText();
            String name = nameTextField.getText();
            String password = passwordTextField.getText();
            String accountType = (String) accountTypeComboBox.getSelectedItem();
            String meterNumber = meterNumberTextField.getText();

            if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (accountType.equals("Customer") && meterNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the meter number for the customer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (DbConnection dbConnection = new DbConnection()) {
                String query = null;
                if (accountType.equals("Admin")) {
                    query = "INSERT INTO login VALUES('" + meterNumber + "', '" + username + "', '" + name + "', '" + password + "', '" + accountType + "')";
                } else {
                    query = "UPDATE login SET username = '" + username + "', name = '" + name + "', password = '" + password + "', account_type = '" + accountType + "' WHERE meter_no = '" + meterNumberTextField.getText() + "'";
                }
                dbConnection.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Account Created Successfully");
                this.setVisible(false);
                new Login().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error creating the account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == backButton) {
            this.setVisible(false);
            new Login().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Signup().setVisible(true));
    }
}
