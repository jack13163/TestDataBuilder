package com.testDataBuilder.config.preference;

public interface IPreference {

    
    /**
     * 程序启动时自动连接数据库。（根据上次的连接参数）
     * <p><code>autoConnOnStartup</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    public Boolean getAutoConnWhenProgramStartup();
   
    /**
     * 连接数据库后，保存数据库元信息。再次进入系统可以不登录数据库。
     * <p><code>saveDbMetaInfo</code></p>
     * @return
     * @author LiuXiaojie 2007-12-11
     */
    public Boolean getSaveDbMetaInfo();
    
    /**
     * 当连接数据库失败后，自动读取本地缓存的数据库元信息。
     * <p><code>getReadDBInfoFromFileWhenConnError</code></p>
     * @return
     * @author LiuXiaojie 2007-12-11
     */
    public Boolean getReadDBInfoFromFileWhenConnError();
    
    //[start]反向映射（即根据DB元信息自动反射生成配置文件)，默认设置
    /**
     * 产生的数据的默认数量.(每表)
     * <p><code>getDefaultRowToGenerate</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    public Long getDefaultRowToGenerate();
    
    /**
     * 不包含IDEntity列。
     * <p><code>getNotIncludeIDEntityClumn</code></p>
     * @return
     * @author LiuXiaojie 2007-12-18
     */
    public Boolean getNotIncludeIDEntityClumn();
    /**
     * 默认自动关闭自动增长列
     * <p><code>getCloseIDAutoInsert</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    public Boolean getCloseIDAutoInsert();

    /**
     * 插入前删除原数据。
     * <p><code>getClearBefInsert</code></p>
     * @return
     * @author LiuXiaojie 2007-12-3
     */
    public Boolean getClearBefInsert();
    
    /**
     * 插入数据后生成相应的sql语句。
     * <p><code>generateSQLFile</code></p>
     * @return
     * @author LiuXiaojie 2008-1-11
     */
    public Boolean getGenerateSQLFile();
    
    public java.util.Date getMinDate();
    
    public java.util.Date getMaxDate();
    
    /**
     * Text字段最小值。
     * <p><code>textFieldMin</code></p>
     * @return
     * @author LiuXiaojie 2007-12-25
     */
    public Long getTextFieldMin();
    
    public Long getTextFieldMax();
    
    //[end]
    
    //[start]system
    
    /**
     * tableConfig文件的后缀名。
     * <p><code>getTableConfigSuffix</code></p>
     * @return
     * @author LiuXiaojie 2007-12-11
     */
    public static final  String TABLE_CONFIG_SUFFIX = ".tableConfig.xml";
    
    /**
     * 新添加的类型，所在的目录。
     * <p><code>getDataTypePluginDir</code></p>
     * @return
     * @author LiuXiaojie 2007-12-27
     */
    public String getDataTypePluginDir();
    
    /**
     * 
     * @return
     */
    public String getDateFormat();

    public String getWorkspace();
    
    /**
     * 查询控制台,默认返回的最大值.(如果些值太大,可能会导致死机)
     * @return
     */
    public int getQueryConsoleMaxSize();
    //[end]
    
}
