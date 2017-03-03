package com.time.memory.entity;

import android.os.Parcel;


import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:群记忆
 * @date 2016-9-6下午2:51:15
 * ==============================
 */
public class GroupMemory extends BaseEntity {

	/**
	 * 用户Id
	 */
	private String userIdG;

	/**
	 * 头像
	 */
	private String headPhotoG;

	/**
	 * Id
	 */
	private String memoryId;

	private String memorySrcId;
	/**
	 * 追加记忆数
	 */
	private int addmemoryCount;

	/**
	 * 评论数
	 */
	private int commentCount;

	/**
	 * 日期(不用管)
	 */
	private String memoryDate;

	/**
	 * 日期
	 */
	private String memoryDateForShow;


	/**
	 * 图片1
	 */
	private String photo1;

	/**
	 * 图片2
	 */
	private String photo2;

	/**
	 * 图片3
	 */
	private String photo3;

	/**
	 * 图片数
	 */
	private int photoCount;

	/**
	 * 图片数==photoCount
	 */
	private int photoTotalCount;

	/**
	 * 点赞数
	 */
	private int praiseCount;


	/**
	 * 头
	 */
	private String title;

	/**
	 * 地址
	 */
	private String local;
	/**
	 * 用户名
	 */
	private String userNameG;

	/**
	 * 未读补充记忆数
	 */
	private int unReadMPointAddCnt;
	/**
	 * 位置
	 */
	private int position;
	/**
	 * 是不是头
	 */
	private boolean isHeader;

	/**
	 * 状态
	 */
	private String state;


	public GroupMemory() {
	}

	public GroupMemory(String memoryId, String memorySrcId, String userId, String title) {
		this.memoryId = memoryId;
		this.memorySrcId = memorySrcId;
		this.userIdG = userId;
		this.title = title;
	}


	private boolean isLoaded;

	private ArrayList<GroupMemory> memoryList;

	private List<PhotoInfo> pictureEntits;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getUserIdG() {
		return userIdG;
	}

	public int getUnReadMPointAddCnt() {
		return unReadMPointAddCnt;
	}

	public void setUnReadMPointAddCnt(int unReadMPointAddCnt) {
		this.unReadMPointAddCnt = unReadMPointAddCnt;
	}

	public void setUserIdG(String userIdG) {
		this.userIdG = userIdG;
	}

	public String getHeadPhotoG() {
		return headPhotoG;
	}

	public void setHeadPhotoG(String headPhotoG) {
		this.headPhotoG = headPhotoG;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public int getAddmemoryCount() {
		return addmemoryCount;
	}

	public void setAddmemoryCount(int addmemoryCount) {
		this.addmemoryCount = addmemoryCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getMemoryDate() {
		return memoryDate;
	}

	public void setMemoryDate(String memoryDate) {
		this.memoryDate = memoryDate;
	}

	public String getMemoryDateForShow() {
		return memoryDateForShow;
	}

	public void setMemoryDateForShow(String memoryDateForShow) {
		this.memoryDateForShow = memoryDateForShow;
	}

	public int getPhotoTotalCount() {
		return photoTotalCount;
	}

	public void setPhotoTotalCount(int photoTotalCount) {
		this.photoTotalCount = photoTotalCount;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public int getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}

	public int getPraiseCount() {
		return praiseCount;
	}

	public void setPraiseCount(int praiseCount) {
		this.praiseCount = praiseCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserNameG() {
		return userNameG;
	}

	public void setUserNameG(String userNameG) {
		this.userNameG = userNameG;
	}

	public ArrayList<GroupMemory> getMemoryList() {
		return memoryList;
	}

	public void setMemoryList(ArrayList<GroupMemory> memoryList) {
		this.memoryList = memoryList;
	}

	public List<PhotoInfo> getPictureEntits() {
		return pictureEntits;
	}

	public void setPictureEntits(List<PhotoInfo> pictureEntits) {
		this.pictureEntits = pictureEntits;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
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
		dest.writeString(this.userIdG);
		dest.writeString(this.headPhotoG);
		dest.writeString(this.memoryId);
		dest.writeString(this.memorySrcId);
		dest.writeInt(this.addmemoryCount);
		dest.writeInt(this.commentCount);
		dest.writeString(this.memoryDate);
		dest.writeString(this.memoryDateForShow);
		dest.writeString(this.photo1);
		dest.writeString(this.photo2);
		dest.writeString(this.photo3);
		dest.writeInt(this.photoCount);
		dest.writeInt(this.photoTotalCount);
		dest.writeInt(this.praiseCount);
		dest.writeString(this.title);
		dest.writeString(this.local);
		dest.writeString(this.userNameG);
		dest.writeInt(this.unReadMPointAddCnt);
		dest.writeInt(this.position);
		dest.writeByte(isHeader ? (byte) 1 : (byte) 0);
		dest.writeString(this.state);
		dest.writeByte(isLoaded ? (byte) 1 : (byte) 0);
		dest.writeTypedList(memoryList);
		dest.writeTypedList(pictureEntits);
	}

	protected GroupMemory(Parcel in) {
		super(in);
		this.userIdG = in.readString();
		this.headPhotoG = in.readString();
		this.memoryId = in.readString();
		this.memorySrcId = in.readString();
		this.addmemoryCount = in.readInt();
		this.commentCount = in.readInt();
		this.memoryDate = in.readString();
		this.memoryDateForShow = in.readString();
		this.photo1 = in.readString();
		this.photo2 = in.readString();
		this.photo3 = in.readString();
		this.photoCount = in.readInt();
		this.photoTotalCount = in.readInt();
		this.praiseCount = in.readInt();
		this.title = in.readString();
		this.local = in.readString();
		this.userNameG = in.readString();
		this.unReadMPointAddCnt = in.readInt();
		this.position = in.readInt();
		this.isHeader = in.readByte() != 0;
		this.state = in.readString();
		this.isLoaded = in.readByte() != 0;
		this.memoryList = in.createTypedArrayList(GroupMemory.CREATOR);
		this.pictureEntits = in.createTypedArrayList(PhotoInfo.CREATOR);
	}

	public static final Creator<GroupMemory> CREATOR = new Creator<GroupMemory>() {
		public GroupMemory createFromParcel(Parcel source) {
			return new GroupMemory(source);
		}

		public GroupMemory[] newArray(int size) {
			return new GroupMemory[size];
		}
	};
}
