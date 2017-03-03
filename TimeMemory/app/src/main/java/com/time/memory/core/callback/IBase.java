package com.time.memory.core.callback;

import com.time.memory.presenter.base.BasePresenter;

public interface IBase {
	/**
	 * 创建视图，统一处理
	 */
	void onCreateMyView();

	/**
	 * 初始化视图
	 */
	void initView();

	/**
	 * 初始化数据
	 */
	void initData();

	/**
	 * )
	 */
	BasePresenter initPresenter();

}
