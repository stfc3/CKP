/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Trungtd3
 */
public class DateTimeUtils {
    
    private static String pattern_full = "dd/MM/yyyy HH:mm:ss";
    
     public static String getSysDateTime(String pattern) throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            throw e;
        }
    }
     
     /**
      * Lay ngay he thong theo dinh dang dd/MM/yyyy HH:mm:ss
      * @return
      * @throws Exception 
      */
       public static String getSysDateTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern_full);
        try {
            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            throw e;
        }
    }
    
}
