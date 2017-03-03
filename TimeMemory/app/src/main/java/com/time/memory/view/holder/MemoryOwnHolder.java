package com.time.memory.view.holder;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.util.CLog;
import com.time.memory.view.adapter.MemoryDetailAdapter;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:我的记忆详情填充
 * @date 2016/11/18 8:37
 */
public class MemoryOwnHolder extends BaseHolder<MemoryInfo> implements AdapterCallback {
	private static final String TAG = "MemoryOwnHolder";
	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private MemoryDetailAdapter memoryDetailAdapter;//记忆
	private MemoryInfo memoryInfo;
	private int mPosition;

	public MemoryOwnHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		super.init();
		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
		//去闪屏
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
	}

	@Override
	public void setData(MemoryInfo mData, int positoin) {
		super.setData(mData, positoin);
		this.mPosition = positoin;
		this.memoryInfo = mData;
		setAdapter(memoryInfo);
	}

	public void setAdapter(MemoryInfo entity) {
		if (entity == null) return;
		if (swipeTarget != null) {
			//准备好了
			if (memoryInfo.isRight()) {
				//记忆片段
				memoryDetailAdapter = new MemoryDetailAdapter(memoryInfo.getMemory(), memoryInfo, memoryInfo.getMemoryPointVos(), memoryInfo.getCommentorVos(), mContext);
				memoryDetailAdapter.setCallBack(this);
				swipeTarget.setAdapter(memoryDetailAdapter);
				mView.setVisibility(View.VISIBLE);
			}
			//设置显示状态
			if (memoryInfo.getMemory() == null) {
				//无数据
				mView.setVisibility(View.GONE);
			} else {
				mView.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void notifyItem(int position) {
		memoryDetailAdapter.notifyItemChanged(position);
	}

	//增加一条
	@Override
	public void notifyAddItem(int position, int count) {
		memoryDetailAdapter.notifyItemChanged(position);
		memoryDetailAdapter.notifyItemChanged(position + 1);
	}

	//刷新
	@Override
	public void notifyAllItem(int position) {
		CLog.e(TAG, "notifyAllItem---------------------------------->");
		if (memoryDetailAdapter != null)
			memoryDetailAdapter.notifyDataSetChanged();
	}

	/**
	 * 0:点赞
	 * 1:片段
	 * 2:点赞人
	 *
	 * @param data
	 * @param position
	 */
	@Override
	public void onDataCallBack(Object data, int position) {
		mHolderCallBack.onClick(position, -1, data, mPosition);
	}

	/**
	 * position
	 * 4:评论的更多
	 * 5:观看图片
	 * 6:评论人头像
	 * 8:片段图片->跳转到片段
	 * 9:点击详情->回复
	 *
	 * @param data
	 * @param position
	 */
	@Override
	public void onDataCallBack(Object data, int position, int index) {
//		mHolderCallBack.onClick(position, index, data);
		mHolderCallBack.onClick(position, index, data, mPosition);
	}

	/**
	 * 7:追加记忆
	 *
	 * @param data
	 */
	@Override
	public void onCallback(Object data) {
		mHolderCallBack.onClick(data);
		mHolderCallBack.onClick(-1, -1, data, mPosition);
	}

	@Override
	public void onDataCallBack(Object data, int position, int index, int tag) {
	}

	@Override
	public void onLongCallBack(Object data, int position) {
	}

}
