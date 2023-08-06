package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class About extends JFrame implements ActionListener {

    private JButton exitButton;
    private JTextArea projectDetailsArea;
    private JLabel titleLabel;

    public About() {
        setTitle("About Projects");
        setLayout(new GridBagLayout());

        titleLabel = new JLabel("About Project");
        titleLabel.setForeground(Color.RED);
        titleLabel.setFont(new Font("RALEWAY", Font.BOLD, 26));

        String projectDetails = "Electricity Billing System is a software-based application developed in Java programming language. The project aims at serving"
                + " the department of electricity by computerizing the billing system. It mainly focuses on the calculation of Units consumed during the "
                + "specified time and the money to be paid to electricity offices. This computerized system will make the overall billing system easy, "
                + "accessible, comfortable, and effective for consumers.";

        projectDetailsArea = new JTextArea(projectDetails);
        projectDetailsArea.setLineWrap(true);
        projectDetailsArea.setWrapStyleWord(true);
        projectDetailsArea.setEditable(false);
        projectDetailsArea.setFont(new Font("RALEWAY", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(projectDetailsArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        exitButton = new JButton("Exit");
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new About());
    }
}
