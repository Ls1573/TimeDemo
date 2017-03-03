package com.time.memory.view.activity.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.task.TimeCount;
import com.time.memory.gui.PwdEditText;
import com.time.memory.presenter.AddEmailPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBindEmailView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:绑定邮箱
 * @date 2016/11/24 11:59
 */
public class UpEmailActivity extends BaseActivity implements IBindEmailView {
	@Bind(R.id.mine_bemail_ll)
	RelativeLayout mine_bemail_ll;//绑定邮箱外框

	@Bind(R.id.mine_email_ll)
	LinearLayout mine_email_ll;//邮箱
	@Bind(R.id.register_email_et)
	EditText rEmail_et;//邮箱
	@Bind(R.id.mine_emailverify_et)
	PwdEditText emailVerify_et;//邮箱验证码
	@Bind(R.id.login_send_verifty)
	TextView sendVerifty;// 发送验证码
	@Bind(R.id.app_submit)
	TextView app_submit;// 绑定
	@Bind(R.id.mine_email_tv)
	TextView mine_email_tv;// 邮箱


	private TimeCount timeCountUtil;// 计时器
	private String email;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_email);
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
	public void initView() {
		initTopBarForLeft(getString(R.string.app_email), R.drawable.image_back);
	}

	@Override
	public BasePresenter initPresenter() {
		return new AddEmailPresenter();
	}

	@Override
	public void initData() {
		email = getIntent().getStringExtra("email");
		setEmailRl();
		timeCountUtil = new TimeCount(60 * 1000, 1000, sendVerifty, mContext);
	}

	@OnClick({R.id.tv_main_right, R.id.login_send_verifty, R.id.app_submit})
	public void onClick(View view) {
		int Id = view.getId();
		if (Id == R.id.app_cancle) {
			//取消
			setResultBack(ReqConstant.EMAIL);
		} else if (Id == R.id.login_send_verifty) {
			//发送验证码
			((AddEmailPresenter) mPresenter).sendVerify(getString(R.string.FSSENDEMAIL), rEmail_et.getText().toString());
		} else if (Id == R.id.app_submit) {
			//绑定
			((AddEmailPresenter) mPresenter).reqBindEmail(getString(R.string.FSBINDEMAIL), rEmail_et.getText().toString(), emailVerify_et.getText().toString());
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void onBackPressed() {
		setResultBack(ReqConstant.EMAIL);
	}

	@Override
	public void sendVerify() {
		timeCountUtil.start();
		// 按钮不可用
		sendVerifty.setEnabled(false);
	}

	@Override
	public void bindSuccess(boolean isSuccess, String mEmail) {
		if (isSuccess) {
			//绑定成功
			this.email = mEmail;
			setEmailRl();
			showShortToast("绑定邮箱成功");
		}
	}

	/**
	 * 设置布局
	 */
	private void setEmailRl() {
		int visable = 0x00000008;
		int eVisable = 0x00000000;
		if (TextUtils.isEmpty(email)) {
			visable = View.VISIBLE;
			eVisable = View.GONE;
		} else {
			visable = View.GONE;
			eVisable = View.VISIBLE;
			mine_email_tv.setText(email);
		}
		mine_bemail_ll.setVisibility(visable);
		emailVerify_et.setVisibility(visable);
		app_submit.setVisibility(visable);
		mine_email_ll.setVisibility(eVisable);
	}

	/**
	 * 返回
	 */
	private void setResultBack(int resultCode) {
		Intent intent = new Intent();
		intent.putExtra("email", email);
		setResult(resultCode, intent);
		finish();
	}
}
