package com.testDataBuilder.ui.main.others;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.testDataBuilder.ui.core.JTDButton;

public class IconButton extends JTDButton{

    public IconButton(String iconPath){
        super();
        URL url = this.getClass().getResource(iconPath);
        Icon icon =new  ImageIcon(url);        
        this.setIcon(icon);
    }
}
