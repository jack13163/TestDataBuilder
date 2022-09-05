package com.testDataBuilder.sqlTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * DB配置Template接口.
 * <p>Title：IDBTemplate.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-1-27
 * @version 1.0
 */
abstract public class IDBTemplate {
	
    public abstract String getTemplateName();
    
	public abstract String getName();
	
	public abstract String getDriverClass();
	
	public abstract String getURL();
	
	public abstract String getUserName();
	
	public abstract String getPwd();
	
	public String getIP(){
        if(this.getURL() == null){
            return null;
        }
        return subString(this.getURL(), IP_START, IP_END);       
	}

	public static String subString(String str, String begin, String end){
		if(str == null){
			return null;
		}
		
		int bIndex = 0;
		int eIndex = str.length();
		if(begin != null){
			Pattern pattern = Pattern.compile(begin);
			Matcher matcher = pattern.matcher(str);
			if(matcher.find()){
				bIndex = str.indexOf(matcher.group()) + matcher.group().length();
			}else{
				return null;
			}
		}
		
		if(end != null){
			Pattern pattern = Pattern.compile(end);
			Matcher matcher = pattern.matcher(str);
			if(matcher.find(bIndex)){
				eIndex = str.indexOf(matcher.group(), bIndex);
			}else{
				return null;
			}
		}
		return str.substring(bIndex, eIndex);
	}
	
	public String IP_START = "//";
	public String IP_END = ":";
	
}
