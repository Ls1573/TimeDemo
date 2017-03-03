package com.time.memory.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.time.memory.R;

/**
 * @author Qiu
 * @version V1.0
 * @Description:封装EditText_左hint图片
 * @date 2016/10/8 8:26
 */
public class WriterEditText extends EditText {
	private static final String TAG = "WriterEditText";
	float imageSize = 0;//图片大小
	float textSize = 0;//文字大小
	int textColor = 0x4A4A4A;//字体颜色
	String hintText = "抒写你的记忆...";//hintText
	Drawable mDrawable;//指示图
	Paint paint;//

	public WriterEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitResource(context, attrs);
		InitPaint();
	}

	private void InitResource(Context context, AttributeSet attrs) {
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.writerEdit);
		float density = context.getResources().getDisplayMetrics().density;
		imageSize = mTypedArray.getDimension(R.styleable.writerEdit_imagewidth, 18 * density + 0.5F);
		textColor = mTypedArray.getColor(R.styleable.writerEdit_textColor, textColor);
		textSize = mTypedArray.getDimension(R.styleable.writerEdit_textSize, 14 * density + 0.5F);
		hintText = mTypedArray.getString(R.styleable.writerEdit_hintText);

		mTypedArray.recycle();
	}

	private void InitPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		DrawSearchIcon(canvas);
	}

	private void DrawSearchIcon(Canvas canvas) {
		//没有输入内容的时候
		if (this.getText().toString().length() == 0) {
			float textWidth = paint.measureText(hintText);
			float textHeight = paint.measureText(hintText);
			//左起始位置
			float dx = getPaddingLeft();
			//上起始位置
			float dy = getPaddingTop();
			//画板操作
			canvas.save();
			canvas.translate(getScrollX() + dx, getScrollY() + dy);
			//绘制图片
			if (mDrawable != null) {
				mDrawable.draw(canvas);
			}
			//图片的宽度
			canvas.drawText(hintText, getScrollX() + imageSize + 20, dy - 5, paint);
			canvas.restore();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		//加载图片
		if (mDrawable == null) {
			try {
				mDrawable = getContext().getResources().getDrawable(R.drawable.circle_add);
				mDrawable.setBounds(0, 0, (int) imageSize, (int) imageSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		//释放资源
		if (mDrawable != null) {
			mDrawable.setCallback(null);
			mDrawable = null;
		}
		super.onDetachedFromWindow();
	}
}
