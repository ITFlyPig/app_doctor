package com.wyl.doctor.visitor;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/8
 * 描述    : 处理方法
 */
public class DoctorMethodVisitor extends AdviceAdapter {
    public static final String TAG = DoctorMethodVisitor.class.getName();

    private Type[] paramTypes;//方法参数的类型
    private String classFullName;
    private String methodName;//方法名

    protected DoctorMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, String classFullName) {
        super(api, mv, access, name, desc);
        paramTypes = Type.getArgumentTypes(desc);
        this.classFullName = classFullName;
        this.methodName = name;

    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        if (classFullName == null) {
            classFullName = "";
        }
        //将类名加载到操作数栈
        mv.visitLdcInsn(classFullName);
        //将方法名加载到操作数栈
        mv.visitLdcInsn(methodName);

        int len = paramTypes == null ? 0 : paramTypes.length;
        if (len > 0) {
            //创建一个Object数组，将方法的参数放到数组中
            loadArgArray();
            mv.visitMethodInsn(INVOKESTATIC, "com/wyl/doctor/method/MethodRecordUtil", "onStart", "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", false);
        } else {
            mv.visitMethodInsn(INVOKESTATIC, "com/wyl/doctor/method/MethodRecordUtil", "onStart", "(Ljava/lang/String;Ljava/lang/String;)V", false);
        }

    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        if (classFullName == null) {
            classFullName = "";
        }
        mv.visitMethodInsn(INVOKESTATIC, "com/wy/.doctor/method/MethodRecordUtil", "onEnd", "()V", false);
    }
}
