package com.wyl.doctor.upload;

import com.wyl.doctor.utils.OkhttpHelper;
import com.wyl.doctor.utils.Server;
import com.wyl.doctor.utils.Urls;

import java.io.File;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：Okhttp实现的文件上传器
 */
public class OkHttpUpload implements IUpload {

    @Override
    public boolean upload(File file) {
        OkhttpHelper.instance().uploadFile(Server.host + Urls.UPLOAD_URL, file);
        return true;
    }


}
