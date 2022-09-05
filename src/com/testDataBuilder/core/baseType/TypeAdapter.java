package com.testDataBuilder.core.baseType;

import com.testDataBuilder.exception.BaseException;

public class TypeAdapter implements IType {

    public String dbToString(double dbValue) {
        return null;
    }

    public double getMax() {
        return -1;
    }

    public double getMin() {
        return -1;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public Object randomValue(double min, double max) {
        return "";
    }

    public double stringTodb(String value) throws BaseException {
        return 0;
    }

    public Object valueOf(String value) throws BaseException{
        return value;
    }
    
    public IType init(String basePath)throws BaseException{
        return this;
    }
    

}
