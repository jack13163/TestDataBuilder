package com.testDataBuilder.exception;

public class TagNotFoundException extends BaseException {

	 public TagNotFoundException(String tagName){
        super("tag [" + tagName + "]not found , it's required!");
    }
	    
	 public TagNotFoundException(String typeName,String tagName){
        super("dataGernator name=" + typeName + " tag [" + tagName + "]not found , it's required!");
    }
	    
	    
}
