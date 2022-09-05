package com.testDataBuilder.dbMetaInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.testDataBuilder.util.BeanXMLMapping;
import com.testDataBuilder.util.RadixConverter;


public class ForeignKey {

    private String fkName;
 

    private String refTable;
    
    /**
     * key里面保存的是本表的字段，value里面保存的是参照的表的名字。
     */
    private Map<String,String> refFields = new HashMap<String,String>();

    public void addField(String field, String refField){
        this.getRefFields().put(field, refField);
    }
    
    public String getRefFiled(String field){
        return this.getRefFields().get(field);
    }
    
    public List<String> getFKFileds(){
        return RadixConverter.toList(this.getRefFields().keySet());
    }
    
    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName;
    }


    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }
    public Map<String,String> getRefFields() {
        return refFields;
    }
    public void setRefFields(Map<String,String> refFields) {
        this.refFields = refFields;
    }
    
    public String toString(){
    	if(this.fkName != null){
    		return "FK [" + fkName + "] => " + this.getRefTable();
    	}else{
    		return "FK [foreignKey] => " + this.getRefTable();
    	}
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ForeignKey other = (ForeignKey) obj;
        if (fkName == null) {
            if (other.fkName != null) return false;
        } else if (!fkName.equals(other.fkName)) return false;
        if (refTable == null) {
            if (other.refTable != null) return false;
        } else if (!refTable.equals(other.refTable)) return false;
        return true;
    }
    
    public static void main(String[] args) throws IOException {
        Map<String,String> refFields = new HashMap<String,String>();
        refFields.put("中国", "日本");
        String str = BeanXMLMapping.toXML(refFields);
        System.out.println(str);
    }
}

