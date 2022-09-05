package com.testDataBuilder.ui.main.others;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.resources.ResourceManager;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.util.AppProperty;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JCheckBox;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;


public class AppPropertyDialog extends CenterDialog {

    AppProperty appProperty = null;
    
    private JPanel jContentPane = null;

    private JPanel jInnerPanel = null;

    private JCheckBox cbxShowConfigDialogOnStart = null;

    private JTDButton btnOK = null;

    private JTDButton btnCancel = null;

    private JLabel labelLocale = null;

    private JLabel jLabel = null;

    private JComboBox comboxLocale = null;

    private JComboBox comboxLookAndFeel = null;
    
    List<LookAndFeelItem> lookAndFeels = null;  //  @jve:decl-index=0:
    
    public List<LookAndFeelItem> getLookAndFeels(){
        if(lookAndFeels== null){
            lookAndFeels = new ArrayList<LookAndFeelItem>(4);
            lookAndFeels.add(new LookAndFeelItem ("label.AppPropertyDialog.lookAndFeel.javaDef", AppProperty.LOOK_AND_FEEL_JAVA_DEF)); //$NON-NLS-1$
            lookAndFeels.add(new LookAndFeelItem ("label.AppPropertyDialog.lookAndFeel.sysDef", AppProperty.LOOK_AND_FEEL_SYS_DEF));             //$NON-NLS-1$
        }
        return lookAndFeels;
    }

    public LookAndFeelItem getLookAndFeelByClassName(String className){
        for(LookAndFeelItem lookAndFeelItem : getLookAndFeels()){
            if(lookAndFeelItem.getClassName().equals(className)){
                return lookAndFeelItem;
            }
        }
        return null;
    }
    
    /**
     * This method initializes 
     * 
     */
    public AppPropertyDialog() {
        super();
        initialize();
    }

    public AppPropertyDialog(AppProperty appProperty) {
        super();
        this.appProperty = appProperty;
        initialize();
    }

    public AppPropertyDialog(JFrame parent, AppProperty appProperty) {
        super(parent);
        this.appProperty = appProperty;
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(390, 233));
        this.setTitle(RM.R("label.AppPropertyDialog.title")); //$NON-NLS-1$
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
        this.getRootPane().setDefaultButton(this.getBtnOK());
        this.center();
        this.dataExchange(appProperty, true);
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
            jContentPane.add(getJInnerPanel(), null);
            jContentPane.add(getCbxShowConfigDialogOnStart(), null);
            jContentPane.add(getBtnOK(), null);
            jContentPane.add(getBtnCancel(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jInnerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJInnerPanel() {
        if (jInnerPanel == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(28, 82, 64, 22));
            jLabel.setText(RM.R("label.AppPropertyDialog.label.lookAndFeel")); //$NON-NLS-1$
            labelLocale = new JLabel();
            labelLocale.setBounds(new Rectangle(28, 44, 64, 22));
            labelLocale.setHorizontalTextPosition(SwingConstants.RIGHT);
            labelLocale.setText(RM.R("label.AppPropertyDialog.label.locale")); //$NON-NLS-1$
            jInnerPanel = new JPanel();
            jInnerPanel.setLayout(null);
            jInnerPanel.setBounds(new Rectangle(19, 15, 349, 139));
            jInnerPanel.setBorder(BorderFactory.createTitledBorder(null, "\u7cfb\u7edf\u53c2\u6570", TitledBorder.LEFT, TitledBorder.TOP, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            jInnerPanel.add(labelLocale, null);
            jInnerPanel.add(jLabel, null);
            jInnerPanel.add(getComboxLocale(), null);
            jInnerPanel.add(getComboxLookAndFeel(), null);
        }
        return jInnerPanel;
    }

    void dataExchange(AppProperty appProperty, boolean isToControl){
        if(isToControl){
            this.getComboxLocale().setSelectedItem(appProperty.getLocale());
            LookAndFeelItem lookAndFeelItem = this.getLookAndFeelByClassName(appProperty.getLookAndFeel());
            if(lookAndFeelItem != null){
                this.getComboxLookAndFeel().setSelectedItem(lookAndFeelItem);
            }
            this.getCbxShowConfigDialogOnStart().setSelected(appProperty.getShowConfigDialog());
        }else{
            appProperty.setLocale((String)getComboxLocale().getSelectedItem());
            LookAndFeelItem lookAndFeelItem = (LookAndFeelItem)getComboxLookAndFeel().getSelectedItem();            
            appProperty.setLookAndFeel(lookAndFeelItem.getClassName());
            appProperty.setShowConfigDialog(this.getCbxShowConfigDialogOnStart().isSelected());
        }
    }
    /**
     * This method initializes cbxShowConfigDialogOnStart	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getCbxShowConfigDialogOnStart() {
        if (cbxShowConfigDialogOnStart == null) {
            cbxShowConfigDialogOnStart = new JCheckBox();
            cbxShowConfigDialogOnStart.setBounds(new Rectangle(19, 169, 178, 18));
            cbxShowConfigDialogOnStart.setText(RM.R("label.AppPropertyDialog.label.showConfigDialogWhenStart")); //$NON-NLS-1$
        }
        return cbxShowConfigDialogOnStart;
    }

    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(200, 168, 80, 22));
            btnOK.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK.requestFocusInWindow();
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AppPropertyDialog.this.onOK();
                }
            });
        }
        return btnOK;
    }

    private boolean isOK = false;
    
    protected void onOK() {
        try {
            this.dataExchange(appProperty, false);
            this.appProperty.storeToDefXmlFile();            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, RM.R("label.AppPropertyDialog.error.saveConfig") + e.getMessage()); //$NON-NLS-1$
        }
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
            btnCancel.setBounds(new Rectangle(288, 168, 80, 22));
            btnCancel.setText(RM.R("label.info.btnCancel")); //$NON-NLS-1$
            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    AppPropertyDialog.this.onCancel();
                }
            });
        }
        return btnCancel;
    }

    protected void onCancel() {
        this.dispose();
    }

    /**
     * This method initializes comboBoxLocale	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getComboxLocale() {
        if (comboxLocale == null) {
            comboxLocale = new JComboBox();
            comboxLocale.setBounds(new Rectangle(98, 44, 176, 21));
            List<String> allLocale = ResourceManager.getAllLocale();
            for(String locale : allLocale){
                comboxLocale.addItem(locale);
            }
            comboxLocale.setSelectedItem(Locale.getDefault().toString());
        }
        return comboxLocale;
    }

    /**
     * This method initializes comboBoxLookAndFeel	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getComboxLookAndFeel() {
        if (comboxLookAndFeel == null) {
            comboxLookAndFeel = new JComboBox();
            comboxLookAndFeel.setBounds(new Rectangle(98, 83, 176, 21));
            List<LookAndFeelItem> lookAndFeels = getLookAndFeels();
            for(LookAndFeelItem lookAndFeel : lookAndFeels){
                comboxLookAndFeel.addItem(lookAndFeel);
            }
        }
        return comboxLookAndFeel;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean isOK) {
        this.isOK = isOK;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"

class LookAndFeelItem{
    
    public LookAndFeelItem(String name, String className){
        this.name = name;
        this.displayName = RM.R(name);
        this.className = className;
    }
    
    public LookAndFeelItem(String className){       
        this.className = className;
    }
    
    String name;
    String displayName;
    String className;
    
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString(){
        return this.displayName;
    }
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((className == null) ? 0 : className.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final LookAndFeelItem other = (LookAndFeelItem) obj;
        if (className == null) {
            if (other.className != null) return false;
        } else if (!className.equals(other.className)) return false;
        return true;
    }
    
   
}