package com.testDataBuilder.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.config.preference.IPreference;
import com.testDataBuilder.config.preference.Preferences;
import com.testDataBuilder.core.baseType.JavaTypes;
import com.testDataBuilder.exception.BaseException;


/**
 * 全局帮助类。主要包含系统（软件）配置
 * <p>Title：Global.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-12-6
 * @version 1.0
 */
public class Global {

    static Logger logger = Logger.getLogger(Global.class);
    
    //[start]全局常量     
    public static final String PROJECT_CONFIG = "project.xml";  //  @jve:decl-index=0:
    
    public static final String SEP = System.getProperty("file.separator");
    
    public static final String LINE_SEP = System.getProperty("line.separator");
    
    public SimpleDateFormat dataFormater = null;
    
    public IPreference P = null;
    

    public static final String ICON_TREE_DIR = "/icon/tree/";
    
    //[end]
    private Global(){

    }
    
    public void initlize(){
        
        String lastWorkspace = appProperties.getLastWorkspace();
        if(StringUtils.isNotEmpty(lastWorkspace)){
        	if(isValidWorkspace(lastWorkspace)){
        		this.workspace = lastWorkspace;
        	}else{
        	    this.saveLastWorkspace(null);   
            }
        }
        
        File dir = null;
        if(StringUtils.isNotEmpty(this.getWorkspace())){
            dir = new File(this.getWorkspace());
        }
        P = Preferences.getPreference(dir);
        
        dataFormater = new SimpleDateFormat(P.getDateFormat());
        this.initJavaTypes(P.getDataTypePluginDir());
    }
    
       
    public void initJavaTypes(String typeConfigDir){
    	JavaTypes.getInstance().init(typeConfigDir);
    }
    
    public Class loadTypeClass(String className) throws MalformedURLException, ClassNotFoundException{
        return JavaTypes.getInstance().loadTypeClass(className);
    }
    
	public static boolean isValidWorkspace(String workspaceDir){
		File dir = new File(workspaceDir);
		if(dir.exists()){
			File projectFile = new File(dir, PROJECT_CONFIG);
            return (projectFile.exists());
		}
		return false;
	}
	
    private static Global instance = null;

    public static String APP_VERSION = "1.0"; 
    
    public static synchronized Global getInstance(){
        if(instance == null){
            instance = new Global();
        }
        return instance;
    }

    public String formatDateTime(java.sql.Date date){
    	return  dataFormater.format(date);
    }
    
    //private WorkspaceDataCache workspaceDataCache = null;
     
    public java.sql.Date parseDateTime(String strDatetime) throws BaseException{
		try {
            return new java.sql.Date(dataFormater.parse(strDatetime).getTime());
        } catch (ParseException e) {
            throw new BaseException(e);
        }
    }
    


    /**
     * 得到一个tableConfig对应的配置文件名。
     * <p><code>getTableConfigFilePath</code></p>
     * @param tableName
     * @return
     * @author LiuXiaojie 2007-12-6
     */
//    public String getTableConfigFilePath(String tableName){
//        return getFileAbsPath(tableName + IPreference.TABLE_CONFIG_SUFFIX);
//    }
//    
//    /**
//     * 得到一个文件的全路径名。
//     * <p><code>getFileAbsPath</code></p>
//     * @param fileName
//     * @return
//     * @author LiuXiaojie 2007-12-5
//     */
//    public String getFileAbsPath(String fileName){
//        return this.getWorkspace() + Global.SEP + fileName;
//    }
    
    private AppProperty appProperties = AppProperty.getInstance();  //  @jve:decl-index=0:
      
    public static final String SYSTEM_PROPERTIES_COMMENT = "TestDataBuilder.配置文件";

    public void updateRecentProjectList(String recentProjectsXml){
        this.appProperties.setRecentProjects(recentProjectsXml);
        try {
            this.appProperties.storeToDefXmlFile();
        } catch (IOException e) {
            logger.error("addProject2RecentList", e);
        }
    }
    
    public void setLookAndFeel(String lookAndFeel){
        this.appProperties.setLookAndFeel(lookAndFeel);
        try {
            appProperties.storeToDefXmlFile();
        } catch (IOException ex) {
            logger.error("setLookAndFeel", ex);
        }
    }
    
//    public static final String LOCAL = "locale";
//    
    private String workspace = null; //System.getProperty("user.dir");  //  @jve:decl-index=0:

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * 设置工作区，并且同步更新相关配置文件，（最后的工作区设置，最近的工作区）
     * <p><code>setWorkspaceAndUpdateConfig</code></p>
     * @param workspace
     * @author LiuXiaojie 2007-12-26
     */
    public void setWorkspaceLastWorkspace(String workspace) {
        setWorkspace(workspace);
        this.saveLastWorkspace(workspace);
    }
    
    public void saveLastWorkspace(String workspace){        
        if(StringUtils.isEmpty(workspace)){
            this.appProperties.remove(AppProperty.LAST_WORKSPACE);
        }else{
            this.appProperties.setLastWorkspace(workspace);
        }
        try {
            this.appProperties.storeToDefXmlFile();
        } catch (IOException ex) {
            logger.error("saveLastWorkspace", ex);
        }
    }
    
    public String getProjectConfigFile(){
        return getWorkspace() + SEP + PROJECT_CONFIG;
    }

    public AppProperty getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(AppProperty appProperties) {
        this.appProperties = appProperties;
    }

}
