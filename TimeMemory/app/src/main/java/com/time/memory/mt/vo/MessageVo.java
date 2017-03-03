package com.time.memory.mt.vo;

import android.os.Parcel;

import com.time.memory.entity.BaseEntity;

/**
 * 消息
 *
 * @author admin
 */
public class MessageVo extends BaseEntity {

	private String uuid;
	private String messageType;
	private String messageDetail;
	private String sendUserId;
	private String readFlg;
	private String userId;

	// 做成日
	private String insDate;

	// 做成时间
	private String insTime;


	public String getInsTime() {
		return insTime;
	}

	public void setInsTime(String insTime) {
		this.insTime = insTime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageDetail() {
		return messageDetail;
	}

	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getReadFlg() {
		return readFlg;
	}

	public void setReadFlg(String readFlg) {
		this.readFlg = readFlg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.uuid);
		dest.writeString(this.messageType);
		dest.writeString(this.messageDetail);
		dest.writeString(this.sendUserId);
		dest.writeString(this.readFlg);
		dest.writeString(this.userId);
		dest.writeString(this.insDate);
		dest.writeString(this.insTime);
	}

	public MessageVo() {
	}

	protected MessageVo(Parcel in) {
		super(in);
		this.uuid = in.readString();
		this.messageType = in.readString();
		this.messageDetail = in.readString();
		this.sendUserId = in.readString();
		this.readFlg = in.readString();
		this.userId = in.readString();
		this.insDate = in.readString();
		this.insTime = in.readString();
	}

	public static final Creator<MessageVo> CREATOR = new Creator<MessageVo>() {
		public MessageVo createFromParcel(Parcel source) {
			return new MessageVo(source);
		}

		public MessageVo[] newArray(int size) {
			return new MessageVo[size];
		}
	};
}