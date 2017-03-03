package com.time.memory.gui.loading;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.OnRetryListener;
import com.time.memory.util.NetUtils;

/**
 * 加载
 */
public class LoadingView extends FrameLayout implements OnClickListener {

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (View.GONE == visibility && mState == LoadingState.STATE_LOADING
				&& animation != null && animation.isRunning()) {
			animation.stop();
		}
	}

	public LoadingView(Context context) {
		super(context);
		mContext = context;
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;

	}

	private Context mContext;
	LinearLayout ll_over;// 提示

	LinearLayout ll_loading;// 加载中

	TextView tv_loaded;// 提示

	TextView tv_loading;// 加载中

	Button btn_loaded; // 重新加载

	ImageView iv_loading;// 加载动画图片

	ImageView iv_loaded;// 提示图片

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_loaded) {
			if (mOnRetryListener != null) {
//				setState(LoadingState.STATE_LOADING);
				mOnRetryListener.onRetry();
			}
		}
	}

	/**
	 * 加载中提示文字
	 */
	private String mLoadingText;
	private int mLoadingIco;

	public LoadingView withLoadingIco(int resId) {
		mLoadingIco = resId;
		return this;
	}

	/**
	 * 加载数据为空提示文字
	 */
	private String mLoaded_empty_text = "记忆是一本书，而你就是\n这本书的作者...";
	private int mEmptyIco;

	public LoadingView withEmptyIco(int resId) {
		mEmptyIco = resId;
		return this;
	}

	/**
	 * 没网提示
	 */
	private String mLoaded_not_net_text;
	private int mNoNetIco;

	public LoadingView withNoNetIco(int resId) {
		mNoNetIco = resId;
		return this;
	}

	public OnRetryListener mOnRetryListener;

	public LoadingView withOnRetryListener(OnRetryListener mOnRetryListener) {
		this.mOnRetryListener = mOnRetryListener;
		return this;
	}

	private LoadingState mState;

	public void setState(LoadingState state) {
		if (mState == state) {
			return;
		} else if (state == LoadingState.STATE_LOADING) {
			ll_over.setVisibility(GONE);
			ll_loading.setVisibility(VISIBLE);
		} else if (state != LoadingState.STATE_LOADING) {
			ll_loading.setVisibility(GONE);
			ll_over.setVisibility(VISIBLE);
			if (animation != null && mState == LoadingState.STATE_LOADING)
				animation.stop();
		}
		changeState(state);
	}

	public boolean btn_empty_ennable = true;
	public boolean btn_error_ennable = true;
	public boolean btn_nonet_ennable = true;

	public LoadingView withBtnNoNetEnnable(boolean ennable) {
		btn_nonet_ennable = ennable;
		return this;
	}

	public LoadingView withBtnErrorEnnable(boolean ennable) {
		btn_error_ennable = ennable;
		return this;
	}

	public LoadingView withBtnEmptyEnnable(boolean ennable) {
		btn_empty_ennable = ennable;
		return this;
	}

	private AnimationDrawable animation;

	private void changeState(LoadingState state) {
		switch (state) {
			// 加个重新执行动画
			case STATE_LOADING:
				mState = LoadingState.STATE_LOADING;
				iv_loading.setImageResource(mLoadingIco);
				tv_loading.setText(mLoadingText);
				if (animation == null) {
					animation = (AnimationDrawable) iv_loading.getDrawable();
				}
				if (animation != null)
					animation.start();
				break;
			case STATE_EMPTY:
				//空
				mState = LoadingState.STATE_EMPTY;
				iv_loaded.setImageResource(mEmptyIco);
				tv_loaded.setText(mLoaded_empty_text);
				if (btn_empty_ennable) {
					btn_loaded.setVisibility(VISIBLE);
					btn_loaded.setText(btn_empty_text);
				} else {
					btn_loaded.setVisibility(GONE);
				}
				animation = null;
				break;
			case STATE_ERROR:
				//错误
				mState = LoadingState.STATE_ERROR;
				iv_loaded.setImageResource(mErrorIco);
				tv_loaded.setText(mLoaded_error_text);
				if (btn_error_ennable) {
					btn_loaded.setVisibility(VISIBLE);
					btn_loaded.setText(btn_error_text);
				} else {
					btn_loaded.setVisibility(GONE);
				}
				animation = null;
				break;
			case STATE_NO_NET:
				mState = LoadingState.STATE_NO_NET;
				iv_loaded.setImageResource(mNoNetIco);
				tv_loaded.setText(mLoaded_not_net_text);
				if (btn_nonet_ennable) {
					btn_loaded.setVisibility(VISIBLE);
					btn_loaded.setText(btn_nonet_text);
				} else {
					btn_loaded.setVisibility(GONE);
				}
				animation = null;
				break;
		}

	}

	/**
	 * 后台或者本地出现错误提示
	 */
	private String mLoaded_error_text;
	private int mErrorIco;

	public LoadingView withErrorIco(int resId) {
		mErrorIco = resId;
		return this;
	}

	public LoadingView withLoadedEmptyText(int resId) {
		mLoaded_empty_text = getResources().getString(resId);
		return this;
	}

	public LoadingView withLoadedEmptyText(String mLoadedemptyText) {
		this.mLoaded_empty_text = mLoadedemptyText;
		return this;
	}

	public LoadingView withLoadedNoNetText(int resId) {
		mLoaded_not_net_text = getResources().getString(resId);
		return this;
	}

	public String btn_empty_text = "重试";
	public String btn_error_text = "重试";
	public String btn_nonet_text = "重试";
	private View view;

	public LoadingView withbtnEmptyText(String text) {
		this.btn_empty_text = text;
		return this;
	}

	public LoadingView withbtnErrorText(String text) {
		this.btn_error_text = text;
		return this;
	}

	public LoadingView withbtnNoNetText(String text) {
		this.btn_nonet_text = text;
		return this;
	}

	public LoadingView withLoadedNoNetText(String mLoadedNoNetText) {
		this.mLoaded_not_net_text = mLoadedNoNetText;
		return this;
	}

	public LoadingView withLoadedErrorText(int resId) {
		mLoaded_error_text = getResources().getString(resId);
		return this;
	}

	public LoadingView withLoadedErrorText(String mLoadedErrorText) {
		this.mLoaded_error_text = mLoadedErrorText;
		return this;
	}

	public LoadingView withLoadingText(int resId) {
		mLoadingText = getResources().getString(resId);
		return this;
	}

	public LoadingView withLoadingText(String mLoadingText) {
		this.mLoadingText = mLoadingText;
		return this;
	}

	public void build() {
		view = View.inflate(mContext, R.layout.view_loading, this);
		init();
		if (NetUtils.isNetworkAvailable(mContext)) {
			setState(LoadingState.STATE_LOADING);
		} else {
			setState(LoadingState.STATE_NO_NET);
		}
	}

	private void init() {
		ll_over = (LinearLayout) view.findViewById(R.id.ll_over);
		ll_loading = (LinearLayout) view.findViewById(R.id.ll_loading);

		tv_loaded = (TextView) view.findViewById(R.id.tv_loaded);
		tv_loading = (TextView) view.findViewById(R.id.tv_loading);

		btn_loaded = (Button) view.findViewById(R.id.btn_loaded);
		iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
		iv_loaded = (ImageView) view.findViewById(R.id.iv_loaded);

		btn_loaded.setOnClickListener(this);

	}

}
