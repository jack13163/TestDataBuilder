package com.testDataBuilder.ui.main.queryConsole;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import org.apache.commons.io.FileUtils;
import org.syntax.jedit.tokenmarker.TSQLTokenMarker;

import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.ui.database.JSQLFileChooser;
import com.testDataBuilder.ui.main.MainFrame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class QueryPanel extends JPanel {

    private SQLQueryToolBar sqlQueryToolBar = null;
    private JSplitPane jQueryPane = null;
    private JScrollPane jQueryConsoleTopPane1 = null;
    private JEditTextArea SQLQueryTextArea = null;
    private JScrollPane jQueryConsoleBottomPane = null;
    private JTable jResultTable = null;
    List<String> allConnNames = null;  //  @jve:decl-index=0:
    private JLabel msgLine = null;

    private String queryType = Role.METHOD_SQL_QUERY;
    
    public QueryPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);
        this.setSize(new Dimension(380, 266));
        this.add(getSQLQueryToolBar(), BorderLayout.NORTH);
        this.add(getJQueryPane(), BorderLayout.CENTER);
        this.add(getMsgLine(), BorderLayout.SOUTH);
    }

    public SQLQueryToolBar getSQLQueryToolBar(){
        if(sqlQueryToolBar == null){
            sqlQueryToolBar = new SQLQueryToolBar(this);
        }
        return sqlQueryToolBar;
    }
    
    /**
     * This method initializes jQueryPane	
     * 	
     * @return javax.swing.JSplitPane	
     */
    private JSplitPane getJQueryPane() {
        if (jQueryPane == null) {
            jQueryPane = new JSplitPane();
            jQueryPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            jQueryPane.setDividerLocation(150);
            jQueryPane.setDividerSize(3);
            jQueryPane.setResizeWeight(0.5);
            jQueryPane.setBottomComponent(getJQueryConsoleBottomPane());
            jQueryPane.setTopComponent(getJQueryConsoleTopPane1());
        }
        return jQueryPane;
    }

    /**
     * This method initializes jQueryConsoleTopPane1	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJQueryConsoleTopPane1() {
        if (jQueryConsoleTopPane1 == null) {
            jQueryConsoleTopPane1 = new JScrollPane();
            jQueryConsoleTopPane1.setViewportView(getSQLQueryTextArea());
        }
        return jQueryConsoleTopPane1;
    }

    private JLabel getMsgLine(){
        if(msgLine == null){
            msgLine = new JLabel();
        }
        return msgLine;
    }
    /**
     * This method initializes SQLQueryTextArea	
     * 	
     * @return com.testDataBuilder.ui.main.queryConsole.JEditTextArea	
     */
    private JEditTextArea getSQLQueryTextArea() {
        if (SQLQueryTextArea == null) {
            SQLQueryTextArea = new JEditTextArea();
            SQLQueryTextArea.setTokenMarker(new TSQLTokenMarker());
        }
        return SQLQueryTextArea;
    }
    
    public String getExecuteSQLString(){
        String selStr = getSQLQueryTextArea().getSelectedText();
        if(selStr == null || selStr.equals("")){
            selStr = getSQLString();
        }
        return selStr;
    }
    
    public String getBatchSeparator(){
        return this.getSQLQueryToolBar().getBatchSeparator();
    }
    
    public String getSQLString(){
        return getSQLQueryTextArea().getText().trim();
    }
    
    public void setSQLString(String sqlString){
        getSQLQueryTextArea().setText(sqlString);
    }
    
    public String getCurConnName(){
        return (String) this.getSQLQueryToolBar().getComboxConnection().getSelectedItem();
    }
    
    /**
     * This method initializes jQueryConsoleBottomPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJQueryConsoleBottomPane() {
        if (jQueryConsoleBottomPane == null) {
            jQueryConsoleBottomPane = new JScrollPane();
            jQueryConsoleBottomPane.setViewportView(getJResultTable());
        }
        return jQueryConsoleBottomPane;
    }

    /**
     * This method initializes jResultTable	
     * 	
     * @return javax.swing.JTable	
     */
    public JTable getJResultTable() {
        if (jResultTable == null) {
            jResultTable = new JTable();
            jResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        return jResultTable;
    }

    public void initConnectionCombox(List<String> allConnNames){
        this.allConnNames = allConnNames;
        getSQLQueryToolBar().getComboxConnection().initConnectionCombox(allConnNames);
    }
    
    public void initConnectionCombox(List<String> allConnNames, Object selObject){
        this.allConnNames = allConnNames;
        getSQLQueryToolBar().getComboxConnection().initConnectionCombox(allConnNames, selObject);
    }
    
    
    
    /**
     * This method uses the supplied SQL query string, and the
     * ResultSetTableModelFactory object to create a TableModel that holds
     * the results of the database query.  It passes that TableModel to the
     * JTable component for display.
     **/
    public void displayQueryResults(final String sqls, final JLabel msgLine, final String sep) {

    new Thread(new Runnable() {
        public void run() {
            try {
                msgLine.setText("正在查询.......");                
                String connName = QueryPanel.this.getCurConnName();
                if(connName != null){
                    Connection connection = MainFrame.getInstance().getTestDataConfig().getConn(connName);
                    
                    //Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//                    Statement statement = connection.createStatement();
//                     String[] sqlArr = sqls.toLowerCase().split(sep);
//                    for(String sql :sqlArr){
//                        statement.addBatch(sql);
//                    }
//                    int[] results = statement.executeBatch();
//                    ResultSet rs = statement.getResultSet();
                    
                    ResultSetTableModel dataModel = ResultSetTableModel.getResultSetTableModel(sqls, connection);
                    jResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    QueryPanel.this.jResultTable.setModel(dataModel);
                }else{
                    jResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    String[][] data = {{"没有可用的连接"}};
                    String[] columnNames = {"结果"};
                    QueryPanel.this.jResultTable.setModel(new DefaultTableModel(data,columnNames ));
                }
            }
            catch (SQLException ex) {
                jResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                String[][] data = {{ex.getMessage()}};
                String[] columnNames = {"查询出错"};
                QueryPanel.this.jResultTable.setModel(new DefaultTableModel(data,columnNames ));
            }
            msgLine.setText(" ");
        }
        }).start();
    }

    public void execSQL() {
        String sql = this.getExecuteSQLString();
        String sep = this.getBatchSeparator();
        if(sql != null && sql.trim().length() > 0){
        	sql = sql.trim().toLowerCase();
        	if(this.getQueryType().equalsIgnoreCase(Role.METHOD_SQL_FUNC)){
        		if(!sql.startsWith("select")){
        			sql = "select (" + sql + ")";
        		}
        	}
            this.displayQueryResults(sql, this.msgLine, sep);
        }       
    }
    
    public void openScript(){
        JSQLFileChooser sqlFileChooser = new JSQLFileChooser();  
        
        if (sqlFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String scriptFile = sqlFileChooser.getSelectedFile().getAbsolutePath();
            try {
                String script = FileUtils.readFileToString(new File(scriptFile));
                this.getSQLQueryTextArea().setText(script);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "打开脚本出错，" + e.getMessage());
            }
            
        }
    }

    public List<String> getAllConnNames() {
        return allConnNames;
    }

    public void setAllConnNames(List<String> allConnNames) {
        this.allConnNames = allConnNames;
    }

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
