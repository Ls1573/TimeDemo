package com.time.memory.entity;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:群
 * @date 2016-9-6下午18:51:15
 * ==============================
 */
public class GroupDto {
	/**
	 * 信息对应的Id
	 */
	private String groupId;

	/**
	 * 是否有记忆描述[同学(36)]
	 */
	private String desc;

	/**
	 * 群名字
	 */
	private String groupName;

	/**
	 * 是否有最新的图片
	 */
	private String pic;

	/**
	 * 创建日期
	 */
	private String insDate;

	/**
	 * 管理员Id
	 */
	private String adminUserId;

	/**
	 * 群成员数量
	 */
	private String groupCount;

	/**
	 * 群是否冻结(圈子冻结 0：正常 1：冻结)
	 */
	private String freeze;

	/**
	 * 群是否已经激活了( 0：正常 1：未激活)
	 */
	private String activeFlg;

	/**
	 * 是否有最新的
	 */
	private boolean isNew;
	/**
	 * 管理员name
	 */
	private String adminUserName;

	/**
	 * 是否显示
	 */
	private boolean isVisable;

	/**
	 * 创建人
	 */
	private String userId;

	/**
	 * 创建人的名字
	 */
	private String userName;

	/**
	 * 记忆数
	 */
	private int memoryCnt;

	/**
	 * 好友数
	 */
	private int friendCnt;

	/**
	 * 未读消息数
	 */
	private int unReadCnt;

	/**
	 * 圈子第一篇记忆的标题
	 */
	private String title;

	/**
	 * 圈子第一篇记忆的ID
	 */
	private String memoryId;
	/**
	 * 邀请人Id
	 */
	private String inviteUserId;
	/**
	 * 邀请人姓名
	 */
	private String inviteUserName;

	/**
	 * 类别区分(1:我的;2:他的;3:圈子的)
	 */
	private int type;
	/**
	 * 头像图片集
	 */
	private List<String> headPhotos;


}
