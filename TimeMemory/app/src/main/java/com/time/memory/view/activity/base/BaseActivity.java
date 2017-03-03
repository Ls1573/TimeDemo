package com.time.memory.view.activity.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IBase;
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
import com.time.memory.util.NetUtils;
import com.time.memory.view.activity.login.LoginActivity;
import com.time.memory.view.impl.IBaseView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 父类
 *
 * @author Qiu
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener, OnRefreshListener, OnRetryListener, OnLoadingListener, AdapterCallback, IBase {
	private static final String TAG = "BaseActivity";

	private MyToast mToast;
	protected Context mContext;

	private CustomClipLoading dialog;// 加载
	protected LoadingView fl_loading;// 加载
	protected BasePresenter mPresenter;// P层基类

	private String curName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		try {
			onCreateMyView();
			getPresenter();
			bindView();
			initView();
			initData();
		} catch (Exception e) {
			// 处理|网络上传异常
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);       //统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		ButterKnife.unbind(this);
		if (mPresenter != null && this instanceof IBaseView) {
			mPresenter.detachView();
			mPresenter = null;
		}
		super.onDestroy();
	}

	/**
	 * 绑定
	 */
	protected void bindView() {
		ButterKnife.bind(this);
	}

	/**
	 * 创建视图，统一处理
	 */
	public abstract void onCreateMyView();

	/**
	 * 初始化视图
	 */
	public abstract void initView();

	/**
	 * 初始化数据
	 */
	public abstract void initData();


//	protected void initSwipeBack() {
////		//滑动
////		mSwipeBackLayout = getSwipeBackLayout();
////		//设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
////		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//	}


	/**
	 * 实例P层
	 *
	 * @param
	 */
	private void getPresenter() {
		mPresenter = initPresenter();
		if (mPresenter != null && this instanceof IBaseView) {
			mPresenter.attach((IBaseView) this, this);
		}
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

	@Override
	public void onLongCallBack(Object data, int position) {

	}

	/**
	 * 自定义监听
	 *
	 * @param view
	 */
	public void onMyClick(View view) {
		if (view.getId() == R.id.app_cancle) {
//			ActivityTaskManager.getInstance().removeActivity(getRunningActivityName());
			finish();
		}
		if (view.getId() == R.id.tv_main_left) {
//			ActivityTaskManager.getInstance().removeActivity(getRunningActivityName());
			finish();
		}
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

	@Override
	public void onRefresh() {
	}

	/**
	 * @Title: 加载动画
	 * @Description: 设置LoadingView
	 */
	protected void initLoading() {
		fl_loading = (LoadingView) findViewById(R.id.app_loading);
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
	protected FunctionConfig initPhoto(int maxSize, boolean isOld, boolean isWriter, boolean isAddMore, int state, String Id, List<PhotoInfo> mPhotoList) {
		//公共配置
		FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
		//图片加载
		com.time.memory.gui.gallery.ImageLoader imageLoader = new GlideImageLoader();
		functionConfigBuilder.setCommon(maxSize, isOld, isWriter, isAddMore, false, state, Id, mPhotoList);
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
	 * 继续
	 */
	protected FunctionConfig initPhoto(int maxSize, boolean isOld, boolean isWriter, boolean isAddMore, boolean isContinue, int state, String Id, List<PhotoInfo> mPhotoList) {
		//公共配置
		FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
		//图片加载
		com.time.memory.gui.gallery.ImageLoader imageLoader = new GlideImageLoader();
		functionConfigBuilder.setCommon(maxSize, isOld, isWriter, isAddMore, isContinue, state, Id, mPhotoList);
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
		if (fl_loading != null) {
			fl_loading.setVisibility(View.GONE);
		}
	}

	@Override
	public void showEmpty() {
		if (fl_loading != null) {
			fl_loading.setVisibility(View.VISIBLE);
			fl_loading.setState(LoadingState.STATE_EMPTY);
		}
	}

	@Override
	public void showNoNet() {
		if (fl_loading != null) {
			fl_loading.setVisibility(View.VISIBLE);
			fl_loading.setState(LoadingState.STATE_NO_NET);
		}
	}

	@Override
	public void showLoading() {
		if (fl_loading != null) {
			fl_loading.setVisibility(View.VISIBLE);
			fl_loading.setState(LoadingState.STATE_LOADING);
		}
	}

	;

	@Override
	public boolean checkNet() {
		return NetUtils.isNetworkAvailable(mContext);
	}

	@Override
	public void showFaild() {
		if (fl_loading != null) {
			fl_loading.setVisibility(View.VISIBLE);
			fl_loading.setState(LoadingState.STATE_ERROR);
		}
	}

	@Override
	public void onRetry() {
	}

	/**
	 * 得到当前运行Activity名
	 *
	 * @return
	 */
	public String getRunningActivityName() {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity
				.getClassName();
		return runningActivity.substring(runningActivity.lastIndexOf(".") + 1);
	}

	/**
	 * @param
	 * @Description:登录验证
	 * @reurn
	 */
	protected <T> void checkLogin(String activity, String userId) {
		Intent intent = new Intent();
		if (TextUtils.isEmpty(userId)) {
			intent.putExtra("class", activity);
			intent.setClass(mContext, LoginActivity.class);
			startAnimActivity(intent);
		} else {
			try {
				startAnimActivity(Class.forName(activity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
		startActivity(new Intent(this, cla));
	}

	/**
	 * 显示Toast
	 */
	public void showShortToast(String msg) {
		showToast(msg);
	}

	private void showToast(String text) {
		if (mToast == null) {
			mToast = MyToast.makeTextToast(this, text, Toast.LENGTH_SHORT);
		}
		mToast.setText(text);
		mToast.show();
	}

	/**
	 * 取消显示
	 */
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
			dialog = new CustomClipLoading(this, R.drawable.frame_loading);
		}
		if (!isFinishing()) {
			dialog.show();
		}
	}

	/**
	 * 显示进度条
	 */
	protected void showMyDialog() {
		if (dialog == null) {
			dialog = new CustomClipLoading(this, R.drawable.frame_loading);
			dialog.setOnOutSide(true);
		}
		if (!isFinishing()) {
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

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
//				InputMethodManager manager = (InputMethodManager) getSystemService(mContext.INPUT_METHOD_SERVICE);
//				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		}
//		return super.onTouchEvent(event);
//	}

	/** titlebar 分类 1、标题 2、左返回/左图片 + 标题 3、标题 + 右文字/右图片 4、左返回/左图片 + 标题 + 右文字/右图片 */
	/**
	 * 1、只有标题
	 *
	 * @throws
	 * @Title: initTopBarLayoutByTitle
	 */
	public void initTopBarForOnlyTitle(String titleName) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);
	}

	/**
	 * 2、左图片 + 标题
	 *
	 * @param titleName
	 * @param leftDrawable
	 */
	public void initTopBarForLeft(String titleName, int leftDrawable) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);

		ImageView iVLeft = (ImageView) findViewById(R.id.app_cancle);
		iVLeft.setVisibility(leftDrawable != -1 ? View.VISIBLE : View.GONE);
		if (leftDrawable != -1)
			iVLeft.setImageResource(leftDrawable);

		findViewById(R.id.app_cancle).setVisibility(View.VISIBLE);
		findViewById(R.id.app_cancle).setOnClickListener(this);
	}

	/**
	 * 3、标题 + 右文字/右图片
	 *
	 * @param titleName
	 * @param rightName
	 * @param rdrawable
	 */
	public void initTopBarForRight(String titleName, String rightName,
								   int rdrawable) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);

		TextView tVRight = (TextView) findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rdrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		ImageView iVRight = (ImageView) findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rdrawable != -1 ? View.VISIBLE : View.GONE);
		if (rdrawable != -1)
			iVRight.setImageResource(rdrawable);

		findViewById(R.id.app_cancle).setVisibility(View.GONE);

	}

	/**
	 * 4、左返回/左图片 + 标题 + 右文字/右图片
	 *
	 * @param titleName
	 * @param leftDrawable
	 * @param rightName
	 * @param rightDrawable
	 */
	public void initTopBarForBoth(String titleName, int leftDrawable,
								  String rightName, int rightDrawable) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);

		ImageView iVLeft = (ImageView) findViewById(R.id.app_cancle);
		iVLeft.setVisibility(leftDrawable != -1 ? View.VISIBLE : View.GONE);
		if (leftDrawable != -1)
			iVLeft.setImageResource(leftDrawable);

		TextView tVRight = (TextView) findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rightDrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		ImageView iVRight = (ImageView) findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rightDrawable != -1 ? View.VISIBLE : View.GONE);
		if (rightDrawable != -1)
			iVRight.setImageResource(rightDrawable);
		else
			iVRight.setVisibility(View.GONE);

		findViewById(R.id.tv_main_title).setVisibility(View.VISIBLE);
		findViewById(R.id.app_cancle).setOnClickListener(this);
	}

	/**
	 * 5、左文字 + 标题
	 *
	 * @param titleName
	 */
	public void initTopBarForLeftTv(String titleName, String leftTv) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);
		TextView leftText = (TextView) findViewById(R.id.tv_main_left);
		leftText.setText(leftTv);

		findViewById(R.id.app_cancle_tv).setVisibility(View.VISIBLE);
		findViewById(R.id.tv_main_left).setVisibility(View.VISIBLE);
		findViewById(R.id.app_cancle).setVisibility(View.GONE);
	}


	/**
	 * 5、标题 + 右图片 + 最右边文字/右图片
	 */
	public void initTopBarForRightDouble(String titleName, int oneRDrawable,
										 String rightName, int rdrawable) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);

		TextView tVRight = (TextView) findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rdrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		ImageView iVRight = (ImageView) findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rdrawable != -1 ? View.VISIBLE : View.GONE);
		if (rdrawable != -1)
			iVRight.setImageResource(rdrawable);

		findViewById(R.id.app_cancle).setVisibility(View.GONE);

		ImageView oneRightImg = (ImageView) findViewById(R.id.iv_main_right_one);
		if (oneRDrawable != -1) {
			oneRightImg.setImageResource(oneRDrawable);
			oneRightImg.setVisibility(View.VISIBLE);
		}
	}


	/**
	 * 6 左图片+title+右图1+右图2
	 *
	 * @param titleName
	 * @param leftDrawable
	 * @param rightDrawable
	 * @param rightDrawable2
	 */
	public void initTopBarAll(String titleName, int leftDrawable,
							  int rightDrawable, int rightDrawable2) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);

		ImageView iVLeft = (ImageView) findViewById(R.id.app_cancle);
		iVLeft.setVisibility(leftDrawable != -1 ? View.VISIBLE : View.GONE);
		if (leftDrawable != -1)
			iVLeft.setImageResource(leftDrawable);

		TextView tVRight = (TextView) findViewById(R.id.tv_main_right);
		tVRight.setVisibility(View.GONE);

		ImageView iVRight = (ImageView) findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rightDrawable != -1 ? View.VISIBLE : View.GONE);
		if (rightDrawable != -1)
			iVRight.setImageResource(rightDrawable);
		else
			iVRight.setVisibility(View.GONE);

		ImageView iVRightMore = (ImageView) findViewById(R.id.iv_main_right_one);
		iVRightMore.setVisibility(rightDrawable2 != -1 ? View.VISIBLE : View.GONE);
		if (rightDrawable2 != -1)
			iVRightMore.setImageResource(rightDrawable2);
		else
			iVRightMore.setVisibility(View.GONE);

		findViewById(R.id.tv_main_title).setVisibility(View.VISIBLE);
		findViewById(R.id.app_cancle).setOnClickListener(this);
	}

	/**
	 * 7.左返回+ 标题 + 右文字/右图片
	 *
	 * @param titleName
	 * @param rightName
	 * @param rightDrawable
	 */
	public void initTopBarForBoth(String titleName, String leftName,
								  String rightName, int rightDrawable) {
		TextView title = (TextView) findViewById(R.id.tv_main_title);
		title.setText(titleName);

		TextView tVRight = (TextView) findViewById(R.id.tv_main_right);
		tVRight.setVisibility(rightDrawable == -1 ? View.VISIBLE : View.GONE);
		tVRight.setText(rightName);

		TextView tVLeft = (TextView) findViewById(R.id.tv_main_left);
		tVLeft.setVisibility(View.VISIBLE);
		tVLeft.setText(leftName);

		ImageView iVRight = (ImageView) findViewById(R.id.iv_main_right);
		iVRight.setVisibility(rightDrawable != -1 ? View.VISIBLE : View.GONE);
		if (rightDrawable != -1)
			iVRight.setImageResource(rightDrawable);
		else
			iVRight.setVisibility(View.GONE);

		findViewById(R.id.tv_main_title).setVisibility(View.VISIBLE);
		findViewById(R.id.tv_main_left).setOnClickListener(this);
	}


}
