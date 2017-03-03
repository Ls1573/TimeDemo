package com.time.memory.view.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.time.memory.R;
import com.time.memory.presenter.RegistPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IRegistView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:输入手机号
 * @date 2016/9/23 11:49
 */
public class PhoneActivity extends BaseActivity implements IRegistView {
	@Bind(R.id.register_et)
	EditText register_et;
	private String phone;//手机号

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_phone);
	}

	@Override
	public BasePresenter initPresenter() {
		return new RegistPresenter();
	}

	@Override
	public void initView() {
		//找回密码,修改密码
		String opr = getIntent().getStringExtra("opr");
	}

	@Override
	public void initData() {
	}

	@OnClick(R.id.app_submit)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//下一步
			phone = register_et.getText().toString();
			//提交-发送验证码
			((RegistPresenter) mPresenter).sendVerify(getString(R.string.FSCHANGEVERIFTY), phone);
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		//发送成功
		Intent intent = new Intent(mContext, UpPwdActivity.class);
		intent.putExtra("phone", phone);
		intent.putExtra("opr", getString(R.string.app_findpwd));
		startAnimActivity(intent);
		finish();
	}

	@Override
	public void showFaild() {
		//发送失败
		hideLoadingDialog();
	}
}
