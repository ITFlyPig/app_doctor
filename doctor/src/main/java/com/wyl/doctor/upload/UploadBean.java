package com.wyl.doctor.upload;

import java.io.File;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：上传bean
 */
public class UploadBean {
    public File file;//待上传的文件
    public int type;//文件的类型

    public UploadBean(File file, int type) {
        this.file = file;
        this.type = type;
    }
}
