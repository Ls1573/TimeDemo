package com.time.memory.view.impl;

import com.time.memory.entity.User;

/**
 * V-P
 */
public interface IVeriftyEmailView extends IBaseView {

	void sendVerify();

	void setUser(User user);
}
