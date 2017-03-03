package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆追加
 * @date 2016/11/21 8:34
 */
public class MemorySuppory extends BaseEntity {
	private String uuid;

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
		super.writeToParcel(dest, flags);
		dest.writeString(this.uuid);
	}

	public MemorySuppory() {
	}

	protected MemorySuppory(Parcel in) {
		super(in);
		this.uuid = in.readString();
	}

	public static final Creator<MemorySuppory> CREATOR = new Creator<MemorySuppory>() {
		public MemorySuppory createFromParcel(Parcel source) {
			return new MemorySuppory(source);
		}

		public MemorySuppory[] newArray(int size) {
			return new MemorySuppory[size];
		}
	};
}
