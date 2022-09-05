package com.testDataBuilder.config;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.xml.sax.SAXException;

import com.testDataBuilder.core.DatabaseUtil;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.TagNotFoundException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.util.Global;
import com.testDataBuilder.util.XmlFileUtil;

/**
 * 测试数据生成配置信息.
 * <p>Title：TestDataConfig.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-6-16
 * @version 1.0
 */
public class TestDataConfig {

    static Logger logger = Logger.getLogger(TestDataConfig.class);    
    
	public static final String TAG_PROJECT = "project";
	
    public static final String TAG_VERSION = "version";
    
    public static final String TAG_DATABASE_CONFIGS = "DatabaseConfigs";
    
    public static final String TAG_DATABASE_CONFIG = "DatabaseConfig";
	
	public static final String TAG_DATA_GENERATOR = "role";
	
	public static final String TAG_TABLE = "table";
	
	public static final String TAG_NAME = "name";
	
    public static final String TAG_COMMENT = "comment";
    
	public static final String TAG_DEPENDS = "depends";
	
	public TestDataConfig(File file) throws BaseException {
        
		if (!file.exists()) {
			throw new BaseException("file[" + file.getAbsolutePath()
					+ "] is not exists! file");
		}
		xmlFile = new XmlFileUtil(file);
		try {
			configure(xmlFile.getDoc(false, null).getRootElement());
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

	/**
	 * 解析配置文件.
	 * <p><code>configure</code></p>
	 * @param testDataEle
	 * @return
	 * @throws BaseException
	 * @author LiuXiaojie 2007-8-5
	 */
	public void configure(Element testDataEle) throws BaseException{
        Element nameEle = testDataEle.element(TAG_NAME);
        if(nameEle == null){
            throw new TagNotFoundException(TAG_NAME);
        }
        this.setName(nameEle.getTextTrim());
        
        String version = testDataEle.elementText(TAG_VERSION);
        if(StringUtils.isNotEmpty(version)){
            this.setVersion(version);
        }        
        
        String comment = testDataEle.elementText(TAG_COMMENT);
        this.setComment(comment);
        
		Element databaseEle = testDataEle.element(TAG_DATABASE_CONFIG);
		if(databaseEle == null){
			throw new TagNotFoundException(TAG_DATABASE_CONFIG);
		}
		this.setDefDatabaseConfig(new DatabaseConfig().configure(databaseEle));
		
		Element databaseConfigs = testDataEle.element(TAG_DATABASE_CONFIGS);
		if(databaseConfigs != null){
			List<Element> databaseConfigEles = databaseConfigs.elements(TAG_DATABASE_CONFIG);
			for(Element databaseConfigEle : databaseConfigEles){
				this.addDatabaseConfig(new DatabaseConfig().configure(databaseConfigEle));
			}
		}
	}
	
	public void saveConfig() throws SAXException, IOException{
		xmlFile.saveDoc();		
	}
	private String name;
    
    private String version = Global.APP_VERSION;
    
    private String comment;
    
	private DatabaseConfig defDtabaseConfig = null;

	private Map<String, DatabaseConfig> databaseConfigs = new DatabaseConfigMap<String, DatabaseConfig>();
	
	private Map<String, Connection> databaseConnectionCache = new HashMap<String, Connection>();
	
	public static final String DEFAULT = "Default";
	
	public Connection getDefConnection() throws SQLException{
		return this.getConn(DEFAULT);
	}
	
	public Connection getConn(String configName) throws SQLException{
		Connection conn = this.databaseConnectionCache.get(configName);
		if(conn == null || conn.isClosed()){
			if(configName.equalsIgnoreCase(DEFAULT)){
				conn = DatabaseUtil.getConnection(defDtabaseConfig);
			}else{
				conn = DatabaseUtil.getConnection(databaseConfigs.get(configName));
			}
			this.databaseConnectionCache.put(configName, conn);
		}
		
		return conn;
	}
	
    public Connection getConnFromCache(String configName){
        return this.databaseConnectionCache.get(configName);
    }
    
    public void connectAll() throws SQLException{
        Set<String> configs = this.getDatabaseConfigs().keySet();
        for(String config : configs){
            this.getConn(config);            
        }
    }
    
    public void disconnectAll(){
        this.releaseAllConn();       
        this.getDatabaseConnectionCache().clear();
    }
    
    public void connectDatabase(String configName) throws SQLException{
        this.getConn(configName);
    }
    
    public void disconnectDatabase(String configName){
        Connection conn = this.getDatabaseConnectionCache().remove(configName);
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.error("disconnectDatabase", ex);
            }
        }
    }
    
    public void saveOrUpdateDatabaseConfig(DatabaseConfig databaseConfig) throws BaseException{
        try {
            Element root = xmlFile.getDoc(false, null).getRootElement();
            Element databaseConfigsEle = root.element(TAG_DATABASE_CONFIGS);
            if(databaseConfigsEle == null){
                databaseConfigsEle = root.addElement(TAG_DATABASE_CONFIGS);
            }
            
            Element databaseConfigEle = (Element) databaseConfigsEle.selectSingleNode("//DatabaseConfig[name='" 
                            + databaseConfig.getName() +  "']");
            if(databaseConfigEle == null){
                if(databaseConfig.getName().equalsIgnoreCase(DEFAULT)){
                    databaseConfigEle = root.addElement(TAG_DATABASE_CONFIG);
                }else{                    
                    databaseConfigEle = databaseConfigsEle.addElement(TAG_DATABASE_CONFIG);
                }
            }
            databaseConfigEle.clearContent();
            databaseConfig.toElement(databaseConfigEle);
            xmlFile.saveDoc(); 
            if(!DEFAULT.equalsIgnoreCase(databaseConfig.getName())){
                this.addDatabaseConfig(databaseConfig);
            }
        } catch (Exception e) {
            throw new BaseException(e);
        } 
    }
    
    public boolean deleteDatabaseConfig(String configName) throws BaseException{
        try {
            Element root = xmlFile.getDoc(false, null).getRootElement();
            Element databaseConfigsEle = root.element(TAG_DATABASE_CONFIGS);
            if(databaseConfigsEle != null){
                Element databaseConfigEle = (Element) databaseConfigsEle.selectSingleNode("//DatabaseConfig[name='" 
                                + configName +  "']");
                if(databaseConfigEle != null){
                    databaseConfigsEle.remove(databaseConfigEle);
                    this.getDatabaseConfigs().remove(configName);
                    xmlFile.saveDoc();
                    return true;
                }
            }
            
        } catch (Exception e) {
            throw new BaseException(e);
        } 
        return false;
    }
    
    private XmlFileUtil xmlFile = null;
    
	public DatabaseConfig getDefDatabaseConfig() {
		return defDtabaseConfig;
	}

    public List<String> getAllConnNames(){
        List<String> allConnNames = new LinkedList<String>();
        allConnNames.add(this.getDefDatabaseConfig().getName());
        for(String configName : this.getDatabaseConfigs().keySet()){
            allConnNames.add(configName);
        }
        
        return allConnNames;
    }
	public void setDefDatabaseConfig(DatabaseConfig defDatabaseConfig) {
		this.defDtabaseConfig = defDatabaseConfig;
	}

    public XmlFileUtil getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(XmlFileUtil xmlFile) {
        this.xmlFile = xmlFile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

	public Map<String, DatabaseConfig> getDatabaseConfigs() {
		return databaseConfigs;
	}

	public void setDatabaseConfigs(Map<String, DatabaseConfig> databaseConfigs) {
		this.databaseConfigs = databaseConfigs;
	}
	
	public void addDatabaseConfig(DatabaseConfig databaseConfig){
		this.getDatabaseConfigs().put(databaseConfig.getName(), databaseConfig);
	}
	
	public DatabaseConfig removeDatabaseConfig(String configName){
		return this.getDatabaseConfigs().remove(configName);
	}

	public Map<String, Connection> getDatabaseConnectionCache() {
		return databaseConnectionCache;
	}

	public void setDatabaseConnectionCache(
			Map<String, Connection> databaseConnectionCache) {
		this.databaseConnectionCache = databaseConnectionCache;
	}

	@Override
	protected void finalize() throws Throwable {
		this.releaseAllConn();
		super.finalize();
	}
	
	protected void releaseAllConn(){
		Collection<Connection> conns = this.getDatabaseConnectionCache().values();
		for(Connection conn : conns){
			if(conn != null){				
				try {
					if(!conn.isClosed()){
						conn.close();
					}
					conn = null;
				} catch (SQLException e) {
					logger.error("releaseAllConn",e );
				}				
			}
		}
		this.getDatabaseConnectionCache().clear();
	}
}

class DatabaseConfigMap<key,value> extends TreeMap<key,value>{
    
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 2L;

	public String toString(){
        return RM.R("label.main.tree.connPool");
    }
}
