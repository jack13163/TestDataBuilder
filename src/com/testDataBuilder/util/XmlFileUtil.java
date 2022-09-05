package com.testDataBuilder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.FlyweightCDATA;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

/**
 * XML文件操作工具类.
 * <p>Title：XmlFileUtil.java</p>
 * <p>Description：QQClient</p>
 * <p>Copyright：Copyright (c)2006 BaoSight-HY,Inc</p>
 * <p>Company：BaoSight-HY,Inc</p> 
 * @author LiuXiaojie Sep 25, 2006
 * @version 1.0
 */

public class XmlFileUtil {
    static Logger logger = Logger.getLogger(XmlFileUtil.class);
    
    private String fileName;
    
    protected Document doc;

    public XmlFileUtil() {
        this.fileName = "config.xml";
    }
    
    public XmlFileUtil(String fileName) {
        this.fileName = fileName;
    }
    
    public XmlFileUtil(File file) {
        this.fileName = file.getAbsolutePath();
    }

    public void createFile(String content) throws SAXException, IOException{
        this.doc = this.string2Document(content);
        this.saveDoc();
    }
    
    private Document getDoc() throws SAXException, IOException, DocumentException {
        if (doc == null) {
          this.openDoc();
        }
        return doc;
    }

    public Document getDoc(boolean validate, String xsdFile) throws SAXException, IOException, DocumentException{
    	if (doc == null) {
    		if(validate){
    			this.openDoc(xsdFile);
    		}else{
    			this.openDoc();
    		}
          }
          return doc;
    }
    
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置配置文件名.
     * <p><code>setFileName</code></p>
     * @param fileName
     * @author LiuXiaojie Sep 25, 2006
     */
    public void setFileName(String fileName) {
        if(fileName == null || fileName.equals("")) {
            throw new IllegalArgumentException("file name cannot be null.");
        }
        this.fileName = fileName;
        this.doc = null;
    }
    
    public Document openDoc() throws SAXException, IOException, DocumentException {
        SAXReader saxReader = new SAXReader();
        File file = new File(fileName);
        InputStreamReader read = null;
        try{
        	read = new InputStreamReader (new FileInputStream(file), "utf-8");
        	doc = saxReader.read(read);
        }finally{
        	if(read != null){
        		read.close();
        		read = null;
        	}
        }
        return doc;
    }
    
    public Document openDoc(String xsdFile) 
    		throws SAXException, IOException, DocumentException {        
        SAXReader saxReader = new SAXReader();
        File file = new File(fileName);
        InputStreamReader read = new InputStreamReader (
        		new FileInputStream(file), "utf-8");
        saxReader.setValidation(true);
        saxReader.setFeature("http://xml.org/sax/features/validation", true);
        saxReader.setFeature("http://apache.org/xml/features/validation/schema",true);
        saxReader.setProperty("http://apache.org/xml/properties/schema/" +
        				"external-noNamespaceSchemaLocation",xsdFile
		);
        doc = saxReader.read(read);
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        SAXValidator validator = new SAXValidator();
		validator.setErrorHandler(errorHandler);
		validator.validate(doc);
        return doc;
    }
    
    public Document saveDoc() throws IOException, SAXException{
        File file = new File(this.getFileName());
        try {
            String tempXml =  this.formatXml(this.getDoc(), "utf-8");
            FileUtils.writeStringToFile(file, tempXml, "utf-8");
        } catch (DocumentException e) {
            logger.error("saveDoc",e);
        }
        
        return doc;
    }
    
    public static Document string2Document(String str) {
        Document doc = null;
        try {
            doc = new SAXReader().read(new StringReader(str));
        } catch (DocumentException e) {
            logger.error("string2Document",e);
            return null;
        }
        return doc;
    }
    
    
    public static Document inputStream2Document(InputStream is) {
        Document document = null;
        SAXReader saxReader = new SAXReader();
        InputStreamReader read;
        try {
            read = new InputStreamReader (is,"UTF-8");
            BufferedReader reader=new BufferedReader(read);
            document = saxReader.read(reader);
        } catch (UnsupportedEncodingException e) {
        	logger.error("XmlFileUtils",e);
        } catch (DocumentException e) {
        	logger.error("XmlFileUtils",e);
        }
        
        return document;
    }
    
    /**
     * 格式化XML.
     * <p><code>formatXml</code></p>
     * @param doc
     * @return
     * @author LiuXiaojie 2007-3-22
     */
    public static String formatXml(Document doc, String encoding){
        StringWriter writer = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        if(encoding == null || encoding.equals("")){
            format.setEncoding("utf-8");
        }else {
            format.setEncoding(encoding);
        }
           
        XMLWriter xmlwriter = new XMLWriter( writer, format );
        try {
            xmlwriter.write(doc);
        } catch (Exception e) {
        	logger.error("XmlFileUtils",e);
        }
        return writer.toString();
    }

    public static String formatXml(Element ele, String encoding){
        Document doc = DocumentHelper.createDocument();
        doc.setRootElement(ele);
        return formatXml(doc, encoding);
        
    }
    
    public static String formatW3CXml(org.w3c.dom.Document doc, String encoding){
        StringWriter writer = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        if(encoding == null || encoding.equals("")){
            format.setEncoding("utf-8");
        }else {
            format.setEncoding(encoding);
        }
           
        XMLWriter xmlwriter = new XMLWriter( writer, format );
        try {
            xmlwriter.write(doc);
        } catch (Exception e) {
        	logger.error("formatW3CXml",e);
        }
        return writer.toString();
    }
    
    public static String formatW3CXml(org.w3c.dom.Document doc){
        return formatW3CXml(doc, null);        
    }

    
    public static String formatXml(String xml, String encoding){
        Document doc = string2Document(xml);
        if(doc != null){
            return formatXml(doc, encoding);
        }
        return xml;        
    }
    
    public static String formatXml(String xml){
       return formatXml(xml, null);    
    }

    public static Element string2Element(String xml)  {
       return string2Document(xml).getRootElement();
    }

    public static String stringToXml(String value){
    	if(value == null){
    		return null;
    	}
    	
        value = value.replaceAll("\n", "&#xA;");
        value = value.replaceAll("\r", "&#xD;");
        value = value.replaceAll("\t", "&#x9;");
//      value = value.replaceAll(" ", "&#x20;");
//        value = value.replaceAll("<", "&lt;");
//        value = value.replaceAll(">", "&gt;");
//        value = value.replaceAll("&", "&amp;");
//        value = value.replaceAll("'", "&apos;");
//        value = value.replaceAll("\"", "&quot;");
        return value;
    }
    
    public static String xmlToString(String xml){
    	if(xml == null){
    		return xml;
    	}
    	
        xml = xml.replaceAll("&#xA;","\n");
        xml = xml.replaceAll("&#xD;","\r");
        xml = xml.replaceAll("&#x9;","\t");
//        xml = xml.replaceAll("&#x20;"," ");
//        xml = xml.replaceAll("&lt;","<");
//        xml = xml.replaceAll("&gt;",">");
//        xml = xml.replaceAll("&amp;","&");
//        xml = xml.replaceAll("&apos;","'");
//        xml = xml.replaceAll("&quot;", "\"");
        return xml;
    }
    
    public static void main(String[] args) throws SAXException, IOException, DocumentException {
    	File file = new File("E:\\a.xml");
    	XmlFileUtil xmlFile = new XmlFileUtil(file);
    	Element root = xmlFile.getDoc().getRootElement();
    	Element roleEle = root.element("role");
  
    	String text = roleEle.getText();
    	text = text + "\r\n" +  System.currentTimeMillis();
    	//roleEle.setData("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n<'\">");
    	FlyweightCDATA cdata = new FlyweightCDATA(text);
    	//root.remove(roleEle);
    	roleEle.clearContent();
    	roleEle.add(cdata);
    	root.add(cdata);
    	
    	xmlFile.saveDoc();
    	
    	System.out.println(text);
    	
	}
    
    private static Document openDoc1() throws SAXException, IOException, DocumentException{
    	XmlFileUtil xml = new XmlFileUtil("D:\\a.xml");
    	return xml.getDoc();
    }
    private static Document openDoc2() throws SAXException, IOException, DocumentException{
    	String content = FileUtils.readFileToString(new File("D:\\a.xml"));
    	return XmlFileUtil.string2Document(content);
    }
    
}
