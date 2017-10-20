/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bi.plugin.birt;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 *
 * @author longh
 */
public class BIRTOption {
    private static String propertiesFilePath = "birt.vbipluggin.properties"; 
    
    private static String boottrapReportCode;
    private static String boottrapReportOutputFile;
    private static String boottrapReportFolder;
    
    private static String birtReportsDir;
    private static String birtReportDocsDir;
    private static String birtReportLibraryDir;
    private static String birtOutputDir;
    private static String birtImagesDir;
    private static String birtFontsConfigFile;
    
    private static String baseImgURL;//Khi chay that se duoc chen them contextpath vao dau
    private static String imgDir;
    
    private static String reportDocumentExtension;
    private static String reportDesignExtension;
    private static String outputExcelFormat;
    
    private static String birtExpressionPattern;
    private static String birtUtilWorkerCapacity;
    
    private static String birtExcelEmitter;
    private static String birtExcelEmitter_native = "native";
    private static String birtExcelEmitter_native_id;
    private static String birtExcelEmitter_native_format;
    private static String birtExcelEmitter_native_version;
    
    private static String birtExcelEmitter_spudsoft = "spudsoft";
    private static String birtExcelEmitter_spudsoft_id;
    private static String birtExcelEmitter_spudsoft_format;
    
    //CSV delimiter Configuration
    private static String birtCSVDelimiter;
    private static String birtCSVCharReplaceDelimiter;
    
    //CSV Emitter
//    private static String birtCSVEmitter;
//    private static String birtCSVEmitter_default = "default";
//    private static String birtCSVEmitter_default_id;
    private static String birtCSVEmitter_default_format;
    //birt params language
    private static String birtParamsLanguage;
    private static String birtReportResourcesDir;
    
    private static String datasourceConnectorID;
    
    private static String birtReportAreaID;
    
    private static String birtParamColumn;
    private static String birtParamSpace;
    
    private static String birtDefaultValueRegex;
    //Cau hinh tham so cho cache parameters
    private static String maxSizeOfCachedParam = "100";
    private static String numOfRemovableObjs = "5";
    
    private static String isCacheParam = "true";
    
    private static String birtParamDept ;
    private static String birtParamDeptTree ;
    private static String birtParamDate ;
    private static String birtParamTypeTree ;
    private static String birtParamTree ;
    //dongdv3
    private static String birtParamServiceTree ;
    private static String birtParamIsMap ;
    
    private static String report_code_fix_error_data="KD_TD_REPORT_DATA_ERROR";

    public static String getBoottrapReportCode() {
        return boottrapReportCode;
    }

    public static String getBoottrapReportOutputFile() {
        return boottrapReportOutputFile;
    }

    public static String getBoottrapReportFolder() {
        return boottrapReportFolder;
    }

    public static String getBirtReportsDir() {
        return birtReportsDir;
    }

    public static String getBirtReportDocsDir() {
        return birtReportDocsDir;
    }

    public static String getBirtReportLibraryDir() {
        return birtReportLibraryDir;
    }

    public static String getBirtOutputDir() {
        return birtOutputDir;
    }

    public static String getBirtImagesDir() {
        return birtImagesDir;
    }

    public static String getBirtFontsConfigFile() {
        return birtFontsConfigFile;
    }

    public static String getBaseImgURL() {
        return baseImgURL;
    }

    public static void setBaseImgURL(String baseImgURL) {
        BIRTOption.baseImgURL = baseImgURL;
    }
    

    public static String getImgDir() {
        return imgDir;
    }

    public static String getReportDocumentExtension() {
        return reportDocumentExtension;
    }

    public static String getReportDesignExtension() {
        return reportDesignExtension;
    }

    public static String getOutputExcelFormat() {
        return outputExcelFormat;
    }

    public static String getBirtExpressionPattern() {
        return birtExpressionPattern;
    }

    public static String getBirtUtilWorkerCapacity() {
        return birtUtilWorkerCapacity;
    }

    public static String getBirtExcelEmitter() {
        return birtExcelEmitter;
    }

    public static String getBirtExcelEmitter_native() {
        return birtExcelEmitter_native;
    }

    public static String getBirtExcelEmitter_native_id() {
        return birtExcelEmitter_native_id;
    }

    public static String getBirtExcelEmitter_native_format() {
        return birtExcelEmitter_native_format;
    }

    public static String getBirtExcelEmitter_native_version() {
        return birtExcelEmitter_native_version;
    }

    public static String getBirtExcelEmitter_spudsoft() {
        return birtExcelEmitter_spudsoft;
    }

    public static String getBirtExcelEmitter_spudsoft_id() {
        return birtExcelEmitter_spudsoft_id;
    }

    public static String getBirtExcelEmitter_spudsoft_format() {
        return birtExcelEmitter_spudsoft_format;
    }

    public static String getBirtCSVDelimiter() {
        return birtCSVDelimiter;
    }

    public static String getBirtCSVCharReplaceDelimiter() {
        return birtCSVCharReplaceDelimiter;
    }

    public static String getBirtCSVEmitter_default_format() {
        return birtCSVEmitter_default_format;
    }

    public static String getBirtParamsLanguage() {
        return birtParamsLanguage;
    }

    public static String getBirtReportResourcesDir() {
        return birtReportResourcesDir;
    }

    public static String getDatasourceConnectorID() {
        return datasourceConnectorID;
    }

    public static String getBirtReportAreaID() {
        return birtReportAreaID;
    }

    public static String getBirtParamColumn() {
        return birtParamColumn;
    }

    public static String getBirtParamSpace() {
        return birtParamSpace;
    }

    public static String getBirtDefaultValueRegex() {
        return birtDefaultValueRegex;
    }

    public static String getMaxSizeOfCachedParam() {
        return maxSizeOfCachedParam;
    }

    public static String getNumOfRemovableObjs() {
        return numOfRemovableObjs;
    }

    public static String getIsCacheParam() {
        return isCacheParam;
    }

    public static String getBirtParamDept() {
        return birtParamDept;
    }

    public static String getBirtParamDeptTree() {
        return birtParamDeptTree;
    }

    public static String getBirtParamDate() {
        return birtParamDate;
    }

    public static String getBirtParamTypeTree() {
        return birtParamTypeTree;
    }

    public static String getBirtParamTree() {
        return birtParamTree;
    }

    public static String getBirtParamServiceTree() {
        return birtParamServiceTree;
    }

    public static String getBirtParamIsMap() {
        return birtParamIsMap;
    }

    public static String getReport_code_fix_error_data() {
        return report_code_fix_error_data;
    }
    
    
    public static void loadProperties() throws Throwable {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        Properties prop = new Properties();
        prop.load(classLoader.getResourceAsStream(propertiesFilePath));
        
        String key;
        Class c = BIRTOption.class;
        Field f;
        
        for(Object okey : prop.keySet()) {
            key = okey.toString();
            f = c.getDeclaredField(key);
            f.set(BIRTOption.class, prop.getProperty(key));
        }
    }
}
