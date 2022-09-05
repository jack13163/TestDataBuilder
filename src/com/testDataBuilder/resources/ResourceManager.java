package com.testDataBuilder.resources;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;

/**
 * 资源管理器。
 * <p>Title：ResourceManager.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-12-6
 * @version 1.0
 */
public class ResourceManager {

    static Logger logger = Logger.getLogger(ResourceManager.class);
    
	public static final String LOCALE_CONFIG_PACKAGE = "res/language";
	
	private static final String CONFIG_FILE_NAME = "LabelResource";
	
	private static final String configFilePath = LOCALE_CONFIG_PACKAGE + "." + CONFIG_FILE_NAME;
	
	private static ResourceBundle res = null;
    
    private static Locale locale = Locale.getDefault();
    
    private ResourceManager(){
        
    }
    
    public static void setLocale(Locale locale){
        ResourceManager.locale = locale;
        res = null;
    }
    
    public static Locale getLocale(){
        return ResourceManager.locale;
    }
    
    public static ResourceBundle getResourceBundle(Locale local){
        if(res == null){
            ResourceManager.locale = local;
            res = ResourceBundle.getBundle(configFilePath, local,getClassLoader());
        }
        return res; 
    }
    
    public static ResourceBundle getResourceBundle(){
        if(res == null){
            res = ResourceBundle.getBundle(configFilePath, locale, getClassLoader());
        }
        return res; 
    }
    
    public static URLClassLoader getClassLoader(){
        File file = new File(System.getProperty("user.dir"));
        URLClassLoader urlClassLoader = null;
        try {
            urlClassLoader = new URLClassLoader(new URL[] {file.toURL()}, ResourceManager.class.getClassLoader());
        } catch (MalformedURLException e) {
            logger.error("ResouceManager", e);
        }

        return urlClassLoader;
    }
    private static FilenameFilter filter = new SuffixFileFilter("properties");
    
    public static List<String> getAllLocale(){
    	File dir = new File(LOCALE_CONFIG_PACKAGE);
        
        List<String> allLocale = new ArrayList<String>();

    	if(dir != null && dir.exists() && dir.isDirectory()){
    		String[] fileNames = dir.list(filter);
    		for(String fileName : fileNames){
    			int beginIndex = CONFIG_FILE_NAME.length() + 1;
    			int endIndex = fileName.lastIndexOf(".");
    			if(beginIndex < endIndex){
    				allLocale.add(fileName.substring(beginIndex, endIndex));
    			}
    		}
    	}
    	return allLocale;
    }

    public static void main(String[] args) {
    	System.out.println(LOCALE_CONFIG_PACKAGE);
		ResourceManager.getAllLocale();
	}
}
