package com.wyl.appdoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wyl.doctor.BeansCache;
import com.wyl.doctor.MethodBean;
import com.wyl.doctor.method.MethodRecordUtil;

public class MainActivity extends AppCompatActivity {
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                test1("时间：" + System.currentTimeMillis(), count);
            }
        });

    }

    private void test1(String name, int age) {
        Object[] args = new Object[]{name, age};
        MethodRecordUtil.onStart(this.getClass(), "test1", args);
        for (int i = 0; i < 1000; i++) {
            i++;
        }

        MethodRecordUtil.onEnd();
    }
}
