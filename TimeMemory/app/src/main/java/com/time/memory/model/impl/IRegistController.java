package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.User;

/**
 * @author Qiu
 * @version V1.0
 * @Description:注册
 * @date 2016/9/23 13:39
 */
public interface IRegistController {

	void reqVerify(String url, String phone, SimpleCallback callback);

	void reqRegist(String url, String mobile, String verifyCode, String pwd, String nickName, SimpleCallback callback);

	void saveUser(User user);

}
