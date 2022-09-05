package com.testDataBuilder.dbMetaInfo;

import java.util.ArrayList;
import java.util.List;

public class PrimaryKey {

    public PrimaryKey(String pkName){
        this.pkName = pkName;
    }
    
    public PrimaryKey(){
    }
    
    private String pkName;
    
    private List<String> fields = new ArrayList<String>();

    public void addField(String field){
        if(!this.getFields().contains(field)){
            this.getFields().add(field);
        }
    }
    
    public boolean contain(String field){
        return this.getFields().contains(field);
    }
    
    public boolean remoreField(String field){
        return this.getFields().remove(field);
    }
    
    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }
    
    public String toString(){
    	if(this.pkName != null){
    		return "PK [" + pkName + "]";
    	}else{
    		return "PK [primaryKey]";
    	}
    }
}
