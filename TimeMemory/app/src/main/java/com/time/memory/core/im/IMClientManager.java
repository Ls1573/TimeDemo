/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * IMClientManager.java at 2016-2-20 11:20:18, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im;

import android.content.Context;

import com.time.memory.core.im.event.ChatBaseEventImpl;
import com.time.memory.core.im.event.ChatTransDataEventImpl;
import com.time.memory.core.im.event.MessageQoSEventImpl;

import com.time.memory.core.im.android.ClientCoreSDK;

public class IMClientManager {
	private static String TAG = IMClientManager.class.getSimpleName();

	private static IMClientManager instance = null;

	/**
	 * MobileIMSDK是否已被初始化. true表示已初化完成，否则未初始化.
	 */
	private boolean init = false;

	// 
	private ChatBaseEventImpl baseEventListener = null;
	//
	private ChatTransDataEventImpl transDataListener = null;
	//
	private MessageQoSEventImpl messageQoSListener = null;

	private Context context = null;

	public static IMClientManager getInstance(Context context) {
		if (instance == null)
			instance = new IMClientManager(context);
		return instance;
	}

	private IMClientManager(Context context) {
		this.context = context;
		initMobileIMSDK();
	}

	public void initMobileIMSDK() {
		if (!init) {
			// 设置AppKey
//			ConfigEntity.appKey = "000000123";
			// MobileIMSDK核心IM框架的敏感度模式设置
//			ConfigEntity.setSenseMode(SenseMode.MODE_10S);
			// 开启/关闭DEBUG信息输出
			ClientCoreSDK.DEBUG = false;
			// 【特别注意】请确保首先进行核心库的初始化（这是不同于iOS和Java端的地方)
			ClientCoreSDK.getInstance().init(this.context);
			// 设置事件回调
			baseEventListener = new ChatBaseEventImpl();
			transDataListener = new ChatTransDataEventImpl();
			messageQoSListener = new MessageQoSEventImpl();
			ClientCoreSDK.getInstance().setChatBaseEvent(baseEventListener);
			ClientCoreSDK.getInstance().setChatTransDataEvent(transDataListener);
			ClientCoreSDK.getInstance().setMessageQoSEvent(messageQoSListener);
			init = true;
		}
	}

	public void release() {
		ClientCoreSDK.getInstance().release();
	}

	public void loginOut() {

	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public ChatTransDataEventImpl getTransDataListener() {
		return transDataListener;
	}

	public ChatBaseEventImpl getBaseEventListener() {
		return baseEventListener;
	}

	public MessageQoSEventImpl getMessageQoSListener() {
		return messageQoSListener;
	}
}
