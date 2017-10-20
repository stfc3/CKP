/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.birt;

import com.stfc.plugin.process.DeleteFile;
import com.stfc.plugin.process.ProcessFileListener;
import com.stfc.plugin.process.PropertyConfigFile;
import com.viettel.eafs.util.FileUtil;
import com.viettel.eafs.util.ServletUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 *
 * @author longh
 */
public class BIRTUtil {

    private BIRTConnector connector;
    private HashMap<String, byte[]> cachedDesign = new HashMap<String, byte[]>();
    //private HashMap<String, String> cachedDocumentFile = new HashMap<String, String>();
//    private HashMap<String, HashMap> cachedParameters = new HashMap<String, HashMap>();
    private List<HashMap> cachedParameters = new ArrayList<HashMap>();
    private Boolean testMode;
    private HashMap<String, String> testReportDataMap;
    private static final Logger logger = Logger.getLogger(BIRTUtil.class);

    //[ System methods
    public void startup() throws Throwable {
        StringBuilder sb = new StringBuilder(ServletUtil.getContextPath());
        sb.append(BIRTOption.getBaseImgURL());

        BIRTOption.setBaseImgURL(sb.toString());

        execBoottrapReport();

        if (getTestMode()) {
            preloadReportsForTest();
        }
    }

    public void shutdown() throws Throwable {
        clearCachedResources();
    }

    public void reset() throws Throwable {
        clearCachedResources();
    }

    private void clearCachedResources() throws Throwable {
        cachedDesign.clear();
        //    cachedDocumentFile.clear();

        StringBuilder sb = new StringBuilder(BIRTOption.getBirtReportDocsDir());
        String path = ServletUtil.getRealPath(sb.toString());

        FileUtil.emptyDirectory(path);
    }
    //]

    //[ Performance obtimization utitlies
    private void preloadReportsForTest() throws Throwable {
        String path;
        HashMap<String, String> hm = getTestReportDataMap();

        /*
         HashMap<String, String> hm = new HashMap<String, String>();
        
         hm.put("data", "new_report");
         hm.put("data2", "new_report_para2");
         hm.put("data3", "new_report_para3");
         hm.put("param", "new_report_para");
         hm.put("param_lib", "new_report_para_lib");
         hm.put("param_toc", "new_report_para_toc");
         */

        for (String code : hm.keySet()) {
            path = getRealPath(BIRTOption.getBirtReportsDir() + "/" + hm.get(code) + "." + BIRTOption.getReportDesignExtension());
            getReportDesign(code, path);
        }
    }

    private void execBoottrapReport() throws Throwable {
        StringBuilder sb = new StringBuilder(BIRTOption.getBirtReportsDir());
        sb.append(BIRTOption.getBoottrapReportFolder());
        sb.append("/");
        sb.append(BIRTOption.getBoottrapReportCode());
        sb.append(".");
        sb.append(BIRTOption.getReportDesignExtension());

        getReportDesign(BIRTOption.getBoottrapReportCode(), getRealPath(sb.toString()));

        IRunAndRenderTask task = getRunAndRenderTask(BIRTOption.getBoottrapReportCode());

        try {
            sb = new StringBuilder(BIRTOption.getBirtOutputDir());
            sb.append(BIRTOption.getBoottrapReportFolder());
            sb.append("/");
            sb.append(BIRTOption.getBoottrapReportOutputFile());

            String outFile = getRealPath(sb.toString());

            HTMLRenderOption options = getHTMLRenderOption();
            options.setOutputFileName(outFile);
            task.setRenderOption(options);

        } finally {
            task.run();
            task.close();
        }
    }
    //]

    //[ Get RenderOptions
    public HTMLRenderOption getHTMLRenderOption() throws Throwable {
        HTMLRenderOption options = new HTMLRenderOption();
        options.setBaseImageURL(BIRTOption.getBaseImgURL());
        options.setImageDirectory(getRealPath(BIRTOption.getImgDir()));
        options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
        return options;
    }

    public EXCELRenderOption getEXCELRenderOption() throws Throwable {
        EXCELRenderOption options = new EXCELRenderOption();
        options.setOutputFormat("xls");
        return options;
    }
    //]

    //[ Get Tasks
//    public IDataExtractionTask getDataExtractionTask(String reportCode, String uniqueKey, long idInSession) throws Throwable {
//        return getEngine().createDataExtractionTask(getReportDocument(reportCode, uniqueKey, idInSession));
//    }
    public IDataExtractionTask getDataExtractionTask(String keyCache) throws Throwable {
        return getEngine().createDataExtractionTask(getReportDocument(keyCache));
    }

    public IGetParameterDefinitionTask getParameterDefinitionTask(String reportCode) throws Throwable {
        return getEngine().createGetParameterDefinitionTask(getReportDesign(reportCode));
    }

    public IRunTask getRunTask(String reportCode) throws Throwable {
        return getEngine().createRunTask(getReportDesign(reportCode));
    }

//    public IRenderTask getRenderTask(String reportCode, String uniqueKey, long idInSession) throws Throwable {
//        return getEngine().createRenderTask(getReportDocument(reportCode, uniqueKey, idInSession));
//    }
    public IRenderTask getRenderTask(String keyCache) throws Throwable {
        return getEngine().createRenderTask(getReportDocument(keyCache));
    }

    public IRunAndRenderTask getRunAndRenderTask(String reportCode) throws Throwable {
        return getEngine().createRunAndRenderTask(getReportDesign(reportCode));
    }
    //]

    //[ ReportDesign utilities
    public IReportRunnable getReportDesign(String reportCode) throws Throwable {
        byte[] bytes = lookupReportDesign(reportCode);
        IReportRunnable design = getEngine().openReportDesign(new ByteArrayInputStream(bytes));

        return design;
    }
    
    public void getReportDesign(String reportCode, byte[] bytes) throws Throwable {
        String content = new String(bytes, "UTF-8");
        //String pattern = "\\{\\[(\\w*)\\]\\}";
        
        Matcher m = Pattern.compile(BIRTOption.getBirtExpressionPattern()).matcher(content);
        while (m.find()) {
            content = content.replace(m.group(0), getRealPath(m.group(1)));
        }
        
//        content = content.replaceAll("_abc_", "Nghìn tỷ tỉ đồng năm tháng quý tuần");
        
//        System.out.println("longh: new birtutil 2 " + System.currentTimeMillis());
        //System.out.println(content);
        //System.out.println("longh: end xml");
        cacheReportDesign(reportCode, content.getBytes("UTF-8"));
    }

    //Coded by MinhCA
    //Get IreportRunnable with the replacement of default value for SHOP_ID Parameter
    private IReportRunnable getReportDesignWithDefaultReplacement(String reportCode, String defaultValue) throws Throwable {
        byte[] bytes = lookupReportDesign(reportCode);
        String content = new String(bytes,"UTF-8");
        Matcher m = Pattern.compile(BIRTOption.getBirtDefaultValueRegex()).matcher(content);
        while (m.find()) {
            content = content.replace(m.group(0), defaultValue);
        }
        byte[] contentBytes = content.getBytes("UTF-8");
        IReportRunnable design = getEngine().openReportDesign(new ByteArrayInputStream(contentBytes));

        return design;
    }
    //Get paramdefinition phuc vu cho viec lay param va set gia tri default cho truong shopid tren parameter

    public IGetParameterDefinitionTask getParameterDefinitionTask(String reportCode, String shopIDDefaultValue) throws Throwable {
        return getEngine().createGetParameterDefinitionTask(getReportDesignWithDefaultReplacement(reportCode, shopIDDefaultValue));
    }

    //dongdv3 create method for add, edit report
    public byte[] getReportDesign(String reportCodeNew, byte[] bytes, String reportCodeOld) throws Throwable {
        String content;// = new String(bytes);

        if (bytes != null) {
            content = new String(bytes);
            Matcher m = Pattern.compile(BIRTOption.getBirtExpressionPattern()).matcher(content);
            while (m.find()) {
                content = content.replace(m.group(0), getRealPath(m.group(1)));
            }
            //neu code cu trung code moi thi change content va luu vao cache
            if (reportCodeNew.equals(reportCodeOld)) {
                cacheReportDesign(reportCodeNew, content.getBytes());
            } //neu khong trung thi xoa report cu va them report moi vao cache
            else {
                removeCacheReportDesign(reportCodeOld);
                cacheReportDesign(reportCodeNew, content.getBytes());
            }
            return content.getBytes();
        } else {
            if (!reportCodeNew.equals(reportCodeOld)) {
                byte[] contentOld = lookupReportDesign(reportCodeOld);
                removeCacheReportDesign(reportCodeOld);
                cacheReportDesign(reportCodeNew, contentOld);
            }
            return null;
        }
    }

    //dongdv3 remove reportOld
    private synchronized void removeCacheReportDesign(String reportCodeOld) throws Throwable {
        cachedDesign.remove(reportCodeOld);
    }

    public void getReportDesign(String reportCode, String reportFile) throws Throwable {
        getReportDesign(reportCode, FileUtil.fileToBytes(reportFile));
    }

    private synchronized void cacheReportDesign(String reportCode, byte[] bytes) throws Throwable {
        cachedDesign.put(reportCode, bytes);
    }

    private byte[] lookupReportDesign(String reportCode) throws Throwable {
        return cachedDesign.get(reportCode);
    }
    //]

    //[ ReportDocument utilities
//    public IReportDocument getReportDocument(String reportCode, String uniqueKey, long idInSession) throws Throwable {
//        StringBuilder sb = getStringBuilderOfReportDocumentKey(reportCode, uniqueKey, idInSession);
//
//        String key = sb.toString();
//        String docOutputFile = lookupReportDocumentFile(key);
//
//        if (docOutputFile == null || docOutputFile.isEmpty() == true) {
//            sb.delete(0, sb.length());
//            sb.append("ReportDocument file not found, conditions =");
//            sb.append(" reportCode: ");
//            sb.append(reportCode);
//            sb.append(", idInSession: ");
//            sb.append(idInSession);
//
//            throw new Exception(sb.toString());
//        }
//
//        return getEngine().openReportDocument(docOutputFile);
//    }
    //trungtd: ham moi
    public IReportDocument getReportDocument(String keyCache) throws Throwable {
        String docOutputFile = lookupReportDocumentFile(keyCache);
        logger.info("get DOC: "+docOutputFile);
        StringBuilder sb = new StringBuilder();
        if (docOutputFile == null || docOutputFile.isEmpty() == true) {
            sb.append("ReportDocument file not found, keyCache =");
            sb.append(keyCache);
            throw new Exception(sb.toString());
        }

        return getEngine().openReportDocument(docOutputFile);
    }

//    public String forceCreateReportDocument(IRunTask task, String reportCode, String uniqueKey, long idInSession) throws Throwable {
//        StringBuilder sb2 = getStringBuilderOfReportDocumentKey(reportCode, uniqueKey, idInSession);
//
//        StringBuilder sb = new StringBuilder(BIRTOption.getBirtReportDocsDir());
//        sb.append("/");
//        sb.append(uniqueKey);
//        sb.append("/");
//        sb.append(sb2);
//        sb.append(".");
//        sb.append(BIRTOption.getReportDocumentExtension());
//
//        String key = sb2.toString();
//        String path = sb.toString();
//        String docOutputFile = ServletUtil.getRealPath(path);
//
//        task.run(docOutputFile);
//
//        cacheReportDocumentFile(key, docOutputFile);
//
//        return docOutputFile;
//    }
    //trungtd: cache theo ham moi dongdv them tham so cache
    public String forceCreateReportDocument(IRunTask task, String reportCode, String keyCache, boolean isCache) throws Throwable {
        StringBuilder sb = new StringBuilder(BIRTOption.getBirtReportDocsDir());
        String key = keyCache;
        addMapKey(key, isCache);

        String fileName = mapKey.get(keyCache);
        sb.append("/");
        sb.append(reportCode);
        sb.append("/");
        sb.append(fileName);
        sb.append(".");
        sb.append(BIRTOption.getReportDocumentExtension());


        String path = sb.toString();
        String docOutputFile = ServletUtil.getRealPath(path);

        task.run(docOutputFile);

        //dongdv
        cacheReportDocumentFile(key, docOutputFile,isCache);
        return docOutputFile;
    }

    private StringBuilder getStringBuilderOfReportDocumentKey(String reportCode, String uniqueKey, long idInSession) throws Throwable {
        StringBuilder sb = new StringBuilder(uniqueKey);
        sb.append("_");
        sb.append(reportCode);
        sb.append("_");
        sb.append(idInSession);

        return sb;
    }

//    public synchronized void cacheReportDocumentFile(String key, String documentFile) throws Throwable {
//        cachedDocumentFile.put(key, documentFile);
//    }
//
//    public String lookupReportDocumentFile(String key) throws Throwable {
//        return cachedDocumentFile.get(key);
//    }
    //]
    //[ Report Design utilities
    public void donotRepeatColumnHeader(String reportCode) throws Throwable {
        donotRepeatColumnHeader(getReportDesign(reportCode));
    }

    public void donotRepeatColumnHeader(IReportRunnable design) throws Throwable {
        ReportDesignHandle designHandle = (ReportDesignHandle) design.getDesignHandle();

        try {
            for (Iterator<DesignElementHandle> iter = designHandle.getBody().iterator(); iter.hasNext();) {
                donotRepeatColumnHeaderRecursive(iter.next());
            }
        } finally {

            designHandle.close();
        }

    }

    private void donotRepeatColumnHeaderRecursive(DesignElementHandle element) throws Throwable {
        GridHandle grid;
        CellHandle cell;
        DesignElementHandle nextElement;

        if (element instanceof GridHandle) {
            grid = (GridHandle) element;

            int rowSize = grid.getRows().getCount();
            int colSize = grid.getColumns().getCount();

            for (int i = 0; i < rowSize; i++) {
                for (int j = 0; j < colSize; j++) {
                    cell = grid.getCell(i, j);
                    nextElement = cell.getContent().get(0);

                    donotRepeatColumnHeaderRecursive(nextElement);
                }
            }

        } else if (element instanceof TableHandle) {

            ((TableHandle) element).setRepeatHeader(false);
        } else if (element instanceof ExtendedItemHandle) {

            if (!"Chart".equals(((ExtendedItemHandle) element).getExtensionName())) {
                element.setProperty("repeatColumnHeader", false);
            }
        }
    }
    //]

    /////
    public String getRealPath(String relPath) {
        return ServletUtil.getRealPath(relPath);
    }

//    public boolean isDocumentExisted(String reportCode, String uniqueKey, long idInSession) throws Throwable {
//        StringBuilder sb = getStringBuilderOfReportDocumentKey(reportCode, uniqueKey, idInSession);
//
//        logger.info(sb.toString());
//        String fileName = lookupReportDocumentFile(sb.toString());
//        logger.info(fileName);
//        return new File(fileName).exists();
//    }
    public boolean isDocumentExisted(String keyCache) throws Throwable {

        String fileName = lookupReportDocumentFile(keyCache);
        logger.info("Check DOC exit: "+fileName);
        File file = new File(fileName);
        return file.exists();
    }

    public IReportEngine getEngine() {
        return getConnector().getEngine();
    }

    public BIRTConnector getConnector() {
        return connector;
    }

    public void setConnector(BIRTConnector connector) {
        this.connector = connector;
    }

    public Boolean getTestMode() {
        return testMode;
    }

    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    public HashMap<String, String> getTestReportDataMap() {
        return testReportDataMap;
    }

    public void setTestReportDataMap(HashMap<String, String> testReportDataMap) {
        this.testReportDataMap = testReportDataMap;
    }
    /*
     * Dongdv3
     * Multilanguage for birt design for export csv
     * get value from key
     */

    public String getValueByKey(String key, String baseName, String locale, String path) throws MalformedURLException {
        String strReturn = null;
        File file = new File(ServletUtil.getRealPath(path));
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        Locale prefer_locale = locale.length() > 2
                ? new Locale(locale.substring(0, 2), locale.substring(3)) : new Locale(locale);
        if (baseName != null) {
            ResourceBundle bundle = ResourceBundle.getBundle(baseName, prefer_locale, loader);
            if (key != null && !"null".equals(key)) {
                try {
                    strReturn = bundle.getString(key);
                } catch (Throwable ex) {
                    logger.error(ex.getMessage(), ex);
                    return null;
                }
            }
            if (strReturn != null) {
                return strReturn;
            }
        }
        return strReturn;
    }

    public String normalizationString(String display) {
        //Lay chuoi trong dau ()
        Matcher m = Pattern.compile("\\(.*\\)").matcher(display);
        if (m.find()) {
            // get key
            m = Pattern.compile("[\\w.]+").matcher(m.group(0));
            if (m.find()) {
                return m.group(0);
            }
        }
        return display;
    }

    public String getValueParameter(String reportCode, String key, String locale) throws Throwable {

        IReportRunnable iReport = getReportDesign(reportCode);
        String valueParameter = null;
        ReportDesignHandle reportHandle = (ReportDesignHandle) iReport.getDesignHandle();
        String resource = reportHandle.getIncludeResource();
        String baseName = null;
        if (resource != null) {
            String[] tmp = resource.split("/");
            if (tmp != null) {
                int length = tmp.length;
                baseName = tmp[length - 1];
            }
        }
        if (key != null) {
            if (!key.contains("reportContext.getMessage")) {
                return key;
            } else {
                String value = normalizationString(key);
                valueParameter = getValueByKey(value, baseName, locale, BIRTOption.getBirtReportResourcesDir());
            }
        }
        reportHandle.close();
        return valueParameter;
    }

    public HashMap getCachedParamMap(String name) {
        HashMap hashRet;
        for (int i = cachedParameters.size() - 1; i >= 0; i--) {
            hashRet = cachedParameters.get(i);
            String paramName = String.valueOf(hashRet.get(BIRTCodes.KEY_NAME));

            if (name.equals(paramName)) {
                synchronized (lock) {
                    cachedParameters.remove(i);
                    cachedParameters.add(hashRet);
                }
                return hashRet;
            }
        }
        return null;
    }

    public HashMap getCachedParamMap(String name, boolean showInfo) {
        HashMap ret = getCachedParamMap(name);
        if (showInfo) {
            printParamMapInfo();
        }
        return ret;
    }

    public void storeParamMapIntoCache(String name, HashMap paramMap) throws Throwable {
        if (name == null) {
            throw new Exception("Cache Param: Param's name must not be null!");
        }
        //Clear paramMap
        clearParamMap();

        //append paramMap to the top of list
        synchronized (lock) {
            cachedParameters.add(paramMap);
        }
    }

    public void removeParamFromCache(String name) {
        //remove param from List if it existed
        int index = indexOfParamInCache(name);
        if (index != -1) {
            synchronized (lock) {
                cachedParameters.remove(index);
            }
        }
    }

    public void clearParamMap() {
        int size = cachedParameters.size();

        if (size >= Integer.parseInt(BIRTOption.getMaxSizeOfCachedParam())) {
            synchronized (lock) {
                cachedParameters = cachedParameters.subList(Integer.parseInt(BIRTOption.getNumOfRemovableObjs()) - 1, size);
            }
        }
    }

    public void clearParamMap(boolean showInfo) {
        clearParamMap();
        if (showInfo) {
            printParamMapInfo();
        }
    }

    private void printParamMapInfo() {
        logger.info("======  Param Map Info  =======");
        HashMap hashMap;
        String paramName;
        for (int i = 0; i < cachedParameters.size(); i++) {
            hashMap = cachedParameters.get(i);
            paramName = String.valueOf(hashMap.get(BIRTCodes.KEY_NAME));
            logger.info(paramName);
        }
        logger.info("======End Param Map Info ======");
    }

    private int indexOfParamInCache(String name) {
        for (int i = 0; i < cachedParameters.size(); i++) {
            HashMap hashMap = cachedParameters.get(i);
            String paramName = String.valueOf(hashMap.get(BIRTCodes.KEY_NAME));
            if (paramName.equals(name)) {
                return i;
            }
        }
        return -1;
    }
    //trungtd: them cachURL
    private static List<HashMap> cachMapUrl = new ArrayList<HashMap>();
    //map keycach voi id tu sinh de tao file.
    private static HashMap<String, String> mapKey = new HashMap<String, String>();
    private static final Object lock = new Object();

    //trungtd: update 13/11: xu ly loi bat dong bo
    //Them dk: check file vat ly. Neu ko ton tai file vat ly thi xoa keycache di
    public static boolean checkUrlCache(String key) {
        try {
            if (cachMapUrl == null || cachMapUrl.isEmpty()) {
                return false;
            }
            for (int i = 0; i < cachMapUrl.size(); i++) {
                if (cachMapUrl.get(i).get(key) != null) {
                    HashMap<String, String> has = cachMapUrl.get(i);
                    String urlFile = has.get(key);
                    boolean existsFile = isExistFile(urlFile);
                    if (!existsFile) {
                        //Xoa key-cache
                        synchronized (lock) {
                            HashMap<String, String> removeKey = cachMapUrl.remove(i);
                            logger.info("Remove key " + key + ", fileDocument  = " + removeKey.get(key));
                        }
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }
    //dongdv them tham so cau hinh cho phep cache hay khong

    public static boolean checkUrlCache(String key, boolean isCache) {
        try {
            if (cachMapUrl == null || cachMapUrl.isEmpty()) {
                return false;
            }
            for (int i = 0; i < cachMapUrl.size(); i++) {
                if (cachMapUrl.get(i).get(key) != null) {
                    HashMap<String, String> has = cachMapUrl.get(i);
                    String urlFile = has.get(key);
                    boolean existsFile = isExistFile(urlFile);
                    if (!existsFile) {
                        //Xoa key-cache
                        synchronized (lock) {
                            HashMap<String, String> removeKey = cachMapUrl.remove(i);
                            logger.info("Remove key " + key + ", fileDocument  = " + removeKey.get(key));
                        }
                        return false;
                    } else {
                        if (isCache) {
                            return true;
                        } else {// neu cau hinh k cache thi xoa key va return false de tao lai doccument
                            synchronized (lock) {
                                cachMapUrl.remove(i);
                            }
                            return false;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    public static boolean isExistFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        return new File(filePath).exists();
    }

    public void cacheReportDocumentFile(String key, String documentFile) throws Throwable {
        //cache lan dau tien
        if (cachMapUrl == null || cachMapUrl.isEmpty()) {
            synchronized (lock) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(key, documentFile);
                cachMapUrl.add(map);
                return;
            }
        }
        //kiem tra tra cache lai gia tri. 
        for (int i = 0; i < cachMapUrl.size(); i++) {
            if (cachMapUrl.get(i).containsKey(key)) {
                return;
            }
        }
        //Kiem tra da qua cache size chua        
        int sizeCache = cachMapUrl.size();
        if (sizeCache >= Integer.valueOf(PropertyConfigFile.getMaxSizeOfCacheDocument())) {
            //So doi tuong can xoa. Xoa theo list tu duoi len
            int numDel = Integer.valueOf(PropertyConfigFile.getNumOfRemoveDocumentObjs());
            if (numDel > sizeCache) {
                numDel = sizeCache;
//            }
//            if (numDel <= sizeCache) {
                List<HashMap> lstDel = cachMapUrl.subList(0, numDel);
                synchronized (lock) {
                    cachMapUrl = cachMapUrl.subList(numDel, sizeCache);
                }

                if (lstDel != null && !lstDel.isEmpty()) {
                    for (int i = 0; i < lstDel.size(); i++) {
                        HashMap<String, String> has = lstDel.get(i);
                        for (String keytmp : has.keySet()) {
                            //remove mapKey 
                            mapKey.remove(keytmp);
                        }
                    }
                    //Goi Process xoa file. (Chi add vao cache. tien trinh se xoa file sau)
                    DeleteFile.callDeleteFile(ProcessFileListener.getProcessFile(), lstDel);
                }
            } else {
                logger.error("Cau hinh sai");
            }
        }



        //thuc hien cache gia tri
        synchronized (lock) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(key, documentFile);
            cachMapUrl.add(map);
        }

    }
    ///dongdv them tham so cho phep cache hay khong

    public void cacheReportDocumentFile(String key, String documentFile, boolean isCache) throws Throwable {
        //cache lan dau tien
        if (cachMapUrl == null || cachMapUrl.isEmpty()) {
            synchronized (lock) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(key, documentFile);
                cachMapUrl.add(map);
                return;
            }
        }
        if (isCache) {
            //kiem tra tra cache lai gia tri. 
            for (int i = 0; i < cachMapUrl.size(); i++) {
                if (cachMapUrl.get(i).containsKey(key)) {
                    return;
                }
            }
            //Kiem tra da qua cache size chua        
            int sizeCache = cachMapUrl.size();
            if (sizeCache >= Integer.valueOf(PropertyConfigFile.getMaxSizeOfCacheDocument())) {
                //So doi tuong can xoa. Xoa theo list tu duoi len
                int numDel = Integer.valueOf(PropertyConfigFile.getNumOfRemoveDocumentObjs());
                if (numDel > sizeCache) {
                    numDel = sizeCache;
//                }
//                if (numDel <= sizeCache) {
                    List<HashMap> lstDel = cachMapUrl.subList(0, numDel);
                    synchronized (lock) {
                        cachMapUrl = cachMapUrl.subList(numDel, sizeCache);
                    }

                    if (lstDel != null && !lstDel.isEmpty()) {
                        for (int i = 0; i < lstDel.size(); i++) {
                            HashMap<String, String> has = lstDel.get(i);
                            for (String keytmp : has.keySet()) {
                                //remove mapKey 
                                mapKey.remove(keytmp);
                            }
                        }
                        //Goi Process xoa file. (Chi add vao cache. tien trinh se xoa file sau)
                        DeleteFile.callDeleteFile(ProcessFileListener.getProcessFile(), lstDel);
                    }
                } else {
                    logger.error("Cau hinh sai");
                }
            }



            //thuc hien cache gia tri
            synchronized (lock) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(key, documentFile);
                cachMapUrl.add(map);
            }
        } else {
            synchronized (lock) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(key, documentFile);
                cachMapUrl.add(map);
            }
        }

    }

//    public String lookupReportDocumentFile(String key) throws Throwable {
//        String documentFile = "";
//        for (int i = 0; i < cachMapUrl.size(); i++) {
//            if (cachMapUrl.get(i).containsKey(key)) {
//                //dao vi tri moi search len dau
//                synchronized (lock) {
//                    HashMap<String, String> newSearch = cachMapUrl.remove(i);
//                    documentFile = newSearch.get(key);
//                    cachMapUrl.add(newSearch);
//                }
//
//                return documentFile;
//            }
//        }
//        return null;
//
//    }
    //dongdv3 07/12/2015 fix loi khong doc dung file moi nhat
    public String lookupReportDocumentFile(String key) throws Throwable {
        String documentFile;
        for (int i = cachMapUrl.size()-1; i >=0; i--) {
//        for (int i = 0; i < cachMapUrl.size(); i++) {
            if (cachMapUrl.get(i).containsKey(key)) {
                //dao vi tri moi search len dau
                synchronized (lock) {
                    HashMap<String, String> newSearch = cachMapUrl.remove(i);
                    documentFile = newSearch.get(key);
                    cachMapUrl.add(newSearch);
                }

                return documentFile;
            }
        }
        return null;

    }

    public static String genStringRamdom() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
//dongdv them tham so cache
    public static void addMapKey(String key, boolean isCache) {
        while (true) {
            String value = genStringRamdom();
            if (mapKey == null || mapKey.isEmpty()) {
                synchronized (lock) {
                    mapKey.put(key, value);
                }
                break;
            }
            if (!mapKey.containsValue(value) && !mapKey.containsKey(key) && isCache) {
                synchronized (lock) {
                    mapKey.put(key, value);
                }
                break;
            }
            /////////
            if (!mapKey.containsValue(value) && !isCache) {
                synchronized (lock) {
                    mapKey.put(key, value);
                }
                break;
            }
            break;
        }
    }

    public static String createKeyCache(String reportCode, HashMap parameterMap) {
        StringBuilder keyCache = new StringBuilder();
        String seperator = "-";
        if (parameterMap != null) {
            Iterator it = parameterMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry mEntry = (Map.Entry) it.next();
                String key = mEntry.getKey().toString();
                Object objValue = parameterMap.get(key);
                if(objValue instanceof String[]){
                    System.out.println("call here arr");
                }
                if(objValue instanceof String){
                    System.out.println("call here");
                }
                String value = String.valueOf(parameterMap.get(key));
                keyCache.append(reportCode);
                keyCache.append(seperator);
                keyCache.append(key);
                keyCache.append(seperator);
                keyCache.append(value);
            }
        }
        return keyCache.toString();
    }
    /**
     * dongdv3 bo sung ngay: 21/07/2015
     * @param report
     * @return
     * @throws Throwable 
     */
    public IReportRunnable getReportDesign(InputStream report) throws Throwable {
        IReportRunnable design = getEngine().openReportDesign(report);
        return design;
    }
    /**
     * dongdv3 bo sung ngay: 21/07/2015
     * @param report
     * @return
     * @throws Throwable 
     */
    public IGetParameterDefinitionTask getParameterDefinitionTask(InputStream report) throws Throwable {
        return getEngine().createGetParameterDefinitionTask(getReportDesign(report));
    }
    /**
     * dongdv3 bo sung ngay: 21/07/2015
     * @param report
     * @return
     * @throws Throwable 
     */
    public IRunTask getRunTask(InputStream report) throws Throwable {
        return getEngine().createRunTask(getReportDesign(report));
    }

    public static List<HashMap> getCachMapUrl() {
        return cachMapUrl;
    }

    public static HashMap<String, String> getMapKey() {
        return mapKey;
    }
    
}
