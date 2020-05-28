package com.wyl.doctor.method;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.wyl.doctor.BeansCache;
import com.wyl.doctor.MethodBean;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：栈处理线程，主要是负责压栈和出栈，尽最大力减轻主线程的负担
 */
public class HandleStackHelper {
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private static final int MSG_PUSH = 1;//压栈
    private static final int MSG_POP = 2;//出栈

    public HandleStackHelper() {
        mHandlerThread = new HandlerThread("handle_stack_" + System.currentTimeMillis());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                handle(msg);
            }
        };
        //初始化缓存
        BeansCache.init();
    }

    /**
     * 处理命令
     * @param msg
     */
    private void handle(Message msg) {
        switch (msg.what) {
            case MSG_POP:
                if (msg.obj instanceof Long) {
                    long threadId = msg.what;
                    //得到一个方法的调用信息
                    MethodBean bean = MethodRecordStack.getInstance().pop(threadId);
                    //将其放到内存缓存中
                    BeansCache.put(bean);

                }
                break;
            case MSG_PUSH:
                if (msg.obj instanceof MethodBean) {
                    MethodBean bean = (MethodBean) msg.obj;
                    MethodRecordStack.getInstance().push(bean);
                }
                break;
        }
    }

    /**
     * 发送压栈命令
     * @param bean
     */
    public void sendPush(MethodBean bean) {
        if (bean == null) {
            return;
        }
        Message msg = Message.obtain();
        msg.what = MSG_PUSH;
        msg.obj = bean;
        mHandler.sendMessage(msg);
    }

    /**
     * 发送出栈命令
     * @param threadId
     */
    public void sendPop(long threadId) {
        Message msg = Message.obtain();
        msg.what = MSG_POP;
        msg.obj = threadId;
        mHandler.sendMessage(msg);
    }


}