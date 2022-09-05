package com.testDataBuilder.ui.main.dataModel;

import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class TestFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private DataModelPanel dataModelPanel = null;

    /**
     * This method initializes dataModelPanel	
     * 	
     * @return com.testDataBuilder.ui.main.dataModel.DataModelPanel	
     */
    private DataModelPanel getDataModelPanel() {
        if (dataModelPanel == null) {
            dataModelPanel = new DataModelPanel();
        }
        return dataModelPanel;
    }

    /**
     * <p><code>main</code></p>
     * @param args
     * @author LiuXiaojie 2008-5-5
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TestFrame thisClass = new TestFrame();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }

    /**
     * This is the default constructor
     */
    public TestFrame() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getDataModelPanel());
        this.setTitle("JFrame");
    }

}
