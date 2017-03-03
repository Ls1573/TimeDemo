package com.time.memory.view.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.lock.LockView;
import com.time.memory.presenter.LockPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.memory.MemoryGroupActivity;
import com.time.memory.view.impl.IBindLockView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:密码锁
 * @date 2016/12/22 12:07
 */
public class LockActivity extends BaseActivity implements IBindLockView {

	private static final String TAG = "LockActivity";
	private static final int ERROR = -1;
	private static final int SUCCESS = 1;
	private static final int NURMAL = 0x000008;
	@Bind(R.id.pattern_view)
	LockView materialLockView;

	private MGroup mGroup;
	private String cParren;
	private String cls;
	private boolean isAddPwd;
	private boolean isUpdate;
	private boolean isCancle;
	private boolean isGroupDetail;
	private MyHandler myHandler;

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		mGroup = getIntent().getParcelableExtra("mGroup");
		isAddPwd = getIntent().getBooleanExtra("isAddPwd", false);
		isGroupDetail = getIntent().getBooleanExtra("isGroup", false);
		cls = getIntent().getStringExtra("cls");
		cParren = mGroup.getGroupPw();
		isUpdate = !TextUtils.isEmpty(cParren);
	}

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_lock);
	}

	@Override
	protected void onDestroy() {
		if (myHandler != null) {
			myHandler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_grouppwd), R.drawable.image_back);
	}

	@Override
	public BasePresenter initPresenter() {
		return new LockPresenter();
	}

	@Override
	public void initData() {
		materialLockView.setOnPatternListener(new LockView.OnPatternListener() {
			@Override
			public void onPatternDetected(List<LockView.Cell> pattern, String SimplePattern) {
				CLog.e(TAG, "SimplePattern:" + SimplePattern);
				reqPwd(SimplePattern);
				super.onPatternDetected(pattern, SimplePattern);
			}
		});
		myHandler = new MyHandler(this);
	}

	@OnClick({R.id.app_submit, R.id.pattern_cancle_tv})
	public void onClick(View view) {
		super.onMyClick(view);
		int id = view.getId();
		if (id == R.id.app_submit) {
			//忘记手势密码
			getUserPwd(true);
		} else if (id == R.id.pattern_cancle_tv) {
			//取消手势密码
//			getUserPwd(false);
			reqParren();
		}
	}

	/**
	 * 忘记手势密码
	 */
	private void getUserPwd(final boolean isForget) {
		if (isAddPwd) {
			showShortToast("请绘制新密码");
			return;
		}
		DialogUtils.reqEditDialog(LockActivity.this, "身份验证", "请输入您的登录密码", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				if (data != null) {
					String pwd = (String) data;
					((LockPresenter) mPresenter).forgetPattern(MainApplication.getUserId(), pwd, getString(R.string.FSPATTERN), mGroup, isForget);
				}
			}
		});
	}

	/**
	 * 验证密码
	 *
	 * @param sPattern
	 */
	private void reqPwd(String sPattern) {
		if (TextUtils.isEmpty(sPattern)) {
			showShortToast(getString(R.string.group_pwd_empty));
			materialLockView.setDisplayMode(LockView.DisplayMode.Wrong);
			return;
		}
		if (sPattern.length() < 3) {
			showShortToast(getString(R.string.group_pwd_length));
			materialLockView.setDisplayMode(LockView.DisplayMode.Wrong);
			return;
		}
		//取消
		if (isCancle) {
			((LockPresenter) mPresenter).veriftyCanclePattern(mGroup, sPattern);
			return;
		}
		//验证
		if (!isAddPwd) {
			((LockPresenter) mPresenter).veriftyPattern(mGroup.getGroupPw(), sPattern);
			return;
		}

		//一次设置
		if (TextUtils.isEmpty(cParren)) {
			cParren = sPattern;
			sendSuceessMessage("请再次输入");
			return;
		}
		if (!sPattern.equals(cParren)) {
			sendErrorMessage("密码错误,请重新输入");
		} else {
			if (isAddPwd && isUpdate && cParren.equals(sPattern)) {
				cParren = "";
				sendSuceessMessage("验证通过,请再次输入");
				isUpdate = false;
				return;
			}
			//二次验证
			materialLockView.setDisplayMode(LockView.DisplayMode.Correct);
			//设置密码
			if (isAddPwd)
				((LockPresenter) mPresenter).reqPattern(getString(R.string.FSPATTERN), sPattern, mGroup, "加密成功", false);
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		Intent intent = getIntent();
		if (!TextUtils.isEmpty(cls)) {
			try {
				intent.setClass(mContext, Class.forName(cls));
				startActivity(intent);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			intent.putExtra("groupPw", mGroup.getGroupPw());
			setResult(ReqConstant.REQUEST_LOCK_GROUP, intent);
			if (isGroupDetail) {
				//验证手势
				intent.putExtra("mGroup", mGroup);
				intent.setClass(mContext, MemoryGroupActivity.class);
				startAnimActivity(intent);
			}
		}
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
		materialLockView.clearPattern();
	}

	@Override
	public void sendErrorMessage(String message) {
		materialLockView.setDisplayMode(LockView.DisplayMode.Wrong);
		materialLockView.setEnabled(false);
		showShortToast(message);
		myHandler.sendEmptyMessageDelayed(NURMAL, 400);
	}

	@Override
	public void sendSuceessMessage(String message) {
		materialLockView.setDisplayMode(LockView.DisplayMode.Correct);
		materialLockView.setEnabled(false);
		showShortToast(message);
		myHandler.sendEmptyMessageDelayed(NURMAL, 400);
	}

	@Override
	public void resetParren() {
		hideLoadingDialog();
		cParren = "";
		isAddPwd = true;
		isUpdate = false;
		materialLockView.clearPattern();
	}

	private void reqParren() {
		if (isAddPwd) {
			showShortToast("请绘制新密码");
			return;
		}
		showShortToast("请绘制手势密码");
		materialLockView.clearPattern();
		isCancle = true;
	}

	class MyHandler extends Handler {
		WeakReference<Activity> weakReference;

		public MyHandler(Activity activity) {
			weakReference = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			if (weakReference.get() != null) {
				int what = msg.what;
				if (what == NURMAL) {
					materialLockView.clearPattern();
					materialLockView.setEnabled(true);
				}
			}
		}
	}
}
