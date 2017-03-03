package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.presenter.MineInfoPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:修改个性签名
 * @date 2016-9-22上午11:28:10
 * ==============================
 */
public class UpSignActivity extends BaseActivity implements IBaseView, TextWatcher {
	@Bind(R.id.update_upsign_et)
	EditText upsignEt;//输入
	@Bind(R.id.update_upsign_tv)
	TextView update_upsign_tv;//提示

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_upsign);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MineInfoPresenter();
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_sign), R.drawable.image_back, getString(R.string.app_save), -1);
	}

	@Override
	public void initData() {
		String sign = getIntent().getStringExtra("sign");
		upsignEt.setText(TextUtils.isEmpty(sign) ? "" : sign);

		upsignEt.addTextChangedListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		//设置长度
		update_upsign_tv.setText(String.valueOf(30 - s.toString().length()));
	}


	@OnClick(R.id.tv_main_right)
	public void onClick(View view) {
		if (view.getId() == R.id.app_cancle) {
			//取消
			setResultBack(Activity.RESULT_CANCELED, "");
		}
		if (view.getId() == R.id.tv_main_right) {
			//返回
			setResultBack(ReqConstant.SIGN, upsignEt.getText().toString());
		}
	}

	@Override
	public void onBackPressed() {
		setResultBack(Activity.RESULT_CANCELED, "");
	}

	/**
	 * 返回
	 *
	 * @param address
	 */
	private void setResultBack(int resultCode, String address) {
		Intent intent = new Intent();
		intent.putExtra("sign", address);
		setResult(resultCode, intent);
		finish();
	}

}
