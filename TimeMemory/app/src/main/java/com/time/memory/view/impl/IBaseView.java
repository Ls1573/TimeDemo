package com.time.memory.view.impl;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0 ==============================
 * @Description:为view和presenter提供接口服务,解耦
 * @date 2016-9-5上午9:56:07
 */
public interface IBaseView {

	void showShortToast(String message);

	void onRefresh();

	void showSuccess();

	void showEmpty();

	boolean checkNet();

	void showFaild();

	void showNoNet();

	void showLoadingDialog();
}
