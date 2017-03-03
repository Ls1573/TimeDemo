package com.time.memory.entity;

import android.os.Parcel;

import com.time.memory.MainApplication;

import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:写记忆
 * @date 2016/10/21 16:06
 */
public class ReleaseMemory extends BaseEntity {
	/**
	 * token
	 */
	private String userToken;

	/**
	 * 题头
	 */
	private WriterStyleMemory memoryVo;

	/**
	 * 记忆片段
	 */
	private List<MemoryPointVo> memoryPointVo;

	public ReleaseMemory() {
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

	public List<MemoryPointVo> getMemoryPointVo() {
		return memoryPointVo;
	}

	public void setMemoryPointVo(List<MemoryPointVo> memoryPointVo) {
		this.memoryPointVo = memoryPointVo;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userToken);
		dest.writeParcelable(this.memoryVo, 0);
		dest.writeTypedList(memoryPointVo);
	}

	protected ReleaseMemory(Parcel in) {
		super(in);
		this.userToken = in.readString();
		this.memoryVo = in.readParcelable(WriterStyleMemory.class.getClassLoader());
		this.memoryPointVo = in.createTypedArrayList(MemoryPointVo.CREATOR);
	}

	public static final Creator<ReleaseMemory> CREATOR = new Creator<ReleaseMemory>() {
		public ReleaseMemory createFromParcel(Parcel source) {
			return new ReleaseMemory(source);
		}

		public ReleaseMemory[] newArray(int size) {
			return new ReleaseMemory[size];
		}
	};
}
