package com.testDataBuilder.ui.main.queryConsole;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * This class creates a Swing GUI that allows the user to enter a SQL query. It
 * then obtains a ResultSetTableModel for the query and uses it to display the
 * results of the query in a scrolling JTable component.
 */
public class TestQueryFrame extends JFrame {
    
    private QueryPanel jContentPane = null;

    private JPanel jRootPanel = null;

    private JLabel jMsgLine = null;

    /**
     * This constructor method creates a simple GUI and hooks up an event
     * listener that updates the table when the user enters a new query.
     */
    public TestQueryFrame() {
        super("QueryFrame");
        initialize(); // Set window title

        // Arrange to quit the program when the user closes the window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(new Dimension(527, 377));
        this.setContentPane(getJRootPanel());
        this.setTitle("QueryConsole");
    }


    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private QueryPanel getJContentPane() {
        if (jContentPane == null) {
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            conns.put("DrmPolicy20", TestQueryFrame.getConnection("DrmPolicy20"));
            conns.put("DrmLog", TestQueryFrame.getConnection("DrmLog"));
            jContentPane = new QueryPanel();
        }
        return jContentPane;
    }

    private Map<String, Connection> conns = new HashMap<String, Connection>();  //  @jve:decl-index=0:
    
    /**
     * This method initializes jRootPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJRootPanel() {
        if (jRootPanel == null) {
            jMsgLine = new JLabel();
            jMsgLine.setText(" ");
            jRootPanel = new JPanel();
            jRootPanel.setLayout(new BorderLayout());
            jRootPanel.add(getJContentPane(), BorderLayout.CENTER);
            jRootPanel.add(jMsgLine, BorderLayout.SOUTH);
        }
        return jRootPanel;
    }

    /**
     * This simple main method tests the class. It expects four command-line
     * arguments: the driver classname, the database URL, the username, and the
     * password
     */
    public static void main(String args[]) throws Exception {
        // Create the factory object that holds the database connection using
        // the data specified on the command line
        TestQueryFrame qf = new TestQueryFrame();

        // Set the size of the QueryFrame, then pop it up
        qf.setSize(500, 600);
        qf.setVisible(true);
    }

    static Connection getConnection(String dbName) {
        String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=" +  dbName;
        String uName = "sa";
        String uPwd = "sa";
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Now use that driver to connect to the database
        try {
            return DriverManager.getConnection(url, uName, uPwd);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
} // @jve:decl-index=0:visual-constraint="10,10"

