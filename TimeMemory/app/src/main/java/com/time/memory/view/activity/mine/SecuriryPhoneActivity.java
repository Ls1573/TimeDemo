package com.time.memory.view.activity.mine;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.task.TimeCount;
import com.time.memory.entity.User;
import com.time.memory.presenter.SecurityPhonePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IUpPhoneView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:隐私修改手机号
 * @date 2016/10/20 19:20
 */
public class SecuriryPhoneActivity extends BaseActivity implements IUpPhoneView {

	@Bind(R.id.register_et)
	EditText rPhone_et;//手机号
	@Bind(R.id.register_code_et)
	EditText veriftyEt;//手机验证码
	@Bind(R.id.login_send_verifty)
	TextView sendVerifty;// 发送手机验证码

	private User user;
	private TimeCount timeCountUtil;// 计时器
	private String phone;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_upphone);
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
		return new SecurityPhonePresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_upphone), R.drawable.image_back);
	}

	@Override
	public void initData() {
		// 计1S一换
		timeCountUtil = new TimeCount(60000, 1000, sendVerifty, mContext);
		((SecurityPhonePresenter) mPresenter).getUser(MainApplication.getUserId());
	}

	@OnClick({R.id.app_submit, R.id.login_send_verifty})
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//提交
			phone = rPhone_et.getText().toString();
			((SecurityPhonePresenter) mPresenter).reqUpPhone(getString(R.string.FSUPDATEPHONE), phone, veriftyEt.getText().toString(), user.getUserId());
		} else if (view.getId() == R.id.login_send_verifty) {
			//发送验证码
			((SecurityPhonePresenter) mPresenter).sendVerify(getString(R.string.FSUPPHONECODE), rPhone_et.getText().toString());
		}
	}

	@Override
	public void upSuccess(String phone) {
		//修改成功
		user.setUserMobile(phone);
		((SecurityPhonePresenter) mPresenter).upUser(user);
	}

	@Override
	public void setUser(User user) {
		//获取本地用户信息
		this.user = user;
	}

	@Override
	public void closeActivity() {
//		Intent intent = new Intent();
//		intent.putExtra("phone", phone);
//		setResult(ReqConstant.REQUEST_CODE_UPHONE, intent);
		finish();
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();

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
