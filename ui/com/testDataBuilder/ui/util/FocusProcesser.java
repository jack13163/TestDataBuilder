package com.testDataBuilder.ui.util;

import java.awt.Component;
import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;
/**
 * 
 * <p>Title：GlobalFocusProcesser.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2008 frontier,Inc</p>
 * <p>Company：Frontier,Inc</p> 
 * @author LiuXiaojie 2008-5-8
 * @version 1.0
 */
public class FocusProcesser {

    /**
     * 获取焦点时,选中所以文本(如果是文本框)
     * <p><code>focusGainedSelectAll</code></p>
     * @param e
     * @author LiuXiaojie 2008-5-8
     */
    static public void focusGainedSelectAll(FocusEvent e) {
        if( e != null && e.getComponent() != null){
            Component comp = e.getComponent();
            if (comp instanceof JTextComponent){
                JTextComponent txtComp = (JTextComponent) comp;       
                txtComp.selectAll();
            }
        }
    }
}
