package com.time.memory.view.activity.login;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.gui.ClearEditText;
import com.time.memory.gui.PwdEditText;
import com.time.memory.presenter.LoginPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.ILoginView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0
 * @Package com.exesc.secondcar.ui.activity.login
 * @Description:登录
 * @date 2016-9-5 下午4:20:19
 * ==============================
 */
public class LoginActivity extends BaseActivity implements ILoginView {
	private static final String TAG = "LoginActivity";

	@Bind(R.id.login_forgetpwd)
	TextView login_forgetpwd;// 忘记密码
	@Bind(R.id.login_phone)
	ClearEditText loginPhone;//手机号
	@Bind(R.id.login_pwd)
	PwdEditText loginPwd;//密码

	private String pwd;//密码

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		loginPwd = null;
	}

	@Override
	public BasePresenter initPresenter() {
		return new LoginPresenter();
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
	}


	@OnClick({R.id.login_forgetpwd, R.id.app_submit, R.id.login_register, R.id.login_wechat_tv})
	public void onClick(View view) {
		super.onMyClick(view);
		switch (view.getId()) {
			case R.id.login_wechat_tv:
				showShortToast("微信登录");
				break;
			case R.id.login_forgetpwd:
				// 忘记密码
				Intent intent = new Intent();
				intent.setClass(mContext, UpPwdActivity.class);
				intent.putExtra("opr", getString(R.string.app_findpwd));
				startAnimActivity(intent);
				break;
			case R.id.login_register:
				// 立即注册
				((LoginPresenter) mPresenter).reqRegister(loginPhone.getText().toString());
				break;
			case R.id.app_submit: {
				//登录
				pwd = loginPwd.getText().toString();
				((LoginPresenter) mPresenter).requestLogin(getString(R.string.FSLOGIN), loginPhone.getText().toString(), pwd);
			}
			break;
		}
	}

	@Override
	public void reqRegister(String phone) {
		Intent intent = new Intent();
		intent.setClass(mContext, RegistActivity.class);
		intent.putExtra("phone", phone);
		startAnimActivity(intent);
	}

	@Override
	public void showSuccess() {
		//失败
		hideLoadingDialog();
		//成功
		redirectTo();
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	// 重定向
	private void redirectTo() {
		startAnimActivity(MainActivity.class);
		finish();
	}

	/**
	 * 导通讯录
	 */
	@Override
	public void reqImportConstacts() {
		//失败
		hideLoadingDialog();
		startAnimActivity(ImportContactsActivity.class);
		finish();
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
		DialogUtils.request(LoginActivity.this, "确定退出时光记忆吗", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isSure = (boolean) data;
				if (isSure) {
					finish();
				}
			}
		});
	}
}
