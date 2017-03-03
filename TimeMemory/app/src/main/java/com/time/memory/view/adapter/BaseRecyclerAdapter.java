package com.time.memory.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IHolderCallBack;
import com.time.memory.view.holder.BaseHolder;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:封装RecyclerAdapter
 * @date 2016-9-6上午11:05:49
 * ==============================
 */
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> implements IHolderCallBack {
	private static final String TAG = "BaseRecyclerAdapter";
	private List<T> mDatas;
	private int mResLayout;
	private Class<? extends BaseHolder<T>> mClazz;
	private AdapterCallback mCallback;

	public void setmDatas(List<T> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	public void addAll(List<T> mDatas) {
		this.mDatas.addAll(mDatas);
		notifyItemRangeInserted(this.mDatas.size() - mDatas.size(),
				this.mDatas.size());
	}

	public void add(T data) {
		this.mDatas.add(data);
		notifyItemInserted(this.mDatas.size() - 1);
	}

	public void add(int index, T data) {
		this.mDatas.add(index, data);
		notifyItemInserted(index);
	}

	public void remove(int position) {
		mDatas.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, mDatas.size() - position);
//		notifyItemRangeChanged(0, mDatas.size());
	}

	public void setCallBack(AdapterCallback callback) {
		this.mCallback = callback;
	}

	public T getItem(int position) {
		try {
			return this.mDatas.get(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public BaseRecyclerAdapter(List<T> mDatas, int mResLayout,
							   Class<? extends BaseHolder<T>> mClazz) {
		if (mClazz == null) {
			throw new RuntimeException("类 ==null,检查下!");
		}
		if (mResLayout == 0) {
			throw new RuntimeException("资源文件==null,检查资源文件");
		}
		this.mDatas = mDatas;
		this.mResLayout = mResLayout;
		this.mClazz = mClazz;
	}


	public HashMap<Integer, Object> tags = new HashMap<>();

	public BaseRecyclerAdapter setTag(int tag, Object mObject) {
		if (mObject != null) {
			tags.put(tag, mObject);
		}
		return this;
	}

	@Override
	public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(
				mResLayout, parent, false);
		if (tags != null && tags.size() > 0) {
			for (int tag : tags.keySet()) {
				view.setTag(tag, tags.get(tag));
			}
		}
		try {
			Constructor<? extends BaseHolder<T>> mClazzConstructor = mClazz
					.getConstructor(View.class);
			if (mClazzConstructor != null) {
				return mClazzConstructor.newInstance(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onBindViewHolder(final BaseHolder<T> holder, int position) {
		holder.setData(mDatas.get(position), position, this);
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}


	@Override
	public void onClick(int position) {
		if (mCallback != null)
			mCallback.onCallback(position);
	}

	@Override
	public void onLongClick(int position) {
		if (mCallback != null)
			mCallback.onLongCallBack(null, position);
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
