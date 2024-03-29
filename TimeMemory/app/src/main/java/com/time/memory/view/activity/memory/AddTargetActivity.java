package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.MGroup;
import com.time.memory.presenter.AddTargetPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.ForwardHolder;
import com.time.memory.view.impl.IForwardView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:发布记忆切换目标
 * @date 2016/11/22 15:59
 */
public class AddTargetActivity extends BaseActivity implements IForwardView, AdapterCallback {
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;

	private String memoryId;//记忆Id
	private int state;//权限
	private String groupId;//群Id
	private BaseRecyclerAdapter adapter;
	private List<MGroup> groupList;
	private int lastPosition = -1;//上一次位置

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_forward);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_addtarget), R.drawable.image_back, getString(R.string.app_confirm), -1);
	}

	@Override
	public void initData() {
		//记忆Id
		groupId = getIntent().getStringExtra("groupId");
		state = getIntent().getIntExtra("state", 1);

		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		//去闪屏
		((DefaultItemAnimator) swipe_target.getItemAnimator()).setSupportsChangeAnimations(false);
		//获取圈子
		((AddTargetPresenter) mPresenter).getGroups(MainApplication.getUserId(), state, groupId);
	}

	@Override
	public BasePresenter initPresenter() {
		return new AddTargetPresenter();
	}

	@OnClick({R.id.tv_main_right})
	public void onClick(View view) {
		super.onMyClick(view);
		if (R.id.tv_main_right == view.getId()) {
			//确定
			if (lastPosition == -1) {
				showShortToast("请选择一项");
			} else {
				setMyResult(RESULT_CANCELED, groupList.get(lastPosition));
			}
		}
	}

	@Override
	public void setAdapter(List<MGroup> groups) {
		if (swipe_target != null) {
			this.groupList = groups;
			adapter = new BaseRecyclerAdapter(groupList, R.layout.item_forward, ForwardHolder.class);
			adapter.setCallBack(this);
			swipe_target.setAdapter(adapter);
		}
	}

	@Override
	public void finishActivity(int type, boolean isRemove, String groupName) {
		if (type == 0)
			setResult(ReqConstant.REQUEST_CODE_FORWARD);
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


	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		if (lastPosition != -1 && lastPosition != position) {
			groupList.get(lastPosition).setIsCheck(false);
			adapter.notifyItemChanged(lastPosition);
		}

		groupList.get(position).setIsCheck(true);
		adapter.notifyItemChanged(position);
		lastPosition = position;
	}

	@Override
	public void onDataCallBack(Object data, int position) {
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
	}

	@Override
	public void onBackPressed() {
		setMyResult(RESULT_CANCELED, null);
	}

	private void setMyResult(int resultCode, MGroup mGroup) {
		Intent intent = new Intent();
		intent.putExtra("mGroup", mGroup);
		setResult(ReqConstant.REQUEST_CODE_ADDTARGET, intent);
		finish();
	}
}
