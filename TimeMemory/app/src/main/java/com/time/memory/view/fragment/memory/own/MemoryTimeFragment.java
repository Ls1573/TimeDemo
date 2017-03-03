package com.time.memory.view.fragment.memory.own;


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
import com.time.memory.entity.Memory;
import com.time.memory.entity.Memorys;
import com.time.memory.gui.sticky.LayoutManager;
import com.time.memory.presenter.MemoryTimePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.memory.MemoryDetailActivtiy;
import com.time.memory.view.adapter.TimeAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.impl.IMemoryTimeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆信息(时间)
 * @date 2016/9/19 13:36
 */
public class MemoryTimeFragment extends BaseFragment implements IMemoryTimeView, AdapterCallback {
	public static final String TAG = "MemoryMonthFragment";
	private final int REQUEST_CODE_GALLERY = 101;

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	@Bind(R.id.memory_sign_rl)
	RelativeLayout memory_sign_rl;
	private TimeAdapter mAdapter;
	private ArrayList<Memory> memories;

	private int curPage = 1;//当前页
	private int pageCount = 3;//每页显示数
	private int lastVisibleItem;//最后一项
	private boolean isLoad;//加载
	private boolean isCanLoad = true;

	private int type;//隐私权限
	private String groupId;//Id
	private LayoutManager layoutManager;

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
	public BasePresenter initPresenter() {
		return new MemoryTimePresenter();
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData() {
		memories = new ArrayList<>();

		mAdapter = new TimeAdapter();
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
	public void onDestroy() {
		if (layoutManager != null)
			layoutManager.onDestory();
		if (memories != null) {
			memories.clear();
			memories = null;
		}
		if (mAdapter != null) {
			mAdapter.unRegisterCallBack();
			mAdapter = null;
		}
		super.onDestroy();
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
					((MemoryTimePresenter) mPresenter).getMessage(curPage, pageCount, mAdapter.getItemCount());
				}
			}
		});
	}


	@Override
	public void setAdapter(List<Memorys> list, ArrayList<Memory> memories) {
		//设置Adapter
		if (swipeTarget != null) {
			isLoad = true;
			if (list != null && !list.isEmpty()) {
				this.memories.addAll(memories);
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
			//没有加载数据-获取
			((MemoryTimePresenter) mPresenter).getMessage(curPage, pageCount, mAdapter.getItemCount());
		}
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
		Intent intent = new Intent(mContext, MemoryDetailActivtiy.class);
		intent.putParcelableArrayListExtra("memorys", memories);
		intent.putExtra("position", ((MemoryTimePresenter) mPresenter).getPosition(memoryId, memories));
		intent.putExtra("pageCount", pageCount);
		intent.putExtra("groupId", "");
		intent.putExtra("state", 1);
		intent.putExtra("type", 2);
		intent.putExtra("title", "我的记忆");
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
				ArrayList<Memory> mList = data.getParcelableArrayListExtra("mlist");
				//清空
				memories.clear();
				memories.addAll(((MemoryTimePresenter) mPresenter).convert(mList));

				mAdapter.clearEmpty();
				mAdapter.addAll(((MemoryTimePresenter) mPresenter).convert(memories, mAdapter.getItemCount()));

				if (!isCanLoad)
					showEmpty();

				swipeTarget.scrollToPosition(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void moveToPosition(int n) {
		//先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
		int firstItem = layoutManager.findFirstVisibleItemPosition();
		int lastItem = layoutManager.findLastVisibleItemPosition();
		//然后区分情况
		if (n <= firstItem) {
			//当要置顶的项在当前显示的第一个项的前面时
			swipeTarget.scrollToPosition(n);
		} else if (n <= lastItem) {
			//当要置顶的项已经在屏幕上显示时
			int top = swipeTarget.getChildAt(n - firstItem).getTop();
			swipeTarget.scrollBy(0, top);
		} else {
			//当要置顶的项在当前显示的最后一项的后面时
			swipeTarget.scrollToPosition(n);
		}

	}
}
