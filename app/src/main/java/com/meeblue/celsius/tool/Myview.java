package com.meeblue.celsius.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.meeblue.celsius.R;
import com.meeblue.celsius.util.Colors;

public class Myview extends View {

    private static final String TAG = "CircleView";
    private Paint mPaint;
    private RectF oval;
    private int dx=0;
    public Myview(Context context,int dx) {

        super(context);
        initView();
        this.dx=dx;

    }
    private void initView() {
        mPaint= new Paint();

        mPaint.setAntiAlias(true);
        oval = new RectF();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub

        super.onDraw(canvas);
        //把整张画布绘制成白色
        canvas.drawColor(Color.BLACK);
        //去锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor( getResources().getColor(R.color.cikirRobot));
        mPaint.setStrokeWidth(3);
        canvas.drawLine(0, 60, 400, 60, mPaint);
        canvas.translate(dx,0);

        //绘制圆形

        int with = 60;
        int height = 60;

        Log.d(TAG,with+"_"+height);

        float radius = with / 2;
//        cx：圆心的x坐标。
//        cy：圆心的y坐标。
//        radius：圆的半径。
//        paint：绘制时所使用的画笔。

        canvas.drawCircle(with / 2, with / 2, radius, mPaint);

        canvas.drawCircle(with / 4, with*2 / 5, with / 16, mPaint);

        canvas.drawCircle(with*3 / 4, with*2 / 5, with / 16, mPaint);


        //左边  上边  右边 下边     左上 对应的坐标 与 右下对应的坐标对角线对应的对应的矩形（椭圆在矩形中）   左右距离是椭圆的长  上下距离是椭圆的宽
        oval.set(with/3, with/3, with*3/4 , with*3/4);
        //绘制圆弧  圆弧所在的椭圆对象 圆弧的起始角度 圆弧的角度 是否显示半径连线
        canvas.drawArc(oval, 0, 180, false, mPaint);  //根据进度画圆弧

        mPaint.setStrokeWidth(10);
        canvas.drawCircle(with / 2, with, with / 16, mPaint);
        canvas.drawRect(100,0, 0, 0, mPaint);



    }

}