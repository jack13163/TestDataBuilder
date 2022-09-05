package com.testDataBuilder.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.testDataBuilder.config.WorkspaceDataCache;
import com.testDataBuilder.dbMetaInfo.Table;

public class JDBCUtil {
    
    private static final Logger logger = Logger.getLogger(JDBCUtil.class);
    
    private static Map<Integer,String> map = null;
    
    public static String getSQLStrType(int type){
        if(map == null){
            map = new HashMap<Integer,String>();
            map.put(Types.BIT, "bit");
            map.put(Types.TINYINT, "tinyint");
            map.put(Types.SMALLINT, "smallint");
            map.put(Types.INTEGER, "int");
            map.put(Types.BIGINT, "bigint");
            map.put(Types.FLOAT, "float");
            map.put(Types.REAL, "real");
            map.put(Types.DOUBLE, "double");
            map.put(Types.NUMERIC, "numeric");
            map.put(Types.DECIMAL, "decimal");            
            map.put(Types.CHAR, "char");
            map.put(Types.VARCHAR, "varchar");
            map.put(Types.LONGVARCHAR, "longVarcahr");            
            map.put(Types.DATE, "data");
            map.put(Types.TIME, "time");
            map.put(Types.TIMESTAMP, "timestamp");
            map.put(Types.BINARY, "binary");
            
            map.put(Types.BLOB, "blob");
            map.put(Types.CLOB, "clob");
            
        }
        String strType =  (String) map.get(type);
        if(strType == null){
            strType = "unknow[" + type + "]";
            logger.error("un mapped type[" + type);
        }
        return strType;
    }
    
    public static Class getJavaType(int sqlType, int columnSize, int decimalDigits) {
        Class rv=String.class;
        if (sqlType == Types.CHAR || sqlType == Types.VARCHAR) {
            rv = String.class;
        }
        else if (sqlType == Types.FLOAT || sqlType == Types.REAL) {
            rv = Float.class;
        }
        else if (sqlType == Types.INTEGER) {
            rv = Integer.class;
        }
        else if (sqlType == Types.DOUBLE) {
            rv = Double.class;
        }
        else if (sqlType == Types.DATE) {
            rv = java.sql.Date.class;
        }
        else if (sqlType == Types.TIMESTAMP) {
            rv = java.sql.Date.class;
        }
        else if (sqlType == Types.TIME) {
            rv = java.sql.Date.class;
        }
        // commented to support JDK version < 1.4
        /*      else if (sqlType == Types.BOOLEAN) {
            rv = Boolean.class;
        } */
        else if (sqlType == Types.SMALLINT) {
            rv = Short.class;
        }
        else if (sqlType == Types.BIT) {
            rv = Boolean.class;
        }
        else if (sqlType == Types.BIGINT) {
            rv = Long.class;
        }
        else if (sqlType == Types.NUMERIC || sqlType == Types.DECIMAL) {
            if (decimalDigits == 0) {
                if (columnSize == 1) {
                    rv = Byte.class;
                }
                else if (columnSize < 5) {
                    rv = Short.class;
                }
                else if (columnSize < 10) {
                    rv = Integer.class;
                }
                else {
                    rv = Long.class;
                }
            }
            else {
                if (columnSize < 9) {
                    rv = Float.class;
                }
                else {
                    rv = Double.class;
                }
            }
        }
        return rv;
    }

    public static List<String> getAllFields(String sql){
        String strFields = StringUtil.subString(sql, "select", "from");
        List<String> allFields = new ArrayList<String>();
        if(StringUtils.isEmpty(strFields)){
            logger.warn(String.format("getAllFields.sql[%s] 为空.", sql));
        }else if(strFields != null && strFields.contains("*")){
            String tableName = StringUtil.subString(sql, "from\\s+", "\\s+");
            Table table = WorkspaceDataCache.getInstance().getDatabase().getTable(tableName);
            if(table != null){
                allFields.addAll(table.getAllFields());
            }else{
            	logger.error("SQL语句[" + sql + "]可能不合法，未找到表[" + tableName + "]");
            }
        }else{
            String[] arrFields = strFields.trim().split(",");
            for(String field : arrFields){
                field = field.trim();
                int dotIndex = field.lastIndexOf('.');
                int spaceIndex = field.lastIndexOf(" ");
                if(spaceIndex == -1){
                    spaceIndex = dotIndex;
                }
                field = field.substring(spaceIndex + 1);
                field = field.replace("'", "");
                allFields.add(field);
            }
        }
        return allFields;
    }
    
    public static void main(String[] args) {
        String sql = "select _Application.id  , _Department.id as departmentId from Application as _Application,Department as _Department";
        List<String> fields = getAllFields(sql);
        for(String s: fields){
            System.out.println(s);
        }
    }
    
}
