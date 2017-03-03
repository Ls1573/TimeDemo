package com.time.memory.model.impl;

import android.content.Context;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/9/28 10:17
 */
public interface IMinaController {
	void sendMsg(Context context, Object obj, boolean isSync, SimpleCallback callBack);

	void getUser(String key, SimpleCallback callBack);


}
