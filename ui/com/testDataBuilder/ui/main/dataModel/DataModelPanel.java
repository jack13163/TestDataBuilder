package com.testDataBuilder.ui.main.dataModel;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import com.testDataBuilder.ui.core.JTDButton;
import java.awt.Rectangle;

public class DataModelPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTDButton draw = null;

    /**
     * This is the default constructor
     */
    public DataModelPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(561, 398);
        this.setLayout(null);
        this.add(getDraw(), null);
    }

    public void draw(){
        Graphics g = this.getGraphics();
        g.setColor(Color.RED);
        g.drawRect(3, 10, 100, 100);
        System.out.println("xxxxxxxxxxxxxxxxxxxxx");
    }
    /**
     * This method initializes draw	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getDraw() {
        if (draw == null) {
            draw = new JTDButton();
            draw.setBounds(new Rectangle(402, 51, 64, 23));
            draw.setText("but");
            draw.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    DataModelPanel.this.draw();
                }
            });
        }
        return draw;
    }

    
}  //  @jve:decl-index=0:visual-constraint="10,10"
