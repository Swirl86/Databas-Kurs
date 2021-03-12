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

public class UserGui extends JFrame {

    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 32);
    private final Font smallFont = new Font("Times New Roman", Font.PLAIN, 24);
    private final JPanel contentPanel;
    private JTextArea infoTextArea;
    private JTextField inputField;
    private String input;
    private final String userName;
    private JButton browseBooks;
    private JButton borrowBook;
    private JButton browseMagazines;
    private JButton logout;
    private final Connection con;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public UserGui(Connection con, String userName) {
        this.con = con;
        this.pstmt = null;
        this.rs = null;

        this.userName = userName;
        this.inputField = new JTextField("Search Title");
        this.input = "-1";

        this.contentPanel = new JPanel();
        this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPanel.setBackground(new Color(62, 181, 157));
        setContentPane(this.contentPanel);
        this.contentPanel.setLayout(null);

        createWelcomeTitle();
        createTextArea();
        createInputField();
        createButtons();
    }

    private void createWelcomeTitle() {
        JLabel welcomeText = new JLabel("Welcome to the User Page");
        welcomeText.setForeground(Color.BLACK);
        welcomeText.setFont(new Font("Times New Roman", Font.BOLD, 26));
        welcomeText.setBounds(220, -15, 750, 93);
        this.contentPanel.add(welcomeText);
    }

    private void createTextArea() {
        this.infoTextArea = new JTextArea();
        this.infoTextArea.setFont(this.smallFont);
        this.infoTextArea.setWrapStyleWord(true);
        this.infoTextArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(this.infoTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(20, 60, 740, 380);
        this.contentPanel.add(scroll);
    }

    private void createInputField() {
        JLabel helpText = new JLabel("  -- Search by title: wildcard, full match or " +
                "with nothing written (everything will be shown) --");
        helpText.setForeground(Color.BLACK);
        helpText.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        helpText.setBounds(20, 450, 740, 30);
        this.contentPanel.add(helpText);

        this.inputField.setFont(this.normalFont);
        this.inputField.setBackground(Color.lightGray);
        this.inputField.setBounds(20, 480, 740, 70);
        this.contentPanel.add(this.inputField);
    }

    private void createButtons() {
        ActionListener al = e -> {
            if (e.getSource() == this.browseBooks) {
                availableBooks();
            } else if (e.getSource() == this.browseMagazines) {
                findMagazine();
            } else if (e.getSource() == this.borrowBook) {
                borrowABook();
            } else if (e.getSource() == this.logout) {
                try {
                    this.con.close(); // Close connection for new user to login
                } catch (SQLException throwables) {
                    //throwables.printStackTrace();
                }
                JComponent comp = (JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
                new Gui();
            }
        };

        this.browseBooks = new JButton("Search for book");
        this.browseBooks.setFont(this.normalFont);
        this.browseBooks.setBounds(80, 570, 300, 70);
        this.contentPanel.add(this.browseBooks);
        this.browseBooks.addActionListener(al);

        this.browseMagazines = new JButton("Search for magazine");
        this.browseMagazines.setFont(this.normalFont);
        this.browseMagazines.setBounds(390, 570, 300, 70);
        this.contentPanel.add(this.browseMagazines);
        this.browseMagazines.addActionListener(al);

        this.borrowBook = new JButton("Borrow a book");
        this.borrowBook.setFont(this.normalFont);
        this.borrowBook.setBounds(230, 660, 300, 70);
        this.contentPanel.add(this.borrowBook);
        this.borrowBook.addActionListener(al);

        this.logout = new JButton("Logout");
        this.logout.setFont(new Font("Times New Roman", Font.BOLD, 24));
        this.logout.setBackground(Color.RED);
        this.logout.setBorder(new LineBorder(Color.BLACK));
        this.logout.setBounds(640, 15, 120, 40);
        this.contentPanel.add(this.logout);
        this.logout.addActionListener(al);
    }

    private void availableBooks() {
        showAllBooks(); // Show all books in the database, easier to search if you know the titles.
        this.input = this.inputField.getText();
        if (!this.input.equals("Search Title")) {
            // Fetch all books in the database and use CASE to create a new column
            // with yes / no values depending on if the book is borrowed or not (exist in tbl_Borrowed_books)
            // Used wildcard before and after input to get as many matches as possible
            String query = "SELECT tbl_books.*, " +
                    "CASE " +
                    "WHEN tbl_Borrowed_books.Book_id is null THEN 'Yes' " +
                    "ELSE 'No' " +
                    "END AS 'Available' " +
                    "FROM tbl_books " +
                    "LEFT OUTER JOIN tbl_Borrowed_books USING (Book_id)" +
                    " WHERE title LIKE ?;";

            String output = "   Id  |           Title           |            Author           |" +
                    "      Pages      |     Classification      |        Available      \n";

            try {
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, "%" + this.input + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    output += "    " + rs.getInt(1) + "   |   " + rs.getString(2) +
                            "   |   " + rs.getString(3) + "   |      " + rs.getString(4) +
                            "      |      " + rs.getString(5) + "      |" +
                            "        " + rs.getString(6) + "    \n";
                }
            } catch (SQLException throwables) {
                //throwables.printStackTrace();
                output = "Nothing to print. . .";
            }
            this.infoTextArea.setText(output);
        }
    }

    private void findMagazine() {
        showAllMagazines(); // Show all magazines in the database, easier to search if you know the titles.
        this.input = this.inputField.getText();
        if (!this.input.equals("Search Title")) {
            // Used wildcard before and after input to get as many matches as possible
            String query = "SELECT * from tbl_magazines WHERE title LIKE ?;";

            String output = "   Id  |           Title           |            " +
                    "Release Date           |      Location\n";
            try {
                pstmt = con.prepareStatement(query);
                pstmt.setString(1, "%" + this.input + "%");
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    output += "    " + rs.getInt(1) + "      |       " + rs.getString(2) +
                            "     |    " + rs.getString(3) + "    |      " + rs.getString(4) +
                            "\n";
                }
            } catch (SQLException throwables) {
                //throwables.printStackTrace();
                output = "Nothing to print. . .";
            }
            this.infoTextArea.setText(output);
        }
    }

    /* NOTE : make sure the logged in user is a borrower in the database
     * Or set the userName to some one who exist in the database so you can borrow a book */
    private void borrowABook() {
        int bookId = -1;
        // Get user input for Book id and fetch the data
        String id = JOptionPane.showInputDialog(this.contentPanel,
                "Enter id number for the book.");

        if (id != null && !id.isEmpty() && onlyNumbers(id)) {
            bookId = Integer.parseInt(id);

            int libraryCard = -1;
            String libraryCardQuery = "SELECT Library_card_nr FROM tbl_borrowers WHERE Name = ?;";
            // Lodged in user must have a name in database
            // Future improvement: let the user login with library card number (unique key) instead of username
            try {
                pstmt = con.prepareStatement(libraryCardQuery);
                pstmt.setString(1, this.userName);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    libraryCard = rs.getInt(1);
                }
                // If libraryCardQuery found a match libraryCard will not have the value -1 and
                // if the user have not borrowed that book already a new query is made to add values in tbl_Borrowed_books
                if (libraryCard != -1 && !relationExist(libraryCard, bookId)) {
                    // CONSTRAINT in database to only allow unique Book_id in tbl_Borrowed_books
                    // so no more then one of that book can be borrowed at a time.
                    // Future improvement: implementation a number of how many copy's of a book can be borrowed
                    // Note! Not using setInt because already checked that bookId and libraryCard is int values
                    String query = "INSERT INTO tbl_Borrowed_books(Book_id, Library_card_nr) " +
                            "SELECT " + bookId + ", " + libraryCard +
                            " WHERE NOT EXISTS (" +
                            "SELECT Book_id, Library_card_nr FROM tbl_Borrowed_books WHERE Book_id = " + bookId + ");";
                    // The innermost query: WHERE NOT EXISTS-condition detects if there already exists a row with the data
                    // The intermediate query: represents the values to be inserted
                    // The outer query: inserts the data, if any is returned by the intermediate query.
                    pstmt = con.prepareStatement(query);
                    pstmt.executeUpdate();
                    // If the book is not added, that book is already borrowed (unique Book_id in tbl_Borrowed_books)
                    checkIfRelationGotAdded(libraryCard, bookId);
                } else if (relationExist(libraryCard, bookId)) {
                    // There is already an relation in tbl_Borrowed_books, the book is already borrowed by the user
                    int reply = JOptionPane.showConfirmDialog(null,
                            "You have already borrowed that book.\n" +
                                    "Do you want to return it?", "Book option", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        returnBook(libraryCard, bookId);
                    }
                } else {
                    // If you login with a valid user, e.g root, and you don't have a borrower named root (in database)
                    // you will still be able to login as a user browse books and magazines but not borrow a book.
                    JOptionPane.showMessageDialog(this.borrowBook, "You don't have a library card number!\n" +
                                    "Make sure your information exist in our database.",
                            "NOTE!", JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException throwables) {
                //  throwables.printStackTrace();
                JOptionPane.showMessageDialog(this.borrowBook, "Check book id and if the book is available!",
                        "NOTE!", JOptionPane.WARNING_MESSAGE);
            }
        } else if (!onlyNumbers(id) && id != null) {
            JOptionPane.showMessageDialog(this.borrowBook, "Must be a number, try again!",
                    "NOTE!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void returnBook(int libraryCard, int bookId) {
        // libraryCard and bookId have already been checked in borrowABook() so no need for same check again
        // if successful query there should be no relation in tbl_Borrowed_books after delete.
        // Note! Not using setInt because already checked that bookId and libraryCard is int values in borrowABook()
        String query = "DELETE FROM tbl_Borrowed_books WHERE Book_id = " + bookId +
                " AND Library_card_nr =" + libraryCard + ";";
        try {
            pstmt = con.prepareStatement(query);
            pstmt.executeUpdate();
            if (!relationExist(libraryCard, bookId)) {
                JOptionPane.showMessageDialog(this.borrowBook, "You have successfully returned the book.",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
                showAllBooks();
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            JOptionPane.showMessageDialog(this.borrowBook, "Something went wrong :(",
                    "NOTE!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean relationExist(int libraryCard, int bookId) {
        // Note! Not using setInt because already checked that bookId and libraryCard is int values in borrowABook()
        boolean exist = false;
        String query = "SELECT * FROM tbl_Borrowed_books WHERE Book_id = '" + bookId + "' AND " +
                "Library_card_nr = '" + libraryCard + "';";
        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // If value is found that book is already borrowed by the user
                exist = true;
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
        }
        return exist;
    }

    private void checkIfRelationGotAdded(int libraryCard, int bookId) {
        if (relationExist(libraryCard, bookId)) {
            JOptionPane.showMessageDialog(this.borrowBook, "You have successfully borrowed the book.",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
            showAllBooks();
        } else {
            JOptionPane.showMessageDialog(this.borrowBook, "That book is already borrowed, choose another book.",
                    "NOTE!", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAllBooks() {
        // See comment in method availableBooks() for CASE
        String query = "SELECT DISTINCT tbl_books.*, " +
                "CASE " +
                "WHEN tbl_Borrowed_books.Book_id is null THEN 'Yes' " +
                "ELSE 'No' " +
                "END AS 'Available' " +
                "FROM tbl_books " +
                "LEFT OUTER JOIN tbl_Borrowed_books USING (Book_id);";

        String output = "   Id  |           Title           |            Author           |" +
                "       Pages      |     Classification      |        Available       \n";

        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                output += "    " + rs.getInt(1) + "   |   " + rs.getString(2) +
                        "   |         " + rs.getString(3) + "     |       " + rs.getString(4) +
                        "       |       " + rs.getString(5) + "         |" +
                        "         " + rs.getString(6) + "       \n";
            }
        } catch (SQLException throwables) {
            //throwables.printStackTrace();
            output = "No information . . .";
        }
        this.infoTextArea.setText(output);
    }

    private void showAllMagazines() {
        String query = "SELECT * FROM tbl_magazines;";

        String output = "   Id  |           Title               |" +
                "            Release Date           |      Location     \n";

        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                output += "    " + rs.getInt(1) + "   |     " + rs.getString(2) +
                        "       |       " + rs.getString(3) + "     |" +
                        "      " + rs.getString(4) + "      \n";
            }
        } catch (SQLException throwables) {
            // throwables.printStackTrace();
            output = "No information . . .";
        }
        this.infoTextArea.setText(output);
    }

    private boolean onlyNumbers(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("[0-9]+");
            Matcher matcher = p.matcher(str);
            return matcher.matches();
        }
        return false;
    }

    public JPanel getPanel() {
        return this.contentPanel;
    }
}
