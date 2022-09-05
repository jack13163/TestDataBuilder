package com.testDataBuilder.core.role;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.xml.sax.SAXException;
import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.util.JDBCUtil;
import com.testDataBuilder.util.XmlFileUtil;

/**
 * 数据工厂.
 * <p>Title：RoleFactory.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-6-16
 * @version 1.0
 */
public class RoleFactory{
	
    static Logger logger = Logger.getLogger(RoleFactory.class);
    
	public static String GLOBAL = "label.roleFactory.global"; 

    private String roleFactoryName;
	
    private TableConfig tableConfig;
    
	public RoleFactory(String roleFactoryName){
		this.roleFactoryName = roleFactoryName;
        this.autoParseFileName(roleFactoryName);
	}
	
	public boolean isGlobalRoleFactory(){
		return GLOBAL.equals(this.getRoleFactoryName());
	}
	
	public String toString(){
        if(this.roleFactoryName == null){
            return null;
        }
        
        if(isGlobalRoleFactory()){            
            return RM.R(this.roleFactoryName);
        }else{        
            return this.roleFactoryName;
        }
	}
	
	private XmlFileUtil xmlFile = null;
	
	private File configFile = null;
	
	public RoleFactory configure() throws BaseException {

		if (configFile == null) {
			throw new InvalidParameterException("config file is null!");
		}
		
		if (!configFile.exists()) {
			throw new BaseException("file[" + configFile.getAbsolutePath()
					+ "] is not exists! file");
		}
		xmlFile = new XmlFileUtil(configFile);
		try {
            return configure((Element)getXmlDoc().selectSingleNode("//" + ROLES));
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}
	
    /**
     * 根据roleFactoryName自动配置。
     * <p><code>autoConfigure</code></p>
     * @return
     * @author LiuXiaojie 2007-12-10
     * @throws BaseException 
     */
    public RoleFactory autoParseFileName(String tableName){
        if(this.configFile == null){
            if(this.roleFactoryName.equalsIgnoreCase(RoleFactory.GLOBAL)){
                configFile = WorkspaceDataCache.getInstance().getGlobalRoleFile();                
            }else{
                configFile = WorkspaceDataCache.getInstance().getTableConfigFile(tableName);
            }
            
            xmlFile = new XmlFileUtil(configFile);           
        }
        return this;
    }
    
	public RoleFactory configure(Element rootEle) throws BaseException{		
		this.getRoles().clear();
		if(rootEle != null){
			List dataTypeEles = rootEle.elements(ROLE);
			for(int i=0;i < dataTypeEles.size();i++){
				Element dtEle = (Element) dataTypeEles.get(i);
				Role role = new Role(dtEle);
				this.addRole(role);
			}
		}
		return this;
	}
	
	public Element toElement(){
		List types = this.getAllDataTypes();
		Element rootEle = DocumentHelper.createElement(ROLES);
		for(int i=0;i < types.size(); i++){
			Role role = this.getRole((String) types.get(i));
			rootEle.add(role.toElement());
		}
		
		return rootEle;
	}
	//[end]

	public static final String ROLES = "roles";
	
	public static final String ROLE = "role";	
	
	private List<Role> roles = new ArrayList<Role>();
	
	/**
	 * 得到工厂中所有类型名称.
	 * <p><code>getAllDataTypes</code></p>
	 * @return 所有类型.List
	 * @author LiuXiaojie 2007-6-16
	 */
    public List<String> getAllDataTypes(){
        List<String> list =  new ArrayList<String>();
        for(Role role:this.roles){
        	list.add(role.getName());
        }
        Collections.sort(list);
        return list;
    }
    
      
	public List<Role> getSortedRoles(){
        Collections.sort(this.getRoles());   
        return this.getRoles();
	}

	
	/**
	 * 添加一个新的Role到内存中。
	 * <p><code>addRole</code></p>
	 * @param name
	 * @param role
	 * @author LiuXiaojie 2007-12-2
	 */
	public void addRole(Role role){
		this.roles.add(role);		
		role.setRoleFactory(this);
	}
	
	/**
	 * 删除一个Role.
	 * <p><code>removeRole</code></p>
	 * @param name
	 * @return
	 * @author LiuXiaojie 2007-12-2
	 */
	public Role removeRole(String name){
		for(Role role : this.roles){
			if(role.getName().equalsIgnoreCase(name)){
				if(roles.remove(role)){
					return role;
				}
			}
		}
		return null;
	}
	
	/**
	 * 从文件删除一个Role。
	 * <p><code>removeRoleFile</code></p>
	 * @param name
	 * @return
	 * @author LiuXiaojie 2007-12-2
	 */
	public Role removeRoleFromFile(String name){
        try {
            this.delRoleFromFile(name);
        } catch (Exception e) {
            logger.error("RoleFactory", e);
        } 
		return this.removeRole(name);
	}
	
	public Role getRole(String name){
		for(Role role : this.roles){
			if(role.getName().equalsIgnoreCase(name)){
				return role;
			}
		}
		return null;
	}
	
	/**
	 *得到下一个值.
	 * <p><code>getNextValue</code></p>
	 * @param roleName
	 * @return 
	 * @author LiuXiaojie 2007-6-16
	 * @throws SQLException 
	 * @throws BaseException 
	 * @throws SQLException 
	 * @throws  
	 */
	public Object getValueByRole(String roleName) throws BaseException, SQLException{		
		Role role = this.getRole(roleName);
        if(role == null){
            return null;
        }
		return role.getValueByRole();
	}

	public void initRole(String roleName, Connection conn, int maxRows) throws SQLException, BaseException {
        Role role = this.getRole(roleName);
        if(role != null){
            role.init(conn, maxRows);
        }
    }

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public XmlFileUtil getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(XmlFileUtil xmlFile) {
		this.xmlFile = xmlFile;
	}
	
	public Document getXmlDoc() throws SAXException, IOException, DocumentException{
		return xmlFile.getDoc(false,null);
	}
	
    public void saveToFile(Role role, String oldName) throws SAXException, IOException, DocumentException{
        Element rootEle = (Element)this.getXmlDoc().selectSingleNode("//" + ROLES);
        if(rootEle == null){
            rootEle = this.getXmlDoc().getRootElement().addElement(ROLES);
        }
        
        Element dtEle = (Element) rootEle.selectSingleNode(
                "//role[@name=\""+ oldName +"\"]");
        if(dtEle == null)/*新元素,添加*/{
            dtEle =  role.toElement();
            rootEle.add(dtEle);
        }else{
            role.toElement(dtEle);
        }
        this.getXmlFile().saveDoc();
    }
    
    public void saveOrUpdateToFile(Role role, String oldName) throws SAXException, IOException, DocumentException{
        saveToFile(role, oldName);
    }
    
    public void saveAllToFile() throws SAXException, IOException, DocumentException{
    	Element dataTypesEle = (Element)this.getXmlDoc().selectSingleNode("//" + ROLES);
        if(dataTypesEle != null){
            this.getXmlDoc().getRootElement().remove(dataTypesEle);
        }
        
        this.getXmlDoc().getRootElement().add(this.toElement());
        this.getXmlFile().saveDoc();
    }
    
    public void deleteAll() throws SAXException, IOException, DocumentException{    	
    	Element dataTypesEle = (Element)this.getXmlDoc().selectSingleNode("//" + ROLES);
        if(dataTypesEle != null){
            dataTypesEle.clearContent();
        }
        this.getXmlFile().saveDoc();
        this.getRoles().clear();
    }
    
    public void deleteItem(Role role) throws SAXException, IOException, DocumentException{  
        if(delRoleFromFile(role.getName())){
            this.getRoles().remove(role);
        }               
    }
    
	public boolean delRoleFromFile(String name) throws SAXException, IOException, DocumentException {
        Element rootEle = (Element)this.getXmlDoc().selectSingleNode("//" + ROLES);
		Element dtEle = (Element) rootEle.selectSingleNode(
				"//role[@name=\""+ name +"\"]");
		if(dtEle != null){
			rootEle.remove(dtEle);
			this.getXmlFile().saveDoc();	
            return true;
		}	
        return false;
	}
    
     /**
     * 返回用于List显示的类型。
     * <p><code>getAllRefRoles4List</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    public List<String> getAllRefRoles4List(){        
        List<String> list = new ArrayList<String>();
        for(Role role : this.getRoles()){
                String roleName = role.getName();
                //Role tempRole = this.getRole(genName);
                if(role.isSQLQueryMethod()){
                    List<String> allFields = JDBCUtil.getAllFields(role.getSQL());
                    for(String field : allFields){
                        list.add(roleName + "." + field);
                    }
                }else if(role.isJavaMethod() && role.getJavaSource().isComplexObjType()){
                	String refFields = role.getJavaSource().getReturnFields();
                	String[] strFields = refFields.split(",");
                	for(String field : strFields){
                		list.add(roleName + "." + field);
                	}
                }else{
                    list.add(roleName);
                }     
        }
        return list;
    }

	public String getRoleFactoryName() {
		return roleFactoryName;
	}

	public void setRoleFactoryName(String roleFactoryName) {
		this.roleFactoryName = roleFactoryName;
	}

    private static String defaultRoleName = "NewRole";
    
    public Role addDefaultRole() {
        List<String> roleNames = this.getAllDataTypes();
        
        String tempName = defaultRoleName;
        for(int i=0;i < Integer.MAX_VALUE; i++){
            if(roleNames.contains(tempName)){
                tempName = defaultRoleName + (i + 1);
            }else{
                break;
            }
        }
        Role role = new Role();
        role.setName(tempName);
        this.addRole(role);
        return role;
    }

    public TableConfig getTableConfig() {
        return tableConfig;
    }

    public void setTableConfig(TableConfig tableConfig) {
        this.tableConfig = tableConfig;
    }
    
}
