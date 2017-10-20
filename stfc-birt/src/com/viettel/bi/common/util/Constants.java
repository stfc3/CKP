/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bi.common.util;

/**
 *
 * @author Trungtd3
 */
public interface Constants {
    
    //ten config cau hinh check quyen tren vsa trong file config.properties/
    public static String CONFIG_CHECK_ROLE_VSA="config_check_role_vsa";
    
    public static String IS_CHECK_ROLE_VSA="1";
    
    //Config thong so webservice
    public static String WS_VSA_URL="WS_VSA_URL";
            
    public static String WS_VSA_USERNAME="WS_VSA_USERNAME";
            
    public static String WS_VSA_PASSWORD="WS_VSA_PASSWORD";
    
    public static String ROLE_NONE_AUTHENTICATION="ROLE_NONE_AUTHENTICATION";
    
    
            
   //Cac tac dong tren OLAP
    public static String ACTION_OLAP_QUERY="1";
    public static String ACTION_OLAP_EXPORT_EXCEL="2";
    public static String ACTION_OLAP_EXPORT_CSV="3"; 
    public static String ACTION_OLAP_EXPORT_PDF="4"; 
    
    
    
    public static String ACTION_OLAP_QUERY_NAME="VIEW";
    public static String ACTION_OLAP_EXPORT_EXCEL_NAME="EXCEL";
    public static String ACTION_OLAP_EXPORT_CSV_NAME="CSV"; 
    public static String ACTION_OLAP_EXPORT_PDF_NAME="PDF"; 
    
    public static String ACTION_OLAP_CREATE_MEASURE="5"; 
    public static String ACTION_OLAP_EDIT_MEASURE="6"; 
    public static String ACTION_OLAP_DELETE_MEASURE="7"; 
    public static String ACTION_OLAP_CREATE_MEASURE_NAME="CREATE"; 
    public static String ACTION_OLAP_EDIT_MEASURE_NAME="EDIT"; 
    public static String ACTION_OLAP_DELETE_MEASURE_NAME="DELETE";
    
    
    public static String ACTION_SAVE_MDX="8";
    public static String ACTION_SAVE_MDX_NAME="SAVE_QUERY";
    
    public static String ACTION_DELETE_MDX="9";
    public static String ACTION_DELETE_MDX_NAME="DELETE_QUERY";

            
    
}
