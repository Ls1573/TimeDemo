package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:更换手机号
 * @date 2016/10/8 15:36
 */
public interface IUpPhoneController {

	void reqEmail(String url, String mobile, String verifyCode, SimpleCallback callback);

	void reqVerify(String url, String emial, SimpleCallback callback);

	void reqEmailVerify(String url, String emial, SimpleCallback callback);

	void reqVerifyEmail(String url, String emialCode, SimpleCallback callback);
}