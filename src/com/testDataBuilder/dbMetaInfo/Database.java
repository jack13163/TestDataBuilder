package com.testDataBuilder.dbMetaInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.testDataBuilder.util.BeanXMLMapping;

/**
 * 用于表示数据库信息的类.
 * <p>Title：Database.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-4-6
 * @version 1.0
 */
public class Database {

    
    private String dbName;
    
    private List<Table> tables;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public Table getTable(String tableName){
    	if(tables != null){
    		for(Table table : tables){
        		if(table.getTableName().equalsIgnoreCase(tableName)){
        			return table;
        		}
        	}
    	}
    	return null;
    }
    
    public List<String> getTableNames(){
        List<String> tableNames = new ArrayList<String>();
        for(Table table : this.tables){
            tableNames.add(table.getTableName());
        }
        return tableNames;
    }
    
    private static int count = 0;
    
    /**
     * 对数据表名进行排序。
     * <p><code>getSortedTableNames</code></p>
     * @return
     * @author LiuXiaojie 2007-12-24
     */
    public List<String> getSortedTableNames(){
        count = 0;
        List<String> tableNames = getTableNames();
        
        LinkedList<String> sortedTableNames = new LinkedList<String>();
        
        while(tableNames.size() > 0){
            count ++;
            if(count > 1000){
                count = 0;
                throw new RuntimeException("程序好像出问题了。。似乎进入了一个死循环。。。");
            }
            
            String tableName = tableNames.remove(0);            
            Table table = this.getTable(tableName);
            if(table.getImportedTables() != null){
                LinkedList<String> refTables = new LinkedList<String>();
                refTables(tableNames,null, tableName, refTables);
                sortedTableNames.addAll(refTables);
            }else{
                sortedTableNames.add(tableName);
            }
        }
        count = 0;
        return sortedTableNames;
    }
    /**
     * 找到在tableNames中，tableName所有依赖及间接依赖的Table.
     * 找到的table并从tableNames中删除。
     * <p><code>refTables</code></p>
     * @param tableNames
     * @param tableName
     * @param refTables 返回的值。
     * @return 无返回值.
     * @author LiuXiaojie 2007-12-5
     */
    public void refTables(List<String> tableNames,String parentTable, String tableName, LinkedList<String> refTables){
        count ++;
        if(count > 1000){
            throw new RuntimeException("程序好像出问题了。。似乎进入了一个死循环。。。");
        }
        if(parentTable != null){
            int pIndex = refTables.indexOf(parentTable);
            refTables.add(pIndex, tableName);
        }else{
            refTables.addFirst(tableName);
        }
        Table table = this.getTable(tableName);
        if(table.getImportedTables().size() > 0){
            for(String importedTable : table.getImportedTables()){
                if(tableNames.contains(importedTable)){
                    tableNames.remove(importedTable);
                    refTables(tableNames,tableName,importedTable, refTables);
                }
            }
        }
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.dbName;
    }
    
    public String toXmlString() throws IOException{
        return BeanXMLMapping.toXML(this);
    }
    
    public static Database fromXml(String xml) throws IOException{
        return (Database) BeanXMLMapping.fromXML(xml, Database.class);
    }

    /**
     * 选中的Table.
     */
    Table selTable = null;

	public Table getSelTable() {
		return selTable;
	}

	public void setSelTable(Table selTable) {
		this.selTable = selTable;
	}
    
}
