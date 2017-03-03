package com.time.memory.model.impl;


import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.User;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:我的信息
 * @date 2016/10/14 16:47
 */
public interface IMineController {

	void reqUpUser(String url, User user, SimpleCallback callback);
}
