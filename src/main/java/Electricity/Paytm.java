package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Paytm extends JFrame implements ActionListener {
    private String meter;
    private JButton backButton;
    private JEditorPane editorPane;
    private JPanel loadingPanel;

    public Paytm(String meter) {
        this.meter = meter;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadingPanel = createLoadingPanel();
        add(loadingPanel, BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.NORTH);

        loadPaytmWebsite();

        setSize(800, 600);
        setLocation(250, 120);
        setVisible(true);
    }

    private JPanel createLoadingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel loadingLabel = new JLabel("Loading, please wait...");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        panel.add(loadingLabel, BorderLayout.CENTER);
        return panel;
    }

    private void loadPaytmWebsite() {
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulating a loading delay (You can replace this with actual page loading)

                editorPane = new JEditorPane();
                editorPane.setEditable(false);
                editorPane.setContentType("text/html");

                JScrollPane scrollPane = new JScrollPane(editorPane);
                remove(loadingPanel);
                add(scrollPane, BorderLayout.CENTER);
                revalidate();

                editorPane.setPage("https://paytm.com/electricity-bill-payment");
            } catch (Exception e) {
                editorPane.setText("<html><center><h2>Failed to load the Paytm website.</h2><br>"
                        + "Please check your internet connection and try again later.</center></html>");
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);
        new PayBill(meter).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Paytm("").setVisible(true);
        });
    }
}
