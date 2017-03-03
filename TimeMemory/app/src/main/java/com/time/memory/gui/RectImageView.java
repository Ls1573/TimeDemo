package com.time.memory.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:定義被激活狀態圖層
 * @date 2016/9/27 8:51
 */
public class RectImageView extends ImageView {

	public RectImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RectImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RectImageView(Context context) {
		super(context);
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
