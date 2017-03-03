package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:更换密码
 * @date 2016/10/8 15:36
 */
public interface IUpPwdController {

	void reqUpPwd(String url, String mobile, String verifyCode, String pwd, SimpleCallback callback);

}
