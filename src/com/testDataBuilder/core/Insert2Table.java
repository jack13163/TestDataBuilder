package com.testDataBuilder.core;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.config.ColumnConfig;
import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.core.role.ComplexObj;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.JavaCodeRuntimeException;
import com.testDataBuilder.resources.RM;

/**
 * 插入数据到表中.
 * <p>Title：Insert2Table.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-6-17
 * @version 1.0
 */
public class Insert2Table {

    static Logger logger = Logger.getLogger(Insert2Table.class);
    
	private Connection conn = null;
	
	private TableConfig tableConfig = null;

	private TestDataConfig testDataConfig = null;
	
    
    private boolean _break = false;
    
    public void setBread(boolean _break){
        this._break = _break;
    }
    
    private SQLRecorder sqlRecorder = null;
    
    private boolean isRecordSQL = false;
    
    private IProcess processBar = null;
    
    public Insert2Table(TableConfig tableConfig, TestDataConfig testDataConfig) throws SQLException{
		this.tableConfig = tableConfig;
		this.testDataConfig = testDataConfig;
		this.conn = testDataConfig.getDefConnection();
	}

    public void writeSQL(String sql) throws IOException{
        sqlRecorder.writeSql(sql);
    }
    
	/**
     * 执行一段更新语句.
     * <p><code>executeSQL</code></p>
     * @param conn 数据库连接.
     * @param sql 要执行的SQL语句.
     * @return 返回更新的行数. 
     * @author LiuXiaojie 2007-5-29
     */
    public int executeSQL(String sql){
        PreparedStatement state = null;
        
        try {
            state = this.conn.prepareStatement(sql);
            int size =  state.executeUpdate();
            return size;
        } catch (SQLException e) {
            logger.error("error in executeSQL, sql is[" +sql + "]", e);
        } finally {
        	if(state != null){
        		try {
					state.close();
                    state = null;
				} catch (SQLException ex) {
                    logger.error("state.close", ex);
				}
        	}
        }
        
        return 0;
    }


    public void showInfoAndPos(String info, int pos){
        if(this.processBar != null){
            processBar.setInfo(info);
            processBar.setProcessBarPos(pos);
        }
    }

    protected int init(String sql){
        return executeSQL(sql);
	}
	
	protected int destroy(String sql){
		return executeSQL(sql);
	}

	public void insert() throws IOException,SQLException, JavaCodeRuntimeException {
	    if(this.conn == null){
         throw new InvalidParameterException("Insert2Table.conn is null");   
        }
        if(this.tableConfig == null){
            throw new InvalidParameterException("Insert2Table.tableConfig is null");   
        }
        this.tableConfig.clearCache();
        
        if(this.tableConfig.getColumnConfigs().size() == 0){
            showInfoAndPos(String.format(RM.R("label.Insert2Table.info.overpass.noFieldToInsert"),
                            tableConfig.getTableName()) , 0);
            return;
        }
        
        if(_break){
            return ;
        }
        
        showInfoAndPos(String.format(RM.R("label.Insert2Table.info.beginGenerateData"),
                        tableConfig.getTableName()) , 0);
        
        try{
			String strInit = tableConfig.getInit();
			if(StringUtils.isNotEmpty(strInit)){
                String[] initArr = strInit.split(";");
                for(String init : initArr){
                    if(StringUtils.isNotEmpty(init)){
                        String info = String.format(RM.R("label.Insert2Table.info.beginInitTable"),
                                        tableConfig.getTableName(), init);
                        
                        showInfoAndPos(info,0);
                        this.init(init);
                    }
                }		
                if(sqlRecorder != null){
                    writeSQL(strInit);
                }
			}
			long count = tableConfig.getCount();
			if(tableConfig.isCloseIdAutoInsert()){
	            setIdentityInsert(tableConfig.getTableName(), true);
			}
            
			initRole();
            String pattern = RM.R("label.Insert2Table.info.beginInsertData");
			for(long i=0;i < count && !_break; i++){
				insertItem();
                String info = String.format(pattern,tableConfig.getTableName(), i + 1);
                showInfoAndPos(info, (int)i);
			}
        }catch(JavaCodeRuntimeException ex){
        	throw ex;
        } catch (BaseException e) {
            String info = String.format("插入数据到%s表失败,%s",this.tableConfig.getTableName(), e.getMessage());
            logger.error(info, e);
            showInfoAndPos(info,100);
        }finally{
        	tableConfig.clearCache();
        	tableConfig.destroyStates();
        	
        	if(tableConfig.isCloseIdAutoInsert()){	            
	            setIdentityInsert(tableConfig.getTableName(), false);
			}
        	String strDestroy = tableConfig.getDestroy();
			if(StringUtils.isNotEmpty(strDestroy)){
                String[] destroyArr = strDestroy.split(";");
                for(String destroy : destroyArr){
                    if(StringUtils.isNotEmpty(destroy)){
                        this.destroy(destroy);
                    }
                }   
			}
            String info = String.format(RM.R("label.Insert2Table.info.endGenerateData")
                            , tableConfig.getTableName(), successCount, errorCount);
            
            showInfoAndPos(info , 100);      
            
            logger.info("end insert data to[" + tableConfig.getTableName() + "]");
            logger.info("success insert [" + successCount + "] records");
            logger.info("error   insert [" + errorCount + "] records");
            successCount = 0;
            errorCount = 0;
        }		
	}

	private void initRole() throws SQLException, BaseException{
        List<String> allRoles = tableConfig.getAllRoles();
        for(String roleName : allRoles){
        	Role role = getRoleFactory().getRole(roleName);
        	if(role != null){
        		String sourceName = role.getSourceName();
                int maxRows = 0;
                if(tableConfig.getCount() > Integer.MAX_VALUE){
                    maxRows = Integer.MAX_VALUE;
                }else{
                    maxRows = Double.valueOf((tableConfig.getCount() * 1.2)).intValue();
                }
                
        		if(StringUtils.isEmpty(sourceName)){
        			getRoleFactory().initRole(roleName, conn, maxRows);
        		}else{
        			Connection refConn = this.testDataConfig.getConn(sourceName);
        			getRoleFactory().initRole(roleName, refConn, maxRows);
        		}
        	}
            
        }
    }

    private void insertItem() throws IOException, BaseException{
		
        PreparedStatement state = tableConfig.getInsertStatement(conn);
        List columnConfigs = tableConfig.getColumnConfigs();
        SQLGenerator sqlGenerator = new SQLGenerator(tableConfig.getInsertSQLFormat());        

          try{
            for (int i = 0, j = 0; i < columnConfigs.size(); i++) {
                ColumnConfig columnConfig = (ColumnConfig) columnConfigs.get(i);
                
                Role role = columnConfig.getRole();
            	if(role != null && !role.isSQLFuncMethod()){
            		Object value = tableConfig.getValue(columnConfig);
            		
                    if(value instanceof ComplexObj){
                    	throw new BaseException(String.format("列[%s]配置有误，[%s]不是合法得引用。请检查。", 
                    			columnConfig.getName(), columnConfig.getRef()));
                    }
                    sqlGenerator.addArg(value);                
                    if(value == null){
                        state.setString(++j, null);
                    }else{
                        state.setObject(++j, value);
                    }
                }
            }         
//            if(logger.isDebugEnabled()){
//                logger.debug(String.format("begin execute [%s", sqlGenerator.getSQL()));
//            }
			state.executeUpdate();
			state.clearParameters();

			successCount++;
            if(sqlRecorder != null){
            	String sql = sqlGenerator.getSQL();
                writeSQL(sql);
            }
		} catch (SQLException ex){
			errorCount++;            
			if(this.tableConfig.getOnError().equals(TableConfig.ON_ERROR_EXIT)){
                logger.error(String.format("execute [%s] error[exit]",sqlGenerator.getSQL()), ex);
				throw new BaseException(ex);
		    }else if(this.tableConfig.getOnError().equals(TableConfig.ON_ERROR_IGNORE)){
                logger.error(String.format("execute [%s] error[ignore]",sqlGenerator.getSQL()), ex);
		    }else if(this.tableConfig.getOnError().equals(TableConfig.ON_ERROR_QUERY)){
                logger.error(String.format("execute [%s] error[query]",sqlGenerator.getSQL()), ex);
		        //出错时询问用户，如果点OK，则继续.
		        if(!this.askUser(ex.getMessage())){
		            
		        }
		    } 
		}finally{
			tableConfig.clearCache();
		}
        
	}
	
    private boolean askUser(String message) {
        return false;
    }

	
    int successCount = 0;
    int errorCount = 0;

    /**
     * 打开自动增长列的插入开关.
     * <p><code>setIdentityInsert</code></p>
     * @param tableName
     * @param isOn = true关掉自动插入.
     * @author LiuXiaojie 2007-4-25
     * @throws IOException 
     */
    private void setIdentityInsert(String tableName, boolean isOn) throws IOException{
        Statement statement = null;
        try{
            String sql = "SET IDENTITY_INSERT " + tableName + " " + (isOn?"ON":"OFF");
            statement = this.conn.createStatement();
            statement.execute(sql);              
            if(isOn){
                logger.info("已经将表[" + tableName + "]上的IDENTITY列设置为可以插入");
            }else{
                logger.info("已经将表[" + tableName + "]上的IDENTITY列还原为不可以插入");
            }
            if(sqlRecorder != null){
                writeSQL(sql);
            }
        }catch(SQLException ex){
            logger.error("setIdentityInsert", ex);
        } finally{
            if(statement != null){
                try {
                    statement.close();
                    statement = null;
                } catch (SQLException ex) {
                    logger.error("Insert2Table", ex);
                }
                statement = null;
            }
        }
    }

	private RoleFactory roleFactory = null;

    private RoleFactory getRoleFactory(){
        if(roleFactory == null){
           roleFactory = tableConfig.getRoleFactory();            
        }
        return roleFactory;
    }

    public IProcess getProcessBar() {
        return processBar;
    }

    public void setProcessBar(IProcess processBar) {
        this.processBar = processBar;
    }

    public SQLRecorder getSqlRecorder() {
        return sqlRecorder;
    }

    public void setSqlRecorder(SQLRecorder sqlRecorder) {
        this.sqlRecorder = sqlRecorder;
    }

    public boolean isRecordSQL() {
        return isRecordSQL;
    }

    public void setRecordSQL(boolean isRecordSQL) {
        this.isRecordSQL = isRecordSQL;
    }

}
