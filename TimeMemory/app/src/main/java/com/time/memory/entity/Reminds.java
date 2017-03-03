package com.time.memory.entity;

import android.os.Parcel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class Reminds extends BaseEntity {
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 记忆ID
	 */
	private String memoryId;
	/**
	 * 记忆ID
	 */
	private String memorySrcId;
	/**
	 * 未使用
	 */
	private String pointId;
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用于计算的时间
	 */
	private String insDate;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 头像
	 */
	private String userHphoto;
	/**
	 * 时间
	 */
	private String insDateForShow;

	private ArrayList<Reminds> reminds;
	private List<PhotoInfo> pictureEntits;

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

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserHphoto() {
		return userHphoto;
	}

	public void setUserHphoto(String userHphoto) {
		this.userHphoto = userHphoto;
	}

	public String getInsDateForShow() {
		return insDateForShow;
	}

	public void setInsDateForShow(String insDateForShow) {
		this.insDateForShow = insDateForShow;
	}

	public List<PhotoInfo> getPictureEntits() {
		return pictureEntits;
	}

	public void setPictureEntits(List<PhotoInfo> pictureEntits) {
		this.pictureEntits = pictureEntits;
	}

	public ArrayList<Reminds> getReminds() {
		return reminds;
	}

	public void setReminds(ArrayList<Reminds> reminds) {
		this.reminds = reminds;
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
		dest.writeString(this.pointId);
		dest.writeString(this.userId);
		dest.writeString(this.insDate);
		dest.writeString(this.userName);
		dest.writeString(this.userHphoto);
		dest.writeString(this.insDateForShow);
		dest.writeTypedList(reminds);
		dest.writeTypedList(pictureEntits);
	}

	public Reminds() {
	}

	protected Reminds(Parcel in) {
		super(in);
		this.title = in.readString();
		this.memoryId = in.readString();
		this.memorySrcId = in.readString();
		this.pointId = in.readString();
		this.userId = in.readString();
		this.insDate = in.readString();
		this.userName = in.readString();
		this.userHphoto = in.readString();
		this.insDateForShow = in.readString();
		this.reminds = in.createTypedArrayList(Reminds.CREATOR);
		this.pictureEntits = in.createTypedArrayList(PhotoInfo.CREATOR);
	}

	public static final Creator<Reminds> CREATOR = new Creator<Reminds>() {
		public Reminds createFromParcel(Parcel source) {
			return new Reminds(source);
		}

		public Reminds[] newArray(int size) {
			return new Reminds[size];
		}
	};
}
