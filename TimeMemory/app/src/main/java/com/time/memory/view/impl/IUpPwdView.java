package com.time.memory.view.impl;

import com.time.memory.entity.User;

/**
 * V-P
 */
public interface IUpPwdView extends IBaseView {

	void sendVerify();

	void upSuccess();

	void setUser(User user);
}
