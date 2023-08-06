package Electricity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Project extends JFrame implements ActionListener {
    private String meter;

    public Project(String meter, String person) {
        super("Electricity Billing System");
        this.meter = meter;
        setSize(1920, 1080);

        // Adding Background Image
        ImageIcon backgroundImage = new ImageIcon(ClassLoader.getSystemResource("icon/elect1.jpg"));
        Image scaledBackgroundImage = backgroundImage.getImage().getScaledInstance(1900, 950, Image.SCALE_DEFAULT);
        ImageIcon scaledIcon = new ImageIcon(scaledBackgroundImage);
        JLabel backgroundLabel = new JLabel(scaledIcon);
        add(backgroundLabel);

        // First Column
        JMenuBar menuBar = new JMenuBar();
        JMenu masterMenu = new JMenu("Master");
        JMenuItem newCustomerItem = new JMenuItem("New Customer");
        JMenuItem customerDetailsItem = new JMenuItem("Customer Details");
        JMenuItem depositDetailsItem = new JMenuItem("Deposit Details");
        JMenuItem calculateBillItem = new JMenuItem("Calculate Bill");
        masterMenu.setForeground(Color.BLUE);

        // Customer Details
        newCustomerItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon newCustomerIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon1.png"));
        Image newCustomerImage = newCustomerIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        newCustomerItem.setIcon(new ImageIcon(newCustomerImage));
        newCustomerItem.setMnemonic('D');
        newCustomerItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        newCustomerItem.setBackground(Color.WHITE);

        // Meter Details
        customerDetailsItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon customerDetailsIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon2.png"));
        Image customerDetailsImage = customerDetailsIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        customerDetailsItem.setIcon(new ImageIcon(customerDetailsImage));
        customerDetailsItem.setMnemonic('M');
        customerDetailsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        customerDetailsItem.setBackground(Color.WHITE);

        // Deposit Details
        depositDetailsItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon depositDetailsIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon3.png"));
        Image depositDetailsImage = depositDetailsIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        depositDetailsItem.setIcon(new ImageIcon(depositDetailsImage));
        depositDetailsItem.setMnemonic('N');
        depositDetailsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        depositDetailsItem.setBackground(Color.WHITE);

        calculateBillItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon calculateBillIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon5.png"));
        Image calculateBillImage = calculateBillIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        calculateBillItem.setIcon(new ImageIcon(calculateBillImage));
        calculateBillItem.setMnemonic('B');
        calculateBillItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
        calculateBillItem.setBackground(Color.WHITE);

        newCustomerItem.addActionListener(this);
        customerDetailsItem.addActionListener(this);
        depositDetailsItem.addActionListener(this);
        calculateBillItem.addActionListener(this);

        // Second Column
        JMenu infoMenu = new JMenu("Information");
        JMenuItem updateInfoItem = new JMenuItem("Update Information");
        JMenuItem viewInfoItem = new JMenuItem("View Information");

        infoMenu.setForeground(Color.RED);

        // Update Information
        updateInfoItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon updateInfoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon4.png"));
        Image updateInfoImage = updateInfoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        updateInfoItem.setIcon(new ImageIcon(updateInfoImage));
        updateInfoItem.setMnemonic('P');
        updateInfoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        updateInfoItem.setBackground(Color.WHITE);

        // View Information
        viewInfoItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon viewInfoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon6.png"));
        Image viewInfoImage = viewInfoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        viewInfoItem.setIcon(new ImageIcon(viewInfoImage));
        viewInfoItem.setMnemonic('L');
        viewInfoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        viewInfoItem.setBackground(Color.WHITE);

        updateInfoItem.addActionListener(this);
        viewInfoItem.addActionListener(this);

        // Second Column
        JMenu userMenu = new JMenu("User");
        JMenuItem payBillItem = new JMenuItem("Pay Bill");
        JMenuItem billDetailsItem = new JMenuItem("Bill Details");
        userMenu.setForeground(Color.RED);

        // Pay Bill
        payBillItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon payBillIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon4.png"));
        Image payBillImage = payBillIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        payBillItem.setIcon(new ImageIcon(payBillImage));
        payBillItem.setMnemonic('P');
        payBillItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        payBillItem.setBackground(Color.WHITE);

        // Bill Details
        billDetailsItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon billDetailsIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon6.png"));
        Image billDetailsImage = billDetailsIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        billDetailsItem.setIcon(new ImageIcon(billDetailsImage));
        billDetailsItem.setMnemonic('L');
        billDetailsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        billDetailsItem.setBackground(Color.WHITE);

        payBillItem.addActionListener(this);
        billDetailsItem.addActionListener(this);

        // Third Column
        JMenu reportMenu = new JMenu("Report");
        JMenuItem generateBillItem = new JMenuItem("Generate Bill");
        reportMenu.setForeground(Color.BLUE);

        // Generate Bill
        generateBillItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon generateBillIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon7.png"));
        Image generateBillImage = generateBillIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        generateBillItem.setIcon(new ImageIcon(generateBillImage));
        generateBillItem.setMnemonic('R');
        generateBillItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        generateBillItem.setBackground(Color.WHITE);

        generateBillItem.addActionListener(this);

        // Fourth Column
        JMenu utilityMenu = new JMenu("Utility");
        JMenuItem notepadItem = new JMenuItem("Notepad");
        JMenuItem calculatorItem = new JMenuItem("Calculator");
        JMenuItem webBrowserItem = new JMenuItem("Web Browser");
        utilityMenu.setForeground(Color.RED);

        // Notepad
        notepadItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon notepadIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon12.png"));
        Image notepadImage = notepadIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        notepadItem.setIcon(new ImageIcon(notepadImage));
        notepadItem.setMnemonic('C');
        notepadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        notepadItem.setBackground(Color.WHITE);

        // Calculator
        calculatorItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon calculatorIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon9.png"));
        Image calculatorImage = calculatorIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        calculatorItem.setIcon(new ImageIcon(calculatorImage));
        calculatorItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        calculatorItem.setBackground(Color.WHITE);

        // Web Browser
        webBrowserItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon webBrowserIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon10.png"));
        Image webBrowserImage = webBrowserIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        webBrowserItem.setIcon(new ImageIcon(webBrowserImage));
        webBrowserItem.setMnemonic('W');
        webBrowserItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        webBrowserItem.setBackground(Color.WHITE);

        notepadItem.addActionListener(this);
        calculatorItem.addActionListener(this);
        webBrowserItem.addActionListener(this);

        // Fifth Column
        JMenu exitMenu = new JMenu("Logout");
        JMenuItem logoutItem = new JMenuItem("Logout");
        exitMenu.setForeground(Color.BLUE);

        // Logout
        logoutItem.setFont(new Font("monospaced", Font.PLAIN, 12));
        ImageIcon logoutIcon = new ImageIcon(ClassLoader.getSystemResource("icon/icon11.png"));
        Image logoutImage = logoutIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        logoutItem.setIcon(new ImageIcon(logoutImage));
        logoutItem.setMnemonic('E');
        logoutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        logoutItem.setBackground(Color.WHITE);

        logoutItem.addActionListener(this);

        masterMenu.add(newCustomerItem);
        masterMenu.add(customerDetailsItem);
        masterMenu.add(depositDetailsItem);
        masterMenu.add(calculateBillItem);

        infoMenu.add(updateInfoItem);
        infoMenu.add(viewInfoItem);

        userMenu.add(payBillItem);
        userMenu.add(billDetailsItem);

        reportMenu.add(generateBillItem);

        utilityMenu.add(notepadItem);
        utilityMenu.add(calculatorItem);
        utilityMenu.add(webBrowserItem);

        exitMenu.add(logoutItem);

        if (person.equals("Admin")) {
            menuBar.add(masterMenu);
        } else {
            menuBar.add(infoMenu);
            menuBar.add(userMenu);
            menuBar.add(reportMenu);
        }
        menuBar.add(utilityMenu);
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);

        setFont(new Font("Senserif", Font.BOLD, 16));
        setLayout(new FlowLayout());
        setVisible(false);
    }

    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        if (actionCommand.equals("Customer Details")) {
            new CustomerDetails().setVisible(true);
        } else if (actionCommand.equals("New Customer")) {
            new NewCustomer().setVisible(true);
        } else if (actionCommand.equals("Calculate Bill")) {
            new CalculateBill().setVisible(true);
        } else if (actionCommand.equals("Pay Bill")) {
            new PayBill(meter).setVisible(true);
        } else if (actionCommand.equals("Notepad")) {
            try {
                Runtime.getRuntime().exec("notepad.exe");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionCommand.equals("Calculator")) {
            try {
                Runtime.getRuntime().exec("calc.exe");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionCommand.equals("Web Browser")) {
            try {
                Runtime.getRuntime().exec("C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionCommand.equals("Logout")) {
            this.setVisible(false);
            new Login().setVisible(true);
        } else if (actionCommand.equals("Generate Bill")) {
            new GenerateBill(meter).setVisible(true);
        } else if (actionCommand.equals("Deposit Details")) {
            new DepositDetails().setVisible(true);
        } else if (actionCommand.equals("View Information")) {
            new ViewInformation(meter).setVisible(true);
        } else if (actionCommand.equals("Update Information")) {
            new UpdateInformation(meter).setVisible(true);
        } else if (actionCommand.equals("Bill Details")) {
            new BillDetailsFrame(meter).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project("", "").setVisible(true));
    }
}
