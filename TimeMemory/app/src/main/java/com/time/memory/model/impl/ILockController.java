package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.3
 * @Description:
 * @date 2017/1/12 10:13
 */
public interface ILockController {
	void reqPattern(String url, String groupId, String groupPw, SimpleCallback callback);
}
