package com.time.memory.entity;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/24 15:58
 */
public class UnForkDto extends BaseEntity {
	private String title; //标题 评论时为评论Title 点赞时为记忆标题
	private String memoryId;        //记忆ID
	private String memorySrcId;        //记忆ID
	private String uId;        //操作人的ID
	private String uname; //操作人姓名
	private String hphoto;//操作人头像
	private String toUId; //评论时 的评论对象id 点赞时没有
	private String toName; //评论时 的评论对象姓名 点赞时没有
	private String insdForShow;        //记录时间
	private String flag; //区分 (评论：1) (点赞：2)
	private String groupId;//群Id
	private String groupName;//群name
	private String mfgflag;//跳转目标(1;我的;2:他的;3:圈子)
	private String mtitle;//标题

	private ArrayList<UnForkDto> unReads;

	public String getHphoto() {
		return hphoto;
	}

	public void setHphoto(String hphoto) {
		this.hphoto = hphoto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getToUId() {
		return toUId;
	}

	public void setToUId(String toUId) {
		this.toUId = toUId;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getInsdForShow() {
		return insdForShow;
	}

	public void setInsdForShow(String insdForShow) {
		this.insdForShow = insdForShow;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getFlag() {
		return flag;
	}

	public ArrayList<UnForkDto> getUnReads() {
		return unReads;
	}

	public void setUnReads(ArrayList<UnForkDto> unReads) {
		this.unReads = unReads;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getMfgflag() {
		return mfgflag;
	}

	public void setMfgflag(String mfgflag) {
		this.mfgflag = mfgflag;
	}

	public String getMtitle() {
		return mtitle;
	}

	public void setMtitle(String mtitle) {
		this.mtitle = mtitle;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMemorySrcId() {
		return memorySrcId;
	}

	public void setMemorySrcId(String memorySrcId) {
		this.memorySrcId = memorySrcId;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.title);
		dest.writeString(this.memoryId);
		dest.writeString(this.memorySrcId);
		dest.writeString(this.uId);
		dest.writeString(this.uname);
		dest.writeString(this.hphoto);
		dest.writeString(this.toUId);
		dest.writeString(this.toName);
		dest.writeString(this.insdForShow);
		dest.writeString(this.flag);
		dest.writeString(this.groupId);
		dest.writeString(this.groupName);
		dest.writeString(this.mfgflag);
		dest.writeString(this.mtitle);
		dest.writeTypedList(unReads);
	}

	public UnForkDto() {
	}

	protected UnForkDto(Parcel in) {
		super(in);
		this.title = in.readString();
		this.memoryId = in.readString();
		this.memorySrcId = in.readString();
		this.uId = in.readString();
		this.uname = in.readString();
		this.hphoto = in.readString();
		this.toUId = in.readString();
		this.toName = in.readString();
		this.insdForShow = in.readString();
		this.flag = in.readString();
		this.groupId = in.readString();
		this.groupName = in.readString();
		this.mfgflag = in.readString();
		this.mtitle = in.readString();
		this.unReads = in.createTypedArrayList(UnForkDto.CREATOR);
	}

	public static final Creator<UnForkDto> CREATOR = new Creator<UnForkDto>() {
		public UnForkDto createFromParcel(Parcel source) {
			return new UnForkDto(source);
		}

		public UnForkDto[] newArray(int size) {
			return new UnForkDto[size];
		}
	};
}
