package com.egame.cn.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TextProgressBar extends ProgressBar {

	private Paint mPaint;
	private String text = "";
	TextView textView;

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

		/*Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		
		int x = (getWidth() / 2) - rect.centerX();
		int y = (getHeight() / 2) - rect.centerY();
		if (getMax() > 0) {
			canvas.drawText(this.text, getProgress() / getMax() * getWidth() + 5, y, this.mPaint);
		}else{
			canvas.drawText(this.text, 5, y, this.mPaint);
		}*/
		
	/*	if (textView != null) {
			textView.setText(text);
			LayoutParams p = textView.getLayoutParams();
			
			//textView.msetLayoutParams(new LayoutParams(7, 5).);
		}*/
	}
	
	@Override
	public synchronized void setProgress(int progress) {
	//	setText(progress);
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
			
			float i =(float) progress / (float)this.getMax() * 100;
			this.text  = String.valueOf((int)i) + "%";
		}
	}

}
