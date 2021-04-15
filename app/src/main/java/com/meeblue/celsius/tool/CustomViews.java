package com.meeblue.celsius.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
 
public class CustomViews extends View implements Runnable {
 
	private Paint mPaint;
	private Context mContext;
	private int screenWidth;
	private int screenHeight;
	private int radial = 50;
 
	public CustomViews(Context context) {
		super(context, null);
		init();
	}
 
	public CustomViews(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
 
	/**
	 * 由于onDraw()方法会不停的绘制 所以需要定义一个初始化画笔方法 避免导致不停创建造成内存消耗过大
	 */
	private void init() {
		mPaint = new Paint();
		// 设置画笔为抗锯齿
		mPaint.setAntiAlias(true);
		// 设置颜色为红色
		mPaint.setColor(Color.GREEN);
		/**
		 * 画笔样式分三种： 1.Paint.Style.STROKE：描边 2.Paint.Style.FILL_AND_STROKE：描边并填充
		 * 3.Paint.Style.FILL：填充
		 */
		mPaint.setStyle(Paint.Style.STROKE);
		/**
		 * 设置描边的粗细，单位：像素px 注意：当setStrokeWidth(0)的时候描边宽度并不为0而是只占一个像素
		 */
		mPaint.setStrokeWidth(2);
		/**
		 * 获取屏幕的宽高
		 */
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
	}
 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/**
		 * 绘制圆环drawCircle的前两个参数表示圆心的XY坐标， 这里我们用到了一个工具类获取屏幕尺寸以便将其圆心设置在屏幕中心位置，
		 * 第三个参数是圆的半径，第四个参数则为我们的画笔
		 * 
		 */
		canvas.drawCircle(screenWidth / 2, screenHeight / 2, radial, mPaint);
 
	}
 
	@Override
	public void run() {
		/**
		 * 使用while循环不断的刷新view的半径
		 * 当半径小于100每次增加10 invalidate()重绘view会报错
		 * android.view.ViewRootImpl$CalledFromWrongThreadException 是非主线程更新UI
		 * Android给提供postInvalidate();快捷方法来重绘view 
		 *  现在明白了invalidate和postInvalidate的小区别了吧
		 */ 
		while (true) {
			if (radial <= 170) {
				radial += 20;
				postInvalidate();
			}else{
				radial = 50;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}