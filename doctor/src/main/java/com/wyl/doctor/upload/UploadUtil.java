package com.wyl.doctor.upload;

import com.wyl.doctor.ThreadHelper;
import com.wyl.doctor.UploadFileTask;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/27
 * 描述     ：上传工具类
 */
public class UploadUtil {
    //目前只提供了okhttp的上传实现
    private static IUpload iUpload = new OkHttpUpload();//上传器
    private static LinkedBlockingQueue<File> uploadQueue = new LinkedBlockingQueue<>();//待上传文件的容器
    private static volatile boolean isStartUploadTask = false;//是否开启了上传的任务
    private static volatile boolean isStopUpload = false;//是否停止上传

    /**
     * 异步上传，当队列中的文件超过Integer.MAX_VALUE时，要上传的文件直接被丢弃
     * @param file
     */
    public static void uploadAsync(File file) {

        if (file == null) {
            return;
        }
        if(!isStartUploadTask) {
            //开启上传任务
            isStartUploadTask = true;
            startUploadTask();
        }
        uploadQueue.offer(file);
    }

    /**
     * 停止上传
     */
    public static void stopUpload() {
        isStopUpload = true;
    }

    /**
     * 同步上传
     * @param file
     */
    public static void upload(File file){
        if (file == null) return;
        new UploadFileTask(file, iUpload).run();
    }

    /**
     * 开启上传文件的任务
     */
    private static void startUploadTask() {
        ThreadHelper.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                while (!isStopUpload) {
                    try {
                        //获得文件
                        File file = uploadQueue.take();
                        //上传
                        upload(file);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
}
