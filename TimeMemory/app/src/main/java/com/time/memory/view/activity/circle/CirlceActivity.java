package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.presenter.CircleEditPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.LockActivity;
import com.time.memory.view.activity.memory.MemoryGroupActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.CircleEditHolder;
import com.time.memory.view.impl.ICircleEditView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:圈子页面(编辑部)
 * @date 2016/11/5 9:01
 */
public class CirlceActivity extends BaseActivity implements ICircleEditView, AdapterCallback {

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private List<MGroup> mMGroupList;
	private BaseRecyclerAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_circle);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_circle), R.drawable.image_back, "", R.drawable.circle_more);
	}

	@Override
	public BasePresenter initPresenter() {
		return new CircleEditPresenter();
	}

	@Override
	public void initData() {
		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
	}

	@Override
	protected void onResume() {
		super.onResume();
		((CircleEditPresenter) mPresenter).getGroups(MainApplication.getUserId());
	}

	@Override
	public void setAdapter(List<MGroup> groupList) {
		// 通讯录(ListView)
		if (swipeTarget != null) {
			if (adapter == null) {
				this.mMGroupList = groupList;
				adapter = new BaseRecyclerAdapter(mMGroupList, R.layout.item_cirlceedit, CircleEditHolder.class);
				adapter.setCallBack(this);
				swipeTarget.setAdapter(adapter);
			} else {
				mMGroupList.clear();
				mMGroupList.addAll(groupList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@OnClick(R.id.iv_main_right)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.iv_main_right) {
			//创建新圈子
			Intent intent = new Intent(this, CreateCircleActivity.class);
			intent.putExtra("isCircle", true);
			startAnimActivity(intent);
		}
	}


	@Override
	public void onCallback(Object data) {
		int postion = (int) data;
		Intent intent = new Intent();
		if (TextUtils.isEmpty(mMGroupList.get(postion).getGroupPw()))
			intent.setClass(mContext, MemoryGroupActivity.class);
		else {
			intent.setClass(mContext, LockActivity.class);
			intent.putExtra("isAddPwd", false);
		}
		intent.putExtra("mGroup", mMGroupList.get(postion));
		startAnimActivity(intent);
	}

	@Override
	public void onDataCallBack(Object data, int position) {
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
	}
}
