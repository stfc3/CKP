/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.process;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Trungtd3
 */
public class ProcessFileListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ProcessFileListener.class);
    private static ProcessFile processFile;

    public synchronized static ProcessFile getProcessFile() {
        return processFile;
    }

    public static void setProcessFile(ProcessFile processFile) {
        ProcessFileListener.processFile = processFile;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("start contextInitialized in ProcessFileListener");
            PropertyConfigFile.loadProperties();
            ProcessFileListener.setProcessFile(new ProcessFile(ProcessFile.class.getSimpleName()));
            processFile.start();
        } catch (Throwable ta) {
            logger.info("Init context NOT OK", ta);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            logger.info("Stop contextDestroyed in ProcessFileListener");
            if (processFile != null) {
                processFile.stop();
            }

            ProcessFileListener.setProcessFile(null);
        } catch (Throwable ta) {
            logger.info("Destroyed context NOT OK", ta);
        }
    }

}
