package com.time.memory.entity;

import android.os.Parcel;

import com.time.memory.MainApplication;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:写记忆
 * @date 2016/10/21 16:06
 */
public class SupporyMemory extends BaseEntity {
	/**
	 * token
	 */
	private String userToken;

	/**
	 * 状态
	 */
	private String state;

	/**
	 * 题头
	 */
	private WriterStyleMemory memoryVo;

	/**
	 * 记忆片段
	 */
	private MemoryPointVo memoryPointVo;

	public SupporyMemory() {
		this.userToken = MainApplication.getUserToken();
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public WriterStyleMemory getMemoryVo() {
		return memoryVo;
	}

	public void setMemoryVo(WriterStyleMemory memoryVo) {
		this.memoryVo = memoryVo;
	}

	public MemoryPointVo getMemoryPointVo() {
		return memoryPointVo;
	}

	public void setMemoryPointVo(MemoryPointVo memoryPointVo) {
		this.memoryPointVo = memoryPointVo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userToken);
		dest.writeString(this.state);
		dest.writeParcelable(this.memoryVo, 0);
		dest.writeParcelable(this.memoryPointVo, 0);
	}

	protected SupporyMemory(Parcel in) {
		super(in);
		this.userToken = in.readString();
		this.state = in.readString();
		this.memoryVo = in.readParcelable(WriterStyleMemory.class.getClassLoader());
		this.memoryPointVo = in.readParcelable(MemoryPointVo.class.getClassLoader());
	}

	public static final Creator<SupporyMemory> CREATOR = new Creator<SupporyMemory>() {
		public SupporyMemory createFromParcel(Parcel source) {
			return new SupporyMemory(source);
		}

		public SupporyMemory[] newArray(int size) {
			return new SupporyMemory[size];
		}
	};
}
