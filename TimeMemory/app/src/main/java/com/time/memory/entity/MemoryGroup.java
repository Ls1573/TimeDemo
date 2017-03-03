package com.time.memory.entity;


import android.os.Parcel;

/**
 * @author @Qiu
 * @version V1.3
 * @Description:记忆分属标签
 * @date 2017/1/4 12:04
 */
public class MemoryGroup extends BaseEntity {
	private String name;

	//0->私密;1->群的
	private int self;

	public MemoryGroup(int self, String name) {
		this.self = self;
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSelf() {
		return self;
	}

	public void setSelf(int self) {
		this.self = self;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.name);
		dest.writeInt(this.self);
	}

	public MemoryGroup() {
	}

	protected MemoryGroup(Parcel in) {
		super(in);
		this.name = in.readString();
		this.self = in.readInt();
	}

	public static final Creator<MemoryGroup> CREATOR = new Creator<MemoryGroup>() {
		public MemoryGroup createFromParcel(Parcel source) {
			return new MemoryGroup(source);
		}

		public MemoryGroup[] newArray(int size) {
			return new MemoryGroup[size];
		}
	};
}
