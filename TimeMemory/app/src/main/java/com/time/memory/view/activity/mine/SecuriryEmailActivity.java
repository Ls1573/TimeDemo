package com.time.memory.view.activity.mine;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.task.TimeCount;
import com.time.memory.entity.User;
import com.time.memory.gui.PwdEditText;
import com.time.memory.presenter.SecurityEmailPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IVeriftyEmailView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:验证邮箱
 * @date 2016/10/20 19:20
 */
public class SecuriryEmailActivity extends BaseActivity implements IVeriftyEmailView {
	@Bind(R.id.register_email_et)
	TextView rEmail_et;//邮箱
	@Bind(R.id.register_pwd_et)
	PwdEditText register_pwd_et;//邮箱验证码

	@Bind(R.id.login_send_verifty)
	TextView sendVerifty;// 发送验证码

	private User user;
	private TimeCount timeCountUtil;// 计时器
	private String phone;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_veriftyemail);
	}

	@Override
	public BasePresenter initPresenter() {
		return new SecurityEmailPresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_verifyemail), R.drawable.image_back);
	}

	@Override
	public void initData() {
		// 计1S一换
		timeCountUtil = new TimeCount(60000, 1000, sendVerifty, mContext);
		((SecurityEmailPresenter) mPresenter).getUser(MainApplication.getUserId());
	}

	@OnClick({R.id.app_submit, R.id.login_send_verifty})
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.login_send_verifty) {
			//发送验证码
			((SecurityEmailPresenter) mPresenter).sendVerify(getString(R.string.FSSENDEMAIL), rEmail_et.getText().toString());
		} else if (view.getId() == R.id.app_submit) {
			//提交
			((SecurityEmailPresenter) mPresenter).reqVerify(getString(R.string.FSVERITYEMAIL), register_pwd_et.getText().toString());
		}
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();
		startAnimActivity(SecuriryPhoneActivity.class);
		finish();
	}

	@Override
	public void setUser(User user) {
		//获取本地用户信息
		this.user = user;
		if (!TextUtils.isEmpty(user.getEmail()))
			rEmail_et.setText(user.getEmail());
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	@Override
	public void sendVerify() {
		hideLoadingDialog();
		timeCountUtil.start();
		// 按钮不可用
		sendVerifty.setEnabled(false);
	}

}
