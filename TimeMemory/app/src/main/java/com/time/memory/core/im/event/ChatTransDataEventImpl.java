/*
 * Copyright (C) 2016 即时通讯网(52im.net) The MobileIMSDK Project. 
 * All rights reserved.
 * Project URL:https://github.com/JackJiang2011/MobileIMSDK
 *  
 * 即时通讯网(52im.net) - 即时通讯技术社区! PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * 
 * ChatTransDataEventImpl.java at 2016-2-20 11:20:18, code by Jack Jiang.
 * You can contact author with jack.jiang@52im.net or jb2011@163.com.
 */
package com.time.memory.core.im.event;

import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.CA10RespVo;
import com.time.memory.mt.nio.message.response.CG01RespVo;
import com.time.memory.mt.nio.message.response.CG04RespVo;
import com.time.memory.mt.nio.message.response.CM01RespVo;
import com.time.memory.mt.nio.message.response.CX01RespVo;
import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.mt.nio.message.response.MsgResponse;
import com.time.memory.mt.nio.message.response.SA01ReqVo;
import com.time.memory.mt.nio.message.response.SA10ReqVo;
import com.time.memory.mt.nio.message.response.SA20RespVo;
import com.time.memory.mt.nio.message.response.SG01RespVo;
import com.time.memory.mt.nio.message.response.SG02RespVo;
import com.time.memory.mt.nio.message.response.SG03RespVo;
import com.time.memory.mt.nio.message.response.SG04RespVo;
import com.time.memory.mt.nio.message.response.SM01RespVo;
import com.time.memory.mt.nio.message.response.SW01RespVo;
import com.time.memory.mt.nio.message.response.SW02RespVo;
import com.time.memory.core.callback.IMCallBack;
import com.time.memory.core.callback.IMCircleCallBack;
import com.time.memory.core.callback.IMCirclePeopleCallBack;
import com.time.memory.core.callback.IMCreateCallBack;
import com.time.memory.core.callback.IMDetailCommentCallBack;
import com.time.memory.core.callback.IMMemoryCallBack;
import com.time.memory.core.callback.IMMessageCallBack;
import com.time.memory.core.callback.IMMineCallBack;
import com.time.memory.core.callback.IMMsgReadCallBack;
import com.time.memory.core.callback.IMPointCommentCallBack;
import com.time.memory.core.constant.ImConstant;
import com.time.memory.core.im.android.event.ChatTransDataEvent;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:服务端返回
 * @date 2016/9/27 18:16
 */
public class ChatTransDataEventImpl implements ChatTransDataEvent {
	private final static String TAG = "ChatTransDataEventImpl";

	private Map<String, IMCallBack> imCallBackMap;

	public ChatTransDataEventImpl() {
		imCallBackMap = new HashMap<>();
	}

	/**
	 * 加入
	 *
	 * @param imCallBack
	 * @param key
	 */
	public void setImCallBack(IMCallBack imCallBack, String key) {
		//没有就加入
		imCallBackMap.remove(key);
		imCallBackMap.put(key, imCallBack);
	}

	/**
	 * 移除
	 *
	 * @param key
	 */
	public void removeCallback(String key) {
		imCallBackMap.remove(key);
	}

	@Override
	public void onTransBuffer(String fingerPrintOfProtocal, String dwUserid, String dataContent) {
		CLog.e(TAG, "收到来自用户" + dwUserid + "的消息:" + dataContent);
		MsgResponse response = (MsgResponse) JsonUtils.fromJson(dataContent, MsgResponse.class);
		//TODO 解析MsgResponse根据Type回调不同
		switchType(response.getType(), dataContent);
	}

	@Override
	public void onErrorResponse(int errorCode, String errorMsg) {
		CLog.e(TAG, "收到服务端错误消息，errorCode=" + errorCode + ", errorMsg=" + errorMsg);
	}


	/**
	 * 划分
	 *
	 * @param type
	 */
	private void switchType(String type, String response) {
		switch (BusynessType.getEnum(type)) {
			case CP02:
				//修改个人信息
				if (imCallBackMap.get(ImConstant.IMMINECALLBACK) != null) {
					MsgResponse mineResponse = (MsgResponse) JsonUtils.fromJson(response, MsgResponse.class);
					((IMMineCallBack) imCallBackMap.get(ImConstant.IMMESSAGECALLBACK)).onCode(mineResponse.getCode());
				}
				break;
			case CM01:
				break;
			case CM02:
				//消息-设置消息已读
				if (imCallBackMap.get(ImConstant.IMMSGREADCALLBACK) != null) {
					CM01RespVo cm01RespVo = JsonUtils.fromJson(response, CM01RespVo.class);
					((IMMsgReadCallBack) imCallBackMap.get(ImConstant.IMMSGREADCALLBACK)).onRead(cm01RespVo.getCode());
				}
				break;
			case CG01:
				//创建圈子
				if (imCallBackMap.get(ImConstant.IMCREATECIRCLE) != null) {
					CG01RespVo cm01RespVo = JsonUtils.fromJson(response, CG01RespVo.class);
					((IMCreateCallBack) imCallBackMap.get(ImConstant.IMCREATECIRCLE)).onCreateCall(cm01RespVo);
				}
			case CG04:
				//获取圈子里用户列表
				if (imCallBackMap.get(ImConstant.IMCIRCLEPEOPLE) != null) {
					CG04RespVo cm04RespVo = JsonUtils.fromJson(response, CG04RespVo.class);
					((IMCirclePeopleCallBack) imCallBackMap.get(ImConstant.IMCIRCLEPEOPLE)).onCirclePeople(cm04RespVo);
				}
				break;
			case CA10:
				//片段评论
				if (imCallBackMap.get(ImConstant.IMPOINT) != null) {
					CA10RespVo ca10RespVo = JsonUtils.fromJson(response, CA10RespVo.class);
					((IMPointCommentCallBack) imCallBackMap.get(ImConstant.IMPOINT)).onPointComment(ca10RespVo);
				}
				//详情评论
				if (imCallBackMap.get(ImConstant.IMDETAIL) != null) {
					CA10RespVo ca10RespVo = JsonUtils.fromJson(response, CA10RespVo.class);
					((IMDetailCommentCallBack) imCallBackMap.get(ImConstant.IMDETAIL)).onDetailComment(ca10RespVo);
				}
				break;
			case CX01:
				//详情删除
				if (imCallBackMap.get(ImConstant.IMDETAIL) != null) {
					CX01RespVo ca10RespVo = JsonUtils.fromJson(response, CX01RespVo.class);
					((IMDetailCommentCallBack) imCallBackMap.get(ImConstant.IMDETAIL)).removeDetail(ca10RespVo);
				}
				break;
			case CX02:
				//片段删除评论-TODO	类错误
				if (imCallBackMap.get(ImConstant.IMPOINT) != null) {
					CX02RespVo cx02RespVo = JsonUtils.fromJson(response, CX02RespVo.class);
					((IMPointCommentCallBack) imCallBackMap.get(ImConstant.IMPOINT)).onRemoveComment(cx02RespVo);
				}
				break;
			case CA01:
				//详情点赞
				//片段点赞
				break;
			case SW01:
				//记忆
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SW01RespVo reqVo = JsonUtils.fromJson(response, SW01RespVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onMemory(reqVo);
				}
				break;
			case SW02:
				//记忆转发
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SW02RespVo reqVo = JsonUtils.fromJson(response, SW02RespVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onForward(reqVo);
				}
				break;
			case SA01:
				//未读消息-点赞
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SA01ReqVo reqVo = JsonUtils.fromJson(response, SA01ReqVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onUnRead(reqVo);
				}
				break;
			case SA10:
				//未读消息-评论
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SA10ReqVo reqVo = JsonUtils.fromJson(response, SA10ReqVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onUnReadComment(reqVo);
				}
				break;
			case SA20:
				//补充记忆
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SA20RespVo reqVo = JsonUtils.fromJson(response, SA20RespVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onAddmemory(reqVo);
				}
				break;
			case SG01:
				//邀请加入圈子
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SG01RespVo reqVo = JsonUtils.fromJson(response, SG01RespVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onGroup(reqVo);
				}
				break;
			case SG02:
				//通知建立好友关系
				if (imCallBackMap.get(ImConstant.IMCIRCLE) != null) {
					SG02RespVo reqVo = JsonUtils.fromJson(response, SG02RespVo.class);
					((IMCircleCallBack) imCallBackMap.get(ImConstant.IMCIRCLE)).onContacts(reqVo);
				}

				break;
			case SG03:
				//被移除某个圈子
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SG03RespVo reqVo = JsonUtils.fromJson(response, SG03RespVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onRemoveGroup(reqVo);
				}
				break;
			case SG04:
				//修改圈子名
				if (imCallBackMap.get(ImConstant.IMMEMORY) != null) {
					SG04RespVo reqVo = JsonUtils.fromJson(response, SG04RespVo.class);
					((IMMemoryCallBack) imCallBackMap.get(ImConstant.IMMEMORY)).onUpGroup(reqVo);
				}
				break;
			case SM01:
				//未读消息-评论
				if (imCallBackMap.get(ImConstant.IMMESSAGE) != null) {
					SM01RespVo reqVo = JsonUtils.fromJson(response, SM01RespVo.class);
					((IMMessageCallBack) imCallBackMap.get(ImConstant.IMMESSAGE)).onGroup(reqVo);
				}
				break;
			default:
				break;
		}
	}
}
