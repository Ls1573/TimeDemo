package com.time.memory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IHolderCallBack;
import com.time.memory.view.holder.BaseHolder;
import com.time.memory.view.holder.MemoryDCommentHolder;
import com.time.memory.view.holder.MemoryDForkHolder;
import com.time.memory.view.holder.MemoryDHeaderHolder;
import com.time.memory.view.holder.MemoryDNoCommHolder;
import com.time.memory.view.holder.MemoryDPointHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆详情Adapter
 * @date 2016/10/28 9:28
 */
public class MemoryDetailAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> implements IHolderCallBack {

	private static final String TAG = "MemoryDetailAdapter";

	private static final int TYPE_Header = 0;  //顶部HeaderView
	private static final int TYPE_ITEM = 1;  //片段Item View
	private static final int TYPE_FORK = 2;  //追加+点赞 View
	private static final int TYPE_COMMENT = 3;  //评论 View
	private static final int TYPE_NOCOMMENT = 4;  //无评论 View

	private T memory;//头数据
	private T fork;//点赞数据
	private ArrayList<T> memoryPointVos;//片段
	private List<T> commentVos;//评论

	private Context mContext;

	private AdapterCallback mCallback;

	public void setCallBack(AdapterCallback callback) {
		this.mCallback = callback;
	}

	public MemoryDetailAdapter(T memory, T fork, ArrayList<T> memoryPointVos, List<T> commentVos, Context context) {
		this.mContext = context;
		this.memory = memory;
		this.fork = fork;
		this.memoryPointVos = memoryPointVos;
		this.commentVos = commentVos;
	}

	/**
	 * 刷新片段
	 */
	public void notifyPoints(int position) {
		notifyItemChanged(1 + position);
	}

	/**
	 * 刷新点赞
	 */
	public void notifyPraises() {
		notifyItemChanged(memoryPointVos.size() + 1);
	}

	@Override
	public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//进行判断显示类型，来创建返回不同的View
		if (viewType == TYPE_Header) {
			//页头
			View headerView = LayoutInflater.from(mContext).inflate(R.layout.item_memory_head_view, parent, false);
			return new MemoryDHeaderHolder(headerView);
		} else if (viewType == TYPE_ITEM) {
			//片段
			View pointView = LayoutInflater.from(mContext).inflate(R.layout.item_memory_details, parent, false);
			return new MemoryDPointHolder(pointView);
		} else if (viewType == TYPE_FORK) {
			//追加&&点赞
			View forkView = LayoutInflater.from(mContext).inflate(R.layout.item_memory_fork, parent, false);
			return new MemoryDForkHolder(forkView);
		} else if (viewType == TYPE_COMMENT) {
			//评论
			View commentView = LayoutInflater.from(mContext).inflate(R.layout.item_memory_comment, parent, false);
			return new MemoryDCommentHolder(commentView);
		} else {
			//无评论数据
			View nocommentView = LayoutInflater.from(mContext).inflate(R.layout.item_memory_nocomment, parent, false);
			return new MemoryDNoCommHolder(nocommentView);
		}
	}

	@Override
	public void onBindViewHolder(final BaseHolder<T> holder, int position) {
		if (position == 0) {
			//页头数据
			holder.setData(memory, position, this);
		} else if (position > 0 && position <= memoryPointVos.size()) {
			//片段
			holder.setData(memoryPointVos.get(position - 1), position - 1, this);
		} else if (position == memoryPointVos.size() + 1) {
			//追加&点赞
			holder.setData(fork, position, this);
		} else {
			if (commentVos == null || commentVos.isEmpty()) {
				//无评论数据
				holder.setData(null);
			} else {
				//评论数据
				int index = position - memoryPointVos.size() - 2;
				holder.setData(commentVos.get(index), index, this);
			}
		}
	}

	@Override
	public int getItemCount() {
		int commentCount = commentVos.size() == 0 ? 1 : commentVos.size();
		int size = memoryPointVos.size() + commentCount + 2;
		return size;
	}

	/**
	 * 断视图
	 *
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_Header;
		} else if (position > 0 && position <= memoryPointVos.size()) {
			//片段
			return TYPE_ITEM;
		} else if (position == memoryPointVos.size() + 1) {
			//追加&点赞
			return TYPE_FORK;
		} else {
			if (commentVos == null || commentVos.isEmpty()) {
				//无评论数据
				return TYPE_NOCOMMENT;
			}
			//评论的数据了
			return TYPE_COMMENT;
		}
	}

	@Override
	public void onClick(int position) {
		if (mCallback != null)
			mCallback.onCallback(position);
	}

	@Override
	public void onLongClick(int position) {
		if (mCallback != null)
			mCallback.onLongCallBack(null,position);
	}

	@Override
	public void onClick(Object obj) {
		if (mCallback != null)
			mCallback.onCallback(obj);
	}

	@Override
	public void onClick(int position, Object obj) {
		if (mCallback != null)
			mCallback.onDataCallBack(obj, position);
	}

	@Override
	public void onClick(int position, int index, Object obj) {
		if (mCallback != null)
			mCallback.onDataCallBack(obj, position, index);
	}

	@Override
	public void onClick(int position, int index, Object obj, int tag) {
		if (mCallback != null)
			mCallback.onDataCallBack(obj, position, index, tag);
	}
}
