package com.wyl.doctor;

import android.util.Log;

import com.wyl.doctor.upload.UploadUtil;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：log文件操作：1、将数据写入log文件；2、触发上传到服务器操作
 * log可能有很多种，所以设计成一般的实例。
 */
public class LogManager {
    private static final String TAG = LogManager.class.getName();
    private String logDir;//log文件的存储目录
    private int maxNum = 2;//最大文件数量
    private FileManager fileManager;//用于屏蔽具体文件的管理

    public LogManager(String logDir, int maxNum) {
        this.logDir = logDir;
        this.maxNum = maxNum;
        fileManager = new FileManager(logDir);
    }

    private static LogManager logManager = Holder.logManager;
    private static class Holder {
        private static LogManager logManager = new LogManager(AppDoctor.getLogDirPath(), 3);//测试 3个文件就开始传
    }
    public static LogManager instance() {
        return logManager;
    }

    /**
     * 将数据写到文件
     * @param obj
     */
    public synchronized void  writeToLog(Serializable obj) {
        Log.d(TAG, "writeToLog: 将对象写入到文件");
        if (obj == null) return;
        //写到文件
        fileManager.writeToFile(obj);
        //检查是否上传
        List<File> files = fileManager.findUploadFiles();
        Log.d(TAG, "writeToLog: bean写入后，待上传文件数量：" + files.size());

        if (files.size() >= maxNum) {
            //重置可上传集合
            fileManager.resetUploadSet();
            //将文件添加到队列，等待上传
            for (File file : files) {
                UploadUtil.uploadAsync(file);
            }
        }
    }


}
