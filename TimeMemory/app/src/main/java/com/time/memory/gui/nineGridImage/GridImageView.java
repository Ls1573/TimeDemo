package com.time.memory.gui.nineGridImage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.time.memory.R;


/**
 * @author @Qiu
 * @version V1.0
 * @Description: 带边框的ImageView
 * @date 2016/10/25 11:14
 */
public class GridImageView extends ImageView {

	private boolean isBorder = false;

	private int co;
	private int borderwidth;

	public GridImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridImageView(Context context, boolean isBorder) {
		super(context);
		this.isBorder = isBorder;
		this.co = context.getResources().getColor(R.color.grey_E9);
		this.borderwidth = 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isBorder) {
			// 画边框
			Rect rec = canvas.getClipBounds();
			Paint paint = new Paint();
			//设置边框颜色
			paint.setColor(co);
			paint.setStyle(Paint.Style.STROKE);
			//设置边框宽度
			paint.setStrokeWidth(borderwidth);
			canvas.drawRect(rec, paint);
//			canvas.drawRect(rec, paint);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		setImageDrawable(null);
	}

}