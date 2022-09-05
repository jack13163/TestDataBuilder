package com.testDataBuilder.ui.core;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JDialog;

public class CenterDialog extends JDialog {

    public void center(){
        Dimension screenSize = this.getToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) /2;
        this.setLocation(x, y);
    }


//    public static String R(String key){
//        return RM.R(key);
//    }
    
    public CenterDialog() throws HeadlessException {
        this.center();
    }

    public CenterDialog(Frame arg0) throws HeadlessException {
        super(arg0);
        this.center();
    }

    public CenterDialog(Dialog arg0) throws HeadlessException {
        super(arg0);
        this.center();
    }

    public CenterDialog(Frame arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        this.center();
    }

    public CenterDialog(Frame arg0, String arg1) throws HeadlessException {
        super(arg0, arg1);
        this.center();
    }

    public CenterDialog(Dialog arg0, boolean arg1) throws HeadlessException {
        super(arg0, arg1);
        this.center();
    }

    public CenterDialog(Dialog arg0, String arg1) throws HeadlessException {
        super(arg0, arg1);
        this.center();
    }

    public CenterDialog(Frame arg0, String arg1, boolean arg2) throws HeadlessException {
        super(arg0, arg1, arg2);
        this.center();
    }

    public CenterDialog(Dialog arg0, String arg1, boolean arg2) throws HeadlessException {
        super(arg0, arg1, arg2);
        this.center();
    }

    public CenterDialog(Frame arg0, String arg1, boolean arg2, GraphicsConfiguration arg3) {
        super(arg0, arg1, arg2, arg3);
        this.center();
    }

    public CenterDialog(Dialog arg0, String arg1, boolean arg2, GraphicsConfiguration arg3) throws HeadlessException {
        super(arg0, arg1, arg2, arg3);
        this.center();
    }

}
