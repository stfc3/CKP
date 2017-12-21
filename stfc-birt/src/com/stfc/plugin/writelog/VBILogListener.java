/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.writelog;

import com.stfc.common.util.ResourceBundleUtils;
import com.viettel.logging.LogWriter;
import java.net.URISyntaxException;
import java.net.URL;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Trungtd3
 */
public class VBILogListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(VBILogListener.class);
    private static LogWriter logWriterRef;
    private static String log_config = ResourceBundleUtils.getResource("log_config");
    private static String bi_log = ResourceBundleUtils.getResource("bi_log");
    private static String seperator = ResourceBundleUtils.getResource("seperator");

    public synchronized static LogWriter getLogWriterRef() {
        return logWriterRef;
    }

    public static void setLogWriterRef(LogWriter logWriterRef) {
        VBILogListener.logWriterRef = logWriterRef; 
    }

    public synchronized static String getSeperator() {
        return seperator;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String subPath = "/";
            String log_config_use;
            if (log_config.startsWith("/")) {
                log_config_use = log_config;
            } else {
                log_config_use = "/" + log_config;
            }
            URL urlFileDB = Thread.currentThread().getContextClassLoader().getResource(log_config_use);
            String configFile = null;
            try {
                configFile = urlFileDB.toURI().getPath();
            } catch (URISyntaxException ex) {
                logger.error(ex.getMessage(), ex);
            }

            VBILogListener.setLogWriterRef(new LogWriter(configFile, subPath, bi_log));
            logWriterRef.start();
        } catch (Throwable ta) {
            logger.info("Init context NOT OK", ta);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            if (logWriterRef != null) {
                logWriterRef.stop();
            }

            VBILogListener.setLogWriterRef(null);
        } catch (Throwable ta) {
            logger.info("Destroyed context NOT OK", ta);
        }
    }

}
