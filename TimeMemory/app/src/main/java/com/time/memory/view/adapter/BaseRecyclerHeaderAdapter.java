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
 * @author @Qiu
 * @version V1.0
 * @Description:封装RecyclerAdapter_headerView
 * @date 2016/9/20 10:54
 */
public class BaseRecyclerHeaderAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> implements IHolderCallBack {

	private static final String TAG = "BaseRecyclerHeaderAdapter";
	protected List<T> mDatas;
	protected T HDates;
	private int mResLayout;//内容
	private int headerLayout;//页头
	private Class<? extends BaseHolder<T>> mClazz;
	private Class<? extends BaseHolder<T>> mCHeader;
	private boolean isHeaderVisable;//控制头部显示

	private static final int TYPE_ITEM = 0;  //普通Item View
	private static final int TYPE_Header = 1;  //顶部HeaderView

	private AdapterCallback mCallback;

	public void setCallBack(AdapterCallback callback) {
		this.mCallback = callback;
	}

	public void setmDatas(List<T> mDatas) {
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	public void setHeaderVisable(boolean visable) {
		this.isHeaderVisable = visable;
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

	public void notifiyItem(int position) {
		int index = isHeaderVisable ? position + 1 : position;
		notifyItemChanged(index);
	}

	public void notifiyHeader() {
		notifyItemChanged(0);
	}


	public T getItem(int position) {
		try {
			return this.mDatas.get(position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public BaseRecyclerHeaderAdapter(List<T> mDatas, int mResLayout, Class<? extends BaseHolder<T>> mClazz, T HDates, int headerLayout, Class<? extends BaseHolder<T>> mHeader) {
		if (mClazz == null) {
			throw new RuntimeException("类 ==null,检查下!");
		}
		if (mHeader == null) {
			throw new RuntimeException("类 ==null,检查下!");
		}
		if (mResLayout == 0) {
			throw new RuntimeException("资源文件==null,检查资源文件");
		}
		if (headerLayout == 0) {
			throw new RuntimeException("资源文件==null,检查资源文件");
		}
		this.mDatas = mDatas;
		this.HDates = HDates;
		this.mResLayout = mResLayout;
		this.headerLayout = headerLayout;
		this.mClazz = mClazz;
		this.mCHeader = mHeader;
	}

	@Override
	public BaseHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
		//进行判断显示类型，来创建返回不同的View
		if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(mResLayout, parent, false);
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
			if (viewType == TYPE_Header) {
				//页头
				View headerView = LayoutInflater.from(parent.getContext()).inflate(headerLayout, parent, false);
				try {
					Constructor<? extends BaseHolder<T>> mClazzConstructor = mCHeader
							.getConstructor(View.class);
					if (mClazzConstructor != null) {
						return mClazzConstructor.newInstance(headerView);
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
		int index = isHeaderVisable ? position - 1 : position;
		if (position == 0) {
			//页头数据
			holder.setData(HDates, 0, this);
		} else {
			holder.setData(mDatas.get(index), index, this);
		}

	}

	@Override
	public int getItemCount() {
		int size = isHeaderVisable ? mDatas.size() + 1 : mDatas.size();
		return size;
	}

	/**
	 * 进行判断是普通Item视图还是HeaderView视图
	 *
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_Header;
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
}
