package com.testDataBuilder.ui.main.queryConsole;

import javax.swing.JComboBox;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.testDataBuilder.ui.core.JTDButton;

import com.testDataBuilder.resources.RM;

import java.awt.GridBagLayout;

public class SQLQueryToolBar extends JToolBar {

    QueryPanel parent = null;
    private JTDButton btnExec = null;
    private JTDButton btnOpen = null;
    private JTDButton btnSave = null;
    
    private ConnectionComboBox comboxConnection = null;  //  @jve:decl-index=0:visual-constraint="-72,113"
    private JComboBox comboxBatchSeparator = null;
    
    public SQLQueryToolBar(QueryPanel parent) {
        this.parent = parent;
        initialize();        
    }
		

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(405, 24));
        this.add(getComboxConnection());
        this.add(getBtnExec());
        this.add(getBtnOpen());
        this.add(getBtnSave());
        this.add(getComboxBatchSeparator());
    }

    

    /**
     * This method initializes btnExec	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnExec() {
        if (btnExec == null) {
            btnExec = new JTDButton(RM.R("label.info.btnExecute"));
            btnExec.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    SQLQueryToolBar.this.parent.execSQL();
                }
            });
        }
        return btnExec;
    }


    /**
     * This method initializes btnOpen	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOpen() {
        if (btnOpen == null) {
            btnOpen = new JTDButton(RM.R("label.info.btnOpen"));
            btnOpen.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    SQLQueryToolBar.this.parent.openScript();
                }
            });
        }
        return btnOpen;
    }


    /**
     * This method initializes btnSave	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JTDButton(RM.R("label.info.btnSave"));
        }
        return btnSave;
    }


    /**
     * This method initializes comboxConnection	
     * 	
     * @return javax.swing.JComboBox	
     */
    public ConnectionComboBox getComboxConnection() {
        if (comboxConnection == null) {
            comboxConnection = new ConnectionComboBox();                    
        }
        return comboxConnection;
    }


    /**
     * This method initializes comboxBatchSeparator	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getComboxBatchSeparator() {
        if (comboxBatchSeparator == null) {
            comboxBatchSeparator = new JComboBox();
            comboxBatchSeparator.setEditable(true);
            comboxBatchSeparator.addItem(";");
            comboxBatchSeparator.addItem("go");
        }
        return comboxBatchSeparator;
    }    
    
    public String getBatchSeparator(){
          String sep = (String) getComboxBatchSeparator().getSelectedItem();
          return sep;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
