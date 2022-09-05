package com.testDataBuilder.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.JavaCodeRuntimeException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.util.AppProperty;
import com.testDataBuilder.util.Global;
import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.core.baseType.JavaTypes;

public class GenerateAndInsertData {

    static Logger logger = Logger.getLogger(GenerateAndInsertData.class);
    
	/**
	 * <p><code>main</code></p>
	 * @param args
	 * @author LiuXiaojie 2007-6-17
	 * @throws BaseException 
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws BaseException, SQLException, IOException {
		AppProperty appProperty = AppProperty.getInstance();
		appProperty.loadFromDefXmlFile();
		Global.getInstance().initlize();
		JavaTypes.getInstance().init("E:\\workspace\\TestDataBuilder\\res\\res\\datatypes");
		GenerateAndInsertData gen = new GenerateAndInsertData();
        gen.generateData("D:\\school");
	}
    
    Insert2Table curGenerateor = null;
    
    private IProcess processBar = null;
    
    private SQLRecorder sqlRecorder = null;
    
    private boolean _break = false;
    
    public void setBread(boolean _break){
        this._break = _break;
        if(curGenerateor != null){
            curGenerateor.setBread(_break);
        }
    }
    
    public void showInfoAndPos(String info, int pos){
        if(this.processBar != null){
            processBar.setInfo(info);
            processBar.setProcessBarPos(pos);
        }
    }

    public void generateData(WorkspaceDataCache workspaceDataCache)throws SQLException, IOException, JavaCodeRuntimeException{
        TestDataConfig testDataConfig = workspaceDataCache.getTestDataConfig();       
        List<TableConfig> tableConfigs = workspaceDataCache.getTableConfigs();
        generateData(tableConfigs, testDataConfig);
    }
    

    public void generateData(List<TableConfig> tableConfigs,TestDataConfig testDataConfig)throws SQLException, IOException, JavaCodeRuntimeException{
        logger.info("◆◆◆◆◆◆◆◆◆◆◆◆  begin task ◆◆◆◆◆◆◆◆◆◆◆◆");

        Connection defConn = null;
        try {
            defConn = testDataConfig.getDefConnection();
            defConn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(RM.R("label.error.GenerateAndInsertData.getDefConnError") + ".INFO[" + e.getMessage());
            logger.info("◆◆◆◆◆◆◆◆◆◆◆◆ end task ◆◆◆◆◆◆◆◆◆◆◆◆");
            throw e;
        }
        
        showInfoAndPos(RM.R("label.GenerateAndInsertData.info.initSuccess"), 0);

        try{
            for(int i=0;i < tableConfigs.size() && !_break; i++){   
                TableConfig tableConfig = tableConfigs.get(i);
                if(tableConfig.getIsGenerate()){
                    logger.info("〓〓〓〓〓〓〓〓〓 begin table[" + tableConfig.getTableName()
                            + "] count[" + tableConfig.getCount() + "] 〓〓〓〓〓〓〓〓〓");
                    curGenerateor = new Insert2Table(tableConfig, testDataConfig);
                    curGenerateor.setProcessBar(processBar);
                    curGenerateor.setBread(_break);
                    curGenerateor.setSqlRecorder(sqlRecorder);
                    curGenerateor.insert();                      
                    logger.info("〓〓〓〓〓〓〓〓〓 end talbe[" + tableConfig.getTableName()
                            + "] count[" + tableConfig.getCount() + "] 〓〓〓〓〓〓〓〓〓");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("GenerateAdnInsertData",e);
                    }
                }
            }
        } finally{
            logger.info("◆◆◆◆◆◆◆◆◆◆◆◆ end task ◆◆◆◆◆◆◆◆◆◆◆◆");
            if(defConn != null){
                try {
                    defConn.close();
                } catch (SQLException ex) {
                    logger.error("defConn.close()", ex);
                }                
            }
        }        
        return ;
    }
    
    public void generateData(String workspace)throws SQLException, IOException, BaseException{
        WorkspaceDataCache workspaceDataCache = WorkspaceDataCache.getInstance();
        workspaceDataCache.init(workspace);
        workspaceDataCache.initAll();
        generateData(workspaceDataCache);        
    }
    
    public SQLRecorder getSqlRecorder() {
        return sqlRecorder;
    }

    public void setSqlRecorder(SQLRecorder sqlRecorder) {
        this.sqlRecorder = sqlRecorder;
    }

    public IProcess getProcessBar() {
        return processBar;
    }

    public void setProcessBar(IProcess processBar) {
        this.processBar = processBar;
    }
}
