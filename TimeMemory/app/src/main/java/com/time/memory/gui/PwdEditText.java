package com.time.memory.gui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.time.memory.R;

/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0 ==============================
 * @Package com.time.memory.gui
 * @Description: 自定义显示隐藏的EditText
 * @date 2016-9-5 下午9:25:55
 */
public class PwdEditText extends EditText {
	// EditText右侧的显示隐藏按键
	private Drawable mVisableDrawable;
	private Drawable inVisableDrawable;
	private boolean hasFoucs = true;

	public PwdEditText(Context context) {
		this(context, null);
	}

	public PwdEditText(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public PwdEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
		mVisableDrawable = getCompoundDrawables()[2];
		inVisableDrawable = getCompoundDrawables()[2];

		if (inVisableDrawable == null) {
			inVisableDrawable = getResources().getDrawable(
					R.drawable.visable_pwd);
		}
		if (mVisableDrawable == null) {
			mVisableDrawable = getResources().getDrawable(
					R.drawable.invisable_pwd);
		}

		mVisableDrawable.setBounds(0, 0, mVisableDrawable.getIntrinsicWidth(),
				mVisableDrawable.getIntrinsicHeight());
		inVisableDrawable.setBounds(0, 0,
				inVisableDrawable.getIntrinsicWidth(),
				inVisableDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		setVisable(true);
		// 设置焦点改变的监听
		// setOnFocusChangeListener(this);
	}

	/**
	 * @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标 event.getX()
	 * 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth() 获取控件的宽度 getHeight()
	 * 获取控件的高度 getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离 getPaddingRight()
	 * 获取删除图标右边缘到控件右边缘的距离 isInnerWidth: getWidth() - getTotalPaddingRight()
	 * 计算删除图标左边缘到控件左边缘的距离 getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
	 * isInnerHeight: distance 删除图标顶部边缘到控件顶部边缘的距离 distance + height
	 * 删除图标底部边缘到控件顶部边缘的距离
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				Rect rect = getCompoundDrawables()[2].getBounds();
				int height = rect.height();
				int distance = (getHeight() - height) / 2;
				boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight())
						&& x < (getWidth() - getPaddingRight());
				boolean isInnerHeight = y > distance && y < (distance + height);
				if (isInnerWidth && isInnerHeight) {
					setVisable(hasFoucs);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private void setVisable(boolean visible) {
		hasFoucs = !visible;
		Drawable right = hasFoucs ? mVisableDrawable : inVisableDrawable;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
		if (hasFoucs)
			// 显示密码
			this.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		else
			// 隐藏密码
			this.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		this.setSelection(this.getText().length() == 0 ? 0 : this.getText().length());

	}

}
