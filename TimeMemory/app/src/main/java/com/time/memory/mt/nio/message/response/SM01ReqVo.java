package com.time.memory.mt.nio.message.response;


import android.os.Parcel;
import android.os.Parcelable;


/**
 * 消息
 */
public class SM01ReqVo extends MsgResponse implements Parcelable {
	/**
	 * 日期
	 */
	private String insDate;


	/**
	 * 时间
	 */
	private String insTime;
	/**
	 * 消息内容
	 */
	private String messageDetail;
	/**
	 * 消息类型
	 */
	private String messageType;
	/**
	 * 发送用户Id
	 */
	private String sendUserId;
	/**
	 * 发送用户name
	 */
	private String sendUserName;
	/**
	 * 发送用户头像
	 */
	private String sendUserPhoto;

	private String uuid;

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getInsTime() {
		return insTime;
	}

	public void setInsTime(String insTime) {
		this.insTime = insTime;
	}

	public String getMessageDetail() {
		return messageDetail;
	}

	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserPhoto() {
		return sendUserPhoto;
	}

	public void setSendUserPhoto(String sendUserPhoto) {
		this.sendUserPhoto = sendUserPhoto;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.insDate);
		dest.writeString(this.insTime);
		dest.writeString(this.messageDetail);
		dest.writeString(this.messageType);
		dest.writeString(this.sendUserId);
		dest.writeString(this.sendUserName);
		dest.writeString(this.sendUserPhoto);
		dest.writeString(this.uuid);
	}

	public SM01ReqVo() {
	}

	protected SM01ReqVo(Parcel in) {
		this.insDate = in.readString();
		this.insTime = in.readString();
		this.messageDetail = in.readString();
		this.messageType = in.readString();
		this.sendUserId = in.readString();
		this.sendUserName = in.readString();
		this.sendUserPhoto = in.readString();
		this.uuid = in.readString();
	}

	public static final Creator<SM01ReqVo> CREATOR = new Creator<SM01ReqVo>() {
		@Override
		public SM01ReqVo createFromParcel(Parcel source) {
			return new SM01ReqVo(source);
		}

		@Override
		public SM01ReqVo[] newArray(int size) {
			return new SM01ReqVo[size];
		}
	};
}
