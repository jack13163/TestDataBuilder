package com.testDataBuilder.ui.database;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import com.testDataBuilder.ui.core.JTDButton;

import com.testDataBuilder.config.DatabaseConfig;
import com.testDataBuilder.core.DatabaseUtil;
import com.testDataBuilder.exception.ValidataException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.sqlTemplate.IDBTemplate;
import com.testDataBuilder.sqlTemplate.TemplateFactory;
import com.testDataBuilder.sqlTemplate.XmlConfigTemplate;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.core.JTextFieldEx;

import javax.swing.WindowConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

public class ConfigDBDialog extends CenterDialog {

    static Logger logger = Logger.getLogger(ConfigDBDialog.class);
    
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JComboBox cbxTemplateName = null;

	private JLabel jLabel1 = null;

	private JTextField txtDriverClass = null;

	private JLabel jLabel2 = null;

	private JTextField txtURL = null;

	private JLabel jLabel3 = null;

	private JTextField txtUserName = null;

	private JLabel jLabel4 = null;

	private JPasswordField txtPwd = null;

	private JTDButton btnTestConn = null;

	private JTDButton btnCancel = null;
	
	private DatabaseConfig databaseConfig = null;
    
	/**
	 * @param owner
	 */
	public ConfigDBDialog(Frame owner) {
		super(owner);
		initialize();
		this.center();
	}

	/**
	 * @param owner
	 */
	public ConfigDBDialog(Frame owner, DatabaseConfig databaseConfig) {
		super(owner);		
		initialize();
		this.center();  
        this.databaseConfig = databaseConfig;
        try {
            if(databaseConfig != null){
                this.getTxtName().setText(databaseConfig.getName());
            }else{
                this.getTxtName().setEditable(true);
            }
            dataExchange(databaseConfig, true);
        } catch (Exception ex) {
        	logger.error("OpenDBDialog", ex);
            JOptionPane.showMessageDialog(this, RM.R("label.ConfigDBDialog.parseConfigFileError") + ex.getMessage());
        }
	}


	public void center(){
		Dimension screenSize = this.getToolkit().getScreenSize();
		int x = (screenSize.width - this.getWidth()) / 2;
		int y = (screenSize.height - this.getHeight()) /2;
		this.setLocation(x, y);
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(428, 273);
        this.setResizable(false);
		this.setTitle(RM.R("label.ConfigDBDialog.title")); //$NON-NLS-1$
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel4 = new JLabel();
			jLabel4.setText(RM.R("label.ConfigDBDialog.password"));
			jLabel4.setBounds(new Rectangle(19, 172, 43, 20));
			jLabel3 = new JLabel();
			jLabel3.setText(RM.R("label.ConfigDBDialog.userName"));
			jLabel3.setBounds(new Rectangle(19, 140, 43, 20));
			jLabel2 = new JLabel();
			jLabel2.setText(RM.R("label.ConfigDBDialog.URL"));
			jLabel2.setBounds(new Rectangle(19, 108, 43, 20));
			jLabel1 = new JLabel();
			jLabel1.setText(RM.R("label.ConfigDBDialog.jdbcDriver"));
			jLabel1.setBounds(new Rectangle(19, 76, 43, 20));
			jLabel = new JLabel();
			jLabel.setText(RM.R("label.ConfigDBDialog.type"));
			jLabel.setBounds(new Rectangle(19, 13, 43, 20));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnTestConn(), null);
			jContentPane.add(getBtnCancel(), null);
			jContentPane.add(getBtnOK(), null);
			jContentPane.add(getJInnerPanel(), null);
			jContentPane.add(getBtnSaveAsTemplate(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes cbxTemplateName	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbxTemplateName() {
		if (cbxTemplateName == null) {
			cbxTemplateName = new JComboBox();
			cbxTemplateName.setBounds(new Rectangle(69, 13, 305, 20));
			
			cbxTemplateName.addItemListener(new java.awt.event.ItemListener() {
			    public void itemStateChanged(java.awt.event.ItemEvent e) {
                    ConfigDBDialog.this.cbxTemplateNameClicked();
			    }
			});
           
			List<String> types = TemplateFactory.getInstance().getAllTypes();
			for(String type : types){
				cbxTemplateName.addItem(type);
			}
		}
		return cbxTemplateName;
	}

	private void cbxTemplateNameClicked(){
        if(cbxTemplateName.getSelectedItem() != null){
    		IDBTemplate template = TemplateFactory.getInstance()
    				.getTemplate(cbxTemplateName.getSelectedItem().toString());
    		if(template != null){
                dataExchange(template, true);
    		}
        }
	}
	
	private void dataExchange(IDBTemplate template, boolean isToControl){
        if(isToControl){
            if(template != null){
                String templateName = (String) this.getCbxTemplateName().getSelectedItem();
                if(StringUtils.isNotEmpty(templateName) 
                        && StringUtils.isNotEmpty(template.getTemplateName())
                        && !templateName.equalsIgnoreCase(template.getTemplateName())){
                    this.getCbxTemplateName().setSelectedItem(template.getTemplateName());
                }                
                this.getTxtDriverClass().setText(template.getDriverClass());
        		this.getTxtURL().setText(template.getURL());
        		this.getTxtUserName().setText(template.getUserName());
        		this.getTxtPwd().setText(template.getPwd());                
            }
        }else{
            if(template instanceof DatabaseConfig){
                DatabaseConfig databaseConfig = (DatabaseConfig)template;
                databaseConfig.setName(this.getTxtName().getText().trim());
                databaseConfig.setTemplateName((String) this.getCbxTemplateName().getSelectedItem());
                databaseConfig.setDriverClass(this.getTxtDriverClass().getText().trim());
                databaseConfig.setURL(this.getTxtURL().getText().trim());
                databaseConfig.setUserName(this.getTxtUserName().getText().trim());
                databaseConfig.setPassword(this.getTxtPwd().getText());
            }
        }
	}
	/**
	 * This method initializes txtDriverClass	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtDriverClass() {
		if (txtDriverClass == null) {
			txtDriverClass = new JTextFieldEx();
			txtDriverClass.setBounds(new Rectangle(69, 76, 305, 20));
		}
		return txtDriverClass;
	}

	/**
	 * This method initializes txtURL	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtURL() {
		if (txtURL == null) {
			txtURL = new JTextFieldEx();
			txtURL.setBounds(new Rectangle(70, 108, 305, 20));
		}
		return txtURL;
	}

	/**
	 * This method initializes txtUserName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtUserName() {
		if (txtUserName == null) {
			txtUserName = new JTextFieldEx();
			txtUserName.setBounds(new Rectangle(70, 140, 305, 20));
		}
		return txtUserName;
	}

	/**
	 * This method initializes txtPwd	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getTxtPwd() {
		if (txtPwd == null) {
			txtPwd = new JPasswordField();
			txtPwd.setBounds(new Rectangle(69, 172, 305, 20));
		}
		return txtPwd;
	}

	/**
	 * This method initializes btnLogin	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnTestConn() {
		if (btnTestConn == null) {
			btnTestConn = new JTDButton();
			btnTestConn.setBounds(new Rectangle(257, 215, 67, 25));
			btnTestConn.setText(RM.R("label.ConfigDBDialog.Test")); //$NON-NLS-1$
			btnTestConn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent ev) {
					try {
						if(ConfigDBDialog.this.testConnection()){
						    JOptionPane.showMessageDialog(ConfigDBDialog.this,RM.R("label.ConfigDBDialog.connect.success")); //$NON-NLS-1$
                        }
					} catch (SQLException e) {
                        System.out.println(e.getMessage());
                        if(e.getMessage().contains("No suitable driver")){ //$NON-NLS-1$
                            JOptionPane.showMessageDialog(ConfigDBDialog.this,
                                            RM.R("label.ConfigDBDialog.connect.error.NoJDBCDriver")); //$NON-NLS-1$
                        }else{
    						JOptionPane.showMessageDialog(ConfigDBDialog.this,
    								"连接数据库出错!["  + e.getMessage() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
                        }
					} 
				}
			});
		}
		return btnTestConn;
	}
	
    private boolean ok = false;

    private JTDButton btnOK = null;

    private JPanel jInnerPanel = null;

	private JLabel jLabel5 = null;

	private JTextField txtName = null;

    private JTDButton btnSaveAsTemplate = null;
    
	protected boolean testConnection() throws SQLException {
        try {
            checkInvalid();
        } catch (ValidataException e1) {
           JOptionPane.showMessageDialog(this, e1.getMessage());
           if(e1.getControl() instanceof JTextField){
               JTextField txt = (JTextField)e1.getControl();
               txt.grabFocus();
           }
           return false;
        }
	    DatabaseConfig databaseConfig = new DatabaseConfig();
        this.dataExchange(databaseConfig, false);
		return DatabaseUtil.testConnect(databaseConfig);
	}

    protected void saveConfig() throws SAXException, IOException{
        if(this.databaseConfig == null){
            this.databaseConfig = new DatabaseConfig();
        }
        this.dataExchange(databaseConfig, false);
    }
    
  
    
	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JTDButton();
			btnCancel.setBounds(new Rectangle(179, 215, 67, 25));
			btnCancel.setText(RM.R("label.ConfigDBDialog.cancel")); //$NON-NLS-1$
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ConfigDBDialog.this.destroy();
				}
			});
		}
		return btnCancel;
	}
	
	private  void destroy(){
		this.dispose();
	}

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(336, 215, 67, 25));
            btnOK.setText(RM.R("label.ConfigDBDialog.ok")); //$NON-NLS-1$
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ConfigDBDialog.this.onOK(e);
                }
            });
        }
        return btnOK;
    }

    protected void onOK(ActionEvent e) {
        try {
            try {
                checkInvalid();
            } catch (ValidataException e1) {
               JOptionPane.showMessageDialog(this, e1.getMessage());
               if(e1.getControl() instanceof JTextField){
                   JTextField txt = (JTextField)e1.getControl();
                   txt.grabFocus();
               }
               return;
            }
            saveConfig();
        } catch (SAXException e1) {
        	logger.error("OpenDBDialog", e1); //$NON-NLS-1$
            JOptionPane.showMessageDialog(this, RM.R("label.ConfigDBDialog.XmlFileOperateError") + e1.getMessage()); //$NON-NLS-1$
        } catch (IOException e1) {
        	logger.error("OpenDBDialog", e1); //$NON-NLS-1$
           JOptionPane.showMessageDialog(this, RM.R("label.ConfigDBDialog.saveConfigFileError") + e1.getMessage()); //$NON-NLS-1$
        }
        ok = true;        
        this.destroy();
    }

    protected void checkInvalid() throws ValidataException{
        if(StringUtils.isEmpty(getTxtName().getText())){
            throw new ValidataException(getTxtName(), RM.R("label.ConfigDBDialog.invalid.nameIsNull"));
        }
        if(StringUtils.isEmpty(getTxtDriverClass().getText())){
            throw new ValidataException(getTxtDriverClass(), RM.R("label.ConfigDBDialog.invalid.driverIsNull"));
        }
        if(StringUtils.isEmpty(getTxtURL().getText())){
            throw new ValidataException(getTxtURL(), RM.R("label.ConfigDBDialog.invalid.URLIsNull")); //$NON-NLS-1$
        }
        if(StringUtils.isEmpty(getTxtUserName().getText())){
            throw new ValidataException(getTxtUserName(), RM.R("label.ConfigDBDialog.invalid.userNameIsNull")); //$NON-NLS-1$
        }        
    }
    /**
     * This method initializes jInnerPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJInnerPanel() {
        if (jInnerPanel == null) {
            jLabel5 = new JLabel();
            jLabel5.setBounds(new Rectangle(19, 45, 43, 20));
            jLabel5.setText(RM.R("label.ConfigDBDialog.name")); //$NON-NLS-1$
            jInnerPanel = new JPanel();
            jInnerPanel.setLayout(null);
            jInnerPanel.setBounds(new Rectangle(12, 3, 393, 206));
            jInnerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            jInnerPanel.add(jLabel, null);
            jInnerPanel.add(getCbxTemplateName(), null);
            jInnerPanel.add(jLabel1, null);
            jInnerPanel.add(getTxtDriverClass(), null);
            jInnerPanel.add(jLabel2, null);
            jInnerPanel.add(getTxtURL(), null);
            jInnerPanel.add(jLabel3, null);
            jInnerPanel.add(getTxtUserName(), null);
            jInnerPanel.add(jLabel4, null);
            jInnerPanel.add(getTxtPwd(), null);
            jInnerPanel.add(jLabel5, null);
            jInnerPanel.add(getTxtName(), null);
        }
        return jInnerPanel;
    }

	/**
	 * This method initializes txtName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	public JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextFieldEx();
			txtName.setBounds(new Rectangle(68, 45, 305, 20));
			txtName.setEditable(false);
		}
		return txtName;
	}

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    /**
     * This method initializes btnSaveAsTemplate	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSaveAsTemplate() {
        if (btnSaveAsTemplate == null) {
            btnSaveAsTemplate = new JTDButton();
            btnSaveAsTemplate.setBounds(new Rectangle(14, 215, 114, 25));
            btnSaveAsTemplate.setText(RM.R("label.ConfigDBDialog.saveAsTemplate")); //$NON-NLS-1$
            btnSaveAsTemplate.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                   ConfigDBDialog.this.saveAsTemplate();
                }
            });
        }
        return btnSaveAsTemplate;
    }

    protected void saveAsTemplate() {
        try {
            checkInvalid();
        } catch (ValidataException ex) {
           JOptionPane.showMessageDialog(this, ex.getMessage());
           if(ex.getControl() instanceof JTextField){
               JTextField txt = (JTextField)ex.getControl();
               txt.grabFocus();
           }
           return;
        }
        
        String templateFileName = JOptionPane.showInputDialog(this, RM.R("label.ConfigDBDialog.pleaseInputTemplateFileName")); //$NON-NLS-1$
        if(StringUtils.isNotEmpty(templateFileName)){
            File configFile = new File(TemplateFactory.TEMPLATE_DIR,templateFileName + ".xml");
            XmlConfigTemplate xmlConfigTemplate = new XmlConfigTemplate();
            this.dataExchange(xmlConfigTemplate, false);
            xmlConfigTemplate.setTemplateName(null);
            xmlConfigTemplate.setName(templateFileName);
            try {
                xmlConfigTemplate.storeToXmlFile(configFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, String.format(RM.R("label.ConfigDBDialog.ErrorOnSaveAsTemplate"),  //$NON-NLS-1$
                                new Object[] {e.getMessage()}) ); //$NON-NLS-1$
            }
        }else{
            JOptionPane.showMessageDialog(this,RM.R("label.ConfigDBDialog.nullTemplateName"));
        }
    }
}