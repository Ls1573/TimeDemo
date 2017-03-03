package com.time.memory.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 基类
 */
public class BaseEntity implements Parcelable {

	/**
	 * 成功标志
	 */
	private int code = -1;
	/**
	 * 异常信息
	 */
	private String message;

	public int getStatus() {
		return code;
	}

	public void setStatus(int status) {
		this.code = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.code);
		dest.writeString(this.message);
	}

	public BaseEntity() {
	}

	protected BaseEntity(Parcel in) {
		this.code = in.readInt();
		this.message = in.readString();
	}

}
