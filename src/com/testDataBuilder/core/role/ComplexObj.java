package com.testDataBuilder.core.role;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.testDataBuilder.util.Global;
import com.testDataBuilder.util.RadixConverter;

/**
 * xml对象.
 * <p>Title：XmlObj.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-6-16
 * @version 1.0
 */
public class ComplexObj {

	private Map<String,Object> values = new HashMap<String,Object>(8,0.75f);
	
	public void addValue(String key, Object value){
		values.put(key, value);
	}
	
	public Object getValue(String key){
		return values.get(key);
	}
	
	public Object removeValue(String key){
		return values.remove(key);
	}
	
	public List<String> getAllFields(){
		return RadixConverter.toList(this.values.keySet());
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("complexObj{" + Global.SEP );
		if(this.values != null){
		  for(Iterator<String> it = this.values.keySet().iterator();it.hasNext();){
			  String key = it.next();
			  Object value = this.values.get(key);
			  sb.append(key ).append("=").append(value).append(Global.SEP);
		  }
		}
		sb.append("}");
		return sb.toString();
	}
	
}
