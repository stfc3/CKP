/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.birt;

import com.viettel.eafs.util.ServletUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

/**
 *
 * @author longh
 */
public class BIRTService {

    private BIRTUtil util;
//    private String BIRT_RESOURCES_PATH = "/birt-resources";
    private static final Logger logger = Logger.getLogger(BIRTService.class);

    public void runAndRenderXLS(String reportCode, HashMap parameterMap, OutputStream out) throws Throwable {
        IReportRunnable design = getUtil().getReportDesign(reportCode);

        EXCELRenderOption options = getUtil().getEXCELRenderOption();
        options.setOutputStream(out);

        if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_spudsoft())) {
            getUtil().donotRepeatColumnHeader(design);

            options.setEmitterID(BIRTOption.getBirtExcelEmitter_spudsoft_id());
            options.setOutputFormat(BIRTOption.getBirtExcelEmitter_spudsoft_format());

            options.setOption("ExcelEmitter.SingleSheet", true);
//            options.setOption("ExcelEmitter.AutoFilter", true);
//            options.setOption("ExcelEmitter.AutoColWidthsIncludeTableHeader", true);
//            options.setOption("ExcelEmitter.FreezePanes", true);
//            options.setWrappingText(true);
            //options.setOption("ExcelEmitter.SingleSheetWithPageBreaks", true);
        } else if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_native())) {

            options.setOption(IExcelRenderOption.OFFICE_VERSION, BIRTOption.getBirtExcelEmitter_native_version());
            options.setOption(IRenderOption.EMITTER_ID, BIRTOption.getBirtExcelEmitter_native_id());
        }

        IRunAndRenderTask task = getEngine().createRunAndRenderTask(design);

        try {
            task.setParameterValues(parameterMap);
            task.validateParameters();

            task.setRenderOption(options);

            task.run();

        } finally {

            task.close();
        }
    }
//dongdv bo sung displayText cho ten don vi

    public void runAndRenderHTML(String reportCode, HashMap parameterMap, OutputStream out, String depName) throws Throwable {
        IRunAndRenderTask task = getUtil().getRunAndRenderTask(reportCode);

        task.getReportRunnable().getDesignHandle().getDesignHandle().setImageDPI(400);
        try {
            task.setParameterValues(parameterMap);
            task.setParameterDisplayText("dept_para_tree", depName);
            task.validateParameters();

            HTMLRenderOption options = getUtil().getHTMLRenderOption();
            options.setOutputStream(out);
            options.setImageHandler(new HTMLServerImageHandler());
            task.setRenderOption(options);

            task.run();

        } finally {
            task.close();
        }
    }

//    public void runAndCreateDocument(String reportCode, String uniqueKey, long idInSession, HashMap parameterMap) throws Throwable {
//        IRunTask runTask = getUtil().getRunTask(reportCode);
//
////        parameterMap.put(BIRTOption.getBirtParamsLanguage(), langLocale);
//
//        try {
//            runTask.setParameterValues(parameterMap);
//            runTask.validateParameters();
//
//            getUtil().forceCreateReportDocument(runTask, reportCode, uniqueKey, idInSession);
//
//        } finally {
//            runTask.close();
//        }
//    }
    //trungtd: cache theo ham moi// dongdv bo sung tham số cache
    public void runAndCreateDocument(String reportCode, HashMap parameterMap, HashMap parameterDisplayMap, boolean isCache) throws Throwable {

        long startTime = System.currentTimeMillis();
        IRunTask runTask = getUtil().getRunTask(reportCode);
        System.out.println("========> time get RunTask: " + (System.currentTimeMillis() - startTime) / 1000 + " s");

//        parameterMap.put(BIRTOption.getBirtParamsLanguage(), langLocale);
        try {
            if (parameterDisplayMap != null) {
                for (Object keyParamDisplay : parameterDisplayMap.keySet()) {
                    String paramName = String.valueOf(keyParamDisplay);
                    String paramDisplayText = String.valueOf(parameterDisplayMap.get(keyParamDisplay));
                    runTask.setParameterDisplayText(paramName, paramDisplayText);
                }
            }
            runTask.setParameterValues(parameterMap);
            runTask.validateParameters();
            //create ket
            String keyCache = getUtil().createKeyCache(reportCode, parameterMap);

            startTime = System.currentTimeMillis();
            getUtil().forceCreateReportDocument(runTask, reportCode, keyCache, isCache);
            System.out.println("========> time render DOC: " + (System.currentTimeMillis() - startTime) / 1000 + " s");

        } finally {
            runTask.close();
        }
    }

//    public long renderHTMLFromDocument(String reportCode, String uniqueKey, long idInSession, long _pageNumber, OutputStream out) throws Throwable {
//        if (!getUtil().isDocumentExisted(reportCode, uniqueKey, idInSession)) {
//            throw new Exception("ReportDocument associated with the reportCode no longer exist");
//        }
//
//        IReportDocument document = getUtil().getReportDocument(reportCode, uniqueKey, idInSession);
//        IRenderTask renderTask = getEngine().createRenderTask(document);
//        long totalPage, pageNumber;
//
//        try {
//
//
//            totalPage = renderTask.getTotalPage();
//
//            if (_pageNumber > 0) {
//                pageNumber = (_pageNumber > totalPage) ? 1 : _pageNumber;
//                renderTask.setPageNumber(pageNumber);
//            }
//
//            HTMLRenderOption options = getUtil().getHTMLRenderOption();
//            options.setOutputStream(out);
//            
//            options.setImageHandler(new HTMLServerImageHandler());
//            options.setBaseImageURL(ServletUtil.getContextPath() + BIRTOption.getBirtImagesDir());
//            options.setImageDirectory(util.getRealPath(BIRTOption.getBirtImagesDir()));
//
//            renderTask.setRenderOption(options);
//
//            renderTask.render();
//
//        } finally {
//            renderTask.close();
//            document.close();
//        }
//
//        return totalPage;
//    }
    //ham moi
    public long renderHTMLFromDocument(String keyCache, long _pageNumber, OutputStream out) throws Throwable {
        System.out.println("keyCacheError" + keyCache);
        if (!getUtil().isDocumentExisted(keyCache)) {
            throw new Exception("ReportDocument associated with the reportCode no longer exist");
        }
        IReportDocument document = getUtil().getReportDocument(keyCache);
        document.getReportDesign().setImageDPI(96);
        IRenderTask renderTask = getEngine().createRenderTask(document);
        long totalPage, pageNumber;

        try {

            totalPage = renderTask.getTotalPage();

            if (_pageNumber > 0) {
                pageNumber = (_pageNumber > totalPage) ? 1 : _pageNumber;
                renderTask.setPageNumber(pageNumber);
            }

            HTMLRenderOption options = getUtil().getHTMLRenderOption();
            options.setOutputStream(out);
//            options.setOutputFileName(util.getRealPath(BIRTOption.getBirtOutputDir())+"/dongtest.html");

            options.setImageHandler(new HTMLServerImageHandler());
            options.setBaseImageURL(ServletUtil.getContextPath() + BIRTOption.getBirtImagesDir());
            options.setImageDirectory(util.getRealPath(BIRTOption.getBirtImagesDir()));

//            getEngine().getConfig().getEmitterConfigs().put(options.getOutputFormat(), options);
//            options.setEnableAgentStyleEngine(true);
//            options.setSupportedImageFormats("PNG;GIF;JPG;BMP;SWF;SVG");
//            //dongdv
//            options.setBirtJsUrl(ServletUtil.getContextPath()+BIRTOption.getBirtReportResourcesDir()+"/testScript.js");
//            System.out.println("URL JS: "+ServletUtil.getContextPath()+BIRTOption.getBirtReportResourcesDir()+"/testScript.js");
//            //
            renderTask.setRenderOption(options);

            renderTask.render();

        } finally {
            renderTask.close();
            document.close();
        }

        return totalPage;
    }

    public long renderPDFFromDocument(String keyCache, long _pageNumber, OutputStream out) throws Throwable {
        System.out.println("keyCacheError" + keyCache);
        if (!getUtil().isDocumentExisted(keyCache)) {
            throw new Exception("ReportDocument associated with the reportCode no longer exist");
        }
        IReportDocument document = getUtil().getReportDocument(keyCache);
        document.getReportDesign().setImageDPI(400);
        IRenderTask renderTask = getEngine().createRenderTask(document);
        long totalPage, pageNumber;

        try {

            totalPage = renderTask.getTotalPage();

            if (_pageNumber > 0) {
                pageNumber = (_pageNumber > totalPage) ? 1 : _pageNumber;
                renderTask.setPageNumber(pageNumber);
            }

            PDFRenderOption options = new PDFRenderOption();
            options.setOutputStream(out);
            options.setSupportedImageFormats("PNG;GIF;JPG;BMP;SWF;SVG");
//            options.setOption(PDFRenderOption.PAGE_OVERFLOW,
//                    IPDFRenderOption.ENLARGE_PAGE_SIZE);
            options.setImageHandler(new HTMLServerImageHandler());
            options.setOption(PDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.FIT_TO_PAGE_SIZE);
            options.setOption(IRenderOption.HTML_PAGINATION, Boolean.FALSE);
            options.setOutputFormat(RenderOption.OUTPUT_FORMAT_PDF);

            renderTask.setRenderOption(options);
            renderTask.render();

        } finally {
            renderTask.close();
            document.close();
        }

        return totalPage;
    }
//    public long renderXLSFromDocument(String reportCode, String uniqueKey, long idInSession, long _pageNumber, OutputStream out) throws Throwable {
//        if (!getUtil().isDocumentExisted(reportCode, uniqueKey, idInSession)) {
//            throw new Exception("ReportDocument associated with the reportCode no longer exist");
//        }
//
//        IReportDocument document = getUtil().getReportDocument(reportCode, uniqueKey, idInSession);
//        IRenderTask renderTask = getEngine().createRenderTask(document);
//        long totalPage, pageNumber;
//
//        try {
//            totalPage = renderTask.getTotalPage();
//
//            if (_pageNumber > 0) {
//                pageNumber = (_pageNumber > totalPage) ? 1 : _pageNumber;
//                renderTask.setPageNumber(pageNumber);
//            }
//
//            EXCELRenderOption options = util.getEXCELRenderOption();
//            options.setOutputStream(out);
//
//            if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_spudsoft())) {
//                getUtil().donotRepeatColumnHeader(document.getReportRunnable());
//
//                options.setEmitterID(BIRTOption.getBirtExcelEmitter_spudsoft_id());
//                options.setOutputFormat(BIRTOption.getBirtExcelEmitter_spudsoft_format());
//
//                options.setOption("ExcelEmitter.SingleSheet", true);
//            } else if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_native())) {
//
//                options.setOption(IExcelRenderOption.OFFICE_VERSION, BIRTOption.getBirtExcelEmitter_native_version());
//                options.setOption(IRenderOption.EMITTER_ID, BIRTOption.getBirtExcelEmitter_native_id());
//            }
//
//            renderTask.setRenderOption(options);
//            renderTask.render();
//
//        } finally {
//            renderTask.close();
//            document.close();
//        }
//
//        return totalPage;
//    }

    public long renderXLSFromDocument(String keyCache, long _pageNumber, OutputStream out) throws Throwable {
        if (!getUtil().isDocumentExisted(keyCache)) {
            throw new Exception("ReportDocument associated with the reportCode no longer exist");
        }

        IReportDocument document = getUtil().getReportDocument(keyCache);
        document.getReportDesign().setImageDPI(400);
        IRenderTask renderTask = getEngine().createRenderTask(document);
        long totalPage, pageNumber;

        try {
            totalPage = renderTask.getTotalPage();

            if (_pageNumber > 0) {
                pageNumber = (_pageNumber > totalPage) ? 1 : _pageNumber;
                renderTask.setPageNumber(pageNumber);
            }

            EXCELRenderOption options = util.getEXCELRenderOption();
            options.setOutputStream(out);

            if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_spudsoft())) {
                getUtil().donotRepeatColumnHeader(document.getReportRunnable());

                options.setEmitterID(BIRTOption.getBirtExcelEmitter_spudsoft_id());
                options.setOutputFormat(BIRTOption.getBirtExcelEmitter_spudsoft_format());

                options.setOption("ExcelEmitter.SingleSheet", true);

//                options.setOption("ExcelEmitter.AutoFilter", true);
//                options.setOption("ExcelEmitter.AutoColWidthsIncludeTableHeader", true);
//                options.setOption("ExcelEmitter.FreezePanes", true);
                options.setWrappingText(true);

            } else if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_native())) {

                options.setOption(IExcelRenderOption.OFFICE_VERSION, BIRTOption.getBirtExcelEmitter_native_version());
                options.setOption(IRenderOption.EMITTER_ID, BIRTOption.getBirtExcelEmitter_native_id());
            }

            renderTask.setRenderOption(options);
            renderTask.render();

        } finally {
            renderTask.close();
            document.close();
        }

        return totalPage;
    }

//    public void renderCSVFromDocument(String reportCode, String uniqueKey, long idInSession, Writer out,String locale) throws Throwable {
//        if (!getUtil().isDocumentExisted(reportCode, uniqueKey, idInSession)) {
//            throw new Exception("ReportDocument associated with the reportCode no longer exist");
//        }
//
////        String locale="en_US";
//        IReportDocument document = getUtil().getReportDocument(reportCode, uniqueKey, idInSession);
//        IDataExtractionTask iDataExtract = getUtil().getEngine().createDataExtractionTask(document);
////        String values;
////        String[] tmp;
////        String baseName = null;
////        String resource;
//
////        PrintStream ps = new PrintStream(out, true, "UTF-16");
//        //Get list of result sets		
//        ArrayList resultSetList = (ArrayList) iDataExtract.getResultSetList();
//        try {
//            if (resultSetList == null || resultSetList.isEmpty()) {
//                exportCSV(document, out, locale, null);
//            }
//
//            for (int i = 0, size = resultSetList.size(); i < size; i++) {
//                IResultSetItem resultItem = (IResultSetItem) resultSetList.get(i);
//                String dispName = resultItem.getResultSetName();
//                System.out.println("---------------------- ResultSetName = " + dispName);
//                iDataExtract.selectResultSet(dispName);
//
//                IExtractionResults iExtractResults = iDataExtract.extract();
////                IDataIterator iData = null;
//                exportCSV(document, out, locale, iExtractResults);
//            }
//        } finally {
//            iDataExtract.close();
//            document.close();
//        }
//    }
    public void renderCSVFromDocument(String keyCache, Writer out, String locale) throws Throwable {
        if (!getUtil().isDocumentExisted(keyCache)) {
            throw new Exception("ReportDocument associated with the reportCode no longer exist");
        }

//        String locale="en_US";
        IReportDocument document = getUtil().getReportDocument(keyCache);
        IDataExtractionTask iDataExtract = getUtil().getEngine().createDataExtractionTask(document);
//        String values;
//        String[] tmp;
//        String baseName = null;
//        String resource;

//        PrintStream ps = new PrintStream(out, true, "UTF-16");
        //Get list of result sets		
        ArrayList resultSetList = (ArrayList) iDataExtract.getResultSetList();
        try {
            if (resultSetList == null || resultSetList.isEmpty()) {
                exportCSV(document, out, locale, null);
            } else {
                for (int i = 0, size = resultSetList.size(); i < size; i++) {
                    IResultSetItem resultItem = (IResultSetItem) resultSetList.get(i);
                    String dispName = resultItem.getResultSetName();
                    System.out.println("---------------------- ResultSetName = " + dispName);
                    iDataExtract.selectResultSet(dispName);

                    IExtractionResults iExtractResults = iDataExtract.extract();
//                IDataIterator iData = null;
                    exportCSV(document, out, locale, iExtractResults);
                }
            }
        } finally {
            iDataExtract.close();
            document.close();
        }
    }

    private static List getDisplayedColumns(IReportDocument iReportDocument, Writer out) throws Throwable {
        List retListColumns = new ArrayList();
        ReportDesignHandle reportHandle = iReportDocument.getReportDesign();

        for (Iterator<DesignElementHandle> iter = reportHandle.getBody().iterator(); iter.hasNext();) {

            DesignElementHandle designElement = (DesignElementHandle) iter.next();

            if (designElement instanceof GridHandle) {
                GridHandle gridHandle = (GridHandle) designElement;

                SlotHandle rowSlotHandle = gridHandle.getRows();
                int rowCount = rowSlotHandle.getCount();
                RowHandle rowEleHandle;

                for (int j = 0; j < rowCount; j++) {
                    rowEleHandle = (RowHandle) rowSlotHandle.get(j);
                    SlotHandle cellsSlot = rowEleHandle.getCells();

                    CellHandle cellHander = (CellHandle) cellsSlot.get(0);
                    SlotHandle subCellHandle = (SlotHandle) cellHander.getContent();
                    DesignElementHandle subCellElement = subCellHandle.get(0);

                    if (subCellElement instanceof LabelHandle) {
                        System.out.println("Title: " + subCellElement.getDisplayProperty("text"));
                        out.write(subCellElement.getDisplayProperty("text"));
                        out.write("\n");
                    }

                    if (subCellElement instanceof TableHandle) {
                        TableHandle tableHandle = (TableHandle) subCellElement;
                        // Dongdv add header
                        SlotHandle headerSlot = tableHandle.getHeader();
                        RowHandle rowInHeader = (RowHandle) headerSlot.get(0);
                        SlotHandle cellsInHeader = rowInHeader.getCells();
                        int numCellsInHeader = cellsInHeader.getCount();
                        CellHandle cellHeaderHander;
                        StringBuilder strHeader = new StringBuilder();
                        for (int h = 0; h < numCellsInHeader; h++) {
                            cellHeaderHander = (CellHandle) cellsInHeader.get(h);
                            SlotHandle contentCellHeader = cellHeaderHander.getContent();
                            DesignElementHandle labelCell = contentCellHeader.get(0);
                            if (labelCell instanceof LabelHandle) {
                                strHeader.append(labelCell.getDisplayProperty("text"));
                                strHeader.append(BIRTOption.getBirtCSVDelimiter());

                            }
                        }
                        out.write(strHeader.toString());
                        out.write("\n");
                        // End dongdv
                        SlotHandle detailSlot = tableHandle.getDetail();

                        RowHandle rowTableHandle = (RowHandle) detailSlot.get(0);
                        SlotHandle cells = rowTableHandle.getCells();
                        int numCells = cells.getCount();
                        CellHandle cellHandleFinal;

                        for (int t = 0; t < numCells; t++) {
                            cellHandleFinal = (CellHandle) cells.get(t);
                            SlotHandle contentCell = cellHandleFinal.getContent();
                            DesignElementHandle dataCell = contentCell.get(0);
                            if (dataCell instanceof DataItemHandle) {
                                DataItemHandle dataItemHandle = (DataItemHandle) dataCell;
                                retListColumns.add(dataItemHandle.getProperty("resultSetColumn"));
                                System.out.println("" + dataItemHandle.getProperty("resultSetColumn"));
                            }
                        }
                    }
                }
            }
        }

        reportHandle.close();

        return retListColumns;
    }
    ////

    ///
    public String getExcelExt() {
        String ext;
        if (BIRTOption.getBirtExcelEmitter().equals(BIRTOption.getBirtExcelEmitter_spudsoft())) {
            ext = BIRTOption.getBirtExcelEmitter_spudsoft_format();
        } else {
            ext = "xls";
        }

        return ext;
    }

    //
    public String getCSVExt() {
        String ext = BIRTOption.getBirtCSVEmitter_default_format();
        return ext;
    }

    public BIRTUtil getUtil() {
        return util;
    }

    public void setUtil(BIRTUtil util) {
        this.util = util;
    }

    public IReportEngine getEngine() {
        return util.getEngine();
    }

    private void exportCSV(IReportDocument document, Writer out, String locale, IExtractionResults iExtractResults) throws IOException {
        String resource;
        String[] tmp;
        String values;
        String baseName = null;
        IDataIterator iData;
        ReportDesignHandle reportHandle = document.getReportDesign();
        ///get baseName for ResourceBundle
        resource = reportHandle.getIncludeResource();
        if (resource != null) {
            tmp = resource.split("/");
            if (tmp != null) {
                int length = tmp.length;
                baseName = tmp[length - 1];
            }
        }
        for (Iterator<DesignElementHandle> iter = reportHandle.getBody().iterator(); iter.hasNext();) {

            DesignElementHandle designElement = (DesignElementHandle) iter.next();

            if (designElement instanceof GridHandle) {
                GridHandle gridHandle = (GridHandle) designElement;

                SlotHandle rowSlotHandle = gridHandle.getRows();
                int rowCount = rowSlotHandle.getCount();
                RowHandle rowEleHandle;

                for (int j = 0; j < rowCount; j++) {
                    rowEleHandle = (RowHandle) rowSlotHandle.get(j);
                    SlotHandle cellsSlot = rowEleHandle.getCells();

                    CellHandle cellHander = (CellHandle) cellsSlot.get(0);
                    SlotHandle subCellHandle = (SlotHandle) cellHander.getContent();
                    DesignElementHandle subCellElement = subCellHandle.get(0);

                    if (subCellElement instanceof LabelHandle) {
                        System.out.println("Title: " + subCellElement.getDisplayProperty("text"));
                        out.write(subCellElement.getDisplayProperty("text"));
                        out.write("\n");
                    }
                    if (subCellElement instanceof TextItemHandle) {
                        values = getUtil().normalizationString(subCellElement.getDisplayProperty("content"));
                        out.write(getUtil().getValueByKey(values, baseName, locale, BIRTOption.getBirtReportResourcesDir()));
                        out.write("\n");
                    }

                    if (subCellElement instanceof TableHandle) {
                        TableHandle tableHandle = (TableHandle) subCellElement;
                        /////////get header//////////////
                        // Dongdv add header
                        SlotHandle headerSlot = tableHandle.getHeader();
                        RowHandle rowInHeader = (RowHandle) headerSlot.get(0);
                        SlotHandle cellsInHeader = rowInHeader.getCells();
                        int numCellsInHeader = cellsInHeader.getCount();
                        CellHandle cellHeaderHander;
                        StringBuilder strHeader = new StringBuilder();
                        for (int h = 0; h < numCellsInHeader; h++) {
                            cellHeaderHander = (CellHandle) cellsInHeader.get(h);
                            SlotHandle contentCellHeader = cellHeaderHander.getContent();
                            DesignElementHandle labelCell = contentCellHeader.get(0);
                            if (labelCell instanceof LabelHandle) {
                                strHeader.append(labelCell.getDisplayProperty("text"));
                                strHeader.append(BIRTOption.getBirtCSVDelimiter());

                            }
                            if (labelCell instanceof TextItemHandle) {
                                values = getUtil().normalizationString(labelCell.getDisplayProperty("content"));
                                strHeader.append(getUtil().getValueByKey(values, baseName, locale, BIRTOption.getBirtReportResourcesDir()));
                                strHeader.append(BIRTOption.getBirtCSVDelimiter());

                            }
                        }
                        out.write(strHeader.toString());
                        out.write("\n");
                        // End dongdv
                        //////End get header//////

                        ////////////////Get detail////////////
                        SlotHandle detailSlot = tableHandle.getDetail();

                        RowHandle rowTableHandle = (RowHandle) detailSlot.get(0);
                        SlotHandle cells = rowTableHandle.getCells();
                        int numCells = cells.getCount();
                        CellHandle cellHandleFinal;

                        try {
                            if (iExtractResults != null) {
                                iData = iExtractResults.nextResultIterator();

                                //iterate through the results
                                if (iData != null) {
                                    while (iData.next()) {
                                        ///detail////
                                        StringBuilder rowStr = new StringBuilder("");
                                        for (int t = 0; t < numCells; t++) {
                                            cellHandleFinal = (CellHandle) cells.get(t);
                                            SlotHandle contentCell = cellHandleFinal.getContent();
                                            DesignElementHandle detailCell = contentCell.get(0);
                                            if (detailCell instanceof DataItemHandle) {
                                                DataItemHandle dataItemHandle = (DataItemHandle) detailCell;
                                                String columnLabel = String.valueOf(dataItemHandle.getProperty("resultSetColumn"));
                                                Object value = iData.getValue(columnLabel);
                                                if (value != null) {
                                                    String strValue = String.valueOf(value);
                                                    strValue = strValue.replaceAll(BIRTOption.getBirtCSVDelimiter(), BIRTOption.getBirtCSVCharReplaceDelimiter());
                                                    rowStr.append(strValue);
                                                    rowStr.append(BIRTOption.getBirtCSVDelimiter());
                                                }

                                            }
                                        }

                                        out.write(rowStr.toString());
                                        out.write("\n");
                                    }
                                    /////footer///
                                    SlotHandle footerSlot = tableHandle.getFooter();

                                    RowHandle rowInFooter = (RowHandle) footerSlot.get(0);
                                    SlotHandle cellsFooter = rowInFooter.getCells();
                                    int numCellsFooter = cellsFooter.getCount();
                                    CellHandle cellFooterHander;
                                    //////// footer///////
                                    StringBuilder rowStrFooter = new StringBuilder("");
                                    for (int t = 0; t < numCellsFooter; t++) {
                                        cellFooterHander = (CellHandle) cellsFooter.get(t);
                                        SlotHandle contentCell = cellFooterHander.getContent();
                                        DesignElementHandle footerCell = contentCell.get(0);
                                        //get so colSpan cua 1 cell
                                        int numColumnEmpty = 1;
                                        if (cellFooterHander.getDisplayProperty("colSpan") != null) {
                                            numColumnEmpty = Integer.parseInt(cellFooterHander.getDisplayProperty("colSpan"));
                                        }
//                                                System.out.println("ColSpan:  " + cellFooterHander.getDisplayProperty("colSpan"));
                                        if (footerCell instanceof DataItemHandle) {
                                            DataItemHandle dataItemHandle = (DataItemHandle) footerCell;
                                            String columnLabel = String.valueOf(dataItemHandle.getProperty("resultSetColumn"));
                                            Object value = iData.getValue(columnLabel);
                                            if (value != null) {
                                                String strValue = String.valueOf(value);
                                                strValue = strValue.replaceAll(BIRTOption.getBirtCSVDelimiter(), BIRTOption.getBirtCSVCharReplaceDelimiter());
                                                rowStrFooter.append(strValue);
                                                rowStrFooter.append(BIRTOption.getBirtCSVDelimiter());
                                            }

                                        }
                                        if (footerCell instanceof LabelHandle) {
                                            rowStrFooter.append(footerCell.getDisplayProperty("text"));
                                            // them column tuong ung voi so colspan
                                            for (int k = 0; k < numColumnEmpty; k++) {
                                                rowStrFooter.append(BIRTOption.getBirtCSVDelimiter());
                                            }
                                        }
                                        if (footerCell instanceof TextItemHandle) {
                                            values = getUtil().normalizationString(footerCell.getDisplayProperty("content"));
                                            rowStrFooter.append(getUtil().getValueByKey(values, baseName, locale, BIRTOption.getBirtReportResourcesDir()));
                                            // them column tuong ung voi so colspan
                                            for (int k = 0; k < numColumnEmpty; k++) {
                                                rowStrFooter.append(BIRTOption.getBirtCSVDelimiter());
                                            }
                                        }
                                    }

                                    out.write(rowStrFooter.toString());
                                    out.write("\n");
                                    iData.close();

                                }

                            }
                        } catch (BirtException e) {
                            logger.error(e.getMessage(), e);
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        } catch (NumberFormatException e) {
                            logger.error(e.getMessage(), e);
                        } finally {
                            if (iExtractResults != null) {
                                iExtractResults.close();
                            }
                        }
                    }
                }
            }
        }
        reportHandle.close();
    }

    public void runAndRenderPDF(String reportCode, HashMap parameterMap, OutputStream out) throws Throwable {
        IRunAndRenderTask task = getUtil().getRunAndRenderTask(reportCode);

        try {
            task.setParameterValues(parameterMap);
            task.validateParameters();
            PDFRenderOption options = new PDFRenderOption();
            options.setOutputStream(out);
            options.setSupportedImageFormats("PNG;GIF;JPG;BMP;SWF;SVG");
            options.setImageHandler(new HTMLServerImageHandler());
            options.setOption(PDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.FIT_TO_PAGE_SIZE);
            options.setOption(PDFRenderOption.PAGE_OVERFLOW, IPDFRenderOption.OUTPUT_TO_MULTIPLE_PAGES);
            options.setOption(IRenderOption.HTML_PAGINATION, Boolean.FALSE);
            options.setOutputFormat(RenderOption.OUTPUT_FORMAT_PDF);
            task.setRenderOption(options);
//            task.setLocale(Locale.getDefault());
            task.run();

        } finally {
            task.close();
        }
    }

    /**
     * dongdv3 bo sung ngay 22/07/2015
     *
     * @param reportCode
     * @param parameterMap
     * @param parameterDisplayMap
     * @param isCache
     * @param report
     * @throws Throwable
     */
    public void runAndCreateDocument(String reportCode, HashMap parameterMap, HashMap parameterDisplayMap, boolean isCache, InputStream report) throws Throwable {
        IRunTask runTask = getUtil().getRunTask(report);

        System.out.println("runAndCreateDocument START ");
        try {
            if (parameterDisplayMap != null) {
                for (Object keyParamDisplay : parameterDisplayMap.keySet()) {
                    String paramName = String.valueOf(keyParamDisplay);
                    String paramDisplayText = String.valueOf(parameterDisplayMap.get(keyParamDisplay));
                    runTask.setParameterDisplayText(paramName, paramDisplayText);
                }
            }
            runTask.setParameterValues(parameterMap);
            runTask.validateParameters();
            //create ket
            String keyCache = getUtil().createKeyCache(reportCode, parameterMap);

            getUtil().forceCreateReportDocument(runTask, reportCode, keyCache, isCache);

        } catch (Throwable ex) {
            System.out.println("Lỗi: " + ex);
        } finally {
            runTask.close();
        }
    }
}
