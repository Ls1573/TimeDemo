/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * PLoginInfoResponse.java at 2016-2-20 11:06:06, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im.protocal.s;

/**
 * 登陆结果响应信息DTO类。
 * 
 * @author Jack Jiang, 2013-10-03
 * @version 1.0
 */
public class PLoginInfoResponse
{
	/** 错误码：0表示认证成功，否则是用户自定的错误码（该码应该是>1024的整数） */
	private int code = 0;
	/** 用户登陆路成功后分配给客户的唯一id：此值只在code==0时才有意义 */
	private String user_id = "-1";
	
	public PLoginInfoResponse(int code, String user_id)
	{
		this.code = code;
		this.user_id = user_id;
	}

	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}
}
