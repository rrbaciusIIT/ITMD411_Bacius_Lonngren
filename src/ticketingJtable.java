/*
This class creates the frame for the ticket viewTicket.

This displays the ticket table and displays into a java GUI for users to see all data fields related to the ticket.

The user can direct themselves here by signing in and clicking "View Tickets".
 */

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;


    //create method for ticket viewing table
    public class ticketingJtable {
    public final DefaultTableModel tableModel = new DefaultTableModel();
    public static DefaultTableModel buildTableModel(ResultSet rj) throws SQLException {

        //fetch ticketData from ticket table
        ResultSetMetaData fetchMetaData = rj.getMetaData();
        Vector<String> tickeNames = new Vector<String>();
        int columnNumber = fetchMetaData.getColumnCount();
        for (int columnIndex = 1; columnIndex <= columnNumber; columnIndex++) {
            tickeNames.add(fetchMetaData.getColumnName(columnIndex));
        }
        //display tickets in ticket table in row format
        Vector<Vector<Object>> ticketData = new Vector<Vector<Object>>();
        while (rj.next()) {
            Vector<Object> vectorObjectShape = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnNumber; columnIndex++) {
                vectorObjectShape.add(rj.getObject(columnIndex));
            }
            ticketData.add(vectorObjectShape);
        }
        //create ticket model frame
        DefaultTableModel model = new DefaultTableModel(ticketData, tickeNames);
        JTable table = new JTable(model);
        JFrame frame = new JFrame("All Ticket Info");
        frame.setSize(1000, 500);
        frame.add(new JScrollPane(table));
        frame.setDefaultCloseOperation(0);
        frame.pack();
        frame.setVisible(true);
        return new DefaultTableModel(ticketData, tickeNames);

    }

}