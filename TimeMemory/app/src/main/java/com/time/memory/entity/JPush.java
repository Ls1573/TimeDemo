package com.time.memory.entity;

import android.os.Parcel;

/**
 * ==============================
 * 
 * @author Qiu
 * 
 * @Package com.njkj.yulian.entity
 * 
 * @Description:极光推送
 * 
 * @date 2016-8-05 上午10:27:52
 * 
 * @version 1.0 ==============================
 */
public class JPush extends BaseEntity {

	private static final long serialVersionUID = 4442872453661840951L;
	/**
	 * topicId
	 */
	private String topicId;
	/**
	 * 推送的信息
	 */
	private String msg;
	/**
	 * 提示头
	 */
	private String title;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.topicId);
		dest.writeString(this.msg);
		dest.writeString(this.title);
	}

	public JPush() {
	}

	protected JPush(Parcel in) {
		super(in);
		this.topicId = in.readString();
		this.msg = in.readString();
		this.title = in.readString();
	}

	public static final Creator<JPush> CREATOR = new Creator<JPush>() {
		public JPush createFromParcel(Parcel source) {
			return new JPush(source);
		}

		public JPush[] newArray(int size) {
			return new JPush[size];
		}
	};
}
