package com.wyl.doctor.method;

import com.wyl.doctor.MethodBean;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：记录方法的开始和结束；方法的开始和结束必须成对出现，不然统计不准确。
 */
public class MethodRecordUtil {
    private static HandleStackHelper stackHelper = new HandleStackHelper();

    /**
     * 方法开始
     * @param cls
     * @param methodName
     * @param args
     */
    public static void onStart(Class cls, String methodName, Object[] args) {
        if (cls == null || methodName == null) {
            return;
        }

        //记录方法的相关信息
        MethodBean methodBean = new MethodBean();
        //记录所属类
        methodBean.classFullName = cls.getName();
        //记录方法名称
        methodBean.name = methodName;
        //记录方法的参数
        methodBean.args = args;
        //记录开始时间
        methodBean.startTime = System.currentTimeMillis();
        //记录方法所属线程信息
        Thread curThread = Thread.currentThread();
        ThreadInfo info = ThreadCache.getThreadInfo(curThread.getId(), curThread.getName());
        methodBean.threadInfo = info;

        //压栈
        stackHelper.sendPush(methodBean);
    }

    /**
     * 方法的开始会调用
     * @param signature
     * @param args
     */
    public static void onStart(String signature, Object[] args) {
    }

    /**
     * 没有参数的情况
     * @param signature
     */
    public static void onStart(String signature) {
    }

    /**
     * 方法的结束会调用
     */
    public static void onEnd() {
        Thread curThread = Thread.currentThread();
        stackHelper.sendPop(curThread.getId());
    }
}
