package com.wyl.doctor.method;

import com.wyl.doctor.MethodBean;

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
     * @param bean
     */
    public void push(MethodBean bean) {
        if (bean == null || bean.threadInfo == null) {
            return;
        }
        long threadId = bean.threadInfo.id;

        //据线程的id获取对应的栈
        Deque<MethodBean> stack = stackMap.get(threadId);
        if (stack == null) {
            stack = new LinkedList<>();
            stackMap.put(threadId, stack);
        }
        //将方法信息入栈
        stack.push(bean);
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
            if (stack.size() > 0) {
                //记录当前方法的父方法
                MethodBean parent = stack.getLast();
                if (parent != null) {
                    if (parent.childs == null) {
                        parent.childs = new ArrayList<>();
                    }
                    //将当前方法添加到父方法的childs列表中
                    parent.childs.add(bean);
                }

            }
        }
        return bean;
    }

}
