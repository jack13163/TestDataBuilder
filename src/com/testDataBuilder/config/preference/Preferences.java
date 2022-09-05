package com.testDataBuilder.config.preference;

import java.io.File;

public class Preferences {

    private static IPreference preference = null;
    
    public static IPreference getPreference(File workspace){  
        if(preference == null){
            preference = XmlPreference.autoConfig(workspace);
        }
        return preference;
    }
    
    public static IPreference getPreference(){  
       return preference;
    }
    
    
    public static void main(String[] args) {
        
    }
}
