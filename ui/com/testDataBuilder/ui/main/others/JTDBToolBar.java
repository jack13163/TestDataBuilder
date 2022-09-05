package com.testDataBuilder.ui.main.others;

import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.ui.main.MainFrame;
import java.awt.Dimension;

public class JTDBToolBar extends JToolBar {

    static Logger logger = Logger.getLogger(JTDBToolBar.class);
    
    public static final String ICON_DIR = "/icon/"; //$NON-NLS-1$
    
    private MainFrame parent;
    
    private JTDButton btnConfigDefDB = null;
    
    private JTDButton btnNewConnection = null;
    
    private JTDButton btnOpenProject = null;
    
    private JTDButton btnNewProject = null;
    
    private JTDButton btnRole = null; 
    
    private JTDButton btnGenerate = null;

    private JTDButton btnPreferences = null;

    private JTDButton btnHelp = null;
    
  
    public JTDBToolBar(MainFrame parent){
        super();
        this.putClientProperty("JToolBar.isRollover",Boolean.TRUE); //$NON-NLS-1$
        this.parent = parent;
        this.initialize();        
        setToolbarState();
    }
		
    public boolean hasWorkspace = false;
    
    public void setHasWorkspace(boolean hasWorkspace){
        if(this.hasWorkspace != hasWorkspace){
            this.hasWorkspace = hasWorkspace;
            this.setToolbarState();
        }
    }
    
    private void setToolbarState(){
       this.getBtnConfigDefDB().setEnabled(hasWorkspace);
       this.getBtnNewConnection().setEnabled(hasWorkspace);
       this.getBtnRole().setEnabled(hasWorkspace);
       this.getBtnGenerate().setEnabled(hasWorkspace);       
    }
    
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(329, 23));
        
        this.add(getBtnNewProject());
        this.add(getBtnOpenProject());
        this.addSeparator();
        
        this.add(getBtnConfigDefDB());
        this.add(getBtnNewConnection());
        this.addSeparator();
        
        this.add(getBtnRole());
        this.add(getBtnGenerate());
        this.addSeparator();
        
        this.add(getBtnPreferences());
        this.addSeparator();
        
        this.add(getBtnHelp());
    		
    }

    private JTDButton getBtnConfigDefDB(){
        if(btnConfigDefDB == null){
//            Icon icon = new ImageIcon();
            btnConfigDefDB = new IconButton(ICON_DIR +"database_edit.png"); //$NON-NLS-1$
            btnConfigDefDB.setToolTipText(R("label.JTDBMenuBar.menu.database.defaultConn")); //$NON-NLS-1$
            btnConfigDefDB.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        parent.configDBAndInitWorkspace(TestDataConfig.DEFAULT,false);
                    } catch (Exception e1) {
                        logger.error("Main", e1); //$NON-NLS-1$
                        JOptionPane.showMessageDialog(parent,  
                        RM.R("label.JTDBToolBar.error.parseConfigFile") + e1.getMessage()); //$NON-NLS-1$
                    }
                }
            });
        }
        return btnConfigDefDB;
    }
    
    private JTDButton getBtnNewConnection(){
        if(btnNewConnection == null){
            btnNewConnection = new IconButton(ICON_DIR + "database_add.png"); //$NON-NLS-1$
            btnNewConnection.setToolTipText(R("label.JTDBMenuBar.menu.database.newConn")); //$NON-NLS-1$
            btnNewConnection.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                     parent.addConnectConfig();
                }
            });
        }
        return btnNewConnection;
    }
    

    private JTDButton getBtnRole(){
        if(btnRole == null){
            btnRole = new IconButton(ICON_DIR + "database_role.png"); //$NON-NLS-1$
            btnRole.setToolTipText(R("label.JTDBMenuBar.menu.window.role")); //$NON-NLS-1$
            btnRole.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    parent.showRoleWindow();
                }
            });
        }
        return btnRole;
    }
        
    private JTDButton getBtnGenerate(){
        if(btnGenerate == null){
            btnGenerate = new IconButton(ICON_DIR + "database_go.png"); //$NON-NLS-1$
            btnGenerate.setToolTipText(R("label.JTDBMenuBar.menu.role.generateData")); //$NON-NLS-1$
            btnGenerate.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    parent.showGeneateDateDialog();
                }
            });
        }
        return btnGenerate;
    }

    private JTDButton getBtnOpenProject(){
        if(btnOpenProject == null){                     
            btnOpenProject = new IconButton(ICON_DIR + "open_project.png"); //$NON-NLS-1$
            btnOpenProject.setToolTipText(RM.R("label.JTDBToolBar.openProject.tooltip")); //$NON-NLS-1$
            btnOpenProject.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    parent.openProject();
                }
            });
        }
        return btnOpenProject;
    }
    
    private JTDButton getBtnNewProject(){
        if(btnNewProject == null){
            btnNewProject = new IconButton(ICON_DIR + "newProject.png"); //$NON-NLS-1$
            btnNewProject.setToolTipText(RM.R("label.JTDBToolBar.newProject.tooltip")); //$NON-NLS-1$
            btnNewProject.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    parent.createProject();
                }
            });
            
        }
        return btnNewProject;
    }
    
    public static String R(String key){
        return RM.R(key);
    }


    /**
     * This method initializes btnPreferences	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnPreferences() {
        if (btnPreferences == null) {
            btnPreferences = new IconButton(ICON_DIR + "preferences.png"); //$NON-NLS-1$
            btnPreferences.setToolTipText(RM.R("label.JTDBToolBar.preferences.tooltip")); //$NON-NLS-1$
            btnPreferences.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JTDBToolBar.this.parent.showPreferenceDialog();
                }
            });
        }
        return btnPreferences;
    }


    /**
     * This method initializes btnHelp	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnHelp() {
        if (btnHelp == null) {
            btnHelp = new  IconButton(ICON_DIR + "help.png"); //$NON-NLS-1$
            btnHelp.setToolTipText(RM.R("label.JTDBToolBar.help.tooltip")); //$NON-NLS-1$
            btnHelp.addActionListener(Helper.getInstance().getHelpListener());
        }
        return btnHelp;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
