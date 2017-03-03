package com.time.memory.view.activity.login;

import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.presenter.RegistPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.WebActivity;
import com.time.memory.view.impl.IRegistView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:注册页(输入手机号)
 * @date 2016-9-6上午8:39:53
 * ==============================
 */
public class RegistActivity extends BaseActivity implements IRegistView {
	@Bind(R.id.register_et)
	EditText register_et;
	@Bind(R.id.about_proto_tv)
	TextView about_proto_tv;

	private String phone;//手机号

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_regist);
	}

	@Override
	public BasePresenter initPresenter() {
		return new RegistPresenter();
	}

	@Override
	public void initView() {
//		initTopBarForOnlyTitle(getString(R.string.app_register));
		setOnHref("注册即代表同意«", "用户协议", "»");
	}

	@Override
	public void initData() {
		phone = getIntent().getStringExtra("phone");
		register_et.setText(phone);
	}

	@OnClick({R.id.app_submit, R.id.app_cancle, R.id.about_proto_tv})
	public void onClick(View view) {
		if (view.getId() == R.id.app_submit) {
			phone = register_et.getText().toString();
			//提交-发送验证码
			((RegistPresenter) mPresenter).sendVerify(getString(R.string.FSGETVALIDCODE), phone);
		} else if (R.id.app_cancle == view.getId()) {
			finish();
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		//发送成功
		Intent intent = new Intent(mContext, SettingPwdActivity.class);
		intent.putExtra("phone", phone);
		startAnimActivity(intent);
		finish();
	}

	@Override
	public void showFaild() {
		//发送失败
		hideLoadingDialog();
	}

	/**
	 * 设置超链接
	 */
	private void setOnHref(String top, String proto, String end) {
		SpannableString spanttt = new SpannableString(proto);
		ClickableSpan clickttt = new MClickableSpan();

		spanttt.setSpan(clickttt, 0, proto.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		about_proto_tv.setText(top);
		about_proto_tv.append(spanttt);
		about_proto_tv.append(end);
		about_proto_tv.setMovementMethod(LinkMovementMethod.getInstance());

		//设置高亮背景,注释掉后会有默认选中背景色
		about_proto_tv.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
	}


	/**
	 * 超链接点击
	 */
	class MClickableSpan extends ClickableSpan {
		public MClickableSpan() {
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			//链接字体颜色-黄
			ds.setColor(mContext.getResources().getColor(R.color.yellow_CEA));
		}

		@Override
		public void onClick(View widget) {
			//用户协议
			Intent intent = new Intent(mContext, WebActivity.class);
			intent.putExtra("url", getString(R.string.FSAGREEMENT));
			intent.putExtra("title","用户协议");
			startAnimActivity(intent);
		}
	}

}
