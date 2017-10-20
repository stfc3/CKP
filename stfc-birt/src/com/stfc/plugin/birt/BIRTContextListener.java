/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.stfc.plugin.birt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

/**
 * 
 * @author longh@viettel.com.vn
 */
public class BIRTContextListener extends HttpServlet implements ServletContextListener {

    private static Logger logger = Logger.getLogger(BIRTContextListener.class);
    private ExecutorService executor;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            BIRTOption.loadProperties();

            executor = Executors.newFixedThreadPool(Integer.parseInt(BIRTOption.getBirtUtilWorkerCapacity()));
        } catch (Throwable ta) {
            logger.info("Init context NOT OK", ta);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            executor.shutdown();
        } catch (Throwable ta) {
            logger.info("Destroy context NOT OK", ta);
        }
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
