package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.gui.ClearEditText;
import com.time.memory.presenter.MineInfoPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:修改地址
 * @date 2016/9/23 9:23
 */
public class UpAddressActivity extends BaseActivity implements IBaseView {

	@Bind(R.id.mine_address_et)
	ClearEditText mine_address_et;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_upaddress);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MineInfoPresenter();
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_address), R.drawable.image_back, getString(R.string.app_save), -1);
	}

	@Override
	public void initData() {
		String address = getIntent().getStringExtra("address");
		mine_address_et.setText(TextUtils.isEmpty(address) ? "" : address);
	}

	@OnClick(R.id.tv_main_right)
	public void onClick(View view) {
		if (view.getId() == R.id.app_cancle) {
			//取消
			setResultBack(Activity.RESULT_CANCELED, "");
		}
		if (view.getId() == R.id.tv_main_right) {
			//返回
			setResultBack(ReqConstant.ADDRESS, mine_address_et.getText().toString());
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
		intent.putExtra("address", address);
		setResult(resultCode, intent);
		finish();
	}

}
