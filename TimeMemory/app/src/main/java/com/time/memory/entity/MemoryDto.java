package com.time.memory.entity;

import android.os.Parcel;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:首页信息
 * @date 2016/10/19 15:10
 */
public class MemoryDto extends BaseEntity {
	//提到你的个数
	private long remindCnt;
	//我的
	private String titleM; //我的记忆
	private String memoryIdM;
	private String headPhotoM;//我的图片

	private String updateDateMForShow;//我的时间
	private String updateDateFForShow;//他的时间

	private String updateDateM; //记忆的更新时间
	private long memoryCntM;    //记忆数
	private long unReadCntM;    //未读消息数
	private  long totalPCnt;   //总的未读记忆和点赞数 我的+他的+圈子的和

	//他的
	private String userIdF; //朋友的ID
	private String memoryIdF; //朋友的第一篇记忆ID
	private String userNameF; //朋友的姓名
	private String titleF;//朋友的第一篇记忆标题
	private String updateDateF;
	private long memoryCntF;    //记忆数
	private long friendCntF;    //好友数
	private long unReadCntF;    //未读消息数
	private List<String> headPhotosF; //朋友中前四个人的头像
	private long unReadMPointAddCntF;//他的未读补充记忆
	private long unReadMemoryCntF;//他的未读记忆（新增）

	//圈子的
	private List<MGroup> groups;

	public List<String> getHeadPhotosF() {
		return headPhotosF;
	}

	public long getRemindCnt() {
		return remindCnt;
	}

	public void setRemindCnt(long remindCnt) {
		this.remindCnt = remindCnt;
	}

	public String getTitleM() {
		return titleM;
	}

	public void setTitleM(String titleM) {
		this.titleM = titleM;
	}

	public String getMemoryIdM() {
		return memoryIdM;
	}

	public void setMemoryIdM(String memoryIdM) {
		this.memoryIdM = memoryIdM;
	}

	public String getHeadPhotoM() {
		return headPhotoM;
	}

	public void setHeadPhotoM(String headPhotoM) {
		this.headPhotoM = headPhotoM;
	}

	public String getUpdateDateMForShow() {
		return updateDateMForShow;
	}

	public void setUpdateDateMForShow(String updateDateMForShow) {
		this.updateDateMForShow = updateDateMForShow;
	}

	public String getUpdateDateFForShow() {
		return updateDateFForShow;
	}

	public void setUpdateDateFForShow(String updateDateFForShow) {
		this.updateDateFForShow = updateDateFForShow;
	}

	public String getUpdateDateM() {
		return updateDateM;
	}

	public void setUpdateDateM(String updateDateM) {
		this.updateDateM = updateDateM;
	}

	public long getMemoryCntM() {
		return memoryCntM;
	}

	public void setMemoryCntM(long memoryCntM) {
		this.memoryCntM = memoryCntM;
	}

	public long getUnReadCntM() {
		return unReadCntM;
	}

	public void setUnReadCntM(long unReadCntM) {
		this.unReadCntM = unReadCntM;
	}

	public String getUserIdF() {
		return userIdF;
	}

	public void setUserIdF(String userIdF) {
		this.userIdF = userIdF;
	}

	public String getMemoryIdF() {
		return memoryIdF;
	}

	public void setMemoryIdF(String memoryIdF) {
		this.memoryIdF = memoryIdF;
	}

	public String getUserNameF() {
		return userNameF;
	}

	public void setUserNameF(String userNameF) {
		this.userNameF = userNameF;
	}

	public String getTitleF() {
		return titleF;
	}

	public void setTitleF(String titleF) {
		this.titleF = titleF;
	}

	public String getUpdateDateF() {
		return updateDateF;
	}

	public void setUpdateDateF(String updateDateF) {
		this.updateDateF = updateDateF;
	}

	public long getMemoryCntF() {
		return memoryCntF;
	}

	public void setMemoryCntF(long memoryCntF) {
		this.memoryCntF = memoryCntF;
	}

	public long getFriendCntF() {
		return friendCntF;
	}

	public void setFriendCntF(long friendCntF) {
		this.friendCntF = friendCntF;
	}

	public long getUnReadCntF() {
		return unReadCntF;
	}

	public void setUnReadCntF(long unReadCntF) {
		this.unReadCntF = unReadCntF;
	}

	public void setHeadPhotosF(List<String> headPhotosF) {
		this.headPhotosF = headPhotosF;
	}

	public List<MGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<MGroup> groups) {
		this.groups = groups;
	}

	public long getUnReadMPointAddCntF() {
		return unReadMPointAddCntF;
	}

	public void setUnReadMPointAddCntF(long unReadMPointAddCntF) {
		this.unReadMPointAddCntF = unReadMPointAddCntF;
	}

	public long getUnReadMemoryCntF() {
		return unReadMemoryCntF;
	}

	public void setUnReadMemoryCntF(long unReadMemoryCntF) {
		this.unReadMemoryCntF = unReadMemoryCntF;
	}

	public long getTotalPCnt() {
		return totalPCnt;
	}

	public void setTotalPCnt(long totalPCnt) {
		this.totalPCnt = totalPCnt;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeLong(this.remindCnt);
		dest.writeString(this.titleM);
		dest.writeString(this.memoryIdM);
		dest.writeString(this.headPhotoM);
		dest.writeString(this.updateDateMForShow);
		dest.writeString(this.updateDateFForShow);
		dest.writeString(this.updateDateM);
		dest.writeLong(this.memoryCntM);
		dest.writeLong(this.unReadCntM);
		dest.writeLong(this.totalPCnt);
		dest.writeString(this.userIdF);
		dest.writeString(this.memoryIdF);
		dest.writeString(this.userNameF);
		dest.writeString(this.titleF);
		dest.writeString(this.updateDateF);
		dest.writeLong(this.memoryCntF);
		dest.writeLong(this.friendCntF);
		dest.writeLong(this.unReadCntF);
		dest.writeStringList(this.headPhotosF);
		dest.writeLong(this.unReadMPointAddCntF);
		dest.writeLong(this.unReadMemoryCntF);
		dest.writeTypedList(groups);
	}

	public MemoryDto() {
	}

	protected MemoryDto(Parcel in) {
		super(in);
		this.remindCnt = in.readLong();
		this.titleM = in.readString();
		this.memoryIdM = in.readString();
		this.headPhotoM = in.readString();
		this.updateDateMForShow = in.readString();
		this.updateDateFForShow = in.readString();
		this.updateDateM = in.readString();
		this.memoryCntM = in.readLong();
		this.unReadCntM = in.readLong();
		this.totalPCnt = in.readLong();
		this.userIdF = in.readString();
		this.memoryIdF = in.readString();
		this.userNameF = in.readString();
		this.titleF = in.readString();
		this.updateDateF = in.readString();
		this.memoryCntF = in.readLong();
		this.friendCntF = in.readLong();
		this.unReadCntF = in.readLong();
		this.headPhotosF = in.createStringArrayList();
		this.unReadMPointAddCntF = in.readLong();
		this.unReadMemoryCntF = in.readLong();
		this.groups = in.createTypedArrayList(MGroup.CREATOR);
	}

	public static final Creator<MemoryDto> CREATOR = new Creator<MemoryDto>() {
		public MemoryDto createFromParcel(Parcel source) {
			return new MemoryDto(source);
		}

		public MemoryDto[] newArray(int size) {
			return new MemoryDto[size];
		}
	};
}
