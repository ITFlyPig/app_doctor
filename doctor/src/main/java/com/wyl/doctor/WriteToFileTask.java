package com.wyl.doctor;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : wangyuelin
 * time   : 2020/5/9 5:53 PM
 * desc   : 写入到文件的task
 */
public class WriteToFileTask implements Runnable{
    private ArrayList<Serializable> beans;

    public WriteToFileTask(ArrayList<Serializable> beans) {
        this.beans = beans;
    }

    @Override
    public void run() {
        Log.d("wyl", "WriteToFileTask 开始写入任务");
        try {
            if (beans != null) {
                for (Serializable bean : beans) {
                    Log.d("wyl", "开始写的bean：" + bean.toString());
                }
            }
            Thread.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("wyl", "WriteToFileTask 写入任务完成");
    }
}
