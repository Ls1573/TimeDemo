package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.MGroup;
import com.time.memory.presenter.UpdateCirclePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IUpdateCircleView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:修改圈子
 * @date 2016/10/18 11:32
 */
public class UpdateCircleActivity extends BaseActivity implements IUpdateCircleView {
	@Bind(R.id.circle_name_et)
	EditText circle_name_et;
	private String upTitle;

	private MGroup group;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_upcircle);
	}

	@Override
	public void initView() {
		group = getIntent().getParcelableExtra("group");
		initTopBarForLeftTv(getString(R.string.app_upcirclename), getString(R.string.app_cancle));
	}

	@Override
	public void initData() {
		circle_name_et.setText(TextUtils.isEmpty(group.getGroupName()) ? "" : group.getGroupName());
	}

	@Override
	public BasePresenter initPresenter() {
		return new UpdateCirclePresenter();
	}


	@OnClick({R.id.app_submit, R.id.tv_main_left})
	public void onClick(View view) {
		if (view.getId() == R.id.tv_main_left)
			finish();
		if (view.getId() == R.id.app_submit) {
			upTitle = circle_name_et.getText().toString();
			((UpdateCirclePresenter) mPresenter).reqUpdateCircle(getString(R.string.FSREQUPNAME), upTitle, group);
		}
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();
		setMyResult(ReqConstant.UPCIRCLE);
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	/**
	 * 设置返回
	 *
	 * @param resultCode
	 */
	private void setMyResult(int resultCode) {
		Intent intent = new Intent();
		intent.putExtra("title", upTitle);
		setResult(resultCode, intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		setMyResult(RESULT_CANCELED);
	}
}
