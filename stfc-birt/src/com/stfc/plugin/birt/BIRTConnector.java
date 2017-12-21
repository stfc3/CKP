/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.birt;

import java.util.logging.Level;
import javax.servlet.ServletContext;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import com.stfc.plugin.AbstractPluginConnector;
import com.viettel.eafs.util.ServletUtil;
import java.io.File;
import java.net.URL;

/**
 *
 * @author longh
 */
public class BIRTConnector extends AbstractPluginConnector {
    
    private IReportEngine engine;
    private EngineConfig engineConfig;
    private String birtLogsDir = "/birt-logs";
    
    @Override
    public void startup() throws Throwable {
        initBIRTEngine();
    }
    
    @Override
    public void shutdown() throws Throwable {
        destroyBIRTEngine();
    }
    
    private void initBIRTEngine() throws Throwable {
        /*
         engineConfig = new EngineConfig();
         engineConfig.setEngineHome(ServletUtil.getRealPath("/WEB-INF/lib"));
        
         String str = ServletUtil.getRealPath("/WEB-INF/lib");
         engineConfig.getAppContext().put(EngineConstants.PROJECT_CLASSPATH_KEY, str);
         engineConfig.getAppContext().put(EngineConstants.WEBAPP_CLASSPATH_KEY, str);
         engineConfig.getAppContext().put(EngineConstants.WORKSPACE_CLASSPATH_KEY, str);
        
         engineConfig.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, Thread.currentThread().getContextClassLoader());
         engineConfig.setLogConfig(ServletUtil.getRealPath(birtLogsDir), Level.FINE);

         Platform.startup(engineConfig);
         IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);

         engine = factory.createReportEngine(engineConfig);
         */

        //new
        ServletContext sc = ServletUtil.getServletContext();
        engineConfig = new EngineConfig();
        //dongdv3 bo sung 28/01/2016 disable log BIRT
//        engineConfig.setLogConfig("", Level.OFF);
        //end
        engineConfig.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, Thread.currentThread().getContextClassLoader());
        //if you are using 3.7 POJO Runtime no need to setEngineHome
        engineConfig.setEngineHome("");
        IPlatformContext context = new PlatformServletContext(sc);
        engineConfig.setPlatformContext(context);
        
        File file = new File(ServletUtil.getRealPath(BIRTOption.getBirtFontsConfigFile()));
        URL url = file.toURI().toURL();
        engineConfig.setFontConfig(url);
        
        Platform.startup(engineConfig);
        
        IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
        engine = factory.createReportEngine(engineConfig);
        //--new

    }
    
    private void destroyBIRTEngine() throws Throwable {
        if (engine == null) {
            return;
        }
        
        engine.destroy();
        Platform.shutdown();
        engine = null;
    }

    //////
    public IReportEngine getEngine() {
        return engine;
    }
    
    public void setEngine(IReportEngine engine) {
        this.engine = engine;
    }
    
    public EngineConfig getEngineConfig() {
        return engineConfig;
    }
    
    public void setEngineConfig(EngineConfig engineConfig) {
        this.engineConfig = engineConfig;
    }
    
    public String getBirtLogsDir() {
        return birtLogsDir;
    }
    
    public void setBirtLogsDir(String birtLogsDir) {
        this.birtLogsDir = birtLogsDir;
    }
}
