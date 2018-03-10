/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.mailsend.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author viettx
 */
public class DatetimeUtils {

    private static final Logger logger = LogManager.getLogger();

    public static String convertDateToString(Date pstrDate, String pstrPattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pstrPattern);
        try {
            if (pstrDate == null) {
                return "";
            }
            return dateFormat.format(pstrDate);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
