package com.testDataBuilder.ui.role;

import com.testDataBuilder.core.baseType.JavaTypes;
import com.testDataBuilder.core.role.EnumObj;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.ValidataException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import com.testDataBuilder.ui.core.JTextFieldEx;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

public class EnumValueDialog extends CenterDialog {

    private JPanel jContentPane = null;
    private JPanel innerPanel = null;
    private JTDButton btnOK = null;
    private JTDButton btnCancel = null;
    private JLabel label = null;
    private JTextField txtValue = null;
    private JLabel jLabel = null;
    private JTextField txtPercent = null;

    private EnumObj enumObj = null;  //  @jve:decl-index=0:
    private Class valueValidator = null;
    
    /**
     * This method initializes 
     * 
     */
    public EnumValueDialog(JDialog parent,EnumObj enumObj, Class valueValidator) {
    	super(parent);
        this.enumObj = enumObj;
        this.valueValidator = valueValidator;
    	initialize();
        this.dataExchange(enumObj, true);
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(375, 228));
        this.setTitle(RM.R("label.EnumValueDialog.title"));
        this.setResizable(false);
        this.setContentPane(getJContentPane());
    		
    }

    public void dataExchange(EnumObj enumObj, boolean isToControl){
        if(isToControl){
            if(enumObj == null){
                enumObj = new EnumObj();
            }
            if(enumObj.getPercent() != null){
                this.getTxtPercent().setText(enumObj.getPercent().toString());
            }
            if(enumObj.getValue() != null){
                this.getTxtValue().setText(enumObj.getValue().toString());
            }
        }else{
            Object value;
            try {
                value = JavaTypes.getInstance().valueOf(this.valueValidator, this.getTxtValue().getText().trim());
            } catch (BaseException e) {
                JOptionPane.showMessageDialog(this, RM.R("label.EnumValueDialog.error.addEnumError") + e.getMessage());
                return ;
            }
            enumObj.setValue(value);
            enumObj.setPercent(Integer.valueOf(this.getTxtPercent().getText().trim()));
        }
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
            jContentPane.add(getBtnOK(), null);
            jContentPane.add(getBtnCancel(), null);
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
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(23, 64, 63, 25));
            jLabel.setText(RM.R("label.EnumValueDialog.label.percent"));
            label = new JLabel();
            label.setBounds(new Rectangle(23, 22, 63, 25));
            label.setText(RM.R("label.EnumValueDialog.label.value"));
            innerPanel = new JPanel();
            innerPanel.setLayout(null);
            innerPanel.setBounds(new Rectangle(14, 15, 343, 145));
            innerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            innerPanel.add(label, null);
            innerPanel.add(getTxtValue(), null);
            innerPanel.add(jLabel, null);
            innerPanel.add(getTxtPercent(), null);
        }
        return innerPanel;
    }

    private boolean isOK = false;
    
    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(192, 167, 77, 21));
            btnOK.setText(RM.R("label.info.btnOK"));
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    EnumValueDialog.this.onOK();
                }
            });
        }
        return btnOK;
    }

    protected void onOK() {
        try {
            this.validateControlValue();
        } catch (ValidataException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            if(e.getControl() instanceof JTextField){
                JTextField txt = (JTextField)e.getControl();
                txt.grabFocus();
                txt.selectAll();
            }
            return ;
        }
        if(enumObj == null){
            enumObj = new EnumObj();
        }
        this.dataExchange(enumObj, false);
        isOK = true;
        this.dispose();
    }

    private void validateControlValue()throws ValidataException {
        String strValue = this.getTxtValue().getText();
        if(StringUtils.isEmpty(strValue)){
            throw new ValidataException(txtValue, RM.R("label.EnumValueDialog.validation.valueIsNull"));
        }
        
        try {
            JavaTypes.getInstance().valueOf(this.valueValidator, strValue);
        } catch (Exception e) {
            throw new ValidataException(txtValue, RM.R("label.EnumValueDialog.validation.invalidValue") + e.getMessage());
        }
        String strPercent = this.getTxtPercent().getText().trim();
        if(StringUtils.isEmpty(strPercent)){
            throw new ValidataException(txtPercent, RM.R("label.EnumValueDialog.validation.percentIsNull"));
        }
        try{
            Integer.valueOf(strPercent);
        }catch(NumberFormatException ex){
            throw new ValidataException(txtPercent, RM.R("label.EnumValueDialog.validation.percentValueInvalid"));
        }
    }

    /**
     * This method initializes btnCancel	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnCancel() {
        if (btnCancel == null) {
            btnCancel = new JTDButton();
            btnCancel.setBounds(new Rectangle(281, 167, 77, 21));
            btnCancel.setText(RM.R("label.info.btnCancel"));
            btnCancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    EnumValueDialog.this.dispose();
                }
            });
        }
        return btnCancel;
    }

    /**
     * This method initializes txtValue	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtValue() {
        if (txtValue == null) {
            txtValue = new JTextFieldEx();
            txtValue.setBounds(new Rectangle(94, 22, 225, 24));           
        }
        return txtValue;
    }

    /**
     * This method initializes txtPercent	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtPercent() {
        if (txtPercent == null) {
            txtPercent = new JTextFieldEx();
            txtPercent.setBounds(new Rectangle(94, 64, 225, 24));           
        }
        return txtPercent;
    }

    public EnumObj getEnumObj() {
        return enumObj;
    }

    public void setEnumObj(EnumObj enumObj) {
        this.enumObj = enumObj;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean isOK) {
        this.isOK = isOK;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
