package com.time.memory.entity;

import android.os.Parcel;


import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:记忆
 * @date 2016-9-6下午2:51:15
 * ==============================
 */
public class Memory extends BaseEntity {

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 头像
	 */
	private String netPath;

	/**
	 * Id
	 */
	private String memoryId;

	/**
	 * memoryIdSource
	 */
	private String memoryIdSource;

	/**
	 * 记忆源ID
	 */
	private String memorySrcId;
	/**
	 * Id
	 */
	private String memoryPointId;
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
	 * 记忆详细内容
	 */
	private String detail;

	/**
	 * 用户名
	 */
	private String userName;


	/**
	 * 追加记忆数
	 */
	private String addPointCnt;

	/**
	 * 追加记忆数
	 */
	private int unReadMPointAddCnt;


	/**
	 * lableId
	 */
	private String labelId;

	/**
	 * lableName
	 */
	private String labelName;

	/**
	 * 是否点赞Flag(0:true;1:false)
	 */
	private String mpFlag;

	/**
	 * 未读记忆数
	 */
	private int unReadMemoryCnt;

	/**
	 * 地址
	 */
	private String local;

	/**
	 * 状态来源
	 */
	private String stateForShow;

	/**
	 * 是不是头
	 */
	private boolean isHeader;

	/**
	 * 0：公开，1：私密 2：圈子
	 */
	private String state;

	/**
	 * 记忆群Id
	 */
	private String groupId;

	/**
	 */
	private int position;

	private boolean isLoaded;

	private String groupName;
	/**
	 * 圈子名
	 */
	private ArrayList<String> groupNameList;

	private ArrayList<Memory> memoryList;

	private ArrayList<MemoryGroup> memoryGroups;

	private List<PhotoInfo> pictureEntits;

	public Memory(String memoryId, String memorySrcId, String userId, String title) {
		this.memoryId = memoryId;
		this.memorySrcId = memorySrcId;
		this.userId = userId;
		this.title = title;
	}

	public Memory(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public Memory() {
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getStateForShow() {
		return stateForShow;
	}

	public void setStateForShow(String stateForShow) {
		this.stateForShow = stateForShow;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNetPath() {
		return netPath;
	}

	public void setNetPath(String netPath) {
		this.netPath = netPath;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ArrayList<Memory> getMemoryList() {
		return memoryList;
	}

	public void setMemoryList(ArrayList<Memory> memoryList) {
		this.memoryList = memoryList;
	}

	public List<PhotoInfo> getPictureEntits() {
		return pictureEntits;
	}

	public void setPictureEntits(List<PhotoInfo> pictureEntits) {
		this.pictureEntits = pictureEntits;
	}

	public String getAddPointCnt() {
		return addPointCnt;
	}

	public void setAddPointCnt(String addPointCnt) {
		this.addPointCnt = addPointCnt;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getMpFlag() {
		return mpFlag;
	}

	public void setMpFlag(String mpFlag) {
		this.mpFlag = mpFlag;
	}

	public String getMemoryPointId() {
		return memoryPointId;
	}

	public void setMemoryPointId(String memoryPointId) {
		this.memoryPointId = memoryPointId;
	}

	public int getUnReadMemoryCnt() {
		return unReadMemoryCnt;
	}

	public void setUnReadMemoryCnt(int unReadMemoryCnt) {
		this.unReadMemoryCnt = unReadMemoryCnt;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setIsLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public int getPhotoTotalCount() {
		return photoTotalCount;
	}

	public void setPhotoTotalCount(int photoTotalCount) {
		this.photoTotalCount = photoTotalCount;
	}

	public String getMemorySrcId() {
		return memorySrcId;
	}

	public void setMemorySrcId(String memorySrcId) {
		this.memorySrcId = memorySrcId;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<String> getGroupNameList() {
		return groupNameList;
	}

	public void setGroupNameList(ArrayList<String> groupNameList) {
		this.groupNameList = groupNameList;
	}

	public ArrayList<MemoryGroup> getMemoryGroups() {
		return memoryGroups;
	}

	public void setMemoryGroups(ArrayList<MemoryGroup> memoryGroups) {
		this.memoryGroups = memoryGroups;
	}

	public String getMemoryIdSource() {
		return memoryIdSource;
	}

	public void setMemoryIdSource(String memoryIdSource) {
		this.memoryIdSource = memoryIdSource;
	}

	public int getUnReadMPointAddCnt() {
		return unReadMPointAddCnt;
	}

	public void setUnReadMPointAddCnt(int unReadMPointAddCnt) {
		this.unReadMPointAddCnt = unReadMPointAddCnt;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userId);
		dest.writeString(this.netPath);
		dest.writeString(this.memoryId);
		dest.writeString(this.memoryIdSource);
		dest.writeString(this.memorySrcId);
		dest.writeString(this.memoryPointId);
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
		dest.writeString(this.detail);
		dest.writeString(this.userName);
		dest.writeString(this.addPointCnt);
		dest.writeInt(this.unReadMPointAddCnt);
		dest.writeString(this.labelId);
		dest.writeString(this.labelName);
		dest.writeString(this.mpFlag);
		dest.writeInt(this.unReadMemoryCnt);
		dest.writeString(this.local);
		dest.writeString(this.stateForShow);
		dest.writeByte(isHeader ? (byte) 1 : (byte) 0);
		dest.writeString(this.state);
		dest.writeString(this.groupId);
		dest.writeInt(this.position);
		dest.writeByte(isLoaded ? (byte) 1 : (byte) 0);
		dest.writeString(this.groupName);
		dest.writeStringList(this.groupNameList);
		dest.writeTypedList(memoryList);
		dest.writeTypedList(memoryGroups);
		dest.writeTypedList(pictureEntits);
	}

	protected Memory(Parcel in) {
		super(in);
		this.userId = in.readString();
		this.netPath = in.readString();
		this.memoryId = in.readString();
		this.memoryIdSource = in.readString();
		this.memorySrcId = in.readString();
		this.memoryPointId = in.readString();
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
		this.detail = in.readString();
		this.userName = in.readString();
		this.addPointCnt = in.readString();
		this.unReadMPointAddCnt = in.readInt();
		this.labelId = in.readString();
		this.labelName = in.readString();
		this.mpFlag = in.readString();
		this.unReadMemoryCnt = in.readInt();
		this.local = in.readString();
		this.stateForShow = in.readString();
		this.isHeader = in.readByte() != 0;
		this.state = in.readString();
		this.groupId = in.readString();
		this.position = in.readInt();
		this.isLoaded = in.readByte() != 0;
		this.groupName = in.readString();
		this.groupNameList = in.createStringArrayList();
		this.memoryList = in.createTypedArrayList(Memory.CREATOR);
		this.memoryGroups = in.createTypedArrayList(MemoryGroup.CREATOR);
		this.pictureEntits = in.createTypedArrayList(PhotoInfo.CREATOR);
	}

	public static final Creator<Memory> CREATOR = new Creator<Memory>() {
		public Memory createFromParcel(Parcel source) {
			return new Memory(source);
		}

		public Memory[] newArray(int size) {
			return new Memory[size];
		}
	};
}
