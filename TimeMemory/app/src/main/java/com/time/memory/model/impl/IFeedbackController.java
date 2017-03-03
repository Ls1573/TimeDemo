package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/14 16:23
 */
public interface IFeedbackController {
	void reqFeedback(String url, String msg, SimpleCallback call);
}
