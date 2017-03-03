package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.Memory;
import com.time.memory.entity.UnForkDto;
import com.time.memory.presenter.UnReadForkPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.UnReadForkHolder;
import com.time.memory.view.impl.IUnReadForkView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:未读点赞评论
 * @date 2016/11/24 15:38
 */
public class UnReadForkActivity extends BaseActivity implements IUnReadForkView, AdapterCallback {
	private static final String TAG = "UnReadForkActivity";
	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private ArrayList<UnForkDto> unReads;
	private BaseRecyclerAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_unread_memory);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_fork), R.drawable.image_back);
		initLoading();
		fl_loading.withEmptyIco(R.drawable.commentemp)
				.withLoadedEmptyText("")
				.withBtnEmptyEnnable(false);
	}

	@Override
	public void initData() {
		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
		//获取数据
		((UnReadForkPresenter) mPresenter).reqUnReadForks(getString(R.string.FSUNREADFORK));
	}

	@Override
	public BasePresenter initPresenter() {
		return new UnReadForkPresenter();
	}

	@Override
	public void setAdapter(ArrayList<UnForkDto> list) {
		if (swipeTarget != null) {
			if (adapter == null) {
				this.unReads = list;
				adapter = new BaseRecyclerAdapter(unReads, R.layout.item_unread_fork, UnReadForkHolder.class);
				adapter.setCallBack(this);
				swipeTarget.setAdapter(adapter);
			} else {
				this.unReads.clear();
				unReads.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}

	}

	@Override
	public void showSuccess() {
		super.showSuccess();
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		super.showFaild();
		hideLoadingDialog();
	}

	@Override
	public void showEmpty() {
		//结果为空
		hideLoadingDialog();
		swipeTarget.setVisibility(View.GONE);
		super.showEmpty();
	}

	@Override
	public void refreshAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	@Override
	public void closeActivity() {
		finish();
	}

	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		start(position);
	}

	private void start(int position) {
		Intent intent = new Intent(this, MemoryDetailActivtiy.class);
		UnForkDto unForkDto = unReads.get(position);
		if (unForkDto.getMfgflag().equals("1")) {
			//我的
			ArrayList<Memory> memories = new ArrayList<>();
			memories.add(new Memory(unForkDto.getMemoryId(), unForkDto.getMemorySrcId(), unForkDto.getuId(), unForkDto.getMtitle()));
			intent.putExtra("memorys", memories);
			intent.putExtra("state", 1);
			intent.putExtra("title", "我的记忆");
		}
//		else if (unForkDto.getMfgflag().equals("2")) {
//			//他的
//			ArrayList<OtherMemory> otherList = new ArrayList<>();
//			otherList.add(new OtherMemory(unForkDto.getMemoryId(), unForkDto.getuId(), unForkDto.getMtitle()));
//			intent.putExtra("otherList", otherList);
//			intent.putExtra("state", 0);
//			intent.putExtra("title", "他的记忆");
//		}
		else {
			//圈子
			ArrayList<GroupMemory> groupList = new ArrayList<>();
			groupList.add(new GroupMemory(unForkDto.getMemoryId(), unForkDto.getMemorySrcId(), unForkDto.getuId(), unForkDto.getMtitle()));
			intent.putExtra("groupList", groupList);
			intent.putExtra("title", unForkDto.getGroupName());
			intent.putExtra("state", 2);
		}
		intent.putExtra("type", 1);
		intent.putExtra("groupId", unForkDto.getGroupId());
		intent.putExtra("isSignle", true);
		intent.putExtra("pageCount", 5);
		intent.putExtra("position", 0);
		startActivity(intent);
		//移除
		((UnReadForkPresenter) mPresenter).removeList(position, unForkDto, unReads);
	}
}
