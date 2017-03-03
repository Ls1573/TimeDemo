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
import android.widget.ScrollView;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.Lable;
import com.time.memory.entity.Memory;
import com.time.memory.entity.Memorys;
import com.time.memory.gui.TagView;
import com.time.memory.gui.sticky.LayoutManager;
import com.time.memory.presenter.MemoryTagPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.memory.AddTagActivity;
import com.time.memory.view.activity.memory.MemoryDetailActivtiy;
import com.time.memory.view.adapter.TimeAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.impl.IMemoryTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆信息(标签)
 * @date 2016/9/26 13:18
 */
public class MemoryTagFragment extends BaseFragment implements IMemoryTagView, TagView.OnTagClickListener, AdapterCallback {
	public static final String TAG = "MemoryTagFragment";
	@Bind(R.id.add_tag)
	TagView tagView;
	@Bind(R.id.memory_tag_sl)
	ScrollView memorySl;
	@Bind(R.id.memory_tag_rl)
	RelativeLayout memory_tag_rl;
	@Bind(R.id.memory_sign_rl)
	RelativeLayout memory_sign_rl;
	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	@Bind(R.id.memory_tagall_tv)
	TextView memoryAll;//全部

	@Bind(R.id.memory_taginfo_tv)
	TextView memoryInfo;//描述

	private TimeAdapter mAdapter;
	private ArrayList<Memory> memories;

	private List<Lable> tags;//tag内容

	private int curPage = 1;//当前页
	private int pageCount = 5;//每页显示数
	private String groupId;//Id
	private String tagId;
	private boolean isLoad;//有无加载数据
	private boolean isCanLoad = true;
	private int position;
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
		View view = inflater.inflate(R.layout.fragment_memorytag, null);
		return view;
	}

	@Override
	public void onDestroy() {
		if (layoutManager != null)
		layoutManager.onDestory();
		if (memories != null) {
			memories.clear();
			memories = null;
		}
		if (tags != null) {
			tags.clear();
			tags = null;
		}
		if (mAdapter != null) {
			mAdapter.unRegisterCallBack();
			mAdapter = null;
		}
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryTagPresenter();
	}

	@Override
	public void initView(View view) {
		initLoading(view);
		fl_loading.withLoadedEmptyText("暂无记忆签").withEmptyIco(R.drawable.tag_title_).withbtnEmptyText("立即创建");
	}

	@Override
	public void initData() {
		tags = new ArrayList<>();
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
					((MemoryTagPresenter) mPresenter).getMessage(curPage, pageCount, tags.get(position).getLabelId(), groupId, mAdapter.getItemCount());
				}
			}
		});
	}

	@OnClick({R.id.memory_tagall_tv})
	public void onClick(View view) {
		if (view.getId() == R.id.memory_tagall_tv) {
			//切换为tag选择
			changeLayout(true);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (getUserVisibleHint() && !isLoad) {
			//没有加载数据-获取tag数据
			((MemoryTagPresenter) mPresenter).getMyTags();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		CLog.e(TAG, "setUserVisibleHint:" + isVisibleToUser);
		if (isVisibleToUser && !isLoad) {
			//没有加载数据-获取tag数据
			((MemoryTagPresenter) mPresenter).getMyTags();
		}
	}

	@Override
	public void onTagClick(int position) {
		this.position = position;
		//切换为List显示
		changeLayout(false);
		memoryInfo.setText(tags.get(position).getLabelName());
		tagId = tags.get(position).getLabelId();
		((MemoryTagPresenter) mPresenter).getMessage(curPage, pageCount, tagId, groupId, mAdapter.getItemCount());
	}

	@Override
	public void onIvClick(Integer position) {
		//点击删除图标
	}

	@Override
	public void onRemoveIndex(int index) {
		//tagView删除回调
	}

	@Override
	public void setTags(List<Lable> tags) {
		//获取tags数据成功
		this.tags.clear();
		this.tags.addAll(tags);
		tagView.setTags(tags, false);
		tagView.setOnTagClickListener(this);
		isLoad = true;
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
			}
		}
	}

	@Override
	public void showTags(boolean isSuccess) {
		hideLoadingDialog();
		if (isSuccess) {
			super.showSuccess();
			memorySl.setVisibility(View.VISIBLE);
		} else {
			//管理员
			fl_loading.btn_empty_ennable = true;
			super.showEmpty();
		}
	}


	@Override
	public void showSuccess() {
		hideLoadingDialog();
		isCanLoad = true;
		curPage++;
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
		isCanLoad = false;
	}

	@Override
	public void showEmpty() {
		hideLoadingDialog();
		isCanLoad = false;
		mAdapter.showEmpry();
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void onRetry() {
		Intent intent = new Intent(mContext, AddTagActivity.class);
		intent.putExtra("Id", groupId);
		intent.putExtra("state", 1);
		intent.putExtra("ismemory", true);
		startActivityForResult(intent, ReqConstant.ADDTAG);
	}

	/**
	 * 切换页间
	 *
	 * @param isTag-true:Tag-页面;false-List页面
	 */
	private void changeLayout(boolean isTag) {
		if (isTag) {
			memorySl.setVisibility(View.VISIBLE);
			memory_tag_rl.setVisibility(View.GONE);
			mAdapter.clearEmpty();
		} else {
			curPage = 1;
			memorySl.setVisibility(View.GONE);
			memory_tag_rl.setVisibility(View.VISIBLE);
		}
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
		intent.putExtra("position", ((MemoryTagPresenter) mPresenter).getPosition(memoryId, memories));
		intent.putExtra("pageCount", pageCount);
		intent.putExtra("state", 1);
		intent.putExtra("groupId", "");
		intent.putExtra("lableId", tagId);
		intent.putExtra("type", 3);
		intent.putExtra("title", "我的记忆");
		startActivityForResult(intent, ReqConstant.REQUEST_CODE_DETAILS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (resultCode == ReqConstant.ADDTAG) {
			//添加了标签
			((MemoryTagPresenter) mPresenter).getMyTags();
		} else if (resultCode == ReqConstant.REQUEST_CODE_DETAILS) {
			try {
				curPage = data.getIntExtra("curPage", curPage);
				isCanLoad = data.getBooleanExtra("isCanLoad", isCanLoad);
				//集合
				ArrayList<Memory> mList = data.getParcelableArrayListExtra("mlist");
				//清空
				memories.clear();
				memories.addAll(((MemoryTagPresenter) mPresenter).convert(mList));

//				mAdapter = null;
//				mAdapter = new TimeAdapter(mContext);
//				mAdapter.setCallBack(this);
//				LayoutManager layoutManager = new LayoutManager.Builder(mContext).addAdapter(mAdapter).build();
//				swipeTarget.setLayoutManager(layoutManager);
//				swipeTarget.setAdapter(mAdapter);

				mAdapter.clearEmpty();
				mAdapter.addAll(((MemoryTagPresenter) mPresenter).convert(memories, mAdapter.getItemCount()));


				if (!isCanLoad)
					showEmpty();
				swipeTarget.scrollToPosition(0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
