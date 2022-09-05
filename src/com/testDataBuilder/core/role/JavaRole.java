package com.testDataBuilder.core.role;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.exception.BaseException;

/**
 * 高级规则支持(java代码)
 * <p>Title：JavaRole.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2008 frontier,Inc</p>
 * <p>Company：company-name,Inc</p> 
 * @author LiuXiaojie 2008-9-5
 * @version 1.0
 */
public class JavaRole implements IJavaRole {

	private TableConfig tableConfig = null;

	protected static Logger logger = Logger.getLogger(JavaRole.class);
	
	public static final String JAVA_SOURCE_FILE = "JavaRoleEx.java.bak";
	/* (non-Javadoc)
	 * @see com.testDataBuilder.core.role.IJavaRole#getValue(java.lang.String)
	 */
    public Object getValue(String field) throws BaseException, SQLException{
    	return this.getTableConfig().getValue(field);
    }
    
    /* (non-Javadoc)
	 * @see com.testDataBuilder.core.role.IJavaRole#getReferencer(java.lang.String)
	 */
    public TableConfig getReferencer(String ... fields ) throws BaseException, SQLException{
    	return tableConfig.getReferencer(fields);
    }
    
	/* (non-Javadoc)
	 * @see com.testDataBuilder.core.role.IJavaRole#getTableConfig()
	 */
	public TableConfig getTableConfig() {
		return tableConfig;
	}

	/* (non-Javadoc)
	 * @see com.testDataBuilder.core.role.IJavaRole#setTableConfig(com.testDataBuilder.config.TableConfig)
	 */
	public void setTableConfig(TableConfig tableConfig) {
		this.tableConfig = tableConfig;
	}
	
	/* (non-Javadoc)
	 * @see com.testDataBuilder.core.role.IJavaRole#getComplexObjByJava()
	 */
	public Object getValueByJava() throws BaseException, SQLException{
		return null;
	}
	
	public static String getJavaSourceTemplate() throws IOException{
		InputStream is = JavaRole.class.getResourceAsStream(JAVA_SOURCE_FILE);
		if(is == null){
			logger.error("get Java Source from " + JAVA_SOURCE_FILE + " faild.");
			return null;
		}
		return IOUtils.toString(is, "utf8");
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(getJavaSourceTemplate());
	}
}
