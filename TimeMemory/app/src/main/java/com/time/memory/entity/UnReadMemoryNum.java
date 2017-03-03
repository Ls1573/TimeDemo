package com.time.memory.entity;

import android.os.Parcel;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:未读记忆数
 * @date 2016/11/21 16:42
 */
public class UnReadMemoryNum extends BaseEntity {

	private String unReadMPointAddCntM;//我的补充记忆数
	private String unReadMPointAddCntF;//他的补充记忆数
	private String unReadMemoryCntF;//他的新增记忆数

	private String groupId;//群Id
	private String unReadMPointAddCntG;//群补充记忆数
	private String unReadMemoryCntG;//群新增记忆数
	private String totalPCnt;//总的未读记忆和点赞数

	private List<UnReadMemoryNum> unReadGroups;

	private List<NewContacts> ufrieds;//未捕获的好友

	public String getUnReadMPointAddCntM() {
		return unReadMPointAddCntM;
	}

	public void setUnReadMPointAddCntM(String unReadMPointAddCntM) {
		this.unReadMPointAddCntM = unReadMPointAddCntM;
	}

	public String getUnReadMPointAddCntF() {
		return unReadMPointAddCntF;
	}

	public void setUnReadMPointAddCntF(String unReadMPointAddCntF) {
		this.unReadMPointAddCntF = unReadMPointAddCntF;
	}

	public String getUnReadMemoryCntF() {
		return unReadMemoryCntF;
	}

	public void setUnReadMemoryCntF(String unReadMemoryCntF) {
		this.unReadMemoryCntF = unReadMemoryCntF;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUnReadMPointAddCntG() {
		return unReadMPointAddCntG;
	}

	public void setUnReadMPointAddCntG(String unReadMPointAddCntG) {
		this.unReadMPointAddCntG = unReadMPointAddCntG;
	}

	public String getUnReadMemoryCntG() {
		return unReadMemoryCntG;
	}

	public void setUnReadMemoryCntG(String unReadMemoryCntG) {
		this.unReadMemoryCntG = unReadMemoryCntG;
	}

	public List<UnReadMemoryNum> getUnReadGroups() {
		return unReadGroups;
	}

	public void setUnReadGroups(List<UnReadMemoryNum> unReadGroups) {
		this.unReadGroups = unReadGroups;
	}

	public String getTotalPCnt() {
		return totalPCnt;
	}

	public void setTotalPCnt(String totalPCnt) {
		this.totalPCnt = totalPCnt;
	}

	public List<NewContacts> getUfrieds() {
		return ufrieds;
	}

	public void setUfrieds(List<NewContacts> ufrieds) {
		this.ufrieds = ufrieds;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.unReadMPointAddCntM);
		dest.writeString(this.unReadMPointAddCntF);
		dest.writeString(this.unReadMemoryCntF);
		dest.writeString(this.groupId);
		dest.writeString(this.unReadMPointAddCntG);
		dest.writeString(this.unReadMemoryCntG);
		dest.writeString(this.totalPCnt);
		dest.writeTypedList(unReadGroups);
		dest.writeTypedList(ufrieds);
	}

	public UnReadMemoryNum() {
	}

	protected UnReadMemoryNum(Parcel in) {
		super(in);
		this.unReadMPointAddCntM = in.readString();
		this.unReadMPointAddCntF = in.readString();
		this.unReadMemoryCntF = in.readString();
		this.groupId = in.readString();
		this.unReadMPointAddCntG = in.readString();
		this.unReadMemoryCntG = in.readString();
		this.totalPCnt = in.readString();
		this.unReadGroups = in.createTypedArrayList(UnReadMemoryNum.CREATOR);
		this.ufrieds = in.createTypedArrayList(NewContacts.CREATOR);
	}

	public static final Creator<UnReadMemoryNum> CREATOR = new Creator<UnReadMemoryNum>() {
		public UnReadMemoryNum createFromParcel(Parcel source) {
			return new UnReadMemoryNum(source);
		}

		public UnReadMemoryNum[] newArray(int size) {
			return new UnReadMemoryNum[size];
		}
	};
}
