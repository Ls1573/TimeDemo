package com.time.memory.view.impl;

import com.time.memory.entity.User;

/**
 * V-P
 */
public interface IUpPhoneView extends IBaseView {

	void sendVerify();

	void upSuccess(String phone);

	void setUser(User user);

	void closeActivity();
}
