package com.wyl.doctor;

import android.text.TextUtils;

import com.wyl.doctor.utils.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
    private int maxNum = 10;//最大文件数量
    private FileManager fileManager;//用于屏蔽具体文件的管理

    public LogManager(String logDir, int maxNum) {
        this.logDir = logDir;
        this.maxNum = maxNum;
        fileManager = new FileManager(logDir);
    }

    /**
     * 将数据写到文件
     * @param obj
     */
    public void writeToLog(Serializable obj) {
        if (obj == null) return;
        //写到文件
        fileManager.writeToFile(obj);
        //检查是否上传
        List<File> files = fileManager.findUploadFiles();
        if (files.size() >= maxNum) {
            //重置可上传集合
            fileManager.resetUploadSet();
            //添加上传文件的任务

        }
    }


}
