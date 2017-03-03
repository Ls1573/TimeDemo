package com.time.memory.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IHolderCallBack;
import com.time.memory.view.holder.BaseHolder;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:封装RecyclerAdapter_footView
 * @date 2016-9-12上午09:25:49
 * ==============================
 */
public class BaseRecyclerFootAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> implements IHolderCallBack {

	private List<T> mDatas;
	private T fDatas;
	private int mResLayout;//内容
	private int footLayout;//页脚
	private Class<? extends BaseHolder<T>> mClazz;
	private Class<? extends BaseHolder<T>> mClFoot;


	private static final int TYPE_ITEM = 0;  //普通Item View
	private static final int TYPE_FOOTER = 1;  //FootView

	private AdapterCallback mCallback;

	public void setCallBack(AdapterCallback callback) {
		this.mCallback = callback;
	}

	public void setmDatas(List<T> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	public void addAll(List<T> mDatas) {
		this.mDatas.addAll(mDatas);
		notifyDataSetChanged();
	}

	public void add(T data) {
		this.mDatas.add(data);
		notifyItemInserted(this.mDatas.size() - 1);
	}

	public void add(int index, T data) {
		this.mDatas.add(index, data);
		notifyItemInserted(index);
	}

	public void nofityFooter() {
		notifyItemChanged(mDatas.size());
	}

	public T getItem(int position) {
		try {
			return this.mDatas.get(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public BaseRecyclerFootAdapter(List<T> mDatas, int mResLayout, Class<? extends BaseHolder<T>> mClazz, T fDatas, int footLayout, Class<? extends BaseHolder<T>> mFoot) {
		if (mClazz == null) {
			throw new RuntimeException("类 ==null,检查下!");
		}
		if (mFoot == null) {
			throw new RuntimeException("类 ==null,检查下!");
		}
		if (mResLayout == 0) {
			throw new RuntimeException("资源文件==null,检查资源文件");
		}
		if (footLayout == 0) {
			throw new RuntimeException("资源文件==null,检查资源文件");
		}

		this.mDatas = mDatas;
		this.fDatas = fDatas;
		this.mResLayout = mResLayout;
		this.footLayout = footLayout;
		this.mClazz = mClazz;
		this.mClFoot = mFoot;
	}

	@Override
	public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
		//进行判断显示类型，来创建返回不同的View
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(mResLayout, parent, false);
			try {
				Constructor<? extends BaseHolder<T>> mClazzConstructor = mClazz
						.getConstructor(View.class);
				if (mClazzConstructor != null) {
					return mClazzConstructor.newInstance(view);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (viewType == TYPE_FOOTER) {
				//页脚
				View foot_view = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(footLayout, parent, false);
				try {
					Constructor<? extends BaseHolder<T>> mClazzConstructor = mClFoot
							.getConstructor(View.class);
					if (mClazzConstructor != null) {
						return mClazzConstructor.newInstance(foot_view);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return null;
	}

	@Override
	public void onBindViewHolder(final BaseHolder<T> holder, int position) {
		if (position + 1 == getItemCount()) {
			//页脚
//			holder.setData(fDatas.get(position));
			holder.setData(fDatas, position + 1, this);
		} else {
//			holder.setData(mDatas.get(position));
			holder.setData(mDatas.get(position), position, this);
		}
	}

	@Override
	public int getItemCount() {
		return mDatas.size() + 1;
	}

	/**
	 * 进行判断是普通Item视图还是FootView视图
	 *
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType(int position) {
		// 最后一个item设置为footerView
		if (position + 1 == getItemCount()) {
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;
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

	public void clearCallBack() {
		if (mCallback != null) {
			mCallback = null;
		}
	}
}
