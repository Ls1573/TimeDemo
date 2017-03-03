package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.DividerItemDecoration;
import com.time.memory.presenter.TagManagerPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.circle.GroupTagActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.TagManagerHolder;
import com.time.memory.view.impl.ITagManagerView;

import java.util.List;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:标签管理
 * @date 2016/12/1 9:22
 */
public class TagManagerActivity extends BaseActivity implements ITagManagerView, AdapterCallback {

	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	private BaseRecyclerAdapter adapter;
	private List<MGroup> mGroupList;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_tagmanager);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_tagmanager), R.drawable.image_back);
	}

	@Override
	public void initData() {
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		swipe_target.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, ContextCompat.getColor(mContext, R.color.grey_divider), 1));

		((TagManagerPresenter) mPresenter).getMyAdminGroup(MainApplication.getUserId());
	}

	@Override
	public BasePresenter initPresenter() {
		return new TagManagerPresenter();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void setAdapter(List<MGroup> mGroups) {
		if (swipe_target != null) {
			this.mGroupList = mGroups;
			adapter = new BaseRecyclerAdapter(mGroupList, R.layout.item_tagmanager, TagManagerHolder.class);
			adapter.setCallBack(this);
			swipe_target.setAdapter(adapter);
		}
	}


	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		Intent intent = new Intent();
		if (position == 0) {
			//我的
			intent.putExtra("Id", MainApplication.getUserId());
			intent.putExtra("state", 1);
			intent.setClass(mContext, AddTagActivity.class);
		} else {
			//群的
			intent.setClass(mContext, GroupTagActivity.class);
			intent.putExtra("isRetuen", false);
			intent.putExtra("Id", mGroupList.get(position).getGroupId());
		}
		startAnimActivity(intent);
	}
}
