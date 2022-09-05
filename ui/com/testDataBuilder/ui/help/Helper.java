package com.testDataBuilder.ui.help;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import com.testDataBuilder.resources.ResourceManager;

public class Helper {
    static Logger logger = Logger.getLogger(Helper.class);
    
	public static final String DEF_HELP_PKG = "res/helpdoc";
    
	public static final String DEF_HELP_HS = DEF_HELP_PKG + "/def/help.hs";

	
    HelpSet helpset = null;
    ActionListener helpListener = null;
    HelpBroker helpBroker = null;
    
    private static Helper instance = null;
    
    public static Helper getInstance(){
        if(instance == null){
            instance = new Helper();
        }
        return instance;
    }
    
    private Helper(){
        try {
            helpset = new HelpSet(this.getClass().getClassLoader(), getURL());
          } catch (HelpSetException e) {
              logger.error("Error loading helpSystem", e);            
          } catch (MalformedURLException e) {
              logger.error("Error loading helpSystem", e);   
          }
       
          helpBroker = helpset.createHelpBroker();
          helpBroker.setLocation(new Point(100,100));
          helpBroker.setSize(new Dimension(800,600));     
          
          helpListener  =  new CSH.DisplayHelpFromSource(helpBroker);
    }

    private URL getURL() throws MalformedURLException{
        String helpName =DEF_HELP_PKG +  "/" + ResourceManager.getLocale().toString() + "/help.hs";
    	File file = new File(helpName);
    	if(!file.exists()){
    		String errorInfo = "加载帮助文档时出错！如果是在eclipse里面运行，"
    			+ "\n\r请将Working Directory设置为TestDataBuilder\bin. " +
    					"\n\r方法: Menu Run->Run...->TestDataBuilder->" +
    					"arguments->WorkingDirectory->Other.";
    		
    		JOptionPane.showMessageDialog(null, errorInfo);
    	}
    	URL url =  file.toURL();
        return url;
    }
    
    protected HelpBroker getHelpBroker() {
        return helpBroker;
    }

    public void setHelpBroker(HelpBroker helpBroker) {
        this.helpBroker = helpBroker;
    }

    public ActionListener getHelpListener() {
        return helpListener;
    }

    public void setHelpListener(ActionListener helpListener) {
        this.helpListener = helpListener;
    }

    public HelpSet getHelpset() {
        return helpset;
    }

    public void setHelpset(HelpSet helpset) {
        this.helpset = helpset;
    }
    
    public void enableHelpKey(Component comp, String helpKey){
        this.getHelpBroker().enableHelpKey(comp, helpKey, getHelpset());
    }
    
}
