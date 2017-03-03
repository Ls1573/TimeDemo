package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.User;
import com.time.memory.presenter.SettingPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.ISecurityView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:账户安全
 * @date 2016/9/18 15:32
 */
public class SecurityActivity extends BaseActivity implements ISecurityView {
	@Bind(R.id.security_phone_tv)
	TextView security_phone_tv;

	private User mUser;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_security);
	}

	@Override
	public BasePresenter initPresenter() {
		return new SettingPresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_security), R.drawable.image_back);
	}

	@Override
	public void initData() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		((SettingPresenter) mPresenter).getUser(MainApplication.getUserId());
	}

	@OnClick({R.id.mine_phone_ll, R.id.mine_pwd_ll})
	public void onClick(View view) {
		super.onMyClick(view);
		switch (view.getId()) {
			case R.id.mine_phone_ll:
				//修改手机号
				upPhone();
				break;
			case R.id.mine_pwd_ll:
				startAnimActivity(SecuriryPwdActivity.class);
				break;
		}
	}

	@Override
	public void setUser(User user) {
		this.mUser = user;
		//手机号
		security_phone_tv.setText(mUser.getUserMobile());
	}

	/**
	 * 更改手机号判定
	 */
	private void upPhone() {
		if (TextUtils.isEmpty(mUser.getEmail())) {
			//去绑定
			bindDialog();
		} else {
			//去修改
			Intent intent = new Intent(mContext, SecuriryEmailActivity.class);
			startAnimActivity(intent);
		}
	}


	/**
	 * 弹窗提示
	 */
	private void bindDialog() {
		DialogUtils.request(SecurityActivity.this, "未绑定邮箱,去绑定", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isBind = (boolean) data;
				if (isBind) {
					Intent intent = new Intent(mContext, UpEmailActivity.class);
					startActivityForResult(intent, ReqConstant.EMAIL);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (data == null) return;
		if (resultCode == ReqConstant.EMAIL) {
			String email = data.getStringExtra("email");
			if (!TextUtils.isEmpty(email))
				mUser.setEmail(email);
		}
	}
}
