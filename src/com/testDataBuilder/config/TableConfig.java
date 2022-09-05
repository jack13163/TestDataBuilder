package com.testDataBuilder.config;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.testDataBuilder.core.role.ComplexObj;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.dbMetaInfo.Database;
import com.testDataBuilder.dbMetaInfo.ForeignKey;
import com.testDataBuilder.dbMetaInfo.Table;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.TagNotFoundException;
import com.testDataBuilder.util.Global;
import com.testDataBuilder.util.XmlFileUtil;


public class TableConfig {
	
	 static Logger logger = Logger.getLogger(TableConfig.class);
	
    public static final String TAG_ROLES = "roles";
	public static final String TAG_TABLE = "table";
	public static final String TAG_NAME = "name";
	public static final String TAG_CLOSE_ID_AUTO_INSERT = "closeIdAutoInsert";
	public static final String TAG_COUNT = "count";
	//public static final String TAG_INSERT_TYPE = "insertType";
	public static final String TAG_ON_ERROR = "onError";
	public static final String TAG_INIT = "init";
	public static final String TAG_DESTROY = "destroy";
	public static final String TAG_COLUMN = "column";
	public static final String TAG_REF = "ref";
	public static final String TAG_VALUE = "value";
	public static final String TAG_CODE = "code";
	
	/**
	 * <table name="tableName" closeIdAutoInsert="false" count="500"
		insertType="idInsert" onError="" >
		<init description="数据表的初始化">sel语句</init>
		<destroy description="当数据传输完成后需要对目的表的清理工作.">sql语句</destroy>
		<column name="columnName" ref="birthday">
			<code>
			</code>
		</column>	
		<column name="cID" ref="class.id" isPK="false" value="3"/>
		<column name="cName" ref="class.name"/>
		<column name="sex" ref="sex"/>
	</table>
	 */
    
    public static final String CONIFG_DEF_VALUE = "<?xml version='1.0' encoding='utf-8'?>" + Global.LINE_SEP
                + "<table name='tableName' closeIdAutoInsert='false' count='100' onError='ignore'>" + Global.LINE_SEP
                + "<dataTypes>"+ Global.LINE_SEP
                + "</dataTypes>"+ Global.LINE_SEP
                + "</table>";
    
    public String getConfigDefValue(){
        return CONIFG_DEF_VALUE.replaceAll("tableName", this.getTableName());
    }
	
    public TableConfig configure(File file) throws BaseException{
        if (!file.exists()) {
            throw new BaseException("file[" + file.getName()
                    + "] is not exists!");
        }
        XmlFileUtil xmlFile = new XmlFileUtil(file);
        try {
            Element ele = xmlFile.getDoc(false, null).getRootElement();
            return configure(ele);
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

    /**
	 * <p><code>configure</code></p>
	 * @param tableEle
	 * @return
	 * @throws TagNotFoundException
	 * @author LiuXiaojie 2007-6-17
	 */
	public TableConfig configure(Element tableEle) throws BaseException{
		String strName = tableEle.attributeValue(TAG_NAME);
		if(StringUtils.isEmpty(strName)){
			throw new TagNotFoundException(TAG_NAME);
		}
		this.setTableName(strName);
        
		String onError = tableEle.attributeValue(TAG_ON_ERROR);
		if(StringUtils.isNotEmpty(onError)){
			this.setOnError(onError);
		}

		String strCount = tableEle.attributeValue(TAG_COUNT);
		if(StringUtils.isNotEmpty(strCount)){
			try{
			this.setCount(Long.valueOf(strCount).longValue());
			}catch(Exception ex){
				throw new BaseException(ex);
			}
		}
		
		String closeIdAutoInsert = tableEle.attributeValue(TAG_CLOSE_ID_AUTO_INSERT);
		if(closeIdAutoInsert != null 
				&& closeIdAutoInsert.equalsIgnoreCase("true")){
			this.setCloseIdAutoInsert(true);
		}
		
		String init = tableEle.elementTextTrim(TAG_INIT);
		if(StringUtils.isNotEmpty(init)){
			this.setInit(init);
		}
		
		String destroy = tableEle.elementTextTrim(TAG_DESTROY);
		if(StringUtils.isNotEmpty(destroy)){
			this.setDestroy(destroy);
		}
		
		List columns = tableEle.elements(TAG_COLUMN);
		if(columns != null){
			for(int i=0;i < columns.size();i++){
				Element columnEle = (Element) columns.get(i);
				ColumnConfig columnConfig = ColumnConfig.configure(columnEle);
				this.addColumnConfig(columnConfig);
			}
		}
		
        this.roleFactory = new RoleFactory(strName);
        roleFactory.setTableConfig(this);
		Element ele = tableEle.element(TAG_ROLES);
		if(ele != null){
            this.roleFactory.configure(ele);            
		}
        
		return this;
	}
	
	@Override
	public String toString() {
		return this.tableName;
	}

	public Element toElement(){
		Element tableConfigEle = DocumentHelper.createElement(TAG_TABLE);
		
		tableConfigEle.addAttribute(TAG_NAME, this.getTableName());
		tableConfigEle.addAttribute(TAG_CLOSE_ID_AUTO_INSERT, this.isCloseIdAutoInsert() + "");
		tableConfigEle.addAttribute(TAG_COUNT, this.getCount() + "");
        tableConfigEle.addAttribute(TAG_ON_ERROR, this.getOnError());
		if(StringUtils.isNotEmpty(this.getInit())){
			Element initEle = tableConfigEle.element(TAG_INIT);
			if(initEle == null){
				initEle = tableConfigEle.addElement(TAG_INIT);
			}
			initEle.setText(this.getInit());
		}
        
		if(StringUtils.isNotEmpty(this.getDestroy())){
			Element destroyEle = tableConfigEle.element(TAG_DESTROY);
			if(destroyEle == null){
				destroyEle = tableConfigEle.addElement(TAG_DESTROY);
			}
			destroyEle.setText(this.getDestroy());
		}
		
		List<ColumnConfig> columnConfigs = this.getColumnConfigs();
		if(columnConfigs != null && columnConfigs.size() > 0){
			for(ColumnConfig columnConfig : columnConfigs){
				tableConfigEle.add(columnConfig.toElement());
			}
		}
        
		RoleFactory roleFactory = this.getRoleFactory();
        if(roleFactory != null){
        	Element roleFactoryEle = roleFactory.toElement();
            tableConfigEle.add(roleFactoryEle);
        }else{
        	tableConfigEle.addElement(TAG_ROLES);
        }
        
		return tableConfigEle;
	}

	public void saveConfig() throws IOException{
		String xml = this.toElement().asXML();		
		xml = XmlFileUtil.formatXml(xml, "UTF-8");
		FileUtils.writeStringToFile(this.getConfigFile(),
				xml, "utf-8");
	}
	
    public void deleteAndBankupFile(){
        File configFile = getConfigFile();            
        if(configFile.exists()){
            if(!configFile.renameTo(new File(getBankupConfigFileName()))){
                configFile.delete();
            }
        }
    }
    
    public void updateAllRefs(String oldGenerateName, String newGenerateName){
        if(this.getColumnConfigs() != null){
            for(ColumnConfig columnConfig : this.getColumnConfigs()){
                if(columnConfig.getRef().equalsIgnoreCase(oldGenerateName)){
                    columnConfig.setRef(newGenerateName);
                }else if(columnConfig.getRef().startsWith(oldGenerateName + ".")){
                    String tempRef = columnConfig.getRef().replace(oldGenerateName + ".", newGenerateName + ".");
                    columnConfig.setRef(tempRef);
                }
            }
        }
    }
    
    public void deleteBankupFile(){
        File bankupFile = new File(getBankupConfigFileName());
        if(bankupFile.exists()){
            bankupFile.delete();
        }
    }
	public File getConfigFile(){
		return WorkspaceDataCache.getInstance().getTableConfigFile(tableName);
	}
	
	public String getBankupConfigFileName(){
	    return this.getConfigFile().getAbsolutePath() + ".bank";   
    }
    
	/**
	 * 表名.
	 */
	private String tableName;
	/**
	 * 关闭自动增长列.
	 */
	private boolean closeIdAutoInsert = false;
	/**
	 * 插入的数据行数.
	 */
	private long count = 100;
	/**
	 * 初始化sql语句.
	 */
	private String init = null;
	/**
	 * 数据插完后的清理.sql语句.
	 */
	private String destroy = null;
	
    private RoleFactory roleFactory;
    
    private Boolean isGenerate = Boolean.TRUE;
    
	/**
	 * 退出.
	 */
	public static final String ON_ERROR_EXIT = "exit";
	/**
	 * 忽略.
	 */
	public static final String ON_ERROR_IGNORE = "ignore";
	/**
	 * 询问.
	 */
	public static final String ON_ERROR_QUERY = "query";
	
	private String onError = ON_ERROR_IGNORE;
	
	/**
	 * 先删掉再添加.
	 */
//	public static final String INSERT_TYPE_DELETE = "delete";
//	
//	public static final String INSERT_TYPE_ID_INSERT = "idInsert";
//	
//	private String insertType = INSERT_TYPE_ID_INSERT;
	
	
	/**
	 * 列.
	 */
	private List<ColumnConfig> columnConfigs = new LinkedList<ColumnConfig>();


	public void addColumnConfig(ColumnConfig columnConfig){
		columnConfig.setTableConfig(this);
		this.getColumnConfigs().add(columnConfig);
	}
	
	public boolean isCloseIdAutoInsert() {
		return closeIdAutoInsert;
	}

	public void setCloseIdAutoInsert(boolean closeIdAutoInsert) {
		this.closeIdAutoInsert = closeIdAutoInsert;
	}

	public List<ColumnConfig>  getColumnConfigs() {
		return columnConfigs;
	}

    public static List<ColumnConfig> cloneColumnConfigs(List<ColumnConfig> columnConfigs){
        if(columnConfigs != null){
            List<ColumnConfig> tempColumnConfigs = new LinkedList<ColumnConfig>();
            for(ColumnConfig columnConfig : columnConfigs){
               tempColumnConfigs.add(columnConfig.clone());
            }
            return tempColumnConfigs;
        }
        
        return null;
    }
    
    
    /**
     * 设置columnConfig.
     * <p><code>setColumnConfigs</code></p>
     * @param columnConfigs
     * @author LiuXiaojie 2007-12-11
     */
	public void setColumnConfigs(List<ColumnConfig> columnConfigs) {
		this.columnConfigs = columnConfigs;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getDestroy() {
		return destroy;
	}

	public void setDestroy(String destroy) {
		this.destroy = destroy;
	}

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getOnError() {
		return onError;
	}

	public void setOnError(String onError) {
		this.onError = onError;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
    
    /**
     * 得到所有用到的role名称.
     * <p><code>getAllUsedRoles</code></p>
     * @return
     * @author LiuXiaojie 2007-6-18
     */
    public List<String> getAllRoles(){
        List<String> roleNames = new LinkedList<String>();
        for(int i=0;i < this.getColumnConfigs().size(); i++){
            ColumnConfig columnConfig = (ColumnConfig) this.getColumnConfigs().get(i);
            String roleName = columnConfig.getRoleName();
            if(!roleNames.contains(roleName)){
                roleNames.add(roleName);
            }
        }
        return roleNames;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final TableConfig other = (TableConfig) obj;
        if (tableName == null) {
            if (other.tableName != null) return false;
        } else if (!tableName.equals(other.tableName)) return false;
        return true;
    }

    public TableConfig(String tableName){
    	this.tableName = tableName;
    }
    
    public TableConfig(){
    	
    }

    public RoleFactory getRoleFactory() {
        return roleFactory;
    }

    public void setRoleFactory(RoleFactory roleFactory) {
        this.roleFactory = roleFactory;
        this.roleFactory.setTableConfig(this);
    }

    public Boolean getIsGenerate() {
        return isGenerate;
    }

    public void setIsGenerate(Boolean isGenerate) {
        this.isGenerate = isGenerate;
    }
    
    public RoleFactory getGlobalRoleFactory(){
    	return WorkspaceDataCache.getInstance().getGlobalRoleFactory() ;
    }
    
    /**
	 * 根据参照的role生成数据.
	 * <p><code>getValueByRole</code></p>
	 * @return
	 * @throws BaseException
	 * @throws SQLException
	 * @author LiuXiaojie 2008-9-5
     * @throws BaseException 
     * @throws SQLException 
	 */
	private Object getValueByRole(ColumnConfig columnConfig) throws BaseException, SQLException {
        Object retValue = null;
		String ref = columnConfig.getRef();
		if (ref != null) {
			if (columnConfig.isComplexType()) { 
				String roleName = ref.substring(0, ref.indexOf("."));
				String valueKey = ref.substring(ref.indexOf(".") + 1);
				ComplexObj complexObj = getComplexRole(roleName);
                if(complexObj != null){
                    retValue = complexObj.getValue(valueKey);
                }else{
                    retValue = null;
                }
			} else {
                if(this.getGlobalRoleFactory() != null){
                    retValue = this.getGlobalRoleFactory().getValueByRole(ref);
                }
                if(retValue == null){
                    retValue = getRoleFactory().getValueByRole(ref);
                }
			}
		} 
        
		return retValue;
	}
	
	/**
	 * 从RoleFactory取出一个复合数据类型
	 * <p>
	 * <code>getComplexRole</code>
	 * </p>
	 * 
	 * @param roleName
	 * @return
	 * @author LiuXiaojie 2007-6-17
	 * @throws BaseException 
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public ComplexObj getComplexRole(String roleName) throws BaseException, SQLException {
		Object complexObj = (Object) complexTypes.get(roleName);
		if (complexObj == null) {
            Object tempObj = null;
            if(this.getGlobalRoleFactory() != null){
                tempObj = this.getGlobalRoleFactory().getValueByRole(roleName);
            }
            if(complexObj == null){
                tempObj = getRoleFactory().getValueByRole(roleName);
            }
            
            if(tempObj != null){
                try{
                    complexObj = (Object)tempObj;
                }catch(Throwable ex){
                    throw new BaseException(String.format("获取的复合类型[%s]转换出错", roleName), ex);
                }
            }else{
            	complexObj = new NullObject();
            }
			complexTypes.put(roleName, complexObj);
		}
		if(complexObj instanceof NullObject){
			return null;
		}
		return (ComplexObj)complexObj;
	}

	private Map<String, Object> valueCache = new HashMap<String, Object>();
	
	private Map<String,Object> complexTypes = new HashMap<String,Object>();
	
	public void clearCache(){
		this.complexTypes.clear();
		this.valueCache.clear();
	}
	
	/**
	 * 根据配置的规则,生成值,并将取的值,存入cache中.
	 * <p><code>getValue</code></p>
	 * @param columnConfig
	 * @return
	 * @throws BaseException
	 * @author LiuXiaojie 2008-9-5
	 * @throws SQLException 
	 */
	public Object getValue(ColumnConfig columnConfig) throws BaseException, SQLException{
		Object value = valueCache.get(columnConfig.getName());
		if(value == null){
			value = this.getValueByRole(columnConfig);
			if(value == null){
				value = new NullObject();
			}
			valueCache.put(columnConfig.getName(), value);
		}
		if(value instanceof NullObject){
			value = null;
		}
		return value;
	}

	/**
	 * 获取当前表当前行的一个字段的值。
	 * @param columnName 列名。
	 * @return 返回列值。
	 * @throws BaseException
	 * @throws SQLException
	 */
	public Object getValue(String columnName) throws BaseException, SQLException{
		ColumnConfig columnConfig = getColumnConfig(columnName);
		if(columnConfig == null){
			throw new BaseException("column [" + columnName + "] not exist");
		}
		return getValue(columnConfig);
	}
	
	/**
	 * 根据给定的字段，获取参照表。
	 * @param fields 字段（这些字段必须是一个外键中的多个字段）
	 * @return 方法会根据给出的fields，获取对应的值，并在参照表中找到对应的行。
	 * @throws BaseException
	 * @throws SQLException
	 */
    public TableConfig getReferencer(String ... fields ) throws BaseException, SQLException{
    	WorkspaceDataCache workspaceDataCache = WorkspaceDataCache.getInstance();
    	Database database = workspaceDataCache.getDatabase();
    	Table table = database.getTable(this.getTableName());
    	ForeignKey foreignKey = table.getForeignKeyByField(fields[0]);
    	String refTable = foreignKey.getRefTable();
    	TableConfig retTableConfig = workspaceDataCache.getTableConfig(refTable);
    	
    	StringBuilder sql = new StringBuilder("select * from ").append(refTable);
    	sql.append(" where 1=1 ");
    	List<Object> args = new LinkedList<Object>();
    	for(String field : fields){
    		Object value = this.getValue(field);
    		args.add(value);
    		String refField = foreignKey.getRefFiled(field);
    		sql.append(" and ").append(refField).append("=? ");
    	}
       Connection conn = workspaceDataCache.getTestDataConfig().getDefConnection();
       PreparedStatement statement = conn.prepareStatement(sql.toString());
       for (int i = 0; i < args.size(); i++) {
    	   if(args.get(i) == null){
    		   statement.setString(i+1,null);
    	   }else{
    		   statement.setObject(i+1, args.get(i));
       		}
       }
       ResultSet rs =  statement.executeQuery();
      
       if(rs.next()){
    	   ResultSetMetaData metaData = rs.getMetaData();
           int columnCount = metaData.getColumnCount();
           for(int i=0;i < columnCount;i++){
        	   String columnName = metaData.getColumnName(i+1);
        	   Object value = rs.getObject(columnName);
        	   if(value == null){
        		   value = new NullObject();
        	   }
        	   retTableConfig.valueCache.put(columnName, value);
           }           
       }
       
    	return retTableConfig;
    }
    
	public ColumnConfig getColumnConfig(String columnName){
		ColumnConfig columnConfig = null;
		List<ColumnConfig> columnConfigs = this.getColumnConfigs();
		if(columnConfigs != null){
			for(ColumnConfig tmpColumnConfig : columnConfigs){
				if(tmpColumnConfig.getName().equalsIgnoreCase(columnName)){
					columnConfig = tmpColumnConfig;
					break;
				}
			}
		}
		return columnConfig;
	}
	
	private String insertSQLFormat = null;
	private PreparedStatement insertStatement = null;
    
	/**
     * 得到插入语句.
     * <p><code>getInsertStatement</code></p>
     * @return
     * @throws DrmException
     * @author LiuXiaojie 2007-4-25
     * @throws SQLException 
     */
    public PreparedStatement getInsertStatement(Connection conn) {
        if (insertStatement == null) {
            if (this.getColumnConfigs().size() > 0) {
                StringBuffer strFields = new StringBuffer("insert into " + this.getTableName() + " (");
                StringBuffer values_ = new StringBuffer(" values (");
                
                List columnConfigs = this.getColumnConfigs();
                for (int i = 0; i < columnConfigs.size(); i++) {
                	ColumnConfig columnConfig = (ColumnConfig) columnConfigs.get(i);
                	
                    if (i == 0) {
                        strFields.append(columnConfig.getName());
                      
                    } else {
                        strFields.append(", " + columnConfig.getName());
                        values_.append(", ");
                    }
                	
                    Role role = this.getRoleFactory().getRole(columnConfig.getRef());
                	if(role != null && role.isSQLFuncMethod()){
                    	values_.append( role.getSQL());
                    }else{
                    	values_.append("?");
                    }
                }

                strFields.append(")");
                values_.append(")");
                try {
                    insertSQLFormat = strFields.toString() + values_.toString();
                    this.insertStatement = conn.prepareStatement(insertSQLFormat);
                    insertSQLFormat = insertSQLFormat.replace("?", "%s");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                logger.info("INSERT SQL:" + strFields.toString() + values_.toString());
            }else{
                logger.error("*************** no any insert fields! *****************");
            }
        }
        return insertStatement;
    }
   
	public void destroyStates() {
		if(this.insertStatement != null){
			try {
				this.insertStatement.close();
				this.insertSQLFormat = null;
			} catch (SQLException ex) {				
                logger.error("destroy", ex);
			}
			this.insertStatement = null;
			
		}
	}

	public String getInsertSQLFormat() {
		return insertSQLFormat;
	}
}
