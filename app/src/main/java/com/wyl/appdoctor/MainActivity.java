package com.wyl.appdoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.wyl.doctor.BeansCache;
import com.wyl.doctor.MethodBean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BeansCache.init();

        new Thread(new Runnable() {
            @Override
            public void run() {
               while (true) {
                   Log.d("wyl", "任务1开始提供数据");
                   for (int i = 0; i < 30; i++) {
                       BeansCache.put(new MethodBean());
                   }
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
               while (true) {
                   Log.d("wyl", "任务2开始提供数据");
                   for (int i = 0; i < 10; i++) {
                       BeansCache.put(new MethodBean());
                   }
                   try {
                       Thread.sleep(800);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        }).start();
    }
}
