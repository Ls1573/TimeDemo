package com.time.memory.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.time.memory.R;

import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * 弹窗(通用)
 */
@SuppressWarnings("ALL")
public class ActionSheet extends Fragment implements OnClickListener {

	private static final String ARG_CANCEL_BUTTON_TITLE = "cancel_button_title";
	private static final String ARG_OTHER_BUTTON_TITLES = "other_button_titles";
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final String ARG_TITLEPAGIND = "titlePadding";
	private static final String ARG_BOTTOMPADDING = "bottomPadding";

	private static final int CANCEL_BUTTON_ID = 100;
	private static final int BG_VIEW_ID = 10;
	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;

	private boolean mDismissed = true;
	private ActionSheetListener mListener;
	private View mView;
	private LinearLayout mPanel;
	private ViewGroup mGroup;
	private View mBg;
	private Attributes mAttrs;
	private boolean isCancel = true;

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
//		getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//		getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		// 隐藏输入法
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = getActivity().getCurrentFocus();
			if (focusView != null) {
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}
		// 获取属性配置
		mAttrs = readAttribute();
		// 创建一个View
		mView = createView();
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView();
		// 创建内容展示区域
		createItems();

		mGroup.addView(mView);
		// 启动动画效果（淡入）
		mBg.startAnimation(createAlphaInAnimation());
		// 启动动画效果（重下面进入）
		mPanel.startAnimation(createTranslationInAnimation());
		return super.onCreateView(inflater, container, savedInstanceState);
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

	/**
	 * @param
	 * @Description:创建展示区域
	 * @reurn
	 */
	private View createView() {
		FrameLayout parent = new FrameLayout(getActivity());
		parent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		parent.setPadding(0, titlePadding(), 0, bottomPadding());

		// 遮罩层
		mBg = new View(getActivity());
		mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setId(BG_VIEW_ID);
		mBg.setOnClickListener(this);

		// 内容区域
		mPanel = new LinearLayout(getActivity());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		mPanel.setLayoutParams(params);
		mPanel.setOrientation(LinearLayout.VERTICAL);

		parent.addView(mBg);
		parent.addView(mPanel);
		return parent;
	}

	/**
	 * @param
	 * @Description:创建选项
	 * @reurn
	 */
	@SuppressLint("NewApi")
	private void createItems() {
		// 其他按钮
		String[] titles = getOtherButtonTitles();
		if (titles != null) {
			for (int i = 0; i < titles.length; i++) {
				createButton(i, titles[i]);
			}
		}
		// 取消按钮
		TextView bt = new TextView(getActivity());
		bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
		bt.setId(CANCEL_BUTTON_ID);
		bt.setGravity(Gravity.CENTER);
		bt.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.mine_ll));
		bt.setText(getCancelButtonTitle());
		bt.setTextColor(mAttrs.cancelButtonTextColor);

		bt.setOnClickListener(this);
		LinearLayout.LayoutParams params = createButtonLayoutParams();
		params.topMargin = mAttrs.cancelButtonMarginTop;
		// 加入到内容区域里
		mPanel.addView(bt, params);
		mPanel.setBackgroundDrawable(mAttrs.background);
		mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding,
				mAttrs.padding);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void createButton(int i, String title) {
		TextView bt = new TextView(getActivity());
		bt.setId(CANCEL_BUTTON_ID + i + 1);
		bt.setOnClickListener(this);
		bt.setGravity(Gravity.CENTER);
		bt.setBackgroundDrawable(getActivity().getResources().getDrawable(
				R.drawable.mine_ll));
		bt.setText(title);
		bt.setTextColor(mAttrs.otherButtonTextColor);
		bt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mAttrs.actionSheetTextSize);
		LinearLayout.LayoutParams params = createButtonLayoutParams();
		if (i > 0) {
			params.topMargin = mAttrs.otherButtonSpacing;
		}
		mPanel.addView(bt, params);
	}

	public LinearLayout.LayoutParams createButtonLayoutParams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, DeviceUtils.dip2px(getActivity(), 55));
		return params;
	}


	private Drawable getOtherButtonBg() {
		return mAttrs.otherButtonBackground;
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
			mListener.onDismiss(this, isCancel);
		}
		super.onDestroyView();
	}

	/**
	 * @param
	 * @Description:读取参数配置
	 * @reurn
	 */
	private Attributes readAttribute() {
		Attributes attrs = new Attributes(getActivity());
		TypedArray a = getActivity().getTheme().obtainStyledAttributes(null,
				R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
		Drawable background = a
				.getDrawable(R.styleable.ActionSheet_actionSheetBackground);
		if (background != null) {
			attrs.background = background;
		}
		Drawable cancelButtonBackground = a
				.getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
		if (cancelButtonBackground != null) {
			attrs.cancelButtonBackground = cancelButtonBackground;
		}
		Drawable otherButtonMiddleBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonBackground);
		if (otherButtonMiddleBackground != null) {
			attrs.otherButtonBackground = otherButtonMiddleBackground;
		}

		attrs.cancelButtonTextColor = a.getColor(
				R.styleable.ActionSheet_cancelButtonTextColor,
				attrs.cancelButtonTextColor);
		attrs.otherButtonTextColor = a.getColor(
				R.styleable.ActionSheet_otherButtonTextColor,
				attrs.otherButtonTextColor);
		attrs.padding = (int) a.getDimension(
				R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
		attrs.otherButtonSpacing = (int) a.getDimension(
				R.styleable.ActionSheet_otherButtonSpacing,
				attrs.otherButtonSpacing);
		attrs.cancelButtonMarginTop = (int) a.getDimension(
				R.styleable.ActionSheet_cancelButtonMarginTop,
				attrs.cancelButtonMarginTop);
		attrs.actionSheetTextSize = a.getDimensionPixelSize(
				R.styleable.ActionSheet_actionSheetTextSize,
				(int) attrs.actionSheetTextSize);

		a.recycle();
		return attrs;
	}

	private String getCancelButtonTitle() {
		return getArguments().getString(ARG_CANCEL_BUTTON_TITLE);
	}

	private String[] getOtherButtonTitles() {
		return getArguments().getStringArray(ARG_OTHER_BUTTON_TITLES);
	}

	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	private int titlePadding() {
		return getArguments().getInt(ARG_TITLEPAGIND);
	}

	private int bottomPadding() {
		return getArguments().getInt(ARG_BOTTOMPADDING);
	}

	public void setActionSheetListener(ActionSheetListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		// 点击遮罩层&&外部不可以点击的情况
		if (v.getId() == BG_VIEW_ID && !getCancelableOnTouchOutside()) {
			return;
		}
		dismiss();
		// 回调
		if (v.getId() != CANCEL_BUTTON_ID && v.getId() != BG_VIEW_ID) {
			if (mListener != null) {
				mListener.onOtherButtonClick(this, v.getId() - CANCEL_BUTTON_ID
						- 1);
			}
			isCancel = false;
		}
	}

	public static Builder createBuilder(Context context,
										FragmentManager fragmentManager) {
		return new Builder(context, fragmentManager);
	}

	private static class Attributes {
		private Context mContext;

		public Attributes(Context context) {
			mContext = context;
			this.background = new ColorDrawable(Color.TRANSPARENT);
			ColorDrawable gray = new ColorDrawable(Color.GRAY);
			this.cancelButtonTextColor = Color.WHITE;
			this.otherButtonTextColor = Color.BLACK;
			this.padding = dp2px(20);
			this.otherButtonSpacing = dp2px(2);
			this.cancelButtonMarginTop = dp2px(10);
			this.actionSheetTextSize = dp2px(16);
		}

		private int dp2px(int dp) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					dp, mContext.getResources().getDisplayMetrics());
		}

		Drawable background;
		Drawable cancelButtonBackground;
		Drawable otherButtonBackground;
		int cancelButtonTextColor;
		int otherButtonTextColor;
		int padding;
		int otherButtonSpacing;
		int cancelButtonMarginTop;
		float actionSheetTextSize;
	}

	public static class Builder {
		private Context mContext;
		private FragmentManager mFragmentManager;
		private String mCancelButtonTitle;
		private String[] mOtherButtonTitles;
		private String mTag = "actionSheet";
		private boolean mCancelableOnTouchOutside;
		private int titlePadding;
		private int bottomPadding;

		private ActionSheetListener mListener;

		public Builder(Context context, FragmentManager fragmentManager) {
			mContext = context;
			mFragmentManager = fragmentManager;
		}

		public Builder setCancelButtonTitle(String title) {
			mCancelButtonTitle = title;
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

		public Builder setCancelButtonTitle(int strId) {
			return setCancelButtonTitle(mContext.getString(strId));
		}

		public Builder setOtherButtonTitles(String... titles) {
			mOtherButtonTitles = titles;
			return this;
		}

		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}

		public Builder setListener(ActionSheetListener listener) {
			this.mListener = listener;
			return this;
		}

		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putString(ARG_CANCEL_BUTTON_TITLE, mCancelButtonTitle);
			bundle.putStringArray(ARG_OTHER_BUTTON_TITLES, mOtherButtonTitles);
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE, mCancelableOnTouchOutside);
			bundle.putInt(ARG_TITLEPAGIND, titlePadding);
			bundle.putInt(ARG_BOTTOMPADDING, bottomPadding);
			return bundle;
		}

		public ActionSheet show() {
			ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(
					mContext, ActionSheet.class.getName(), prepareArguments());
			actionSheet.setActionSheetListener(mListener);
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}
	}

	public static interface ActionSheetListener {
		/**
		 * 取消
		 */
		void onDismiss(ActionSheet actionSheet, boolean isCancel);

		/**
		 * 点击其他按钮
		 */
		void onOtherButtonClick(ActionSheet actionSheet, int index);
	}

}