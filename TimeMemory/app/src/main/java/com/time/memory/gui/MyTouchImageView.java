package com.time.memory.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:定義被激活狀態圖層
 * @date 2016/9/27 8:51
 */
public class MyTouchImageView extends ImageView {

	public MyTouchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyTouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyTouchImageView(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isActivated()) {
			canvas.drawColor(0x66000000);
		}
	}

	@Override
	protected void dispatchSetActivated(boolean activated) {
		super.dispatchSetActivated(activated);
		invalidate();
	}

//	@Override
//	protected void onDetachedFromWindow() {
//		super.onDetachedFromWindow();
//		setImageDrawable(null);
//	}

}
