package com.time.memory.view.fragment.memory.group;


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
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.GroupMemorys;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.sticky.LayoutManager;
import com.time.memory.presenter.MemoryGroupTimePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.memory.MemoryDetailActivtiy;
import com.time.memory.view.adapter.GroupTimeAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.impl.IMemoryGroupTimeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆信息(时间)
 * @date 2016/9/19 13:36
 */
public class MemoryGroupTimeFragment extends BaseFragment implements IMemoryGroupTimeView, AdapterCallback {
	public static final String TAG = "MemoryMonthFragment";

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;
	@Bind(R.id.memory_sign_rl)
	RelativeLayout memory_sign_rl;

	private GroupTimeAdapter mAdapter;
	private ArrayList<GroupMemory> groupMemories;

	private int curPage = 1;//当前页
	private int pageCount = 5;//每页显示数
	private int type;//显示内容类型
	private String groupId;//Id
	private String title;
	private boolean isLoad;//加载
	private boolean isCanLoad = true;
	private LayoutManager layoutManager;


	private final int REQUEST_CODE_GALLERY = 101;
	private List<PhotoInfo> mPhotoList;//图片集合

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		type = getArguments().getInt("type");
		title = getArguments().getString("title");
		groupId = getArguments().getString("groupId");
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
		if (groupMemories != null) {
			groupMemories.clear();
			groupMemories = null;
		}
		if (mAdapter != null) {
			mAdapter.unRegisterCallBack();
			mAdapter = null;
		}
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryGroupTimePresenter();
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData() {
		mPhotoList = new ArrayList<>();
		groupMemories = new ArrayList<>();

		mAdapter = new GroupTimeAdapter(mContext);
		mAdapter.setCallBack(this);
		layoutManager = new LayoutManager.Builder(mContext).addAdapter(mAdapter).build();
		swipeTarget.setLayoutManager(layoutManager);
		swipeTarget.setAdapter(mAdapter);
		//滚动监听
		addScrollerListener();
		//去闪屏
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
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
					((MemoryGroupTimePresenter) mPresenter).getMessage(curPage, pageCount, "", groupId, mAdapter.getItemCount());
				}
			}
		});
	}

	@Override
	public void setAdapter(List<GroupMemorys> list, ArrayList<GroupMemory> groupMemories) {
		//设置Adapter
		if (swipeTarget != null) {
			isLoad = true;
			if (list != null && !list.isEmpty()) {
				this.groupMemories.addAll(groupMemories);
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
			((MemoryGroupTimePresenter) mPresenter).getMessage(curPage, pageCount, "", groupId, mAdapter.getItemCount());
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
		intent.putParcelableArrayListExtra("groupList", groupMemories);
		intent.putExtra("position", ((MemoryGroupTimePresenter) mPresenter).getPosition(memoryId, groupMemories));
		intent.putExtra("pageCount", pageCount);
		intent.putExtra("groupId", groupId);
		intent.putExtra("state", 2);
		intent.putExtra("title", title);
		intent.putExtra("type", 1);
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
				ArrayList<GroupMemory> gList = data.getParcelableArrayListExtra("glist");

				groupMemories.clear();
				groupMemories.addAll(((MemoryGroupTimePresenter) mPresenter).convert(gList));


				mAdapter.clearEmpty();
				mAdapter.addAll(((MemoryGroupTimePresenter) mPresenter).convert(groupMemories, mAdapter.getItemCount()));

				if (!isCanLoad)
					showEmpty();

				swipeTarget.scrollToPosition(0);

			} catch (Exception e) {
			}
		}
	}

}
