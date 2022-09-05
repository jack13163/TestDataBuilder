package com.testDataBuilder.core;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class SQLGenerator {

	static Logger logger = Logger.getLogger(SQLGenerator.class);
	
    private String format ;
    
    private List<Object> args = null;
    
    
    public SQLGenerator(String format){
        this.format = format;
    }


    public List<Object> getArgs() {
        if(args == null){
            args = new LinkedList<Object>();
        }
        return args;
    }

    public void addArg(Object arg){
        if(mustAddQuote(arg)){
            this.getArgs().add("'" + arg + "'");
        }else{
            this.getArgs().add(arg);
        }
    }
    
    public boolean mustAddQuote(Object value){
        return (value instanceof String || value instanceof java.util.Date);
    }
    
    public String getSQL(){
        if(format != null && args != null){
        	try{
            return String.format(format, args.toArray());
        	}catch(java.util.MissingFormatArgumentException ex){
        		logger.error("错误的SQL语句:" + format + " args.size:" + args.size());
        		return null;
        	}
        }else{
            return null;
        }
    }
    
}
