package com.testDataBuilder.util;

import com.wutka.jox.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.log4j.Logger;

public class BeanXMLMapping {

    static Logger logger = Logger.getLogger(BeanXMLMapping.class);
    
	/**
	 * Retrieves a bean object for the received XML and matching bean class
	 * @throws IOException 
	 */
	public static Object fromXML(String xml, Class className) throws IOException {
		ByteArrayInputStream xmlData = new ByteArrayInputStream(xml.getBytes("utf-8"));
		JOXBeanInputStream joxIn = new JOXBeanInputStream(xmlData);
		try {
			return (Object) joxIn.readObject(className);
		}finally {
			try {
				xmlData.close();
				joxIn.close();
			} catch (Exception ex) {
                logger.error("BeanXMLMapping",ex);
			}
		}
	}

	/**
	 * Returns an XML document.nbspString for the received bean
	 * @throws IOException 
	 */
	public static String toXML(Object bean) throws IOException {
		ByteArrayOutputStream xmlData = new ByteArrayOutputStream();
		JOXBeanOutputStream joxOut = new JOXBeanOutputStream(xmlData,"utf-8");
		try {
			joxOut.writeObject(beanName(bean), bean);
			return xmlData.toString("utf-8");
		} finally {
			try {
				xmlData.close();
				joxOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Find out the bean class name
	 */
	private static String beanName(Object bean) {
		String fullClassName = bean.getClass().getName();
		String classNameTemp = fullClassName.substring(fullClassName
				.lastIndexOf(".") + 1, fullClassName.length());
		return classNameTemp.substring(0, 1) + classNameTemp.substring(1);
	}
}
