package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.User;
import com.time.memory.gui.ClearEditText;
import com.time.memory.presenter.RemarkPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IReMarkView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:设置备注
 * @date 2016/11/30 10:22
 */
public class ReMarkActivity extends BaseActivity implements IReMarkView {

	@Bind(R.id.mine_remark_et)
	ClearEditText mine_remark_et;

	private User user;
	private int position;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_remark);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_remark), R.drawable.image_back, getString(R.string.app_save), -1);
	}

	@Override
	public void initData() {
		user = getIntent().getParcelableExtra("user");
		position = getIntent().getIntExtra("position", -1);
		if (!TextUtils.isEmpty(user.getUserName()))
			mine_remark_et.setText(user.getUserName());
	}

	@Override
	public BasePresenter initPresenter() {
		return new RemarkPresenter();
	}


	@OnClick({R.id.tv_main_right})
	public void onClick(View view) {
		super.onMyClick(view);
		if (R.id.tv_main_right == view.getId()) {
			//保存
			((RemarkPresenter) mPresenter).upContacts(getString(R.string.FSUPCONTACTS), user.getUserId(), mine_remark_et.getText().toString());
		}
	}


	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void upSuccess(String userName) {
		Intent intent = new Intent();
		intent.putExtra("userName", userName);
		if (position == -1) {
			setResult(ReqConstant.REQUEST_CODE_REMARK, intent);
		} else {
			intent.putExtra("position", position);
			intent.setClass(mContext, ShowCircleActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startAnimActivity(intent);
		}
		finish();
	}
}
