package com.testDataBuilder.util;

import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.swing.UIManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class AppProperty extends Properties {
   
    public static final String LOOK_AND_FEEL_JAVA_DEF = "javax.swing.plaf.metal.MetalLookAndFeel"; 
    
    public static final String LOOK_AND_FEEL_SYS_DEF = UIManager.getSystemLookAndFeelClassName();
    
    
    private static final String TDS_CONFIG = "tdsConfig.xml";
    
    public static final String RECENT_PROJECTS = "recentProjects";
    
    public static final String LAST_WORKSPACE = "lastWorkspace";  
    
    public static final String LOOK_AND_FEEL = "lookAndFeel";
    
    public static final String LOCALE = "locale";
    
    public static final String SHOW_CONFIG_DIALOG = "showConfigDialog";
    
    private static AppProperty instance = null;
    
    public static AppProperty getInstance(){
        if(instance == null){
            instance = new AppProperty();
        }
        return instance;
    }
    
    boolean propertyChange = false;
    
    private AppProperty(){
        
    }
    
    public AppProperty loadFromDefXmlFile() throws InvalidPropertiesFormatException, IOException{
        File file = new File(TDS_CONFIG);
        if(file.exists()){
            this.loadFromXML(FileUtils.openInputStream(file));
        }
        return this;
    }
    
    public void storeToDefXmlFile() throws IOException{
        if(propertyChange){
            propertyChange = false;
            File file = new File(TDS_CONFIG);
            this.storeToXML(FileUtils.openOutputStream(file), "TestDataBuilder.SysConfigFile", "utf-8");
        }
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        propertyChange = true;
        return super.put(key, value);
    }

    @Override
    public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
        propertyChange = true;
        super.putAll(t);
    }

    @Override
    public synchronized Object remove(Object key) {
        propertyChange = true;
        return super.remove(key);
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        propertyChange = true;
        return super.setProperty(key, value);
    }
 
    public String getLastWorkspace(){
        return this.getProperty(LAST_WORKSPACE);
    }
    
    public void setLastWorkspace(String lastWorkspace){
        this.setProperty(LAST_WORKSPACE, lastWorkspace);
    }
    
    public String getRecentProjects(){
        return this.getProperty(RECENT_PROJECTS);
    }
    
    public void setRecentProjects(String recentProjects){
        this.setProperty(RECENT_PROJECTS, recentProjects);
    }
    
    public String getLookAndFeel(){
        String lookAndFeel =  this.getProperty(LOOK_AND_FEEL);
        if(StringUtils.isEmpty(lookAndFeel)){
            lookAndFeel = LOOK_AND_FEEL_JAVA_DEF;
        }
        return lookAndFeel;
    }
    
    public void setLookAndFeel(String lookAndFeel){
        this.setProperty(LOOK_AND_FEEL, lookAndFeel);
    }
    
    
    public String getLocale(){
        String locale = this.getProperty(LOCALE);
        if(StringUtils.isEmpty(locale)){
            locale = Locale.getDefault().toString();
            this.setLocale(locale);
        }
        return locale;
    }
    
    public void setLocale(String locale){
        this.setProperty(LOCALE, locale);
    }
    
    public boolean getShowConfigDialog(){
        String strShowConfigDialog = this.getProperty(SHOW_CONFIG_DIALOG);
        if(strShowConfigDialog != null && strShowConfigDialog.equalsIgnoreCase("FALSE")){
            return false;
        }else{
            return true;
        }
    }
    
    public void setShowConfigDialog(boolean showConfigDialog){
        this.setProperty(SHOW_CONFIG_DIALOG, Boolean.toString(showConfigDialog));
    }
}
