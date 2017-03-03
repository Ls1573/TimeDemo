package com.time.memory.model.impl;

import com.time.memory.entity.MyPush;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0 ==============================
 * @Description:登录
 * @date 2016-9-5下午4:52:08
 */
public interface IPushController {

	void savePush(MyPush push);

	void updatePush(MyPush myPush);

	MyPush getPushByKey(String key);

}
