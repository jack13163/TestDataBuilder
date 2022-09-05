package com.testDataBuilder.util;

public class RandomUtil {

    public static int randomInt(int min, int max){
        return (int) (min + Math.random() * (max - min));
    }
    
    public static double randomDouble(double min, double max){
        return (min + Math.random() * (max - min));
    }
    
    public static long randomLong(long min, long max){
        return (min + (long)(Math.random() * (max - min)));
    }
}
