package com.time.memory.core.callback;

/**
 * 加载状态
 */
public interface OnLoadingListener {

	void showSuccess();

	void showEmpty();

	boolean checkNet();

	void showFaild();

	void showLoading();

	void showNoNet();
}
