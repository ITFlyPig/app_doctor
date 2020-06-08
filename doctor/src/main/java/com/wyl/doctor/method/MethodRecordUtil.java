package com.wyl.doctor.method;

import com.wyl.doctor.LogType;
import com.wyl.doctor.unchanged.MethodBean;
import com.wyl.doctor.unchanged.ThreadInfo;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：记录方法的开始和结束；方法的开始和结束必须成对出现，不然统计不准确。
 */
public class MethodRecordUtil {
    private static StackCacheHelper stackHelper = new StackCacheHelper();

    /**
     * 方法开始
     * @param classFullName
     * @param methodName
     * @param args
     */
    public static void onStart(String classFullName, String methodName, Object[] args) {
        if (classFullName == null || methodName == null) {
            return;
        }

        //记录方法的相关信息
        MethodBean methodBean = new MethodBean();
        methodBean.type = LogType.ALL_PATH;
        //记录所属类
        methodBean.classFullName = classFullName;
        //记录方法名称
        methodBean.methodName = methodName;
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

    public static void onStart(String classFullName, String methodName) {
        onStart(classFullName, methodName, null);

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
