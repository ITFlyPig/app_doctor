package com.wyl.doctor;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/5/28
 * 描述     ：
 */
public class AppDoctor {
    private static String logDirPath;//日志文件的存储目录

    public static void init(String logDirPath) {
        AppDoctor.logDirPath = logDirPath;
    }

    public static String getLogDirPath() {
        return logDirPath;
    }
}
