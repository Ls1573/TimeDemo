package com.time.memory.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Qiu
 * @version V1.0
 * @Description:等高TextVeiw
 * @date 2016/9/20 13:54
 */
public class MyTextView extends TextView {
	public MyTextView(Context context) {
		super(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}
}
