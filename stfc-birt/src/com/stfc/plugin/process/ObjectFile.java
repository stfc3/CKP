/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stfc.plugin.process;

/**
 *
 * @author trungtd3
 */
public class ObjectFile {
    
    private String filePath;
    private Long fileSize;

 


    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ObjectFile() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ObjectFile(String filePath, Long fileSize) {
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    
    
    
    
}
