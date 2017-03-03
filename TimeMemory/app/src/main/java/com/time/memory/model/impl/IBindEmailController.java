package com.time.memory.model.impl;


import com.time.memory.core.callback.SimpleCallback;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:绑定邮箱
 * @date 2016/11/24 11:43
 */
public interface IBindEmailController {

	void reqBind(String url,String email, String verifyCode, SimpleCallback callback);

}
