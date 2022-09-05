package com.testDataBuilder.dbMetaInfo;

import java.io.IOException;
import java.sql.Types;


import com.testDataBuilder.util.BeanXMLMapping;
import com.testDataBuilder.util.JDBCUtil;

public class Column {

    public static void main(String[] args) {
       
    }
    private String columnName;
    
    //int => 来自 java.sql.Types 的 SQL 类型 
    private int type;
    
    //数据类型名.
    private String typeName;
    
    //int => 列的大小。对于 char 或 date 类型，列的大小是最大字符数，对于 numeric 和 decimal 类型，列的大小就是精度。
    private int size;
       
    //int => 小数部分的位数
    private int decimalDigits;
    
    //int => 是否允许使用 NULL。
    private boolean nullable = true;
    
    //是否唯一
    private boolean unique = false;
    
    private boolean isAutoIncrement = false;
    
    private String remarks;


    public String getColumnName() {
        return columnName;
    }

    public String getLongColumnName(){
    	StringBuffer longName = new StringBuffer(columnName + " (");
    	longName.append(getSQLType(getType()));
    	switch(this.type){
    	case Types.VARCHAR:
    	case Types.CHAR:
    	case Types.BLOB:
    		longName.append("(" + this.getSize() + ")");
    		break;
    	case Types.DECIMAL:
    	case Types.NUMERIC:
    		longName.append("(" +size + "," + decimalDigits + ")");
    	case Types.FLOAT:
    	case Types.REAL:
    		longName.append("(" + this.getSize() + ")");
    	default: //Types.INTEGER,Types.BIT,Types.DATE,Types.TIME;
    			
    	}
    	longName.append(", ");
    	longName.append((this.getNullable()? "Null":"Not Null"));
    	longName.append(")");
    	return longName.toString();
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean getNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.getLongColumnName();
    }
    
    public String toXMLString() throws IOException{
        return BeanXMLMapping.toXML(this);
    }
    
    public static String getSQLType(int type){
    	return JDBCUtil.getSQLStrType(type);
    }

    public Class JavaType(){
        return JDBCUtil.getJavaType(this.type, this.size, decimalDigits);
    }
    
    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }
}
