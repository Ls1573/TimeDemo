package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * 消息
 *
 * @author admin
 */
@Entity(nameInDb = "message_vo")
public class Message extends BaseEntity {

	@Id
	@Property(nameInDb = "Id")
	private String uuid;
	@Property(nameInDb = "messageType")
	private String messageType;
	@Property(nameInDb = "messageDetail")
	private String messageDetail;
	@Property(nameInDb = "sendUserId")
	private String sendUserId;
	@Property(nameInDb = "readFlg")
	private String readFlg;
	@Property(nameInDb = "userId")
	private String userId;
	@Property(nameInDb = "sendUserName")
	private String sendUserName;
	@Property(nameInDb = "sendUserPhoto")
	private String sendUserPhoto;
	/**
	 * 有没有处理(0:未处理;1:已处理)
	 */
	@Property(nameInDb = "hasDeal")
	private String hasDeal;

	// 做成日
	@Property(nameInDb = "insDate")
	private String insDate;
	// 做成时间
	@Property(nameInDb = "insTime")
	private String insTime;
	//消息状态
	@Transient
	private String msgType;
	// 消息列表
	@Transient
	private List<Message> messageVoList;

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
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

	public String getInsTime() {
		return insTime;
	}

	public void setInsTime(String insTime) {
		this.insTime = insTime;
	}

	public List<Message> getMessageVoList() {
		return messageVoList;
	}

	public void setMessageVoList(List<Message> messageVoList) {
		this.messageVoList = messageVoList;
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

	public String getHasDeal() {
		return hasDeal;
	}

	public void setHasDeal(String hasDeal) {
		this.hasDeal = hasDeal;
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
		dest.writeString(this.sendUserName);
		dest.writeString(this.sendUserPhoto);
		dest.writeString(this.hasDeal);
		dest.writeString(this.insDate);
		dest.writeString(this.insTime);
		dest.writeString(this.msgType);
		dest.writeTypedList(messageVoList);
	}

	public Message() {
	}

	protected Message(Parcel in) {
		super(in);
		this.uuid = in.readString();
		this.messageType = in.readString();
		this.messageDetail = in.readString();
		this.sendUserId = in.readString();
		this.readFlg = in.readString();
		this.userId = in.readString();
		this.sendUserName = in.readString();
		this.sendUserPhoto = in.readString();
		this.hasDeal = in.readString();
		this.insDate = in.readString();
		this.insTime = in.readString();
		this.msgType = in.readString();
		this.messageVoList = in.createTypedArrayList(Message.CREATOR);
	}

	@Generated(hash = 17002389)
	public Message(String uuid, String messageType, String messageDetail,
			String sendUserId, String readFlg, String userId, String sendUserName,
			String sendUserPhoto, String hasDeal, String insDate, String insTime) {
		this.uuid = uuid;
		this.messageType = messageType;
		this.messageDetail = messageDetail;
		this.sendUserId = sendUserId;
		this.readFlg = readFlg;
		this.userId = userId;
		this.sendUserName = sendUserName;
		this.sendUserPhoto = sendUserPhoto;
		this.hasDeal = hasDeal;
		this.insDate = insDate;
		this.insTime = insTime;
	}

	public static final Creator<Message> CREATOR = new Creator<Message>() {
		public Message createFromParcel(Parcel source) {
			return new Message(source);
		}

		public Message[] newArray(int size) {
			return new Message[size];
		}
	};
}