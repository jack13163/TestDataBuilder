package com.testDataBuilder.config.preference;

import java.sql.Date;

public class DefPreference implements IPreference {

    public Boolean getAutoConnWhenProgramStartup() {
        return false;
    }

    public Boolean getSaveDbMetaInfo() {
        return true;
    }
    
    public Boolean getReadDBInfoFromFileWhenConnError(){
        return true;
    }
    
    public Long getDefaultRowToGenerate() {
        return 108L;
    }

    public Boolean getCloseIDAutoInsert() {
        return false;
    }

    public Boolean getClearBefInsert() {
        return false;
    }

    public Date getMaxDate() {
        return java.sql.Date.valueOf("8888-8-8");
    }

    public Date getMinDate() {
        return java.sql.Date.valueOf("2008-8-8");
    }


    public Boolean getNotIncludeIDEntityClumn() {
        return true;
    }

    public Long getTextFieldMax() {
        return 100L;
    }

    public Long getTextFieldMin() {
        return 10L;
    }

    public String getDataTypePluginDir() {
        return "res/datatypes";
    }

    public String getDateFormat() {
        return "yyyy-MM-dd HH-mm-ss";
    }

    public Boolean getGenerateSQLFile() {        
        return true;
    }
    
    public String getWorkspace(){
        return "projects";
    }

	public int getQueryConsoleMaxSize() {
		return 1000;
	}
    
}
