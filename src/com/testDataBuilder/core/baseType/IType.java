package com.testDataBuilder.core.baseType;

import com.testDataBuilder.exception.BaseException;

/**
 * 类型接口，所有新添加的类型都必须实现该接口。
 * <p>Title：IType.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-12-19
 * @version 1.0
 */
public interface IType {

    public IType init(String path) throws BaseException;
    /**
     * 类型名。
     */
    public String getName();

    /**
     * 最小值（字符串是最小长度).
     */
    public double getMin();
    
    /**
     * 最大值。
     */
    public double getMax();
    
    /**
     * 字段串到本类型转换。
     * <p><code>valueOf</code></p>
     * @param str
     * @return
     * @author LiuXiaojie 2007-12-19
     */
    public Object valueOf(String value)throws BaseException;
    
    public double stringTodb(String value) throws BaseException;
    
    public String dbToString(double dbValue);
    
    public Object randomValue(double min, double max);
}
