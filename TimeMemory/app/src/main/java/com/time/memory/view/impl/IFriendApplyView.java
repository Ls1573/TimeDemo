package com.time.memory.view.impl;

import com.time.memory.entity.User;

/**
 * V-P
 */
public interface IFriendApplyView extends IBaseView {

	void refuse();

	void reportSuccess();

	void setMessage(User user);
}
