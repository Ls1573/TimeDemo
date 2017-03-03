package com.time.memory.view.activity.circle;

import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.gui.ClearEditText;
import com.time.memory.presenter.NewFriendPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.INewFriendiew;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:新好友
 * @date 2016/9/24 8:57
 */
public class NewFriendActivity extends BaseActivity implements INewFriendiew {

	@Bind(R.id.newfriend_name_tv)
	ClearEditText newfriendNameTv;//用户名
	@Bind(R.id.newfriend_phone_tv)
	ClearEditText newfriendPhoneTv;//手机号

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_newfriend);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_newfriend), R.drawable.image_back);
	}

	@Override
	public BasePresenter initPresenter() {
		return new NewFriendPresenter();
	}

	@Override
	public void initData() {
	}


	@OnClick(R.id.app_submit)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//上传
			((NewFriendPresenter) mPresenter).addNewFriend(MainApplication.getUserId(), newfriendNameTv.getText().toString(), newfriendPhoneTv.getText().toString());
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		KeyBoardUtils.hideKeyboard(this);
		setResult(ReqConstant.REQUEST_CODE_NEWFRIEND);
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}
}
