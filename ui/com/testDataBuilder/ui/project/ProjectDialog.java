package com.testDataBuilder.ui.project;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JTextField;
import com.testDataBuilder.ui.core.JTDButton;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.util.Global;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public class ProjectDialog extends CenterDialog{

    static final Logger logger = Logger.getLogger(ProjectDialog.class);
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1999L;
    
    public static final String PROJ_CONFIG_DIR = "res/folder/";  //  @jve:decl-index=0: //$NON-NLS-1$
    public static final String GLOBAL_DATA_GENERATOR = "globalRole.xml"; //$NON-NLS-1$
    public static final String PROJECT_FILE_NAME = "project.xml";  //  @jve:decl-index=0: //$NON-NLS-1$
    

    /**
     * This method initializes 
     * 
     */
    public ProjectDialog() {
    	super();
    	initialize();
        center();
    }
    
    public ProjectDialog(Frame arg0) throws HeadlessException {
        super(arg0);
        initialize();
        center();
    }
    
    private String initTitle = RM.R("label.ProjectDialog.initTitle"); //$NON-NLS-1$
    private JPanel jContentPane = null;
    private JLabel labProjectName = null;
    private JTextField txtProjectName = null;
    private JLabel labProjectPath = null;
    private JTextField txtProjectPath = null;
    private JTDButton btnOK = null;
    private JTDButton btnCancel = null;
    private JTDButton btnSelDir = null;
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setLayout(null);
        this.setSize(new Dimension(384, 236));
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setTitle(initTitle);
        Helper.getInstance().enableHelpKey(
             this.getRootPane(), "detail.project.newProject"); //$NON-NLS-1$
        
    }

    /**
     * This method initializes jContentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            labProjectPath = new JLabel();
            labProjectPath.setText(RM.R("label.ProjectDialog.projectDir")); //$NON-NLS-1$
            labProjectPath.setBounds(new Rectangle(8, 83, 66, 18));
            labProjectName = new JLabel();
            labProjectName.setText(RM.R("label.ProjectDialog.projectName")); //$NON-NLS-1$
            labProjectName.setBounds(new Rectangle(8, 29, 64, 18));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getInnerPanel(), null);
            jContentPane.add(getBtnCancel(), null);
            jContentPane.add(getBtnOK(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes txtProjectName	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtProjectName() {
        if (txtProjectName == null) {
            txtProjectName = new JTextField();
            txtProjectName.setText("newProject"); //$NON-NLS-1$
            txtProjectName.setBounds(new Rectangle(87, 27, 223, 24));
        }
        return txtProjectName;
    }

    /**
     * This method initializes txtProjectPath	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtProjectPath() {
        if (txtProjectPath == null) {
            txtProjectPath = new JTextField();
            txtProjectPath.setBounds(new Rectangle(87, 80, 223, 24));
            File dir = new File(Global.getInstance().P.getWorkspace());
            dir.mkdir();
            txtProjectPath.setText(dir.getAbsolutePath());
        }
        return txtProjectPath;
    }

    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK.setBounds(new Rectangle(289, 167, 69, 24));
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ProjectDialog.this.createProject(e);
                }
            });
        }
        return btnOK;
    }

    protected void createProject(ActionEvent e) {
        String projectName = this.getTxtProjectName().getText().trim();
        if(StringUtils.isEmpty(projectName)){
            JOptionPane.showMessageDialog(this, RM.R("label.ProjectDialog.invalid.projectNameIsNull")); //$NON-NLS-1$
            txtProjectName.grabFocus();
            txtProjectName.selectAll();
            return;
        }
        
        projectPath = this.getTxtProjectPath().getText().trim();
        if(StringUtils.isEmpty(projectPath)){
            JOptionPane.showMessageDialog(this, RM.R("label.ProjectDialog.invalid.ProjectDirIsNull")); //$NON-NLS-1$
            txtProjectPath.grabFocus();
            txtProjectPath.selectAll();
            return;
        }
        projectPath = projectPath + Global.SEP + projectName;
        
        if(Global.isValidWorkspace(projectPath)){
            JOptionPane.showMessageDialog(this, 
                            String.format(RM.R("label.ProjectDialog.dirIsAnExistProject"), projectPath )); //$NON-NLS-1$ //$NON-NLS-2$
            txtProjectPath.grabFocus();
            txtProjectPath.selectAll();
            return;
        }
        File projectDir = null;
        try {
            projectDir = new File(projectPath);
            FileUtils.forceMkdir(projectDir);
            FileUtils.copyDirectory(new File(PROJ_CONFIG_DIR), projectDir);
            
            File projectXmlFile = new File(projectDir, PROJECT_FILE_NAME);
            String content = FileUtils.readFileToString(projectXmlFile,"utf-8");
            content = content.replace("projectName", projectName); //$NON-NLS-1$
            FileUtils.writeStringToFile(projectXmlFile,content, "utf-8"); //$NON-NLS-1$
            
            isOK = true;
            super.dispose();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(this, RM.R("label.ProjectDialog.error.createProject") + e1.getMessage()); //$NON-NLS-1$
            if(projectDir != null){
                try {
                    FileUtils.forceDelete(projectDir);
                } catch (IOException ex) {
                    logger.error("delete project dir", ex);
                }
            }
            return;
        } 
        
    }
    
    private boolean copyFile(String url, File file) throws IOException{
        InputStream is = null;
        FileOutputStream os = null;
        try{
            is = this.getClass().getResourceAsStream(url);
            os = FileUtils.openOutputStream(file);
            IOUtils.copy(is, os);            
        }finally{
            try{
                if(os != null){
                    os.flush();
                    os.close();
                }
                if(is != null){
                    is.close();
                }
            }catch(IOException ex){
                logger.error("close stream" , ex);
            }
        }
        
        return true;
    }
    
    private boolean isOK = false;
    private String projectPath = null;
    private JPanel innerPanel = null;
    
    public boolean isOK(){
        return isOK;
    }
    
    public String getProjectPath(){
        return this.projectPath;
    }
    
    /**
     * This method initializes btnCancel	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JTDButton();
            btnCancel.setText(RM.R("label.info.btnCancel")); //$NON-NLS-1$
            btnCancel.setBounds(new Rectangle(204, 167, 69, 24));
            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ProjectDialog.this.dispose();
                }
            });
        }
        return btnCancel;
    }

    /**
     * This method initializes btnSelDir	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSelDir() {
        if (btnSelDir == null) {
            btnSelDir = new JTDButton();
            btnSelDir.setText("..."); //$NON-NLS-1$
            btnSelDir.setBounds(new Rectangle(316, 81, 26, 23));
            btnSelDir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ProjectDialog.this.selProjectPath(e);
                }
            });
        }
        return btnSelDir;
    }

    protected void selProjectPath(ActionEvent e) {
        String curDir = this.getTxtProjectPath().getText().trim();
        JFileChooser directoryChooser = new JFileChooser(curDir);        
        directoryChooser.setDialogTitle(RM.R("label.ProjectDialog.dirChooser.title")); //$NON-NLS-1$
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (directoryChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String dirPath = directoryChooser.getSelectedFile().getAbsolutePath();
            this.getTxtProjectPath().setText(dirPath);           
        }
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
            innerPanel.setBounds(new Rectangle(13, 12, 350, 138));
            innerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            innerPanel.add(labProjectName, null);
            innerPanel.add(getTxtProjectName(), null);
            innerPanel.add(labProjectPath, null);
            innerPanel.add(getTxtProjectPath(), null);
            innerPanel.add(getBtnSelDir(), null);
        }
        return innerPanel;
    }

    /**
     * <p><code>main</code></p>
     * @param args
     * @author LiuXiaojie 2007-12-24
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ProjectDialog thisClass = new ProjectDialog();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
