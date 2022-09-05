package com.testDataBuilder.ui.help;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Rectangle;
import com.testDataBuilder.ui.core.JTDButton;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UpgradeDialog extends CenterDialog {

    private JPanel jContentPane = null;
    private JPanel jInnerPanel = null;
    private JScrollPane jScrollPane = null;
    private JTextArea jTextArea = null;
    private JTDButton btnOK = null;
    /**
     * This method initializes 
     * 
     */
    public UpgradeDialog() {
        super();
        initialize();
    }

    public UpgradeDialog(JFrame parent) {
        super(parent);
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(367, 222));
        this.setContentPane(getJContentPane());
        this.setTitle(RM.R("label.UpgradeDialog.title")); //$NON-NLS-1$
        this.setResizable(false);
        this.center();
        this.setModal(true);
    		
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
            jContentPane.add(getBtnOK(), null);
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
            jInnerPanel = new JPanel();
            jInnerPanel.setLayout(null);
            jInnerPanel.setBounds(new Rectangle(16, 17, 329, 137));
            jInnerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            jInnerPanel.add(getJScrollPane(), null);
        }
        return jInnerPanel;
    }

    /**
     * This method initializes jScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(14, 11, 303, 114));
            jScrollPane.setViewportView(getJTextArea());
        }
        return jScrollPane;
    }

    /**
     * This method initializes jTextArea	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            jTextArea.setTabSize(4);
            jTextArea.setLineWrap(true);
            jTextArea.setText(RM.R("label.UpgradeDialog.content")); //$NON-NLS-1$
        }
        return jTextArea;
    }

    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(268, 164, 77, 23));
            btnOK.setText(RM.R("label.info.btnOK")); //$NON-NLS-1$
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    UpgradeDialog.this.dispose();
                }
            });
        }
        return btnOK;
    }

}  //  @jve:decl-index=0:visual-constraint="10,10"
