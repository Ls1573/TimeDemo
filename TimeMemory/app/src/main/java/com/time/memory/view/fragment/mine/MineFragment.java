package com.time.memory.view.fragment.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.im.android.core.LocalUDPDataSender;
import com.time.memory.entity.User;
import com.time.memory.presenter.MinePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.login.LoginActivity;
import com.time.memory.view.activity.mine.AboutActivity;
import com.time.memory.view.activity.mine.DataStatisticsActivity;
import com.time.memory.view.activity.mine.FeedbakActivity;
import com.time.memory.view.activity.mine.MineActivity;
import com.time.memory.view.activity.mine.PushActivity;
import com.time.memory.view.activity.mine.SecurityActivity;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.impl.IMineView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0
 * @Package com.exesc.secondcar.ui.fragment.home
 * @Description:主页
 * @date 2016-7-4 下午2:58:41
 * ==============================
 */
public class MineFragment extends BaseFragment implements IMineView {

	private static final String TAG = "MineFragment";
	@Bind(R.id.mine_photo_iv)
	ImageView mine_photo_iv;
	@Bind(R.id.main_mine_tv)
	TextView main_mine_tv;


	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container,
							   Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mine, null);
		return view;
	}

	@Override
	public void initView(View view) {
		initTopBarForOnlyTitle(view, getString(R.string.main_mine));
	}

	@Override
	public void initData() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new MinePresenter();
	}

	@Override
	public void setUserInfos(User user) {
		main_mine_tv.setText(user.getUserName());
		if (!TextUtils.isEmpty(user.getHeadPhoto())) {
			if (user.getHeadPhoto().contains("http"))
				Picasso.with(mContext).load(user.getHeadPhoto() + getString(R.string.FSIMAGEOSS)).resize(300, 300).centerCrop().error(R.drawable.friend_photo).into(mine_photo_iv);
			else
				Picasso.with(mContext).load(getString(R.string.FSIMAGEPATH) + user.getHeadPhoto() + getString(R.string.FSIMAGEOSS)).resize(300, 300).centerCrop().error(R.drawable.friend_photo).into(mine_photo_iv);
		} else if (!TextUtils.isEmpty(user.getLocpath())) {
			Picasso.with(mContext).load("file://" + user.getLocpath()).config(Bitmap.Config.RGB_565).into(mine_photo_iv);
		}
	}

	@OnClick({R.id.mine_info_ll, R.id.mine_feed_ll, R.id.mine_push_ll, R.id.mine_safe_ll, R.id.mine_about_ll, R.id.app_exit, R.id.mine_statist_ll})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_exit:
				//退出登录
				exitLogin();
				break;
			case R.id.mine_info_ll:
				//个人信息
				startAnimActivity(MineActivity.class);
				break;
			case R.id.mine_push_ll:
				//新消息通知
				startAnimActivity(PushActivity.class);
				break;
			case R.id.mine_safe_ll:
				//账户安全
				startAnimActivity(SecurityActivity.class);
				break;
			case R.id.mine_about_ll:
				//关于我们
				startAnimActivity(AboutActivity.class);
				break;
			case R.id.mine_feed_ll:
				//意见反馈
				startAnimActivity(FeedbakActivity.class);
				break;
			case R.id.mine_statist_ll:
				//数据统计
				startAnimActivity(DataStatisticsActivity.class);
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//获取用户信息
		((MinePresenter) mPresenter).getUser(MainApplication.getUserId());
	}

	/**
	 * 退出登录
	 */
	private void exitLogin() {
		DialogUtils.request(getActivity(), "确定退出时光记忆吗?", new DialogCallback() {
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
		showLoadingDialog();
		int code = -1;
		code = LocalUDPDataSender.getInstance(mContext).sendLoginout();
		((MinePresenter) mPresenter).loginOut(MainApplication.getUserId());
		if (code == 0) {
			CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求已完成！");
		} else {
			CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求发送失败。错误码是：" + code + "！");
		}
		handler.sendEmptyMessage(0);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			hideLoadingDialog();
			System.gc();
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startAnimActivity(intent);
		}
	};

	@Override
	public void onDestroy() {
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();

	}
}
