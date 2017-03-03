package com.time.memory.view.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.callback.ICircleCallBack;
import com.time.memory.core.callback.OnMessageCallBack;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.im.android.ClientCoreSDK;
import com.time.memory.core.im.android.core.LocalUDPDataSender;
import com.time.memory.core.manager.ScreenManager;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.presenter.MainPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.FragmentAdapter;
import com.time.memory.view.fragment.circle.CircleFragment;
import com.time.memory.view.fragment.memory.MemoryFragment;
import com.time.memory.view.fragment.message.MessageFragment;
import com.time.memory.view.fragment.mine.MineFragment;
import com.time.memory.view.impl.IMainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0
 * @Package com.qiu.frame.view.activity
 * @Description:MVP
 * @date 2016-9-25 下午4:43:21
 * ==============================
 */
public class MainActivity extends BaseActivity implements IMainView, OnMessageCallBack, ViewPager.OnPageChangeListener, ICircleCallBack, EasyPermissions.PermissionCallbacks {

	private static final String TAG = "MainActivity";


	@Bind({R.id.main_memory_tv, R.id.main_circle_tv, R.id.main_message_tv, R.id.main_mine_tv})
	List<TextView> tvList;    // 记忆| 圈子| 消息| 我

	@Bind({R.id.main_memory_iv, R.id.main_circle_iv, R.id.main_message_iv, R.id.main_mine_iv})
	List<ImageView> ivList;//图片

	@Bind({R.id.main_memory_dot, R.id.main_circle_dot, R.id.main_message_dot, R.id.main_mine_dot})
	List<ImageView> dotList;//消息指示

	@Bind(R.id.view_pager)
	ViewPager view_pager;

	private FragmentAdapter mFragmentAdapter;

	private List<Fragment> mFragmentList;//view

	private MemoryFragment homeFragment;//记忆
	private CircleFragment circleFragment;//圈子
	private MessageFragment messageFragment;//消息
	private MineFragment mineFragment;// 我的

	private long exitTime = 0;
	private int currentPosition = 0;// 当前位置
	private int lastPosition = 0;// 上一次 位置
	private ScreenManager screenManager;//屏幕监听

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MainPresenter();
	}

	@Override
	protected void onDestroy() {
		if (screenManager != null)
			screenManager.unregisterListener();
		super.onDestroy();
	}

	@Override
	public void initView() {
		mFragmentList = new ArrayList<>();
		mFragmentList.add(homeFragment = new MemoryFragment());
		mFragmentList.add(circleFragment = new CircleFragment());
		mFragmentList.add(messageFragment = new MessageFragment());
		mFragmentList.add(mineFragment = new MineFragment());

		circleFragment.setiCircleCallBack(this);

		mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
		view_pager.setAdapter(mFragmentAdapter);
		view_pager.setOffscreenPageLimit(mFragmentList.size());//设置缓存数量
		view_pager.addOnPageChangeListener(this);

		messageFragment.setOnMessageCallBack(this);
		tvList.get(0).setActivated(true);
		ivList.get(0).setActivated(true);

	}

	@Override
	public void initData() {
		//建立长连接
		((MainPresenter) mPresenter).reqLoginIm(MainApplication.getUserId());
		((MainPresenter) mPresenter).startListener();
		((MainPresenter) mPresenter).openRecord(getString(R.string.FSOPENRECORD));

		initScreen();
		requestWriterPermission();
	}

	/**
	 * 屏幕监听
	 */
	private void initScreen() {
		screenManager = new ScreenManager(this);
		screenManager.begin(new ScreenManager.ScreenStateListener() {
			@Override
			public void onUserPresent() {
				// 解锁
				CLog.e(TAG, "-------->解锁了<------------");
			}

			@Override
			public void onScreenOn() {
				// 开屏
				CLog.e(TAG, "-------->开屏了<------------");
				try {
					ClientCoreSDK.getInstance().setConnectedToServer(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onScreenOff() {
				// 锁屏
				CLog.e(TAG, "--------锁屏了<------------");
				try {
					ClientCoreSDK.getInstance().setConnectedToServer(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@OnClick({R.id.main_memory_rl, R.id.main_circle_rl, R.id.main_message_rl, R.id.main_mine_rl})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.main_memory_rl:
				if (currentPosition == 0) {
					break;
				}
				currentPosition = 0;
				break;
			case R.id.main_circle_rl:
				if (currentPosition == 1) {
					break;
				}
				currentPosition = 1;
				break;
			case R.id.main_message_rl:
				if (currentPosition == 2) {
					break;
				}
				currentPosition = 2;
				break;
			case R.id.main_mine_rl:
				if (currentPosition == 3) {
					break;
				}
				currentPosition = 3;
				break;
		}
		setCurrentFragment();

	}


	/**
	 * 设置未读消息提示
	 */
	@Override
	public void setMesageTag(int visibility) {
		dotList.get(2).setVisibility(visibility);
	}

	@Override
	public void onPageSelected(int position) {
		this.currentPosition = position;
		setCurrentFragment();
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	/**
	 * 圈子人数改变
	 *
	 * @param num
	 */
	@Override
	public void onNotiftyNum(String num) {
		if (!TextUtils.isEmpty(num) && !num.equals("0")) {
			//不为空&&不为0
			homeFragment.setGroupNum(num);
		}
	}

	/**
	 * 切换当前页面
	 */
	private void setCurrentFragment() {
		if (currentPosition != lastPosition) {
			tvList.get(lastPosition).setActivated(false);
			tvList.get(currentPosition).setActivated(true);
			ivList.get(lastPosition).setActivated(false);
			ivList.get(currentPosition).setActivated(true);
			lastPosition = currentPosition;
			if (view_pager.getCurrentItem() != currentPosition)
				view_pager.setCurrentItem(currentPosition);
		}
	}

	/**
	 * SD卡获取权限
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_GALLERY)
	protected void requestWriterPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			((MainPresenter) mPresenter).reqVersion(getString(R.string.FsGetAppVersion), MainActivity.this);
		} else {
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_writer),
					ReqConstant.REQUEST_CODE_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}

	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {

		}
	};



	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝,再次申请
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (requestCode == ReqConstant.REQUEST_CODE_GALLERY) {
			requestWriterPermission();
		} else {
			//权限下发
			for (Fragment ff : mFragmentList) {
				ff.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	/**
	 * 双击退出
	 */
	@Override
	public void onBackPressed() {
		exitLogin();
	}

	/**
	 * 退出登录
	 */
	private void exitLogin() {
		DialogUtils.request(MainActivity.this, "确定退出时光记忆吗?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isSure = (boolean) data;
				if (isSure) {
					doLogout();
				}
			}
		});
	}

	/**
	 * 发出退出登陆请求包（Android系统要求必须要在独立的线程中发送）
	 */
	private void doLogout() {
		ExecutorManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				int code = -1;
				try {
					code = LocalUDPDataSender.getInstance(MainActivity.this).sendLoginout();
				} catch (Exception e) {
					Log.w(TAG, e);
				}
				if (code == 0) {
					CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求已完成！");
					// 退出释放内存
					//System.exit(0);
				} else {
					CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求发送失败。错误码是：" + code + "！");
				}
				finish();
			}
		});
	}
}
