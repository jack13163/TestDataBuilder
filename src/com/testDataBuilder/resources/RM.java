package com.testDataBuilder.resources;

import org.apache.log4j.Logger;

public class RM {

    static Logger logger = Logger.getLogger(RM.class);
    
	public static String R(String key){
		String value = key;
		try{
          value = ResourceManager.getResourceBundle().getString(key);
		}catch(java.util.MissingResourceException ex){
			logger.error("RM", ex);
		}
		return value;
    }
}
