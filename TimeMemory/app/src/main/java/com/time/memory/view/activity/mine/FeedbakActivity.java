package com.time.memory.view.activity.mine;

import android.view.View;

import com.time.memory.R;
import com.time.memory.gui.WriterEditText;
import com.time.memory.presenter.FeedBackPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:意见反馈
 * @date 2016/10/14 15:59
 */
public class FeedbakActivity extends BaseActivity implements IBaseView {

	@Bind(R.id.feedback_et)
	WriterEditText feedback_et;//输入内容

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_feedback);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_feedback), R.drawable.image_back);
	}

	@Override
	public void initData() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new FeedBackPresenter();
	}

	@OnClick(R.id.app_submit)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//提交
			((FeedBackPresenter) mPresenter).reqFeedback(getString(R.string.FSFEEDBACK), feedback_et.getText().toString().trim());
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		showShortToast("感谢您的反馈!");
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}
}
