package com.time.memory.view.activity.login;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.GridViewWithHeaderAndFooter;
import com.time.memory.presenter.ImportSPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.StartCircleAdapter;
import com.time.memory.view.impl.IImportCircleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:激活圈子引导
 * @date 2016/10/19 21:30
 */
public class ImportCircleActivity extends BaseActivity implements IImportCircleView {

	private static final String TAG = "ImportCircleActivity";

	@Bind(R.id.import_circle_tv)
	TextView importSign;//提示

	@Bind(R.id.swipe_target)
	GridViewWithHeaderAndFooter swipe_target;

	private View viewFooter;
	private ArrayList<MGroup> unGroups;//圈子原始数据
	private ArrayList<MGroup> chGroups;//要激活圈子
	private int friends;//好友数


	private StartCircleAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_importcircle);
	}

	@Override
	public void initView() {
		unGroups = getIntent().getParcelableArrayListExtra("groups");
		friends = getIntent().getIntExtra("friends", 0);
		//设置提示信息
		importSign.setText(String.format(getString(R.string.import_circle), String.valueOf(friends), String.valueOf(unGroups.size())));

		viewFooter = LayoutInflater.from(mContext).inflate(R.layout.item_startcircle, null);
		ButterKnife.findById(viewFooter, R.id.app_submit).setOnClickListener(new MyClick());
		ButterKnife.findById(viewFooter, R.id.app_jump).setOnClickListener(new MyClick());
		swipe_target.addFooterView(viewFooter);
	}

	@Override
	public void initData() {
		//设置
		setAdapter(MainApplication.getUserId());
	}

	@Override
	public BasePresenter initPresenter() {
		return new ImportSPresenter();
	}


	@OnClick({R.id.app_cancle})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_cancle:
				//退出
				startActivity();
				break;
		}
	}

	class MyClick implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.app_submit) {
				//提交
				((ImportSPresenter) mPresenter).reqStartCircle(getString(R.string.FSSTARTCIRCLE), MainApplication.getUserToken(), MainApplication.getUserId(), chGroups);
			} else if (view.getId() == R.id.app_jump) {
				//跳过
				startActivity();
			}
		}
	}


	@Override
	public void startActivity() {
		hideLoadingDialog();
		startAnimActivity(MainActivity.class);
		finish();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@OnItemClick(R.id.swipe_target)
	public void onItemClick(View view, int position) {
		if (position >= unGroups.size()) {
		} else {
			//切换状态
			MGroup group = unGroups.get(position);
			if (chGroups.contains(group)) {
				//包含就拿掉
				group.setActiveFlg("1");
				chGroups.remove(group);
			} else {
				//没有就加入
				group.setActiveFlg("0");
				chGroups.add(group);
			}
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 填充数据
	 */
	private void setAdapter(String userId) {
		for (MGroup entity : unGroups) {
			//全部选中
			entity.setActiveFlg("0");
			entity.setComeFrom(userId);
		}
		chGroups = new ArrayList<>();
		chGroups.addAll(unGroups);
		adapter = new StartCircleAdapter(mContext, unGroups);
		swipe_target.setAdapter(adapter);
	}


}
