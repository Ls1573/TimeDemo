package com.time.memory.view.impl;

/**
 * V-P
 */
public interface IBindEmailView extends IBaseView {

	void sendVerify();

	void bindSuccess(boolean isSuccess, String email);
}
