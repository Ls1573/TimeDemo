package com.time.memory.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.net.ExecutorManager;

import java.util.concurrent.ExecutorService;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:二维码弹窗
 * @date 2016/10/18 12:04
 */
public class QrCodeSheet extends Fragment implements OnClickListener {

	private static final String TAG = "QrCodeSheet";
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final String MESSAGE = "data_msg";
	private static final String ARG_TITLEPAGIND = "titlePadding";
	private static final String ARG_BOTTOMPADDING = "bottomPadding";

	private boolean mTitleNeedsUpdate = true;
	private View mBg;// 背景色
	private ViewGroup mGroup;// 全局视图
	private View mView;// 主视图
	private ImageView qrcode_iv;// 二维码图
	private boolean mDismissed = true;

	private static final int ALPHA_DURATION = 300;
	private static final int SCALE_DURATION = 200;
	private static final int SCALEOUt_DURATION = 150;

	private static final int SUCCESS = 1;// 成功
	private static final int FINAL = -1;// 失败

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
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		mBg.startAnimation(createAlphaOutAnimation());
		qrcode_iv.setVisibility(View.GONE);
		qrcode_iv.startAnimation(createScaleOutAnimation());
		mView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mGroup.removeView(mView);
			}
		}, ALPHA_DURATION);
		super.onDestroyView();
	}

	private View createView() {
		// 初始化
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.view_qrcode, null);
		view.setPadding(0, titlePadding(), 0, bottomPadding());
		mBg = view.findViewById(R.id.view_bg);
		qrcode_iv = (ImageView) view.findViewById(R.id.qrcode_iv);
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setOnClickListener(this);
		createQrcode();
		return view;
	}

	/**
	 * 创建QrCode
	 */
	private void createQrcode() {
		ExecutorService threadPool = ExecutorManager.getInstance();
		// 放入到主线程
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == SUCCESS) {
					Bitmap bitmap = (Bitmap) msg.obj;
					qrcode_iv.setImageBitmap(bitmap);
					qrcode_iv.setVisibility(View.VISIBLE);
					qrcode_iv.startAnimation(createScaleInAnimation());
				} else {
					//出异常了
				}
			}
		};
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					BitmapDrawable bd = (BitmapDrawable) MainApplication.getContext().getResources().getDrawable(R.drawable.headpic);
					Bitmap logo = bd.getBitmap();
					Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(getData(), BGAQRCodeUtil.dp2px(MainApplication.getContext(), 220), R.color.black_01, logo);
					handler.sendMessage(handler.obtainMessage(SUCCESS, bitmap));
				} catch (Exception e) {
					handler.sendMessage((handler.obtainMessage(FINAL, null)));
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// 点击遮罩层&&外部不可以点击的情况
		if ((v.getId() == mBg.getId()) && getCancelableOnTouchOutside()) {
			dismiss();
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
		private int titlePadding;
		private int bottomPadding;

		private String msg;
		private boolean mCancelableOnTouchOutside;

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

		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public Builder setData(String msg) {
			this.msg = msg;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE, mCancelableOnTouchOutside);
			bundle.putString(MESSAGE, msg);
			bundle.putInt(ARG_TITLEPAGIND, titlePadding);
			bundle.putInt(ARG_BOTTOMPADDING, bottomPadding);
			return bundle;
		}

		public QrCodeSheet show() {
			QrCodeSheet actionSheet = (QrCodeSheet) Fragment
					.instantiate(mContext, QrCodeSheet.class.getName(),
							prepareArguments());
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}
	}

	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	// 创建淡入动画
	private Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	// 创建淡出的动画
	private Animation createAlphaOutAnimation() {
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}

	// 创建缩放的动画
	private Animation createScaleInAnimation() {
		ScaleAnimation an = new ScaleAnimation(0.3f, 1f, 0.3f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		an.setDuration(SCALE_DURATION);
		return an;
	}

	// 创建缩放的动画
	private Animation createScaleOutAnimation() {
		ScaleAnimation an = new ScaleAnimation(1f, 0.3f, 1f, 0.3f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		an.setDuration(SCALEOUt_DURATION);
		return an;
	}

	private String getData() {
		return getArguments().getString(MESSAGE);
	}

	private int titlePadding() {
		return getArguments().getInt(ARG_TITLEPAGIND);
	}

	private int bottomPadding() {
		return getArguments().getInt(ARG_BOTTOMPADDING);
	}
}