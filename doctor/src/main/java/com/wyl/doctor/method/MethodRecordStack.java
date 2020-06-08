package com.wyl.doctor.method;

import com.wyl.doctor.unchanged.MethodBean;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/26
 * 描述     ：使用栈的方式记录方法块
 */
public class MethodRecordStack {
    ///////////构造单例///////////////
    private static MethodRecordStack instance = Holder.methodRecordStack;


    private MethodRecordStack() {
    }

    private static class Holder {
        private static MethodRecordStack methodRecordStack = new MethodRecordStack();
    }

    public static MethodRecordStack getInstance() {
        return instance;
    }
    //////////////////////////

    private Map<Long, Deque<MethodBean>> stackMap = new HashMap<>();

    /**
     * 将方法信息入栈
     * @param newCall
     */
    public void push(MethodBean newCall) {
        if (newCall == null || newCall.threadInfo == null) {
            return;
        }
        long threadId = newCall.threadInfo.id;

        //据线程的id获取对应的栈
        Deque<MethodBean> stack = stackMap.get(threadId);
        if (stack == null) {
            stack = new LinkedList<>();
            stackMap.put(threadId, stack);
        }

        //建立方法调用中的父子关系
        if (!stack.isEmpty()) {
            MethodBean parentCall = stack.peek();
            if (parentCall != null) {
                if (parentCall.childs == null) {
                    parentCall.childs = new ArrayList<>();
                }
                parentCall.childs.add(newCall);
                newCall.parent = parentCall;
            }
        }

        //将方法信息入栈
        stack.push(newCall);
    }

    /**
     * 方法出栈
     * @return
     */
    public MethodBean pop(long threadId) {
        Deque<MethodBean> stack = stackMap.get(threadId);
        if (stack == null) {
            return null;
        }
        MethodBean bean = stack.pop();
        if (bean != null) {
            //记录结束时间
            bean.endTime = System.currentTimeMillis();
        }

        //一个完整的方法片段
        if (stack.isEmpty()) {
            return bean;
        }
        return null;
    }

}
