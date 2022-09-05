package com.testDataBuilder.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.config.DatabaseConfig;
import com.testDataBuilder.dbMetaInfo.Column;
import com.testDataBuilder.dbMetaInfo.Database;
import com.testDataBuilder.dbMetaInfo.ForeignKey;
import com.testDataBuilder.dbMetaInfo.PrimaryKey;
import com.testDataBuilder.dbMetaInfo.Table;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.util.Global;

/**
 * 数据库源.
 * <p>Title：Database.java</p>
 * @author LiuXiaojie 2007-4-16
 * @version 1.0
 */
public class DatabaseUtil{
 
    static Logger logger = Logger.getLogger(DatabaseUtil.class);
    
    //只得到DB信息。
    public static final int LEVEL_DB = 1;
    
    //得到DB和table信息.
    public static final int LEVEL_TABLE = 2;
    
    public static final int LEVEL_COLUMN = 3;

    private static Connection conn;
    
    private DatabaseConfig config;
    
    public DatabaseUtil(){
        
    }

    public boolean connect()  throws SQLException{
        DatabaseConfig dbConfig = this.getConfig();
        if(this.conn == null || this.conn.isClosed()){
            this.conn = this.getConnectin(dbConfig.getDriverClass(),
                            dbConfig.getURL(), dbConfig.getUserName(), dbConfig.getPassword());
        }
        return true;
    }

    public static boolean testConnect(DatabaseConfig databaseConfig) throws SQLException{
        Connection tempConn = null;
        try{
        tempConn = getConnectin(databaseConfig.getDriverClass(),
                        databaseConfig.getURL(), databaseConfig.getUserName(), databaseConfig.getPassword());
        
        }finally{
            if(tempConn != null){
                try{
                tempConn.close();
                }catch(SQLException ex){
                    logger.error("tempConn.close", ex);
                }
            }
        }
        return true;
    }
    
//    public List getArchitecture()  throws BaseException{
//        
//        try {
//            return getCatalogs(getConn(), LEVEL_COLUMN);
//        } catch (SQLException e) {
//            throw new BaseException(e);
//        }
//    }
//
//    public List getArchitecture(int level)  throws BaseException{        
//        try {
//            return getCatalogs(getConn(), level);
//        } catch (SQLException e) {
//            throw new BaseException(e);
//        }
//    }

    /**
     * 得到当前连接的数据库的结构。
     * <p><code>getDefArchitecture</code></p>
     * @param level
     * @return
     * @throws BaseException
     * @author LiuXiaojie 2008-1-24
     */
    public Database getDefArchitecture(int level)  throws BaseException{        
        try {
            Connection conn = this.getConn();
            return getCatalog(conn,conn.getCatalog(), level);
        } catch (SQLException e) {
            throw new BaseException(e);
        }
    }
    
    public ResultSet getData(String tableName) throws BaseException {
        String sql = "select * from " + tableName;        
        return getDataFromSQL(sql);
    }
    
    public ResultSet getDataFromSQL(String sql) throws BaseException{
        try {
            PreparedStatement statement = getConn().prepareStatement(sql);
            return statement.executeQuery();
        } catch (SQLException e) {
           throw new BaseException(e);
        }
    }
    public static Connection getConnection(DatabaseConfig config) throws SQLException{
        return getConnectin(config.getDriverClass(), config.getURL(), config.getUserName(), config.getPassword());
    }
    
    public static Connection getConnectin(String driver,String url, String userName, String pwd ) throws SQLException {
        try {
            File lib = new File("res/lib");
            ClassLoader classLoader = null;
            if(lib.exists()){
                logger.info("load driverClass from " + lib.getAbsolutePath());
                URL[] urls = null;
                try {
                    urls = new URL[]{lib.toURL()};
                } catch (MalformedURLException e) {
                    logger.error("加载SQL驱动时出错", e);
                }
                classLoader = new URLClassLoader(urls, DatabaseUtil.class.getClassLoader());
            }else{
                classLoader = DatabaseUtil.class.getClassLoader();
            }
            Class.forName(driver, true, classLoader);
            
        } catch (ClassNotFoundException e) {
            logger.error("加载SQL驱动时出错", e);
        }
        return DriverManager.getConnection(url, userName, pwd);
    }
    
    
   public static void main(String[] args) throws IOException, SQLException, BaseException {
//       System.out.println(" =================== sql server ==================");
//       DatabaseUtil dbUtil = new DatabaseUtil();
//       DatabaseConfig config = new DatabaseConfig();
//       config.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//       config.setURL("jdbc:sqlserver://localhost:1433;databaseName=drmpolicy20");
//       config.setUserName("sa");
//       config.setPassword("sa");
//       dbUtil.setConfig(config);
//       Connection conn = dbUtil.getConn();
//       Statement stat = conn.createStatement();
//       String fileName = "E:\\workspace\\TestDataBuilder\\res\\res\\demo\\tdbDemoForSqlserver.sql";
//       String sqls= FileUtils.readFileToString(new File(fileName));
//       
//       String[] strSQL = sqls.split("go");
//       for(String sql : strSQL){
//    	   if(sql != null){
//    		   sql = sql.trim();
//    	   }
//    	   if(StringUtils.isNotEmpty(sql)){
//    		   stat.addBatch(sql);
//    	   }
//       }
//       stat.executeBatch();
//       
//       if(conn != null){
//    	   conn.close();
//       }
       
       testHSQLDB();
//       String name = getHSQLDBName("jdbc:hsqldb:file:testdb;shutdown=true");
//       System.out.println(">" + name);
//       System.out.println(" =================== oracle ==================");
//       testOracle();
    }
   
   public static void testSQLServer(){
       try {
           DatabaseUtil dbUtil = new DatabaseUtil();
           DatabaseConfig config = new DatabaseConfig();
           config.setDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           config.setURL("jdbc:sqlserver://localhost:1433;databaseName=school");
           config.setUserName("sa");
           config.setPassword("sa");
           dbUtil.setConfig(config);
           Database db = dbUtil.getDefArchitecture(DatabaseUtil.LEVEL_COLUMN);
           dbUtil.setBasePath("E:\\");
           
               dbUtil.writeDBInfo(db);
               System.out.println("DBName:" + db.getDbName());
              
       }catch (BaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
   }
   
   public static void testHSQLDB() throws SQLException, BaseException, IOException{
      
           DatabaseUtil dbUtil = new DatabaseUtil();
           DatabaseConfig config = new DatabaseConfig();
           config.setDriverClass("org.hsqldb.jdbcDriver");
           config.setURL("jdbc:hsqldb:file:testdb;shutdown=true");
           config.setUserName("sa");
           config.setPassword("");
           dbUtil.setConfig(config);
           Connection conn = dbUtil.getConn();
           Statement stat = conn.createStatement();
           String fileName = "E:\\workspace\\TestDataBuilder\\res\\res\\demo\\tdbDemoForSqlserver.sql";
           String sqls= FileUtils.readFileToString(new File(fileName));
           String[] strSQL = sqls.split("go");
           for(String sql : strSQL){
        	   if(sql != null){
        		   sql = sql.trim();
        	   }
        	   if(StringUtils.isNotEmpty(sql)){
        		   stat.addBatch(sql);
        	   }
           }
           stat.executeBatch();
           
           conn.close();

   }
   
   public static void testOracle(){
       try {
           DatabaseUtil dbUtil = new DatabaseUtil();
           DatabaseConfig config = new DatabaseConfig();
           config.setDriverClass("oracle.jdbc.driver.OracleDriver");
           config.setURL("jdbc:oracle:thin:@192.168.0.16:1521:orcl2");
           config.setUserName("drmpolicyuser");
           config.setPassword("drmpolicypwd");
           dbUtil.setConfig(config);
           Database db = dbUtil.getDefArchitecture(DatabaseUtil.LEVEL_COLUMN);
           
           System.out.println("DBName:" + db.getDbName());
           for(Table table :db.getTables()){
               System.out.println(table.getTableName());
           }
             
       }catch (BaseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
   }
   
    private String basePath = "";
    
    public static final String DB_CACHE_FILE_SUFFIX = ".metaInfo.xml";
    
    public boolean writeDBInfo(Database database){
        if(database != null){
            File file = new File(getBasePath() +  Global.SEP + "DB" + DB_CACHE_FILE_SUFFIX);
            try {
                String xml = database.toXmlString();
                FileUtils.writeByteArrayToFile(file, xml.getBytes("utf-8"));
                logger.info("write metainfo to [" + file.getAbsolutePath());
                return true;
            } catch (UnsupportedEncodingException e) {
                logger.error("writeDBInfo", e);
            } catch (IOException e) {
                logger.error("writeDBInfo", e);
            }
        }
        return false;
    }
    
    static FilenameFilter cacheFileFilter = new FilenameFilter(){
        public boolean accept(File file, String fileName) {
            return fileName.endsWith(DatabaseUtil.DB_CACHE_FILE_SUFFIX);
        }
    };
    
    public Database readDBInfoFromBasePath() throws IOException{
        File dir = new File(this.getBasePath());
        Database database = null;
        if(dir.exists()){
            File[] files = dir.listFiles(cacheFileFilter);
            if(files.length > 0){
                String xml = null;
                try{
                    xml = FileUtils.readFileToString(files[0], "utf-8");               
                    database = Database.fromXml(xml);
                }catch(Throwable ex){
                    logger.error(String.format("加载工程[%s]下的配置时出错, xml [\n%s", this.getBasePath(),xml), ex);
                }
            }
        }
        return database;
    }
    
    public boolean break_ = false;
    
    public synchronized boolean getBreak() {
        return this.break_;
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
        if(this.conn != null && !this.conn.isClosed()){
            conn.close();
            conn = null;
        }
    }

    public synchronized void setBreak(boolean break_){
        this.break_ = break_;
    }
    
    /**
     * 得到一个DBMS中的所有数据库信息.及其中的表信息.
     * <p>
     * <code>getCatalogs</code>
     * </p>
     * 
     * @param conn
     * @return
     * @throws SQLException
     * @author LiuXiaojie 2007-4-6
     */
    public synchronized List getCatalog(Connection conn, int level) throws SQLException{
        List catalogs = new ArrayList();
        DatabaseMetaData dbMetadata =  conn.getMetaData();
        ResultSet catalogResult = dbMetadata.getCatalogs();
        
        ResultSetMetaData resultMetaData = catalogResult.getMetaData();
        
        // 依次取出所有的数据库信息.
        while(resultMetaData.getColumnCount() > 0){
            if(!catalogResult.next() || this.getBreak()){
                this.setBreak(false);
                break;
            }
            
            String catalogName = catalogResult.getString(1);
            Database db =  getDBMetaInfo(dbMetadata, catalogName, level);
            catalogs.add(db);            
        }
        return catalogs;
    }

    public synchronized Database getCatalog(Connection conn, String dbName, int level) throws SQLException{
        DatabaseMetaData dbMetadata =  conn.getMetaData();
        Database db =  getDBMetaInfo(dbMetadata, dbName, level);
        return db;
    }

    public static boolean isOracleDriver(DatabaseMetaData dbMetadata) throws SQLException{
        if(dbMetadata != null){
            String temp = dbMetadata.getDatabaseProductName().toUpperCase();
            return temp.contains("ORACLE");
        }
        return false;
    }
    
    public static boolean isHSQLDriver(DatabaseMetaData dbMetadata) throws SQLException{
        if(dbMetadata != null){
            String temp = dbMetadata.getDatabaseProductName().toUpperCase();
            return temp.contains("HSQL");
        }
        return false;
    }
    
    public static boolean isSQLServerDriver(DatabaseMetaData dbMetadata) throws SQLException{
        if(dbMetadata != null){
            String temp = dbMetadata.getDatabaseProductName().toUpperCase();
            return temp.contains("MICROSOFT SQL SERVER");
        }
        return false;
    }
    
    public static boolean isMysqlDriver(DatabaseMetaData dbMetadata) throws SQLException{
        if(dbMetadata != null){
            String temp = dbMetadata.getDatabaseProductName().toUpperCase();
            return temp.contains("MYSQL");
        }
        return false;
    }
    
    /**
     * 取出一个库的信息.
     * <p>
     * <code>getDBMetaInfo</code>
     * </p>
     * 
     * @param dbMetadata
     * @param catalogName
     * @param level
     * @return
     * @throws SQLException
     * @author LiuXiaojie 2007-7-13
     */
    private Database getDBMetaInfo(DatabaseMetaData dbMetadata, String catalogName, int level) throws SQLException {
        
        String databaseProductName = dbMetadata.getDatabaseProductName().toUpperCase();
        if(logger.isDebugEnabled()){
            logger.debug("isMysqlDriver.DatabaseProductName:" + databaseProductName);
        }
        
        Database db = new Database();
        db.setDbName(catalogName);
        
        if(level > LEVEL_DB){
            List<Table> tables = new ArrayList<Table>();
            db.setTables(tables); 
            String schema = null;
            if(isOracleDriver(dbMetadata)){
                schema = this.getConfig().getUserName().toUpperCase();
                db.setDbName("ORCL=>" + schema);
            }else if(isHSQLDriver(dbMetadata)){
            	schema = getHSQLDBName(dbMetadata.getURL());
            	db.setDbName(schema);
            }

            if(logger.isDebugEnabled()){
                logger.debug(String.format("&&&&&&&&&&&&&&&&  开始读取数据库[%s]的元信息&&&&&&&&&&&&&&&&&&&&&&&&&", db.getDbName()));
            }
            
            ResultSet tableResult = dbMetadata.getTables(catalogName, 
                            schema, null,
                            new String[]{Table.TABLE});
            // 依次取出一个库中的所有表的信息.
            while(tableResult != null) {
                if(!tableResult.next()){
                    break;
                }
                try{
                    Table table = new Table();
                    String type = tableResult.getString("TABLE_TYPE");

                    table.setType(type);
                    
                    String tableName = tableResult.getString("TABLE_NAME");
                    table.setTableName(tableName);
                    if(logger.isDebugEnabled()){
                        logger.debug(String.format("================ 开始读取表[%s]的元信息. ============", table.getTableName()));
                    }
                    // 添加主键字段。。。
                    ResultSet primaryKeyRs = dbMetadata.getPrimaryKeys(catalogName, null, tableName);
                    while(primaryKeyRs.next()){
                        PrimaryKey primaryKey = table.getPrimaryKey();
                        if(primaryKey == null){
                            primaryKey = new PrimaryKey(primaryKeyRs.getString("PK_NAME"));
                            table.setPrimaryKey(primaryKey);
                        }
                        primaryKey.addField(primaryKeyRs.getString("COLUMN_NAME"));
                    }
                    
                    // foreignKey
                    ResultSet fkRs = dbMetadata.getImportedKeys(null, null, tableName);

                    while(fkRs.next()){
                        String refTableName = fkRs.getString("PKTABLE_NAME");
                        table.addImportedTable(refTableName);
                        ForeignKey foreignKey = table.getForeignKeyByRefTable(refTableName);
                        foreignKey.setFkName(fkRs.getString("FK_NAME"));
                        foreignKey.addField(fkRs.getString("FKCOLUMN_NAME"), fkRs.getString("PKCOLUMN_NAME"));
                    }
                   
                    ResultSet refFkRs = dbMetadata.getExportedKeys(null, null, tableName);
                    while(refFkRs.next()){
                        String refedTableName = refFkRs.getString("FKTABLE_NAME");
                        table.addExportedTable(refedTableName);
                    }

                    tables.add(table);                 
                    if(level > LEVEL_TABLE){
                        
                        ResultSet columnResult = dbMetadata.getColumns(catalogName, null, tableName, null);
                        // 依次取出一个表中的所有列的信息.
                        while(columnResult.next()){
                            try{
                                Column column = convert2Column(columnResult, dbMetadata);   
                                table.getColumns().add(column);
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }// end of if(level > LEVEL_TABLE){
                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            } // end of while(true) {
         }// end of if(level > LEVEL_DB){
        return db;
    }

    public static String getHSQLDBName(String url){
    	String name = "hsqldb";
    	int beginIndex = url.lastIndexOf(':');
    	int endIndex = url.indexOf(';');
    	if(endIndex == -1){
    		endIndex = url.length();
    	}
    	
    	name = url.substring(beginIndex,endIndex);
    	
    	return name;
    }
    
    private Column convert2Column(ResultSet columnResult, DatabaseMetaData dbMetadata) throws SQLException {
        Column column = new Column();
        column.setColumnName(columnResult.getString("COLUMN_NAME"));
        column.setType(columnResult.getInt("DATA_TYPE"));
        column.setTypeName(columnResult.getString("TYPE_NAME"));
        column.setSize(columnResult.getInt("COLUMN_SIZE"));
        column.setNullable(columnResult.getBoolean("NULLABLE"));
        column.setDecimalDigits(columnResult.getInt("DECIMAL_DIGITS")); 
        column.setRemarks(columnResult.getString("REMARKS"));
        if(logger.isDebugEnabled()){
            showResultSetValue(columnResult);
        }
        if(isMysqlDriver(dbMetadata)){
            mysqlRsConvert2Column(columnResult, column);
        }else if(isOracleDriver(dbMetadata)){
            oracleRsConvert2Column(columnResult, column);
        }else if(isSQLServerDriver(dbMetadata)){
            sqlServerRsConvert2Column(columnResult, column);
        }
        
        return column;
    }
    
    void mysqlRsConvert2Column(ResultSet columnResult, Column column) throws SQLException{
        Boolean isAutoIncrement = columnResult.getBoolean("IS_AUTOINCREMENT");
        column.setAutoIncrement(isAutoIncrement);
    }
    
    void oracleRsConvert2Column(ResultSet columnResult, Column column){
        
    }
    
    void sqlServerRsConvert2Column(ResultSet columnResult, Column column) throws SQLException{
        boolean isAutoIncreaseColumn = false;
        String typeName = columnResult.getString("TYPE_NAME");
        if(typeName != null){
            if(typeName.indexOf("identity") != -1){
                isAutoIncreaseColumn = true;
            }
        }
        column.setAutoIncrement(isAutoIncreaseColumn);
    }
    
    private void showResultSetValue(ResultSet columnResult) throws SQLException{
         ResultSetMetaData metaData = columnResult.getMetaData();
        int count = metaData.getColumnCount();
        logger.debug(String.format("===================%s.%s=========================",
                        columnResult.getString("TABLE_NAME"),
                        columnResult.getString("COLUMN_NAME")));
        for(int i=1;i <=count;i++){
            String columnName = metaData.getColumnName(i);
            Object value = columnResult.getObject(i);
            String type = null;
            if(value != null){
                type = value.getClass().getName();
            }
            logger.debug(String.format("%20s=%s [%s]", columnName, value, type));
        }
    }
    
    /**
     * 得到一个表的所有主键值.
     * <p>
     * <code>getTablePKS</code>
     * </p>
     * 
     * @param conn
     * @param catalogName
     * @param tableName
     * @return
     * @throws SQLException
     * @author LiuXiaojie 2007-5-23
     */
    public static List getTablePKS(Connection conn , String catalogName, String tableName) throws SQLException{
        DatabaseMetaData dbMetadata =  conn.getMetaData();
        List columns = new ArrayList();
        ResultSet columnResult = dbMetadata.getPrimaryKeys(catalogName, null, tableName);
        while(!columnResult.next()){
            try{             
                columns.add(columnResult.getString("COLUMN_NAME"));
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        return columns;
    }
    
    public static Object executeFunc(Connection conn, String func) throws SQLException{
    	Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    	ResultSet r = statement.executeQuery(func);
    	if(r.next()){
    		return r.getObject(1);
    	}
    	return null;
    }
    
    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Connection getConn() throws SQLException {
        this.connect();
        return conn;
    }
    
    public DatabaseConfig getConfig() {
        return config;
    }

    public void setConfig(DatabaseConfig config) {
        this.config = config;
    }


}
