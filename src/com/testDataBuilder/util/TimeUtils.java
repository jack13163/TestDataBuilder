package com.testDataBuilder.util;

import java.text.SimpleDateFormat;

public class TimeUtils {

    public static final String FILENAME_FORMAT = "yyyyMMdd_HHmmss";
    
    public static String formatData(java.util.Date date, String format){
        SimpleDateFormat dataFormater = new SimpleDateFormat(format);
        return dataFormater.format(date);
    }
    
}
