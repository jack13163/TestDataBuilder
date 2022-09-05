package com.testDataBuilder.config;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.testDataBuilder.core.role.ComplexObj;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.TagNotFoundException;

/**
 * 列配置.

 * @version 1.0
 */
public class ColumnConfig {

	/**
	 * <column name="columnName" ref="birthday" prefix="stu-" suffix="end">
		<code>		
		</code>
	</column>	
	<column name="cID" ref="class.id" isPK="false" value="3"/>
	<column name="cName" ref="class.name"/>
	<column name="sex" ref="sex"/>
	 */
	public static final String TAG_COLUMN = "column";
	public static final String TAG_NAME = "name";
	public static final String TAG_REF = "ref";
	public static final String TAG_IS_PK = "isPK";
//	public static final String TAG_VALUE = "value";
	
	/**
	 * 解析列配置信息.
	 * <p><code>configure</code></p>
	 * @param columnEle
	 * @return
	 * @throws BaseException
	 * @author LiuXiaojie 2007-6-17
	 */
	public static ColumnConfig configure(Element columnEle) throws BaseException{
		ColumnConfig columnConfig = new ColumnConfig();
		String name = columnEle.attributeValue(TAG_NAME);
		if(StringUtils.isEmpty(name)){
			throw new TagNotFoundException(TAG_NAME);
		}
		columnConfig.setName(name);
		
		String ref = columnEle.attributeValue(TAG_REF);
        if(StringUtils.isEmpty(ref)){
            throw new TagNotFoundException(TAG_REF);
        }
		columnConfig.setRef(ref);
		
		
		String isPK = columnEle.attributeValue(TAG_IS_PK);
		if(isPK != null && isPK.equalsIgnoreCase("TRUE")){
			columnConfig.setPK(true);
		}

		return columnConfig;
	}
	
	public Element toElement(){
		Element columnConfigEle = DocumentHelper.createElement(TAG_COLUMN);
		
		columnConfigEle.addAttribute(TAG_NAME, this.getName());
		columnConfigEle.addAttribute(TAG_REF, this.getRef());
		columnConfigEle.addAttribute(TAG_IS_PK, this.isPK() + "");
//		columnConfigEle.addAttribute(TAG_VALUE, this.getValue());
		
		return columnConfigEle;
	}
	
	public ColumnConfig(){
		
	}
	
	public ColumnConfig(String name){
		this.name = name;
	}
	
    public ColumnConfig(ColumnConfig other){
        this.setName(other.getName());
        this.setPK(other.isPK());
        this.setRef(other.getRef());
        this.setTableConfig(other.getTableConfig());
    }
    
	private String name;
	
	private String ref;
	
	private boolean isPK = false;
	
	private TableConfig tableConfig = null;
	
//	private String value;


	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}


//	public String getValue() {
//		return value;
//	}
//
//	public void setValue(String value) {
//		this.value = value;
//	}
//	
    public boolean isComplexObj(){
        if(getRef() == null){
            return false;
        }
        
        return (getRef().indexOf(".") != -1);
    }
	
    public String getRoleName(){
        if(this.isComplexObj()){
            return ref.substring(0, ref.indexOf("."));
        }
        return this.getRef();
    }
    
    public String toString(){
    	return ToStringBuilder.reflectionToString(this, 
    			ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public ColumnConfig clone() {        
        return new ColumnConfig(this);
    }   
    
    /**
     * 获取参照的规则.
     * <p><code>getRole</code></p>
     * @return
     * @author LiuXiaojie 2008-9-5
     */
	public Role getRole() {
		Role role = null;
		String roleName = this.getRef();

		if (roleName != null) {
			if (isComplexType()) {
				roleName = roleName.substring(0, roleName.indexOf("."));

			}
			return getRoleByName(roleName);
		}

		return role;
	}
    
	public Role getRoleByName(String roleName){
		Role role = null;
		RoleFactory globalRoleFactory = WorkspaceDataCache.getInstance().getGlobalRoleFactory() ;
		if(globalRoleFactory != null){
			role = globalRoleFactory.getRole(roleName);
		}
		if(role == null){
			role = this.getTableConfig().getRoleFactory().getRole(roleName);
		}
		return role;
	}
	
//	/**
//	 * 根据参照的role生成数据.
//	 * <p><code>getValueByRole</code></p>
//	 * @return
//	 * @throws BaseException
//	 * @throws SQLException
//	 * @author LiuXiaojie 2008-9-5
//	 */
//	public Object getValueByRole() throws BaseException, SQLException {
//		return tableConfig.getValueByRole(this);
//	}
	
	public boolean isComplexType() {
		return (this.getRef().indexOf(".") != -1);
	}

	public TableConfig getTableConfig() {
		return tableConfig;
	}

	public void setTableConfig(TableConfig tableConfig) {
		this.tableConfig = tableConfig;
	}
	
}
