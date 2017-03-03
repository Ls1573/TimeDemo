package com.time.memory.mt.common.constant.message;

public enum BusynessType {

	/**
	 * 登录后建立UDP连接
	 */
	CL01("登录后建立UDP连接", "CL01"),

	/**
	 * 新建圈子
	 */
	CG01("新建圈子", "CG01"),
	/**
	 * 我的圈子列表
	 */
	CG02("我的圈子列表", "CG02"),
	/**
	 * 添加好友进圈子
	 */
	CG03("添加好友进圈子", "CG03"),
	/**
	 * 获取圈子里用户列表
	 */
	CG04("获取圈子里用户列表", "CG04"),
	/**
	 * 首次上传通讯录
	 */
	CG05("首次上传通讯录", "CG05"),
	/**
	 * 获取好友列表
	 */
	CG06("获取好友列表", "CG06"),
	/**
	 * 获取好友详情
	 */
	CG07("获取好友详情", "CG07"),
	/**
	 * 修改圈子名称
	 */
	CG08("修改圈子名称", "CG08"),
	/**
	 * 删除圈子成员
	 */
	CG09("删除圈子成员", "CG09"),

	/**
	 * 通知好友进圈子
	 */
	SG01("通知好友进圈子", "SG01"),
	/**
	 * 通知好友已建立好友关系
	 */
	SG02("通知好友已建立好友关系", "SG02"),
	/**
	 * 通知用户被移除圈子
	 */
	SG03("通知用户被移除圈子", "SG03"),
	/**
	 * 通知用户圈子名称被修改
	 */
	SG04("通知用户圈子名称被修改", "SG04"),

	/**
	 * 获取未读消息
	 */
	CM01("获取未读消息", "CM01"),
	/**
	 * 消息设置为已读
	 */
	CM02("消息设置为已读", "CM02"),

	/**
	 * 通知用户有新消息
	 */
	SM01("通知用户有新消息", "SM01"),

	/**
	 * 获取个人信息，好友详情
	 */
	CP01("获取个人信息，好友详情", "CP01"),
	/**
	 * 修改个人信息
	 */
	CP02("修改个人信息", "CP02"),
	/**
	 * 不让他看我的记忆
	 */
	CP03("不让他看我的记忆", "CP03"),
	/**
	 * 不看他的记忆
	 */
	CP04("不看他的记忆", "CP04"),

	/**
	 * 点赞
	 */
	CA01("点赞", "CA01"),
	/**
	 * 评论
	 */
	CA10("评论", "CA10"),
	/**
	 * 补充记忆
	 */
	CA20("补充记忆", "CA20"),
	/**
	 * 点赞成功推送给其它好友
	 */
	SA01("点赞成功推送给其它好友", "SA01"),
	/**
	 * 评论成功推送给其它好友
	 */
	SA10("评论成功推送给其它好友", "SA10"),
	/**
	 * 补充记忆成功推送给其它好友
	 */
	SA20("补充记忆成功推送给其它好友", "SA20"),

	/**
	 * 记忆首页
	 */
	CF01("记忆首页", "CF01"),
	/**
	 * 提醒我一览
	 */
	CF02("提醒我一览", "CF02"),
	/**
	 * 我的记忆未读提示点击后一览
	 */
	CF03("我的记忆未读提示点击后一览", "CF03"),
	/**
	 * 他的记忆未读提示点击后一览
	 */
	CF04("他的记忆未读提示点击后一览", "CF04"),
	/**
	 * 圈子的记忆未读提示点击后一览
	 */
	CF05("圈子的记忆未读提示点击后一览", "CF05"),

	/**
	 * 获取我的记忆列表
	 */
	CE01("获取我的记忆列表", "CE01"),
	/**
	 * 获取他的记忆列表
	 */
	CE02("获取他的记忆列表", "CE02"),
	/**
	 * 获取圈子的记忆列表
	 */
	CE03("获取圈子的记忆列表", "CE03"),

	/**
	 * 获取我的记忆详细
	 */
	CD01("获取我的记忆详细", "CD01"),
	/**
	 * 获取他的记忆详细
	 */
	CD02("获取他的记忆详细", "CD02"),
	/**
	 * 获取圈子的记忆详细
	 */
	CD03("获取圈子的记忆详细", "CD03"),
	/**
	 * 获取圈子的记忆详细
	 */
	CD90("获取记忆片段详细", "CD90"),

	/**
	 * 用户举报
	 */
	CS01("用户举报", "CS01"),

	/**
	 * 写记忆
	 */
	CW01("写记忆", "CW01"),
	/**
	 * 编辑记忆
	 */
	CW02("编辑记忆", "CW02"),
	/**
	 * 记忆推送
	 */
	SW01("记忆推送", "SW01"),

	SW02("记忆补充", "SW02"),

	/**
	 * 删除记忆
	 */
	CX01("删除记忆", "CX01"),
	/**
	 * 删除评论
	 */
	CX02("删除评论", "CX02"),

	/**  */
	XX99("", "XX99");

	private String name;
	private String index;

	private BusynessType(String name, String index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(String index) {
		for (BusynessType c : BusynessType.values()) {
			if (c.getIndex().equals(index)) {
				return c.name;
			}
		}
		return null;
	}

	public static BusynessType getEnum(String index) {
		for (BusynessType c : BusynessType.values()) {
			if (c.getIndex().equals(index)) {
				return c;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
