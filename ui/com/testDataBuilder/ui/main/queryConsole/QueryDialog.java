package com.testDataBuilder.ui.main.queryConsole;

import java.util.List;

import javax.swing.JDialog;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import com.testDataBuilder.ui.core.JTDButton;

public class QueryDialog extends CenterDialog {

    private static final long serialVersionUID = 1L;
    private QueryPanel queryPanel = null;
    private JPanel jContentPane = null;
    private JTDButton btnOK = null;
    private JTDButton btnCancel = null;
    
    /**
     * This is the default constructor
     */
    public QueryDialog(JDialog parent) {
        super(parent);
        initialize();
        this.center();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(568, 420);
        this.setContentPane(getJContentPane());
        this.setTitle("SQL编写器");
    }

    /**
     * This method initializes queryPanel	
     * 	
     * @return com.testDataBuilder.ui.main.queryConsole.QueryPanel	
     */
    private QueryPanel getQueryPanel() {
        if (queryPanel == null) {
            queryPanel = new QueryPanel();
            queryPanel.setBounds(new Rectangle(8, 14, 543, 334));
            queryPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        }
        return queryPanel;
    }
    
    public String getSQLString(){
        return this.getQueryPanel().getSQLString();
    }
    
    public void setSQLString(String sqlString){
        this.getQueryPanel().setSQLString(sqlString);
    }

    public void setQueryType(String queryType){
    	this.getQueryPanel().setQueryType(queryType);
    }
    
    public String getSelConnName(){
        return this.getQueryPanel().getCurConnName();
    }
    
    public void initConnectionCombox(List<String> allConnNames){
        this.getQueryPanel().initConnectionCombox(allConnNames);
    }
    
    public void initConnectionCombox(List<String> allConnNames, Object selObject){
        this.getQueryPanel().initConnectionCombox(allConnNames, selObject);
    }

    /**
     * This method initializes jContentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getQueryPanel(), null);
            jContentPane.add(getBtnOK(), null);
            jContentPane.add(getBtnCancel(), null);
            jContentPane.addComponentListener(new ComponentAdapter(){
                @Override
                public void componentResized(ComponentEvent e) {
                    QueryDialog.this.frameResized();
                }
                
            });
        }
        return jContentPane;
    }

    public static final int space = 10;
    
    protected void frameResized() {
        int width = this.getJContentPane().getWidth();
        int height = this.getJContentPane().getHeight();
        if(width < 400){
            width = 400;
        }
        if(height < 300){
            height = 300;
        }
        int btnY = height - space - this.getBtnOK().getHeight();
        int btnCancelX = width - this.getBtnOK().getWidth() * 2 - space * 2;
        int btnOKX = width - this.getBtnOK().getWidth() - space;
        
        this.getQueryPanel().setBounds(space, space,
                        width - space * 2, height - this.getBtnCancel().getHeight() -  space * 3);
        this.getBtnCancel().setBounds(btnCancelX, btnY, this.getBtnCancel().getWidth(),
                        this.getBtnCancel().getHeight());
        this.getBtnOK().setBounds(btnOKX, btnY, this.getBtnOK().getWidth(),
                        this.getBtnOK().getHeight());
        this.getQueryPanel().updateUI();
        this.getBtnCancel().updateUI();
        this.getBtnOK().updateUI();
    }

    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(477, 358, 73, 24));
            btnOK.setText(RM.R("label.info.btnOK"));
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    QueryDialog.this.onOK();
                }
            });
        }
        return btnOK;
    }

    boolean isOK = false;
    
    protected void onOK() {
        isOK = true;
        this.dispose();
    }

    /**
     * This method initializes btnCancel	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JTDButton();
            btnCancel.setBounds(new Rectangle(389, 358, 73, 24));
            btnCancel.setText(RM.R("label.info.btnCancel"));
            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    QueryDialog.this.dispose();
                }
            });
        }
        return btnCancel;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean isOK) {
        this.isOK = isOK;
    }

    
}  //  @jve:decl-index=0:visual-constraint="10,10"
