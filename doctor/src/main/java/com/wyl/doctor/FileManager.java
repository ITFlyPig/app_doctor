package com.wyl.doctor;

import android.text.TextUtils;

import com.wyl.doctor.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：文件的管理，主要是记录文件的相关状态：新建、等待上传等，提供文件获取、写入和删除操作
 */
public class FileManager {
    public static final String TAG = FileManager.class.getName();
    private String dir;
    private ArrayList<File> writeableFiles;//可写入的文件集合，包括：新建文件、写了部分容量的文件
    private ArrayList<File> waitUploadFiles;//等待上传的文件集合
    private int oneFileMaxSize = 5 * 1024 * 1024; //单个文件最大的size 5M
    private int maxNum = 10;//最大文件数量

    public FileManager(String dir) {
        this.dir = dir;
        writeableFiles = new ArrayList<>();
        waitUploadFiles = new ArrayList<>();
        initFileStatus();
    }

    /**
     * 初始化所有文件的状态
     */
    private void initFileStatus() {
        List<File> files = getAllFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.length() >= oneFileMaxSize) {
                waitUploadFiles.add(file);
            } else {
                writeableFiles.add(file);
            }
        }
    }

    /**
     * 得到文件夹下的所有文件
     */
    private List<File> getAllFiles() {
        if (!makeSureDir(dir)) {
            return null;
        }
        File[] files = new File(dir).listFiles();
        if (files == null) {
            return null;
        }
        return Arrays.asList(files);
    }

    /**
     * 确保dir存在
     * @param dirPath
     * @return
     */
    private boolean makeSureDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        //确保目录存在
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                //目录创建失败，返回
                LogUtil.e(TAG, "saveToFile 目录创建失败：" + dir.getAbsolutePath());
                return false;
            }
        }
        return true;
    }

    /**
     * 创建log文件
     * @return
     */
    private File createNewLogFile() {
        String logFilepath = dir + File.separator + System.currentTimeMillis() + ".log";
        File logFile = new File(logFilepath);
        try {
            //文件创建成功，记录下来
             if (logFile.createNewFile()) {
                 writeableFiles.add(logFile);
                 return logFile;
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是可写的文件
     * @param file
     * @return
     */
    private boolean isWritable(File file) {
        return file.length() < oneFileMaxSize;
    }


    /**
     * 删除List中，路劲为path的文件记录
     * @param path
     * @param files
     */
    private void remove(String path, List<File> files) {
        if (path == null || files == null) {
            return;
        }
        Iterator<File> it = files.iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(it.next().getAbsolutePath(), path)) {
                it.remove();
            }
        }
    }

    /**
     * 找到可写入的文件
     * 如果返回的文件不为空，那么文件一定是可以用的
     * @return
     */
    private File findWritableFile() {
        File logFile = null;
        for (File file : writeableFiles) {
            if (isWritable(file)) {
                logFile = file;
                return logFile;
            }
        }
        //未找到，创建新的
        logFile = createNewLogFile();
        return logFile;
    }

//    /**
//     * 检查文件的上传
//     */
//    private void handleUpload() {
//        List<File> uploadFiles = findUploadFiles();
//        int len = uploadFiles == null ? 0 : uploadFiles.size();
//        //检查文件总数是否达到上传
//        if (len >= maxNum) {
//            //开始
//        }
//    }



    //////////////一下为对外暴露的方法/////////////////


    /**
     * 可上传的文件集合
     * @return
     */
    public List<File> findUploadFiles() {
        return writeableFiles;
    }

    /**
     * 重置可上传文件集合
     */
    public void resetUploadSet() {
        waitUploadFiles = new ArrayList<>();
    }

    /**
     * 删除文件
     * @param path
     */
    public void removeFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        //删除磁盘上的文件
        File f = new File(path);
        f.delete();
        //删除内存中的记录
        remove(path, waitUploadFiles);
        remove(path, writeableFiles);
    }

    /**
     * 保存到文件
     * @param obj
     */
    public void writeToFile(Serializable obj) {
        if (obj == null || TextUtils.isEmpty(dir)) {
            return;
        }

        //目录不可用
        if(!makeSureDir(dir)) {
            return;
        }
        //找到可写入的log文件
        File logFile = findWritableFile();
        if (logFile == null) {
            LogUtil.e(TAG, "找不到合适的log文件写入");
            return;
        }
        //开始写入
        FileUtils.writeToFile(logFile, obj);

        //检查是否达到上传的大小，达到的放到可上传的集合里
        if(logFile.length() >= oneFileMaxSize) {
            remove(logFile.getAbsolutePath(), writeableFiles);
            waitUploadFiles.add(logFile);
        }
    }

    //////////////////////////////////////////

}
