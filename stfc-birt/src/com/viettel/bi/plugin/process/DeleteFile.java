/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.bi.plugin.process;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author trungtd3
 */
public class DeleteFile {
        
    //Ham giao tiep app vao cache xoa file
     public static void callDeleteFile(ProcessFile processFile, List<HashMap>  lstDel){
         processFile.callDeleteFile(lstDel);
     }
     
     
}
