package com.wyl.doctor;

import com.wyl.doctor.base.BaseTransform;
import com.wyl.doctor.visitor.DoctorClassVisitor;

import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

/**
 * 创建人   : yuelinwang
 * 创建时间 : 2020/6/8
 * 描述    : 变换操作
 */
public class DoctorTransform extends BaseTransform {
    public DoctorTransform(Project project) {
        super(project);
    }

    @Override
    protected BiConsumer<InputStream, OutputStream> inject() {
        return new BiConsumer<InputStream, OutputStream>() {
            @Override
            public void accept(InputStream in, OutputStream out) {
                try {
                    ClassReader classReader = new ClassReader(in);
                    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    DoctorClassVisitor doctorClassVisitor = new DoctorClassVisitor(Opcodes.ASM5, classWriter);
                    classReader.accept(doctorClassVisitor, ClassReader.EXPAND_FRAMES);
                    out.write(classWriter.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public String getName() {
        return "doctor_transform";
    }

    @Override
    protected boolean classFilter(String classPath) {
        //避免循环引用
        return classPath.contains("MethodRecordUtil");
    }
}
