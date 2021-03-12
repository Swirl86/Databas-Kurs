import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminGui extends JFrame {

    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 32);
    private final Font smallFont = new Font("Times New Roman", Font.PLAIN, 24);
    private final JPanel contentPanel;
    private JTextArea infoTextArea;
    private JButton borrowedBooks;
    private JButton accessEmployees;
    private JButton updateEmployeeData;
    private JButton logout;
    private final Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public AdminGui(Connection con) {
        this.con = con;
        this.pstmt = null;
        this.rs = null;

        this.contentPanel = new JPanel();
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPanel.setBackground(new Color(62, 181, 157));
        setContentPane(this.contentPanel);
        this.contentPanel.setLayout(null);

        createWelcomeTitle();
        createTextArea();
        createButtons();
    }

    private void createWelcomeTitle() {
        JLabel welcomeText = new JLabel("Welcome to the Admin Page");
        welcomeText.setForeground(Color.BLACK);
        welcomeText.setFont(new Font("Times New Roman", Font.BOLD, 26));
        welcomeText.setBounds(220, -10, 750, 93);
        this.contentPanel.add(welcomeText);
    }

    private void createTextArea() {
        this.infoTextArea = new JTextArea();
        this.infoTextArea.setFont(this.smallFont);
        this.infoTextArea.setWrapStyleWord(true);
        this.infoTextArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(this.infoTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(20, 80, 740, 390);
        this.contentPanel.add(scroll);
    }

    private void createButtons() {
        ActionListener al = e -> {
            if (e.getSource() == this.borrowedBooks) {
                borrowedBooks();
            } else if (e.getSource() == this.accessEmployees) {
                employees();
            } else if (e.getSource() == this.updateEmployeeData) {
                updateEmployeeInfo();
            } else if (e.getSource() == this.logout) {
                try {
                    this.con.close();
                } catch (SQLException throwables) {
                    // throwables.printStackTrace();
                }
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
                new Gui();
            }
        };

        this.borrowedBooks = new JButton("Borrowed books");
        this.borrowedBooks.setFont(this.normalFont);
        this.borrowedBooks.setBounds(80, 500, 300, 70);
        this.contentPanel.add(this.borrowedBooks);
        this.borrowedBooks.addActionListener(al);

        this.accessEmployees = new JButton("Employees info");
        this.accessEmployees.setFont(this.normalFont);
        this.accessEmployees.setBounds(390, 500, 300, 70);
        this.contentPanel.add(this.accessEmployees);
        this.accessEmployees.addActionListener(al);

        this.updateEmployeeData = new JButton("Update employee");
        this.updateEmployeeData.setFont(this.normalFont);
        this.updateEmployeeData.setBounds(230, 600, 300, 70);
        this.contentPanel.add(this.updateEmployeeData);
        this.updateEmployeeData.addActionListener(al);

        this.logout = new JButton("Logout");
        this.logout.setFont(new Font("Times New Roman", Font.BOLD, 24));
        this.logout.setBackground(Color.RED);
        this.logout.setBorder(new LineBorder(Color.BLACK));
        this.logout.setBounds(640, 20, 120, 40);
        this.contentPanel.add(this.logout);
        this.logout.addActionListener(al);
    }

    public void borrowedBooks() {
        // Fetch relevant info with a relation to tbl_Borrowed_books
        String query = "SELECT tbl_borrowers.Library_card_nr, tbl_borrowers.Name, tbl_books.Title " +
                "FROM tbl_borrowers, tbl_books, tbl_Borrowed_books " +
                "WHERE tbl_Borrowed_books.Library_card_nr = tbl_borrowers.Library_card_nr " +
                "AND tbl_books.Book_id = tbl_Borrowed_books.Book_id;";

        String output = "  Library Card     |       Borrowers       |           Title      \n";

        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                output += "         " + rs.getInt(1) + "        |       " + rs.getString(2) +
                        "       |       " + rs.getString(3) + "     \n";
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            output = "Nothing to print. . .";
        }
        this.infoTextArea.setText(output);
    }

    public void employees() {
        // Fetch all information in tbl_employees
        String query = "SELECT * FROM tbl_employees;";
        String output = "   Id  |   Name  |      Address      |     Phone1  " +
                "|   Phone2  |   Phone3  |   Salary  |   Vacation days   \n";

        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                output += "    " + rs.getInt(1) + "   |   " + rs.getString(2) +
                        "   |   " + rs.getString(3) + "   |   " + rs.getString(4) +
                        "   |   " + rs.getString(5) + "   |   " + rs.getString(6) +
                        "   |   " + rs.getString(7) +
                        "   |     " + rs.getString(8) + "\n";
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            output = "Nothing to print. . .";
        }
        this.infoTextArea.setText(output);
    }

    private void updateEmployeeInfo() {
        int employeeId = -1;
        // Get user input for Employee id and fetch the data if valid input and the employee exist
        String id = JOptionPane.showInputDialog(this.contentPanel, "Enter id number for Employee");

        if (id != null && onlyNumbers(id) && checkIfEmployeeExist(id)) { // If cancel button is clicked id will be null
            employeeId = Integer.parseInt(id);
            String query = "SELECT * from tbl_employees WHERE Employee_id = " + employeeId + ";";

            // Create fields for input
            JTextField field1 = new JTextField(id);
            field1.setEditable(false); // Can not update the id
            JTextField field2 = new JTextField();
            JTextField field3 = new JTextField();
            JTextField field4 = new JTextField();
            JTextField field5 = new JTextField();
            JTextField field6 = new JTextField();
            JTextField field7 = new JTextField();
            JTextField field8 = new JTextField();
            try {
                pstmt = con.prepareStatement(query);
                rs = pstmt.executeQuery();
                // Add existing values in fields (less to write)
                while (rs.next()) {
                    field2.setText(rs.getString(2));
                    field3.setText(rs.getString(3));
                    field4.setText(rs.getString(4));
                    field5.setText(rs.getString(5));
                    field6.setText(rs.getString(6));
                    field7.setText(rs.getString(7));
                    field8.setText(rs.getString(8));
                }
            } catch (SQLException throwables) {
                // throwables.printStackTrace();
            }
            // Prepare values for option popup window
            Object[] message = {
                    "Employee Id:", field1,
                    "Name:", field2,
                    "Address:", field3,
                    "Phone number 1:", field4,
                    "Phone number 2:", field5,
                    "Phone number 3:", field6,
                    "Salary:", field7,
                    "Vacation Days:", field8,
            };
            // Create popup window with the employees already existing values so it can be easy edited.
            int option = JOptionPane.showConfirmDialog(this.updateEmployeeData, message, "Enter Employee " +
                    "values for update", JOptionPane.OK_CANCEL_OPTION);

            // Making sure all values are in valid form before running query by checking with regex from testValues()
            boolean validValues = testValues(field2.getText(), field4.getText(), field5.getText(), field6.getText(),
                    field7.getText(), field8.getText());

            if (option == JOptionPane.OK_OPTION && validValues) {
                // Create update query with new values
                int value1 = Integer.parseInt(field1.getText());
                query = "UPDATE tbl_employees " +
                        "SET Name = '" + field2.getText() + "', Adress = '" + field3.getText() +
                        "', Phone_number_1 = '" + field4.getText()
                        + "', Phone_number_2 = '" + field5.getText() + "', Phone_number_3 = '" + field6.getText()
                        + "', Salary = '" + field7.getText() + "', Vacation_days = '" + field8.getText() +
                        "' WHERE Employee_id  = " + value1 + ";";

                tryRunUpdateQuery(query);
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                // Do nothing but avoids warning message in else statement when cancel button is clicked
            } else {
                JOptionPane.showMessageDialog(this.updateEmployeeData, "Invalid input, try again!",
                        "NOTE!", JOptionPane.WARNING_MESSAGE);
                // Future improvement: add more specific message on valid forms
            }
        } else if (id == null) {
            // Do nothing but avoids warning message in else statement when cancel button is clicked
        } else {
            JOptionPane.showMessageDialog(this.updateEmployeeData, "Must be a Employee number, try again!",
                    "NOTE!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean checkIfEmployeeExist(String id) {
        boolean exist = false;
        String query = "SELECT * from tbl_employees WHERE Employee_id = ?;";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(id));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                exist = true;
            }
        } catch (SQLException throwables) {
            // throwables.printStackTrace();
        }
        return exist;
    }

    private boolean testValues(String name, String phnr1, String phnr2, String phnr3, String salary, String vacad) {
        boolean valid = false;
        if (validString(name) && validPhoneNumber(phnr1) && validPhoneNumber(phnr2) && validPhoneNumber(phnr3)
                && onlyNumbers(salary) && onlyNumbers(vacad)) {
            valid = true;
        }
        return valid;
    }

    private void tryRunUpdateQuery(String query) {
        try {
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this.updateEmployeeData, "You have successfully updated " +
                    "the information.", "Success!", JOptionPane.INFORMATION_MESSAGE);
            employees();

        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            JOptionPane.showMessageDialog(this.updateEmployeeData, "Something went wrong :(\n" +
                            "Could not update the information!",
                    "NOTE!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean onlyNumbers(String str) {
        Pattern p = Pattern.compile("[0-9]+");
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }

    public static boolean validPhoneNumber(String input) {
        // Some valid forms: 3333      04375555555       0437-5555555       0437/5555555       04 375555555
        Pattern p = Pattern.compile("([\\s]*)|([0-9]{5,12})|" +
                "([0-9]{2,4}[0-9\\s/\\-]?[0-9]{2,9})");
        Matcher matcher = p.matcher(input);
        return matcher.matches();
    }

    public static boolean validString(String input) {
        Pattern p = Pattern.compile("[A-z\\s-`´]{0,50}");
        Matcher matcher = p.matcher(input);

        while (matcher.find()) {
            if (matcher.group().length() != 0) {
                return true;
            }
        }
        return false;
    }

    public JPanel getPanel() {
        return this.contentPanel;
    }
}
