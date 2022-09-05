package com.testDataBuilder.ui.main.others;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.ui.main.MainFrame;
import com.testDataBuilder.ui.project.ProjectInfo;
import com.testDataBuilder.ui.project.RecentProjects;


public class JTDBMenuBar extends JMenuBar {

    static Logger logger = Logger.getLogger(JTDBMenuBar.class);
    
	private MainFrame parent;
    
    private JMenu projectMenu;
    private JMenu databaseMenu;
    private JMenu roleMenu;
    private JMenu windowMenu;    
    private JMenu helpMenu;
    
    public boolean hasWorkspace = false;
    
    public void setHasWorkspace(boolean hasWorkspace){
        if(this.hasWorkspace != hasWorkspace){
            this.hasWorkspace = hasWorkspace;
            this.setMenuState();
        }
    }
    
    private void setMenuState(){
        this.getCloseProjectItem().setEnabled(hasWorkspace);
        this.getOpenProjectInExplorerItem().setEnabled(hasWorkspace);
        this.getDefaultConnectionItem().setEnabled(hasWorkspace);
        this.getAddConnectionMenuItem().setEnabled(hasWorkspace);
        this.getConfigMenuItem().setEnabled(hasWorkspace);
        this.getConnectMenuItem().setEnabled(hasWorkspace);
        this.getDisConnectItem().setEnabled(hasWorkspace);
        this.getConnectAllMenuItem().setEnabled(hasWorkspace);
        this.getDisconnectAllMenuItem().setEnabled(hasWorkspace);
        this.getRoleWindowItem().setEnabled(hasWorkspace);
        this.getGenerateItem().setEnabled(hasWorkspace);
    }
    
    public JTDBMenuBar(MainFrame parent){
        this.parent = parent;     
        this.add(getProjectMenu());
        this.add(getDatabaseMenu());
        this.add(getRoleMenu());
        this.add(getWindowMenu());
        this.add(getHelpMenu());
        setMenuState();
    }
    
    public JMenu getHelpMenu(){
        if(helpMenu == null){
            helpMenu = new JMenu(R("label.JTDBMenuBar.menu.help"));
            
            //帮助主题
            JMenuItem subjectItem = new JMenuItem(R("label.JTDBMenuBar.menu.help.content"));
//            subjectItem.addActionListener(new ActionListener(){
//                public void actionPerformed(ActionEvent e) {
//                    JTDBMenuBar.this.parent.showHelp();
//                }                
//            });
            subjectItem.addActionListener(Helper.getInstance().getHelpListener());
            
            helpMenu.add(subjectItem);
            
            helpMenu.addSeparator();
            
            JMenuItem autoUpgradeItem = new JMenuItem(R("label.JTDBMenuBar.menu.help.autoUpgrade"));
            autoUpgradeItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showUpgradeDialog();
                }
                
            });
            helpMenu.add(autoUpgradeItem);
            
            helpMenu.addSeparator();
            //关于
            JMenuItem aboutItem = new JMenuItem(R("label.JTDBMenuBar.menu.help.about"));
            aboutItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                   JTDBMenuBar.this.parent.showAboutDialog();
                }
            });
            helpMenu.add(aboutItem);
        }
        return helpMenu;
    }
    
    private JMenu getWindowMenu(){
        if(windowMenu == null){
            windowMenu = new JMenu(R("label.JTDBMenuBar.menu.window"));
            
            //QueryConsole            
            windowMenu.add(getQueryConsoleWindowItem());            
            //DataModel            
            windowMenu.add(getDataModelItem());            
            //DataExchanger            
            windowMenu.add(getDataExchangerItem());            
            windowMenu.addSeparator();            
            //Preference            
            windowMenu.add(getPreferenceItem());
            
            windowMenu.addSeparator();     
            windowMenu.add(getSysPropertyItem());
            
        }
        return windowMenu;
    }
    
    public JMenu getRoleMenu(){
        if(roleMenu == null){
            roleMenu = new JMenu(R("label.JTDBMenuBar.menu.role"));
            
            roleMenu.add(getRoleWindowItem());
            roleMenu.addSeparator();
            roleMenu.add(getGenerateItem());
        }
        return roleMenu;
    }
    
    private JMenu getDatabaseMenu(){
    	if(databaseMenu == null){
    		databaseMenu = new JMenu(R("label.JTDBMenuBar.menu.database"));
    		//重新连接           
            databaseMenu.add(getDefaultConnectionItem());
            databaseMenu.addSeparator();
            databaseMenu.add(getAddConnectionMenuItem());
            databaseMenu.add(getConfigMenuItem());
            
            databaseMenu.add(getConnectMenuItem());
            databaseMenu.add(getDisConnectItem());
            
            databaseMenu.addSeparator();
            databaseMenu.add(getConnectAllMenuItem());
            databaseMenu.add(getDisconnectAllMenuItem());
    	}
    	return databaseMenu;
    }
    
    private JMenu getProjectMenu(){
        if(projectMenu == null){
            projectMenu = new JMenu(R("label.JTDBMenuBar.menu.project"));
            projectMenu.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    JTDBMenuBar.this.projectMenuMouseClicked(e);
                }
            });
            
            //新建            
            projectMenu.add(getNewProjectItem());
            //打开           
            projectMenu.add(getOpenProjectItem());
            //关闭            
            projectMenu.add(getCloseProjectItem());             
            //在Explorer文件浏览器中查看工程
            projectMenu.add(getOpenProjectInExplorerItem());

            recentProjectBeginPos = projectMenu.getItemCount();
            
            addRecentProjectMenuItem(projectMenu);
            
            projectMenu.addSeparator();
            
            projectMenu.add(getExitItem());
            
        }
        return projectMenu;
    }

    int recentProjectBeginPos = 0;
    
    protected void projectMenuMouseClicked(MouseEvent e) {
        if(RecentProjects.getInstance().isChanged()){
        	RecentProjects.getInstance().setChanged(false);
            for(int i=recentProjectBeginPos;i <  projectMenu.getItemCount() -2;){
                projectMenu.remove(i);
            }
            addRecentProjectMenuItem(projectMenu);
            projectMenu.updateUI();
        }
    }

    protected void addRecentProjectMenuItem(JMenu menu){
        List<JMenuItem> recentProjectsMenuItems =  getRecentProjectsMenuItems();
        if(recentProjectsMenuItems != null){           
            menu.add(new JPopupMenu.Separator(), recentProjectBeginPos);
            for(int i=0; i<recentProjectsMenuItems.size(); i++ ){
                menu.insert(recentProjectsMenuItems.get(i), i + recentProjectBeginPos + 1);
            }
        }        
    }
    
    public List<JMenuItem> getRecentProjectsMenuItems(){
        List<JMenuItem> recentProjectMenuList = null;
        RecentProjects recentProjects = RecentProjects.getInstance();
        if(recentProjects != null && recentProjects.getRecentProjectList().size() > 0){
            recentProjectMenuList = new ArrayList<JMenuItem>();
            for(ProjectInfo projectInfo : recentProjects.getRecentProjectList()){
                
                StringBuffer menuText = new StringBuffer(projectInfo.getProjectName());
                menuText.append("  [");
                if(projectInfo.getProjectPath().length() < 30){
                    menuText.append(projectInfo.getProjectPath());
                }else{
                    menuText.append(StringUtils.left(projectInfo.getProjectPath(), 27) + "...");
                }
                menuText.append("]");
                JMenuItem recProjectMenuItem = new JMenuItem(menuText.toString());
                recProjectMenuItem.setToolTipText(projectInfo.getProjectPath());
                recProjectMenuItem.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        JMenuItem menuItem = (JMenuItem)e.getSource(); 
                        JTDBMenuBar.this.parent.openProject(menuItem.getToolTipText());
                    }                    
                });
                recentProjectMenuList.add(recProjectMenuItem);
            }
        }
        
        return recentProjectMenuList;
    }
    
    public JPopupMenu getDBPopupMenu(){
        JPopupMenu root = new JPopupMenu("database");
        
        root.add(getAutoConfigAllMenuItem());
        root.add(getDeleteAllConfigMenuItem());
        
        return root;
    }
    
    public JPopupMenu getConnectionPoolPopupMenu(){
        JPopupMenu root = new JPopupMenu("connPool");
        
        root.add(getAddConnectionMenuItem());
        root.add(getConnectAllMenuItem());  
        root.add(getDisconnectAllMenuItem());
        
        return root;
    }
    
    public JPopupMenu getConnectionPopupMenu(){
        JPopupMenu root = new JPopupMenu("connPool");
        
        root.add(getConfigMenuItem());        
        root.add(getDeleteConfigMenuItem());
        root.add(getConnectMenuItem());
        root.add(getDisConnectItem());
        
        return root;
    }
   
    //[start]menuItem.

    
    //Build
    JMenuItem  generateItem = null;
    public JMenuItem getGenerateItem(){
        if(generateItem == null){
            generateItem = new JMenuItem(R("label.JTDBMenuBar.menu.role.generateData"));
            generateItem.addActionListener(new ActionListener(){
    
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showGeneateDateDialog();
                }
                
            });
        }
        return generateItem;
    }
    
    //project
    JMenuItem newProjectItem = null;
    public JMenuItem getNewProjectItem(){
        if(newProjectItem == null){
            newProjectItem = new JMenuItem(R("label.JTDBMenuBar.menu.project.new"));
            newProjectItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.createProject();
                }
                
            });
        }
        return newProjectItem;
    }
    
    JMenuItem openProjectItem = null;
    public JMenuItem getOpenProjectItem(){
        if(openProjectItem == null){
            openProjectItem = new JMenuItem(R("label.JTDBMenuBar.menu.project.open"));
            openProjectItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.openProject();
                }                
            });
        }
        return openProjectItem;
    }
    
    JMenuItem closeProjectItem = null;
    public JMenuItem getCloseProjectItem(){
        if(closeProjectItem == null){
            closeProjectItem = new JMenuItem(R("label.JTDBMenuBar.menu.project.close"));
            closeProjectItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.closeProject();
                }
            });           
        }
        return closeProjectItem;
    }
    
    JMenuItem openProjectInExplorerItem = null;
    public JMenuItem getOpenProjectInExplorerItem(){
        if(openProjectInExplorerItem == null){
            openProjectInExplorerItem = new JMenuItem(R("label.JTDBMenuBar.menu.project.viewInExplorer"));
            openProjectInExplorerItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.openProjectInExplorer();
                }
                
            });
        }
        return openProjectInExplorerItem;
    }
    
    JMenuItem exitItem = null;
    public JMenuItem getExitItem(){
        if(exitItem == null){
            exitItem = new JMenuItem(R("label.JTDBMenuBar.menu.project.exit"));
            exitItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                    JTDBMenuBar.this.parent.dispose();
                    System.exit(0);
                }                
            });
        }
        return exitItem;
    }
    
    //database
    JMenuItem configureDefaultConnectionItem = null;
    public JMenuItem getDefaultConnectionItem(){
        if(configureDefaultConnectionItem == null){
            configureDefaultConnectionItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.defaultConn"));
            configureDefaultConnectionItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        JTDBMenuBar.this.parent.configDBAndInitWorkspace(TestDataConfig.DEFAULT,false);
                    } catch (Exception ex) {
                         logger.error("MainFrame", ex);
                         JOptionPane.showMessageDialog(JTDBMenuBar.this.parent, "连接数据库时出错!" + ex.getMessage());
                    }
                }                
            });
        }
        return configureDefaultConnectionItem;
    }
    
    JMenuItem addConnectionMenuItem = null;
    public JMenuItem getAddConnectionMenuItem(){
        if(addConnectionMenuItem == null){
            addConnectionMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.newConn"));
            addConnectionMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.addConnectConfig();
                }            
            }); 
        }
        return addConnectionMenuItem;
    }
    
    JMenuItem connectAllMenuItem = null;
    public JMenuItem getConnectAllMenuItem(){
        if(connectAllMenuItem == null){
            connectAllMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.connAll"));
            connectAllMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.connectAll();
                    
                }
                
            });
        }
        return connectAllMenuItem;
    }
    
    JMenuItem disconnectAllMenuItem = null;
    public JMenuItem getDisconnectAllMenuItem(){
        if(disconnectAllMenuItem == null){
            disconnectAllMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.disConnAll"));
            disconnectAllMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.disconnectAll();                
                }            
            });
        }
        return disconnectAllMenuItem;
    }
    
    JMenuItem configMenuItem = null;
    public JMenuItem getConfigMenuItem(){
        if(configMenuItem == null){
            configMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.config"));
            configMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        JTDBMenuBar.this.parent.configAndReconnect();      
                    } catch (Exception ex) {
                        logger.error("MainFrame", ex);
                        JOptionPane.showMessageDialog(JTDBMenuBar.this.parent, "保存配置时出错，" +ex.getMessage());
                    }
                    
                }
                
            });
        }
        return configMenuItem;
    }
    
    JMenuItem deleteConfigMenuItem = null;
    public JMenuItem getDeleteConfigMenuItem(){
        if(deleteConfigMenuItem == null){
            deleteConfigMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.delConn"));
            deleteConfigMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.delelteConfig();
                }
            });
        }
        return deleteConfigMenuItem;
    }
    
    JMenuItem connectMenuItem = null;
    public JMenuItem getConnectMenuItem(){
        if(connectMenuItem  == null){
            connectMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.connect"));
            connectMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.connect();
                    
                }
                
            });
        }
        return connectMenuItem;
    }
    
    JMenuItem disconnectMenuItem = null;
    public JMenuItem getDisConnectItem(){
        if(disconnectMenuItem == null){
            disconnectMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.disConnect"));
            disconnectMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.disconnect();                
                }            
            });
        }
        return disconnectMenuItem;
    }
    
    //config
    JMenuItem autoConfigAllMenuItem = null;
    public JMenuItem getAutoConfigAllMenuItem(){
        if(autoConfigAllMenuItem == null){
            autoConfigAllMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.autoConfigAll"));
            autoConfigAllMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.autoConfigAll();
                    
                }
                
            });
        }
        return autoConfigAllMenuItem;
    }
    
    JMenuItem deleteAllConfigMenuItem = null;
    public JMenuItem getDeleteAllConfigMenuItem(){
        if(deleteAllConfigMenuItem == null){
            deleteAllConfigMenuItem = new JMenuItem(R("label.JTDBMenuBar.menu.database.delAllConfig"));
            deleteAllConfigMenuItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.deleteAllConfig();                
                }            
            });
        }
        return deleteAllConfigMenuItem;
    }
    //window
    JMenuItem roleItem = null;
    public JMenuItem getRoleWindowItem(){
        if(roleItem == null){
            roleItem = new JMenuItem(R("label.JTDBMenuBar.menu.window.role"));
            roleItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showRoleWindow();
                }               
            });
        }
        return roleItem;
    }
    
    JMenuItem queryConsoleWindowItem = null;
    public JMenuItem getQueryConsoleWindowItem(){
        if(queryConsoleWindowItem == null){
            queryConsoleWindowItem = new JMenuItem(R("label.JTDBMenuBar.menu.window.queryConsole"));
            queryConsoleWindowItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                   JTDBMenuBar.this.parent.showQueryConsole();
                }
                
            });
        }
        return queryConsoleWindowItem;
    }
    
    JMenuItem dataModelItem = null;
    public JMenuItem getDataModelItem(){
        if(dataModelItem == null){
            dataModelItem = new JMenuItem(R("label.JTDBMenuBar.menu.window.dataModel"));
            dataModelItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showDataModelView();
                 }
                 
             });
        }
        return dataModelItem;
    }
    
    JMenuItem dataExchangerItem = null;
    public JMenuItem getDataExchangerItem(){
        if(dataExchangerItem == null){
            dataExchangerItem = new JMenuItem(R("label.JTDBMenuBar.menu.window.dataExchange"));
            dataExchangerItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showDataExchangerWindow();
                }
                
            });
        }
        return dataExchangerItem;
    }
    
    JMenuItem preferenceItem = null;
    public JMenuItem getPreferenceItem(){
        if(preferenceItem == null){
            preferenceItem = new JMenuItem(R("label.JTDBMenuBar.menu.window.preferences"));
            preferenceItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showPreferenceDialog();
                }
            });
        }
        return preferenceItem;
    }
    
    JMenuItem sysPropertyItem = null;
    public JMenuItem getSysPropertyItem(){
        if(sysPropertyItem == null){
            sysPropertyItem = new JMenuItem(R("label.JTDBMenuBar.menu.window.sysProperty"));
            sysPropertyItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    JTDBMenuBar.this.parent.showSysPropertyDialog();
                }
            });
        }
        return sysPropertyItem;
    }
    
    //[end]
    
    String R(String key){
        return RM.R(key);
    }
}
