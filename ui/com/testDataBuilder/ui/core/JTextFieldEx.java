package com.testDataBuilder.ui.core;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.text.Document;

import com.testDataBuilder.ui.util.FocusProcesser;
/**
 * selectAll on focusGained
 * <p>Title：JTextFieldEx.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2008 frontier,Inc</p>
 * <p>Company：Frontier,Inc</p> 
 * @author LiuXiaojie 2008-5-8
 * @version 1.0
 */
public class JTextFieldEx extends JTextField {

    
    
    public JTextFieldEx() {
        super();
        this.init();
    }

    public JTextFieldEx(Document doc, String text, int columns) {
        super(doc, text, columns);
        this.init();
    }

    public JTextFieldEx(int columns) {
        super(columns);
        this.init();
    }

    public JTextFieldEx(String text, int columns) {
        super(text, columns);
        this.init();
    }

    public JTextFieldEx(String text) {
        super(text);
        this.init();
    }

    private void init(){
        this.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e) {
                FocusProcesser.focusGainedSelectAll(e);
                super.focusGained(e);
            }
            
        });
    }
}
