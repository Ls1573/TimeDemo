/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * Protocal.java at 2016-2-20 11:06:06, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im.protocal;

import com.google.gson.Gson;

import java.util.UUID;

/**
 * 协议报文对象.
 * 
 * @author Jack Jiang, 2013-10-07
 * @version 1.0
 * @see ProtocalType
 */
public class Protocal
{
	/** 
	 * 协议类型.
	 * 
	 * @see ProtocalType
	 */
	private int type = 0;
	/** 协议数据内容 */
	private String dataContent = null;
	
	/** 消息发出方的id（当用户登陆时，此值可不设置） */
	private String from = "-1";
	/** 消息接收方的id（当用户退出时，此值可不设置） */
	private String to = "-1";
	
	/** 用于QoS消息包的质量保证时作为消息的指纹特征码（理论上全局唯一） */
	private String fp = null;
	/** 
	 * true表示本包需要进行QoS质量保证，否则不需要.
	 * <b>注：</b>目前只支持客户发给客户端的质量保证，暂不支持服务端主动发起的包哦（比如上下线通知等） */
	private boolean QoS = false;
	
	/** 本字段仅用于客户端QoS时：表示丢包重试次数 */
	private transient int retryCount = 0;
	
	/**
	 * 构造方法（QoS标记默认为false）。
	 * 
	 * @param type 协议类型
	 * @param dataContent 协议数据内容
	 * @param from 消息发出方的id（当用户登陆时，此值可不设置）
	 * @param to 消息接收方的id（当用户退出时，此值可不设备）
	 */
	public Protocal(int type, String dataContent, String from, String to)
	{
		this(type, dataContent, from, to, false, null);
	}
	/**
	 * 构造方法。
	 * 
	 * @param type 协议类型
	 * @param dataContent 协议数据内容
	 * @param from 消息发出方的id（当用户登陆时，此值可不设置）
	 * @param to 消息接收方的id（当用户退出时，此值可不设备）
	 * @param QoS 是否需要QoS支持，true表示是，否则不需要
	 * @param fingerPrint 协议包的指纹特征码，当 QoS字段=true时且本字段为null时方法中
	 * 将自动生成指纹码否则使用本参数指定的指纹码
	 */
	public Protocal(int type, String dataContent, String from, String to, boolean QoS, String fingerPrint)
	{
		this.type = type;
		this.dataContent = dataContent;
		this.from = from;
		this.to = to;
		this.QoS = QoS;
		
		// 只有在需要QoS支持时才生成指纹，否则浪费数据传输流量
		// 目前一个包的指纹只在对象建立时创建哦
		if(QoS && fingerPrint == null)
			fp = Protocal.genFingerPrint();
		else
			fp = fingerPrint;
	}
	
	/**
	 * 返回协议类型.
	 * 
	 * @return
	 * @see ProtocalType
	 */
	public int getType()
	{
		return type;
	}
	/**
	 * 设置协议类型.
	 * 
	 * @param type 协议类型
	 * @see ProtocalType
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
	/**
	 * 返回协议数据内容。
	 * 
	 * @return
	 */
	public String getDataContent()
	{
		return dataContent;
	}
	/**
	 * 设置协议数据内容。
	 * 
	 * @param dataContent
	 */
	public void setDataContent(String dataContent)
	{
		this.dataContent = dataContent;
	}
	
	/**
	 * 返回消息发出方的id。
	 * 
	 * @return
	 */
	public String getFrom()
	{
		return from;
	}
	/**
	 * 设置消息发出方的id（当用户登陆时，此值可不设置）。
	 * 
	 * @param from
	 */
	public void setFrom(String from)
	{
		this.from = from;
	}

	/**
	 * 返回消息接收方的id。
	 * 
	 * @return
	 */
	public String getTo()
	{
		return to;
	}
	/**
	 * 消息接收方的id（当用户退出时，此值可不设置）。 
	 * 
	 * @param to
	 */
	public void setTo(String to)
	{
		this.to = to;
	}
	
	/**
	 * 返回QoS质量保证时的指统特征码.
	 * 
	 * @return
	 * @see Protocal#genFingerPrint()
	 */
	public String getFp()
	{
		return fp;
	}

	/**
	 * 本字段仅用于QoS时：表示丢包重试次数。
	 * <p>
	 * <b>本值是<code>transient</code>类型，对象序列化时将会忽略此字段。
	 * 
	 * @return
	 */
	public int getRetryCount()
	{
		return retryCount;
	}
	/**
	 * 本方法仅用于QoS时：选出包重试次数+1。
	 * <p>
	 * <b>本方法理论上由MobileIMSDK内部调用，应用层无需额外调用。</b>
	 */
	public void increaseRetryCount()
	{
		retryCount += 1;
	}
	
	/**
	 * 是否需QoS支持。
	 * 
	 * @return true表示是，否则不是
	 */
	public boolean isQoS()
	{
		return QoS;
	}

	/**
	 * 将本对象转换成JSON字符串.
	 * 
	 * @return
	 */
	public String toGsonString()
	{
		return new Gson().toJson(this);
	}
	
	/**
	 * 将本对象转换成JSON后再转换成byte数组.
	 * 
	 * @return
	 * @see #toGsonString()
	 */
	public byte[] toBytes()
	{
		return CharsetHelper.getBytes(toGsonString());
	}
	
	/**
	 * Protocal深度对象克隆方法实现。
	 * 
	 * @return 克隆完成后的新对象
	 */
	@Override
	public Object clone()
	{
		// 克隆一个Protocal对象（该对象已重置retryCount数值为0）
		Protocal cloneP = new Protocal(this.getType()
				, this.getDataContent(), this.getFrom(), this.getTo(), this.isQoS(), this.getFp());
		return cloneP;
	}
	
	/**
	 * 返回QoS需要的消息包的指纹特征码.
	 * <p>
	 * <b>重要说明：</b>使用系统时间戳作为指纹码，则意味着只在Protocal生成的环境中可能唯一.
	 * 它存在重复的可能性有2种：
	 * <ul>
	 * 		<li>1) 比如在客户端生成时如果生成过快的话（时间戳最小单位是1毫秒，如1毫秒内生成
	 * 		多个指纹码），理论上是有重复可能性；</li>
	 * 		<li>2) 不同的客户端因为系统时间并不完全一致，理论上也是可能存在重复的，所以唯一性应是：好友+指纹码才对.</li>
	 * </ul>
	 * 
	 * <p>
	 * * 目前使用的UUID基本能保证全局唯一，但它有36位长（加上分隔符32+4），目前为了保持框架的算法可读性
	 * 暂时不进行优化，以后可考虑使用2进制方式或者Protobuffer实现。
	 * 
	 * @return 指纹特征码实际上就是系统的当时时间戳
	 * @see System#currentTimeMillis()
	 */
	public static String genFingerPrint()
	{
		return UUID.randomUUID().toString();
	}
}
