package com.testDataBuilder.ui.main.queryConsole;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.*;
import javax.swing.event.*;
import org.apache.log4j.Logger;

import com.testDataBuilder.config.preference.Preferences;
import com.testDataBuilder.ui.main.others.JTDBToolBar;

/**
 * This class takes a JDBC ResultSet object and implements the TableModel
 * interface in terms of it so that a Swing JTable component can display the
 * contents of the ResultSet. Note that it requires a scrollable JDBC 2.0
 * ResultSet. Also note that it provides read-only access to the results
 */
public class ResultSetTableModel extends  AbstractTableModel {
    
    static Logger logger = Logger.getLogger(JTDBToolBar.class);
    
    ResultSet results; // The ResultSet to interpret

    ResultSetMetaData metadata; // Additional information about the results

    int numcols, numrows; // How many rows and columns in the table

    /**
     * This constructor creates a TableModel from a ResultSet. It is package
     * private because it is only intended to be used by
     * ResultSetTableModelFactory, which is what you should use to obtain a
     * ResultSetTableModel
     */
    ResultSetTableModel(ResultSet results) throws SQLException {
        this.results = results; // Save the results
        metadata = results.getMetaData(); // Get metadata on them
        numcols = metadata.getColumnCount() + 1; // How many columns?
        results.last(); // Move to last row
        numrows = results.getRow(); // How many rows?
    }

    /**
     * Call this when done with the table model. It closes the ResultSet and the
     * Statement object used to create it.
     */
    public void close() {
        try {
            results.getStatement().close();
        } catch (SQLException e) {
            logger.error("ResultSetTableModel", e);
        }
    }

    /** Automatically close when we're garbage collected */
    protected void finalize() {
        close();
    }

    // These two TableModel methods return the size of the table
    public int getColumnCount() {
        return numcols;
    }

    public int getRowCount() {
        return numrows;
    }

    // This TableModel method returns columns names from the ResultSetMetaData
    public String getColumnName(int column) {
        try {
            if(column == 0){
                return "No.";
            }else{
                return metadata.getColumnLabel(column);
            }
        } catch (SQLException e) {
            return e.toString();
        }
    }

    // This TableModel method specifies the data type for each column.
    // We could map SQL types to Java types, but for this example, we'll just
    // convert all the returned data to strings.
    public Class getColumnClass(int column) {
        return String.class;
    }

    /**
     * This is the key method of TableModel: it returns the value at each cell
     * of the table. We use strings in this case. If anything goes wrong, we
     * return the exception as a string, so it will be displayed in the table.
     * Note that SQL row and column numbers start at 1, but TableModel column
     * numbers start at 0.
     */
    public Object getValueAt(int row, int column) {
        String retValue = "";

        if (column == 0) {
            retValue = Integer.valueOf(row + 1).toString();
        } else {
            try {
                if (results.getRow() != row + 1) {
                    results.absolute(row + 1); // Go to the specified row
                }
                Object o = results.getObject(column); // Get value of the

                if (o != null) {
                    retValue = o.toString(); // Convert it to a string
                }
            } catch (SQLException e) {
                retValue = e.toString();
            }
        }
        
        Integer width = widths.get(column);
        if(width == null){
            width = retValue.length();
            widths.put(column, width);
        }else{
            width += retValue.length();
        }
        
        return retValue.toString();
    }

    public Integer getColumnWidth(int column){
        Integer width = widths.get(column);
        if(width != null){
            return width / numrows;
        }else{
            return 0;
        }
    }
    
    Map<Integer, Integer> widths = new HashMap<Integer, Integer>();
    
    // Our table isn't editable
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    // Since its not editable, we don't need to implement these methods
    public void setValueAt(Object value, int row, int column) {
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    
    /**
     * This method takes a SQL query, passes it to the database, obtains the
     * results as a ResultSet, and returns a ResultSetTableModel object that
     * holds the results in a form that the Swing JTable component can use.
     */
    public static ResultSetTableModel getResultSetTableModel(String query, Connection conn) throws SQLException {
        // If we've called close(), then we can't call this method
        if (conn == null){
            throw new IllegalStateException("Connection already closed.");
        }
       
        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        int maxResult = 0;
        if(Preferences.getPreference() != null){
        	maxResult = Preferences.getPreference().getQueryConsoleMaxSize();
        }
        if(maxResult == 0){
        	maxResult = 1000;
        }
        
        statement.setMaxRows(maxResult);
        ResultSet r = statement.executeQuery(query);
        
        return new ResultSetTableModel(r);
    }
}
