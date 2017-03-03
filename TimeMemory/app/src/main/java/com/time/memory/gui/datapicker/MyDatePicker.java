package com.time.memory.gui.datapicker;

import android.content.Context;
import android.util.AttributeSet;

import com.time.memory.R;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/12/1 8:29
 */
public class MyDatePicker extends android.widget.DatePicker {
	public MyDatePicker(Context context) {
		this(context, null);
	}

	public MyDatePicker(Context context, AttributeSet attrs) {
		this(context, attrs, R.style.AppDatePicker);
	}

	public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, R.style.AppDatePicker);
	}
}
