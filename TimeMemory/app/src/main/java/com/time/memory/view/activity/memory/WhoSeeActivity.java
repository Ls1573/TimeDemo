package com.time.memory.view.activity.memory;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.time.memory.R;
import com.time.memory.core.callback.OnExpenListClick;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.UserGroup;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.NoSeeAdapter;
import com.time.memory.view.impl.INoSeeView;

import java.util.List;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:给谁看
 * @date 2016/9/13 15:04
 * ==============================
 */
public class WhoSeeActivity extends BaseActivity implements INoSeeView, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener, OnExpenListClick {
	private static final String TAG = "NoSeeActivity";
	@Bind(R.id.swipe_target)
	ExpandableListView swipe_target;//扩展

	private NoSeeAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_nosee);
	}

	@Override
	public void initView() {
		//TODO 获取title
		int title = getIntent().getIntExtra("title", R.string.app_nosee);
		initTopBarForBoth(getString(title), R.drawable.image_back, getString(R.string.app_confirm), -1);
		View viewSearch = LayoutInflater.from(mContext).inflate(R.layout.item_nosee_search, null);
		if (swipe_target != null) swipe_target.addHeaderView(viewSearch);
	}


	@Override
	public void initData() {
		swipe_target.setOnGroupExpandListener(this);
		swipe_target.setOnGroupCollapseListener(this);
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		//点击展开监听
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		//点击父组回缩监听
	}

	@Override
	public void onGroupCheck(int groupPosition) {
		//点击父类复选框
		CLog.e(TAG, "onGroupCheck:" + groupPosition);

	}

	@Override
	public void onGroupAll(int groupPosition) {
		//点击父类全选
		CLog.e(TAG, "onGroupAll:" + groupPosition);
	}

	@Override
	public void setAdapter(List<MGroup> MGroupEntities, List<UserGroup> userEntities) {
		//设置Adapter
		if (swipe_target != null) {
			adapter = new NoSeeAdapter(mContext, MGroupEntities, userEntities);
			adapter.setOnExpenListClick(this);
			swipe_target.setAdapter(adapter);
		}
	}

	@Override
	public void nofiAdapter() {
		//更新数据
		if (swipe_target != null && adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}
}
