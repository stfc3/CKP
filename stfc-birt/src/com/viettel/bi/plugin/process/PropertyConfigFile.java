/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bi.plugin.process;

import java.lang.reflect.Field;
import java.util.Properties;

public class PropertyConfigFile {
    private static String propertiesFilePath = "file.vbipluggin.properties"; 
    
  
    private static String maxTimeDelete="1";
    private static String isActive="true";
      
    //Cau hinh tham so cho cache document
    private static String maxSizeOfCacheDocument = "100";
    private static String numOfRemoveDocumentObjs="1";
    private static String numPercentRemoveDocument = "50";
    
    private static String numMbBirtDocument="10000";//Megebyte
    private static String isCache;//Megebyte

    public static String getMaxTimeDelete() {
        return maxTimeDelete;
    }

    public static String getIsActive() {
        return isActive;
    }

    public static String getMaxSizeOfCacheDocument() {
        return maxSizeOfCacheDocument;
    }

    public static String getNumOfRemoveDocumentObjs() {
        return numOfRemoveDocumentObjs;
    }

    public static String getNumPercentRemoveDocument() {
        return numPercentRemoveDocument;
    }

    public static String getNumMbBirtDocument() {
        return numMbBirtDocument;
    }

    public static String getIsCache() {
        return isCache;
    }
    
    
        
    public static void loadProperties() throws Throwable {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        Properties prop = new Properties();
        prop.load(classLoader.getResourceAsStream(propertiesFilePath));
        
        String key;
        Class c = PropertyConfigFile.class;
        Field f;
        
        for(Object okey : prop.keySet()) {
            key = okey.toString();
            f = c.getDeclaredField(key);
            f.set(PropertyConfigFile.class, prop.getProperty(key));
        }
    }
}
