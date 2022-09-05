package com.testDataBuilder.util;

import org.apache.log4j.Logger;

public class TLoger {

    public static Logger logger = null;

    static {
        if (logger == null) {
            logger = Logger.getLogger("com.testDataBuilder.log");
        }
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }

    public static void debug(String msg, Throwable ex){
    	logger.debug(msg, ex);
    }
    
    public static void info(String msg) {
        logger.info(msg);
    }

    public static void info(String msg, Throwable ex) {
        logger.info(msg, ex);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void warn(String msg, Throwable ex) {
        logger.warn(msg);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(String msg, Throwable ex) {
        logger.error(msg, ex);
    }

    public static void fatal(String msg) {
        logger.fatal(msg);
    }
    public static void fatal(String msg, Throwable ex) {
        logger.fatal(msg, ex);
    }

}
