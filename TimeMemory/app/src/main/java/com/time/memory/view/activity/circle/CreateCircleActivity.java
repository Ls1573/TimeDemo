package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.time.memory.R;
import com.time.memory.presenter.CreateCirclePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.ICreateView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:新建圈子
 * @date 2016/9/12 18:18
 * ==============================
 */
public class CreateCircleActivity extends BaseActivity implements ICreateView {
	@Bind(R.id.circle_name_et)
	EditText circle_name_et;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_addcircle);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_addcircle), R.drawable.image_back);
	}
	@Override
	public void initData() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new CreateCirclePresenter();
	}


	@OnClick({R.id.app_submit})
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit)
			((CreateCirclePresenter) mPresenter).choseFriend(circle_name_et.getText().toString().trim(),getIntent().getBooleanExtra("isCircle",false));

	}

	@Override
	public void success(Intent intent) {
		startAnimActivity(intent);
	}
}
