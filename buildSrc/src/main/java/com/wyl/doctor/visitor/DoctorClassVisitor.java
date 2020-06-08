package com.wyl.doctor.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/8
 * 描述     ：方法统计类访问
 */
public class DoctorClassVisitor extends ClassVisitor {
    private String classFullName;

    public DoctorClassVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        classFullName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new DoctorMethodVisitor(this.api, methodVisitor, access, name, desc, classFullName);
        return methodVisitor;
    }
}
