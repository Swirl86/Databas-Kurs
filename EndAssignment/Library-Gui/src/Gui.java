import javax.swing.*;
import java.sql.Connection;

public class Gui extends JFrame {

    /* "Main Gui" set size, CloseOperation and add correct panel
     depending on user privileges after login. */

    public Gui() {
        this.setSize(800, 800);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(false);
        new LoginGui(this);
    }

    public void setVisibleDependingOnAccess(boolean isAdmin, Connection connection, String userName) {
        if (isAdmin) {
            AdminGui agui = new AdminGui(connection);
            this.add(agui.getPanel());
        } else {
            UserGui ugui = new UserGui(connection, userName);
            this.add(ugui.getPanel());
        }
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}