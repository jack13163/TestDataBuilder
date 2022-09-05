package com.testDataBuilder.ui.help;

import java.awt.HeadlessException;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.core.CenterDialog;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.Frame;
import com.testDataBuilder.ui.core.JTDButton;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class AboutDialog extends CenterDialog {

    static Logger logger = Logger.getLogger(AboutDialog.class);
    
    private JPanel jContentPane = null;
    private JTDButton btnOK = null;
    private JLabel labCopyRight = null;
    private JTextField txtCopyRight = null;
    private JPanel jEmbedPanel = null;
    private JLabel labTitle = null;
    private JLabel labAuthor = null;
    private JTextField txtAuthor = null;
    private JScrollPane jScrollPane = null;
    private JEditorPane txtContent = null;
    public static final String IMG = "/icon/TDB.png";  //  @jve:decl-index=0:
    public static final String README_CONFIG = "/txt/readme.txt";  //  @jve:decl-index=0: //$NON-NLS-1$ //$NON-NLS-2$
    private JTDButton btnImage = null;
    private JLabel jLabel = null;
    private JTextField txtTranslator = null;
    
    public AboutDialog(Frame arg0) throws HeadlessException {
        super(arg0);
        initialize();
        this.center();
    }
    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(501, 333));
        this.setContentPane(getJContentPane());
        this.setResizable(false);
        this.setTitle(RM.R("label.AboutDialog.title"));    		 //$NON-NLS-1$
    }

    /**
     * This method initializes jContentPane	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            labCopyRight = new JLabel();
            labCopyRight.setText(RM.R("label.AboutDialog.copyright")); //$NON-NLS-1$
            labCopyRight.setBounds(new Rectangle(110, 35, 99, 21));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getBtnOK(), null);
            jContentPane.add(getJEmbedPanel(), null);
        }
        return jContentPane;
    }
    /**
     * This method initializes btnOK	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnOK() {
        if (btnOK == null) {
            btnOK = new JTDButton();
            btnOK.setBounds(new Rectangle(410, 270, 71, 26));
            btnOK.setText(RM.R("label.AboutDialog.ok")); //$NON-NLS-1$
            btnOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                   AboutDialog.this.dispose();
                }
            });
        }
        return btnOK;
    }
    /**
     * This method initializes txtCopyRight	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtCopyRight() {
        if (txtCopyRight == null) {
            txtCopyRight = new JTextField();
            txtCopyRight.setText("2008  Liuxiaojie"); //$NON-NLS-1$
            txtCopyRight.setBounds(new Rectangle(218, 35, 233, 22));
            txtCopyRight.setEditable(false);
        }
        return txtCopyRight;
    }
    /**
     * This method initializes jEmbedPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJEmbedPanel() {
        if (jEmbedPanel == null) {
            jLabel = new JLabel();
            jLabel.setBounds(new Rectangle(110, 227, 99, 21));
            jLabel.setText(RM.R("label.AboutDialog.lab.translate")); //$NON-NLS-1$
            labAuthor = new JLabel();
            labAuthor.setBounds(new Rectangle(110, 201, 99, 22));
            labAuthor.setText(RM.R("label.AboutDialog.lab.author")); //$NON-NLS-1$
            labTitle = new JLabel();
            labTitle.setBounds(new Rectangle(110, 10, 342, 21));
            labTitle.setText(RM.R("label.AboutDialog.lab.title")); //$NON-NLS-1$
            jEmbedPanel = new JPanel();
            jEmbedPanel.setLayout(null);
            jEmbedPanel.setBounds(new Rectangle(11, 7, 470, 262));
            jEmbedPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            jEmbedPanel.add(labCopyRight, null);
            jEmbedPanel.add(getTxtCopyRight(), null);
            jEmbedPanel.add(labTitle, null);
            jEmbedPanel.add(labAuthor, null);
            jEmbedPanel.add(getTxtAuthor(), null);
            jEmbedPanel.add(getJScrollPane(), null);
            jEmbedPanel.add(getBtnImage(), null);
            jEmbedPanel.add(jLabel, null);
            jEmbedPanel.add(getTxtTranslator(), null);
        }
        return jEmbedPanel;
    }
    /**
     * This method initializes txtAuthor	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtAuthor() {
        if (txtAuthor == null) {
            txtAuthor = new JTextField();
            txtAuthor.setBounds(new Rectangle(218, 201, 233, 22));
            txtAuthor.setText("LiuXiaojie"); //$NON-NLS-1$
            txtAuthor.setEditable(false);
        }
        return txtAuthor;
    }
    /**
     * This method initializes jScrollPane	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(110, 68, 341, 119));
            jScrollPane.setViewportView(getTxtContent());
        }
        return jScrollPane;
    }
    /**
     * This method initializes txtContent	
     * 	
     * @return javax.swing.JTextArea	
     */
    private JEditorPane getTxtContent() {
        if (txtContent == null) {            
            txtContent = new JEditorPane();
            txtContent.setEditable(false);
            //java.net.URL page = this.getClass().getResource("/helpdoc/def/quickStart/readme.html"); //$NON-NLS-1$
            File file = new File("helpdoc/def/quickStart/readme.html");
            if(!file.exists()){
                String errorInfo = "加载帮助文档时出错！如果是在eclipse里面运行，"
                    + "\n\r请将Working Directory设置为TestDataBuilder\bin. " +
                            "\n\r方法: Menu Run->Run...->TestDataBuilder->" +
                            "arguments->WorkingDirectory->Other.";
                
                JOptionPane.showMessageDialog(this, errorInfo);
            }
            
            try {
                URL page =  file.toURL();
                txtContent.setPage(page);                
            } catch (IOException e) {
                logger.error("AboutDialog", e); //$NON-NLS-1$
                txtContent.setText(e.getMessage());
            }
            
        }
        return txtContent;
    }
    /**
     * This method initializes btnImage	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnImage() {
        if (btnImage == null) {
            byte[] bytes = null;
            InputStream is = this.getClass().getResourceAsStream(IMG); //$NON-NLS-1$
            try {
                bytes = IOUtils.toByteArray(is);
            } catch (IOException e) {
               logger.error("AboutDialog", e); //$NON-NLS-1$
            }
            Icon icon = new ImageIcon(bytes);
            btnImage = new JTDButton(icon);
            btnImage.setBounds(new Rectangle(4, 9, 94, 96));
            btnImage.setFocusable(false);            
            btnImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            btnImage.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("AK:062E70305DA088D165F2F3F41E9F4F9C" ); //$NON-NLS-1$
                }
            });
        }
        return btnImage;
    }
    /**
     * This method initializes txtTranslator	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getTxtTranslator() {
        if (txtTranslator == null) {
            txtTranslator = new JTextField();
            txtTranslator.setBounds(new Rectangle(218, 227, 233, 22));
            txtTranslator.setText(RM.R("label.AboutDialog.txt.author")); //$NON-NLS-1$
            txtTranslator.setEditable(false);
        }
        return txtTranslator;
    }

    public static void main(String[] args) {
        AboutDialog ab = new AboutDialog(null);
        ab.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ab.setVisible(true);
        
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
