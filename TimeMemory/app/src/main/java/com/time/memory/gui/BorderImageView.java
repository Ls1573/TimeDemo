package com.time.memory.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.time.memory.R;

/**
 * 加边框的ImageView
 */
public class BorderImageView extends ImageView {
	private int co;
	private int borderwidth;

	public BorderImageView(Context context) {
		this(context, null);
	}


	public BorderImageView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public BorderImageView(Context context, AttributeSet attrs,
						   int defStyle) {
		super(context, attrs, defStyle);
		this.co = context.getResources().getColor(R.color.yellow_D0);
		this.borderwidth = 8;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 画边框
//		Rect rec = canvas.getClipBounds();
//		Paint paint = new Paint();
//		//设置边框颜色
//		paint.setColor(co);
//		paint.setStyle(Paint.Style.STROKE);
//		//设置边框宽度
//		paint.setStrokeWidth(borderwidth);
//		if (isActivated()) {
//			canvas.drawRect(rec, paint);
//		}
	}

	@Override
	protected void dispatchSetActivated(boolean activated) {
		super.dispatchSetActivated(activated);
		invalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		setImageDrawable(null);
	}
}
