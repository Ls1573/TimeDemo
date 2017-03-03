package com.time.memory.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.gui.datapicker.MyDatePicker;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description: 自定义日期选择弹窗
 * @date 2016-9-8下午1:29:53
 * ==============================
 */
public class DatePickerSheet extends Fragment implements OnClickListener,
		OnDateChangedListener {

	private static final String TAG = "DatePickerSheet";
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final String ARG_YEAR = "date_year";
	private static final String ARG_MONTH = "date_monty";
	private static final String ARG_DAY = "date_day";
	private static final String ARG_TITLEPAGIND = "titlePadding";
	private static final String ARG_BOTTOMPADDING = "bottomPadding";

	private MyDatePicker mDatePicker;
	private Calendar mCalendar;
	private boolean mTitleNeedsUpdate = true;
	private View mBg;// 背景色
	private ViewGroup mGroup;// 全局视图
	private View mView;// 主视图
	private TextView app_submit;// 提交
	private TextView date_tv;// 当前日期
	private LinearLayout mPanel;
	private DatePickerListener mListener;
	private boolean mDismissed = true;

	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;

	public void show(FragmentManager manager, String tag) {
		if (!mDismissed) {
			return;
		}
		mDismissed = false;
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void dismiss() {
		if (mDismissed) {
			return;
		}
		mDismissed = true;
		getFragmentManager().popBackStack();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(this);
		ft.commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = getActivity().getCurrentFocus();
			if (focusView != null) {
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}
		// 获取属性配置
		// 创建一个View
		mView = createView();
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView();

		mGroup.addView(mView);
		// 启动动画效果（淡入）
		mBg.startAnimation(createAlphaInAnimation());
		// 启动动画效果（重下面进入）
		mPanel.startAnimation(createTranslationInAnimation());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		mPanel.startAnimation(createTranslationOutAnimation());
		mBg.startAnimation(createAlphaOutAnimation());
		mView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mGroup.removeView(mView);
			}
		}, ALPHA_DURATION);
		if (mListener != null) {
			mListener.onDismiss();
		}
		super.onDestroyView();
	}

	private View createView() {
		// 初始化
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.view_date_picker, null);
		view.setPadding(0, titlePadding(), 0, bottomPadding());
		mBg = view.findViewById(R.id.view_bg);
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setOnClickListener(this);

		// 展示区域
		mPanel = (LinearLayout) view.findViewById(R.id.date_panel);
		app_submit = (TextView) view.findViewById(R.id.app_submit);
		date_tv = (TextView) view.findViewById(R.id.date_tv);
		// 日期选择
		int year = getYear();
		int month = getMonth();
		int day = getDay();
		mCalendar = Calendar.getInstance();
		// 获取当前对应的年、月、日的信息
//		year = mCalendar.get(Calendar.YEAR);
//		month = mCalendar.get(Calendar.MONTH);
//		day = mCalendar.get(Calendar.DAY_OF_MONTH);

		CLog.e(TAG, "year:" + year + "  month:" + month + "     day:" + day);
		mDatePicker = (MyDatePicker) view.findViewById(R.id.date_picker);
		setDatePickerDividerColor(mDatePicker);
		setWeight(mDatePicker);
		mDatePicker.init(year, month, day, this);
		setDateTv(year, month, day);
		app_submit.setOnClickListener(this);
		return view;
	}

	@SuppressLint("NewApi")
	private void setWeight(DatePicker datePicker) {
		LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(DevUtils.dip2px(getActivity(), 30), 0,
				DevUtils.dip2px(getActivity(), 40), 0);
		((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
				.getChildAt(1).setLayoutParams(layoutParams);
	}

	/**
	 * 设置时间选择器的分割线颜色
	 *
	 * @param datePicker
	 */
	private void setDatePickerDividerColor(DatePicker datePicker) {
		// 获取 mSpinners
		LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
		// 获取 NumberPicker
		LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
		for (int i = 0; i < mSpinners.getChildCount(); i++) {
			NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

			Field[] pickerFields = NumberPicker.class.getDeclaredFields();
			for (Field pf : pickerFields) {
				if (pf.getName().equals("mSelectionDivider")) {
					pf.setAccessible(true);
					try {
						pf.set(picker, new ColorDrawable(getActivity()
								.getResources().getColor(R.color.yellow_E3)));
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
		mCalendar.set(year, monthOfYear, dayOfMonth);
		setDateTv(year, monthOfYear, dayOfMonth);
	}

	/**
	 * 设置日期
	 *
	 * @param
	 * @reurn
	 */
	private void setDateTv(int year, int month, int day) {
		String mMonth = null;
		String mDay = null;
		if (month < 9)
			mMonth = "0" + (month + 1);
		else
			mMonth = String.valueOf(month + 1);
		if (day < 10)
			mDay = "0" + day;
		else {
			mDay = String.valueOf(day);
		}
		date_tv.setText(year + "-" + mMonth + "-" + mDay + "    " + setWeek());
	}

	/**
	 * 计算星期
	 *
	 * @return
	 */
	private String setWeek() {
		// 一周第一天是否为星期天
		boolean isFirstSunday = (mCalendar.getFirstDayOfWeek() == Calendar.SUNDAY);
		// 获取周几
		int weekDay = mCalendar.get(Calendar.DAY_OF_WEEK);
		String week = "周日";
		// 若一周第一天为星期天，则-1
		if (isFirstSunday) {
			weekDay = weekDay - 1;
			if (weekDay == 0) {
				weekDay = 7;
			}
		}
		// 打印周几
		switch (weekDay) {
			case 1:
				week = "周一";
				break;
			case 2:
				week = "周二";
				break;
			case 3:
				week = "周三";
				break;
			case 4:
				week = "周四";
				break;
			case 5:
				week = "周五";
				break;
			case 6:
				week = "周六";
				break;
			case 7:
				week = "周日";
				break;
		}
		return week;
	}

	@Override
	public void onClick(View v) {
		// 点击遮罩层&&外部不可以点击的情况
		if ((v.getId() == mBg.getId() || v.getId() == app_submit.getId())
				&& getCancelableOnTouchOutside()) {
			dismiss();
			if (v.getId() == app_submit.getId()) {
				// 提交
				date_tv.getText().toString();
				mListener.onSubmit(date_tv.getText().toString());
			}
		}
	}

	public static Builder createBuilder(Context context,
										FragmentManager fragmentManager) {
		return new Builder(context, fragmentManager);
	}

	public static class Builder {
		private Context mContext;
		private FragmentManager mFragmentManager;
		private String mTag = "actionSheet";

		private int year;
		private int month;
		private int day;

		private int titlePadding;
		private int bottomPadding;

		private boolean mCancelableOnTouchOutside;
		private DatePickerListener mListener;

		public Builder(Context context, FragmentManager fragmentManager) {
			mContext = context;
			mFragmentManager = fragmentManager;
		}

		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}

		public Builder setTitlePadding(int titlePadding) {
			this.titlePadding = titlePadding;
			return this;
		}

		public Builder setBottomPadding(int bottomPadding) {
			this.bottomPadding = bottomPadding;
			return this;
		}

		public Builder setListener(DatePickerListener listener) {
			this.mListener = listener;
			return this;
		}

		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public Builder setYear(int yy) {
			this.year = yy;
			return this;
		}

		public Builder setMonty(int mm) {
			this.month = mm - 1;
			return this;
		}

		public Builder setDay(int dd) {
			this.day = dd;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE, mCancelableOnTouchOutside);
			bundle.putInt(ARG_YEAR, year);
			bundle.putInt(ARG_MONTH, month);
			bundle.putInt(ARG_DAY, day);
			bundle.putInt(ARG_TITLEPAGIND, titlePadding);
			bundle.putInt(ARG_BOTTOMPADDING, bottomPadding);
			return bundle;
		}

		public DatePickerSheet show() {
			DatePickerSheet actionSheet = (DatePickerSheet) Fragment
					.instantiate(mContext, DatePickerSheet.class.getName(),
							prepareArguments());
			actionSheet.setDatePickerListener(mListener);
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}
	}

	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	// 创建进入动画
	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	// 创建淡入动画
	private Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	// 创建退出位移动画
	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}

	// 创建淡出的动画
	private Animation createAlphaOutAnimation() {
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}

	public void setDatePickerListener(DatePickerListener listener) {
		mListener = listener;
	}

	private int getYear() {
		return getArguments().getInt(ARG_YEAR);
	}

	private int getMonth() {
		return getArguments().getInt(ARG_MONTH);
	}

	private int getDay() {
		return getArguments().getInt(ARG_DAY);
	}


	private int titlePadding() {
		return getArguments().getInt(ARG_TITLEPAGIND);
	}

	private int bottomPadding() {
		return getArguments().getInt(ARG_BOTTOMPADDING);
	}

	public static interface DatePickerListener {
		/**
		 * 确定
		 */
		void onSubmit(String myDate);

		/**
		 * 取消
		 */
		void onDismiss();
	}

}