/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.birt;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

/**
 *
 * @author dmin
 */
public class BirtServlet extends HttpServlet {

    private IReportEngine birtReportEngine = null;
    private static final Logger logger = Logger.getLogger(BirtServlet.class);

    public BirtServlet() {
        super();
    }

    @Override
    public void destroy() {
        super.destroy();
        BirtEngine.destroyBirtEngine();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String reportName = request.getParameter(BirtConstant.PARAM_REPORT);
        String extentsion = request.getParameter(BirtConstant.PARAM_EXTENSION);
        String fileName = request.getParameter(BirtConstant.PARAM_FILE_NAME);
        Enumeration<String> paramNames = request.getParameterNames();
        HashMap param = new HashMap();
        while (paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            if (!BirtConstant.PARAM_REPORT.equals(key) && !BirtConstant.PARAM_EXTENSION.equals(key)
                    && !BirtConstant.PARAM_FILE_NAME.equals(key) && !BirtConstant.PARAM_TIME.equals(key)) {
                Object value = request.getParameter(key);
                param.put(key, value);
                logger.info("ParamerName: " + key + "-->" + value);
            }
        }
        IReportRunnable design;
        RenderOption options;
        try {
            ServletContext context = request.getSession().getServletContext();
            birtReportEngine = BirtEngine.getBirtEngine(context);

            if (BirtConstant.EXCEL_EXTENSION.equals(extentsion)) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xlsx\"");
                options = getEXCELOption(response);
            } else {
                options = getHTMLOption(context, response);
            }
            //Open report design
            design = birtReportEngine.openReportDesign(context.getRealPath(BirtConstant.BIRT_REPORTS) + File.separator + reportName);
            //create task to run and render report
            IRunAndRenderTask task = birtReportEngine.createRunAndRenderTask(design);
            task.setParameterValues(param);
            task.validateParameters();
            task.setRenderOption(options);
            //run report
            task.run();
            task.close();
        } catch (IOException | EngineException ex) {
            logger.error(ex.getMessage(), ex);

        }
    }

    private HTMLRenderOption getHTMLOption(ServletContext context, HttpServletResponse response) throws IOException {
        HTMLRenderOption options = new HTMLRenderOption();
        options.setBaseImageURL(BirtConstant.BIRT_IMAGES);
        options.setImageDirectory(context.getRealPath(BirtConstant.BIRT_IMAGES));
        options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
        options.setOutputStream(response.getOutputStream());
        return options;
    }

    private EXCELRenderOption getEXCELOption(HttpServletResponse response) throws IOException {
        EXCELRenderOption options = new EXCELRenderOption();
        options.setEmitterID(BirtConstant.EXCEL_EMITTER_ID);
        options.setOutputFormat(BirtConstant.EXCEL_EXTENSION);

        options.setOption(BirtConstant.EXCEL_EMITTER_OPTION, true);
        options.setOutputStream(response.getOutputStream());
        return options;
    }

    @Override
    public void init() throws ServletException {
        BirtEngine.initBirtConfig();
    }
}
