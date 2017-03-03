package com.time.memory.view.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.time.memory.core.callback.IHolderCallBack;
import com.time.memory.util.CLog;

import butterknife.ButterKnife;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:RecyclerView的ViewHodler封装
 * @date 2016-9-6上午11:06:58
 * ==============================
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
	private static final String TAG = "BaseHolder";
	protected IHolderCallBack mHolderCallBack;
	protected View mView;
	protected T mData;
	protected Context mContext;

	public View getView() {
		return mView;
	}

	public BaseHolder(View view) {
		super(view);
		this.mView = view;
		mContext = mView.getContext().getApplicationContext();
		ButterKnife.bind(this, mView);
		init();
	}

	public void init() {
	}

	public void setData(T mData) {
		this.mData = mData;
	}

	public void setData(T mData, int positoin) {
		this.setData(mData);
	}

	public void setData(T mData, int positoin, IHolderCallBack holderCallBack) {
		this.setData(mData, positoin);
		this.mHolderCallBack = holderCallBack;
	}

	public void notifyItem(int position) {
	}

	public void notifyAddItem(int position, int count) {
	}

	public void notifyAllItem(int position) {
		CLog.e(TAG, "position:" + position);
	}

}
