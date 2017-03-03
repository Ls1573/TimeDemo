package com.time.memory.presenter.base;

import android.content.Context;

import com.time.memory.view.impl.IBaseView;


/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0
 * @Package com.qiu.frame.presenter
 * @Description:presenter-层基类
 * @date 2016-7-25 下午3:46:56
 * ==============================
 */
public abstract class BasePresenter<T extends IBaseView> {

	public T mView;
	public Context context;

	/**
	 * @param
	 * @Description: 激活view
	 */
	public void attach(T mView, Context context) {
		this.mView = mView;
		this.context = context;
	}

	/**
	 * @param
	 * @Description: 销毁持有的View
	 */
	public void detachView() {
		if (mView != null) {
			mView = null;
		}
	}
}
