package com.testDataBuilder.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.apache.log4j.Logger;

public class SQLRecorder {

    static Logger logger = Logger.getLogger(SQLRecorder.class);
    
    public static final String LINE_SEP = System.getProperty("line.separator");
    
    private RandomAccessFile file = null;
    private File sqlFile = null;
    
    public SQLRecorder(File sqlFile){
        try{
            this.sqlFile = sqlFile;
            file = new RandomAccessFile(sqlFile, "rw");
        }catch(FileNotFoundException ex){
            logger.error("open file[" +sqlFile.getAbsolutePath() +"] failure", ex);
        }
    }
    
    public void writeSql(String sql) throws IOException{
        file.write((sql + LINE_SEP).getBytes());
    }

    @Override
    protected void finalize() throws Throwable {
       file.close();
    }

    public File getSqlFile() {
        return sqlFile;
    }

    public void setFile(File sqlFile) {
        this.sqlFile = sqlFile;
    }
    
    public static void main(String[] args) throws Throwable {
    	SQLRecorder recorder = new SQLRecorder(new File("D:", "a.txt"));
    	for(int i=0;i<10;i++){
    		recorder.writeSql("中国" + System.currentTimeMillis());
    	}
    	recorder.finalize();
	}
}
