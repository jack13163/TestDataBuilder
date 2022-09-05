package com.testDataBuilder.config;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.testDataBuilder.exception.TagNotFoundException;
import com.testDataBuilder.sqlTemplate.IDBTemplate;


public class DatabaseConfig extends IDBTemplate{
   
    public static final String TAG_DATABASE_CONFIG = "DatabaseConfig";
    
    public static final String TAG_NAME = "name";
    
    public static final String TAG_TEMPLATE_NAME = "templateName";
    
    public static final String TAG_DRIVER_CLASS = "driverClass";
    
    public static final String TAG_URL = "url";
    
    public static final String TAG_USER_NAME = "userName";
    
    public static final String TAG_PASSWORD = "password";
    
    /**
     * <DatabaseConfig>
     * 	  <name>连接名称</name>
          <driverClass>com.microsoft.jdbc.sqlserver.SQLServerDriver</driverClass>
          <url>jdbc:microsoft:sqlserver://192.168.0.20:1433;DatabaseName=DrmLog;SelectMethod=cursor</url>
            <userName>sa</userName>
            <password>sa</password>
       </DatabaseConfig>
     * <p><code>configure</code></p>
     * @param databaseConfig
     * @author LiuXiaojie 2007-5-15
     */
    public DatabaseConfig configure(Element databaseConfigEle) throws TagNotFoundException{
        String strName =databaseConfigEle.elementText(TAG_NAME);
        if(StringUtils.isEmpty(strName)){
        	throw new TagNotFoundException(TAG_NAME);
        }
        this.setName(strName);
        
        String templateName = databaseConfigEle.elementText(TAG_TEMPLATE_NAME);
        this.setTemplateName(templateName);
        
        Element driverClassEle = databaseConfigEle.element(TAG_DRIVER_CLASS);
        if(driverClassEle == null){
            throw new TagNotFoundException(TAG_DRIVER_CLASS);
        }
        this.setDriverClass(driverClassEle.getTextTrim());
        
        Element urlEle = databaseConfigEle.element(TAG_URL);
        if(urlEle == null){
            throw new TagNotFoundException(TAG_URL);
        }
        this.setURL(urlEle.getTextTrim());
        
        Element userNameEle = databaseConfigEle.element(TAG_USER_NAME);
        if(userNameEle == null){
            throw new TagNotFoundException(TAG_USER_NAME);
        }
        this.setUserName(userNameEle.getTextTrim());
        
        Element passwordEle = databaseConfigEle.element(TAG_PASSWORD);
        if(passwordEle == null){
            throw new TagNotFoundException(TAG_PASSWORD);
        }
        this.setPassword(passwordEle.getTextTrim());
        return this;
    }
    
    /**
     * 将databaseConfig 内容写到databaseConfigEle里面.
     * <p><code>toElement</code></p>
     * @param databaseConfig
     * @param databaseConfigEle
     * @author LiuXiaojie 2007-8-5
     */
    public Element toElement(Element databaseConfigEle){
    	if(databaseConfigEle == null){
    		databaseConfigEle = DocumentHelper.createElement(TAG_DATABASE_CONFIG);
    	}else{
            databaseConfigEle.clearContent();
        }
    	
        databaseConfigEle.addElement(TAG_NAME).setText(this.getName());
        if(StringUtils.isNotEmpty(this.getTemplateName())){
            databaseConfigEle.addElement(TAG_TEMPLATE_NAME).setText(this.getTemplateName());
        }
        databaseConfigEle.addElement(TAG_DRIVER_CLASS).setText(this.getDriverClass());
        if(StringUtils.isNotEmpty(this.getURL())){
            databaseConfigEle.addElement(TAG_URL).setText(this.getURL());
        }
    	databaseConfigEle.addElement(TAG_USER_NAME).setText(this.getUserName());
    	databaseConfigEle.addElement(TAG_PASSWORD).setText(this.getPassword());
        
    	return databaseConfigEle;
    }

    private String name;
    
    private String templateName;
    
    private String driverClass;

    private String url;
    
    private String userName;
    
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DatabaseConfig(String driverClass, String url) {
        super();
        this.driverClass = driverClass;
        this.url = url;
    }

    public DatabaseConfig(){
        super();
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
    
    public void setURL(String url){
    	this.url = url;
    }

	public String getIP() {
		return subString(this.getURL(), "//", ":");
	}

	public String getName() {
		return this.name;
	}

	public String getPwd() {
		return this.getPassword();
	}

	public String getURL() {
		return url;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    public String toString(){
        return getName();
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
