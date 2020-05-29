package com.wyl.doctor;

import com.wyl.doctor.upload.IUpload;
import com.wyl.doctor.upload.UploadUtil;

import java.io.File;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：上传文件的任务
 */
public class UploadFileTask implements Runnable {
    private File file;
    private IUpload iUpload;

    public UploadFileTask(File file, IUpload iUpload) {
        this.file = file;
        this.iUpload = iUpload;
    }

    @Override
    public void run() {
        if (file == null || iUpload == null) return;

        if (iUpload.upload(file)) {
            //上传成功，删除文件
            file.delete();
        }
    }
}
