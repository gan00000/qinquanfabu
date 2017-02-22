package com.egame.cn.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar {

	private Paint mPaint;
	private String text = "";

	public TextProgressBar(Context context) {
		super(context);
		initPaint();
	}

	@SuppressLint("NewApi")
	public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initPaint();
	}

	public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initPaint();
	}

	public TextProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ProgressBar#onDraw(android.graphics.Canvas)
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, getProgress()/getMax() * getWidth(), y, this.mPaint);
	}
	
	@Override
	public synchronized void setProgress(int progress) {
		//setText(progress);
		super.setProgress(progress);
	}

	private void initPaint(){
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.RED);
		this.mPaint.setTextSize(getHeight());
		this.text = "downloading...";
	}
	
	public void setTextSize(float textSize){
		this.mPaint.setTextSize(textSize);
	}

/*	private void setText() {
		// 设置文字内容
		setText(this.getProgress());
	}*/

	private void setText(int progress) {
		if (progress == 0) {
			this.text  = "0%";
		}else{
			
			int i = (progress * 100) / this.getMax();
			this.text  = String.valueOf(i) + "%";
		}
	}

}
