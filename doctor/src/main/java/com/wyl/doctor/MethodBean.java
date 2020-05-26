package com.wyl.doctor;

import java.io.Serializable;
import java.util.List;

/**
 * author : wangyuelin
 * time   : 2020/5/9 3:38 PM
 * desc   : 方法信息
 */
public class MethodBean implements Serializable {
    public String name;//方法的短名
    public String fullName;//方法的全名 = 包 + 短名
    public String signature;//签名，唯一表示一个方法
    public long startTime;//开始调用的时间
    public long endTime;//结束调用的时间
    public Object[] args;//方法的参数
    public String classFullName;//所属的类

    //下面的表示父子关系
    public List<MethodBean> childs;
    public MethodBean parent;
}
