package com.time.memory.view.activity.mine;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.task.TimeCount;
import com.time.memory.entity.User;
import com.time.memory.gui.PwdEditText;
import com.time.memory.presenter.SecurityPwdPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IUpPwdView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:隐私修改密码
 * @date 2016/10/20 19:20
 */
public class SecuriryPwdActivity extends BaseActivity implements IUpPwdView {

	@Bind(R.id.register_code_et)
	EditText registerCodeEt;//验证码
	@Bind(R.id.register_pwd_et)
	PwdEditText registerPwdEt;//密码
	@Bind(R.id.login_send_verifty)
	TextView sendVerifty;// 发送验证码
	@Bind(R.id.register_et)
	TextView register_et;// 手机号

	private User user;
	private TimeCount timeCountUtil;// 计时器

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_securuppwd);
	}

	@Override
	protected void onDestroy() {
		if (timeCountUtil != null) {
			timeCountUtil.cancel();
			timeCountUtil = null;
		}
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return new SecurityPwdPresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_uppwd), R.drawable.image_back);
	}

	@Override
	public void initData() {
		// 计1S一换
		timeCountUtil = new TimeCount(60000, 1000, sendVerifty, mContext);
		((SecurityPwdPresenter) mPresenter).getUser(MainApplication.getUserId());
	}

	@OnClick({R.id.app_submit, R.id.login_send_verifty})
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//提交
			((SecurityPwdPresenter) mPresenter).reqUpPwd(getString(R.string.FSCHANGE), user.getUserMobile(), registerCodeEt.getText().toString(), registerPwdEt.getText().toString(), user);
		} else if (view.getId() == R.id.login_send_verifty) {
			//发送验证码
			opr();
		}
	}

	/**
	 * 判断操作
	 */
	private void opr() {
		if (user == null) {
			((SecurityPwdPresenter) mPresenter).getUser(MainApplication.getUserId());
		} else {
			//获取验证码
			((SecurityPwdPresenter) mPresenter).sendVerify(getString(R.string.FSCHANGEVERIFTY), user.getUserMobile());
		}
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();

	}

	@Override
	public void upSuccess() {
		//修改成功
		showShortToast("修改成功");
		finish();
	}

	@Override
	public void setUser(User user) {
		//获取本地用户信息
		this.user = user;
		register_et.setText(user.getUserMobile());
//		opr();
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	@Override
	public void sendVerify() {
		timeCountUtil.start();
		// 按钮不可用
		sendVerifty.setEnabled(false);
	}


}
