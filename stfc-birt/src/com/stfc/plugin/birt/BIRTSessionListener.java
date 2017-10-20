/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.birt;

import com.viettel.eafs.util.FileUtil;
import com.viettel.eafs.util.ServletUtil;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

/**
 *
 * @author longh
 */
public class BIRTSessionListener implements HttpSessionListener {
    private static Logger logger = Logger.getLogger(BIRTSessionListener.class);
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        try {
            StringBuilder sb = new StringBuilder(BIRTOption.getBirtReportDocsDir());
            sb.append("/");
            sb.append(event.getSession().getId());
            
            String path = ServletUtil.getRealPath(sb.toString());
            FileUtil.delete(path);
            
            logger.info("Destroy session OK, path= " + path);
        } catch (Throwable ex) {
            logger.error("Destroy session " + event.getSession().getId() + " NOT OK: ", ex);
        }
    }
    
}
