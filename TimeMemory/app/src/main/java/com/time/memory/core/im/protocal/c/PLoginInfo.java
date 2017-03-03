/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * PLoginInfo.java at 2016-2-20 11:06:06, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im.protocal.c;

/**
 * 登陆信息DTO类.
 * 
 * @author Jack Jiang, 2013-10-07
 * @version 1.0
 */
public class PLoginInfo
{
	/** 登陆时提交的用户名：此用户名对框架来说可以随意，具体意义由上层逻辑决即可 */
	private String loginName = null;
	/** 登陆时提交的密码：此密码对框架来说可以随意，具体意义由上层逻辑决即可 */
	private String loginPsw = null;
	
	/**
	 * 额外信息字符串。本字段目前为保留字段，供上层应用自行放置需要的内容。
	 * @since 2.1.6 */
	private String extra = null;
	
	/**
	 * 构造方法。
	 * 
	 * @param loginName 登陆时提交的用户名：此用户名对框架来说可以随意，具体意义由上层逻辑决即可
	 * @param loginPsw 登陆时提交的密码：此密码对框架来说可以随意，具体意义由上层逻辑决即可
	 */
	public PLoginInfo(String loginName, String loginPsw)
	{
//		this.loginName = loginName;
//		this.loginPsw = loginPsw;
		this(loginName, loginPsw, null);
	}
	
	/**
	 * 构造方法。
	 * 
	 * @param loginName 登陆时提交的用户名：此用户名对框架来说可以随意，具体意义由上层逻辑决即可
	 * @param loginPsw 登陆时提交的密码：此密码对框架来说可以随意，具体意义由上层逻辑决即可
	 * @param extra 额外信息字符串。本字段目前为保留字段，供上层应用自行放置需要的内容
	 */
	public PLoginInfo(String loginName, String loginPsw, String extra)
	{
		this.loginName = loginName;
		this.loginPsw = loginPsw;
		this.extra = extra;
	}
	
	/**
	 * 返回登陆时提交的用户名：此用户名对框架来说可以随意，具体意义由上层逻辑决即可
	 * 
	 * @return
	 */
	public String getLoginName()
	{
		return loginName;
	}

	/**
	 * 设置登陆时提交的用户名：此用户名对框架来说可以随意，具体意义由上层逻辑决即可
	 * reqLoginIm
	 * @param loginName
	 */
	public void setLoginName(String loginName)
	{
		this.loginName = loginName;
	}

	/**
	 * 返回登陆时提交的密码：此密码对框架来说可以随意，具体意义由上层逻辑决即可
	 * 
	 * @return
	 */
	public String getLoginPsw()
	{
		return loginPsw;
	}

	/**
	 * 设置登陆时提交的密码：此密码对框架来说可以随意，具体意义由上层逻辑决即可
	 * 
	 * @param loginPsw
	 */
	public void setLoginPsw(String loginPsw)
	{
		this.loginPsw = loginPsw;
	}

	/**
	 * 返回额外信息字符串。本字段目前为保留字段，供上层应用自行放置需要的内容。
	 * 
	 * @return
	 * @since 2.1.6
	 */
	public String getExtra()
	{
		return extra;
	}
	
	/**
	 * 设置额外信息字符串。本字段目前为保留字段，供上层应用自行放置需要的内容。
	 * 
	 * @param extra
	 * @since 2.1.6
	 */
	public void setExtra(String extra)
	{
		this.extra = extra;
	}
}
