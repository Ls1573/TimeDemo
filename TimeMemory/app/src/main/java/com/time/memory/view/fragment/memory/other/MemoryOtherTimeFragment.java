package com.time.memory.view.fragment.memory.other;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.OtherMemorys;
import com.time.memory.gui.sticky.LayoutManager;
import com.time.memory.presenter.MemoryOtherTimePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.memory.MemoryDetailActivtiy;
import com.time.memory.view.adapter.OtherTimeAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.impl.IMemoryOtherTimeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:他的记忆信息(时间)
 * @date 2016/9/19 13:36
 */
public class MemoryOtherTimeFragment extends BaseFragment implements IMemoryOtherTimeView, AdapterCallback {
	public static final String TAG = "MemoryMonthFragment";

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;
	@Bind(R.id.memory_sign_rl)
	RelativeLayout memory_sign_rl;

	private OtherTimeAdapter mAdapter;

	private ArrayList<OtherMemory> otherMemories;

	private int curPage = 1;//当前页
	private int pageCount = 5;//每页显示数
	private String groupId;//Id
	private boolean isLoad;//加载
	private boolean isCanLoad = true;

	private LayoutManager layoutManager;

	private final int REQUEST_CODE_GALLERY = 101;
	private int type;//隐私权限

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		groupId = getArguments().getString("groupId");
		type = getArguments().getInt("type");
	}

	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container,
							   Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memorytime, null);
		return view;
	}

	@Override
	public void onDestroy() {
		if (layoutManager != null)
			layoutManager.onDestory();
		if (otherMemories != null) {
			otherMemories.clear();
			otherMemories = null;
		}
		if (mAdapter != null) {
			mAdapter.unRegisterCallBack();
			mAdapter = null;
		}
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryOtherTimePresenter();
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData() {
		otherMemories = new ArrayList<>();

		mAdapter = new OtherTimeAdapter(mContext);
		mAdapter.setCallBack(this);
		layoutManager = new LayoutManager.Builder(mContext).addAdapter(mAdapter).build();
		swipeTarget.setLayoutManager(layoutManager);
		swipeTarget.setAdapter(mAdapter);
		//滚动监听
		addScrollerListener();
		//去闪屏
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
	}

	@Override
	public void setAdapter(List<OtherMemorys> list, ArrayList<OtherMemory> otherMemories) {
		//设置Adapter
		if (swipeTarget != null) {
			isLoad = true;
			if (list != null && !list.isEmpty()) {
				this.otherMemories.addAll(otherMemories);
				mAdapter.addAll(list);
				memory_sign_rl.setVisibility(View.VISIBLE);
			} else {
				isCanLoad = false;
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && !isLoad) {
			//获取数据
			((MemoryOtherTimePresenter) mPresenter).getMessage(curPage, pageCount, "", groupId, mAdapter.getItemCount());
		}
	}

	/**
	 * 滚动监听
	 */
	private void addScrollerListener() {
		//RecyclerView滑动监听
		swipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (!ViewCompat.canScrollVertically(recyclerView, 1) && isCanLoad && curPage != 1) {
					((MemoryOtherTimePresenter) mPresenter).getMessage(curPage, pageCount, "", groupId, mAdapter.getItemCount());
				}
			}
		});
	}


	@Override
	public void showSuccess() {
		hideLoadingDialog();
		curPage++;
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
		isCanLoad = false;
		curPage++;
	}

	@Override
	public void showEmpty() {
		hideLoadingDialog();
		isCanLoad = false;
		mAdapter.showEmpry();
	}

	@Override
	public void onDataCallBack(Object data, int position) {
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {

	}

	@Override
	public void onCallback(Object data) {
		String memoryId = (String) data;
		int position = ((MemoryOtherTimePresenter) mPresenter).getPosition(memoryId, otherMemories);
		Intent intent = new Intent(mContext, MemoryDetailActivtiy.class);
		intent.putParcelableArrayListExtra("otherList", otherMemories);
		intent.putExtra("position", position);
		intent.putExtra("pageCount", pageCount);
		intent.putExtra("state", 0);
		intent.putExtra("groupId", "");
		intent.putExtra("type", 2);
		intent.putExtra("title", "TA的的记忆");
		intent.putExtra("title", otherMemories.get(position).getUserNameF() + "的记忆");
		startActivityForResult(intent, ReqConstant.REQUEST_CODE_DETAILS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (resultCode == ReqConstant.REQUEST_CODE_DETAILS) {
			try {
				curPage = data.getIntExtra("curPage", curPage);
				isCanLoad = data.getBooleanExtra("isCanLoad", isCanLoad);
				//集合
				ArrayList<OtherMemory> oList = data.getParcelableArrayListExtra("list");
				//清空
				otherMemories.clear();
				otherMemories.addAll(((MemoryOtherTimePresenter) mPresenter).convert(oList));


				mAdapter.clearEmpty();
				mAdapter.addAll(((MemoryOtherTimePresenter) mPresenter).convert(otherMemories, mAdapter.getItemCount()));


				if (!isCanLoad)
					showEmpty();

				swipeTarget.scrollToPosition(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
