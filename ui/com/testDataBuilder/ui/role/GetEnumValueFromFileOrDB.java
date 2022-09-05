package com.testDataBuilder.ui.role;

import com.testDataBuilder.core.role.EnumList;
import com.testDataBuilder.core.role.EnumObj;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.help.Helper;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.ButtonGroup;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JRadioButton;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class GetEnumValueFromFileOrDB extends CenterDialog {

    private JPanel jContentPane = null;
    private JPanel innerPanel = null;
    private JTDButton btnSelFromFile = null;
    private JScrollPane jEnumScrollPane = null;
    private JEnumTable jEnumTable = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel jFromFilePanel = null;
    private JPanel jFromDBPanel = null;
    private JTDButton btnOK = null;
    private JTDButton btnOK1 = null;
    private JTDButton btnOK2 = null;
    private JTDButton btnCancel = null;
    private JTDButton btnFromDB = null;
    
    private EnumList enumList = null;
    private JTDButton btnClearAll = null;
    private JPanel jInnerPanel1 = null;
    private JPanel innerPanel2 = null;
    private JLabel jLabel = null;
    private JTextField txtFilePath = null;
    private JTDButton btnSelFile = null;
    private JRadioButton radioLineFeed = null;
    private JRadioButton radioComma = null;
    private JRadioButton radioSemicolon = null;
    private JRadioButton radioOther = null;
    private ButtonGroup  radioGroup = null;  //  @jve:decl-index=0:
    private JTextField txtOther = null;
    /**
     * This method initializes 
     * 
     */
    public GetEnumValueFromFileOrDB() {
    	super();
    	initialize();
    }

    
    public GetEnumValueFromFileOrDB(Dialog arg0, EnumList enumList) throws HeadlessException {
        super(arg0);
        this.enumList = enumList;
        initialize();
    }


    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(444, 354));
        this.setResizable(false);
        this.setContentPane(getJTabbedPane());
        this.setTitle(RM.R("label.GetEnumValueFromFileOrDB.title")); //$NON-NLS-1$
        this.center();
        Helper.getInstance().enableHelpKey(this.getRootPane(), "detail.role.userRole.getEnumValueFromFileOrSQL"); //$NON-NLS-1$
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
            jContentPane.add(getInnerPanel(), null);            
            jContentPane.add(getBtnCancel(), null);
            jContentPane.add(getBtnOK(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes innerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getInnerPanel() {
        if (innerPanel == null) {
            innerPanel = new JPanel();
            innerPanel.setLayout(null);
            innerPanel.setBounds(new Rectangle(12, 12, 408, 248));
            innerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            innerPanel.add(getBtnSelFromFile(), null);
            innerPanel.add(getJEnumScrollPane(), null);
            innerPanel.add(getBtnFromDB(), null);
            innerPanel.add(getBtnClearAll(), null);
        }
        return innerPanel;
    }

    /**
     * This method initializes btnSelFromFile	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSelFromFile() {
        if (btnSelFromFile == null) {
            btnSelFromFile = new JTDButton();
            btnSelFromFile.setBounds(new Rectangle(313, 15, 78, 22));
            btnSelFromFile.setText(RM.R("label.GetEnumValueFromFileOrDB.fromFile")); //$NON-NLS-1$
            btnSelFromFile.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.showFromFilePanel();
                }
            });           
        }
        return btnSelFromFile;
    }

    protected void showFromFilePanel() {
        this.getJTabbedPane().setSelectedIndex(1);
    }

    /**
     * This method initializes jEnumScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJEnumScrollPane() {
        if (jEnumScrollPane == null) {
            jEnumScrollPane = new JScrollPane();
            jEnumScrollPane.setBounds(new Rectangle(16, 16, 286, 210));
            jEnumScrollPane.setViewportView(getJEnumTable());
        }
        return jEnumScrollPane;
    }

    /**
     * This method initializes jEnumTable	
     * 	
     * @return com.testDataBuilder.ui.role.JEnumTable	
     */
    private JEnumTable getJEnumTable() {
        if (jEnumTable == null) {
            jEnumTable = new JEnumTable();
            jEnumTable.setData(this.enumList);
        }
        return jEnumTable;
    }

    /**
     * This method initializes jTabbedPane1	
     * 	
     * @return javax.swing.JTabbedPane	
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab(RM.R("label.GetEnumValueFromFileOrDB.enumValue"), null, getJContentPane(), null); //$NON-NLS-1$
            jTabbedPane.addTab(RM.R("label.GetEnumValueFromFileOrDB.fromFile"), null, getJFromFilePanel(), null); //$NON-NLS-1$
            jTabbedPane.addTab(RM.R("label.GetEnumValueFromFileOrDB.fromDB"), null, getJFromDBPanel(), null); //$NON-NLS-1$
        }
        return jTabbedPane;
    }

    /**
     * This method initializes jFromFilePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJFromFilePanel() {
        if (jFromFilePanel == null) {
            jFromFilePanel = new JPanel();
            jFromFilePanel.setLayout(null);
            jFromFilePanel.add(getJInnerPanel1(), null);
            jFromFilePanel.add(getBtnOK1(), null);
        }
        return jFromFilePanel;
    }

    /**
     * This method initializes jFromDBPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJFromDBPanel() {
        if (jFromDBPanel == null) {
            jFromDBPanel = new JPanel();
            jFromDBPanel.setLayout(null);
            jFromDBPanel.add(getInnerPanel2(), null);
            jFromDBPanel.add(getBtnOK2(), null);
        }
        return jFromDBPanel;
    }

    /**
     * This method initializes btnOK    
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(279, 269, 65, 20));
            btnOK.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.onOK();
                }
            });
        }
        return btnOK;
    }

    /**
     * This method initializes btnOK    
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnOK1() {
        if (btnOK1 == null) {
            btnOK1 = new JTDButton();
            btnOK1.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK1.setBounds(new Rectangle(354, 269, 65, 20));
            btnOK1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.getEnumFromFile();
                }
            });
        }
        return btnOK1;
    }

    protected void getEnumFromFile() {
        String filePath = this.getTxtFilePath().getText();
        if(StringUtils.isEmpty(filePath)){
            JOptionPane.showMessageDialog(this, RM.R("label.GetEnumValueFromFileOrDB.selFileAlter")); //$NON-NLS-1$
            this.getTxtFilePath().grabFocus();
            return;
        }
        
        String sep = this.getSep();
        if(sep == null || sep.equals("")){ //$NON-NLS-1$
            JOptionPane.showMessageDialog(this, RM.R("label.GetEnumValueFromFileOrDB.inputSeparatorAlter")); //$NON-NLS-1$
            this.getTxtOther().grabFocus();
            return;
        }
        
        String content = ""; //$NON-NLS-1$
        try {
            content = FileUtils.readFileToString(new File(filePath));
        } catch (IOException e) {
           JOptionPane.showMessageDialog(this, "读取文件[" + filePath + "]失败。" + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
           return;
        }
        
        String[] items = content.split(sep);
        for(String item: items){
            item = item.trim();
            if(StringUtils.isNotEmpty(item)){
                this.getEnumList().add(new EnumObj(item,1));
            }
        }
        this.getJTabbedPane().setSelectedIndex(0);
        this.getJEnumTable().updateUI();
    }


    /**
     * This method initializes btnOK    
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnOK2() {
        if (btnOK2 == null) {
            btnOK2 = new JTDButton();
            btnOK2.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK2.setBounds(new Rectangle(354, 269, 65, 20));
            btnOK2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    
                }
            });
        }
        return btnOK2;
    }

    private boolean isOK = false;
    
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
            btnCancel.setBounds(new Rectangle(354, 269, 65, 20));
            btnCancel.setText(RM.R("label.info.btnCancel")); //$NON-NLS-1$
            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                   GetEnumValueFromFileOrDB.this.dispose();
                }
            });
        }
        return btnCancel;
    }

    /**
     * This method initializes btnFromDB	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnFromDB() {
        if (btnFromDB == null) {
            btnFromDB = new JTDButton();
            btnFromDB.setBounds(new Rectangle(313, 44, 79, 22));
            btnFromDB.setText(RM.R("label.GetEnumValueFromFileOrDB.btnFromDB")); //$NON-NLS-1$
            btnFromDB.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.showFromDBPanel();
                }
            });
        }
        return btnFromDB;
    }

    protected void showFromDBPanel() {
        this.getJTabbedPane().setSelectedIndex(2);
    }


    public EnumList getEnumList() {
        return enumList;
    }


    public void setEnumList(EnumList enumList) {
        this.enumList = enumList;
    }


    /**
     * This method initializes btnClearAll	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnClearAll() {
        if (btnClearAll == null) {
            btnClearAll = new JTDButton();
            btnClearAll.setBounds(new Rectangle(317, 198, 73, 24));
            btnClearAll.setText(RM.R("label.GetEnumValueFromFileOrDB.btnClear")); //$NON-NLS-1$
            btnClearAll.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.clearAll();
                }
            });
        }
        return btnClearAll;
    }


    protected void clearAll() {
        this.getJEnumTable().getData().clear();
        this.getJEnumTable().updateUI();
    }


    /**
     * This method initializes jInnerPanel1	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJInnerPanel1() {
        if (jInnerPanel1 == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(15, 12, 57, 23));
            jLabel.setText(RM.R("label.GetEnumValueFromFileOrDB.file")); //$NON-NLS-1$
            jInnerPanel1 = new JPanel();
            jInnerPanel1.setLayout(null);
            jInnerPanel1.setBounds(new Rectangle(12, 12, 408, 248));
            jInnerPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            jInnerPanel1.add(jLabel, null);
            jInnerPanel1.add(getTxtFilePath(), null);
            jInnerPanel1.add(getBtnSelFile(), null);
            jInnerPanel1.add(getRadioLineFeed(), null);
            jInnerPanel1.add(getRadioComma(), null);
            jInnerPanel1.add(getRadioSemicolon(), null);
            jInnerPanel1.add(getRadioOther(), null);
            jInnerPanel1.add(getTxtOther(), null);
            
            this.getRadioGroup().add(getRadioLineFeed());
            this.getRadioGroup().add(getRadioComma());
            this.getRadioGroup().add(getRadioSemicolon());
            this.getRadioGroup().add(getRadioOther());
        }
        return jInnerPanel1;
    }


    /**
     * This method initializes innerPanel2	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getInnerPanel2() {
        if (innerPanel2 == null) {
            innerPanel2 = new JPanel();
            innerPanel2.setLayout(new GridBagLayout());
            innerPanel2.setBounds(new Rectangle(12, 12, 408, 248));
            innerPanel2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        }
        return innerPanel2;
    }


    /**
     * This method initializes txtFilePath	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtFilePath() {
        if (txtFilePath == null) {
            txtFilePath = new JTextField();
            txtFilePath.setBounds(new Rectangle(76, 13, 291, 21));
        }
        return txtFilePath;
    }


    /**
     * This method initializes btnSelFile	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSelFile() {
        if (btnSelFile == null) {
            btnSelFile = new JTDButton();
            btnSelFile.setBounds(new Rectangle(376, 13, 21, 20));
            btnSelFile.setText("..."); //$NON-NLS-1$
            btnSelFile.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.selTxtFile();
                }
            });
        }
        return btnSelFile;
    }


    protected void selTxtFile() {
        
        JFileChooser txtFileChooser = new JFileChooser(this.getTxtFilePath().getText());  
        
        FileFilter filter = 
            new FileFilter(){
                @Override
                public boolean accept(File f) {
                    return f.getAbsolutePath().endsWith(".txt"); //$NON-NLS-1$
                }
                @Override
                public String getDescription() {                    
                    return RM.R("label.GetEnumValueFromFileOrDB.txtFile"); //$NON-NLS-1$
                }            
        }; 
        txtFileChooser.setFileFilter(filter);
        txtFileChooser.setDialogTitle(RM.R("label.GetEnumValueFromFileOrDB.FileChooser.title")); //$NON-NLS-1$
        txtFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (txtFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String txtFile = txtFileChooser.getSelectedFile().getAbsolutePath();
            this.getTxtFilePath().setText(txtFile);
        }
    }


    /**
     * This method initializes radioLineFeed	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRadioLineFeed() {
        if (radioLineFeed == null) {
            radioLineFeed = new JRadioButton();
            radioLineFeed.setBounds(new Rectangle(12, 49, 106, 25));
            radioLineFeed.setText(RM.R("label.GetEnumValueFromFileOrDB.enterOrNewLine")); //$NON-NLS-1$
            radioLineFeed.setSelected(true);
        }
        return radioLineFeed;
    }


    private ButtonGroup getRadioGroup(){
        if(radioGroup == null){
            radioGroup = new ButtonGroup();
        }
        return radioGroup;
    }
    /**
     * This method initializes radioComma	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRadioComma() {
        if (radioComma == null) {
            radioComma = new JRadioButton();
            radioComma.setBounds(new Rectangle(125, 52, 97, 19));
            radioComma.setText(RM.R("label.GetEnumValueFromFileOrDB.comma")); //$NON-NLS-1$
        }
        return radioComma;
    }


    /**
     * This method initializes radioSemicolon	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRadioSemicolon() {
        if (radioSemicolon == null) {
            radioSemicolon = new JRadioButton();
            radioSemicolon.setBounds(new Rectangle(226, 52, 130, 19));
            radioSemicolon.setText(RM.R("label.GetEnumValueFromFileOrDB.semicolon")); //$NON-NLS-1$
        }
        return radioSemicolon;
    }


    /**
     * This method initializes radioOther	
     * 	
     * @return javax.swing.JRadioButton	
     */
    private JRadioButton getRadioOther() {
        if (radioOther == null) {
            radioOther = new JRadioButton();
            radioOther.setBounds(new Rectangle(13, 74, 66, 25));
            radioOther.setText(RM.R("label.GetEnumValueFromFileOrDB.other")); //$NON-NLS-1$
            radioOther.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GetEnumValueFromFileOrDB.this.onOtherRadio();
                }
            });
        }
        return radioOther;
    }


    protected void onOtherRadio() {
        this.getTxtOther().setEnabled(this.getRadioOther().isSelected());
    }

    protected String getSep(){
        String sep = null;
        if(this.getRadioLineFeed().isSelected()){
            sep = System.getProperty("line.separator"); //$NON-NLS-1$
        }else if(this.getRadioComma().isSelected()){
            sep = ","; //$NON-NLS-1$
        }else if(this.getRadioSemicolon().isSelected()){
            sep = ";"; //$NON-NLS-1$
        }else if(this.getRadioOther().isSelected()){
            sep = this.getTxtOther().getText();
        }
        return sep;
    }
    
    /**
     * This method initializes txtOther	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtOther() {
        if (txtOther == null) {
            txtOther = new JTextField();
            txtOther.setBounds(new Rectangle(106, 78, 170, 20));
            txtOther.setEnabled(false);
        }
        return txtOther;
    }


    public boolean isOK() {
        return isOK;
    }


    public void setOK(boolean isOK) {
        this.isOK = isOK;
    }


}  //  @jve:decl-index=0:visual-constraint="10,10"
