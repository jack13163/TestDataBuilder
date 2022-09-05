package com.testDataBuilder.core;

/**
 * 数据库类型，及通用字符串模板。
 * <p>Title：DBType.java</p>
 * <p>Description：CommonEncryptInterface</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-4-16
 * @version 1.0
 */
public class DBType {
    
    public static final String ACCESS = "Access";

    public static final String MY_SQL = "MY_SQL";

    public static final String MS_SQL_SERVER = "MS_SQL_SERVER";

    public static final String ORACLE = "Oracle";

    public static final String SYBASE = "Sybase";

    public static final String ODBC = "odbc";

    public static final String DB2 = "db2";

    public static final String POSTGRE_SQL = "PostgreSQL";

//    public static final String DRIVER_ACCESS = "sun.jdbc.odbc.JdbcOdbcDriver";
//
//    public static final String DRIVER_MY_SQL = "com.mysql.jdbc.Driver";
//
//    public static final String DRIVER_MS_SQL_SERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//
//    public static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
//
//    public static final String DRIVER_SYBASE = "com.sybase.jdbc2.jdbc.SybDriver";
//
//    public static final String DRIVER_ODBC = "sun.jdbc.odbc.JdbcOdbcDriver";

//    public static final String DRIVER_DB2 = "Com.ibm.db2.jdbc.net.DB2Driver";
//
//    public static final String DRIVER_POSTGRE_SQL = "org.postgresql.Driver";

//    public static Map getAllDBTemplate(){
//        Map template = new HashMap();
//        template.put(ACCESS, new DatabaseConfig(DRIVER_ACCESS, 
//                     "dbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=F:\\data.mdb"));
//        template.put(MY_SQL, new DatabaseConfig(DRIVER_MY_SQL, 
//                      "jdbc:mysql://MyDbComputerNameOrIP:3306/myDatabaseName"));
//        template.put(MS_SQL_SERVER, new DatabaseConfig(DRIVER_MS_SQL_SERVER,
//                        "jdbc:sqlserver://mssqlhostreport:1433;DatabaseName=myDBName"));
//        template.put(ORACLE, new DatabaseConfig(DRIVER_ORACLE, 
//                        "jdbc:oracle:thin:@MyDbComputerNameOrIP:1521:ORCL"));
//        template.put(SYBASE, new DatabaseConfig(DRIVER_SYBASE, 
//                        "jdbc:sybase:Tds:MyDbComputerNameOrIP:2638"));
//        template.put(ODBC, new DatabaseConfig(DRIVER_ODBC, 
//                        "jdbc:odbc:dsnName"));
//        template.put(DB2, new DatabaseConfig(DRIVER_DB2, 
//                        "jdbc:db2://192.9.200.108:6789/SAMPLE"));
//        template.put(POSTGRE_SQL, new DatabaseConfig(DRIVER_POSTGRE_SQL, 
//                        "jdbc:postgresql://MyDbComputerNameOrIP/myDatabaseName"));
//        
//        return template;
//    }
    
   
    /**
     * 得到一个样板.
     * <p><code>getDBTemplate</code></p>
     * @param dbType
     * @return
     * @author LiuXiaojie 2007-4-16
     */
//   public static final DatabaseConfig getDBTemplate(String dbType){
//       return (DatabaseConfig) getAllDBTemplate().get(dbType);
//   }
//   
//   
//   public static void main(String[] args) {
//       DatabaseConfig dbConfig = DBType.getDBTemplate(DBType.SYBASE);
//       System.out.println(dbConfig.getUrl());
//       System.out.println(dbConfig.getUrl("abc"));
//   }
}
