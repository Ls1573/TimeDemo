package com.time.memory.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:字母筛选
 * @date 2016/9/12 13:52
 * ==============================
 */
public class SideLetterBar extends View {
	private String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private ArrayList<String> sLetters = new ArrayList<>();
	private int choose = -1;
	private Paint paint = new Paint();
	private boolean showBg = false;
	private OnLetterChangedListener onLetterChangedListener;
	private TextView overlay;

	public SideLetterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sLetters.addAll(Arrays.asList(b));
		//sLetters.add("*");
	}

	public SideLetterBar(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public SideLetterBar(Context context) {
		this(context, null);
	}

	/**
	 * 设置悬浮的textview
	 *
	 * @param overlay
	 */
	public void setOverlay(TextView overlay) {
		this.overlay = overlay;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBg) {
			canvas.drawColor(Color.TRANSPARENT);
		}
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / sLetters.size();
		for (int i = 0; i < sLetters.size(); i++) {
			paint.setTextSize(getResources().getDimension(R.dimen.tv_12_sp));
			paint.setColor(getResources().getColor(R.color.browen_B9));
			paint.setAntiAlias(true);
			float xPos = width / 2 - paint.measureText(sLetters.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(sLetters.get(i), xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnLetterChangedListener listener = onLetterChangedListener;
		final int c = (int) (y / getHeight() * sLetters.size());

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBg = true;
				if (oldChoose != c && listener != null) {
					if (c >= 0 && c < sLetters.size()) {
						listener.onLetterChanged(sLetters.get(c));
						choose = c;
						invalidate();
						if (overlay != null) {
							overlay.setVisibility(VISIBLE);
							overlay.setText(sLetters.get(c));
						}
					}
				}

				break;
			case MotionEvent.ACTION_MOVE:
				if (oldChoose != c && listener != null) {
					if (c >= 0 && c < sLetters.size()) {
						listener.onLetterChanged(sLetters.get(c));
						choose = c;
						invalidate();
						if (overlay != null) {
							overlay.setVisibility(VISIBLE);
							overlay.setText(sLetters.get(c));
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBg = false;
				choose = -1;
				invalidate();
				if (overlay != null) {
					overlay.setVisibility(GONE);
				}
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
		this.onLetterChangedListener = onLetterChangedListener;
	}

	public interface OnLetterChangedListener {
		void onLetterChanged(String letter);
	}

	public void setLetters(ArrayList<String> sLetters) {
		this.sLetters.clear();
		this.sLetters.addAll(sLetters);
		invalidate();
	}
}
