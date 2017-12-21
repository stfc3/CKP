package com.stfc.plugin.process;

import com.stfc.plugin.birt.BIRTOption;
import com.stfc.plugin.birt.BIRTUtil;
import com.viettel.eafs.util.FileUtil;
import com.viettel.eafs.util.ServletUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trungtd3
 */
public class ProcessFile extends ProcessThread {

    private volatile long startTime;
    //chu ky xoa file (1phut)
    private int maxTimeDelete = 60000;
    //co active tientrinh hay khong
    private volatile boolean isActive;
    private boolean isStop = true;
    //List cac url file can xoa
    private List<HashMap> lstCacheDelete = new ArrayList<HashMap>();
    private static float numMbInByte = 1024 * 1024;

    public ProcessFile(String theadName) {
        super(theadName);
        this.logger.info("start ProcessFile");
        init();

    }

    //khoi tao tham so
    private void init() {
        try {

            this.logger.info("Init param process");
            int numThread = Runtime.getRuntime().availableProcessors();
            this.logger.info("num threads: " + numThread);
            this.startTime = System.currentTimeMillis();

            this.maxTimeDelete = Integer.valueOf(PropertyConfigFile.getMaxTimeDelete()) * 60000;
            String iActive = PropertyConfigFile.getIsActive();
            if ("true".equalsIgnoreCase(iActive)) {
                this.isActive = true;
                this.logger.info("ProcessFile is actived");
            } else {
                this.isActive = false;
                this.logger.info("ProcessFile is not active, do not delete file");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

    }

    @Override
    public void start() {
        this.logger.info("start thread timing");
        //xoa het thu muc cu
        if (this.isActive) {
            deleteAllDir();
            deleteAllDirImg();
        }
        this.isStop = false;
        super.start();

        this.logger.info("start thread write log to file");

    }

    @Override
    public void stop() {

        this.logger.info("flush current log to file");
        deleteWhenStop();
        this.isStop = true;
        this.logger.info("stop thread timing");
        super.stop();

    }

    @Override
    protected void process() {
        try {
            logger.info("Goi ham process file");
            if (!isStop) {
                long intervalTime = System.currentTimeMillis() - this.startTime;
                if (intervalTime >= this.maxTimeDelete) {
                    deleteWhenTimeout();

                }
                //Bo sung xoa theo thoi gian.
                // Xoa cac ban ghi cu. truoc 1 nga
                deleteByTime();
                //Xoa thu muc image
                deleteByTimeImg();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            //Stop tien trinh trong 
            logger.info("stop tien trinh trong" + this.maxTimeDelete / 1000 + " (s)");
            try {
                Thread.sleep(this.maxTimeDelete);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Xoa khi thoa man thoi gian
     */
    private synchronized void deleteWhenTimeout() {
        long intervalTime = System.currentTimeMillis() - this.startTime;
        if (intervalTime >= this.maxTimeDelete) {
            deleteImmediately();
        }
    }

    /**
     * Xoa file khi stop ung dung
     */
    private synchronized void deleteWhenStop() {
        if (this.isActive) {
            this.isActive = false;
            deleteAllDir();
            deleteAllDirImg();
        }

    }

    private synchronized void deleteImmediately() {

        //lay url xoa file.
        deleteByCacheSize();

        deleteByVolumeDir();
        this.startTime = System.currentTimeMillis();

    }

    //Ham xoa file tren URL
    public synchronized void deletefile(String url) {
        if (!this.isActive) {
            System.out.println("Chua active");
            return;
        }
        //xoa file tren server
        try {
            File fileTmp = new File(url);
            if (fileTmp.exists()) {
                System.out.println("deletefile:" + url);
                fileTmp.delete();
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }
    }

    //Ham giao tiep app vao cache xoa file
    public synchronized void callDeleteFile(List<HashMap> lstDel) {
        System.out.println("goi ham add vao cache de xoa");
        if (lstDel != null && lstDel.size() > 0) {
            System.out.println("lstDel add vao:" + lstDel.size());
        }
        this.lstCacheDelete.addAll(lstDel);

        if (lstCacheDelete != null && lstCacheDelete.size() > 0) {
            System.out.println("lstCacheDelete" + lstCacheDelete.size());
        } else {
            System.out.println(":Null Cáche");
        }

    }

    //Ham xoa file theo cachesize
    private synchronized void deleteByCacheSize() {
        logger.info("Goi tien trinh xoa theo cachsize");
        if (!this.isActive) {
            return;
        }
        if (lstCacheDelete != null && !lstCacheDelete.isEmpty()) {

            for (int i = 0; i < lstCacheDelete.size(); i++) {
                HashMap<String, String> has = lstCacheDelete.get(i);
                for (String key : has.keySet()) {
                    String url = has.get(key);
                    deletefile(url);
                }

            }
            lstCacheDelete.clear();

        }
        //empty lstCacheDelete
        //xoa file tren server
    }

    //Ham xoa file theo dir volume (dung luong thu muc)
    private synchronized void deleteByVolumeDir() {
        logger.info("Goi tien trinh xoa theo dung luong thu muc");
        if (!this.isActive) {
            return;
        }
        try {
            String path = ServletUtil.getRealPath(BIRTOption.getBirtReportDocsDir());
            File folderDocument = new File(path);
            if (folderDocument.isDirectory()) {
                //Dung luong theo byte

                float volumeFolder = getFolderSize(folderDocument) / numMbInByte;
                logger.info("Dung luong thu muc birtReportDocument:" + volumeFolder + " (Mb)");

                //List<HashMap> lst = BIRTUtil.cachMapUrl;
                Long volumeFolderConfig = Long.parseLong(PropertyConfigFile.getNumMbBirtDocument());
                float percent = Float.valueOf(PropertyConfigFile.getNumPercentRemoveDocument()) / 100f;

                if (volumeFolder > volumeFolderConfig) {
                    //xoa theo so % cau hinh trong thu muc                    
                    //Xoa cac file trong folder cho den khi con 50%.
                    //Xoa dan cho den khi dung luong nho hon cau hinh.
                    int num = 0;
                    while (getFolderSize(folderDocument) / numMbInByte > volumeFolderConfig * percent) {
                        //Them dk nay de tranh lap vo han lap vo han. 
                        num++;
                        if (num > Integer.valueOf(PropertyConfigFile.getMaxSizeOfCacheDocument())) {
                            break;
                        }

                        //Lay toan bo file trong thu muc
                        List<ObjectFile> listFileInDir = new ArrayList<ObjectFile>();
                        listFileInDir = getLstFile(folderDocument, listFileInDir);
                        //lay ra file lon nhat
                        ObjectFile maxfile = getMaxFile(listFileInDir);
                        //xoa file ra cacheMapUrl

                        for (int i = 0; i < BIRTUtil.getCachMapUrl().size(); i++) {
                            HashMap<String, String> has = BIRTUtil.getCachMapUrl().get(i);
                            for (String key : has.keySet()) {
                                String urlDocument = has.get(key);
                                //check neu url co chua ten file thi xoa.                                 
                                if (urlDocument.equals(maxfile.getFilePath())) {
                                    BIRTUtil.getCachMapUrl().remove(i);
                                    BIRTUtil.getMapKey().remove(key);
                                    deletefile(maxfile.getFilePath());
                                }

                            }

                        }//end for

                    }

                }

            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        //xoa file tren server
    }

    /**
     * Xoa all file va thu muc con trong thu muc birt-documents
     *
     * @throws Throwable
     */
    private synchronized void deleteAllDir() {
        try {
            String path = ServletUtil.getRealPath(BIRTOption.getBirtReportDocsDir());
            logger.info("pathDirDel: " + path);
            FileUtil.emptyDirectory(path);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private synchronized void deleteAllDirImg() {
        try {

//            System.out.println("all aa:"+ServletUtil.getRealPath("/birt-resources"));
//            System.out.println("all aa2:"+ServletUtil.getRealPath("/birt-documents"));
            String path = ServletUtil.getRealPath(BIRTOption.getBaseImgURL());
            logger.info("pathDirImage PRE: " + path);
            path = path.replaceFirst(ServletUtil.getContextPath(), "");
            logger.info("pathDirImage: " + path);
//            System.out.println("all path:2"+ path);

            FileUtil.emptyDirectory(path);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    //BIRTOption.getBirtReportDocsDir()
    private synchronized void deleteByTime() {
        try {
            String path = ServletUtil.getRealPath(BIRTOption.getBirtReportDocsDir());
            logger.info("pathDel: " + path);
            File folderDocument = new File(path);
            if (folderDocument.isDirectory()) {
                deleteByFolder(folderDocument);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private synchronized void deleteByTimeImg() {
        try {
            String path = ServletUtil.getRealPath(BIRTOption.getBaseImgURL());
            path = path.replaceFirst(ServletUtil.getContextPath(), "");
            logger.info("pathFileImage: " + path);
            File folderDocument = new File(path);
            if (folderDocument.isDirectory()) {
                deleteByFolder(folderDocument);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void deleteByFolder(File folder) {

        //Lay ra listFile trong thu muc con\
        File[] fileChild = folder.listFiles();
        //Xoa file.
        if (fileChild != null && fileChild.length > 0) {
            for (int j = 0; j < fileChild.length; j++) {
                if (fileChild[j].isDirectory()) {
                    deleteByFolder(fileChild[j]);
                } else {
                    deleteFileChild(fileChild[j]);
                }

            }
        }
    }

    private void deleteFileChild(File fileChild) {
        long lastMod = fileChild.lastModified();
        long currentTime = System.currentTimeMillis();
        //So ngay da sua doi
        long hour = (currentTime - lastMod) / (1000 * 60 * 60);
        //long hour = (currentTime - lastMod) / (1000 * 60 );//test 1 phut.
        //long date = (currentTime - lastMod) / (1000 * 60);
        //System.out.print("\nFile da sua doi trong " + date + "ngay \n");
        //Chi xoa cac file truoc 1 gio
        if (hour >= 1) {
            System.out.println("Xoa file do qua 1 h:" + fileChild.getName());
            //Duyet xoa phan tu trong cache
            for (int i = 0; i < BIRTUtil.getCachMapUrl().size(); i++) {
                HashMap<String, String> has = BIRTUtil.getCachMapUrl().get(i);
                for (String key : has.keySet()) {
                    // System.out.println("KeyMapURl:"+ key);
                    String urlDocument = has.get(key);
                    //   System.out.println("urlDocument:"+ urlDocument);
                    //    System.out.println("fileChild.getAbsolutePath():"+ fileChild.getAbsolutePath());
                    //check neu url co chua ten file thi xoa.                                 
                    if (urlDocument.equals(fileChild.getAbsolutePath())) {
                        logger.info("Xoa file do trung key " + fileChild.getName());
                        BIRTUtil.getCachMapUrl().remove(i);
                        BIRTUtil.getMapKey().remove(key);
                        fileChild.delete();
                    }

                }

            }//end for
            //Xoa file vat ly.
            fileChild.delete();

            //System.out.print("\nDa xoa file " + fileName + "\n");
        } else {
            System.out.println("File ko xoa do time=:" + hour + "(h). " + (currentTime - lastMod) + "(ms)");
        }
    }

    /**
     * Lay dung luong theo byte cua folder.
     *
     * @param folder
     * @return
     */
    public static float getFolderSize(File folder) {
        float foldersize = 0;
        File[] filelist = folder.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
                    foldersize += getFolderSize(filelist[i]);
                } else {
                    foldersize += filelist[i].length();
                }
            }
        }
        return foldersize;
    }

    /**
     * Get list filename, filesize
     *
     * @param folder
     * @param mapFile
     * @return và
     */
    public static List<ObjectFile> getLstFile(File folder, List<ObjectFile> mapFile) {
        File[] filelist = folder.listFiles();
        if (filelist != null && filelist.length > 0) {
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].isDirectory()) {
                    getLstFile(filelist[i], mapFile);
                } else {
                    ObjectFile objFile = new ObjectFile(filelist[i].getAbsolutePath(), filelist[i].length());
                    mapFile.add(objFile);
                }
            }
        }
        return mapFile;
    }

    public ObjectFile getMaxFile(List<ObjectFile> mapFile) {
        Long maxsize = Long.MIN_VALUE;
        String fileMax = "";
        ObjectFile maxfile = new ObjectFile();
        if (mapFile != null && !mapFile.isEmpty()) {
            for (int i = 0; i < mapFile.size(); i++) {
                ObjectFile objFile = mapFile.get(i);
                if (objFile.getFileSize() > maxsize) {
                    maxsize = objFile.getFileSize();
                    fileMax = objFile.getFilePath();
                }

            }
            maxfile.setFilePath(fileMax);
            maxfile.setFileSize(maxsize);
        }
        return maxfile;
    }
}
