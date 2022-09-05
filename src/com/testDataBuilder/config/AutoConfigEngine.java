package com.testDataBuilder.config;

import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import com.testDataBuilder.core.baseType.JavaTypes;
import com.testDataBuilder.core.role.DataCommon;
import com.testDataBuilder.core.role.RoleFactory;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.dbMetaInfo.Column;
import com.testDataBuilder.dbMetaInfo.ForeignKey;
import com.testDataBuilder.dbMetaInfo.Table;
import com.testDataBuilder.util.Global;

public class AutoConfigEngine {
	
    
    private Global G = com.testDataBuilder.util.Global.getInstance();
    
	protected RoleFactory reverseRoles(Table table, List<String> tempColumnNames){
		List<Column> columns = table.getColumns();
        RoleFactory roleFactory = new RoleFactory(table.getTableName());
        
		for(Column column : columns){
            //如果是自动增长列，并且配置的是不包含自动增长列。
            if(column.isAutoIncrement() 
                  && G.P.getNotIncludeIDEntityClumn()){
                continue;                                                                      
            }                                                                                                                                                                                                                                          
            
            //如果是主键加外键列。
            if(tempColumnNames.size() > 0 
                            && tempColumnNames.contains(column.getColumnName())){
                //tempColumns.add(column);
                continue;
            }
            
			String columnName = column.getColumnName();
            //如果是外键。
			ForeignKey foreignKey = table.getForeignKeyByField(columnName);
			if(foreignKey != null){
				String refTableName = foreignKey.getRefTable();
				String genName = refTableName + "4" + table.getTableName();
				Role role = roleFactory.getRole(genName);
				if(role == null){
					role = constructRole(genName,foreignKey, column.getNullable());
                    roleFactory.addRole(role);
				}
			}else{
//                String genName = table.getTableName() + "-" + column.getColumnName() ;
                String genName = column.getColumnName() ;
                
				int sqlType = column.getType();
				Role role = roleFactory.getRole(genName);
				if(role == null){
					role = new Role(genName);
					Class javaType = column.JavaType();
                    role.setType(javaType);
                    role.setNullable(column.getNullable());
                    if(javaType.equals(JavaTypes.STRING)){
                        //System.out.println(columnName + ":" +sqlType);
                        switch (sqlType) {
                        case Types.BLOB:
                        case Types.CLOB:
                        case Types.BINARY:                                
                        case Types.LONGVARBINARY:
                        case Types.LONGVARCHAR:
                            role.setMin(G.P.getTextFieldMin());
                            role.setMax(G.P.getTextFieldMax());
                            break;
                        case Types.CHAR:
                            role.setMin(column.getSize());
                            role.setMax(column.getSize());
                            role.setMethod(Role.METHOD_RANDOM);
                            break;
                        case Types.VARCHAR:
                            role.setMin(1);
                            role.setMax(column.getSize());
                            role.setMethod(Role.METHOD_RANDOM);
                            break;
                        default:
                            role.setMin(G.P.getTextFieldMin());
                            role.setMax(G.P.getTextFieldMax());
                        }                                            
					}else if(javaType.equals(JavaTypes.DATE)){
                        role.setMinDate(G.P.getMinDate());
                        role.setMaxDate(G.P.getMaxDate());
                        role.setStep(DataCommon.HOUR);
                    }else{
                        if(javaType.equals(JavaTypes.DOUBLE)){
                            role.setSize(column.getSize());
                            role.setDecimalDigits(column.getDecimalDigits());
                        }
                        
		                role.setMin(0);
                        double max = JavaTypes.getInstance().getMax(javaType,column.getSize(), column.getDecimalDigits());
                        if(max > (Double.MAX_VALUE / 2)){
                            max = Double.MAX_VALUE / 2;
                        }
	                	role.setMax(max);
					}

	                role.setStep(1);
					
                    roleFactory.addRole(role);
                }
			}
		}	
        
        if(tempColumnNames.size() > 0){
            Role role = constructRole(tempColumnNames, table);
            roleFactory.addRole(role);
        }
        
        return roleFactory;
	}

    private Role constructRole(List<String> tempColumnNames, Table table) {
        List<ForeignKey> configedColumns = new LinkedList<ForeignKey>(); 
        StringBuffer select = new StringBuffer("select ");
        StringBuffer from = new StringBuffer(" from ");
        
        boolean selIsFirst = true;
        boolean nullable = true;
        
        for(int i=0;i < tempColumnNames.size(); i++){
            String columnName = tempColumnNames.get(i);
            ForeignKey foreignKey = table.getForeignKeyByField(columnName);
            if(!configedColumns.contains(foreignKey)){
                configedColumns.add(foreignKey);
                String refTable = foreignKey.getRefTable();
                String alias = "_" + refTable;
                if(i==0){
                    from.append(refTable + " as " + alias);
                }else{
                    from.append("," + refTable + " as " + alias);
                }
                for(String fkField : foreignKey.getFKFileds()){
                    String refField = foreignKey.getRefFields().get(fkField);
                    if(selIsFirst){
                        selIsFirst = false;
                        nullable = table.getColumnByName(columnName).getNullable();
                        
                        select.append(alias + "." + refField + " as " + fkField);
                    }else{
                        select.append(", " + alias + "." + refField+ " as " + fkField);
                    }
                }
            }
        }
//        String roleName = table.getTableName() + "-id";
        String roleName = "id";
        Role role = new Role(roleName);
        role.setSQL(select.toString() + from.toString());
        role.setType(JavaTypes.OBJECT);
        role.setMethod(Role.METHOD_SQL_QUERY);
        role.setStep(1);
        role.setNullable(nullable);
        return role;
    }
	
	private Role constructRole(String genName, ForeignKey foreignKey, boolean nullable){
		Role role = new Role(genName);
        role.setNullable(nullable);
		StringBuilder sql = new StringBuilder("select ");
		List<String> fkFields = foreignKey.getFKFileds();
		for(int i=0;i < fkFields.size(); i++){
			if(i == 0){
				sql.append(foreignKey.getRefFiled(fkFields.get(i)));
			}else{
                sql.append(", "  + foreignKey.getRefFiled(fkFields.get(i)));
			}
		}
        
		sql.append(" from ");
		sql.append(foreignKey.getRefTable());
		
		role.setSQL(sql.toString());
        role.setType(JavaTypes.OBJECT);
        role.setMethod(Role.METHOD_SQL_QUERY);
		role.setStep(1);
		return role;
	}
	
	public TableConfig reverseTable(Table table){        
        TableConfig tableConfig = new TableConfig(table.getTableName());
        tableConfig.setCount(G.P.getDefaultRowToGenerate());
        List<String> tempColumnNames = table.getIsPkAndIsFkColumn();
        
        RoleFactory roleFactory = this.reverseRoles(table, tempColumnNames);        
        tableConfig.setRoleFactory(roleFactory);
        
        if(table.hasAutoIncreaseColumn() && G.P.getCloseIDAutoInsert()){
            tableConfig.setCloseIdAutoInsert(true);
        }
        
        if(G.P.getClearBefInsert()){
            StringBuffer init = new StringBuffer();
            
            if(table.getExportedTables() != null){
                for(String exprotedTable : table.getExportedTables()){
                    init.append("delete from " + exprotedTable + ";" + Global.LINE_SEP);
                }
            }
            init.append("delete from " + table.getTableName());
            tableConfig.setInit(init.toString());
        }
        
		List<Column> columns = table.getColumns();
        for(Column column : columns){
            //如果是自动增长列，并且配置的是不包含自动增长列。
            if(column.isAutoIncrement() 
                            && G.P.getNotIncludeIDEntityClumn()){
                continue;
            }
            
            ColumnConfig columnConfig = reverseColumn(table, column, tempColumnNames);
            tableConfig.addColumnConfig(columnConfig);
        }
		return tableConfig;
	}
	
	private ColumnConfig reverseColumn(Table table, Column column, List<String> tempColumnNames){
		ColumnConfig columnConfig = new ColumnConfig();
		columnConfig.setName(column.getColumnName());

		if(table.getPrimaryKey() != null){
			columnConfig.setPK(table.getPrimaryKey().contain(column.getColumnName()));
		}
		ForeignKey foreignKey = table.getForeignKeyByField(column.getColumnName());
        //是外键
        if(tempColumnNames.contains(column.getColumnName())){
//            String genName = table.getTableName() + "-id." + column.getColumnName(); 
            String genName = "id." + column.getColumnName(); 
            columnConfig.setRef(genName);
        }else if(foreignKey != null){
            String refTableName = foreignKey.getRefTable();
            String genName = refTableName + "4" + table.getTableName() + "." 
                    + foreignKey.getRefFiled(column.getColumnName());
            columnConfig.setRef(genName);
        } else {
            String genName = column.getColumnName() ;
            columnConfig.setRef(genName);
        }
		
		return columnConfig;
	}
}
