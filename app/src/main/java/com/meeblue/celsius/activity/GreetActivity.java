package com.meeblue.celsius.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meeblue.celsius.MainActivity;
import com.meeblue.celsius.R;

import java.util.Timer;
import java.util.TimerTask;

public class GreetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greet_min);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        initUI();
    }

    private void initUI() {
        //利用timer让此界面延迟3秒后跳转，timer有一个线程，该线程不断执行task
        Timer timer = new Timer();
        //TimerTask实现runnable接口，TimerTask类表示在一个指定时间内执行的task
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {//发送intent实现页面跳转，第一个参数为当前页面的context，第二个参数为要跳转的主页
                Intent intent = new Intent(GreetActivity.this, MainActivity.class);
                startActivity(intent);
                GreetActivity.this.finish();//跳转后关闭当前欢迎页面
            }
        };
        timer.schedule(timerTask,3000);//调度执行timerTask，第二个参数传入延迟时间（毫秒）
    }
}
