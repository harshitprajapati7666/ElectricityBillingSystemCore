package Electricity;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener {
    private JLabel usernameLabel, passwordLabel, loginAsLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton, signupButton;
    private JComboBox<String> loginAsComboBox;
    private JPanel panel;

    public Login() {
        super("Login Page");

        // Use GridBagLayout
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        usernameLabel = new JLabel("Username");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);

        usernameTextField = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usernameTextField, constraints);

        passwordLabel = new JLabel("Password");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(passwordField, constraints);

        loginAsLabel = new JLabel("Logging in as");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(loginAsLabel, constraints);

        loginAsComboBox = new JComboBox<>();
        loginAsComboBox.addItem("Admin");
        loginAsComboBox.addItem("Customer");
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(loginAsComboBox, constraints);

        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        signupButton = new JButton("Signup");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(signupButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(this);
        cancelButton.addActionListener(this);
        signupButton.addActionListener(this);

        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/second.jpg"));
        Image image = imageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT);
        ImageIcon imageIconScaled = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIconScaled);
        add(imageLabel, BorderLayout.WEST);

        setSize(640, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton) {
            String username = usernameTextField.getText();
            char[] password = passwordField.getPassword();
            String loginAs = (String) loginAsComboBox.getSelectedItem();

            // Validate Input Fields
            if (username.isEmpty() || password.length == 0) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (DbConnection connection = new DbConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT meter_no, user, password, salt FROM login WHERE username = ? AND user = ?")) {

                statement.setString(1, username);
                statement.setString(2, loginAs);

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        String storedSalt = rs.getString("salt");

                        if (BCrypt.checkpw(new String(password), storedPassword)) {
                            String meter = rs.getString("meter_no");
                            new Project(meter, loginAs).setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "Invalid Login", "Error", JOptionPane.ERROR_MESSAGE);
                            passwordField.setText(""); // Clear password field after an unsuccessful login attempt
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Login", "Error", JOptionPane.ERROR_MESSAGE);
                        passwordField.setText(""); // Clear password field after an unsuccessful login attempt
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (ae.getSource() == cancelButton) {
            this.dispose();
        } else if (ae.getSource() == signupButton) {
            this.dispose();
            new Signup().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
