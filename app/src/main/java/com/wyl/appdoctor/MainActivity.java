package com.wyl.appdoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.wyl.doctor.AppDoctor;
import com.wyl.doctor.ThreadHelper;
import com.wyl.doctor.method.MethodRecordUtil;

public class MainActivity extends AppCompatActivity {
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDoctor.init(getApplicationContext());

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                count++;
//                test1("时间：" + System.currentTimeMillis(), count);
//                uploadTest();
//                test1("haha", 20);
//                test1("name:"  , 1);
                test1("wang", 2);
            }
        });

    }

    private void test1(String name, int age) {
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test2();
        test3();
    }

    private void test2() {

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void test3() {
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void uploadTest() {
        ThreadHelper.getInstance().submit(new Runnable() {
            @Override
            public void run() {
//                OkhttpHelper.instance().uploadFile("http://localhost:8080/doctor/upload/save", new File("/sdcard/Jessica_Alba.zip"));
            }
        });
    }

    private void makeMethodTest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    test1("name:" + i, 1);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
