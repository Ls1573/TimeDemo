package com.time.memory.view.fragment.memory.own;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.Footer;
import com.time.memory.entity.Memory;
import com.time.memory.entity.TempMemory;
import com.time.memory.presenter.MemoryDayPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.memory.MemoryDetailActivtiy;
import com.time.memory.view.adapter.BaseRecyclerFootAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.holder.CommonFooterHolder;
import com.time.memory.view.holder.MemoryDayHolder;
import com.time.memory.view.impl.IMemoryDayView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:记忆信息(天 )
 * @date 2016-9-9下午3:01:46
 * ==============================
 */
public class MemoryHotFragment extends BaseFragment implements IMemoryDayView, AdapterCallback {
	public static final String TAG = "MemoryDayFragment";

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private int curPage = 1;//当前页
	private int pageCount = 5;//每页显示数
	private int lastVisibleItem;//最后一项
	private boolean isCanLoad = true;
	private boolean isFirst = true;
	private ArrayList<Memory> list;//记忆内容
	private Footer mFooter;//脚数据
	private BaseRecyclerFootAdapter adapter;
	private LinearLayoutManager linearLayoutManager;
	private final int REQUEST_CODE_GALLERY = 101;

	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memoryinfo, null);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		//获取
		TempMemory memory = getActivity().getIntent().getParcelableExtra("tempMemory");
		if (!isFirst)
			((MemoryDayPresenter) mPresenter).addMemoryFromTemp(memory);
		getActivity().getIntent().removeExtra("tempMemory");
	}

	@Override
	public void onDestroy() {
		if (list != null) {
			list.clear();
			list = null;
		}
		if (adapter != null) {
			adapter.clearCallBack();
			adapter = null;
		}
		super.onDestroy();
	}


	@Override
	public BasePresenter initPresenter() {
		return new MemoryDayPresenter();
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public void initData() {
		list = new ArrayList<>();

		mFooter = new Footer();
		linearLayoutManager = new LinearLayoutManager(mContext);
		//线性
		swipeTarget.setLayoutManager(linearLayoutManager);
		//去闪屏
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
		//滚动监听
		addScrollerListener();
		//获取信息(根据类型选择)
		((MemoryDayPresenter) mPresenter).getMessage(MainApplication.getUserId(), curPage, pageCount);
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
				if (newState == RecyclerView.SCROLL_STATE_IDLE && adapter != null) {
					if (lastVisibleItem + 1 == adapter.getItemCount() && isCanLoad) {
						//滑到底部,加载
						((MemoryDayPresenter) mPresenter).getMessage("", curPage, pageCount);
					}
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
			}
		});
	}

	@Override
	public void setAdapter(ArrayList<Memory> mList) {
		//设置Adapter
		if (swipeTarget != null) {
			isFirst = false;
			if (adapter == null) {
				this.list = mList;
				adapter = new BaseRecyclerFootAdapter(list, R.layout.item_mymemory_day, MemoryDayHolder.class,
						mFooter, R.layout.recycler_load_more_layout, CommonFooterHolder.class);
				adapter.setCallBack(this);
				swipeTarget.setAdapter(adapter);
			} else {
				adapter.addAll(mList);
			}
		}
	}

	@Override
	public void addAdapter(Memory memory) {
		if (list == null) {
			list = new ArrayList<>();
		}
		list.add(0, memory);
		if (adapter == null) {
			adapter = new BaseRecyclerFootAdapter(list, R.layout.item_mymemory_day, MemoryDayHolder.class,
					mFooter, R.layout.recycler_load_more_layout, CommonFooterHolder.class);
			adapter.setCallBack(this);
			swipeTarget.setAdapter(adapter);
		} else {
			adapter.notifyItemRangeChanged(0, list.size());
		}
		getActivity().setIntent(new Intent());
	}

	/**
	 * 刷新页面
	 */
	@Override
	public void refreshAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		curPage++;
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void showEmpty() {
		isCanLoad = false;
		mFooter.setType(2);
		if (adapter != null)
			adapter.nofityFooter();

	}

	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		adapter.notifyItemChanged(position);
		Intent intent = new Intent(mContext, MemoryDetailActivtiy.class);
		intent.putParcelableArrayListExtra("memorys", list);
		intent.putExtra("position", position);
		intent.putExtra("groupId", "");
		intent.putExtra("pageCount", pageCount);
		intent.putExtra("state", 1);
		intent.putExtra("title", "我的记忆");
		intent.putExtra("type", 1);
		intent.putExtra("isOwen", true);
		startActivityForResult(intent, ReqConstant.REQUEST_CODE_DETAILS);
	}

	@Override
	public void onDataCallBack(Object data, int position) {
		CLog.e(TAG, "position:" + position);

	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (resultCode == ReqConstant.REQUEST_CODE_DETAILS) {
			try {
				curPage = data.getIntExtra("curPage", curPage);
				isCanLoad = data.getBooleanExtra("isCanLoad", isCanLoad);
				ArrayList<Memory> mList = data.getParcelableArrayListExtra("mlist");
				list.clear();
				list.addAll(((MemoryDayPresenter) mPresenter).convert(mList));
				adapter.notifyDataSetChanged();
				if (!isCanLoad)
					showEmpty();
			} catch (Exception e) {
			}
		}
	}
}
