package com.testDataBuilder.dbMetaInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.testDataBuilder.util.BeanXMLMapping;


public class Table {

    //"TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"。 
    
    public static final String TABLE = "TABLE";
    public static final String VIEW = "VIEW";
    public static final String SYSTEM_TABLE = "SYSTEM TABLE";
    public static final String GLOBAL_TEMPORARY = "GLOBAL TEMPORARY";
    public static final String LOCAL_TEMPORARY = "LOCAL TEMPORARY";
    public static final String ALIAS = "ALIAS";
    public static final String SYNONYM = "SYNONYM";
    
    private String tableName;
    
    private String type;
    
    private String remark;
    
    private List<Column> columns;

    private List<ForeignKey> foreignKeys;
    
    private PrimaryKey primaryKey = null;
    
    //外键引用了该表的表。
    private List<String> exportedTables = null;
    
    //该表外键引用了的表。
    private List<String> importedTables = null;
    
    /**
     * 添加一个引用表。
     * <p><code>addImportedTable</code></p>
     * @param tableName
     * @author LiuXiaojie 2007-12-5
     */
    public void addImportedTable(String tableName){
        if(!this.getImportedTables().contains(tableName)){
            this.getImportedTables().add(tableName);
        }
    }
    
    /**
     * 添加一个引用了该表的表。
     * <p><code>addExportedTable</code></p>
     * @param tableName
     * @author LiuXiaojie 2007-12-5
     */
    public void addExportedTable(String tableName){
        if(!this.getExportedTables().contains(tableName)){
            this.getExportedTables().add(tableName);
        }
    }
    
    public List<Column> getColumns() {
    	if(columns == null){
    		columns = new ArrayList<Column>(){
                public String toString(){
                    return "列";
                }      
            };
    	}
        return columns;
    }

    public Column getColumnByName(String columnName){
        if(columns == null){
            return null;
        }
        
        for(Column column : columns){
            if(column.getColumnName().equalsIgnoreCase(columnName)){
                return column;
            }
        }
        return null;
    }
    public List<String> getAllFields(){
    	List allFields = new ArrayList();
    	if(columns != null){
    		for(Column column : columns){
    			allFields.add(column.getColumnName());
    		}
    	}
    	
    	return allFields;
    }
    
    /**
     * 设置Columns,请尽量不要用该方法，而用this.getColumns().add
     * <p><code>setColumns</code></p>
     * @param columns
     * @author LiuXiaojie 2007-12-11
     */
    public void setColumns(List<Column> columns) {
        if(columns != null){
            for(Column column:columns){
                this.getColumns().add(column);
            }
        }else{
            this.columns.clear();
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.tableName;
    }

    public String toXMLString() throws IOException{
        return BeanXMLMapping.toXML(this);
    }
    
    public List<ForeignKey> getForeignKeys() {
        if(foreignKeys == null){
            foreignKeys = new ArrayList<ForeignKey>(5);
        }
        
        return foreignKeys;
    }

    public ForeignKey getForeignKeyByRefTable(String refTableName){
        List<ForeignKey> fks = this.getForeignKeys();
        ForeignKey tempFK = null;
        for(ForeignKey fk : fks){
             if(fk.getRefTable().equalsIgnoreCase(refTableName)){
                 tempFK = fk;
                 break;
             }
        }
        if(tempFK == null){
            tempFK = new ForeignKey();
            tempFK.setRefTable(refTableName);
            this.getForeignKeys().add(tempFK);
        }
        return tempFK;
    }

    public ForeignKey getForeignKeyByField(String field){
    	List<ForeignKey> fks = this.getForeignKeys();
        ForeignKey tempFK = null;
        for(ForeignKey fk : fks){
             if(fk.getFKFileds().contains(field)){
                 tempFK = fk;
                 break;
             }
        }
        
        return tempFK;
    }
    
    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getExportedTables() {
        if(exportedTables == null){
            exportedTables = new ArrayList<String>();
        }
        return exportedTables;
    }

    public void setExportedTables(List<String> exportedTables) {
        this.exportedTables = exportedTables;
    }

    public List<String> getImportedTables() {
        if(importedTables == null){
            importedTables = new ArrayList<String>();
        }
        return importedTables;
    }

    public void setImportedTables(List<String> importedTables) {
        this.importedTables = importedTables;
    }

    public void setForeignKeys(List<ForeignKey> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }
    
    public boolean hasAutoIncreaseColumn(){
        boolean hasAutoIncreaseColumn = false;
        if(this.columns != null){
            for(Column column : this.columns){
                if(column.isAutoIncrement()){
                    hasAutoIncreaseColumn = true;
                    break;
                }
            }
        }
        return hasAutoIncreaseColumn;
    }
    /**
     * 得到即是主键，又是外键的列。
     * <p><code>getIsPkAndIsFkColumn</code></p>
     * @return
     * @author LiuXiaojie 2008-1-17
     */
    public List<String> getIsPkAndIsFkColumn(){
        List<String> retColumns = new ArrayList<String>(5);
        if(this.foreignKeys != null && this.primaryKey != null){
            for(ForeignKey fk : this.foreignKeys){
                for(String fkField : fk.getFKFileds()){
                    if(primaryKey.getFields().contains(fkField)){
                        retColumns.add(fkField);
                    }
                }
            }
        }
        return retColumns;
    }
}
