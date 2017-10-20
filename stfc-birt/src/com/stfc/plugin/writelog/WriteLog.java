/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.writelog;

import com.viettel.logging.LogWriter;
import java.util.List;

/**
 *
 * @author Trungtd3
 */
public class WriteLog {
    
    public static void writeLog(LogWriter logWriter, List<String> data, String seperator){
        if(data != null && !data.isEmpty()){
            StringBuilder strLog = new StringBuilder();
            int leng = data.size();
            for(int i = 0; i < leng; i ++){
                strLog.append(data.get(i));
                if(i < leng-1){
                    strLog.append(seperator);    
                }
                
            }
            String a = strLog.toString();
            a = a.replace("\n"," ");            
            logWriter.writeLn(a);
            
            
            
        }
    }
    
}
