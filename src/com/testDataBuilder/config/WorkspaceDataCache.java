package com.testDataBuilder.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;

import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.config.TestDataConfig;
import com.testDataBuilder.config.preference.IPreference;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.dbMetaInfo.Database;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.util.Global;

public class WorkspaceDataCache {

	private String workspace;
	
	private Database database = null;
	
    public static final String GEN_SQL_DIR_NAME = "sqlFiles";
    
	public WorkspaceDataCache(){
		
	}
		
	private static WorkspaceDataCache instance = null;
	
	public static WorkspaceDataCache getInstance(){
		if(instance == null){
			instance = new WorkspaceDataCache();
		}
		return instance;
	}
	
	public WorkspaceDataCache init(String workspace) throws BaseException{
		
		if(StringUtils.isEmpty(workspace)){
			this.clearCache();
			this.workspace = workspace;
		}else{
			/**
			 * 只有工作区发生改变时，才初始化。
			 */
			if(!workspace.equalsIgnoreCase(this.workspace)){
				clearCache();
				this.workspace = workspace;
				this.testDataConfig = new TestDataConfig(getProjectConfigFile());
				getGlobalRoleFactory().configure();
			}
		}
		
        return this;
	}
	
	public void clearCache(){
		this.testDataConfig = null;
		this.tableConfigs.clear();
		this.globalRoleFactory = null;
		this.database = null;
		this.workspace = null;
	}
	
	private File getProjectConfigFile() {       
		return new File(getWorkspace(), Global.PROJECT_CONFIG);
    }
	    
		
    public static long getFileLastModifyTime(String fileName){
        return new File(fileName).lastModified();
    }
    
    public void initAll() throws BaseException {
        getGlobalRoleFactory().configure();      
		String[] tableNames = this.getTableConfigNames();
		for(String tableName : tableNames){
			this.getTableConfig(tableName);
		}
	}	

    FilenameFilter tableConfigFileFilter = new SuffixFileFilter(IPreference.TABLE_CONFIG_SUFFIX);
    
    public File[] getTableConfigFiles(){
        File file = new File(this.getWorkspace());
        return file.listFiles(tableConfigFileFilter);
    }
    
    public String[] getTableConfigNames(){
        File[] files = getTableConfigFiles();
        String[] tableConfigNames = new String[files.length];
        for(int i=0;i < files.length; i++){
            tableConfigNames[i] = files[i].getName().replace(IPreference.TABLE_CONFIG_SUFFIX, "");
        }
        return tableConfigNames;
    }
    
    public TableConfig getTableConfigFromList(String tableName){
        TableConfig tableConfig = null;
        for(TableConfig tempTableConfig : this.getTableConfigs()){
            if(tempTableConfig.getTableName().equalsIgnoreCase(tableName)){
                tableConfig = tempTableConfig;
                break;
            }
        }
        return tableConfig;
    }
    
    public TableConfig getTableConfig(String tableName) throws BaseException{
        TableConfig tableConfig = getTableConfigFromList(tableName);
        if(tableConfig == null){    
        	tableConfig = new TableConfig(tableName);
        	File configFile = tableConfig.getConfigFile();
            if(configFile.exists()){
                tableConfig.configure(configFile);
                this.getTableConfigs().add(tableConfig);
            }else{
                throw new RuntimeException("ConfigDataCatch.getTableConfig [" + configFile.getAbsolutePath() + "] file not exist");
            }
        }
        return tableConfig;
    }
    
    public void removeAndBankupTableConfig(String tableName){
        TableConfig tableConfig = getTableConfigFromList(tableName);
        if(tableConfig != null){
            this.getTableConfigs().remove(tableConfig);
        }else{
            tableConfig = new TableConfig(tableName);
        }
        tableConfig.deleteAndBankupFile();
    }
    
    public void removeAndBankupAllTableConfig(){        
        String[] tableConfigNames = this.getTableConfigNames();
        for(String tableConfigName : tableConfigNames){
            TableConfig tableConfig = new TableConfig(tableConfigName);
            tableConfig.deleteAndBankupFile();
        }
        this.getTableConfigs().clear();
    }
    
    public void addOrRefershTableConfig(TableConfig tableConfig){
        TableConfig tempTableConfig = this.getTableConfigFromList(
                            tableConfig.getTableName());
        if(tempTableConfig != null){
            int index = this.getTableConfigs().indexOf(tempTableConfig);
            this.getTableConfigs().remove(tempTableConfig);
            this.getTableConfigs().add(index, tableConfig);
        }else{
            this.getTableConfigs().add(tableConfig);
        }
    }
    
	public File getGlobalRoleFile() {
		return new File(getWorkspace(), GLOBAL_DATAGEN_CONFIG);
	}

	public File getTableConfigFile(String tableName){
		return getFileFromWorkspace(tableName + IPreference.TABLE_CONFIG_SUFFIX);
	}
	
    public File getFileFromWorkspace(String fileName) {
		return new File(this.getWorkspace(), fileName);
	}
	
    public static final String GLOBAL_DATAGEN_CONFIG = "globalRole.xml";

    
	private RoleFactory globalRoleFactory = null;
	
    private TestDataConfig testDataConfig = null;  //  @jve:decl-index=0:
    
    private Map<String, Long> lastModifyTimes = new HashMap<String, Long>();
    
	private List<TableConfig> tableConfigs = new ArrayList<TableConfig>();
	
	public String toString(){
		return "Database";
	}

	public Role getRole(String tableName, String roleName){
		if(tableName.equalsIgnoreCase(RM.R("label.roleFactory.global"))){
			return getGlobalRoleFactory().getRole(roleName);
		}else{
			return this.getTableConfigFromList(tableName)
			.getRoleFactory().getRole(roleName);
		}
	}
    
	public RoleFactory getGlobalRoleFactory() {
		if(globalRoleFactory == null){
			globalRoleFactory = new RoleFactory(RoleFactory.GLOBAL);
		}
		return globalRoleFactory;
	}

	public void setGlobalRoleFactory(RoleFactory globalRoleFactory) {
		this.globalRoleFactory = globalRoleFactory;
	}

	public String getWorkspace() {
		return workspace;
	}

    public String getGeneratedSQLDir(){
        return workspace + Global.SEP + GEN_SQL_DIR_NAME;
    }
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

    public List<TableConfig> getTableConfigs() {
        return tableConfigs;
    }

    public synchronized List<TableConfig> getSortedTableConfig(List<String> sortedTableNames){
        if(this.tableConfigs != null){
            List sortedTableConfig = new ArrayList<TableConfig>();
            for(String tableName : sortedTableNames){
                for(TableConfig tableConfig : tableConfigs){
                    if(tableConfig.getTableName().equalsIgnoreCase(tableName)){
                        sortedTableConfig.add(tableConfig);
                        break;
                    }
                }
            }
            this.tableConfigs = sortedTableConfig;
            return this.tableConfigs;
        }
        return null;
    }
    
    public void setTableConfigs(List<TableConfig> tableConfigs) {
        this.tableConfigs = tableConfigs;
    }

    public TestDataConfig getTestDataConfig() {
        return testDataConfig;
    }

    public void setTestDataConfig(TestDataConfig testDataConfig) {
        this.testDataConfig = testDataConfig;
    }

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
