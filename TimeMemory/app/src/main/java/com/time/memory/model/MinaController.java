package com.time.memory.model;

import android.content.Context;

import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.im.android.core.LocalUDPDataSender;
import com.time.memory.entity.User;
import com.time.memory.entity.UserDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMinaController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;


/**
 * @author Qiu
 * @version V1.0
 * @Description:与后天交互(Mina)
 * @date 2016/9/28 10:19
 */
public class MinaController extends BaseController implements IMinaController {

	@Override
	public void sendMsg(Context context, Object obj, boolean isSync, final SimpleCallback callback) {
		String json = JsonUtils.toJson(obj);
		CLog.e(TAG, "json:" + json);
		// 发送消息（Android系统要求必须要在独立的线程中发送）
		new LocalUDPDataSender.SendCommonDataAsync(context, json, "0", isSync) {
			@Override
			protected void onPostExecute(Integer code) {
				onCallback(callback, code);
			}
		}.execute();
	}

	@Override
	public void getUser(String key, SimpleCallback callBack) {
		//获取信息
		UserDao userDao = MainApplication.getDaoSession().getUserDao();
		User user = userDao.load(key);
		onCallback(callBack, user);
	}

}
