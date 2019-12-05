/*
This class is where we connect to our given database and start to add and pull data
to and from different tables.

The table we use to read authorized admins and users is "rbac_users". This is read under the username == user_name
and password == pass_word.

The table we use to create, read, updateTicket, and delete tickets is rbac_tickets.
*/

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

//create Dao class for JDBC logic and connection to our database.
public class Dao {
    static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false";
    static final String USER = "fp411", PASS = "411";
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
    Statement stment = null;

    //create method to have a user be able to add a ticket into the "bagel_tickets" table
    public int addTicket(String summary, Object detail) {
        //start the ticket id to be at 0 to increment upward
        int ticketID = 0;
        try {
            stment = connect().createStatement();
            //execute SQL statement to add the summary and details to the ticket
            stment.executeUpdate("INSERT INTO rbac_tickets" + "(summary, detail) values(" + " '"
                    + summary + "','" + detail + "')", Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = null;
            resultSet = stment.getGeneratedKeys();
            if (resultSet.next()) {
                //set ticket id to the first column in the table "bagel_tickets"
                ticketID = resultSet.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticketID;
    }

    //create method to viewTicket ticket from viewTicket ticket button upon login
    public ResultSet viewTicket() {
        ResultSet reslts = null;
        try {
            stment = connect().createStatement();
            //SQL statement to viewTicket the table of tickets
            reslts = stment.executeQuery("SELECT * FROM rbac_tickets");
            connect().close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return reslts;
    }

    //create method to updateTicket tickets by via ticket number
    public int updateTicket(String ticketId, String ticketUpdatingDetail) {
        int ticketID = 0;
        //SQL statement to inject new ticket details via ticket id
        String sqlQuary = "UPDATE rbac_tickets SET detail = ? WHERE tid = ?";
        try (PreparedStatement stmt = connect().prepareStatement(sqlQuary)) {
            stmt.setString(1, ticketUpdatingDetail);
            stmt.setString(2, ticketId);
            ticketID = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ticketID;

    }


    //add a closed marker on the ticketing field once a ticket is closed
    public int closeUpdatedTicket(String ticketId, String tickedClosed) {
        int ticketID = 0;
        //sql statement to add "closed" tag to a ticket row once a ticket is closed
        String query = "UPDATE rbac_tickets SET end_date = ?, status = 'CLOSED' WHERE tid = ?";
        try (PreparedStatement stmt = connect().prepareStatement(query)) {
            stmt.setString(1, tickedClosed);
            stmt.setString(2, ticketId);
            ticketID = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ticketID;

    }

    //create method to delete a ticket based on ticket id
    public int deleteTicket(String ticketDelete) {
        int ticketID = 0;
        //SQL statement to delete a ticket from the table via ticket id
        String sqlQuary = "DELETE FROM rbac_tickets WHERE tid = ?";
        try (PreparedStatement stmt = connect().prepareStatement(sqlQuary)) {
            stmt.setString(1, ticketDelete);
            ticketID = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ticketID;
    }

}