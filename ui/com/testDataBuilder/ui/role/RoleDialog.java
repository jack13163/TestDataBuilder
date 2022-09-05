package com.testDataBuilder.ui.role;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Rectangle;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.core.Insert2Table;
import com.testDataBuilder.core.baseType.JavaTypes;
import com.testDataBuilder.core.role.JavaRole;
import com.testDataBuilder.core.role.JavaSource;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.core.role.EnumList;
import com.testDataBuilder.core.role.EnumObj;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.CompileException;
import com.testDataBuilder.exception.JavaCodeRuntimeException;
import com.testDataBuilder.exception.ValidataException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.core.JTextFieldEx;
import com.testDataBuilder.ui.core.ProgressLable;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.dynamicCompile.DynamicCompiler;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.ui.main.MainFrame;
import com.testDataBuilder.ui.main.queryConsole.ConnectionComboBox;
import com.testDataBuilder.ui.main.queryConsole.JEditTextArea;
import com.testDataBuilder.ui.main.queryConsole.QueryDialog;
import com.testDataBuilder.ui.preference.PreferenceDialog;
import com.testDataBuilder.util.Global;
import com.testDataBuilder.util.StringUtil;

import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;
import org.syntax.jedit.tokenmarker.TSQLTokenMarker;
import javax.swing.JTabbedPane;


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
public class RoleDialog extends CenterDialog{

    static Logger logger = Logger.getLogger(PreferenceDialog.class);
    
	private static final long serialVersionUID = 1L;
	private JSplitPane jSplitPane = null;	
	private JPanel jContentPane = null;	
	private JRoleToolBar jToolBar = null;
	private JScrollPane leftPane = null;
	private JPanel rightPanel = null;
	private JRoleTree roleTree = null;
	private JLabel jLabel = null;
	private JTextField txtName = null;
	private JLabel jLabel10;
	private JComboBox cbxReturnType;
	private JLabel jLabel1 = null;
	private JavaTypeComboBox cbxType = null;
	private JLabel jLabel2 = null;
	private JComboBox cbxMethod = null;
	private JLabel labMin = null;
	private JTextField txtMin = null;
	private JLabel labMax = null;
	private JTextField txtMax = null;
	private JLabel jLabel3 = null;
	private JScrollPane jEnumlPane = null;
	private JEnumTable jEnumTable = null;
	private JLabel jLabel4 = null;
	private JTextField txtPrefix = null;
	private JLabel jLabel5 = null;
	private JTextField txtSuffix = null;
	private JTDButton btnAddEnum = null;
	private JTDButton btnDelEnum = null;
	private JLabel jLabel6 = null;
	private JScrollPane jSQLPane = null;
	private JEditTextArea txtSQL = null;
	private JTDButton btnShowSQLQueryDialog = null;
	private JTDButton btnSave = null;
	private JTDButton btnOK = null;
	private JTDButton btnClose = null;	
	private WorkspaceDataCache workspaceDataCache;  //  @jve:decl-index=0:
    Role curRole = null;  //  @jve:decl-index=0:
    RoleFactory curRoleFactory = null;  //  @jve:decl-index=0:
    
    //String curTableName = null;
    protected Object cbxInva;
    private JButton btnEditInNewWindow;
    private JTextField txtReturnFields;
    private JLabel jLabel9;
    private JEditTextArea txtJavaSource;
    private JLabel jLabel7 = null;
    private ConnectionComboBox comboxConnection = null;
    private JTDButton btnSelEnum = null;
    private JLabel jLabel8 = null;
    private JPanel jJavaSourcePanel;
    private JTextField txtNullPercent = null;

    private JTabbedPane jMethodTabbedPane = null;

    private JPanel jIncRandomPanel = null;

    private JPanel jEnumPanel = null;

    private JPanel jSQLQueryPanel = null;
  
    private ProgressLable msgLine = null;
    
    public ProgressLable getMsgLine(){
        if(msgLine == null){
            msgLine = new ProgressLable(" ");             //$NON-NLS-1$
        }
        return msgLine;
    }
    
    /**
	 * This is the default constructor
	 */
	public RoleDialog(JFrame parent, WorkspaceDataCache workspaceDataCache) {
		super(parent);
		this.workspaceDataCache = workspaceDataCache;		
		initialize();
		center();
		initControl();
		
	}

    
    public MainFrame getParent(){
        return (MainFrame)super.getParent();
    }
    
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(650, 500);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setJMenuBar(new JRoleMenuBar(this));
		this.setContentPane(getJContentPane());
		this.setTitle(RM.R("label.RoleDialog.title"));
        this.enableControl(false);
        Helper.getInstance().enableHelpKey(this.getRootPane(), "detail.role.userRole");
	}
    
    @Override
    public JRoleMenuBar getJMenuBar() {
        return (JRoleMenuBar)super.getJMenuBar();
    }
    
	/**
	 * 根据配置文件初始化控�?
	 * <p><code>initControl</code></p>
	 * @author LiuXiaojie 2007-7-7
	 */
	private void initControl() {
        this.getRoleTree().setData(this.workspaceDataCache);
	}

//    public void refreshData(){
//    	this.getRoleTree().clear();
//    	this.getRoleTree().setData(this.workspaceDataCache);
//    }

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
//            jContentPane.add(getJToolBar(), BorderLayout.NORTH);
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
			jContentPane.add(getMsgLine(),BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes jToolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JRoleToolBar getJToolBar() {
		if (jToolBar == null) {
			jToolBar = new JRoleToolBar(this);
		}
		return jToolBar;
	}
	
	
	protected void showTablePopupMenu(Component invoker, int x, int y) {
		JPopupMenu pupupMenu = this.getJMenuBar().getTablePopupMenu();
		pupupMenu.show(invoker, x, y);
	}

    
	protected void deleteSelRole() {
        if(curRole != null){
            try {
                int result = JOptionPane.showConfirmDialog(this, RM.R("label.RoleDialog.info.confirm.delete"));
                if(result == JOptionPane.OK_OPTION){
                    this.getCurRoleFactory().deleteItem(curRole);
                    this.getRoleTree().updateUI();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, RM.R("label.RoleDialog.error.delete") + e.getMessage());
            } 
        }
    
    }


    protected void deleteAll() {
		int result = JOptionPane.showConfirmDialog(this, RM.R("label.RoleDialog.info.confirm.deleteAll"));
		if(result == JOptionPane.OK_OPTION){
			try {
				this.getCurRoleFactory().deleteAll();
				this.getRoleTree().updateUI();
			} catch (Exception e1) {
				logger.error("deleteAll", e1);
				JOptionPane.showMessageDialog(this, RM.R("label.RoleDialog.error.deleteAll") + e1.getMessage());
			} 
		}
	}

    public Role addRole(ActionEvent e){
        if(!this.getRoleTree().isSelectionEmpty()){
            Role role = this.getCurRoleFactory().addDefaultRole();
            this.getRoleTree().updateUI();
            this.getTxtName().grabFocus();
            int index[] = this.getRoleTree().getSelectionRows();
            if(index.length == 1){
                int selRow = index[0] + this.getCurRoleFactory().getRoles().size();
                this.getRoleTree().setSelectionRow(selRow);   
            }else{
                System.out.println("error sel much rows。[" + index.length);
            }
            return role;
        }
        return null;
    }

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerSize(3);
			jSplitPane.setLeftComponent(getLeftPane());
			jSplitPane.setRightComponent(getRightPanel());
			jSplitPane.setDividerLocation(180);
			jSplitPane.setResizeWeight(0.4);   
			//jSplitPane.setPreferredSize(new java.awt.Dimension(606, 490));

		}
		return jSplitPane;
	}

	/**
	 * This method initializes leftPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getLeftPane() {
		if (leftPane == null) {
			leftPane = new JScrollPane();
			leftPane.setViewportView(getRoleTree());
		}
		return leftPane;
	}

	/**
	 * This method initializes rightPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRightPanel() {
		if (rightPanel == null) {
			jLabel8 = new JLabel();
			jLabel8.setBounds(237, 54, 69, 23);
			jLabel8.setText(RM.R("label.RoleDialog.label.nullPercent"));
			jLabel7 = new JLabel();
			jLabel7.setText(RM.R("label.RoleDialog.label.useConnection"));
			jLabel7.setBounds(11, 7, 75, 24);
			jLabel6 = new JLabel();
			jLabel6.setText(RM.R("label.RoleDialog.label.sqlQuery"));
			jLabel6.setBounds(new Rectangle(670, 222, 60, 19));
			jLabel5 = new JLabel();
			jLabel5.setBounds(237, 87, 67, 25);
			jLabel5.setText(RM.R("label.RoleDialog.label.suffix"));
			jLabel4 = new JLabel();
			jLabel4.setBounds(23, 87, 64, 25);
			jLabel4.setText(RM.R("label.RoleDialog.label.prefix"));
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(673, 172, 60, 22));
			jLabel3.setText(RM.R("label.RoleDialog.label.enumValue"));
			labMax = new JLabel();
			labMax.setText(RM.R("label.RoleDialog.label.maxLength"));
			labMax.setBounds(67, 88, 69, 25);
			labMin = new JLabel();
			labMin.setText(RM.R("label.RoleDialog.label.minLength"));
			labMin.setBounds(68, 54, 69, 25);
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(24, 124, 63, 25));
			jLabel2.setText(RM.R("label.RoleDialog.label.generateMethod"));
			jLabel1 = new JLabel();
			jLabel1.setBounds(23, 55, 69, 23);
			jLabel1.setText(RM.R("label.RoleDialog.label.javaType"));
			jLabel = new JLabel();
			jLabel.setBounds(23, 16, 69, 25);
			jLabel.setText(RM.R("label.RoleDialog.label.genName"));
			rightPanel = new JPanel();
			rightPanel.setLayout(null);
			rightPanel.setPreferredSize(new java.awt.Dimension(437, 442));
            rightPanel.add(jLabel, null);
			rightPanel.add(getTxtName(), null);
			rightPanel.add(jLabel1, null);
			rightPanel.add(getCbxType(), null);
			rightPanel.add(jLabel2, null);
			rightPanel.add(getCbxMethod(), null);
			rightPanel.add(jLabel3, null);
			rightPanel.add(jLabel4, null);
			rightPanel.add(getTxtPrefix(), null);
			rightPanel.add(jLabel5, null);
			rightPanel.add(getTxtSuffix(), null);
			rightPanel.add(getBtnSave(), null);
			rightPanel.add(getBtnOK(), null);
			rightPanel.add(getBtnClose(), null);
            rightPanel.add(jLabel8, null);
            rightPanel.add(getTxtNullPercent(), null);
            rightPanel.add(getJMethodTabbedPane(), null);
            rightPanel.add(jLabel6, null);
		}
		return rightPanel;
	}

	/**
	 * This method initializes datatypeList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JRoleTree getRoleTree() {
		if (roleTree == null) {
			roleTree = new JRoleTree();
			roleTree.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					RoleDialog.this.treeMouseClicked(e);
				}
			});
            roleTree.addTreeSelectionListener(new TreeSelectionListener()
            {
                public void valueChanged(TreeSelectionEvent arg0) {
                    RoleDialog.this.treeSelChange();
                }                
            });
			
		}
		
		return roleTree;			
	}

    protected void treeSelChange(){
        if(this.getRoleTree().getSelectionPath() == null){
            return ;
        }
        
        Object[] paths = this.getRoleTree().getSelectionPath().getPath();
        if(isSelRole(paths)){
            Role dataGeneraor = (Role)paths[2];
            if(dataGeneraor != curRole){
                curRole = dataGeneraor;
                synchorControl(curRole);
            }               
        }else{
            curRole = null;
            synchorControl(curRole);
        }
        
        if(isSelTableAndRoles(paths)){
            RoleFactory roleFactory = (RoleFactory)paths[1];
            
            if(roleFactory != this.curRoleFactory){
                this.curRoleFactory = roleFactory;
            }
        }
    }
    
	protected void treeMouseClicked(MouseEvent e) {
		JTree tree = (JTree) e.getComponent();
        TreePath treePath = null;
        if (e.getButton() == 3)/* 右键 */{
            treePath = tree.getPathForLocation(e.getX(), e.getY());
            if(treePath == null){
                return;
            }
            tree.setSelectionPath(treePath);
        }
        
		treePath = tree.getSelectionPath();
		if(treePath == null){
			return;
		}
		
		Object[] paths = treePath.getPath();
		if(e.getButton() == 3 && isSelTable(paths)){
			RoleDialog.this.showTablePopupMenu(e.getComponent(),e.getX(), e.getY());
		}else if(e.getButton() == 3 && isSelRole(paths)){
            RoleDialog.this.showRolePopupMenu(e.getComponent(),e.getX(), e.getY());
        }
	}

	
	private void showRolePopupMenu(Component component, int x, int y) {
        JPopupMenu rolePopupMenu = this.getJMenuBar().getRolePopupMenu();
        rolePopupMenu.show(component, x, y);
        
    }

    
    private boolean isSelTable(Object[] paths){
		return (paths != null && paths.length == 2);
	}
	
    private boolean isSelTableAndRoles(Object[] paths){
        return (paths != null && paths.length >= 2);
    }

    private boolean isSelRole(Object[] paths){		
		return (paths != null && paths.length == 3);
	}
	
	protected void synchorControl(Role role) {
		dataExchange(role, true);
	}
	
	private void enableControl(boolean enable){
		this.getTxtName().setEnabled(enable);
        this.getCbxType().setEnabled(enable);
        this.getCbxMethod().setEnabled(enable);
        this.getTxtMin().setEnabled(enable);
        this.getTxtMax().setEnabled(enable);
        this.getTxtPrefix().setEnabled(enable);
        this.getTxtSuffix().setEnabled(enable);
        this.getJEnumTable().setEnabled(enable);
        this.getComboxConnection().setEnabled(enable);
        this.getTxtSQL().setEnabled(enable);
        this.getBtnSave().setEnabled(enable);
        this.getBtnOK().setEnabled(enable);
        this.getBtnAddEnum().setEnabled(enable);
        this.getBtnDelEnum().setEnabled(enable);
        this.getBtnShowSQLQueryDialog().setEnabled(enable);
        this.getBtnSelEnum().setEnabled(enable);
        this.getTxtNullPercent().setEnabled(enable);
        this.getCbxReturnType().setEnabled(enable);
        //this.getTxtReturnFields().setEnabled(enable);
        this.getTxtJavaSource().setEnabled(enable);
	}

    /**
     * 数据交换.
     * <p><code>dataExchange</code></p>
     * @param role 数据
     * @param dataToConstrol 标识数据是流向控件的.
     * @author LiuXiaojie 2007-7-7
     */
	protected void dataExchange(Role role, boolean isToControl){
        if(isToControl){
        	if(role != null){
        		this.enableControl(true);
	            this.getTxtName().setText(role.getName());
	            this.getCbxType().setSelectedItem(role.getType());
	            this.getCbxMethod().setSelectedItem(role.getMethod());
	            this.getTxtMin().setText(role.getStringMin());
	            this.getTxtMax().setText(role.getStringMax());
	            this.getTxtPrefix().setText(role.getPrefix());
	            this.getTxtSuffix().setText(role.getSuffix());
                this.getTxtNullPercent().setText(role.getNullPercent() + "");
                 EnumList<EnumObj> tempEnumList = Role.cloneEnumList(role.getEnumerate());
		         this.getJEnumTable().setData(tempEnumList);

		         if(role.isSQLQueryMethod() || role.isSQLFuncMethod()){
		            this.getTxtSQL().setText(role.getSQL());
		            if(StringUtils.isEmpty(role.getSourceName())){
	                    this.getComboxConnection().setSelectedIndex(0);
	                }else{
	                    this.getComboxConnection().setSelectedItem(role.getSourceName());
	                }
                }else if(role.isJavaMethod()){
                	this.getCbxReturnType().setSelectedItem(role.getJavaSource().getReturnType());
                	this.getTxtReturnFields().setText(role.getJavaSource().getReturnFields());
                	this.getTxtJavaSource().setText(role.getJavaSource().getJavaCode());
                	this.getTxtJavaSource().setFirstLine(14);
                }
        	}else{
        		this.getTxtName().setText("");
	            this.getCbxType().setSelectedItem(1);
	            this.getCbxMethod().setSelectedItem(1);
                this.getTxtNullPercent().setText("");
	            this.getTxtMin().setText("");
	            this.getTxtMax().setText("");
	            this.getTxtPrefix().setText("");
	            this.getTxtSuffix().setText("");
	            this.getJEnumTable().setData(null);
	            this.getTxtSQL().setText("");
	            this.getCbxReturnType().setSelectedIndex(0);
	            this.getTxtJavaSource().setText("");
	            this.getTxtReturnFields().setText("");
	            this.getComboxConnection().setSelectedIndex(0);
	            this.enableControl(false);
        	}
        }else{
            role.setName(this.getTxtName().getText().trim());
            String type = this.getCbxType().getSelectedItem().toString();
            Class javaType = this.getCbxType().getSelectedClass();
            role.setType(javaType);
            role.setNullPercent(Float.valueOf(this.getTxtNullPercent().getText().trim()));
            role.setPrefix(this.getTxtPrefix().getText().trim());
            role.setSuffix(this.getTxtSuffix().getText().trim());
            
            String method =this.getCbxMethod().getSelectedItem().toString(); 
            role.setMethod(method);
            
            if(Role.METHOD_INCREMENT.equalsIgnoreCase(method)
                ||Role.METHOD_RANDOM.equalsIgnoreCase(method)){
                if(type.equalsIgnoreCase("DATE")){
                    try {
                        role.setMaxDate(Global.getInstance().parseDateTime(this.getTxtMax().getText().trim()));
                    } catch (BaseException e) {
                        logger.error("ConfigDatatype", e);
                    }
                    try {
                        role.setMinDate(Global.getInstance().parseDateTime(this.getTxtMin().getText().trim()));
                    } catch (BaseException e) {
                        logger.error("ConfigDatatype", e);
                    }                
                }else{
                    try{
                        double max = JavaTypes.getInstance().getDbValue(javaType, this.getTxtMax().getText().trim());
                        role.setMax(max);
                        double min = JavaTypes.getInstance().getDbValue(javaType, this.getTxtMin().getText().trim());
                        role.setMin(min);
                    }catch(BaseException ex){
                        logger.error("RoleDialog", ex);
                    }
                }
            }else if(Role.METHOD_ENUM.equalsIgnoreCase(method)){
                EnumList<EnumObj> tempEnumList = Role.cloneEnumList(this.getJEnumTable().getData());
                role.setEnumerate(tempEnumList);
            }else if(Role.METHOD_SQL_QUERY.equalsIgnoreCase(method)
            		||Role.METHOD_SQL_FUNC.equalsIgnoreCase(method)){
                role.setSQL(this.getTxtSQL().getText().trim());
                role.setSourceName((String) this.getComboxConnection().getSelectedItem());
            } else if(Role.METHOD_JAVA.equals(method)){
            	try {
            		String returnType = (String) this.getCbxReturnType().getSelectedItem();
            		JavaSource javaSource = role.getJavaSource();
            		            		
            		if(javaSource == null){
            			javaSource = new JavaSource();
            			role.setJavaSource(javaSource);
            		}
            		javaSource.setReturnType(returnType);
            		
            		if(JavaSource.RETURN_TYPE_SIMPLE_OBJ.equalsIgnoreCase(returnType)){
            			javaSource.setJavaCode(this.getTxtJavaSource().getText());
            		}else{
            			javaSource.setJavaCode(this.getTxtJavaSource().getText());
            			javaSource.setReturnFields(this.getTxtReturnFields().getText());
            		}
				} catch (BaseException ex) {
					logger.error("setJavaCode",ex);
				}
            }
        }
    }

	/**
	 * This method initializes txtName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextFieldEx();
			txtName.setBounds(98, 16, 340, 25);            
		}
		return txtName;
	}

	/**
	 * This method initializes cbxType	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JavaTypeComboBox getCbxType() {
		if (cbxType == null) {
            List<Class> types = JavaTypes.getInstance().getAllTypes();
			cbxType = new JavaTypeComboBox(types);
			cbxType.setBounds(98, 53, 121, 27);            
            cbxType.addItemListener(new ItemListener(){
                public void itemStateChanged(ItemEvent e) {
                    RoleDialog.this.typeChange();
                }
                
            });
            cbxType.addPopupMenuListener(new PopupMenuListener(){
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    RoleDialog.this.cbxInvasible();
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                    
                }

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    
                }

            });
           
		}
		return cbxType;
	}

	protected void cbxInvasible() {
        Class type = this.getCbxType().getSelectedClass();        
        double min = JavaTypes.getInstance().getMin(type, 0, 0);
        String strMin = JavaTypes.getInstance().getStringValue(type, min);
        double max = JavaTypes.getInstance().getMax(type, 0, 0);
        String strMax = JavaTypes.getInstance().getStringValue(type, max);
        this.getTxtMin().setText(strMin);
        this.getTxtMax().setText(strMax);
    }

    protected void typeChange() {
		Class type = this.getCbxType().getSelectedClass();
        
		if(type != null && type.equals(JavaTypes.STRING)){
			this.labMax.setText(RM.R("label.RoleDialog.label.maxLength"));
			this.labMin.setText(RM.R("label.RoleDialog.label.minLength"));
		}else{
			this.labMax.setText(RM.R("label.RoleDialog.label.maxValue"));
			this.labMin.setText(RM.R("label.RoleDialog.label.minValue"));
		}
	}
	

	/**
	 * This method initializes cbxMethod	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCbxMethod() {
		if (cbxMethod == null) {
			cbxMethod = new JComboBox();
			cbxMethod.setBounds(99, 123, 120, 27);
			cbxMethod.addItemListener(new java.awt.event.ItemListener() {
			    public void itemStateChanged(java.awt.event.ItemEvent e) {
			        RoleDialog.this.methodBoxStateChanged(e);
			    }
			});
			cbxMethod.addItem(Role.METHOD_INCREMENT);
            cbxMethod.addItem(Role.METHOD_RANDOM);
            cbxMethod.addItem(Role.METHOD_ENUM);
            cbxMethod.addItem(Role.METHOD_SQL_QUERY);
            cbxMethod.addItem(Role.METHOD_SQL_FUNC);
            cbxMethod.addItem(Role.METHOD_JAVA);
		}
		return cbxMethod;
	}

	protected void methodBoxStateChanged(ItemEvent e) {
        String method = (String) getCbxMethod().getSelectedItem();
        int index = 0;

        if(Role.METHOD_INCREMENT.equalsIgnoreCase(method)
          ||Role.METHOD_RANDOM.equalsIgnoreCase(method)){
            index = 0;            
        }else if(Role.METHOD_ENUM.equalsIgnoreCase(method)){
            index = 1;
        }else if(Role.METHOD_SQL_QUERY.equalsIgnoreCase(method)
        		||Role.METHOD_SQL_FUNC.equalsIgnoreCase(method)){
            index = 2;
            this.getCbxType().setSelectedItem(JavaTypes.OBJECT);
        }else if(Role.METHOD_JAVA.equalsIgnoreCase(method)){
        	index = 3;
        	this.getCbxType().setSelectedItem(JavaTypes.OBJECT);
        	String javaSource = this.getTxtJavaSource().getText();
        	if(StringUtils.isEmpty(javaSource)){
        		try {
					javaSource = JavaRole.getJavaSourceTemplate();
				} catch (IOException ex) {
					logger.error("methodBoxStateChanged", ex);
				}
        		this.getTxtJavaSource().setText(javaSource);
        	}
        }
        
        selectTab(index);
    }

    protected void selectTab(int index){
        this.getJMethodTabbedPane().setSelectedIndex(index);
        int count = this.getJMethodTabbedPane().getTabCount();
        for(int i=0;i <count;i++){
            boolean enable = (i == index);
            this.getJMethodTabbedPane().setEnabledAt(i, enable);
        }
    }

    /**
	 * This method initializes txtMin	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtMin() {
		if (txtMin == null) {
			txtMin = new JTextFieldEx();
			txtMin.setBounds(153, 54, 106, 25);
		}
		return txtMin;
	}

    /**
	 * This method initializes txtMax	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtMax() {
		if (txtMax == null) {
			txtMax = new JTextFieldEx();
			txtMax.setBounds(153, 90, 106, 25);
		}
		return txtMax;
	}

	/**
	 * This method initializes jEnumlPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJEnumlPane() {
		if (jEnumlPane == null) {
			jEnumlPane = new JScrollPane();
			jEnumlPane.setBounds(12, 12, 323, 161);
			jEnumlPane.setViewportView(getJEnumTable());
		}
		return jEnumlPane;
	}

	/**
	 * This method initializes jEnumList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JEnumTable getJEnumTable() {
		if (jEnumTable == null) {
			jEnumTable = new JEnumTable();
			jEnumTable.setFont(new Font("Dialog", Font.BOLD, 14));
			jEnumTable.setToolTipText(RM.R("label.RoleDialog.enumTable.alt"));
			jEnumTable.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2){
						RoleDialog.this.editCurEnum();
					}
				}
		       });
		}
		return jEnumTable;
	}

	/**
	 * This method initializes txtPrefix	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtPrefix() {
		if (txtPrefix == null) {
			txtPrefix = new JTextFieldEx();
			txtPrefix.setBounds(99, 87, 120, 27);            
		}
		return txtPrefix;
	}

	/**
	 * This method initializes txtSuffix	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTxtSuffix() {
		if (txtSuffix == null) {
			txtSuffix = new JTextFieldEx();
			txtSuffix.setBounds(312, 88, 126, 25);            
		}
		return txtSuffix;
	}

	/**
	 * This method initializes btnAddEnum	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnAddEnum() {
		if (btnAddEnum == null) {
			btnAddEnum = new JTDButton();
			btnAddEnum.setFont(new Font("Dialog", Font.BOLD, 14));
			btnAddEnum.setBounds(353, 47, 44, 24);
			btnAddEnum.setText("+");
            btnAddEnum.setToolTipText(RM.R("label.RoleDialog.info.addEnum.alt"));
			btnAddEnum.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					RoleDialog.this.addEnum();
				}
			});
		}
		return btnAddEnum;
	}

	protected void addEnum() {
		addOrEditEnum(null);
	}

	protected void editCurEnum(){
		EnumObj enumObj = this.getJEnumTable().getSelEnumObj();
		addOrEditEnum(enumObj);
	}
	
	protected void addOrEditEnum(EnumObj enumObj){
		EnumObj tempEnumObj = null;
		if(enumObj != null){
			tempEnumObj = new EnumObj(enumObj);
		}
		
		EnumValueDialog enumValueDialog = new EnumValueDialog(this, tempEnumObj, this.getCbxType().getSelectedClass());
        enumValueDialog.setModal(true);
        enumValueDialog.setVisible(true);
        if(enumValueDialog.isOK()){
        	tempEnumObj = enumValueDialog.getEnumObj();
        	//表示添加�?
        	if(enumObj == null){
	            if(this.getJEnumTable().getData().contains(tempEnumObj)){
	                String info = String.format(RM.R("label.RoleDialog.error.enumNameExist"), tempEnumObj.toString());
	                JOptionPane.showMessageDialog(this, info ,"警告", JOptionPane.WARNING_MESSAGE);
	            }else{
	                this.getJEnumTable().getData().add(tempEnumObj); 
	                this.getJEnumTable().updateUI();
	            }
        	}else /*修改一个EnumValue*/{
        		enumObj.setValue(tempEnumObj.getValue());
        		enumObj.setPercent(tempEnumObj.getPercent());
        	}            
        }
	}
	/**
	 * This method initializes btnDelEnum	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnDelEnum() {
		if (btnDelEnum == null) {
			btnDelEnum = new JTDButton();
			btnDelEnum.setFont(new Font("Dialog", Font.BOLD, 18));
			btnDelEnum.setBounds(353, 82, 44, 24);
			btnDelEnum.setText("-");
            btnDelEnum.setToolTipText(RM.R("label.RoleDialog.info.delEnum.alt"));
			btnDelEnum.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					RoleDialog.this.delEnum();
				}
			});
		}
		return btnDelEnum;
	}

	protected void delEnum() {
        int selRow = this.getJEnumTable().getSelectedRow();
        if(selRow == -1 || selRow >= this.getJEnumTable().getData().size()){
            JOptionPane.showMessageDialog(this, RM.R("label.RoleDialog.error.noRowIsSelect"));
        }else{
            int returnV = JOptionPane.showConfirmDialog(this, RM.R("label.RoleDialog.info.confirmDeleteEnum"),
                            "enum", JOptionPane.OK_CANCEL_OPTION);
            if(returnV == JOptionPane.OK_OPTION){
                this.getJEnumTable().deleteCurrentItem();
            }
        }
	}


	/**
	 * This method initializes jSQLPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJSQLPane() {
		if (jSQLPane == null) {
			jSQLPane = new JScrollPane();
			jSQLPane.setFont(new Font("Dialog", Font.PLAIN, 14));
			jSQLPane.setBounds(13, 42, 383, 131);
			jSQLPane.setViewportView(getTxtSQL());
		}
		return jSQLPane;
	}

	/**
	 * This method initializes txtSQL	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JEditTextArea getTxtSQL() {
		if (txtSQL == null) {
			txtSQL = new JEditTextArea();
            txtSQL.setTokenMarker(new TSQLTokenMarker());
			txtSQL.setToolTipText(RM.R("label.RoleDialog.info.txtSQl.alt"));
			txtSQL.setPreferredSize(new java.awt.Dimension(344, 109));
		}
		return txtSQL;
	}

	/**
	 * This method initializes btnSQL	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnShowSQLQueryDialog() {
		if (btnShowSQLQueryDialog == null) {
			btnShowSQLQueryDialog = new JTDButton();
			btnShowSQLQueryDialog.setText("...");
			btnShowSQLQueryDialog.setBounds(347, 7, 49, 24);
			btnShowSQLQueryDialog.setOpaque(false);
			btnShowSQLQueryDialog.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					RoleDialog.this.showEditSqlDialog();
				}
			});
		}
		return btnShowSQLQueryDialog;
	}

	protected void showEditSqlDialog() {
        QueryDialog sqlEditDialog = new QueryDialog(this);
        List<String> allConnNames = this.getParent().getTestDataConfig().getAllConnNames();
        sqlEditDialog.initConnectionCombox(allConnNames, this.getComboxConnection().getSelectedItem());
        sqlEditDialog.setSQLString(this.getTxtSQL().getText().trim());
        sqlEditDialog.setQueryType((String)this.getCbxMethod().getSelectedItem());
        sqlEditDialog.setModal(true);
        sqlEditDialog.setVisible(true);
        if(sqlEditDialog.isOK()){
            this.getComboxConnection().setSelectedItem(sqlEditDialog.getSelConnName());
            this.getTxtSQL().setText(sqlEditDialog.getSQLString());            
        }
    }

    /**
	 * This method initializes btnSave	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnSave() {
		if (btnSave == null) {
			btnSave = new JTDButton();
			btnSave.setBounds(202, 387, 69, 23);
			btnSave.setText(RM.R("label.info.btnSave"));
			btnSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
                        RoleDialog.this.saveConfig();
                    } catch (ValidataException e1) {
                        JOptionPane.showMessageDialog(RoleDialog.this, 
                                        RM.R("label.RoleDialog.error.saveError") + e1.getMessage());
                        e1.getControl().grabFocus();
                        if(e1.getControl() instanceof JTextComponent){
                            JTextComponent txtControl = (JTextComponent)e1.getControl();
                            txtControl.selectAll();
                        }
                    }
				}
			});
		}
		return btnSave;
	}

	protected void saveConfig() throws ValidataException {
        if (this.curRole == null) {
            return;
        }
        // 验证�?
        validateControlValue();
        String oldName = this.curRole.getName();
        
         this.dataExchange(this.curRole, false);
         try {
            
            getCurRoleFactory().saveOrUpdateToFile(this.curRole, oldName);
            this.getRoleTree().updateUI();
            
            //同步更新TableConfig中的引用 �?
            String newName = this.curRole.getName();
            if(!oldName.equals(newName)){
                TableConfig tableConfig = this.getCurRoleFactory().getTableConfig();
                if(tableConfig != null){
                    tableConfig.updateAllRefs(oldName, newName);
                    tableConfig.saveConfig();
                }else{
                    System.out.println("isGlobal..");
                }
            }
        } catch (Exception e) {
            logger.error("RoleFrame", e);
            JOptionPane.showMessageDialog(this, RM.R("label.RoleDialog.error.saveConfigError") + e.getMessage());
            return ;
        }
         
    }
	
    private void validateControlValue()throws ValidataException{
        String name = this.txtName.getText();
        if (StringUtils.isEmpty(name)) {
            throw new ValidataException(txtName, 
                            RM.R("label.RoleDialog.error.validation.nameIsNull"));
        }
        Class type = this.getCbxType().getSelectedClass();
        
        String strNullPercent = this.txtNullPercent.getText().trim();
        if(StringUtils.isNotEmpty(strNullPercent)){
            try{
                float v = Float.valueOf(strNullPercent).floatValue();
                if(v < 0 || v > 1){
                    throw new ValidataException(txtNullPercent, 
                                    RM.R("label.RoleDialog.error.validation.percentValueInvalid"));
                }
            }catch (Exception ex){
                throw new ValidataException(txtNullPercent, 
                                RM.R("label.RoleDialog.error.validation.percentValueInvalid"));
            }
        }
        String method = (String) this.getCbxMethod().getSelectedItem();
        if(Role.METHOD_INCREMENT.equalsIgnoreCase(method)
            ||Role.METHOD_RANDOM.equalsIgnoreCase(method)){
        	if(type.equals(JavaTypes.OBJECT)){
        		throw new ValidataException(this.cbxType, RM.R("label.RoleDialog.error.validation.methodTypeNotMatch"));
        	}
        	
            String strMin = this.txtMin.getText().trim();
            String strMax = this.txtMax.getText().trim();
            if (StringUtils.isNotEmpty(strMin)) {
                try {
                    JavaTypes.getInstance().valueOf(type, strMin);
                } catch (Exception ex) {
                    throw new ValidataException(txtMin, ex.getMessage());
                } 
            }

            if (StringUtils.isNotEmpty(strMax)) {
                try {
                    JavaTypes.getInstance().valueOf(type, strMax);
                } catch (Exception e) {
                    throw new ValidataException(txtMax, e.getMessage());
                }
            }        
          }else if(Role.METHOD_ENUM.equalsIgnoreCase(method)){
              if(this.getJEnumTable().getData() == null || this.getJEnumTable().getData().isEmpty()){
                  throw new ValidataException(this.getJEnumTable(), 
                                  RM.R("label.RoleDialog.error.validation.enumValueIsNull"));  
              }
          }else if(Role.METHOD_SQL_QUERY.equalsIgnoreCase(method)){
              String sql = this.getTxtSQL().getText();
              if(StringUtils.isEmpty(sql)){
                  throw new ValidataException(this.txtSQL,
                                  RM.R("label.RoleDialog.error.validation.sqlIsNull"));  
              }
          }else if(Role.METHOD_JAVA.equals(method)){
        	  String returnType = (String) this.getCbxReturnType().getSelectedItem();
        	  if(JavaSource.RETURN_TYPE_COMPLEX_OBJ.equalsIgnoreCase(returnType)){
        		  String returnFields = this.getTxtReturnFields().getText();
            	  if(StringUtils.isEmpty(returnFields)){
            		  throw new ValidataException(this.txtReturnFields,
            				  RM.R("label.RoleDialog.error.validation.returnFieldsIsNull"));
            	  }
        	  }
        	  
        	  String javaSource = this.getTxtJavaSource().getText();
        	  if(StringUtils.isEmpty(javaSource)){
        		  throw new ValidataException(this.txtJavaSource,
        				  RM.R("label.RoleDialog.error.validation.javaSourceIsNull"));
        	  } else {
				if (this.curRole.getJavaSource() == null || !StringUtil	.equals(javaSource, 
							this.curRole.getJavaSource().getJavaCode())) {
					DynamicCompiler dynamicCompiler = new DynamicCompiler();
					try {
						dynamicCompiler.validateJavaSource(javaSource);
					} catch (BaseException e) {
						throw new CompileException(this.txtJavaSource, e
								.getMessage());
					}
				}
			}
          }
                      



         
         String oldName = this.curRole.getName();
         //表示有重�?
         if(!oldName.equals(name) && this.getWorkspaceDataCache()
                         .getRole(this.curRoleFactory.toString(), name) != null){
             throw new ValidataException(txtName,RM.R("label.RoleDialog.error.validation.nameExist"));
         }
    }
    
    public RoleFactory getCurRoleFactory(){
    	return this.curRoleFactory;
    }

	/**
	 * This method initializes OK	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnOK() {
		if (btnOK == null) {
			btnOK = new JTDButton();
			btnOK.setBounds(288, 387, 69, 23);
			btnOK.setText(RM.R("label.info.btnOK"));
			btnOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
                        RoleDialog.this.saveConfig();
                    } catch (ValidataException e1) {
                        JOptionPane.showMessageDialog(RoleDialog.this, e1.getMessage());
                        if(e1.getControl() instanceof JTextComponent){
                            JTextComponent txtControl = (JTextComponent)e1.getControl();
                            txtControl.grabFocus();
                            txtControl.selectAll();
                        }
                        return ;
                    }
					RoleDialog.this.dispose();
				}
			});
		}
		return btnOK;
	}


	/**
	 * This method initializes cancel	
	 * 	
	 * @return com.testDataBuilder.ui.core.JTDButton	
	 */
	private JTDButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JTDButton();
			btnClose.setBounds(369, 387, 69, 23);
			btnClose.setText(RM.R("label.info.btnClose"));
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					RoleDialog.this.dispose();
				}
			});
		}
		return btnClose;
	}


	public WorkspaceDataCache getWorkspaceDataCache() {
		return workspaceDataCache;
	}


	public void setConfigDataCatch(WorkspaceDataCache workspaceDataCache) {
		this.workspaceDataCache = workspaceDataCache;
	}

    /**
     * This method initializes comboxConnection	
     * 	
     * @return javax.swing.JComboBox	
     */
    private ConnectionComboBox getComboxConnection() {
        if (comboxConnection == null) {            
            comboxConnection = new ConnectionComboBox();
            comboxConnection.setBounds(98, 7, 213, 24);
            List<String> connNames = this.getParent().getTestDataConfig().getAllConnNames();
            comboxConnection.setConnNames(connNames);
        }
        return comboxConnection;
    }


    /**
     * This method initializes btnSelEnum	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnSelEnum() {
        if (btnSelEnum == null) {
            btnSelEnum = new JTDButton();
            btnSelEnum.setText("...");
            btnSelEnum.setBounds(353, 12, 44, 24);
            btnSelEnum.setToolTipText(RM.R("label.RoleDialog.info.enum.alt"));
            btnSelEnum.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    RoleDialog.this.getEnumFromFile();
                }
            });
        }
        return btnSelEnum;
    }


    protected void getEnumFromFile() {
        EnumList tempEnumList = new EnumList(Arrays.asList(new Object[this.getJEnumTable().getData().size()]));
        Collections.copy(tempEnumList, this.getJEnumTable().getData());
        GetEnumValueFromFileOrDB getEnumValueFromFileOrDB = new GetEnumValueFromFileOrDB(this, tempEnumList);
        getEnumValueFromFileOrDB.setModal(true);
        getEnumValueFromFileOrDB.setVisible(true);
        if(getEnumValueFromFileOrDB.isOK()){
            this.getJEnumTable().setData(getEnumValueFromFileOrDB.getEnumList());
        }
    }



    /**
     * This method initializes txtNullPercent	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtNullPercent() {
        if (txtNullPercent == null) {
            txtNullPercent = new JTextFieldEx();
            txtNullPercent.setBounds(312, 54, 126, 25);            
        }
        return txtNullPercent;
    }


    /**
     * This method initializes jMethodTabbedPane	
     * 	
     * @return javax.swing.JTabbedPane	
     */
    private JTabbedPane getJMethodTabbedPane() {
        if (jMethodTabbedPane == null) {
            jMethodTabbedPane = new JTabbedPane();
            jMethodTabbedPane.setBounds(25, 163, 413, 212);
            jMethodTabbedPane.addTab(Role.METHOD_INCREMENT + "&" + Role.METHOD_RANDOM, getJIncRandomPanel());
            jMethodTabbedPane.addTab(Role.METHOD_ENUM, getJEnumPanel());
            jMethodTabbedPane.addTab(Role.METHOD_SQL_QUERY, getJSQLQueryPanel());
            jMethodTabbedPane.addTab(Role.METHOD_JAVA, null, getJJavaSourcePanel(), null);
            }
        return jMethodTabbedPane;
    }


    /**
     * This method initializes jIncRandomPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJIncRandomPanel() {
        if (jIncRandomPanel == null) {
            jIncRandomPanel = new JPanel();
            jIncRandomPanel.setLayout(null);
            jIncRandomPanel.add(labMin, null);
            jIncRandomPanel.add(getTxtMin(), null);
            jIncRandomPanel.add(labMax, null);
            jIncRandomPanel.add(getTxtMax(), null);
        }
        return jIncRandomPanel;
    }


    /**
     * This method initializes jEnumPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJEnumPanel() {
        if (jEnumPanel == null) {
            jEnumPanel = new JPanel();
            jEnumPanel.setLayout(null);
            jEnumPanel.add(getJEnumlPane(), null);
            jEnumPanel.add(getBtnAddEnum(), null);
            jEnumPanel.add(getBtnDelEnum(), null);
            jEnumPanel.add(getBtnSelEnum(), null);
        }
        return jEnumPanel;
    }


    /**
     * This method initializes jSQLQueryPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJSQLQueryPanel() {
        if (jSQLQueryPanel == null) {
            jSQLQueryPanel = new JPanel();
            jSQLQueryPanel.setLayout(null);
            jSQLQueryPanel.add(jLabel7, null);
            jSQLQueryPanel.add(getComboxConnection(), null);            
            jSQLQueryPanel.add(getBtnShowSQLQueryDialog(), null);
            jSQLQueryPanel.add(getJSQLPane(), null);
        }
        return jSQLQueryPanel;
    }

    public void  genToPreview(){
    	 TestDataConfig testDataConfig = WorkspaceDataCache.getInstance().getTestDataConfig();
    	 if(testDataConfig != null){
    		 TableConfig tableConfig = this.getCurRoleFactory().getTableConfig();
    		 if(tableConfig != null){
    			 new Thread(new ProgressRunner(this, testDataConfig, tableConfig)).start();
    		 }
    	 }else{
    		 JOptionPane.showMessageDialog(this, "出错了..testDataBuilder为null");
    	 }
    }
    
    private JPanel getJJavaSourcePanel() {
    	if(jJavaSourcePanel == null) {
    		jJavaSourcePanel = new JPanel();
    		jJavaSourcePanel.setLayout(null);
    		jJavaSourcePanel.setPreferredSize(new java.awt.Dimension(372, 173));
    		jJavaSourcePanel.add(getTxtJavaSource());
    		jJavaSourcePanel.add(getJLabel9());
    		jJavaSourcePanel.add(getTxtReturnFields());
    		jJavaSourcePanel.add(getBtnEditInNewWindow());
    		jJavaSourcePanel.add(getCbxReturnType());
    		jJavaSourcePanel.add(getJLabel10());
    	}
    	return jJavaSourcePanel;
    }
    
    private JEditTextArea getTxtJavaSource() {
    	if(txtJavaSource == null) {
    		txtJavaSource = new JEditTextArea();
    		txtJavaSource.setTokenMarker(new JavaTokenMarker());
    		txtJavaSource.setBounds(12, 63, 384, 110);
    	}
    	return txtJavaSource;
    }
    
    private JLabel getJLabel9() {
    	if(jLabel9 == null) {
    		jLabel9 = new JLabel();
    		jLabel9.setText(RM.R("label.RoleDialog.label.returnFields"));
    		jLabel9.setBounds(12, 34, 81, 22);
    	}
    	return jLabel9;
    }
    
    private JTextField getTxtReturnFields() {
    	if(txtReturnFields == null) {
    		txtReturnFields = new JTextField();
    		txtReturnFields.setBounds(120, 35, 276, 22);
    		txtReturnFields.setEnabled(false);
    	}
    	return txtReturnFields;
    }
    
    private JButton getBtnEditInNewWindow() {
    	if(btnEditInNewWindow == null) {
    		btnEditInNewWindow = new JButton();
    		btnEditInNewWindow.setText("...");
    		btnEditInNewWindow.setToolTipText(RM.R("label.RoleDialog.label.btnEditJavaInNewWindow"));
    		btnEditInNewWindow.setBounds(289, 7, 107, 22);
    		btnEditInNewWindow.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
						RoleDialog.this.showJavaSourceEditor();
				}
    			
    		});
    	}
    	return btnEditInNewWindow;
    }
    
    protected void showJavaSourceEditor() {
		JavaEditorDialog editorDialog = new JavaEditorDialog(this);
		editorDialog.setReturnType(this.getCbxReturnType().getSelectedItem());
		editorDialog.setReturnFields(this.getTxtReturnFields().getText());
		editorDialog.setJavaCode(this.getTxtJavaSource().getText());
		editorDialog.setVisible(true);
		if(editorDialog.isOK()){
			this.getCbxReturnType().setSelectedItem(editorDialog.getReturnType());
			this.getTxtReturnFields().setText(editorDialog.getReturnFields());
			this.getTxtJavaSource().setText(editorDialog.getJavaCode());
		}
	}

	private JComboBox getCbxReturnType() {
    	if(cbxReturnType == null) {    		
    		cbxReturnType = new JComboBox();
    		cbxReturnType.addItem(JavaSource.RETURN_TYPE_SIMPLE_OBJ);
    		cbxReturnType.addItem(JavaSource.RETURN_TYPE_COMPLEX_OBJ);
    		cbxReturnType.addItemListener(new java.awt.event.ItemListener() {
			    public void itemStateChanged(java.awt.event.ItemEvent e) {
			        RoleDialog.this.returnTypeBoxStateChanged(e);
			    }
			});
    		
    		cbxReturnType.setBounds(120, 7, 157, 22);
    	}
    	return cbxReturnType;
    }
    
    protected void returnTypeBoxStateChanged(ItemEvent e) {
		String returnType = (String)(this.getCbxReturnType().getSelectedItem());
		if(JavaSource.RETURN_TYPE_SIMPLE_OBJ.equalsIgnoreCase(returnType)){
			this.getTxtReturnFields().setEnabled(false);
		}else{
			this.getTxtReturnFields().setEnabled(true);
		}
	}

	private JLabel getJLabel10() {
    	if(jLabel10 == null) {
    		jLabel10 = new JLabel();
    		jLabel10.setText(RM.R("label.RoleDialog.label.returnType"));
    		jLabel10.setBounds(12, 7, 81, 22);
    	}
    	return jLabel10;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"


class ProgressRunner implements Runnable{

	static Logger logger = Logger.getLogger(ProgressRunner.class);
	
	RoleDialog roleDialog = null;
	TestDataConfig testDataConfig = null;
	TableConfig tableConfig = null;
	
	public ProgressRunner(RoleDialog roleDialog,TestDataConfig testDataConfig,TableConfig tableConfig){
		this.roleDialog = roleDialog;
		this.testDataConfig = testDataConfig;
		this.tableConfig = tableConfig;
	}
	
	
	public void run() {

		 Connection defConn = null;
		 TableConfig tableConfig = null;
		 try {
			 defConn = testDataConfig.getDefConnection();
			defConn.setAutoCommit(false);
			tableConfig = roleDialog.getCurRoleFactory().getTableConfig();
			if(tableConfig == null){
				return;
			}
			
			Insert2Table insert2Table =  new Insert2Table(tableConfig, testDataConfig);
			insert2Table.setProcessBar(roleDialog.getMsgLine());
							
			insert2Table.insert();
	
			String sql = "select * from " + tableConfig.getTableName();
			
			QueryDialog queryDialog = new QueryDialog(roleDialog);
			String defConnName = testDataConfig.getDefDatabaseConfig().getName();
	        List<String> allConnNames = new ArrayList<String>(1);
	        allConnNames.add(defConnName);
	        
	        queryDialog.initConnectionCombox(allConnNames, defConnName);
	        queryDialog.setSQLString(sql);
	        queryDialog.setQueryType(Role.METHOD_SQL_QUERY);
	        queryDialog.setModal(true);
	        queryDialog.setVisible(true);
		 }catch(JavaCodeRuntimeException ex){
		 logger.error("生成预览时出现运行时异常.", ex);
		} catch (SQLException ex) {
			logger.error("生成预览时出错",ex);
		} catch (IOException ex) {
			logger.error("生成预览时出现IO异常",ex);
		} finally{
			roleDialog.getMsgLine().clearText();
			if(defConn != null){
				try {
					defConn.rollback();
				} catch (SQLException ex) {
					logger.error("生成预览回滚时出错",ex);
				}
			}
		}	 
	}
	
}
