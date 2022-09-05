package com.testDataBuilder.ui.core;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class JTDButton extends JButton {

    public JTDButton() {
        // TODO Auto-generated constructor stub
    }

    public JTDButton(Icon icon) {
        super(icon);
        // TODO Auto-generated constructor stub
    }

    public JTDButton(String text) {
        super(text);
        this.setToolTipText(text);
    }

    public JTDButton(Action a) {
        super(a);
        // TODO Auto-generated constructor stub
    }

    public JTDButton(String text, Icon icon) {
        super(text, icon);
        this.setToolTipText(text);
    }

    @Override
    public void setText(String text){
        super.setText(text);
        this.setToolTipText(text);
    }
}
