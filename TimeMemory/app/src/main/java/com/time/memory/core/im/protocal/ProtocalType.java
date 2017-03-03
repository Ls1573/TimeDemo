/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * ProtocalType.java at 2016-2-20 11:06:06, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im.protocal;

/**
 * 协议类型.
 * 
 * @author Jack Jiang, 2013-10-07
 * @version 1.0
 */
public interface ProtocalType
{
	//------------------------------------------------------- from client
	public interface C
	{
		/** 由客户端发出 - 协议类型：客户端登陆 */
		int FROM_CLIENT_TYPE_OF_LOGIN = 0;
		/** 由客户端发出 - 协议类型：心跳包 */
		int FROM_CLIENT_TYPE_OF_KEEP$ALIVE = 1;
		/** 由客户端发出 - 协议类型：发送通用数据 */
		int FROM_CLIENT_TYPE_OF_COMMON$DATA = 2;
		/** 由客户端发出 - 协议类型：客户端退出登陆 */
		int FROM_CLIENT_TYPE_OF_LOGOUT = 3;
		
		/** 由客户端发出 - 协议类型：QoS保证机制中的消息应答包（目前只支持客户端间的QoS机制哦） */
		int FROM_CLIENT_TYPE_OF_RECIVED = 4;
		
		/** 由客户端发出 - 协议类型：C2S时的回显指令（此指令目前仅用于测试时） */
		int FROM_CLIENT_TYPE_OF_ECHO = 5;
	}
	
	//------------------------------------------------------- from server
	public interface S
	{
		/** 由服务端发出 - 协议类型：响应客户端的登陆 */
		int FROM_SERVER_TYPE_OF_RESPONSE$LOGIN = 50;
		/** 由服务端发出 - 协议类型：响应客户端的心跳包 */
		int FROM_SERVER_TYPE_OF_RESPONSE$KEEP$ALIVE = 51;
		
		/** 由服务端发出 - 协议类型：反馈给客户端的错误信息 */
		int FROM_SERVER_TYPE_OF_RESPONSE$FOR$ERROR = 52;
		
		/** 由服务端发出 - 协议类型：反馈回显指令给客户端 */
		int FROM_SERVER_TYPE_OF_RESPONSE$ECHO = 53;
	}
}
