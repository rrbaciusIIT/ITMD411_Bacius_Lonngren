/*
This class displays the frame for users to sign into the ticketing system.

Users sign in using a username and password that is checked via the "rbac_users" table and makes sure an admin flag is ON.

If a user attempts to sign in more than 5 times, it locks them out of the system.
 */

import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

//create class to display initial login page
public class userLogin extends JFrame {
    private static final long serialVersionUID = 42L;
    Dao databaseConnection = new Dao();
    public userLogin() {
        super("The Bagel Support System");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseConnection = new Dao();
        setSize(600, 500);
        setLayout(new GridLayout(8, 6));
        setLocationRelativeTo(null);

        //add all text fields and labels to login screen
        JLabel userNameButton = new JLabel("Enter Your Username", JLabel.CENTER);
        JLabel passwordButton = new JLabel("Enter Your Password", JLabel.CENTER);
        JLabel setUserStatus = new JLabel(" ", JLabel.CENTER);
        JTextField fieldForUsername = new JTextField(20);
        //set password field
        JPasswordField fieldForPassword = new JPasswordField();
        JButton enterButton = new JButton("Enter Ticket System");
        JButton exitButton = new JButton("Leave");
        JButton resetPassButton = new JButton("Reset Your Password");
        resetPassButton.setVisible(false);
        Font mainFont = new Font("Arial", Font.PLAIN, 25);
        Font statusFont = new Font("Arial", Font.PLAIN, 25);

        //add all GUI buttons for login screen
        add(exitButton);
        add(setUserStatus);
        add(resetPassButton);
        add(userNameButton);
        add(fieldForUsername);
        add(passwordButton);
        add(fieldForPassword);
        add(enterButton);
        enterButton.setBackground(Color.BLUE);
        exitButton.setBackground(Color.BLUE);
        enterButton.setForeground(Color.WHITE);
        exitButton.setForeground(Color.WHITE);
        resetPassButton.setForeground(Color.WHITE);
        resetPassButton.setBackground(Color.BLUE);
        setUserStatus.setFont(statusFont);
        userNameButton.setFont(mainFont);
        passwordButton.setFont(mainFont);
        enterButton.setFont(mainFont);
        exitButton.setFont(mainFont);
        resetPassButton.setFont(mainFont);

        enterButton.addActionListener(new ActionListener() {
            //set user login counter to none
            int loginCounter = 0;
            public void actionPerformed(ActionEvent e) {
                boolean admin = false;
                //count the amount of times the user signs in
                loginCounter = loginCounter + 1;

                //sql statement to check user credentials
                String sqlQuary = "SELECT * FROM rbac_users WHERE user_name = ? and pass_word = ?;";
                try (PreparedStatement stmt = databaseConnection.connect().prepareStatement(sqlQuary)) {
                    stmt.setString(1, fieldForUsername.getText());
                    stmt.setString(2, fieldForPassword.getText());
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        admin = rs.getBoolean("admin");
                        new mainTicketingSystem(admin);
                        setVisible(false);
                        dispose();
                    } else {

                        setUserStatus.setText((5 - loginCounter) + " /5 tries remaining before you are locked out.");
                    }

                    //set login counter to 5 before we lock the user out
                    if (loginCounter == 5) {
                        fieldForUsername.disable();
                        fieldForPassword.disable();
                        fieldForUsername.setBackground(Color.BLUE);
                        fieldForPassword.setBackground(Color.BLUE);
                        enterButton.setVisible(false);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        setVisible(true);
    }

    //main function to display the login
    public static void main(String[] args) {

        new userLogin();
    }
}