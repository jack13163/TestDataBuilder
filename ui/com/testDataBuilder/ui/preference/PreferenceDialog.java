package com.testDataBuilder.ui.preference;

import com.testDataBuilder.config.preference.DefPreference;
import com.testDataBuilder.config.preference.IPreference;
import com.testDataBuilder.config.preference.XmlPreference;
import com.testDataBuilder.exception.ValidataException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.core.JTextFieldEx;
import com.testDataBuilder.ui.help.Helper;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JComboBox;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PreferenceDialog extends CenterDialog {

    static Logger logger = Logger.getLogger(PreferenceDialog.class);
    
    private JPanel jContentPane = null;
    private JPanel jInnerPanel = null;
    private JCheckBox cbxAutoConnWhenProgramStartup = null;
    private JCheckBox cbxSaveDbMetaInfo = null;
    private JCheckBox cbxReadDBInfoFromFileWhenConnError = null;
    private JLabel jLabel = null;
    private JCheckBox cbxNotIncludeIDEntityClumn = null;
    private JTextField txtDefaultRowToGenerate = null;
    private JCheckBox cbxCloseIDAutoInsert = null;
    private JCheckBox cbxClearBefInsert = null;
    private JLabel jLabel1 = null;
    private JTextField txtMinDate = null;
    private JLabel jLabel2 = null;
    private JTextField txtMaxDate = null;
    private JLabel jLabel3 = null;
    private JTextField txtTextFieldMin = null;
    private JLabel jLabel4 = null;
    private JTextField txtTextFieldMax = null;
    private JCheckBox cbxGenerateSQLFile = null;
    private JLabel jLabel6 = null;
    private JTextField txtDataTypePluginDir = null;
    private JLabel jLabel7 = null;
    private JTextField txtDateFormat = null;
    private JTDButton btnOK = null;
    private JTDButton btnCancel = null;
    private JTDButton btnApply = null;
    private JTDButton btnDefault = null;
    private JTDButton btnSelDatatypeDir = null;  
    private JTDButton btnSelWorkspaceDir = null; 
    
    private IPreference preference = null;
    private String workspace = null;
    private JLabel jLabel5 = null;
    private JComboBox comBoxScope = null;
    private JTDButton btnDeleteCurrentProjectPreferenceConfig = null;
    private JLabel jLabel8 = null;
    private JTextField txtDateExample = null;
    private java.text.SimpleDateFormat dateFormat = null;

    private JLabel jLabel9 = null;

    private JTextField txtWorkspace = null;
	public PreferenceDialog(JFrame parent, IPreference preference) {
        super(parent);
        this.preference = preference;
        initialize();   
        dateFormat = new  java.text.SimpleDateFormat(preference.getDateFormat());
        this.dataExchange(preference, true);
    }

    public void dataExchange(IPreference preference, boolean isToConsole){
        if(isToConsole){
            if(preference == null){
                this.enableAll(false);
            }
            
            if(preference instanceof XmlPreference){
                XmlPreference xmlPreference = (XmlPreference)preference;
                if(xmlPreference.isGlobal()){
                    this.getComBoxScope().setSelectedIndex(0);
                }else{
                    this.getComBoxScope().setSelectedIndex(1);
                } 
                this.enableAll(true);
            }else{
                this.getComBoxScope().setSelectedIndex(0); 
                this.enableAll(false);
            }
            this.getCbxSaveDbMetaInfo().setSelected(preference.getSaveDbMetaInfo());
            this.getCbxAutoConnWhenProgramStartup().setSelected(preference.getAutoConnWhenProgramStartup());
            this.getCbxReadDBInfoFromFileWhenConnError().setSelected(preference.getReadDBInfoFromFileWhenConnError());
            this.getCbxNotIncludeIDEntityClumn().setSelected(preference.getNotIncludeIDEntityClumn());
            this.getCbxCloseIDAutoInsert().setSelected(preference.getCloseIDAutoInsert());
            this.getCbxClearBefInsert().setSelected(preference.getClearBefInsert());
            this.getCbxGenerateSQLFile().setSelected(preference.getGenerateSQLFile());
            this.getTxtDefaultRowToGenerate().setText(preference.getDefaultRowToGenerate() + ""); //$NON-NLS-1$
            this.getTxtDateFormat().setText(preference.getDateFormat());
            this.getTxtDateExample().setText(this.getCurTime(preference.getDateFormat()));
            this.getTxtMinDate().setText(this.dateToString(preference.getMinDate()));
            this.getTxtMaxDate().setText(dateToString(preference.getMaxDate()));
            this.getTxtTextFieldMin().setText(preference.getTextFieldMin() + ""); //$NON-NLS-1$
            this.getTxtTextFieldMax().setText(preference.getTextFieldMax() + ""); //$NON-NLS-1$
            this.getTxtWorkspace().setText(preference.getWorkspace());
            this.getTxtDataTypePluginDir().setText(preference.getDataTypePluginDir());
            
        }else{
            if(preference instanceof XmlPreference){
                XmlPreference xmlPreference = (XmlPreference)preference;
                if(this.getComBoxScope().getSelectedIndex() == 0){
                    xmlPreference.setGlobal(true);
                }else{
                    xmlPreference.setGlobal(false);
                }
                xmlPreference.setSaveDbMetaInfo(this.getCbxSaveDbMetaInfo().isSelected());
                xmlPreference.setAutoConnWhenProgramStartup(this.getCbxAutoConnWhenProgramStartup().isSelected());
                xmlPreference.setReadDBInfoFromFileWhenConnError(this.getCbxReadDBInfoFromFileWhenConnError().isSelected());
                xmlPreference.setNotIncludeIDEntityClumn(this.getCbxNotIncludeIDEntityClumn().isSelected());
                xmlPreference.setCloseIDAutoInsert(this.getCbxCloseIDAutoInsert().isSelected());
                xmlPreference.setClearBefInsert(this.getCbxClearBefInsert().isSelected());
                xmlPreference.setGenerateSQLFile(this.getCbxGenerateSQLFile().isSelected());
                String strRows = this.getTxtDefaultRowToGenerate().getText().trim();
                xmlPreference.setDefaultRowToGenerate(Long.valueOf(strRows));
                xmlPreference.setDateFormat(this.getTxtDateFormat().getText().trim());
                
				try {
					java.util.Date minDate = this.strToDate(this.getTxtMinDate().getText().trim());
					xmlPreference.setMinDate(minDate);
				} catch (ParseException e) {
                    logger.error("PreferenceDialog",e); //$NON-NLS-1$
				}
                
				try {
					java.util.Date maxDate = strToDate(this.getTxtMaxDate().getText().trim());
					xmlPreference.setMaxDate(maxDate);
				} catch (ParseException e) {
                    logger.error("PreferenceDialog",e); //$NON-NLS-1$
				}
                
                xmlPreference.setWorkspace(this.getTxtWorkspace().getText().trim());
                xmlPreference.setDataTypePluginDir(this.getTxtDataTypePluginDir().getText().trim());                
            }else{
                JOptionPane.showMessageDialog(this, RM.R("label.PreferenceDialog.unProcessed")); //$NON-NLS-1$
            }
        }
    }
    
    public String getCurTime(String strDateFormat){
        this.dateFormat.applyPattern(strDateFormat);
        return dateFormat.format(new java.util.Date());
    }
    
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(605, 419));
        this.setTitle(RM.R("label.PreferenceDialog.title")); //$NON-NLS-1$
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.center();
        Helper.getInstance().enableHelpKey(this.getRootPane(), "detail.window.preferences"); //$NON-NLS-1$
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
            jContentPane.add(getBtnOK(), null);
            jContentPane.add(getBtnCancel(), null);
            jContentPane.add(jLabel5, null);
            jContentPane.add(getComBoxScope(), null);
            jContentPane.add(getBtnDeleteCurrentProjectPreferenceConfig(), null);
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
            jLabel9 = new JLabel();
            jLabel9.setBounds(new Rectangle(22, 235, 117, 22));
            jLabel9.setText(RM.R("label.PreferenceDialog.workspaceDir"));
            jLabel8 = new JLabel();
            jLabel8.setBounds(new Rectangle(289, 146, 102, 22));
            jLabel8.setText(RM.R("label.PreferenceDialog.TimeExample")); //$NON-NLS-1$
            jLabel5 = new JLabel();
            jLabel5.setText(RM.R("label.PreferenceDialog.ConfigApplyTo")); //$NON-NLS-1$
            jLabel5.setBounds(new Rectangle(30, 4, 113, 21));
            jLabel7 = new JLabel();
            jLabel7.setBounds(new Rectangle(22, 146, 99, 22));
            jLabel7.setText(RM.R("label.PreferenceDialog.TimeFormat")); //$NON-NLS-1$
            jLabel6 = new JLabel();
            jLabel6.setBounds(new Rectangle(22, 266, 118, 21));
            jLabel6.setText(RM.R("label.PreferenceDialog.dataTypeDir")); //$NON-NLS-1$
            jLabel4 = new JLabel();
            jLabel4.setBounds(new Rectangle(290, 207, 121, 22));
            jLabel4.setText(RM.R("label.PreferenceDialog.TextFieldMaxLength")); //$NON-NLS-1$
            jLabel3 = new JLabel();
            jLabel3.setBounds(new Rectangle(22, 207, 127, 22));
            jLabel3.setText(RM.R("label.PreferenceDialog.TextFieldMinLength")); //$NON-NLS-1$
            jLabel2 = new JLabel();
            jLabel2.setBounds(new Rectangle(290, 177, 101, 22));
            jLabel2.setText(RM.R("label.PreferenceDialog.DataMax")); //$NON-NLS-1$
            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(22, 177, 99, 22));
            jLabel1.setText(RM.R("label.PreferenceDialog.DataMin")); //$NON-NLS-1$
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(22, 116, 99, 21));
            jLabel.setText(RM.R("label.PreferenceDialog.RowOfPerTable")); //$NON-NLS-1$
            jInnerPanel = new JPanel();
            jInnerPanel.setLayout(null);
            jInnerPanel.setBounds(new Rectangle(9, 31, 578, 329));
            jInnerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            jInnerPanel.add(getCbxAutoConnWhenProgramStartup(), null);
            jInnerPanel.add(getCbxSaveDbMetaInfo(), null);
            jInnerPanel.add(getCbxReadDBInfoFromFileWhenConnError(), null);
            jInnerPanel.add(jLabel, null);
            jInnerPanel.add(getCbxNotIncludeIDEntityClumn(), null);
            jInnerPanel.add(getTxtDefaultRowToGenerate(), null);
            jInnerPanel.add(getCbxCloseIDAutoInsert(), null);
            jInnerPanel.add(getCbxClearBefInsert(), null);
            jInnerPanel.add(jLabel1, null);
            jInnerPanel.add(getTxtMinDate(), null);
            jInnerPanel.add(jLabel2, null);
            jInnerPanel.add(getTxtMaxDate(), null);
            jInnerPanel.add(jLabel3, null);
            jInnerPanel.add(getTxtTextFieldMin(), null);
            jInnerPanel.add(jLabel4, null);
            jInnerPanel.add(getTxtTextFieldMax(), null);
            jInnerPanel.add(getCbxGenerateSQLFile(), null);
            jInnerPanel.add(jLabel6, null);
            jInnerPanel.add(getTxtDataTypePluginDir(), null);
            jInnerPanel.add(jLabel7, null);
            jInnerPanel.add(getTxtDateFormat(), null);
            jInnerPanel.add(getBtnApply(), null);
            jInnerPanel.add(getBtnDefault(), null);
            jInnerPanel.add(getBtnSelDatatypeDir(), null);
            jInnerPanel.add(jLabel8, null);
            jInnerPanel.add(getTxtDateExample(), null);
            jInnerPanel.add(jLabel9, null);
            jInnerPanel.add(getTxtWorkspace(), null);
            jInnerPanel.add(getBtnSelWorkspaceDir(), null);
        }
        return jInnerPanel;
    }

    /**
     * This method initializes cbxAutoConnWhenProgramStartup    
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxAutoConnWhenProgramStartup() {
        if (cbxAutoConnWhenProgramStartup == null) {
            cbxAutoConnWhenProgramStartup = new JCheckBox();
            cbxAutoConnWhenProgramStartup.setBounds(new Rectangle(15, 32, 270, 24));
            cbxAutoConnWhenProgramStartup.setText(RM.R("label.PreferenceDialog.autoConnDBWhenProgramStartup")); //$NON-NLS-1$
        }
        return cbxAutoConnWhenProgramStartup;
    }

    /**
     * This method initializes cbxSaveDbMetaInfo    
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxSaveDbMetaInfo() {
        if (cbxSaveDbMetaInfo == null) {
            cbxSaveDbMetaInfo = new JCheckBox();
            cbxSaveDbMetaInfo.setBounds(new Rectangle(15, 10, 514, 24));
            cbxSaveDbMetaInfo.setText(RM.R("label.PreferenceDialog.saveDBMetaInfo")); //$NON-NLS-1$
        }
        return cbxSaveDbMetaInfo;
    }

    /**
     * This method initializes cbxReadDBInfoFromFileWhenConnError   
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxReadDBInfoFromFileWhenConnError() {
        if (cbxReadDBInfoFromFileWhenConnError == null) {
            cbxReadDBInfoFromFileWhenConnError = new JCheckBox();
            cbxReadDBInfoFromFileWhenConnError.setBounds(new Rectangle(292, 32, 279, 24));
            cbxReadDBInfoFromFileWhenConnError.setText(RM.R("label.PreferenceDialog.readLocalFileWhenConnDBFailure")); //$NON-NLS-1$
        }
        return cbxReadDBInfoFromFileWhenConnError;
    }

    /**
     * This method initializes cbxNotIncludeIDEntityClumn   
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxNotIncludeIDEntityClumn() {
        if (cbxNotIncludeIDEntityClumn == null) {
            cbxNotIncludeIDEntityClumn = new JCheckBox();
            cbxNotIncludeIDEntityClumn.setBounds(new Rectangle(15, 60, 269, 19));
            cbxNotIncludeIDEntityClumn.setText(RM.R("label.PreferenceDialog.whenAutoGenConfigNotIncludeIDEntityClumn")); //$NON-NLS-1$
        }
        return cbxNotIncludeIDEntityClumn;
    }

    /**
     * This method initializes txtDefaultRowToGenerate  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtDefaultRowToGenerate() {
        if (txtDefaultRowToGenerate == null) {
            txtDefaultRowToGenerate = new JTextFieldEx();
            txtDefaultRowToGenerate.setBounds(new Rectangle(131, 116, 149, 21));
        }
        return txtDefaultRowToGenerate;
    }

    /**
     * This method initializes cbxCloseIDAutoInsert 
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxCloseIDAutoInsert() {
        if (cbxCloseIDAutoInsert == null) {
            cbxCloseIDAutoInsert = new JCheckBox();
            cbxCloseIDAutoInsert.setBounds(new Rectangle(292, 60, 279, 19));
            cbxCloseIDAutoInsert.setText(RM.R("label.PreferenceDialog.closeIDAutoInsert")); //$NON-NLS-1$
        }
        return cbxCloseIDAutoInsert;
    }

    /**
     * This method initializes cbxClearBefInsert    
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxClearBefInsert() {
        if (cbxClearBefInsert == null) {
            cbxClearBefInsert = new JCheckBox();
            cbxClearBefInsert.setBounds(new Rectangle(15, 83, 271, 24));
            cbxClearBefInsert.setText(RM.R("label.PreferenceDialog.clearTableBefInsert")); //$NON-NLS-1$
        }
        return cbxClearBefInsert;
    }

    /**
     * This method initializes txtMinData   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtMinDate() {
        if (txtMinDate == null) {
            txtMinDate = new JTextFieldEx();
            txtMinDate.setBounds(new Rectangle(131, 177, 149, 22));
        }
        return txtMinDate;
    }

    /**
     * This method initializes txtMaxDate   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtMaxDate() {
        if (txtMaxDate == null) {
            txtMaxDate = new JTextFieldEx();
            txtMaxDate.setBounds(new Rectangle(406, 177, 149, 22));
        }
        return txtMaxDate;
    }

    /**
     * This method initializes txtTextFieldMin  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtTextFieldMin() {
        if (txtTextFieldMin == null) {
            txtTextFieldMin = new JTextFieldEx();
            txtTextFieldMin.setBounds(new Rectangle(159, 207, 121, 22));
        }
        return txtTextFieldMin;
    }

    /**
     * This method initializes txtTextFieldMax  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtTextFieldMax() {
        if (txtTextFieldMax == null) {
            txtTextFieldMax = new JTextFieldEx();
            txtTextFieldMax.setBounds(new Rectangle(417, 207, 139, 22));
        }
        return txtTextFieldMax;
    }

    /**
     * This method initializes cbxMustXsdValidate   
     *  
     * @return javax.swing.JCheckBox    
     */
    private JCheckBox getCbxGenerateSQLFile() {
        if (cbxGenerateSQLFile == null) {
            cbxGenerateSQLFile = new JCheckBox();
            cbxGenerateSQLFile.setBounds(new Rectangle(292, 84, 279, 21));
            cbxGenerateSQLFile.setText(RM.R("label.PreferenceDialog.generateSQLFile")); //$NON-NLS-1$
        }
        return cbxGenerateSQLFile;
    }

    /**
     * This method initializes txtDataTypePluginDir 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtDataTypePluginDir() {
        if (txtDataTypePluginDir == null) {
            txtDataTypePluginDir = new JTextFieldEx();
            txtDataTypePluginDir.setBounds(new Rectangle(150, 265, 369, 21));
        }
        return txtDataTypePluginDir;
    }

    /**
     * This method initializes txtDateFormat    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getTxtDateFormat() {
        if (txtDateFormat == null) {
            txtDateFormat = new JTextFieldEx();
            txtDateFormat.setBounds(new Rectangle(131, 146, 149, 22));
            txtDateFormat.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent e) {
                    PreferenceDialog.this.txtDateFormatKeyReleased();
                }
            });
        }
        return txtDateFormat;
    }

    protected void txtDateFormatKeyReleased() {
        String strFormat = getTxtDateFormat().getText();
        if(StringUtils.isEmpty(strFormat)){
            this.getTxtDateExample().setText(""); //$NON-NLS-1$
        }else{
            try{
                this.getTxtDateExample().setText(getCurTime(strFormat));
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,
                                String.format(RM.R("label.PreferenceDialog.error.dateFormat"),strFormat, ex.getMessage())); //$NON-NLS-1$ //$NON-NLS-2$
                this.getTxtDateFormat().selectAll();
            }
        }
    }

    /**
     * This method initializes btnOK    
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(496, 367, 71, 20));
            btnOK.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreferenceDialog.this.onOK();
                }
            });
        }
        return btnOK;
    }

    protected void onOK() {
        try {
            this.saveConfig();
         } catch (IOException e) {
             JOptionPane.showMessageDialog(this, RM.R("label.PreferenceDialog.error.saveConfig") + e.getMessage()); //$NON-NLS-1$
             return;
         } catch (ValidataException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.getControl().grabFocus();
            if(e.getControl() instanceof JTextField){
                JTextField txt = (JTextField)e.getControl();
                txt.selectAll();
            }
            return;
        }
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
            btnCancel.setBounds(new Rectangle(403, 367, 71, 20));
            btnCancel.setText(RM.R("label.info.btnCancel")); //$NON-NLS-1$
            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreferenceDialog.this.dispose();
                }
            });
        }
        return btnCancel;
    }

    /**
     * This method initializes btnApply 
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnApply() {
        if (btnApply == null) {
            btnApply = new JTDButton();
            btnApply.setBounds(new Rectangle(484, 301, 74, 19));
            btnApply.setText(RM.R("label.info.btnApply")); //$NON-NLS-1$
            btnApply.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreferenceDialog.this.apply();
                }
            });
        }
        return btnApply;
    }

    protected void apply() {
       try {
           this.saveConfig();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, RM.R("label.PreferenceDialog.error.saveConfig") + e.getMessage()); //$NON-NLS-1$
            
        }  catch (ValidataException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.getControl().grabFocus();
            if(e.getControl() instanceof JTextField){
                JTextField txt = (JTextField)e.getControl();
                txt.selectAll();
            }
        }     
    }

    /**
     * This method initializes btnDefault   
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnDefault() {
        if (btnDefault == null) {
            btnDefault = new JTDButton();
            btnDefault.setBounds(new Rectangle(320, 301, 144, 19));
            btnDefault.setText(RM.R("label.PreferenceDialog.storeDefault")); //$NON-NLS-1$
            btnDefault.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreferenceDialog.this.restoreDefaultPreference();
                }
            });           
        }
        return btnDefault;
    }

    protected void restoreDefaultPreference() {   
       if(this.preference instanceof XmlPreference){
    	   XmlPreference xmlPreference = new XmlPreference(new DefPreference());
    	   this.dataExchange(xmlPreference, true);
       }
    }

    /**
     * This method initializes btnSelDatatypeDir    
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnSelDatatypeDir() {
        if (btnSelDatatypeDir == null) {
            btnSelDatatypeDir = new JTDButton();
            btnSelDatatypeDir.setBounds(new Rectangle(532, 266, 26, 21));
            btnSelDatatypeDir.setText("..."); //$NON-NLS-1$
            btnSelDatatypeDir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreferenceDialog.this.selectDir(PreferenceDialog.this.txtDataTypePluginDir);
                }
            });
        }
        return btnSelDatatypeDir;
    }

    private JTDButton getBtnSelWorkspaceDir() {
        if (btnSelWorkspaceDir == null) {
            btnSelWorkspaceDir = new JTDButton();
            btnSelWorkspaceDir.setText("..."); //$NON-NLS-1$
            btnSelWorkspaceDir.setBounds(new Rectangle(531, 235, 26, 21));
            btnSelWorkspaceDir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreferenceDialog.this.selectDir(PreferenceDialog.this.txtWorkspace);
                }
            });
        }
        return btnSelWorkspaceDir;
    }

    protected void selectDir(JTextField textField) {
        String curDir = textField.getText().trim();
        JFileChooser directoryChooser = new JFileChooser(curDir);        
        directoryChooser.setDialogTitle(RM.R("label.PreferenceDialog.fileChooser.title")); //$NON-NLS-1$
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String dirPath = directoryChooser.getSelectedFile().getAbsolutePath();
            textField.setText(dirPath);           
        }
    }

    public void enableAll(boolean enable){
        this.getCbxAutoConnWhenProgramStartup().setEnabled(enable);
        this.getCbxClearBefInsert().setEnabled(enable);
        this.getCbxCloseIDAutoInsert().setEnabled(enable);
        this.getCbxGenerateSQLFile().setEnabled(enable);
        this.getCbxNotIncludeIDEntityClumn().setEnabled(enable);
        this.getCbxReadDBInfoFromFileWhenConnError().setEnabled(enable);
        this.getCbxSaveDbMetaInfo().setEnabled(enable);
        this.getTxtDataTypePluginDir().setEditable(enable);
        this.getTxtWorkspace().setEnabled(enable);
        this.getTxtDateFormat().setEditable(enable);
        this.getTxtDefaultRowToGenerate().setEditable(enable);
        this.getTxtMaxDate().setEditable(enable);
        this.getTxtMinDate().setEditable(enable);
        this.getTxtTextFieldMax().setEditable(enable);
        this.getTxtTextFieldMin().setEditable(enable);
        this.getBtnDeleteCurrentProjectPreferenceConfig().setEnabled(enable);
        this.getBtnApply().setEnabled(enable);
        this.getBtnDefault().setEnabled(enable);
        this.getBtnOK().setEnabled(enable);
        
    }

    /**
     * This method initializes comBoxScope  
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getComBoxScope() {
        if (comBoxScope == null) {
            comBoxScope = new JComboBox();
            comBoxScope.setBounds(new Rectangle(161, 4, 120, 21));
            comBoxScope.addItem(RM.R("label.PreferenceDialog.allProject")); //$NON-NLS-1$
            comBoxScope.addItem(RM.R("label.PreferenceDialog.currentProject")); //$NON-NLS-1$
        }
        return comBoxScope;
    }

    /**
     * This method initializes btnDeleteCurrentProjectPreferenceConfig  
     *  
     * @return com.testDataBuilder.ui.core.JTDButton  
     */
    private JTDButton getBtnDeleteCurrentProjectPreferenceConfig() {
        if (btnDeleteCurrentProjectPreferenceConfig == null) {
            btnDeleteCurrentProjectPreferenceConfig = new JTDButton();
            btnDeleteCurrentProjectPreferenceConfig.setBounds(new Rectangle(299, 4, 257, 21));
            btnDeleteCurrentProjectPreferenceConfig.setText(RM.R("label.PreferenceDialog.deleteCurrentProjectProferenceConfig")); //$NON-NLS-1$
            btnDeleteCurrentProjectPreferenceConfig.setToolTipText(RM.R("label.PreferenceDialog.deleteCurrentProjectProferenceConfig.tooltip")); //$NON-NLS-1$
            btnDeleteCurrentProjectPreferenceConfig
            		.addActionListener(new java.awt.event.ActionListener() {
            			public void actionPerformed(java.awt.event.ActionEvent e) {
            				PreferenceDialog.this.deleteCurProjectPreferenceConfig();
            			}
            		});
        }
        return btnDeleteCurrentProjectPreferenceConfig;
    }
    
    protected void deleteCurProjectPreferenceConfig() {
		File configFile = new File(this.workspace, XmlPreference.config);
		if(configFile.exists()){
			configFile.delete();
			JOptionPane.showMessageDialog(this,
                            RM.R("label.PreferenceDialog.deleteCurConfig.success")); //$NON-NLS-1$ //$NON-NLS-2$
		}
		this.getComBoxScope().setSelectedIndex(0);
	}

	public void saveConfig() throws IOException, ValidataException{
        this.validateConsoleValue();
        this.dataExchange(preference, false);
        if(this.preference != null && this.preference instanceof XmlPreference){
            XmlPreference xmlPreference = (XmlPreference)preference;
            if(StringUtils.isEmpty(getWorkspace())){
                xmlPreference.saveConfig(null);
            }else{
                xmlPreference.saveConfig(new File(this.getWorkspace()));
            }
        }
    }

    private void validateConsoleValue() throws ValidataException {
        
        String strRows = this.getTxtDefaultRowToGenerate().getText().trim();
        if(StringUtils.isEmpty(strRows)){
            throw new ValidataException(this.txtDefaultRowToGenerate, RM.R("label.PreferenceDialog.error.rowNumberIsNull")); //$NON-NLS-1$
        }
        try{
            Long.valueOf(strRows);            
        }catch(NumberFormatException ex){
            throw new ValidataException(this.txtDefaultRowToGenerate,
                            String.format(RM.R("label.PreferenceDialog.error.invalidNumber") ,strRows)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        String strFormat = this.getTxtDateFormat().getText().trim();
        if(StringUtils.isEmpty(strFormat)){
            throw new ValidataException(this.txtDateFormat, RM.R("label.PreferenceDialog.error.dateFormat.isNull")); //$NON-NLS-1$
        }
        this.dateFormat.applyPattern(strFormat);
        try{
        	dateFormat.format(new java.util.Date());
        }catch(java.lang.IllegalArgumentException ex){
            throw new ValidataException(this.txtDateFormat,
                            String.format(RM.R("label.PreferenceDialog.error.dateFormat"),
                                            strFormat , ex.getMessage())); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        String strMinDate = this.getTxtMinDate().getText().trim();
        if(StringUtils.isEmpty(strMinDate)){
            throw new ValidataException(this.txtMinDate, RM.R("label.PreferenceDialog.error.minDate.isNull")); //$NON-NLS-1$
        }
        try {
        	dateFormat.parse(strMinDate);
        } catch (ParseException e) {
            throw new ValidataException(this.txtMinDate, 
                            String.format(RM.R("label.PreferenceDialog.error.minDate"),strMinDate)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String strMaxDate = this.getTxtMaxDate().getText().trim();
        if(StringUtils.isEmpty(strMaxDate)){
            throw new ValidataException(this.txtMaxDate, RM.R("label.PreferenceDialog.error.maxDate.isNull")); //$NON-NLS-1$
        }
        try {
        	dateFormat.parse(strMaxDate);
        } catch (ParseException e) {
            throw new ValidataException(this.txtMaxDate,
                            String.format(RM.R("label.PreferenceDialog.error.maxDate"), strMaxDate)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        String strTextMinLength = this.txtTextFieldMin.getText().trim();
        if(StringUtils.isEmpty(strTextMinLength)){
            throw new ValidataException(this.txtTextFieldMin, RM.R("label.PreferenceDialog.error.txtFieldMin.isNull")); //$NON-NLS-1$
        }
        try{
            Long.valueOf(strTextMinLength);
        }catch(NumberFormatException ex){
            throw new ValidataException(this.txtTextFieldMin,
                            String.format(RM.R("label.PreferenceDialog.error.invalidNumber"),strTextMinLength )); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String strTextMaxLength = this.txtTextFieldMax.getText().trim();
        if(StringUtils.isEmpty(strTextMaxLength)){
            throw new ValidataException(this.txtTextFieldMax, RM.R("label.PreferenceDialog.error.txtFieldMax.isNull")); //$NON-NLS-1$
        }
        try{
            Long.valueOf(strTextMaxLength);
        }catch(NumberFormatException ex){
            throw new ValidataException(this.txtTextFieldMax, 
                            String.format(RM.R("label.PreferenceDialog.error.invalidNumber"), strTextMaxLength )); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
    }

    public java.util.Date strToDate(String strDate) throws ParseException{        
    	return new java.sql.Date(dateFormat.parse(strDate).getTime());
    }
    
    public String dateToString(java.util.Date date){
    	return dateFormat.format(date);
    }
    
    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * This method initializes txtDateExample	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtDateExample() {
        if (txtDateExample == null) {
            txtDateExample = new JTextFieldEx();
            txtDateExample.setBounds(new Rectangle(407, 146, 149, 22));
            txtDateExample.setEditable(false);
        }
        return txtDateExample;
    }

	/**
     * This method initializes txtWorkspace	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtWorkspace() {
        if (txtWorkspace == null) {
            txtWorkspace = new JTextField();
            txtWorkspace.setBounds(new Rectangle(150, 235, 368, 22));
        }
        return txtWorkspace;
    }

    public static void main(String[] args) {
				
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
