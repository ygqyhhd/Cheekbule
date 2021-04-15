package com.meeblue.celsius;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meeblue.celsius.tool.Myview;

public class RobotActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.robot);
        FrameLayout mFrameLayout = findViewById(R.id.framelayout);
        //将当前的布局和内部类相关联
        mFrameLayout.addView(new Myview(this,500));
    }
    //内部类用于绘画

}

