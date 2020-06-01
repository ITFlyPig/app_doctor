package com.wyl.doctor.upload.socket;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 创建人   ：yuelinwang
 * 创建时间 ：2020/6/1
 * 描述     ：socket使用帮助类，提供了socket相关的操作
 */
public class SocketHelper {
    private static final String TAG = SocketHelper.class.getName();
    private int port;
    private String host;
    private Socket socket;
    private OutputStream os;
    private static final int MAX_TIMES = 5;//最大重连次数
    private int count = 0;

    public SocketHelper(int port, String host) {
        this.port = port;
        this.host = host;
        socket = new Socket();
    }

    /**
     * socket写入数据
     *
     * @param bytes
     * @return
     */
    public boolean write(byte[] bytes) {
        if (bytes == null) return false;
        if (!socket.isConnected()) {
            tryConnect();
        }
        if (!socket.isConnected()) {
            Log.e(TAG, "write: 不能写入数据，socket建立连接失败");
            return false;
        }
        try {
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 尝试建立连接
     */
    private void tryConnect() {
        count = 0;
        while (count < MAX_TIMES && !socket.isConnected()) {
            try {
                socket.connect(new InetSocketAddress(host, port));
                os = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                count++;
            }
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (socket != null) {

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

}
