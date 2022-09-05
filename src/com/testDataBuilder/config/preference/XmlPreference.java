package com.testDataBuilder.config.preference;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.util.BeanXMLMapping;

public class XmlPreference implements IPreference {
 
    static Logger logger = Logger.getLogger(XmlPreference.class);
    
    private Boolean global = Boolean.TRUE;//是全局配置还是本工程的配置。
    
    public Boolean isGlobal(){
        return global;
    }
    
    public void setGlobal(Boolean global){
        this.global = global;
    }
    
    public XmlPreference(){
        
    }
    public XmlPreference(IPreference pre){
    	constructMe(pre);
    }
    
    public XmlPreference constructMe(IPreference pre){
        this.setAutoConnWhenProgramStartup(pre.getAutoConnWhenProgramStartup());
        this.setClearBefInsert(pre.getClearBefInsert());
        this.setCloseIDAutoInsert(pre.getCloseIDAutoInsert());
        this.setDataTypePluginDir(pre.getDataTypePluginDir());
        this.setDateFormat(pre.getDateFormat());
        this.setDefaultRowToGenerate(pre.getDefaultRowToGenerate());
        this.setMaxDate(pre.getMaxDate());
        this.setMinDate(pre.getMinDate());
        this.setNotIncludeIDEntityClumn(pre.getNotIncludeIDEntityClumn());
        this.setReadDBInfoFromFileWhenConnError(pre.getReadDBInfoFromFileWhenConnError());
        this.setSaveDbMetaInfo(pre.getSaveDbMetaInfo());
        this.setTextFieldMax(pre.getTextFieldMax());
        this.setTextFieldMin(pre.getTextFieldMin());
        this.setGenerateSQLFile(pre.getGenerateSQLFile());
        this.setWorkspace(pre.getWorkspace());
        this.setQueryConsoleMaxSize(pre.getQueryConsoleMaxSize());
        return this;
    }
    
    private String workspace;
    
    private int queryConsoleMaxSize = 1000;
    
    /**
     * 程序启动时自动连接数据库。（根据上次的连接参数）
     * <p><code>autoConnOnStartup</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    private Boolean autoConnWhenProgramStartup;
   
    /**
     * 连接数据库后，保存数据库元信息。再次进入系统可以不登录数据库。
     * <p><code>saveDbMetaInfo</code></p>
     * @return
     * @author LiuXiaojie 2007-12-11
     */
    private Boolean saveDbMetaInfo;
    
    /**
     * 当连接数据库失败后，自动读取本地缓存的数据库元信息。
     * <p><code>readDBInfoFromFileWhenConnError</code></p>
     * @return
     * @author LiuXiaojie 2007-12-11
     */
    private Boolean readDBInfoFromFileWhenConnError;
    
    //[start]反向映射（即根据DB元信息自动反射生成配置文件)，默认设置
    /**
     * 产生的数据的默认数量.(每表)
     * <p><code>defGenNum</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    private Long defaultRowToGenerate;
    
    /**
     * 不包含IDEntity列。
     * <p><code>notIncludeIDEntityClumn</code></p>
     * @return
     * @author LiuXiaojie 2007-12-18
     */
    private Boolean notIncludeIDEntityClumn;
    /**
     * 默认自动关闭自动增长列
     * <p><code>CloseIDAutoInsert</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    private Boolean closeIDAutoInsert;

    /**
     * 插入前删除原数据。
     * <p><code>clearBefInsert</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    private Boolean clearBefInsert;
    
    private Date minDate;
    
    private Date maxDate;
    
    /**
     * Text字段最小值。
     * <p><code>textFieldMin</code></p>
     * @return
     * @author LiuXiaojie 2007-12-25
     */
    private Long textFieldMin;
    
    private Long textFieldMax;
    
    //[end]
    
    //[start]system
     private Boolean generateSQLFile;
    
    /**
     * 新添加的类型，所在的目录。
     * <p><code>getDataTypePluginDir</code></p>
     * @return
     * @author LiuXiaojie 2007-12-27
     */
    private String dataTypePluginDir;
    
    private String dateFormat;
    //[end]

    public static final String config = "preference.config.xml";

    public String toXMLString() throws IOException{
        return BeanXMLMapping.toXML(this);
    }
    
    /**
     * 首先从工作区加载preference.config.xml,如果未找到从全局区找，
     * 再未找到就返回DefPreference实例。
     * <p><code>autoConfig</code></p>
     * @return
     * @author LiuXiaojie 2007-12-28
     */
    public static IPreference autoConfig(File workspace){   
        
        IPreference pre = null;
        if(workspace != null && workspace.exists() && workspace.isDirectory()){
            File configFile = new File(workspace.getAbsolutePath(),
                            config);
            if(configFile.exists()){
                try {
                    XmlPreference xmlPre = configFromFile(configFile);
                    xmlPre.setGlobal(false);
                    pre = xmlPre;
                } catch (IOException e) {
                    logger.error("autoConfig", e);
                }
            }
        }
        if(pre == null){
            pre = configFromGlobal();
        }
        return pre;
    }
    
    public static IPreference configFromGlobal(){
        File globalConfigFile = new File(config);
        IPreference pre = null;
        if(globalConfigFile.exists()){
            try {
                XmlPreference xmlPre = configFromFile(globalConfigFile);
                xmlPre.setGlobal(true);
                pre = xmlPre;
            } catch (IOException e) {
                logger.error("configFromGlobal", e);
            }
        }
        if(pre == null){
            pre = new XmlPreference(new DefPreference());
        }
        return pre;
    }

    public static XmlPreference configFromFile(File config) throws IOException{
        logger.debug("Preferences file:" + config.getAbsolutePath());
        String xml = FileUtils.readFileToString(config, "utf-8");
        if(StringUtils.isNotEmpty(xml)){
           return  fromXML(xml);
        }
        return null;
    }
    
    public static XmlPreference fromXML(String xml) throws IOException{
        return (XmlPreference) BeanXMLMapping.fromXML(xml, XmlPreference.class);
    }
    
    public void saveConfig(File workspace) throws IOException{
        String xml = this.toXMLString();
        if(this.isGlobal()){
            FileUtils.writeStringToFile(new File(config), xml, "utf-8");
        }else{
            FileUtils.writeStringToFile(new File(workspace,config), xml, "utf-8");
        }
    }

    
    public Boolean getAutoConnWhenProgramStartup() {
        return autoConnWhenProgramStartup;
    }

    public void setAutoConnWhenProgramStartup(Boolean autoConnWhenProgramStartup) {
        this.autoConnWhenProgramStartup = autoConnWhenProgramStartup;
    }

    public Boolean getClearBefInsert() {
        return clearBefInsert;
    }

    public void setClearBefInsert(Boolean clearBefInsert) {
        this.clearBefInsert = clearBefInsert;
    }

    public Boolean getCloseIDAutoInsert() {
        return closeIDAutoInsert;
    }

    public void setCloseIDAutoInsert(Boolean closeIDAutoInsert) {
        this.closeIDAutoInsert = closeIDAutoInsert;
    }

    public String getDataTypePluginDir() {
        return dataTypePluginDir;
    }

    public void setDataTypePluginDir(String dataTypePluginDir) {
        this.dataTypePluginDir = dataTypePluginDir;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Long getDefaultRowToGenerate() {
        return defaultRowToGenerate;
    }

    public void setDefaultRowToGenerate(Long defaultRowToGenerate) {
        this.defaultRowToGenerate = defaultRowToGenerate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Boolean getNotIncludeIDEntityClumn() {
        return notIncludeIDEntityClumn;
    }

    public void setNotIncludeIDEntityClumn(Boolean notIncludeIDEntityClumn) {
        this.notIncludeIDEntityClumn = notIncludeIDEntityClumn;
    }

    public Boolean getReadDBInfoFromFileWhenConnError() {
        return readDBInfoFromFileWhenConnError;
    }

    public void setReadDBInfoFromFileWhenConnError(Boolean readDBInfoFromFileWhenConnError) {
        this.readDBInfoFromFileWhenConnError = readDBInfoFromFileWhenConnError;
    }

    public Boolean getSaveDbMetaInfo() {
        return saveDbMetaInfo;
    }

    public void setSaveDbMetaInfo(Boolean saveDbMetaInfo) {
        this.saveDbMetaInfo = saveDbMetaInfo;
    }

    public Long getTextFieldMax() {
        return textFieldMax;
    }

    public void setTextFieldMax(Long textFieldMax) {
        this.textFieldMax = textFieldMax;
    }

    public Long getTextFieldMin() {
        return textFieldMin;
    }

    public void setTextFieldMin(Long textFieldMin) {
        this.textFieldMin = textFieldMin;
    }

    public Boolean getGenerateSQLFile() {
        return generateSQLFile;
    }

    public void setGenerateSQLFile(Boolean generateSQLFile) {
        this.generateSQLFile = generateSQLFile;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

	public int getQueryConsoleMaxSize() {
		return queryConsoleMaxSize;
	}

	public void setQueryConsoleMaxSize(int queryConsoleMaxSize) {
		this.queryConsoleMaxSize = queryConsoleMaxSize;
	}
    
    
}
