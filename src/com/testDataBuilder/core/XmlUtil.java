package com.testDataBuilder.core;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import com.testDataBuilder.exception.BaseException;


public class XmlUtil {

    
    /**
     * 从parent节点获取一个子元素.
     * <p><code>getChildElement</code></p>
     * @param parent
     * @param tagName
     * @param isRequired 是否是必须的.
     * @return 返回获取到的子节点.
     * @author LiuXiaojie 2008-3-13
     * @throws InvalidProtocolException 如果isRequired并且parent不存在名为tagName的节点,则抛出此Exception.
     */
    public static Element getChildElement(Element parent, String tagName, boolean isRequired)throws BaseException{
        Element childEle = parent.element(tagName);
        if(isRequired && childEle == null){
            throw new BaseException(
                            String.format("元素[%s]缺少子元素[%s]",
                                            parent.getName(),tagName));
        }
        return childEle;
    }
    
    /**
     * 获取一个元素的属性值.
     * <p><code>getStringAttributeValue</code></p>
     * @param ele 要获取值的元素.
     * @param attName 属性名称.
     * @param isRequired 属性是否是必须的.
     * @return 返回获取到的值.
     * @throws InvalidProtocolException,如果isRequired并且ele不存在attName属性则抛出该Exception.
     * @author LiuXiaojie 2008-3-13
     */
    public static String getStringAttributeValue(Element ele, String attName,boolean isRequired)throws BaseException{
        String value = ele.attributeValue(attName);
        if(isRequired && StringUtils.isEmpty(value)){
            throw new BaseException(
                            String.format("元素[%s]缺少属性[%s]",ele.getName(), attName));
        }
        return value;
    }
    
    /**
     * 获取一个元素的内容。
     * @param ele
     * @param isRequired
     * @return
     * @throws BaseException
     */
    public static String getContentValue(Element ele, boolean isRequired) throws BaseException{
    	String content = ele.getText();
    	if(isRequired && StringUtils.isEmpty(content)){
    		throw new BaseException(String.format("元素[%s]内容不能为空.", ele.getName()));
    	}
    	return content;
    }
    
    /**
     * 获取元素的Long型属性值.
     * <p><code>getLongAttributeValue</code></p>
     * @param ele
     * @param attName
     * @param isRequired
     * @return
     * @throws InvalidProtocolException
     * @author LiuXiaojie 2008-3-19
     */
    public static Long getLongAttributeValue(Element ele, String attName , boolean isRequired)throws BaseException{
        String strValue = getStringAttributeValue(ele, attName, isRequired);
        if(strValue == null){
            return null;
        }
        Long value = null;
        try{
            value = Long.valueOf(strValue);
        }catch(NumberFormatException ex){
            String errorMsg = "值[%s]是不合法的,元素[%s]的[%s]属性值必须是整型的.";
            throw new BaseException(String.format(errorMsg,strValue, ele.getName(), attName), ex);
        }
        return value;
    }
    
    /**
     * 获取Int值.
     * <p><code>getIntAttributeValue</code></p>
     * @param ele
     * @param attName
     * @param isRequired
     * @return
     * @throws InvalidProtocolException
     * @author LiuXiaojie 2008-3-25
     */
    public static Integer getIntAttributeValue(Element ele, String attName , boolean isRequired)throws BaseException{
        Long value= getLongAttributeValue(ele, attName, isRequired);
        if(value == null){
            return null;
        }
        return value.intValue();
    }
    
    /**
     * 取得double值。
     * @param ele
     * @param attName
     * @param isRequired
     * @return
     * @throws BaseException
     */
    public static Double getDoubleAttributeValue(Element ele, String attName , boolean isRequired)throws BaseException{
        String strValue = getStringAttributeValue(ele, attName, isRequired);
        if(strValue == null){
            return null;
        }
        Double value = null;
        try{
            value = Double.valueOf(strValue);
        }catch(NumberFormatException ex){
            String errorMsg = "值[%s]是不合法的,元素[%s]的[%s]属性值必须是Double的.";
            throw new BaseException(String.format(errorMsg,strValue, ele.getName(), attName), ex);
        }
        return value;
    }
    
    public static Float getFloatAttributeValue(Element ele, String attName , boolean isRequired)throws BaseException{
    	Double value= getDoubleAttributeValue(ele, attName, isRequired);
        if(value == null){
            return null;
        }
        return value.floatValue();
    }
    
    /**
     * 获取boolean值.
     * <p><code>getBooleanAttributeValue</code></p>
     * @param ele
     * @param attName
     * @param isRequired
     * @return
     * @throws InvalidProtocolException
     * @author LiuXiaojie 2008-3-25
     */
    public static Boolean getBooleanAttributeValue(Element ele, String attName , boolean isRequired)throws BaseException{
        String strValue = getStringAttributeValue(ele, attName, isRequired);
        if(strValue == null){
            return null;
        }
        
        return Boolean.valueOf(strValue);
    }
    
    public static Date getDateAttributeValue(Element ele, String attName, boolean isRequired)throws BaseException{
        String strValue = getStringAttributeValue(ele, attName, isRequired);
        if(strValue == null){
            return null;
        }
        
        Date date = null;
        try{
            date = Timestamp.valueOf(strValue);
        }catch(IllegalArgumentException ex){
            String errorMsg = "值[%s]是不合法的,元素[%s]的[%s]属性值必须是日期型(yyyy-mm-dd hh:mm:ss.fffffffff)的." +
                    "如2008年8月8日20点8分8秒应为:'2008-08-08 20:8:8',年月日中的0不能省略";
            throw new BaseException(String.format(errorMsg,strValue, ele.getName(), attName), ex);
        }
        return date;
    }
    
}
