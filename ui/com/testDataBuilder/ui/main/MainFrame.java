package com.testDataBuilder.ui.main;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;
import com.testDataBuilder.config.AutoConfigEngine;
import com.testDataBuilder.config.ColumnConfig;
import com.testDataBuilder.config.DatabaseConfig;
import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.core.DatabaseUtil;
import com.testDataBuilder.dbMetaInfo.Database;
import com.testDataBuilder.dbMetaInfo.Table;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterFrame;
import com.testDataBuilder.ui.core.JTDButton;
import com.testDataBuilder.ui.core.JTextFieldEx;
import com.testDataBuilder.ui.database.ConfigDBDialog;
import com.testDataBuilder.ui.help.AboutDialog;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.ui.help.UpgradeDialog;
import com.testDataBuilder.ui.role.GenerateDataDialog;
import com.testDataBuilder.ui.role.RoleDialog;
import com.testDataBuilder.ui.main.others.AppPropertyDialog;
import com.testDataBuilder.ui.main.others.JTDBMenuBar;
import com.testDataBuilder.ui.main.others.JTDBToolBar;
import com.testDataBuilder.ui.main.queryConsole.QueryPanel;
import com.testDataBuilder.ui.preference.PreferenceDialog;
import com.testDataBuilder.ui.project.JProjectDirChooser;
import com.testDataBuilder.ui.project.ProjectDialog;
import com.testDataBuilder.ui.project.RecentProjects;
import com.testDataBuilder.util.Global;
import java.io.File;
import java.io.IOException;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;

import com.testDataBuilder.ui.main.dataModel.DataModelPanel;


public class MainFrame extends CenterFrame {

    static Logger logger = Logger.getLogger(MainFrame.class);
    
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

    private JTDBToolBar tdbToolBar = null;
    
	private JSplitPane jSplitPane = null;

    private JLabel msgLine = null;
    
	private WorkspaceDataCache workspaceDataCache = WorkspaceDataCache.getInstance();
    
    public Global G = Global.getInstance();  //  @jve:decl-index=0:
    
	public String getProjectConfigFile(){
	    return getWorkspace() + Global.SEP + Global.PROJECT_CONFIG;
    }

    public Database getDatabase(){
        return this.getJLeftTree().getDatabase();
    }
    
	public void enableControl(boolean enable){
		this.getCbxCloseIDAutoInsert().setEnabled(enable);
		this.getTxtCount().setEnabled(enable);
		this.getCbxOnError().setEnabled(enable);
		this.getTxtInit().setEnabled(enable);
		this.getTxtDestroy().setEnabled(enable);
		this.getColumnTable().setEnabled(enable);
		this.getBtnAutoConfig().setEnabled(enable);
		this.getBtnAdd().setEnabled(enable);
		this.getBtnDel().setEnabled(enable);
        this.getBtnSave().setEnabled(enable);
        this.getBtnClearColumnConfig().setEnabled(enable);
	}
 

    private void resetRefValues(){
		List<String> allTypes = this.getAllRefRoles4List();
		this.getColumnTable().setRefs(allTypes);
	}
	
    /**
     * 返回用于List显示的类型。
     * <p><code>getAllRefRoles4List</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    public List<String> getAllRefRoles4List(){
    	List<String> tempList = null;
    	TableConfig curTableConfig = this.getCurTableConfig();
    	if(curTableConfig != null){
    		tempList = curTableConfig.getRoleFactory().getAllRefRoles4List();
    	}else{
    		tempList = new ArrayList<String>();
    	}

    	if(workspaceDataCache.getGlobalRoleFactory() != null){
    		tempList.addAll(workspaceDataCache.getGlobalRoleFactory().getAllRefRoles4List());
    	}
        return tempList;        
    }
	//private DatabaseUtil dbUtil = null;  //  @jve:decl-index=0:

	private JScrollPane leftPane = null;

	private JDBTree jLeftTree = null;

	private JPanel configPanel = null;

	private JCheckBox cbxGenerate = null;

	private JLabel jLabel = null;

	private JTextField txtCount = null;

	private JLabel jLabel1 = null;

	private JComboBox cbxOnError = null;

	private JLabel jLabel2 = null;

	private JScrollPane initPanel = null;

	private JTextArea txtInit = null;

	private JLabel jLabel3 = null;

	private JScrollPane destroyPane = null;

	private JTextArea txtDestroy = null;

	private JScrollPane columnPane = null;

	private ColumnTable columnTable = null;

	private JLabel jLabel4 = null;

	private JCheckBox cbxCloseIDAutoInsert = null;

	private JTDButton btnAutoConfig = null;

	private JTDButton btnAdd = null;

	private JTDButton btnDel = null;

    private QueryPanel queryPanel = null;
    
    @Override
    public JTDBMenuBar getJMenuBar() {
        return (JTDBMenuBar)super.getJMenuBar();
    }

    public void configDBAndInitWorkspace(String configName, boolean changeWorkspace) throws IOException, BaseException{
        
        boolean result = false;
        try {
            result = this.configAndReconnect(configName);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.connectDatabase") + ex.getMessage()); //$NON-NLS-1$
        }

        if(result &&  configName.equals(TestDataConfig.DEFAULT)){
            if(changeWorkspace){
                this.initWorkspaceFromDB();
            }else{
                this.refreshDataFromDB();           
            }
        }
    }
    
    public boolean configDB(String configName) throws BaseException{
        
        DatabaseConfig databaseConfig = null;
        
        if(configName!= null){       
            databaseConfig = this.getDatabaseConfig(configName);
        }
        
        ConfigDBDialog dbDialog = new ConfigDBDialog(MainFrame.this,databaseConfig);
        dbDialog.setModal(true);
        dbDialog.setVisible(true);
        
        if(dbDialog.isOk()){
            this.getTestDataConfig().saveOrUpdateDatabaseConfig(dbDialog.getDatabaseConfig());
            if(configName == null){
                newConfigName = dbDialog.getTxtName().getText().trim();
            }else{
                newConfigName = null;
            }
        }
        return dbDialog.isOk();
        
    }
    private String newConfigName = null;
    
    public DatabaseConfig getDatabaseConfig(String configName){
        DatabaseConfig databaseConfig = null;
        TestDataConfig testDataConfig = this.getTestDataConfig();
        if(configName.equals(TestDataConfig.DEFAULT)){
            databaseConfig = testDataConfig.getDefDatabaseConfig();
        }else{
            databaseConfig = testDataConfig.getDatabaseConfigs().get(configName);
        }
        return databaseConfig;
    }
    
    private void bindToTree(Database database) {
        if(database != null ){
            this.getJLeftTree().setDatabase(database);
        }
        if(this.workspaceDataCache != null){
        	workspaceDataCache.setDatabase(database);
        }else{
        	logger.error("%%%%%%%%%%%  workspaceDatCache is null %%%%%%%%%");
        }
        this.getJLeftTree().setData(database);
        TestDataConfig tdc = this.getTestDataConfig();
        if(tdc != null && tdc.getDatabaseConfigs() != null){
            this.getJLeftTree().setDatabaseConfigs(tdc.getDatabaseConfigs());            
        }
        this.getJLeftTree().updateUI();
    }
    
	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerSize(3);
			jSplitPane.setLeftComponent(getLeftPane());
            jSplitPane.setRightComponent(getJTabbedPane());
			jSplitPane.setDividerLocation(200);			
			jSplitPane.setResizeWeight(0.4);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes leftPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getLeftPane() {
		if (leftPane == null) {
			leftPane = new JScrollPane();
			leftPane.setViewportView(getJLeftTree());
		}
		return leftPane;
	}
	/**
	 * This method initializes jLeftTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JDBTree getJLeftTree() {
		if (jLeftTree == null) {
			jLeftTree = new JDBTree(this);
			jLeftTree.addMouseListener(new MouseAdapter(){

				public void mouseClicked(MouseEvent e) {
					MainFrame.this.treeMouseClicked(e);
				}
			});
			TreeSelectionListener tsl = new TreeSelectionListener(){

				public void valueChanged(TreeSelectionEvent e) {
					MainFrame.this.treeSelectionChanged(e);
				}
				
			}; 
			jLeftTree.addTreeSelectionListener(tsl);
		}
		return jLeftTree;
	}
	
    protected void treeSelectionChanged(TreeSelectionEvent e) {
		TreePath treePath = e.getPath();
		if(treePath != null){			
			this.treeSelectChanged(treePath.getPath());	
		}
	}
    
	protected void treeMouseClicked(MouseEvent e) {
        JTree tree = (JTree) e.getComponent();
        TreePath treePath = null;
        if (e.getButton() == 3)/* 右键 */{
            treePath = tree.getPathForLocation(e.getX(), e.getY());
            if(treePath == null){
                return;
            }
            tree.setSelectionPath(treePath);
        }
       treePath = tree.getSelectionPath();
        if (treePath == null) {
            return;
        }

        Object[] paths = treePath.getPath();
       // this.treeSelectChanged(paths);
        if (e.getButton() == 3)/* 右键 */{
           if(isSelTable(paths)){
               
           }else if(isSelDatabase(paths)){
               this.showDBPopupMenu(tree, e.getX(), e.getY());
           }else if(isSelConnectionPool(paths)){
               this.showConnectionPoolMenu(tree, e.getX(), e.getY());
           }else if(isSelConnectConfig(paths)){
               this.showConnectMenu(tree, e.getX(), e.getY());
           }
        }
    }
    
	protected void treeSelectChanged(Object[] paths) {
		
        if (isSelTableOrTableField(paths)) {
			String tableName = paths[JDBTree.LEVEL_TABLE].toString();
			Table selTable = (Table)paths[JDBTree.LEVEL_TABLE];
			
			Table curSelTable = this.getJLeftTree().getDatabase().getSelTable();
			if (curSelTable != selTable) {
                this.setCurSelTable(selTable);
				File configFile = workspaceDataCache.getTableConfigFile(selTable
						.getTableName());
				this.getCbxGenerate().setSelected(configFile.exists());
				showConfig(tableName);
			}
		} else {
            this.setCurSelTable(null);
			this.getCbxGenerate().setSelected(false);
			showConfig(null);
		}
        
        if(isSelConnectConfig(paths)){
            DatabaseConfig curSelDatabaseConfig = (DatabaseConfig) paths[1]; 
            this.setCurSelDatabaseConfig(curSelDatabaseConfig);
        }else{
            this.setCurSelDatabaseConfig(null);
        }

		if (this.getCurSelTable() != null) {
			this.getColumnTable().setFields(this.getCurSelTable().getAllFields());
		}

		this.cbxGenerate.setEnabled(this.getCurSelTable() != null);
	}
	
    protected void showDBPopupMenu(Component invoker, int x, int y) {
        JPopupMenu pupupMenu = getJMenuBar().getDBPopupMenu();
        pupupMenu.show(invoker, x, y);
    }
    
    
    protected void showConnectionPoolMenu(Component invoker, int x, int y){
        JPopupMenu pupupMenu = getJMenuBar().getConnectionPoolPopupMenu();
        pupupMenu.show(invoker, x, y);
    }

    
    public void addConnectConfig() {
        try {
            this.addNewConnectionConfig();
            tabbedPaneSelChanged();
            this.getJLeftTree().updateUI();
        } catch (Exception e) {
            logger.error("addConnectConfig", e); //$NON-NLS-1$
            JOptionPane.showMessageDialog(this,RM.R("label.MainFrame.error.addNewConfig") + e.getMessage()); //$NON-NLS-1$
        } 
    }

    public void disconnectAll() {
        this.getTestDataConfig().disconnectAll();
        tabbedPaneSelChanged();
        this.getJLeftTree().updateUI();
    }

    public void disconnect() {
        if(this.getCurSelDatabaseConfig() != null){
            String configName = this.getCurSelDatabaseConfig().getName();
            this.getTestDataConfig().disconnectDatabase(configName);
            tabbedPaneSelChanged();         
            this.getJLeftTree().updateUI();
        }
    }

    public void showConnectMenu(Component invoker, int x, int y){
        if(invoker instanceof JDBTree){           
            JPopupMenu pupupMenu = getJMenuBar().getConnectionPopupMenu();
            pupupMenu.show(invoker, x, y);
        }
    }
    
    public void delelteConfig() {
        if(this.getTestDataConfig() != null && this.getCurSelDatabaseConfig() != null){
            try {    
                if(JOptionPane.showConfirmDialog(this, RM.R("label.MainFrame.deleteConnectConfirm"))==JOptionPane.OK_OPTION ){ //$NON-NLS-1$
                    int[] rows = this.getJLeftTree().getSelectionRows();
                    int selRow = rows[0];
                    //TreePath selPath = this.getJLeftTree().getSelectionPath();
                    TreePath parentPath = this.getJLeftTree().getSelectionPath().getParentPath();
                    if(this.getTestDataConfig().deleteDatabaseConfig(
                                    this.getCurSelDatabaseConfig().getName())){
                        this.getJLeftTree().updateUI();
                        this.getJLeftTree().expandPath(parentPath);
                        
                        int rowCount = this.getJLeftTree().getRowCount();
                        if(selRow == rowCount){
                            selRow = rowCount - 1;
                        }
                        this.getJLeftTree().setSelectionRow(selRow);
                        this.getJLeftTree().updateUI();
                    }
                }
            } catch (BaseException e) {
               JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.deleteConfig") + e.getMessage()); //$NON-NLS-1$
            }
        }
    }

    public boolean configAndReconnect() throws BaseException, SQLException {
        if(this.getCurSelDatabaseConfig() != null){
            String configName = this.getCurSelDatabaseConfig().getName();
            return configAndReconnect(configName);
        }
        
        return false;
    }

    protected boolean configAndReconnect(String configName) throws BaseException, SQLException {       
        boolean result = this.configDB(configName);
        if(result){
            this.getTestDataConfig().disconnectDatabase(configName);
            if(configName == null){
                configName = newConfigName;
            }
            this.getTestDataConfig().connectDatabase(configName);
        }
        return result;
        
    }
    
    public boolean addNewConnectionConfig() throws SQLException, BaseException{
       return configAndReconnect(null);
    }
    
    public TestDataConfig getTestDataConfig(){
        if(this.getWorkspaceDataCache() != null){
            return this.getWorkspaceDataCache().getTestDataConfig();
        }
        return null;
    }
    
    public void connectAll() {
        try{
            this.getTestDataConfig().connectAll();
            tabbedPaneSelChanged();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.connectDatabase") + ex.getMessage()); //$NON-NLS-1$
        }
        this.getJLeftTree().updateUI();
    }

    public void connect() {
        try{
            if(this.getCurSelDatabaseConfig() != null){
                this.getTestDataConfig().connectDatabase(this.getCurSelDatabaseConfig().getName());
                tabbedPaneSelChanged();
            }
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.connectDatabase") + ex.getMessage()); //$NON-NLS-1$
        }
        this.getJLeftTree().updateUI();
    }

    public void autoConfigAll() {
        try{
        this.getJLeftTree().autoConfigAll();
        }catch(IOException ex){
            logger.error("autoConfigAll", ex); //$NON-NLS-1$
            JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.saveAutoConfig") + ex.getMessage()); //$NON-NLS-1$
            
        }
    }
    
    public void deleteAllConfig(){
        int result = JOptionPane.showConfirmDialog(this, RM.R("label.MainFrame.deleteAllConfig.confirm"),
                        RM.R("label.MainFrame.deleteAllConfig.confirm.title"),
                        JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION){
            getWorkspaceDataCache().removeAndBankupAllTableConfig();
            this.getJLeftTree().updateUI();
        }
    }

	private void showConfig(String tableName) {
		if(tableName == null){
			dataExchange(null, true);
			this.getColumnTable().setRefs(new ArrayList(0));
		}else{
			File configFile = workspaceDataCache.getTableConfigFile(tableName);
			if(configFile.exists()){
				try {
					TableConfig tableConfig = this.getWorkspaceDataCache().getTableConfig(tableName);					
					dataExchange(tableConfig, true);
				} catch (BaseException e) {
					logger.error("showConfig", e); //$NON-NLS-1$
					JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.parseConfigFile") + e.getMessage()); //$NON-NLS-1$
				}
			}else{
				dataExchange(null, true);
			}
			resetRefValues();
		}
	}


	private void dataExchange(TableConfig tableConfig, boolean isToControl){
		if(isToControl){
            boolean configExist = (tableConfig != null);
            //this.cbxGenerate.setSelected(configExist);
			if(!configExist){
				this.cbxCloseIDAutoInsert.setSelected(false);
				this.txtCount.setText(null);
				this.cbxOnError.setSelectedItem(0);
				this.txtInit.setText(null);
				this.txtDestroy.setText(null);
				this.columnTable.setData(null);
			}else{
				this.cbxCloseIDAutoInsert.setSelected(tableConfig.isCloseIdAutoInsert());
				this.txtCount.setText(tableConfig.getCount() + ""); //$NON-NLS-1$
				this.cbxOnError.setSelectedItem(tableConfig.getOnError());
				this.txtInit.setText(tableConfig.getInit());
				this.txtDestroy.setText(tableConfig.getDestroy());
                List<ColumnConfig> tempColumnConfig = TableConfig.cloneColumnConfigs(tableConfig.getColumnConfigs());
				this.columnTable.setData(tempColumnConfig);
			}
		}else{
			if(this.getCurSelTable() != null){
				tableConfig.setTableName(this.getCurSelTable().getTableName());
                tableConfig.setOnError((String)cbxOnError.getSelectedItem());
				tableConfig.setCloseIdAutoInsert(cbxCloseIDAutoInsert.isSelected());
				tableConfig.setCount(Integer.valueOf(txtCount.getText()));
				tableConfig.setInit(txtInit.getText().trim());
				tableConfig.setDestroy(txtDestroy.getText().trim());
				List<ColumnConfig> tempColumnConfigs = TableConfig.cloneColumnConfigs(this.columnTable.getData());
				tableConfig.setColumnConfigs(tempColumnConfigs);
			}
		}
	}
	
	public static void printPath(Object[] paths){
		System.out.println("==============================");
		for(Object path : paths){
			System.out.println(path);
		}
	}

    private boolean isSelTableOrTableField(Object[] paths) {
        return (paths != null 
        		&& paths.length >= (JDBTree.LEVEL_TABLE + 1) 
        		&& paths[JDBTree.LEVEL_TABLE] instanceof Table);
    }

    private boolean isSelTable(Object[] paths) {
        boolean isSelTable = (paths != null 
        		&& paths.length == (JDBTree.LEVEL_TABLE + 1) 
        		&& paths[JDBTree.LEVEL_TABLE] instanceof Table);
        return isSelTable;
    }

    private boolean isSelDatabase(Object[] paths) {
        return (paths != null && paths.length == (JDBTree.LEVEL_DB + 1) 
        		&& paths[JDBTree.LEVEL_DB] instanceof Database);
    }
    
    private boolean isSelConnectionPool(Object[] paths) {
        return (paths != null && paths.length == 1 && !(paths[0] instanceof Database));
    }
    
    private boolean isSelConnectConfig(Object[] paths) {
        return (paths != null && paths.length == 2 && paths[1] instanceof DatabaseConfig);
    }

	/**
	 * This method initializes rightPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getConfigPanel() {
		if(configPanel == null) {
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(19, 203, 49, 18));
			jLabel4.setText(RM.R("label.MainFrame.ColumnConfig")); //$NON-NLS-1$
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(20, 137, 45, 20));
			jLabel3.setText(RM.R("label.MainFrame.Clear")); //$NON-NLS-1$
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(20, 73, 51, 20));
			jLabel2.setText(RM.R("label.MainFrame.Init")); //$NON-NLS-1$
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(250, 44, 49, 21));
			jLabel1.setText(RM.R("label.MainFrame.OnError")); //$NON-NLS-1$
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(20, 44, 49, 20));
			jLabel.setText(RM.R("label.MainFrame.Rows")); //$NON-NLS-1$
			configPanel = new JPanel();
			configPanel.setLayout(null);
			configPanel.add(getCbxGenerate(), null);
			configPanel.add(jLabel, null);
			configPanel.add(getTxtCount(), null);
			configPanel.add(jLabel1, null);
			configPanel.add(getCbxOnError(), null);
			configPanel.add(jLabel2, null);
			configPanel.add(getInitPanel(), null);
			configPanel.add(jLabel3, null);
			configPanel.add(getDestroyPane(), null);
			configPanel.add(getColumnPane(), null);
			configPanel.add(jLabel4, null);
			configPanel.add(getCbxCloseIDAutoInsert(), null);
			configPanel.add(getBtnAutoConfig(), null);
			configPanel.add(getBtnAdd(), null);
			configPanel.add(getBtnDel(), null);
			configPanel.add(getBtnSave(), null);
			configPanel.add(getBtnClose(), null);
			configPanel.add(getBtnClearColumnConfig(), null);
            configPanel.addComponentListener(new ComponentAdapter(){
               @Override
                public void componentResized(ComponentEvent e) {
                    MainFrame.this.frameResized(e);                    
                } 
            });
		}
		return configPanel;
	}
    
    public static final int rightSpace = 20;
    
	protected void frameResized(ComponentEvent e) {
        int rightPaneWidth = getConfigPanel().getWidth();
        if(rightPaneWidth <  500){
            rightPaneWidth = 500;
        }
        int rightPaneHeight = getConfigPanel().getHeight();
        if(rightPaneHeight < 350){
            rightPaneHeight = 350;
        }
        int btnCloseX  = rightPaneWidth - getBtnSave().getWidth() - rightSpace;
        int btnSaveX = btnCloseX - rightSpace - getBtnClose().getWidth();
         
        this.getInitPanel().setSize( rightPaneWidth - getInitPanel().getX() - rightSpace, getInitPanel().getHeight());
        this.getDestroyPane().setSize(rightPaneWidth - getDestroyPane().getX() - rightSpace, getDestroyPane().getHeight());
        this.getColumnPane().setSize(rightPaneWidth - getColumnPane().getX() - rightSpace, rightPaneHeight - 250);
        this.getBtnSave().setLocation(btnSaveX, rightPaneHeight - 35);
        this.getBtnClose().setLocation(btnCloseX, rightPaneHeight - 35);

        this.paintComponents(this.getGraphics());
    }


    /**
	 * This method initializes cbxGenerate	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCbxGenerate() {
		if (cbxGenerate == null) {
			cbxGenerate = new JCheckBox();
			cbxGenerate.setBounds(new Rectangle(18, 17, 159, 20));
			cbxGenerate.setEnabled(false);
			cbxGenerate.setText(RM.R("label.MainFrame.generateTestData")); //$NON-NLS-1$
			cbxGenerate.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainFrame.this.roleActionPerformed(e);
				}
                
			});     
            cbxGenerate.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e) {
                    MainFrame.this.roleStateChanged(e);
                }
                
            });
		}
		return cbxGenerate;
	}


	protected void roleStateChanged(ChangeEvent e) {
        boolean isSelected = this.getCbxGenerate().isSelected();
        this.enableControl(isSelected);
    }


    protected void roleActionPerformed(ActionEvent e) {
		boolean isSelected = this.getCbxGenerate().isSelected();
        String tableName = this.getCurSelTable().getTableName();
        File configFile = workspaceDataCache.getTableConfigFile(this.getCurSelTable().getTableName());
        
        String configFileName = configFile.getAbsolutePath();
        String bankupConfigFileName = configFileName + ".bank"; //$NON-NLS-1$
        
        //取消配置。
        if(!isSelected){
        	this.getWorkspaceDataCache().removeAndBankupTableConfig(tableName);   
        }else/*选中，表示生成*/ {
            
            File bankupConfigFile = new File(bankupConfigFileName);
            if(bankupConfigFile.exists()){
                if(!bankupConfigFile.renameTo(configFile)){
                  logger.error("error on rename file [" + bankupConfigFileName  //$NON-NLS-1$
                                  + "] to [" + configFileName + "]"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }else if(!configFile.exists()){
                try {
                    String tableConfigContent = TableConfig.CONIFG_DEF_VALUE.replaceAll("tableName", //$NON-NLS-1$
                                    this.getCurSelTable().getTableName());
                    FileUtils.writeStringToFile(configFile, tableConfigContent);
                } catch (IOException e1) {
                    logger.error("MainFrame", e1); //$NON-NLS-1$
                    JOptionPane.showMessageDialog(this,RM.R("label.MainFrame.error.saveConfigFile") +  e1.getMessage()); //$NON-NLS-1$
                }
            }
            
        }
        
        this.showConfig(tableName);
        
		//enableControl(isSelected);
		if(this.txtCount.getText().trim().equals("")){ //$NON-NLS-1$
			this.txtCount.setText("" + Global.getInstance().P.getDefaultRowToGenerate()); //$NON-NLS-1$
		}
	}


	/**
	 * This method initializes txtCount	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtCount() {
		if (txtCount == null) {
			txtCount = new JTextFieldEx();
			txtCount.setBounds(new Rectangle(87, 43, 137, 21));
			txtCount.addFocusListener(new java.awt.event.FocusAdapter() {
				public void focusLost(java.awt.event.FocusEvent e) {
					MainFrame.this.txtCountLostFocus(e);
				}
			});
		}
		return txtCount;
	}


	protected void txtCountLostFocus(FocusEvent e) {
		try{
			Integer.valueOf(this.txtCount.getText().trim());
		}catch (Exception ex){
			JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.InvalidNumberInfo")); //$NON-NLS-1$
			this.txtCount.setText("100"); //$NON-NLS-1$
			this.txtCount.grabFocus();
			
		}
	}


	/**
	 * This method initializes cbxOnError	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbxOnError() {
		if (cbxOnError == null) {
			cbxOnError = new JComboBox();
			cbxOnError.setBounds(new Rectangle(322, 43, 173, 22));
			cbxOnError.addItem("ignore"); //$NON-NLS-1$
			cbxOnError.addItem("exit"); //$NON-NLS-1$
			cbxOnError.addItem("query"); //$NON-NLS-1$
		}
		return cbxOnError;
	}


	/**
	 * This method initializes initPanel	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getInitPanel() {
		if (initPanel == null) {
			initPanel = new JScrollPane();
			initPanel.setBounds(new Rectangle(88, 73, 409, 53));
			initPanel.setViewportView(getTxtInit());
		}
		return initPanel;
	}


	/**
	 * This method initializes txtInit	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtInit() {
		if (txtInit == null) {
			txtInit = new JTextArea();
		}
		return txtInit;
	}


	/**
	 * This method initializes destroyPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getDestroyPane() {
		if (destroyPane == null) {
			destroyPane = new JScrollPane();
			destroyPane.setBounds(new Rectangle(88, 136, 409, 53));
			destroyPane.setViewportView(getTxtDestroy());
		}
		return destroyPane;
	}


	/**
	 * This method initializes txtDestroy	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtDestroy() {
		if (txtDestroy == null) {
			txtDestroy = new JTextArea();
		}
		return txtDestroy;
	}


	/**
	 * This method initializes columnPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getColumnPane() {
		if (columnPane == null) {
			columnPane = new JScrollPane();
			columnPane.setBounds(new Rectangle(88, 205, 409, 200));
			columnPane.setViewportView(getColumnTable());
		}
		return columnPane;
	}


	/**
	 * This method initializes columnTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private ColumnTable getColumnTable() {
		if (columnTable == null) {
			columnTable = new ColumnTable();
			columnTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					
				}
			});
			columnTable.setCellEditor(getTableCellEditor());
			
		}
		return columnTable;
	}
	

	public DefaultCellEditor getTableCellEditor(){		
		JComboBox cbxTT = new JComboBox();
		cbxTT.addItem("String"); //$NON-NLS-1$
		cbxTT.addItem("def"); //$NON-NLS-1$
		DefaultCellEditor cellEditor = new DefaultCellEditor(cbxTT);
		
		return cellEditor;
	}



	/**
	 * This method initializes cbxCloseIDAutoInsert	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getCbxCloseIDAutoInsert() {
		if (cbxCloseIDAutoInsert == null) {
			cbxCloseIDAutoInsert = new JCheckBox();
			cbxCloseIDAutoInsert.setBounds(new Rectangle(248, 19, 223, 13));
			cbxCloseIDAutoInsert.setText(RM.R("label.MainFrame.idEntityCanInsert")); //$NON-NLS-1$
		}
		return cbxCloseIDAutoInsert;
	}

	/**
	 * This method initializes btnAutoAnalyse	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnAutoConfig() {
		if (btnAutoConfig == null) {
			btnAutoConfig = new JTDButton();
			btnAutoConfig.setBounds(new Rectangle(15, 301, 61, 23));
			btnAutoConfig.setText(RM.R("label.MainFrame.Auto")); //$NON-NLS-1$
			btnAutoConfig.setToolTipText(RM.R("label.MainFrame.auto.toolTip")); //$NON-NLS-1$
			btnAutoConfig.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						MainFrame.this.autoConfig();
					} catch (Exception e1) {
						logger.error("getBtnAutoConfig", e1); //$NON-NLS-1$
						JOptionPane.showMessageDialog(MainFrame.this, RM.R("label.MainFrame.error.saveResult") + e1.getMessage()); //$NON-NLS-1$
					} 
				}
			});
		}
		return btnAutoConfig;
	}


    public void autoConfig() throws SAXException, IOException, DocumentException{        
        Table table = this.getCurSelTable();
        if(table != null){
             AutoConfigEngine autoConfigEngine = new AutoConfigEngine();
             TableConfig tableConfig = autoConfigEngine.reverseTable(table);
             dataExchange(tableConfig, true);
             this.getWorkspaceDataCache().addOrRefershTableConfig(tableConfig);
             resetRefValues();
        }
    }
	/**
	 * This method initializes btnAdd	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JTDButton();
			btnAdd.setBounds(new Rectangle(15, 234, 61, 23));
			btnAdd.setText(RM.R("label.MainFrame.add")); //$NON-NLS-1$
			btnAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainFrame.this.addRow();
				}
			});
		}
		return btnAdd;
	}


	protected void addRow() {
		this.getColumnTable().addRow();
	}


	/**
	 * This method initializes btnDel	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnDel() {
		if (btnDel == null) {
			btnDel = new JTDButton();
			btnDel.setBounds(new Rectangle(15, 268, 61, 23));
			btnDel.setText(RM.R("label.MainFrame.delete")); //$NON-NLS-1$
			btnDel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainFrame.this.deleteCurrentItem();
				}
			});
		}
		return btnDel;
	}


	protected void deleteCurrentItem() {
		if(this.getColumnTable().getSelectedRow() != -1){
			int result = JOptionPane.showConfirmDialog(this, RM.R("label.MainFrame.delete.confirm")); //$NON-NLS-1$
			if(result == JOptionPane.OK_OPTION){
				this.getColumnTable().deleteCurrentItem();
			}
		}
	}


	/**
	 * This method initializes btnSave	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JTDButton();
			btnSave.setBounds(new Rectangle(298, 419, 92, 23));
			btnSave.setText(RM.R("label.MainFrame.SaveConfig")); //$NON-NLS-1$
			btnSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						MainFrame.this.saveConfig(e);
					} catch (IOException e1) {
						logger.error("getBtnSave", e1); //$NON-NLS-1$
						JOptionPane.showMessageDialog(MainFrame.this, "保存配置文件时出错! [" + e1.getMessage()); //$NON-NLS-1$
					}
				}
			});
		}
		return btnSave;
	}


	protected void saveConfig(java.awt.event.ActionEvent e) throws IOException {
		if(this.getCurSelTable() != null){
			TableConfig tableConfig = this.getCurTableConfig();
			if(tableConfig == null){
				tableConfig = new TableConfig();
			}
			this.dataExchange(tableConfig, false);
			tableConfig.saveConfig();
		}
	}


	/**
     * This method initializes btnCancel	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JTDButton();
            btnClose.setBounds(new Rectangle(403, 419, 92, 23));
            btnClose.setText(RM.R("label.info.btnClose")); //$NON-NLS-1$
            btnClose.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    MainFrame.this.dispose();
                    System.exit(0);
                }
            });
        }
        return btnClose;
    }

    /**
	 * This method initializes jTabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab(RM.R("label.MainFrame.TestDataConfig.title"), this.getConfigPanel()); //$NON-NLS-1$
			jTabbedPane.addTab(RM.R("label.MainFrame.queryPane.title"), getQueryPane()); //$NON-NLS-1$
//			jTabbedPane.addTab(RM.R("label.MainFrame.dataModelPane.title"), getDataModelPanel());
            
            jTabbedPane.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e) {
                    MainFrame.this.tabbedPaneSelChanged();
                }
                
            });
		}
		return jTabbedPane;
	}

    public void tabbedPaneSelChanged() {
        int selIndex = this.getJTabbedPane().getSelectedIndex();
        switch(selIndex){
        case 0:
            
            break;
        case 1:
            if(StringUtils.isNotEmpty(getWorkspace())){
                //open default connection.                
                this.getQueryPane().initConnectionCombox(this.getTestDataConfig().getAllConnNames());
            }
                
            break;
        default:
                
        }
    }

    public QueryPanel getQueryPane(){
        if(queryPanel == null){
            queryPanel = new QueryPanel();
        }
        return queryPanel;
    }

	/**
	 * This is the default constructor
	 */
	private MainFrame() {        
		super();
		initialize();
		center();
		enableControl(false); 
	}

    private static MainFrame instance = null;
    
    public synchronized static MainFrame getInstance(){
        if(instance == null){
            instance = new MainFrame();
        }
        return instance;
    }

    private String initTitle = "TestDataBuilder"; //$NON-NLS-1$
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        G.initlize();
		this.setSize(750, 550);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon/TDB.png"))); //$NON-NLS-1$
        this.setFont(new Font("宋体", Font.PLAIN, 10)); //$NON-NLS-1$
        this.setContentPane(getJContentPane());
		this.setJMenuBar(new JTDBMenuBar(this));
		this.setTitle(initTitle);
        Helper.getInstance().enableHelpKey(this.getRootPane(), "quickStart.readme"); //$NON-NLS-1$
    }


	public boolean exists(String filePath){
		return new File(filePath).exists();
	}
	
    public void initWorkspace() {
        this.getJMenuBar().setHasWorkspace(this.getWorkspace() != null);
        this.getJTDBToolBar().setHasWorkspace(this.getWorkspace() != null);
        
        if(this.getWorkspace() != null && !Global.isValidWorkspace(this.getWorkspace())){
            JOptionPane.showMessageDialog(this, String.format(RM.R("label.MainFrame.project.invalid"),
                            new Object[]{this.getWorkspace()})); //$NON-NLS-1$ //$NON-NLS-2$
            G.saveLastWorkspace(null);
            RecentProjects.getInstance().removeProjectByPathAndSave(this.getWorkspace());
            G.setWorkspace(null);
        }
        
		if (StringUtils.isEmpty(this.getWorkspace())) {
			try {
				WorkspaceDataCache.getInstance().init(null);
			} catch (BaseException e) {
				logger.error("initWorkspace", e);
			}
			this.setTitle(this.initTitle);
			this.bindToTree(null);
            this.treeSelectChanged(null);            
		} else {
			try {
				if (G.P.getAutoConnWhenProgramStartup()) {
                    initWorkspaceFromDB();
				} else {
                    initWorkspaceFromLocalFile();
				}				
			} catch (Exception e) {
				logger.error("initWorkspaceFromDB or local file", e); //$NON-NLS-1$
				JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.initApp") //$NON-NLS-1$
						+ e.getMessage());
				this.getJLeftTree().setModel(null);
                this.treeSelectChanged(null);
                this.getJMenuBar().setHasWorkspace(false);
			}
		}
	}
    
    public void initWorkspaceFromDB() throws BaseException{
        initWorkspaceDataCache();
        this.refreshDataFromDB();
        this.setTitle(this.getWorkspace() + "[" //$NON-NLS-1$
                        + this.workspaceDataCache.getTestDataConfig().getName()
                        + "]"); //$NON-NLS-1$
    }
    
    public void initWorkspaceFromLocalFile() throws BaseException{
        initWorkspaceDataCache();
        try {
            DatabaseUtil dbUtil = new DatabaseUtil();
            dbUtil.setBasePath(this.getWorkspace());
            Database database = dbUtil.readDBInfoFromBasePath();
            bindToTree(database);
        } catch (IOException e) {
            logger.error("initWorkspaceFromLocalFile", e); //$NON-NLS-1$
            JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.readLocalConfigFile") //$NON-NLS-1$
                    + e.getMessage());
            bindToTree(null);
        }
        this.setTitle(this.getWorkspace() + "[" //$NON-NLS-1$
                        + this.workspaceDataCache.getTestDataConfig().getName()
                        + "]"); //$NON-NLS-1$
    }
    
    protected void initWorkspaceDataCache() throws BaseException{
        this.workspaceDataCache.init(this.getWorkspace());
    }
    
    public void refreshDataFromDB() {
        DatabaseUtil dbUtil = new DatabaseUtil();
        dbUtil.setBasePath(this.getWorkspace());
        resetRefValues();
        
        dbUtil.setConfig(this.getTestDataConfig().getDefDatabaseConfig());
        
        Database database  = null; 
        try {
        	dbUtil.connect();
            database = dbUtil.getDefArchitecture(DatabaseUtil.LEVEL_COLUMN);
            if(G.P.getSaveDbMetaInfo() && database != null){
                dbUtil.writeDBInfo(database);
            }
        } catch (Exception ex) {
           logger.error("MianFrame", ex); //$NON-NLS-1$
           JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.connectDatabase") + ex.getMessage()); //$NON-NLS-1$
           if(G.P.getReadDBInfoFromFileWhenConnError()){
            try {
                database = dbUtil.readDBInfoFromBasePath();
            } catch (IOException e) {
                logger.error("MianFrame", e); //$NON-NLS-1$
                JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.connectDatabase") + e.getMessage()); //$NON-NLS-1$
            }                   
           }
        }
        bindToTree(database);        
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJTDBToolBar(), BorderLayout.NORTH);
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
            jContentPane.add(getMsgLine(), BorderLayout.SOUTH); 
		}
		return jContentPane;
	}

    private JTDBToolBar getJTDBToolBar(){
        if(this.tdbToolBar == null){
            tdbToolBar = new JTDBToolBar(this);
        }
        return tdbToolBar;
    }
    
    public JLabel getMsgLine(){
        if(msgLine == null){
            msgLine = new JLabel(" ");             //$NON-NLS-1$
        }
        return msgLine;
    }
    
	public String getWorkspace() { 
		return G.getWorkspace();
	}

	
//	public Table curSelTable = null;  //  @jve:decl-index=0:

	private JTDButton btnSave = null;

    private JTDButton btnClose = null;

	private JTabbedPane jTabbedPane = null;

    public Table getCurSelTable() {
    	if(this.getDatabase()!= null){
    		return this.getDatabase().getSelTable();
    	}
    	return null;
	}

	public TableConfig getCurTableConfig(){
		TableConfig curTableConfig = null;
		Table curSelTable =this.getCurSelTable(); 
    	if(curSelTable != null){
    		curTableConfig = this.getWorkspaceDataCache().getTableConfigFromList(curSelTable.getTableName());
    	}
    	return curTableConfig;
	}

	public void setCurSelTable(Table selTable) {
		if(this.getDatabase() != null){
			this.getDatabase().setSelTable(selTable);
		}
	}
    
    public void setCurSelDatabaseConfig(DatabaseConfig curSelDatabaseConfig){
        this.getJLeftTree().setCurSelDatabaseConfig(curSelDatabaseConfig);
    }
    
    public DatabaseConfig getCurSelDatabaseConfig(){
        return this.getJLeftTree().getCurSelDatabaseConfig();
    }
    
    //菜单[工具栏]事件
     public void openProject() {
        JFileChooser directoryChooser = new JProjectDirChooser(G.getInstance().P.getWorkspace());        
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String dirPath = directoryChooser.getSelectedFile().getAbsolutePath();
            openProject(dirPath);                    
        }
    }

    public void openProjectInExplorer(){
        if(StringUtils.isNotEmpty(this.getWorkspace())){
//            String OSName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
//            if(OSName.contains("windows")){ //$NON-NLS-1$
//                try {
//                    Runtime.getRuntime().exec("explorer.exe " + this.getWorkspace()); //$NON-NLS-1$
//                } catch (IOException e) {
//                   JOptionPane.showMessageDialog(this, String.format(RM.R("label.MainFrame.error.openProject"),
//                                   new Object[]{this.getWorkspace(), e.getMessage()}));
//                }
//            }else{
//                
//            }
            OpenWorkspaceUtil.openWorkspaceInExplorer(this.getWorkspace(), this);
        }
    }
    
    public void openProject(String projectPath){        
    	setWorkspaceAndUpdateConfig(projectPath);
        this.initWorkspace();
    }

    public void setWorkspaceAndUpdateConfig(String workspace){
    	G.setWorkspaceLastWorkspace(workspace);
    	int sep_index = workspace.lastIndexOf(Global.SEP);
        
        String projectName = workspace.substring(sep_index + 1);
        RecentProjects.getInstance().addProject2RecentList(projectName, workspace);
    }
    
    public void closeProject(){
        int result = JOptionPane.showConfirmDialog(this, RM.R("label.MainFrame.closeProjectConfirm")); //$NON-NLS-1$
        if(result == JOptionPane.OK_OPTION){            
        	G.setWorkspace(null);
        	this.initWorkspace();
        }
    }

    public void createProject(){
        ProjectDialog projectDialog = new ProjectDialog(this);
        projectDialog.setModal(true);
        projectDialog.setVisible(true);
        if(projectDialog.isOK()){
            this.setWorkspaceAndUpdateConfig(projectDialog.getProjectPath());
            
            try {
                initWorkspaceDataCache();
                this.configDBAndInitWorkspace(TestDataConfig.DEFAULT,true);
                this.getJMenuBar().setHasWorkspace(this.getWorkspace() != null);
                this.getJTDBToolBar().setHasWorkspace(this.getWorkspace() != null);
            } catch (Exception ex) {
                logger.error("MainFrame", ex); //$NON-NLS-1$
                JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.connectDatabase") + ex.getMessage()); //$NON-NLS-1$
            } 
        }
    }

    GenerateDataDialog genDialog = null;

    private JTDButton btnClearColumnConfig = null;

    private DataModelPanel dataModelPanel = null;
    
    GenerateDataDialog getGenerateDataDialog() throws BaseException{
        if(genDialog != null && genDialog.isRunInBackground()){
            genDialog.setIsRunInBackground(false);
            return genDialog;
        }else {
            if(genDialog != null){
                genDialog.dispose();
            }
            genDialog = new GenerateDataDialog(this, this.getSortedWorkspaceDataCache());
            genDialog.setModal(true);
        }
        return genDialog;
    }
    
    public void showGeneateDateDialog(){
        try{
            getGenerateDataDialog().setVisible(true);
        }catch(BaseException ex){
            JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.error.e") + ex.getMessage()); //$NON-NLS-1$
        }
    }
    
    public void showAboutDialog(){
        AboutDialog aboutDialog = new AboutDialog(this);
        aboutDialog.setModal(true);
        aboutDialog.setVisible(true);
    }
    
    public void showQueryConsole(){
        this.getJTabbedPane().setSelectedIndex(1);
    }
    
    public void showDataExchangerWindow(){
        JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.unImplementFunction"));        
    }
    
    public void showDataModelView(){
        JOptionPane.showMessageDialog(this, RM.R("label.MainFrame.unImplementFunction")); //$NON-NLS-1$
    }
    
    public void showPreferenceDialog(){
        PreferenceDialog preferenceDialog = new PreferenceDialog(this, G.P);
        preferenceDialog.setModal(true);
        preferenceDialog.setWorkspace(this.getWorkspace());
        preferenceDialog.setVisible(true);
    }

    public void showSysPropertyDialog(){
        AppPropertyDialog appPropertyDialog = new AppPropertyDialog(this,G.getAppProperties());
        appPropertyDialog.setModal(true);
        appPropertyDialog.setVisible(true);
    }
    
    public void showHelp(){
        
    }
    
    public void showUpgradeDialog(){
        UpgradeDialog upgradeDialog = new UpgradeDialog(this);
        upgradeDialog.setVisible(true);
    }
    
    public void showRoleWindow(){
        try{
            if(getWorkspaceDataCache() != null){
                this.getWorkspaceDataCache().initAll();
            }
        }catch(BaseException ex){
            logger.error("MainFrame", ex); //$NON-NLS-1$
        }
        RoleDialog dataGenerateDialog =  new RoleDialog(this,this.getWorkspaceDataCache());
        dataGenerateDialog.setModal(true);
        dataGenerateDialog.setVisible(true);
		resetRefValues();
    }
	
	
    public WorkspaceDataCache getWorkspaceDataCache() {
        return workspaceDataCache;
    }

    public WorkspaceDataCache getSortedWorkspaceDataCache() throws BaseException{
        List<String> sortedTableNames = null;
        Database curSelDB = this.getDatabase();
        if(curSelDB != null){
            sortedTableNames = curSelDB.getSortedTableNames();
            workspaceDataCache.initAll();
            workspaceDataCache.getSortedTableConfig(sortedTableNames);
        }    
        return workspaceDataCache;
    }
    
    public void setWorkspaceDataCache(WorkspaceDataCache workspaceDataCache) {
        this.workspaceDataCache = workspaceDataCache;
    }

    /**
     * This method initializes btnDelAll	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnClearColumnConfig() {
        if (btnClearColumnConfig == null) {
            btnClearColumnConfig = new JTDButton();
            btnClearColumnConfig.setBounds(new Rectangle(15, 337, 61, 23));
            btnClearColumnConfig.setText(RM.R("label.MainFrame.clearAll")); //$NON-NLS-1$
            btnClearColumnConfig.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    MainFrame.this.delAllColumnConfig();
                }
            });
        }
        return btnClearColumnConfig;
    }

    protected void delAllColumnConfig() {
        if(this.getColumnTable().getData() != null 
                        && this.getColumnTable().getData().size() > 0){
            if(JOptionPane.showConfirmDialog(this, RM.R("label.MainFrame.clearAll.confirm")) == JOptionPane.OK_OPTION){ //$NON-NLS-1$
                this.getColumnTable().setData(null);
            }
        }
    }

    /**
     * This method initializes dataModelPanel	
     * 	
     * @return com.testDataBuilder.ui.main.dataModel.DataModelPanel	
     */
    private DataModelPanel getDataModelPanel() {
        if (dataModelPanel == null) {
            dataModelPanel = new DataModelPanel();
        }
        return dataModelPanel;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"

