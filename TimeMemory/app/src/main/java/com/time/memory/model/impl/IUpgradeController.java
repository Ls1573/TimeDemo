package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/28 9:52
 */
public interface IUpgradeController {
	void reqUpgrade(final String url, final SimpleCallback callback);
}
