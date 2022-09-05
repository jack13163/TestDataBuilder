package com.testDataBuilder.ui.role;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.help.Helper;

public class JRoleMenuBar extends JMenuBar {

    RoleDialog parent = null;
    
    public JRoleMenuBar(RoleDialog parent){
        super();
        this.parent = parent;
        
        this.add(getRoleMenu());
        this.add(getHelp());
    }
    
    JMenu roleMenu = null;
    
    JMenu helpMenu = null;
    
    public JMenu getRoleMenu(){
        if(roleMenu == null){
            roleMenu = new JMenu();
            roleMenu.setText(RM.R("label.JRoleMenuBar.title")); //$NON-NLS-1$
            
            roleMenu.add(this.getNewRoleItem());
            roleMenu.add(this.getDeleteSelRoleItem());
            
            roleMenu.addSeparator();
            roleMenu.add(getPreviewGenItem());
            
            roleMenu.addSeparator();
            roleMenu.add(getExitItem());
            
        }
        return roleMenu;
    }
    
    public JMenu getHelp(){
        if(helpMenu == null){
            helpMenu = new JMenu();
            helpMenu.setText(RM.R("label.JRoleMenuBar.help"));             //$NON-NLS-1$
            helpMenu.add(this.getHelpContentItem());
            helpMenu.add(this.getAboutItem());
        }
        return helpMenu;
    }
    
    protected JPopupMenu getTablePopupMenu(){
        JPopupMenu root = new JPopupMenu("TablePopupMenu"); //$NON-NLS-1$

        root.add(this.getNewRoleItem());     
        root.add(this.getDeleteAllRoleItem());

        root.addSeparator();
        root.add(getPreviewGenItem());
        
        return root;
    }
    

    protected JPopupMenu getRolePopupMenu(){
        JPopupMenu root = new JPopupMenu("roleMenu"); //$NON-NLS-1$

        root.add(this.getNewRoleItem());     
        root.add(this.getDeleteSelRoleItem());
        
        
        return root;
    }
    
    public JMenuItem getNewRoleItem(){
        JMenuItem addNewItem = new JMenuItem(RM.R("label.JRoleMenuBar.addNewRole")); //$NON-NLS-1$
        addNewItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JRoleMenuBar.this.parent.addRole(e);
                
            }
        });
        return addNewItem;
    }
    
    public JMenuItem getDeleteAllRoleItem(){
        JMenuItem deleteAllItem = new JMenuItem(RM.R("label.JRoleMenuBar.deleteAllRole")); //$NON-NLS-1$
        deleteAllItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JRoleMenuBar.this.parent.deleteAll();              
            }
        });
        return deleteAllItem;
    }
    
    public JMenuItem getDeleteSelRoleItem(){
        JMenuItem deleteSelItem = new JMenuItem(RM.R("label.JRoleMenuBar.deleteSelRole")); //$NON-NLS-1$
        deleteSelItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JRoleMenuBar.this.parent.deleteSelRole();
            }
        });
        return deleteSelItem;
    }
    
    /**
     * 根据当前配置预览生成测试数据.
     * @return
     */
    public JMenuItem getPreviewGenItem(){
    	JMenuItem previewGenItem = new JMenuItem(RM.R("label.JRoleMenuBar.previewGen")); //$NON-NLS-1$
        previewGenItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JRoleMenuBar.this.parent.genToPreview();
            }
        });
        return previewGenItem;
    }
    
    public JMenuItem getExitItem(){
        JMenuItem exitItem = new JMenuItem(RM.R("label.JRoleMenuBar.exit")); //$NON-NLS-1$
        exitItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JRoleMenuBar.this.parent.dispose();
            }
        });
        return exitItem;
    }
    
    public JMenuItem getHelpContentItem(){
        JMenuItem helpContentItem = new JMenuItem(RM.R("label.JRoleMenuBar.helpContent")); //$NON-NLS-1$
        helpContentItem.addActionListener(Helper.getInstance().getHelpListener());
        return helpContentItem;
    }
    
    public JMenuItem getAboutItem(){
        JMenuItem aboutItem = new JMenuItem(RM.R("label.JRoleMenuBar.about")); //$NON-NLS-1$
        aboutItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(JRoleMenuBar.this.parent, 
                                RM.R("label.JRoleMenuBar.copyright"), RM.R("label.JRoleMenuBar.copyright.title") //$NON-NLS-1$ //$NON-NLS-2$
                                , JOptionPane.OK_OPTION);
            }
        });
        return aboutItem;
    }
    
    
}
