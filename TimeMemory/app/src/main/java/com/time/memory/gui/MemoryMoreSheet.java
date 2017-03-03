package com.time.memory.gui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.time.memory.R;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MemoryMoreSheet extends Fragment implements View.OnClickListener {
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final String ARG_ISEDIT_ONTOUCHOUTSIDE = "isEDIT";
	private static final String ARG_ISSHARED_ONTOUCHOUTSIDE = "isShared";
	private static final String ARG_ISDELETE_ONTOUCHOUTSIDE = "isDelete";
	private static final String ARG_ISDELETE_ISFORWARD = "isForward";
	private static final String ARG_TITLEPAGIND = "titlePadding";
	private static final String ARG_BOTTOMPADDING = "bottomPadding";

	private View mBg;//背景色
	private ViewGroup mGroup;//全局视图
	private View mView;//主视图

	private LinearLayout mPanel;
	private onMemoryMoreListener mListener;
	private boolean mDismissed = true;

	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;

	private LinearLayout wxLL;//微信
	private LinearLayout friendsCircleLL;//朋友圈
	private LinearLayout qqFriendsLL;//QQ好友
	private LinearLayout qqSpaceLL;//QQ空间
	private LinearLayout wbLL;//微博
	private LinearLayout editorLL;//编辑
	private LinearLayout sendLL;//转发
	private LinearLayout deteleLL;//删除
	private LinearLayout reportLL;//举报
	private LinearLayout shared_edot_ll;

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
		//隐藏输入法
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = getActivity().getCurrentFocus();
			if (focusView != null) {
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}

		//获取属性配置
		//创建一个View
		mView = createView();
		mView.setFitsSystemWindows(true);
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView();
		mGroup.addView(mView);
		//启动动画效果（淡入）
		mBg.startAnimation(createAlphaInAnimation());
		// 启动动画效果（重下面进入）
		mPanel.startAnimation(createTranslationInAnimation());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	private boolean isEdit() {
		return getArguments().getBoolean(ARG_ISEDIT_ONTOUCHOUTSIDE);
	}

	private boolean isForward() {
		return getArguments().getBoolean(ARG_ISDELETE_ISFORWARD);
	}

	private boolean isDelete() {
		return getArguments().getBoolean(ARG_ISDELETE_ONTOUCHOUTSIDE);
	}

	private boolean isShared() {
		return getArguments().getBoolean(ARG_ISSHARED_ONTOUCHOUTSIDE);
	}

	private int titlePadding() {
		return getArguments().getInt(ARG_TITLEPAGIND);
	}

	private int bottomPadding() {
		return getArguments().getInt(ARG_BOTTOMPADDING);
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
			mListener.onCancle();
		}
		super.onDestroyView();
	}

	private View createView() {
		//初始化
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.view_memory_more, null);

		view.setPadding(0, titlePadding(), 0, bottomPadding());

		mBg = view.findViewById(R.id.view_bg);
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setOnClickListener(this);
		//展示区域
		mPanel = (LinearLayout) view.findViewById(R.id.date_panel);

		wxLL = (LinearLayout) view.findViewById(R.id.view_memory_more_wx);
		friendsCircleLL = (LinearLayout) view.findViewById(R.id.view_memory_more_friends_circle);
		qqFriendsLL = (LinearLayout) view.findViewById(R.id.view_memory_more_qq_friends);
		qqSpaceLL = (LinearLayout) view.findViewById(R.id.view_memory_more_qq_space);
		wbLL = (LinearLayout) view.findViewById(R.id.view_memory_more_wb);
		editorLL = (LinearLayout) view.findViewById(R.id.view_memory_more_editor);
		deteleLL = (LinearLayout) view.findViewById(R.id.view_memory_more_detele);
		sendLL = (LinearLayout) view.findViewById(R.id.view_memory_more_send);
		reportLL = (LinearLayout) view.findViewById(R.id.view_memory_more_report);
		shared_edot_ll = (LinearLayout) view.findViewById(R.id.shared_edot_ll);

		//只是分享功能
		if (isShared()) {
			shared_edot_ll.setVisibility(View.GONE);
		} else {
			shared_edot_ll.setVisibility(View.VISIBLE);
		}

		//编辑
		if (isEdit()) {
			//编辑
			editorLL.setVisibility(View.VISIBLE);
			deteleLL.setVisibility(View.VISIBLE);
			sendLL.setVisibility(View.VISIBLE);
			reportLL.setVisibility(View.GONE);
		} else {
			//当前为举报
			editorLL.setVisibility(View.GONE);
			deteleLL.setVisibility(View.GONE);
			sendLL.setVisibility(View.GONE);
			reportLL.setVisibility(View.VISIBLE);
		}
		//删除
		if (isDelete()) {
			deteleLL.setVisibility(View.VISIBLE);
			sendLL.setVisibility(View.GONE);
			reportLL.setVisibility(View.GONE);
			editorLL.setVisibility(View.GONE);
		}

		wxLL.setOnClickListener(this);
		friendsCircleLL.setOnClickListener(this);
		qqFriendsLL.setOnClickListener(this);
		qqSpaceLL.setOnClickListener(this);
		wbLL.setOnClickListener(this);
		editorLL.setOnClickListener(this);
		reportLL.setOnClickListener(this);
		deteleLL.setOnClickListener(this);
		sendLL.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		// 点击遮罩层&&外部不可以点击的情况
		if ((v.getId() == mBg.getId()) && !getCancelableOnTouchOutside()) {
			return;
		}
		dismiss();
		switch (v.getId()) {
			case R.id.view_memory_more_wx:
				mListener.onSubmit(0);//微信
				break;
			case R.id.view_memory_more_friends_circle:
				mListener.onSubmit(1);//朋友圈
				break;
			case R.id.view_memory_more_qq_friends:
				mListener.onSubmit(2);//QQ好友
				break;
			case R.id.view_memory_more_qq_space:
				mListener.onSubmit(3);//QQ空间
				break;
			case R.id.view_memory_more_wb:
				mListener.onSubmit(4);//微博
				break;
			case R.id.view_memory_more_editor:
				mListener.onSubmit(5);//编辑
				break;
			case R.id.view_memory_more_report:
				mListener.onSubmit(6);//举报
				break;
			case R.id.view_memory_more_detele:
				mListener.onSubmit(7);//删除
				break;
			case R.id.view_memory_more_send:
				mListener.onSubmit(8);//转发
				break;
			case R.id.app_cancle:
				mListener.onCancle();//取消
				break;
		}
	}

	public static MemoryMoreBuilder createBuilder(Context context,
												  FragmentManager fragmentManager) {
		return new MemoryMoreBuilder(context, fragmentManager);
	}

	public static class MemoryMoreBuilder {
		private Context mContext;
		private FragmentManager mFragementManager;
		private String mTag = "MemoryMoreSheet";
		private boolean isEdit;
		private boolean isDelete;
		private boolean isForward;
		private boolean isShared;
		private boolean mCancelableOnTouchOutside;
		private int titlePadding;
		private int bottomPadding;

		private onMemoryMoreListener mListener;

		public MemoryMoreBuilder(Context mContext, FragmentManager mFragementManager) {
			this.mContext = mContext;
			this.mFragementManager = mFragementManager;
		}

		public MemoryMoreBuilder setmTag(String mTag) {
			this.mTag = mTag;
			return this;
		}

		public MemoryMoreBuilder setTitlePadding(int titlePadding) {
			this.titlePadding = titlePadding;
			return this;
		}

		public MemoryMoreBuilder setBottomPadding(int bottomPadding) {
			this.bottomPadding = bottomPadding;
			return this;
		}

		public MemoryMoreBuilder isEdit(boolean isEdit) {
			this.isEdit = isEdit;
			return this;
		}

		public MemoryMoreBuilder isDelete(boolean isDelete) {
			this.isDelete = isDelete;
			return this;
		}

		public MemoryMoreBuilder isForwad(boolean isForward) {
			this.isForward = isForward;
			return this;
		}


		public MemoryMoreBuilder isShared(boolean isShared) {
			this.isShared = isShared;
			return this;
		}

		public MemoryMoreBuilder setListener(onMemoryMoreListener listener) {
			this.mListener = listener;
			return this;
		}

		public MemoryMoreBuilder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE, mCancelableOnTouchOutside);
			bundle.putBoolean(ARG_ISEDIT_ONTOUCHOUTSIDE, isEdit);
			bundle.putBoolean(ARG_ISSHARED_ONTOUCHOUTSIDE, isShared);
			bundle.putBoolean(ARG_ISDELETE_ONTOUCHOUTSIDE, isDelete);
			bundle.putBoolean(ARG_ISDELETE_ISFORWARD, isForward);
			bundle.putInt(ARG_TITLEPAGIND, titlePadding);
			bundle.putInt(ARG_BOTTOMPADDING, bottomPadding);
			return bundle;
		}

		public MemoryMoreSheet show() {
			MemoryMoreSheet mSheet = (MemoryMoreSheet) Fragment.instantiate(mContext,
					MemoryMoreSheet.class.getName(), prepareArguments());
			mSheet.setOnMemoryListener(mListener);
			mSheet.show(mFragementManager, mTag);
			return mSheet;
		}

	}

	// 创建淡入动画
	private Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	//创建进入动画
	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation animation = new TranslateAnimation(type, 0, type, 0, type, 1, type, 0);
		animation.setDuration(TRANSLATE_DURATION);
		return animation;
	}

	//创建退出位移动画
	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation animation = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		animation.setDuration(TRANSLATE_DURATION);
		animation.setFillAfter(true);
		return animation;
	}

	//创建淡出的动画
	private Animation createAlphaOutAnimation() {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(ALPHA_DURATION);
		animation.setFillAfter(true);
		return animation;
	}

	public void setOnMemoryListener(onMemoryMoreListener listener) {
		mListener = listener;
	}

	public static interface onMemoryMoreListener {
		//确定
		void onSubmit(int postion);

		//取消
		void onCancle();
	}

}
