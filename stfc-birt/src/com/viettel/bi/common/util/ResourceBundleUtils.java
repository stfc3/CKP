/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.bi.common.util;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;

/**
 *
 * @author trungtd3
 */
public class ResourceBundleUtils {
    static private ResourceBundle rb = null;

    private static final Logger logger = Logger.getLogger(ResourceBundleUtils.class);
    public synchronized static String getResource(String key){
        rb = ResourceBundle.getBundle("config");
        return rb.getString(key);
    }

    /**     
     * Lay gia tri key cua file config
     * @param key: ten key
     * @param fileName: ten file config
     * @return: gia tri key
     */
    public synchronized static String getResource(String key, String fileName) {
        rb = ResourceBundle.getBundle(fileName);
        return rb.getString(key);
    }
    
    
     public synchronized static String getResource(String key, String fileName, String defaultValue) {
        try {
            rb = ResourceBundle.getBundle(fileName);
            return rb.getString(key);
        } catch (java.util.MissingResourceException ex) {
            logger.error(ex.getMessage(), ex);
            //xu ly loi ko co key thi tra ve default.
            return defaultValue;
        }

    }
}
