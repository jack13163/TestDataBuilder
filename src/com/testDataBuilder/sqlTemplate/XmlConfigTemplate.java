package com.testDataBuilder.sqlTemplate;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.xml.sax.SAXException;
import com.testDataBuilder.config.DatabaseConfig;
import com.testDataBuilder.exception.TagNotFoundException;
import com.testDataBuilder.util.XmlFileUtil;

public class XmlConfigTemplate extends DatabaseConfig{

    public XmlConfigTemplate loadFromXmlFile(File configFile) throws SAXException, IOException, DocumentException, TagNotFoundException{
        XmlFileUtil xmlFile = new XmlFileUtil(configFile);
        Document doc = xmlFile.getDoc(false, null);
        this.configure(doc.getRootElement());
        return this;
    }

    public void storeToXmlFile(File configFile) throws IOException{
        Element tempEle = this.toElement(null);
        String xml = XmlFileUtil.formatXml(tempEle, "utf-8");
        FileUtils.writeStringToFile(configFile, xml, "utf-8");
    }
    
    public XmlConfigTemplate() {
        
    }
    
    
}
