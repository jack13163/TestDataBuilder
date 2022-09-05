package com.testDataBuilder.sqlTemplate;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.Logger;

import com.testDataBuilder.util.RadixConverter;

public class TemplateFactory {

    static Logger logger = Logger.getLogger(TemplateFactory.class);
    
	public static final String TEMPLATE_DIR  = "res/sqlTemplate";

	private TemplateFactory(){
		
	}
	
    
	private static TemplateFactory instance = null;
	
	public static TemplateFactory getInstance(){
		if(instance == null){
			instance = new TemplateFactory();
            instance.init();
		}
		return instance;
	}
	
	private Map map = new HashMap();
	
    public TemplateFactory init(){
        File dir = new File(TEMPLATE_DIR);
        if(!dir.exists()){
            logger.info("dir [" + dir.getAbsolutePath() + "] not exist");
            return this;
        }
        
        if(dir.isDirectory()){
            FilenameFilter filter = new SuffixFileFilter(".xml"); 
            String[] tempcnfigFiles = dir.list(filter);
            for(String tempConfigFile : tempcnfigFiles){
                try {
                    XmlConfigTemplate xmlConfigTemplate = new XmlConfigTemplate()
                            .loadFromXmlFile(new File(dir, tempConfigFile));
                    this.map.put(xmlConfigTemplate.getName(), xmlConfigTemplate);
                } catch (Exception e) {
                    logger.error("TemplateFactory, 加载[" + tempConfigFile + "]中的SQLTemplate时出错，", e);
                } 
            }
        }
        return this;
    }
	
	public Map getTemplates(){
		return map;
	}
	
	public List<String> getAllTypes(){
		Set keySet = this.getTemplates().keySet();
		return RadixConverter.toList(keySet);		
	}
	
	public void addTemplate(IDBTemplate dbTemplate){
		this.map.put(dbTemplate.getName(), dbTemplate);
	}
	
	public IDBTemplate removeTemplate(String templateName){
		return (IDBTemplate) map.remove(templateName);
	}
	
	public IDBTemplate getTemplate(String templateName){
		return (IDBTemplate) map.get(templateName);
	}
	
//	public IDBTemplate getTemplateByDriverClass(String driverClass){
//		 Collection coll = this.getTemplates().values();
//		 IDBTemplate retTemplate = null;
//		if(coll != null){
//			Iterator it = coll.iterator();
//			 while(it.hasNext()){
//				 IDBTemplate dbTemplate = (IDBTemplate)it.next();
//				 if(dbTemplate.getDriverClass().equalsIgnoreCase(driverClass)){
//					 retTemplate = dbTemplate;
//					 break;
//				 }
//			 }
//		 }
//		
//		return retTemplate;
//	}
//	
}
