package com.wyl.doctor;

import android.util.TimeUtils;

import androidx.annotation.NonNull;

import com.wyl.doctor.method.ThreadInfo;
import com.wyl.doctor.utils.TimeUtil;

import java.io.Serializable;
import java.util.List;

/**
 * author : wangyuelin
 * time   : 2020/5/9 3:38 PM
 * desc   : 方法信息
 */
public class MethodBean implements Serializable {
    public String name;//方法名字
    public Object[] args;//方法的参数
    public String classFullName;//所属的类：全类名
    public long startTime;//开始调用的时间
    public long endTime;//结束调用的时间


    //下面的表示父子关系
    public List<MethodBean> childs;
    public MethodBean parent;

    public ThreadInfo threadInfo;

    @NonNull
    @Override
    public String toString() {
        return "[name = " + name + "] " +
                "[classFullName = " + classFullName + "] " +
                "[startTime = " + TimeUtil.getDate(startTime) + "] " +
                "[endTime = " + TimeUtil.getDate(endTime) + "] " +
                "[threadName = " + threadInfo.name + "] ";

    }
}
