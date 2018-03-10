/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.common;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author longnt
 */
public class Constants {

    private static final Logger log = LogManager.getLogger();

    public static String COREBANK_IP;
    public static int COREBANK_PORT;
    public static int BUFFER_SIZE;
    public static int COREBANK_TIMEOUT;

    private static boolean isLoaded = false;

    public static void init() {
        if (isLoaded) {
            return;
        }
        isLoaded = true;
        Properties appConfig = new Properties();
        Properties dbConfig = new Properties();
        try {
            String fileName = "conf/config.properties";
            appConfig.load(new FileInputStream(fileName));
            COREBANK_IP = appConfig.getProperty("CoreBankIP", "10.71.64.11");
            COREBANK_PORT = Integer.parseInt(appConfig.getProperty("CoreBankPort", "3423"));
            BUFFER_SIZE = Integer.parseInt(appConfig.getProperty("BufferSize", "8192"));
            COREBANK_TIMEOUT = Integer.parseInt(appConfig.getProperty("CoreBankTimeout", "180000"));

        } catch (Exception e) {
            log.info("Constant init ex:", e);
        }
    }
}
