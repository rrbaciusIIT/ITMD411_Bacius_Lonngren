/*
This class is where the main frame after login will be displayed.

It contains the ability to create, delete, updateTicket, and viewTicket tickets. Users may sign out from this screen.

Users also have the ability to shift views or complete actions via top frame menu items.
 */

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

// This class creates the GUI infrastructure for the program
public class mainTicketingSystem extends JFrame implements ActionListener {

    //connect database from DAO class
    static final long serialVersionUID = 42L;
    Dao dao = new Dao();
    Boolean buttonChecker = null;
    userLogin logon = new userLogin();

    //menu options at the top frame
    JMenu menuOptionsMain = new JMenu("Menu Options");
    JMenu menuAdmin = new JMenu("Admin Options");
    JMenu menuTickets = new JMenu("Ticket Options");

    //buttons on ticketing frame
    JMenuItem exitFrame;
    JMenuItem mainTicketUpdate;
    JButton deleteTicket;
    JButton closeOutTicket;
    JButton signOut;
    JMenuItem removeTicketMain;
    JMenuItem createTicketMain;
    JMenuItem ticketViewerMain;
    JButton newTicket;
    JButton viewTicket;
    JButton updateTicket;

    public mainTicketingSystem(Boolean isTicketUsers) {
        buttonChecker = isTicketUsers;
        createMenu();
        construct();
    }

    void createMenu() {
        //create menu option value descriptions
        exitFrame = new JMenuItem("Exit");
        menuOptionsMain.add(exitFrame);
        mainTicketUpdate = new JMenuItem("Update A Ticket");
        menuAdmin.add(mainTicketUpdate);
        removeTicketMain = new JMenuItem("Remove A Ticket");
        menuAdmin.add(removeTicketMain);
        ticketViewerMain = new JMenuItem("View Tickets");
        menuTickets.add(ticketViewerMain);
        createTicketMain = new JMenuItem("Create A Ticket");
        menuTickets.add(createTicketMain);

        //check when buttons are clicked
        exitFrame.addActionListener(this);
        mainTicketUpdate.addActionListener(this);
        removeTicketMain.addActionListener(this);
        createTicketMain.addActionListener(this);
        ticketViewerMain.addActionListener(this);
    }

    void construct() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        JMenuBar topBar = new JMenuBar();
        topBar.add(menuOptionsMain);
        topBar.add(menuAdmin);
        topBar.add(menuTickets);
        setJMenuBar(topBar);

        //buttons for admins
        newTicket = new JButton("New Ticket");
        viewTicket = new JButton("Current Ticket(s)");
        updateTicket = new JButton("Update Ticket");
        deleteTicket = new JButton("Remove Ticket");
        closeOutTicket = new JButton("Close Ticket");
        signOut = new JButton("Sign Off");

        //colors scheme for frame and buttons
        newTicket.setBackground(Color.BLUE);
        newTicket.setForeground(Color.WHITE);
        viewTicket.setBackground(Color.BLUE);
        viewTicket.setForeground(Color.WHITE);
        updateTicket.setBackground(Color.BLUE);
        updateTicket.setForeground(Color.WHITE);
        deleteTicket.setBackground(Color.BLUE);
        deleteTicket.setForeground(Color.WHITE);
        closeOutTicket.setBackground(Color.BLUE);
        closeOutTicket.setForeground(Color.WHITE);
        signOut.setBackground(Color.BLUE);
        signOut.setForeground(Color.WHITE);

        Font mainFont = new Font("Arial", Font.BOLD, 30);

        //set font and style
        newTicket.setFont(mainFont);
        viewTicket.setFont(mainFont);
        updateTicket.setFont(mainFont);
        deleteTicket.setFont(mainFont);
        closeOutTicket.setFont(mainFont);
        signOut.setFont(mainFont);

        //check when buttons are clicked
        newTicket.addActionListener(this);
        viewTicket.addActionListener(this);
        updateTicket.addActionListener(this);
        deleteTicket.addActionListener(this);
        closeOutTicket.addActionListener(this);
        signOut.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent wE) {
                System.exit(0);
            }
        });

        // GUI layout for main ticketing frame
        setLayout(new GridLayout(3, 2));
        setSize(550, 650);
        getContentPane().setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setVisible(true);

         if (buttonChecker == true) {
            add(newTicket);
            add(viewTicket);
            add(updateTicket);
             add(closeOutTicket);
             add(signOut);
            add(deleteTicket);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exitFrame) {
            System.exit(0);
        }

        else if (e.getSource() == newTicket || e.getSource() == createTicketMain) {
            // Ticket Options for issues
            String[] issueCategoryName = new String[10];
            issueCategoryName[0] = "Connection";
            issueCategoryName[1] = "Hardware";
            issueCategoryName[2] = "Software";
            issueCategoryName[3] = "Installation";
            issueCategoryName[4] = "Removal";
            issueCategoryName[5] = "Rights and Privileges";
            issueCategoryName[6] = "Physical Damage";
            issueCategoryName[7] = "Performance";
            issueCategoryName[8] = "Other";
            String ticketName = JOptionPane.showInputDialog(null, "What is your name");
            // No User input
            while (ticketName.length() == 0) {

                ticketName = JOptionPane.showInputDialog(null, "Please enter your name to continue...");
            }

            Object ticketDesc = JOptionPane.showInputDialog(null, "Enter the issue you are experiencing", "Issue",
                    JOptionPane.QUESTION_MESSAGE, null, issueCategoryName, "");


            // db connection from DAO
            int id = dao.addTicket(ticketName, ticketDesc);

            // display user input
            if (id != 0) {
                System.out.println("Ticket Identification: " + id);
                JOptionPane.showMessageDialog(null, "Ticket identification: " + id);
            }

            else
                System.out.println("Ticket failed");
        }

        else if (e.getSource() == viewTicket || e.getSource() == ticketViewerMain) {

            // pull tickets from ticket table
            try {
                ticketingJtable.buildTableModel(dao.viewTicket());
                setVisible(true);
            }
            // catch exception
            catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        else if (e.getSource() == updateTicket || e.getSource() == mainTicketUpdate) {
            // pull new information
            String ticketIDNumber = JOptionPane.showInputDialog(null, "Enter ID for an updated ticket");

            // error trapping for no input
            while (ticketIDNumber.length() == 0) {
                ticketIDNumber = JOptionPane.showInputDialog(null, "Enter a ticket ID to continue");
            }

            String udatedTicketDesc = JOptionPane.showInputDialog(null, "Enter an updated description");
            while (udatedTicketDesc.length() == 0) {
                udatedTicketDesc = JOptionPane.showInputDialog(null, "Ticket updated ticket has failed");
            }

            // updateTicket ticket information to database
            int id = dao.updateTicket(ticketIDNumber, udatedTicketDesc);
            if (id != 0) {
                System.out.println("Ticket identification: " + ticketIDNumber + " updated ticket: " + udatedTicketDesc);
                JOptionPane.showMessageDialog(null, "Ticket id   " + ticketIDNumber + " updated ticket: " + udatedTicketDesc);
            }
            else {
                //catch if ticket cannot updateTicket
                System.out.println("Ticket was not updated");
            }
        }

        else if (e.getSource() == deleteTicket || e.getSource() == removeTicketMain) {
            String ticketDelete = JOptionPane.showInputDialog(null, "Enter the ticket ID that you want to delete");
            // error trapping for no input
            while (ticketDelete.length() == 0) {
                ticketDelete = JOptionPane.showInputDialog(null, "Enter ticket ID to continue");
            }

            // delete ticket by a given id
            int id = dao.deleteTicket(ticketDelete);
            if (id != 0) {
                System.out.println("Ticket identification " + ticketDelete + "  is deleted");
                JOptionPane.showMessageDialog(null, "Ticket id  " + ticketDelete + " is deleted");
            }
            else
                System.out.println("Cannot Create Ticket");
        }

        else if (e.getSource() == closeOutTicket) {
            // get updateTicket info from admin
            String IDNumber = JOptionPane.showInputDialog(null, "Enter the ID you'd like to close out");

            // error trapping for no input
            while (IDNumber.length() == 0) {
                IDNumber = JOptionPane.showInputDialog(null, "Enter the ID to continue");
            }

            String closeDate = JOptionPane.showInputDialog(null, "What is the date? (YYYY-MM-DD)");
            // error trapping for no input
            while (closeDate.length() == 0) {
                closeDate = JOptionPane.showInputDialog(null, "Enter the date to continue");
            }

            // updateTicket ticket status to database
            int id = dao.closeUpdatedTicket(IDNumber, closeDate);
            if (id != 0) {
                System.out.println("Ticket identification " + IDNumber + "Ticket closed on " + closeDate);
                JOptionPane.showMessageDialog(null, "Ticket id " + IDNumber + " Closed date is " + closeDate);
            }

            else {
                System.out.println("Ticket Closing Failed");
            }
        }
        else if (e.getSource() == signOut) {
            System.exit(0);
        }
    }
}