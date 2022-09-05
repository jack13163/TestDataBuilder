package com.testDataBuilder.ui.role;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;

import com.testDataBuilder.core.role.JavaSource;
import com.testDataBuilder.dynamicCompile.DynamicCompiler;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.ui.main.queryConsole.JEditTextArea;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;

public class JavaEditorDialog extends com.testDataBuilder.ui.core.CenterDialog {
	
	static final Logger logger = Logger.getLogger(JavaEditorDialog.class);
	
	private JEditTextArea txtJavaCode;
	private JTextField txtReturnFields;
	private JComboBox cbxReturnType;
	private JLabel jLabel2;
	private JButton btnHelp;
	private JButton btnValidation;
	private JButton btnCancel;
	private JButton btnOK;
	private JLabel jLabel1;

	private int JavaCodeBottom = 0;
	private int bottom = 0;
	private int right = 0;
	private int mid = 0;
	
	boolean isOK = false;
	
	public void setReturnType(Object returnType){
		this.getCbxReturnType().setSelectedItem(returnType);
	}
	
	public void setReturnFields(String returnFields){
		this.getTxtReturnFields().setText(returnFields);
	}
	
	public void setJavaCode(String javaCode){
		this.getTxtJavaCode().setText(javaCode);
		txtJavaCode.setFirstLine(10);
	}
	
	public Object getReturnType(){
		return  this.getCbxReturnType().getSelectedItem();
	}
	
	public String getReturnFields(){
		return this.getTxtReturnFields().getText();
	}
	
	public String getJavaCode(){
		return this.getTxtJavaCode().getText();
	}
	
	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JDialog dialog = new JDialog();
				JavaEditorDialog inst = new JavaEditorDialog(dialog);
				inst.setVisible(true);
			}
		});
	}

	
	
	public JavaEditorDialog(JDialog parent) {
		super(parent);
		initGUI();
	}
	
	private void initGUI() {
		try {
			getContentPane().setLayout(null);
			this.setSize(638, 459);
			this.setResizable(true);
			this.setModal(true);
			this.setMinimumSize(new Dimension(400,300));
			getContentPane().add(getBtnOK());
			getContentPane().add(getBtnCancel());
			getContentPane().add(getBtnValidation());
			getContentPane().add(getJLabel1());
			getContentPane().add(getJLabel2());
			getContentPane().add(getCbxReturnType());
			getContentPane().add(getTxtReturnFields());
			getContentPane().add(getTxtJavaCode());			
			getContentPane().add(getBtnHelp());

			this.calc();
			getContentPane().addComponentListener(new ComponentAdapter(){
	               @Override
	                public void componentResized(ComponentEvent e) {
	                    JavaEditorDialog.this.frameResized(e);                    
	                } 
	            });
			Helper.getInstance().enableHelpKey(getContentPane(), "detail.role.userRole.javaRole");
			this.center();
		} catch (Exception e) {
			logger.error("initGUI",e );
		}
	}
	
	private void calc() {
		JavaCodeBottom = this.getHeight() - txtJavaCode.getY() - txtJavaCode.getHeight();
		bottom = this.getHeight() - btnCancel.getY() - btnCancel.getHeight();
		right = this.getWidth() - btnCancel.getX() - btnCancel.getWidth();
		mid = btnCancel.getX() -  btnOK.getX() - btnOK.getWidth(); 
	}

	protected void frameResized(ComponentEvent e) {
		int txtJavaCodeWidth = this.getWidth() - (txtJavaCode.getX() * 2 + 5);
		int txtJavaCodeHeight = this.getHeight() - txtJavaCode.getY() - JavaCodeBottom;
		txtJavaCode.setSize(txtJavaCodeWidth, txtJavaCodeHeight);
		
		int x = this.getWidth() - right - btnCancel.getWidth();
		int y = this.getHeight() - bottom - btnCancel.getHeight();
		btnCancel.setLocation(x, y);
		
		x = x - btnOK.getWidth() - mid;
		btnOK.setLocation(x, y);
		
		x = x - btnValidation.getWidth() - mid;
		btnValidation.setLocation(x, y);
		
		this.paintComponents(this.getGraphics());
		
}

	private JLabel getJLabel1() {
		if(jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText(RM.R("label.RoleDialog.label.returnType"));
			jLabel1.setBounds(12, 14, 81, 22);
		}
		return jLabel1;
	}
	
	private JLabel getJLabel2() {
		if(jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText(RM.R("label.RoleDialog.label.returnFields"));
			jLabel2.setBounds(312, 14, 79, 22);
		}
		return jLabel2;
	}
	
	private JComboBox getCbxReturnType() {
		if(cbxReturnType == null) {
			cbxReturnType = new JComboBox();
    		cbxReturnType.addItem(JavaSource.RETURN_TYPE_SIMPLE_OBJ);
    		cbxReturnType.addItem(JavaSource.RETURN_TYPE_COMPLEX_OBJ);
    		cbxReturnType.addItemListener(new java.awt.event.ItemListener() {
			    public void itemStateChanged(java.awt.event.ItemEvent e) {
			        JavaEditorDialog.this.returnTypeBoxStateChanged(e);
			    }
			});
    		
			cbxReturnType.setBounds(105, 14, 130, 22);
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
	 
	 private JTextField getTxtReturnFields() {
		 if(txtReturnFields == null) {
			 txtReturnFields = new JTextField();
			 txtReturnFields.setBounds(409, 15, 208, 22);
			 txtReturnFields.setEnabled(false);
		 }
		 return txtReturnFields;
	 }
	 
	 private JEditTextArea getTxtJavaCode() {
		 if(txtJavaCode == null) {
			 txtJavaCode = new JEditTextArea();
			 txtJavaCode.setTokenMarker(new JavaTokenMarker());
			 txtJavaCode.setBounds(12, 49, 606, 337);
		 }
		 return txtJavaCode;
	 }
	 
	 private JButton getBtnOK() {
		 if(btnOK == null) {
			 btnOK = new JButton();
			 btnOK.setText(RM.R("label.info.btnOK"));
			 btnOK.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						JavaEditorDialog.this.onOK();
					}
					 
				 });
			 btnOK.setBounds(456, 400, 73, 22);			 
		 }
		 return btnOK;
	 }
	 
	 private JButton getBtnCancel() {
		 if(btnCancel == null) {
			 btnCancel = new JButton();
			 btnCancel.setText(RM.R("label.info.btnCancel"));
			 
			 btnCancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					JavaEditorDialog.this.onCancel();
				}
				 
			 });
			 btnCancel.setBounds(546, 400, 73, 22);
		 }
		 return btnCancel;
	 }
	 
	 protected void onCancel() {
		this.dispose();
	}

	protected void onOK() {
		this.isOK = true;
		this.dispose();
	}

	private JButton getBtnValidation() {
		 if(btnValidation == null) {
			 btnValidation = new JButton();
			 btnValidation.setText(RM.R("label.JavaEditorDialog.btn.validation"));
			 btnValidation.setBounds(355, 400, 83, 22);	
			 btnValidation.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						JavaEditorDialog.this.validateJavaCode();
					}
				 });			 
		 }
		 return btnValidation;
	 }

	public void validateJavaCode() {
		String javaSource = this.getJavaCode();
		DynamicCompiler dynamicCompiler = new DynamicCompiler();
		try {
			dynamicCompiler.validateJavaSource(javaSource);
			JOptionPane.showMessageDialog(this,"编译成功!");
		} catch (BaseException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		
	}
	public boolean isOK() {
		return isOK;
	}

	public void setOK(boolean isOK) {
		this.isOK = isOK;
	}
	
	private JButton getBtnHelp() {
		if(btnHelp == null) {
			btnHelp = new JButton();
			btnHelp.setText("Help");
			btnHelp.setBounds(261, 400, 75, 22);
			btnHelp.addActionListener(Helper.getInstance().getHelpListener());
		}
		return btnHelp;
	}
}
