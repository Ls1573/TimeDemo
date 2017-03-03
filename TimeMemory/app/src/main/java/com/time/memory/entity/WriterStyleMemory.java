package com.time.memory.entity;


import android.os.Parcel;

import com.time.memory.MainApplication;
import com.time.memory.util.DateUtil;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆头
 * @date 2016/10/19 8:12
 */
public class WriterStyleMemory extends BaseEntity {

	private String memoryDate;

	private String userId;

	private String username;

	private String title;

	private String sign;

	/**
	 * 标签ID
	 */
	private String labelId;

	/**
	 * 标签
	 */
	private String labelName;
	/**
	 * 选择圈子发布时传圈子ID，否则为空串
	 */
	private String groupId;

	/**
	 * 圈子标签
	 */
	private String groupLabelId;

	/**
	 * 记忆Id
	 */
	private String memoryId;

	/**
	 * 圈子标签名
	 */
	private String groupLabelName;

	/**
	 * 状态 0：公开，1：私密 2：圈子（APP对应 0：TA的记忆 目录页面发布记忆 1：我的记忆 目录页面发布记忆 2：圈子记忆 目录页面发布记忆）
	 */
	private String state;

	public WriterStyleMemory() {
		memoryDate = DateUtil.getCurrentDateLine();
		groupId = "";
		title = "";
		userId = MainApplication.getUserId();
	}

	public String getMemoryDate() {
		return memoryDate;
	}

	public void setMemoryDate(String memoryDate) {
		this.memoryDate = memoryDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupLabelId() {
		return groupLabelId;
	}

	public void setGroupLabelId(String groupLabelId) {
		this.groupLabelId = groupLabelId;
	}

	public String getGroupLabelName() {
		return groupLabelName;
	}

	public void setGroupLabelName(String groupLabelName) {
		this.groupLabelName = groupLabelName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.memoryDate);
		dest.writeString(this.userId);
		dest.writeString(this.username);
		dest.writeString(this.title);
		dest.writeString(this.sign);
		dest.writeString(this.labelId);
		dest.writeString(this.labelName);
		dest.writeString(this.groupId);
		dest.writeString(this.groupLabelId);
		dest.writeString(this.memoryId);
		dest.writeString(this.groupLabelName);
		dest.writeString(this.state);
	}

	protected WriterStyleMemory(Parcel in) {
		super(in);
		this.memoryDate = in.readString();
		this.userId = in.readString();
		this.username = in.readString();
		this.title = in.readString();
		this.sign = in.readString();
		this.labelId = in.readString();
		this.labelName = in.readString();
		this.groupId = in.readString();
		this.groupLabelId = in.readString();
		this.memoryId = in.readString();
		this.groupLabelName = in.readString();
		this.state = in.readString();
	}

	public static final Creator<WriterStyleMemory> CREATOR = new Creator<WriterStyleMemory>() {
		public WriterStyleMemory createFromParcel(Parcel source) {
			return new WriterStyleMemory(source);
		}

		public WriterStyleMemory[] newArray(int size) {
			return new WriterStyleMemory[size];
		}
	};
}
