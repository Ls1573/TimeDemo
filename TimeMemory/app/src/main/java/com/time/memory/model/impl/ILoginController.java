package com.time.memory.model.impl;

import android.content.Context;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.User;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0 ==============================
 * @Description:登录
 * @date 2016-9-5下午4:52:08
 */
public interface ILoginController {

	void reqVerify(String methodName, String phone, SimpleCallback callback);

	void reqLogin(String url, String phone, String pwd,String appVersion, SimpleCallback callback);

	void reqLoginIm(Context context, String Ip, String port, String userId, String pwd, SimpleCallback callback);

	void saveUser(User user);

	void updateUser(User user);

	void updateUser(String key);

	void upDateUser(String url, String pic, String nickName, String email, String company, String sex, String address,String addresss1, String sign, String profression, SimpleCallback callback);

	void updateUser(String key, String obj, String data);

	void reqUser(SimpleCallback callback);

	User getUser(String key);

}
