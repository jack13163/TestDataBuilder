package com.testDataBuilder.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static void main(String[] args) {
		System.out.println(equals(null, ""));
	}
	
	public static boolean equals(String one, String tow){
		if(one == null){one = "";}
		if(tow == null){tow = "";}
		return one.equals(tow);
	}
	
    public static String subString(String str, String begin, String end){
        if(str == null){
            return null;
        }
        str = str.toLowerCase();
        int bIndex = 0;
        if(begin != null && !begin.equals("")){
            Pattern beginPattern = Pattern.compile(begin);
            Matcher matcher = beginPattern.matcher(str);
            if(matcher.find()){
                String tempStr = matcher.group();
                bIndex = str.indexOf(tempStr);
                if(bIndex == -1){
                    return null;
                }
                bIndex = bIndex + tempStr.length();
            }else{
                return null;
            }
        }
        
        int eIndex = str.length();
        if(end != null && !end.equals("")){
            Pattern endPattern = Pattern.compile(end);
            Matcher matcher = endPattern.matcher(str);
            
            if(matcher.find(bIndex)){
                String tempStr = matcher.group();
                eIndex = str.indexOf(tempStr, bIndex);
                if(eIndex == -1){
                    return null;
                }
            }/*else{
                return null;
            }*/
        }
       
        return str.substring(bIndex, eIndex);
    }
}
