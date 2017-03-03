package com.time.memory.view.impl;

import com.time.memory.entity.User;

/**
 * V-P
 */
public interface IMemoryPView extends IBaseView {

	void setUser(User user);

	void removeMemory(int position, int state);
}
