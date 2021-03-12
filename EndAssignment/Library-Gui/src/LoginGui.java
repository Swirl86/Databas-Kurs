import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginGui extends JFrame {

    private static final long serialVersionUID = 1L;
    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 32);
    private final Font boldFont = new Font("Times New Roman", Font.BOLD, 32);
    private JPanel mainPanel;
    private JTextField loginNameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private String userName;
    private String password;
    private boolean isAdmin;
    private Gui gui;
    private Connection con;

    public LoginGui(Gui gui) {
        this.con = null;
        this.gui = gui;
        this.userName = "invalid";
        this.password = "invalid";
        this.isAdmin = false;

        createMainPanel();
        createContent();
        handleLogin();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void createMainPanel() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(250, 100, 800, 800);
        setResizable(false);
        this.mainPanel = new JPanel();
        this.mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.mainPanel.setBackground(new Color(62, 181, 157));
        setContentPane(this.mainPanel);
        this.mainPanel.setLayout(null);
    }

    private void createContent() {
        JLabel welcomeText = new JLabel("Welcome to the library login page");
        welcomeText.setForeground(Color.BLACK);
        welcomeText.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        welcomeText.setBounds(80, 45, 750, 93);
        this.mainPanel.add(welcomeText);

        this.loginNameField = new JTextField("User name");
        this.loginNameField.setFont(this.normalFont);
        this.loginNameField.setBounds(330, 200, 285, 70);
        this.mainPanel.add(this.loginNameField);

        this.passwordField = new JPasswordField("password");
        this.passwordField.setFont(this.normalFont);
        this.passwordField.setBounds(330, 320, 285, 70);
        this.mainPanel.add(this.passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(this.boldFont);
        lblUsername.setBounds(120, 200, 195, 55);
        this.mainPanel.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(this.boldFont);
        lblPassword.setBounds(120, 320, 195, 55);
        this.mainPanel.add(lblPassword);

        this.cancelButton = new JButton("Cancel");
        this.cancelButton.setFont(this.boldFont);
        this.cancelButton.setBounds(170, 440, 165, 75);

        this.loginButton = new JButton("Login");
        this.loginButton.setFont(this.boldFont);
        this.loginButton.setBounds(400, 440, 165, 75);
    }

    /* Two ways to handle / run login.
     *   - Use created user (in terminal) with set privileges (The commented code)
     *   - Use tbl_users (The uncommented code)
     * */
    private void handleLogin() {

        ActionListener cancelAl = e -> {
            // Simple stop program when clicked cancel button
            System.exit(0);
        };

        ActionListener loginAl = e -> {
            this.userName = this.loginNameField.getText();
            this.password = this.passwordField.getText();

            try {
                /* -- To use created terminal users with privileges use this code -- */
                // this.con = DriverManager.getConnection("jdbc:mysql://localhost/library", this.userName, this.password);
                // PreparedStatement pstmt = con.prepareStatement("SHOW GRANTS FOR ?@'localhost';");
                // pstmt.setString(1, this.userName);
                /* ------------------------------------------------------------------- */

                /* -- To use tbl_users in database use this code. -- */
                this.con = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "root");
                PreparedStatement pstmt = con.prepareStatement("SELECT Is_admin FROM tbl_users WHERE Login_name = ?;");
                pstmt.setString(1, this.userName);
                /* ------------------------------------------------------------------- */

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {

                    /*  -- To use created terminal users with privileges use this code -- */
                    //if (rs.getString(1).contains("tbl_employees")) {
                    // An admin have privileges to access the tbl_employees, a user don't.
                    // There is no librarian atm, only admin and user. An admin can do everything a librarian can and more.
                    //     this.isAdmin = true;
                    // }
                    /* ----------------------------------------------------------------- */


                    /* -- To use tbl_users in database use this code. -- */
                    this.isAdmin = rs.getBoolean(1);
                    /* ----------------------------------------------------------------- */
                }
                dispose();
                // Different gui content will added depending on access level / privileges the account have
                this.gui.setVisibleDependingOnAccess(this.isAdmin, this.con, this.userName);
                this.password = "";

                JOptionPane.showMessageDialog(this.loginButton, "You have successfully logged in",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException sqlException) {
                //  sqlException.printStackTrace();
                JOptionPane.showMessageDialog(this.loginButton, "Wrong Username &/ Password", "Warning!",
                        JOptionPane.WARNING_MESSAGE);
            }
        };

        this.cancelButton.addActionListener(cancelAl);
        this.mainPanel.add(this.cancelButton);

        this.loginButton.addActionListener(loginAl);
        this.mainPanel.add(this.loginButton);
    }
}
