package com.testDataBuilder.exception;

public class AttributeNotFoundException extends BaseException {

    public AttributeNotFoundException(String attName){
        super("attribute [" + attName + "]not found , it's required!");
    }
    
}
