package com.wyl.doctor;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author : wangyuelin
 * time   : 2020/5/9 3:54 PM
 * desc   : 用于在内存中暂存bean，避免磁盘的频繁写入
 */
public class BeansCache {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition full = lock.newCondition();

    private static final int MAX_NUM = 5;//缓存的数量
    private static ArrayList<Serializable> beans = new ArrayList<>(MAX_NUM);
    private static volatile boolean init = false;//是否初始化了

    public static void init() {
        if (init) {
            return;
        }
        //准备好写入到磁盘的任务
        startSaveToFile();
        init = true;
    }

    /**
     * 存入
     *
     * @param bean
     */
    public static void put(Serializable bean) {
        if (bean == null) {
            return;
        }
        lock.lock();
        try {
            beans.add(bean);
            int size = beans.size();

            if (size >= MAX_NUM) {
                Log.d("wyl", "数据已满");
                full.signal();
            } else {
                Log.d("wyl", "数据未满");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取魂村好的beans
     *
     * @return
     */
    public static ArrayList<Serializable> getFullCachedBeans() {
        if (beans.size() < MAX_NUM) {
            return null;
        }
        ArrayList<Serializable> fullCache = beans;
        beans = new ArrayList<>(MAX_NUM);
        return fullCache;
    }

    /**
     * 将缓存中的数据保存到磁盘
     */
    private static void startSaveToFile() {
        ThreadHelper.getInstance().submit(new GetBeansTask(lock, full));
    }


}
