package com.time.memory.entity;

import android.os.Parcel;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:指定圈子
 * @date 2016/9/13 11:37
 * ==============================
 */
public class Appoint extends BaseEntity {
	/**
	 * 群对应的Id
	 */
	private String Id;

	/**
	 * 群描述
	 */
	private String desc;

	/**
	 * 被选中
	 */
	private boolean isActicted;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isActicted() {
		return isActicted;
	}

	public void setIsActicted(boolean isActicted) {
		this.isActicted = isActicted;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.Id);
		dest.writeString(this.desc);
		dest.writeByte(isActicted ? (byte) 1 : (byte) 0);
	}

	public Appoint(String desc) {
		this.desc = desc;
	}

	public Appoint() {
	}

	protected Appoint(Parcel in) {
		super(in);
		this.Id = in.readString();
		this.desc = in.readString();
		this.isActicted = in.readByte() != 0;
	}

	public static final Creator<Appoint> CREATOR = new Creator<Appoint>() {
		public Appoint createFromParcel(Parcel source) {
			return new Appoint(source);
		}

		public Appoint[] newArray(int size) {
			return new Appoint[size];
		}
	};
}
