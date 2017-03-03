/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * ChatBaseEventImpl.java at 2016-2-20 11:20:18, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im.event;

import android.util.Log;

import com.time.memory.core.im.android.event.ChatBaseEvent;
import com.time.memory.util.CLog;

import java.util.Observer;

public class ChatBaseEventImpl implements ChatBaseEvent {
	private final static String TAG = "ChatBaseEventImpl";


	// 本Observer目前仅用于登陆时（因为登陆与收到服务端的登陆验证结果
	// 是异步的，所以有此观察者来完成收到验证后的处理）
	private Observer loginOkForLaunchObserver = null;

	@Override
	public void onLoginMessage(String dwUserId, int dwErrorCode) {
		if (dwErrorCode == 0) {
			CLog.e(TAG, "登录成功,当前分配的user_id=" + dwUserId);
		} else {
			Log.e(TAG, "【DEBUG_UI】登录失败，错误代码：" + dwErrorCode);
		}
		// 此观察者只有开启程序首次使用登陆界面时有用
		if (loginOkForLaunchObserver != null) {
			loginOkForLaunchObserver.update(null, dwErrorCode);
			loginOkForLaunchObserver = null;
		}
	}

	@Override
	public void onLinkCloseMessage(int dwErrorCode) {
		Log.e(TAG, "【DEBUG_UI】网络连接出错关闭了，error：" + dwErrorCode);
	}

	public void setLoginOkForLaunchObserver(Observer loginOkForLaunchObserver) {
		this.loginOkForLaunchObserver = loginOkForLaunchObserver;
	}

}
