package com.time.memory.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Qiu
 * @version V1.0
 * @Description:正方形图片
 * @date 2016/9/20 13:54
 */
public class MyImageView extends ImageView {
	public MyImageView(Context context) {
		super(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		setImageDrawable(null);
	}
}
