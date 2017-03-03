package com.time.memory.view.activity.mine;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.entity.MyPush;
import com.time.memory.gui.togglebutton.button.ToggleButton;
import com.time.memory.presenter.PushPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IPushView;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:推送设置
 * @date 2016/9/18 14:21
 */
public class PushActivity extends BaseActivity implements IPushView, ToggleButton.OnToggleChanged {
	private static final String TAG = "PushActivity";
	@Bind(R.id.tb_reveicer)
	ToggleButton tb_reveicer;//全局
	@Bind(R.id.push_sound)
	ToggleButton pushSound;//声音
	@Bind(R.id.push_vibrate)
	ToggleButton pushVibrate;//震动
	@Bind(R.id.push_circle)
	ToggleButton pushCircle;//圈子

	@Bind(R.id.push_circle_ll)
	LinearLayout pushCircleLl;//圈子
	@Bind(R.id.push_sound_ll)
	LinearLayout pushSoundLl;//声音
	@Bind(R.id.push_vibrate_ll)
	LinearLayout pushVibrateLl;//震动
	@Bind(R.id.push_csign_tv)
	TextView push_csign_tv;//圈子提示

	private boolean isChange = false;
	private MyPush mPush;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_push);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_push), R.drawable.image_back);
	}


	@Override
	public BasePresenter initPresenter() {
		return new PushPresenter();
	}


	@Override
	public void initData() {
		//添加监听
		addToggleClick();
		//获取
		((PushPresenter) mPresenter).getPushing(MainApplication.getUserId());
	}

	@Override
	public void setMessage(MyPush push) {
		mPush = push;
		//设置
		setNotify();
		setPushSound();
		setPushVerbrate();
		setPushCircle();
		isChange = true;
	}

	/**
	 * 设置切换监听
	 */
	private void addToggleClick() {
		tb_reveicer.setOnToggleChanged(this);
		pushSound.setOnToggleChanged(this);
		pushVibrate.setOnToggleChanged(this);
		pushCircle.setOnToggleChanged(this);
	}

	/**
	 * 更新推送设置
	 */
	@Override
	public void updatePush(int position) {
		if (position == 0) {
			setNotify();
		} else if (position == 1) {
			setPushSound();
		} else if (position == 2) {
			setPushVerbrate();
		} else {
			setPushCircle();
		}
	}


	@Override
	public void onToggle(View view, boolean on) {
		if (!isChange) return;
		int position = 0;
		if (R.id.tb_reveicer == view.getId()) {
			//全局的
			mPush.setNotifystate(on ? 1 : 0);
			position = 0;
		} else if (R.id.push_sound == view.getId()) {
			//声音
			mPush.setSound(on ? 1 : 0);
			position = 1;
		} else if (R.id.push_vibrate == view.getId()) {
			//震动
			mPush.setVerfity(on ? 1 : 0);
			position = 2;
		} else if (R.id.push_circle == view.getId()) {
			//圈子
			mPush.setCircle(on ? 1 : 0);
			position = 3;
		}
		CLog.e(TAG, "mPush:" + mPush.toString());
		((PushPresenter) mPresenter).updatePush(mPush, position);
	}

	/**
	 * 设置全局显示
	 */
	private void setNotify() {
		// 显示设置
		if (mPush.getNotifystate() == 1) {
			// 切换无动画
			if (!isChange)
				tb_reveicer.toggle(false);
			tb_reveicer.setToggleOn();
			pushCircleLl.setVisibility(View.VISIBLE);
			pushSoundLl.setVisibility(View.VISIBLE);
			pushVibrateLl.setVisibility(View.VISIBLE);
			push_csign_tv.setVisibility(View.VISIBLE);
		} else {
			tb_reveicer.setToggleOff();
			pushCircleLl.setVisibility(View.GONE);
			pushSoundLl.setVisibility(View.GONE);
			pushVibrateLl.setVisibility(View.GONE);
			push_csign_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 声音设置
	 */
	private void setPushSound() {
		if (mPush.getSound() == 1) {
			// 切换无动画
			if (!isChange)
				pushSound.toggle(false);
			pushSound.setToggleOn();
		} else {
			pushSound.setToggleOff();
		}
	}

	/**
	 * 震动设置
	 */
	private void setPushVerbrate() {
		if (mPush.getVerfity() == 1) {
			// 切换无动画
			if (!isChange)
				pushVibrate.toggle(false);
			pushVibrate.setToggleOn();
		} else {
			pushVibrate.setToggleOff();
		}
	}

	/**
	 * 圈子设置
	 */
	private void setPushCircle() {
		if (mPush.getCircle() == 1) {
			// 切换无动画
			if (!isChange)
				pushCircle.toggle(false);
			pushCircle.setToggleOn();
		} else {
			pushCircle.setToggleOff();
		}
	}
}
