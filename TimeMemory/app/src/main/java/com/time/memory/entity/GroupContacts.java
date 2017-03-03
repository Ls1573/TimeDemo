package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;

/**
 * @author Qiu
 * @version V1.0
 * @Description:圈子成员
 * @date 2016/10/14 13:55
 */
@Entity(nameInDb = "groupcontacts")
public class GroupContacts extends BaseEntity {

	/**
	 * 是否激活
	 */
	@Transient
	private String activeFlg;
	/**
	 * 用户Id
	 */
	@Property(nameInDb = "userId")
	private String userId;

	/**
	 * 用户名
	 */
	@Property(nameInDb = "userName")
	private String userName;

	/**
	 * 是否是好友
	 */
	@Transient
	private String isFriend;

	/***
	 * 姓名对应的拼音
	 */
	@Transient
	private String pinyin;
	/**
	 * 首字母
	 */
	@Transient
	private String fLetter;

	/**
	 * 是否被选中
	 */
	@Transient
	private boolean isCheck;

	@Transient
	private String userMobile;
	/**
	 * 当前操作状态(1:add;2:delete)
	 */
	@Transient
	private int state;
	/**
	 * 头像
	 */
	@Transient
	private String headPhoto;
	/**
	 * 集合
	 */
	@Transient
	private ArrayList<GroupContacts> userVoList;


	/**
	 * 记忆数
	 */
	@Transient
	private int addMemoryPointCount;
	/**
	 * 追加记忆数
	 */
	@Transient
	private int memoryCount;

	/**
	 * 获取截取名
	 *
	 * @return
	 */
	public String getSubName() {
		String name = userName;
		if (name == null) {
			name = "#";
		} else if (userName.length() > 2) {
			name = userName.substring(userName.length() - 2);
		}
		return name;
	}

	public GroupContacts(int state) {
		this.state = state;
	}

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getfLetter() {
		return fLetter;
	}

	public void setfLetter(String fLetter) {
		this.fLetter = fLetter;
	}

	public ArrayList<GroupContacts> getUserVoList() {
		return userVoList;
	}

	public void setUserVoList(ArrayList<GroupContacts> userVoList) {
		this.userVoList = userVoList;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getMemoryCount() {
		return memoryCount;
	}

	public void setMemoryCount(int memoryCount) {
		this.memoryCount = memoryCount;
	}

	public int getAddMemoryPointCount() {

		return addMemoryPointCount;
	}

	public void setAddMemoryPointCount(int addMemoryPointCount) {
		this.addMemoryPointCount = addMemoryPointCount;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.activeFlg);
		dest.writeString(this.userId);
		dest.writeString(this.userName);
		dest.writeString(this.isFriend);
		dest.writeString(this.pinyin);
		dest.writeString(this.fLetter);
		dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
		dest.writeString(this.userMobile);
		dest.writeInt(this.state);
		dest.writeString(this.headPhoto);
		dest.writeTypedList(userVoList);
		dest.writeInt(this.addMemoryPointCount);
		dest.writeInt(this.memoryCount);
	}

	protected GroupContacts(Parcel in) {
		super(in);
		this.activeFlg = in.readString();
		this.userId = in.readString();
		this.userName = in.readString();
		this.isFriend = in.readString();
		this.pinyin = in.readString();
		this.fLetter = in.readString();
		this.isCheck = in.readByte() != 0;
		this.userMobile = in.readString();
		this.state = in.readInt();
		this.headPhoto = in.readString();
		this.userVoList = in.createTypedArrayList(GroupContacts.CREATOR);
		this.addMemoryPointCount = in.readInt();
		this.memoryCount = in.readInt();
	}

	@Generated(hash = 1997072030)
	public GroupContacts(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	@Generated(hash = 37457956)
	public GroupContacts() {
	}

	public static final Creator<GroupContacts> CREATOR = new Creator<GroupContacts>() {
		public GroupContacts createFromParcel(Parcel source) {
			return new GroupContacts(source);
		}

		public GroupContacts[] newArray(int size) {
			return new GroupContacts[size];
		}
	};
}
