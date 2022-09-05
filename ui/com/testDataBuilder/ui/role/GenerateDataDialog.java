package com.testDataBuilder.ui.role;

import com.testDataBuilder.config.preference.IPreference;
import com.testDataBuilder.core.GenerateAndInsertData;
import com.testDataBuilder.core.IProcess;
import com.testDataBuilder.core.SQLRecorder;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.core.ProgressPanel;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.exception.JavaCodeRuntimeException;
import com.testDataBuilder.ui.database.JSQLFileChooser;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.ui.main.MainFrame;
import com.testDataBuilder.ui.main.OpenWorkspaceUtil;
import com.testDataBuilder.util.Global;
import com.testDataBuilder.util.TimeUtils;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.JCheckBox;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.awt.Color;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GenerateDataDialog extends CenterDialog implements IProcess{

    static Logger logger = Logger.getLogger(GenerateDataDialog.class);
    
    private JPanel jContentPane = null;
    private ProgressPanel innerPanel = null;
    private JTDButton btnBegin = null;
    private JTDButton btnRunInBackground = null;
    private JTDButton btnGenPreferences = null;
    private JPanel recordFilePanel = null;
    private JCheckBox cbxGenerateSQLFile = null;
    private JTDButton btnOpenScriptDir;
    private JTextField txtSQLFilePath = null;
    private JTDButton btnSelSQLFile = null;
    
    public static final String BTN_BEING_BEING = RM.R("label.GenerateDataDialog.btnStart"); //$NON-NLS-1$
    public static final String BTN_BEING_STOP = RM.R("label.GenerateDataDialog.btnStop"); //$NON-NLS-1$
    
    private IPreference p = Global.getInstance().P;
    private WorkspaceDataCache workspaceDataCache = null;
    
    int minHeight = 165;
    int maxHeight = minHeight;
    @Override
    public MainFrame getParent(){
        return (MainFrame) super.getParent();
    }
    /**
     * This method initializes 
     * 
     */
    public GenerateDataDialog() {
        super();
        initialize();
        this.setDefaultCloseOperation(GenerateDataDialog.DO_NOTHING_ON_CLOSE); 
        this.center();
    }

    public GenerateDataDialog(MainFrame parent, final WorkspaceDataCache workspaceDataCache ) {
        super(parent);
        this.workspaceDataCache = workspaceDataCache;
        initialize();
        this.center();
    }

    
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(490, 432));
        this.setResizable(false);
        this.setTitle(RM.R("label.GenerateDataDialog.title")); //$NON-NLS-1$
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);   
        maxHeight = this.getHeight();
        this.setContentPane(getJContentPane());
        this.initOptionPanel();
        Helper.getInstance().enableHelpKey(this.getRootPane(), "detail.role.generateData"); //$NON-NLS-1$
    }

//    public boolean confirmCloseWindow(){
//        boolean isClose = true;
//        if(generateAndInsertData != null){
//            int result = JOptionPane.showConfirmDialog(this, 
//                            RM.R("label.GenerateDataDialog.4"),RM.R("label.GenerateDataDialog.5"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$ //$NON-NLS-2$
//            if(result == JOptionPane.OK_OPTION){
//                beginOrStopInsert();
//            }else{
//                isClose = false;
//            }
//            
//        }
//        return isClose;
//    }
    
    private void initOptionPanel(){
        this.getCbxGenerateSQLFile().setSelected(p.getGenerateSQLFile());
        this.getJTableTable().setData(this.workspaceDataCache.getTableConfigs());
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
            jContentPane.add(getJOptionTabbedPane(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes innerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private ProgressPanel getInnerPanel() {
        if (innerPanel == null) {
            innerPanel = new ProgressPanel();
            innerPanel.setLayout(null);
            innerPanel.setBounds(14, 13, 452, 112);
            innerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            innerPanel.add(getBtnBegin());
            innerPanel.add(getBtnGenPreferences());
            innerPanel.add(getBtnRunInBackground());
            innerPanel.add(getBtnClose());
        }
        return innerPanel;
    }

    /**
     * This method initializes btnBegin	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnBegin() {
        if (btnBegin == null) {
            btnBegin = new JTDButton();
            btnBegin.setBounds(256, 74, 78, 24);
            btnBegin.setText(BTN_BEING_BEING); //$NON-NLS-1$
            btnBegin.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
//                    try{
                        GenerateDataDialog.this.beginOrStopInsert();
//                    }catch(IOException ex){
//                        JOptionPane.showMessageDialog(GenerateDataDialog.this, "创建SQL文件时出错，" + ex.getMessage());
//                    }
                }
            });
        }
        return btnBegin;
    }

    private SQLRecorder sqlRecorder = null;
    
    protected void beginOrStopInsert() {
        if(this.generateAndInsertData == null){
            this.getBtnBegin().setText(BTN_BEING_STOP);
            this.getBtnClose().setEnabled(false);
            generateAndInsertData = new GenerateAndInsertData();
            generateAndInsertData.setProcessBar(this);
            if(this.getCbxGenerateSQLFile().isSelected()){
                File sqlFile = new File(this.getTxtSQLFilePath().getText().trim());
                sqlRecorder = new SQLRecorder(sqlFile);
                generateAndInsertData.setSqlRecorder(sqlRecorder);
            }
            GenearterExecutor executor = new GenearterExecutor(generateAndInsertData, this, workspaceDataCache);
            new Thread(executor).start();
        }else{
            int result = JOptionPane.showConfirmDialog(this, RM.R("label.GenerateDataDialog.confirmStop"));
            if(result == JOptionPane.OK_OPTION){
                this.getBtnBegin().setText(BTN_BEING_BEING);
                this.getBtnClose().setEnabled(true);
                generateAndInsertData.setBread(true);
                generateAndInsertData = null;
            }
        }
        
        
    }
    /**
     * This method initializes btnCancel	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnRunInBackground() {
        if (btnRunInBackground == null) {
            btnRunInBackground = new JTDButton();
            btnRunInBackground.setBounds(47, 74, 93, 24);
            btnRunInBackground.setText(RM.R("label.GenerateDataDialog.btnRunInBack")); //$NON-NLS-1$
            btnRunInBackground.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GenerateDataDialog.this.runInBackground();
                }
            });
        }
        return btnRunInBackground;
    }

    private boolean isRunInBackground = false;
    
    public boolean isRunInBackground(){
        return isRunInBackground;
    }
    
    public void setIsRunInBackground(boolean isRunInBackground){
        this.isRunInBackground = isRunInBackground;
    }
    
    protected void runInBackground() {
        this.setVisible(false);
        this.isRunInBackground = true;
    }
    
    /**
     * This method initializes btnGenPreferences	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnGenPreferences() {
        if (btnGenPreferences == null) {
            btnGenPreferences = new JTDButton();
            btnGenPreferences.setBounds(159, 74, 78, 24);
            btnGenPreferences.setText(RM.R("label.GenerateDataDialog.btnOption")); //$NON-NLS-1$
            btnGenPreferences.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                     GenerateDataDialog.this.showOrHidePreference();
                }
            });
        }
        return btnGenPreferences;
    }

    protected void showOrHidePreference() {
        if(this.getHeight() == this.minHeight){
            this.setSize(this.getWidth(), this.maxHeight);
        }else{
            this.setSize(this.getWidth(), this.minHeight);
        }
        this.paintAll(this.getGraphics());
    }
    
    private GenerateAndInsertData generateAndInsertData = null;  //  @jve:decl-index=0:
    private JTDButton btnClose = null;

    private JTabbedPane jOptionTabbedPane = null;

    private JScrollPane jDetailScrollPane = null;

    private JTableTable jTableTable = null;

    /**
     * This method initializes recordFilePanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getRecordFilePanel() {
        if (recordFilePanel == null) {
            recordFilePanel = new JPanel();
            recordFilePanel.setLayout(null);
            recordFilePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            recordFilePanel.add(getCbxGenerateSQLFile(), null);
            recordFilePanel.add(getTxtSQLFilePath(), null);
            recordFilePanel.add(getBtnSelSQLFile(), null);
            recordFilePanel.add(getBtnOpenScriptDir());
        }
        return recordFilePanel;
    }

    /**
     * This method initializes cbxGenerateSQLFile	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getCbxGenerateSQLFile() {
        if (cbxGenerateSQLFile == null) {
            cbxGenerateSQLFile = new JCheckBox();
            cbxGenerateSQLFile.setBounds(new Rectangle(13, 10, 132, 19));
            cbxGenerateSQLFile.setText(RM.R("label.GenerateDataDialog.btnGenSQLFile")); //$NON-NLS-1$
            cbxGenerateSQLFile.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    GenerateDataDialog.this.cbxGenerateSQLFileChange();
                }
            });
        }
        return cbxGenerateSQLFile;
    }

    protected void cbxGenerateSQLFileChange() {
        boolean isSeld = this.getCbxGenerateSQLFile().isSelected();
        this.getTxtSQLFilePath().setEnabled(isSeld);
        this.getBtnSelSQLFile().setEnabled(isSeld);
        
        if(isSeld){
            String temp = this.getTxtSQLFilePath().getText();
            if(StringUtils.isEmpty(temp)){
                String sqlFileName = getFileName();
                this.getTxtSQLFilePath().setText(sqlFileName);
            }
        }else{
            
        }
    }
    
    public String getFileName(){
        String t = TimeUtils.formatData(new java.util.Date(), TimeUtils.FILENAME_FORMAT);
        File dir = new File(this.workspaceDataCache.getGeneratedSQLDir());
        if(!dir.exists()){
            dir.mkdirs();
        }
        String fileName = new File(dir, "GenerateData_" + t + ".sql").getAbsolutePath(); //$NON-NLS-1$ //$NON-NLS-2$
        return fileName; 
    }
    
    /**
     * This method initializes txtSQLFilePath	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtSQLFilePath() {
        if (txtSQLFilePath == null) {
            txtSQLFilePath = new JTextField();
            txtSQLFilePath.setBounds(new Rectangle(17, 38, 377, 23));
            txtSQLFilePath.setEnabled(false);
        }
        return txtSQLFilePath;
    }

    /**
     * This method initializes btnSelSQLFile	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSelSQLFile() {
        if (btnSelSQLFile == null) {
            btnSelSQLFile = new JTDButton();
            btnSelSQLFile.setBounds(new Rectangle(405, 38, 27, 22));
            btnSelSQLFile.setEnabled(false);
            btnSelSQLFile.setText("..."); //$NON-NLS-1$
            btnSelSQLFile.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GenerateDataDialog.this.SelFile();
                }
            });
        }
        return btnSelSQLFile;
    }
    protected void SelFile() {
        JSQLFileChooser fileChooser = new JSQLFileChooser(this.getTxtSQLFilePath().getText());        
                
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            this.getTxtSQLFilePath().setText(filePath);           
        }
    }
    public void setInfo(String info) {
        this.getInnerPanel().getTxtInfo().setText(info);
    }
    public void setProcessBarPos(int pos) {
    	pos = (pos % 101);
    	this.getInnerPanel().getProcessBar().setValue(pos);
    }

    public void onGenerateOver(Exception ex, SQLRecorder tmpSqlRecorder){
        this.generateAndInsertData = null;
        String info = null;
        if(ex != null){
            if(ex instanceof SQLException){
                info =  String.format(RM.R("label.GenerateDataDialog.info.SQLException") , ex.getMessage()); //$NON-NLS-1$
            }else if(ex instanceof IOException){
                info = String.format(RM.R("label.GenerateDataDialog.info.IOException"), ex.getMessage()); //$NON-NLS-1$
            }else if(ex instanceof JavaCodeRuntimeException){
            	info = ex.getMessage();
            }else{
                info = String.format(RM.R("label.GenerateDataDialog.info.exception"), ex.getClass().getName()); //$NON-NLS-1$
            }
            logger.error(info, ex);
        }else{
            File sqlFile = null;
            if(tmpSqlRecorder != null && tmpSqlRecorder.getSqlFile() != null){
                sqlFile = tmpSqlRecorder.getSqlFile();
            }
            
            if(sqlFile == null){
                info = RM.R("label.GenerateDataDialog.info.over"); //$NON-NLS-1$
            }else{
                info = RM.R("label.GenerateDataDialog.info.genOverAndGenTheSQLFile") + sqlFile.getAbsolutePath() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        

        this.getBtnBegin().setText(BTN_BEING_BEING);
        this.getBtnClose().setEnabled(true);
        this.setProcessBarPos(0);
        this.setInfo(info);
        JOptionPane.showMessageDialog(this, info);
    }
    /**
     * This method initializes btnClose	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnClose() {
        if (btnClose == null) {
            btnClose = new JTDButton();
            btnClose.setBounds(350, 74, 78, 24);
            btnClose.setText(RM.R("label.info.btnClose")); //$NON-NLS-1$
            btnClose.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                   GenerateDataDialog.this.onClose();
                }
            });
        }
        return btnClose;
    }
    protected void onClose() {
        this.dispose();
    }
    /**
     * This method initializes jOptionTabbedPane	
     * 	
     * @return javax.swing.JTabbedPane	
     */
    private JTabbedPane getJOptionTabbedPane() {
        if (jOptionTabbedPane == null) {
            jOptionTabbedPane = new JTabbedPane();
            jOptionTabbedPane.setBounds(new Rectangle(14, 145, 452, 246));
            jOptionTabbedPane.addTab(RM.R("label.GenerateDataDialog.tabTitle.genDetail"), null, getJDetailScrollPane(), RM.R("label.GenerateDataDialog.tabTitle.genDetail.alt")); //$NON-NLS-1$ //$NON-NLS-2$
            jOptionTabbedPane.addTab(RM.R("label.GenerateDataDialog.tabTitle.recordFile"), null, getRecordFilePanel(),RM.R("label.GenerateDataDialog.tabTitle.recordFile.alt"));      //$NON-NLS-1$ //$NON-NLS-2$
        }
        return jOptionTabbedPane;
    }
    /**
     * This method initializes jDetailScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJDetailScrollPane() {
        if (jDetailScrollPane == null) {
            jDetailScrollPane = new JScrollPane();
            jDetailScrollPane.setViewportView(getJTableTable());
        }
        return jDetailScrollPane;
    }
    /**
     * This method initializes jTableTable	
     * 	
     * @return com.testDataBuilder.ui.role.JTableTable	
     */
    private JTableTable getJTableTable() {
        if (jTableTable == null) {
            jTableTable = new JTableTable();
        }
        return jTableTable;
    }
    
    private JButton getBtnOpenScriptDir() {
    	if(btnOpenScriptDir == null) {
    		btnOpenScriptDir = new JTDButton();
    		btnOpenScriptDir.setText(RM.R("label.GenerateDataDialog.btnOpenScriptDir"));
    		btnOpenScriptDir.setBounds(337, 9, 95, 22);
    		btnOpenScriptDir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    GenerateDataDialog.this.openScriptDir();
                }
            });
    	}
    	return btnOpenScriptDir;
    }
	protected void openScriptDir() {
		File file = new File(this.getTxtSQLFilePath().getText().trim());
		String dir = null;
		if(file != null){
			dir = file.getParent();
		}
		if(dir != null){
			OpenWorkspaceUtil.openWorkspaceInExplorer(dir, this);
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

class GenearterExecutor implements Runnable{

    private GenerateAndInsertData generateAndInsertData;
    
    private GenerateDataDialog genearteDataDialog = null;
    
    private WorkspaceDataCache workspaceDataCache = null;
    
    public GenearterExecutor(GenerateAndInsertData generateAndInsertData
                    ,GenerateDataDialog genearteDataDialog
                    ,WorkspaceDataCache workspaceDataCache){
        this.generateAndInsertData = generateAndInsertData;
        this.genearteDataDialog = genearteDataDialog;
        this.workspaceDataCache = workspaceDataCache;
    }
    
    public void run() {
        Exception tempEx = null;
        try {
            generateAndInsertData.generateData(workspaceDataCache); 
        } catch (Exception ex) {
            tempEx = ex;
        } 
        this.genearteDataDialog.onGenerateOver(tempEx, generateAndInsertData.getSqlRecorder());
    }
    
}