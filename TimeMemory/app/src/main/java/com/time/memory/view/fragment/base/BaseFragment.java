package com.time.memory.view.fragment.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IBaseFragment;
import com.time.memory.core.callback.OnLoadingListener;
import com.time.memory.core.callback.OnRetryListener;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.CustomClipLoading;
import com.time.memory.gui.MyToast;
import com.time.memory.gui.gallery.CoreConfig;
import com.time.memory.gui.gallery.FunctionConfig;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.gui.gallery.loader.GlideImageLoader;
import com.time.memory.gui.loading.LoadingState;
import com.time.memory.gui.loading.LoadingView;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.NetUtils;
import com.time.memory.view.activity.login.LoginActivity;
import com.time.memory.view.impl.IBaseView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;

/**
 * BaseFragment
 *
 * @author Qiu
 */
public abstract class BaseFragment extends Fragment implements OnClickListener,
		OnRefreshListener, OnRetryListener, OnLoadingListener, IBaseFragment, AdapterCallback {

	protected CustomClipLoading dialog;
	protected MyToast mToast;
	protected Context mContext;
	protected LoadingView fl_loading;// 加载
	protected BasePresenter mPresenter;// P层基类

	protected static final long INTERVAL = 500L; //防止连续点击的时间间隔
	protected static long lastClickTime = 0L; //上一次点击的时间

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = null;
		mContext = getContext().getApplicationContext();
		try {
			view = onCreateMyView(inflater, container, savedInstanceState);
			getPresenter();
			bindView(view);
			initView(view);
			initData();
			initIal();
		} catch (Exception e) {
			// 处理|网络上传异常
			e.printStackTrace();
		}
		return view;
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getRunningActivityName(getActivity())); //统计页面，"MainScreen"为页面名称，可自定义
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getRunningActivityName(getActivity()));
	}

	@Override
	public void onDestroy() {
		ButterKnife.unbind(this);
		if (mPresenter != null && this instanceof IBaseView) {
			mPresenter.detachView();
			mPresenter = null;
		}
		super.onDestroy();
//		RefWatcher refWatcher = MainApplication.getRefWatcher();
//		refWatcher.watch(this);
	}


	/**
	 * 绑定view
	 */
	protected void bindView(View view) {
		ButterKnife.bind(this, view);
	}

	/**
	 * 初始化
	 */
	protected void initIal() {
		lastClickTime = 0;
	}

	/**
	 * 连续点击判定
	 *
	 * @return
	 */
	protected boolean isGoOn() {
		//当前时间
		long time = System.currentTimeMillis();
		//时间差额大于500ms-->可以继续
		if ((time - lastClickTime) > INTERVAL) {
			CLog.e("************", "lastClickTime:" + lastClickTime + "  time:" + time);
			lastClickTime = time;
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 实例P层
	 *
	 * @param
	 */
	private void getPresenter() {
		mPresenter = initPresenter();
		if (mPresenter != null && this instanceof IBaseView) {
			mPresenter.attach((IBaseView) this, getActivity());
		}
	}

	/**
	 * 自定义监听
	 *
	 * @param view
	 */
	public void onMyClick(View view) {
	}

	@Override
	public void onLongCallBack(Object data, int position) {

	}

	@Override
	public void onClick(View view) {
		try {
			onMyClick(view);
		} catch (Exception e) {
			// 异常处理|网络
			e.printStackTrace();
		}
	}

	public String getRunningActivityName(Activity activity) {
		ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
				.getClassName();
		return runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
	}

	/**
	 * 跳转Intent
	 */
	protected void startAnimActivity(Intent intent) {
		startActivity(intent);
	}

	/**
	 * 跳转Intent
	 */
	protected void startAnimActivity(Class<?> cla) {
		startActivity(new Intent(getActivity(), cla));
	}

	@Override
	public void onRefresh() {
	}

	/**
	 * @Title: 加载动画
	 * @Description: 设置LoadingView
	 */
	protected void initLoading(View view) {
		fl_loading = (LoadingView) view.findViewById(R.id.app_loading);
		fl_loading.withLoadedEmptyText("记忆是一本书,而你就是\n这本书的作者...")
				.withEmptyIco(R.drawable.ic_chat_empty)
				.withBtnEmptyEnnable(true)
				.withErrorIco(R.drawable.ic_chat_empty)
				.withLoadedErrorText(getString(R.string.net_error))
				.withbtnEmptyText("立即书写").
				withbtnErrorText("重试")
				.withLoadedNoNetText(getString(R.string.net_error))
				.withNoNetIco(R.drawable.ic_chat_empty).withbtnNoNetText("重试")
				.withLoadingIco(R.drawable.frame_loading)
				.withLoadingText("加载中...").withOnRetryListener(this).build();
	}

	/**
	 * 图片初始化配置
	 */
	protected FunctionConfig initPhoto(int maxSize, boolean isOld, boolean isWriter, boolean isAddMore, int state, String Id, List<PhotoInfo> mPhotoList, String... cla) {
		//公共配置
		FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
		//图片加载
		com.time.memory.gui.gallery.ImageLoader imageLoader = new GlideImageLoader();
		functionConfigBuilder.setCommon(maxSize, isOld, isWriter, isAddMore, false, state, Id, mPhotoList, cla);
		FunctionConfig functionConfig = functionConfigBuilder.build();
		//配置参数
		CoreConfig coreConfig = new CoreConfig.Builder(mContext.getApplicationContext(), imageLoader)
				.setFunctionConfig(functionConfig)
				.setNoAnimcation(true)
				.build();
		//初始化
		GalleryFinal.init(coreConfig);
		return functionConfig;
	}

	/**
	 * 图片初始化配置(追加记忆)
	 */
	protected FunctionConfig initSupporyPhoto(int maxSize, boolean isOld, boolean isWriter, boolean isAddMore, int state, String Id, String memoryId, String memorySourceId, String userId, List<PhotoInfo> mPhotoList) {
		//公共配置
		FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
		//图片加载
		com.time.memory.gui.gallery.ImageLoader imageLoader = new GlideImageLoader();
		functionConfigBuilder.setCommon(maxSize, isOld, isWriter, isAddMore, state, Id, memoryId, memorySourceId, userId, mPhotoList);
		FunctionConfig functionConfig = functionConfigBuilder.build();
		//配置参数
		CoreConfig coreConfig = new CoreConfig.Builder(mContext.getApplicationContext(), imageLoader)
				.setFunctionConfig(functionConfig)
				.setNoAnimcation(true)
				.build();
		//初始化
		GalleryFinal.init(coreConfig);
		return functionConfig;
	}

	@Override
	public void showSuccess() {
		fl_loading.setVisibility(View.GONE);
	}

	@Override
	public void showEmpty() {
		fl_loading.setVisibility(View.VISIBLE);
		fl_loading.setState(LoadingState.STATE_EMPTY);

	}

	@Override
	public boolean checkNet() {
		return NetUtils.isNetworkAvailable(mContext.getApplicationContext());
	}

	@Override
	public void showFaild() {
		fl_loading.setVisibility(View.VISIBLE);
		fl_loading.setState(LoadingState.STATE_ERROR);
	}

	@Override
	public void showNoNet() {
		fl_loading.setVisibility(View.VISIBLE);
		fl_loading.setState(LoadingState.STATE_NO_NET);
	}

	@Override
	public void showLoading() {
		fl_loading.setVisibility(View.VISIBLE);
		fl_loading.setState(LoadingState.STATE_LOADING);
	}

	;

	@Override
	public void onRetry() {
		// 重试
	}

	@Override
	public void onDataCallBack(Object data, int position, int index, int tag) {
	}

	@Override
	public void onDataCallBack(Object data, int position) {

	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {

	}

	@Override
	public void onCallback(Object data) {

	}

	/**
	 * @param
	 * @Description:登录验证
	 * @reurn
	 */
	private <T> boolean checkLogin(String activity) {
		String useId = MainApplication.getUserId();
		Intent intent = new Intent();
		if (TextUtils.isEmpty(useId)) {
			intent.putExtra("class", activity);
			intent.setClass(getActivity(), LoginActivity.class);
			startAnimActivity(intent);
			return false;
		} else {
			try {
				if (!TextUtils.isEmpty(activity))
					startAnimActivity(Class.forName(activity));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	/**
	 * 显示Toast
	 */
	public void showShortToast(String msg) {
		showToast(msg);
	}

	private void showToast(String text) {
		if (mToast == null) {
			mToast = MyToast.makeTextToast(getActivity(), text, Toast.LENGTH_SHORT);
		}
		mToast.setText(text);
		mToast.show();
	}

	private void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	/**
	 * 显示进度条
	 */
	public void showLoadingDialog() {
		if (dialog == null) {
			dialog = new CustomClipLoading(getActivity(), R.drawable.frame_loading);
		}
		if (!getActivity().isFinishing()) {
			dialog.show();
		}
	}

	/**
	 * 隐藏进度条
	 */
	protected void hideLoadingDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

/** titlebar 分类 1、标题 2、左返回/左图片 + 标题 3、标题 + 右文字/右图片 4、左返回/左图片 + 标题 + 右文字/右图片 */
	/**
	 * 1、只有标题
	 *
	 * @throws
	 * @Title: initTopBarLayoutByTitle
	 */
	public void initTopBarForOnlyTitle(View view, String titleName) {
		TextView title = (TextView) view.findViewById(R.id.tv_main_title);
		title.setText(titleName);
	}

	/**
	 * 2、左图片 + 标题
	 *
	 * @param titleName
	 * @param leftDrawable
	 */
	public void initTopBarForLeft(View view, String titleName, int leftDrawable) {
		TextView title = (TextView) view.findViewById(R.id.tv_main_title);
		title.setText(titleName);

		ImageView iVLeft = (ImageView) view.findViewById(R.id.app_cancle);
		iVLeft.setVisibility(leftDrawable != -1 ? View.VISIBLE : View.GONE);
		if (leftDrawable != -1)
			iVLeft.setImageResource(leftDrawable);

		view.findViewById(R.id.app_cancle).setVisibility(View.VISIBLE);
		view.findViewById(R.id.app_cancle).setOnClickListener(this);
	}

	/**
	 * 3、标题 + 右文字/右图片
	 *
	 * @param titleName
	 * @param rightName
	 * @param rdrawable
	 */
	public void initTopBarForRight(View view, String titleName,
								   String rightName, int rdrawable) {
		TextView title = (TextView) view.findViewById(R.id.tv_main_title);
		title.setText(titleName);

		TextView tVRight = (TextView) view.findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rdrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		ImageView iVRight = (ImageView) view.findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rdrawable != -1 ? View.VISIBLE : View.GONE);
		if (rdrawable != -1)
			iVRight.setImageResource(rdrawable);

		view.findViewById(R.id.app_cancle).setVisibility(View.GONE);

	}

	/**
	 * 4、左返回/左图片 + 标题 + 右文字/右图片
	 *
	 * @param titleName
	 * @param leftDrawable
	 * @param rightName
	 * @param rightDrawable
	 */
	public void initTopBarForBoth(View view, String titleName,
								  int leftDrawable, String rightName, int rightDrawable) {
		TextView title = (TextView) view.findViewById(R.id.tv_main_title);
		title.setText(titleName);

		ImageView iVLeft = (ImageView) view.findViewById(R.id.app_cancle);
		iVLeft.setVisibility(leftDrawable != -1 ? View.VISIBLE : View.GONE);
		if (leftDrawable != -1)
			iVLeft.setImageResource(leftDrawable);

		TextView tVRight = (TextView) view.findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rightDrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		ImageView iVRight = (ImageView) view.findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rightDrawable != -1 ? View.VISIBLE : View.GONE);
		if (rightDrawable != -1)
			iVRight.setImageResource(rightDrawable);
		view.findViewById(R.id.tv_main_title).setVisibility(View.VISIBLE);
		view.findViewById(R.id.app_cancle).setOnClickListener(this);
	}


	/**
	 * 5、标题 + 右图片 + 最右边文字/右图片
	 */
	public void initTopBarForRightDouble(View view, String titleName, int oneRDrawable,
										 String rightName, int rdrawable) {
		TextView title = (TextView) view.findViewById(R.id.tv_main_title);
		title.setText(titleName);

		TextView tVRight = (TextView) view.findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rdrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		ImageView iVRight = (ImageView) view.findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rdrawable != -1 ? View.VISIBLE : View.GONE);
		if (rdrawable != -1)
			iVRight.setImageResource(rdrawable);

		view.findViewById(R.id.app_cancle).setVisibility(View.GONE);

		ImageView oneRightImg = (ImageView) view.findViewById(R.id.iv_main_right_one);
		if (oneRDrawable != -1) {
			oneRightImg.setImageResource(oneRDrawable);
			oneRightImg.setVisibility(View.VISIBLE);
		}
	}
}
