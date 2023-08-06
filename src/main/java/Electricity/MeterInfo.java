package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

public class MeterInfo extends JFrame implements ActionListener {
    private JLabel meterNumberLabel, meterLocationLabel, meterTypeLabel, phaseCodeLabel, billTypeLabel, noteLabel;
    private JLabel meterNumberValueLabel, noteValueLabel;
    private JComboBox<String> meterLocationComboBox, meterTypeComboBox, phaseCodeComboBox, billTypeComboBox;
    private JButton submitButton, cancelButton;
    private JPanel panel;

    public MeterInfo(String meter) {
        setLocation(600, 200);
        setSize(700, 500);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        JLabel titleLabel = new JLabel("Meter Information");
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        meterNumberLabel = new JLabel("Meter Number");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(meterNumberLabel, gbc);

        meterNumberValueLabel = new JLabel(meter);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(meterNumberValueLabel, gbc);

        meterLocationLabel = new JLabel("Meter Location");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(meterLocationLabel, gbc);

        meterLocationComboBox = new JComboBox<>();
        meterLocationComboBox.addItem("Outside");
        meterLocationComboBox.addItem("Inside");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(meterLocationComboBox, gbc);

        meterTypeLabel = new JLabel("Meter Type");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(meterTypeLabel, gbc);

        meterTypeComboBox = new JComboBox<>();
        meterTypeComboBox.addItem("Electric Meter");
        meterTypeComboBox.addItem("Solar Meter");
        meterTypeComboBox.addItem("Smart Meter");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(meterTypeComboBox, gbc);

        phaseCodeLabel = new JLabel("Phase Code");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(phaseCodeLabel, gbc);

        phaseCodeComboBox = new JComboBox<>();
        phaseCodeComboBox.addItem("011");
        phaseCodeComboBox.addItem("022");
        phaseCodeComboBox.addItem("033");
        phaseCodeComboBox.addItem("044");
        phaseCodeComboBox.addItem("055");
        phaseCodeComboBox.addItem("066");
        phaseCodeComboBox.addItem("077");
        phaseCodeComboBox.addItem("088");
        phaseCodeComboBox.addItem("099");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(phaseCodeComboBox, gbc);

        billTypeLabel = new JLabel("Bill Type");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(billTypeLabel, gbc);

        billTypeComboBox = new JComboBox<>();
        billTypeComboBox.addItem("Normal");
        billTypeComboBox.addItem("Industrial");
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(billTypeComboBox, gbc);

        noteLabel = new JLabel("Note");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panel.add(noteLabel, gbc);

        noteValueLabel = new JLabel("By Default Bill is calculated for 30 Days only.");
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        panel.add(noteValueLabel, gbc);

        submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(submitButton, gbc);

        cancelButton = new JButton("Cancel");
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(cancelButton, gbc);

        setLayout(new BorderLayout());

        add(panel, BorderLayout.CENTER);

        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/hicon1.jpg"));
        Image image = imageIcon.getImage().getScaledInstance(150, 300, Image.SCALE_DEFAULT);
        ImageIcon scaledImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(scaledImageIcon);

        add(imageLabel, BorderLayout.WEST);
        getContentPane().setBackground(Color.WHITE);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submitButton) {
            String meterNumber = meterNumberValueLabel.getText();
            String meterLocation = (String) meterLocationComboBox.getSelectedItem();
            String meterType = (String) meterTypeComboBox.getSelectedItem();
            String phaseCode = (String) phaseCodeComboBox.getSelectedItem();
            String billType = (String) billTypeComboBox.getSelectedItem();
            String days = "30";

            String query = "INSERT INTO meter_info VALUES (?, ?, ?, ?, ?, ?)";
            try (DbConnection connection = new DbConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setString(1, meterNumber);
                statement.setString(2, meterLocation);
                statement.setString(3, meterType);
                statement.setString(4, phaseCode);
                statement.setString(5, billType);
                statement.setString(6, days);

                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Meter Info Added Successfully");
                this.setVisible(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (ae.getSource() == cancelButton) {
            this.setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MeterInfo("").setVisible(true);
        });
    }
}
