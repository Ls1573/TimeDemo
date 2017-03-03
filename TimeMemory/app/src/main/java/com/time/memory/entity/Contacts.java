package com.time.memory.entity;

import android.net.Uri;
import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:通讯录
 * @date 2016-9-9下午5:20:01
 * ==============================
 */
@Entity(nameInDb = "contacts")
public class Contacts extends BaseEntity {
	/**
	 * 联系人ID
	 */
	@Id
	@Property(nameInDb = "Id")
	private String userId;
	/**
	 * 联系人名称
	 */
	@Property(nameInDb = "contactName")
	private String contactName;
	/**
	 * 得到联系人头像Uri
	 */
	@Transient
	private Uri picUri;
	/**
	 * 头像
	 */
	@Property(nameInDb = "headPhoto")
	private String headPhoto;
	/**
	 * 得到联系人手机号
	 */
	@Property(nameInDb = "phone")
	private String phone;
	/**
	 * 得到联系人email
	 */
	@Property(nameInDb = "email")
	private String email;
	/**
	 * 得到联系人昵称
	 */
	@Property(nameInDb = "nickName")
	private String userName;
	/***
	 * 姓名对应的拼音
	 */
	@Property(nameInDb = "pinyin")
	private String pinyin;
	/**
	 * 首字母
	 */
	@Property(nameInDb = "fLetter")
	private String fLetter;
	/**
	 * 被激活
	 */
	@Property(nameInDb = "isActived")
	private String activeFlg;
	/**
	 * 关联用户Id
	 */
	@Property(nameInDb = "userId")
	private String toUserId;
	/**
	 * 是否双向好友(0：是  1：否)
	 */
//	@Property(nameInDb = "isTwoWayFlg")
	private String isTwoWayFlg;
	/**
	 * 被选中
	 */
	@Transient
	private boolean isCheck;
	/**
	 * 是否是好友
	 */
	@Transient
	private String isFriend;

	/**
	 * 获取截取名
	 *
	 * @return
	 */
	public String getSubName() {
		String name = contactName;
		if (contactName.length() > 2)
			name = contactName.substring(contactName.length() - 2);
		return name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Uri getPicUri() {
		return picUri;
	}

	public void setPicUri(Uri picUri) {
		this.picUri = picUri;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}

	public String getIsTwoWayFlg() {
		return isTwoWayFlg;
	}

	public void setIsTwoWayFlg(String isTwoWayFlg) {
		this.isTwoWayFlg = isTwoWayFlg;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userId);
		dest.writeString(this.contactName);
		dest.writeParcelable(this.picUri, 0);
		dest.writeString(this.headPhoto);
		dest.writeString(this.phone);
		dest.writeString(this.email);
		dest.writeString(this.userName);
		dest.writeString(this.pinyin);
		dest.writeString(this.fLetter);
		dest.writeString(this.activeFlg);
		dest.writeString(this.toUserId);
		dest.writeString(this.isTwoWayFlg);
		dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
		dest.writeString(this.isFriend);
	}

	public String getFLetter() {
		return this.fLetter;
	}

	public void setFLetter(String fLetter) {
		this.fLetter = fLetter;
	}

	public Contacts() {
	}

	protected Contacts(Parcel in) {
		super(in);
		this.userId = in.readString();
		this.contactName = in.readString();
		this.picUri = in.readParcelable(Uri.class.getClassLoader());
		this.headPhoto = in.readString();
		this.phone = in.readString();
		this.email = in.readString();
		this.userName = in.readString();
		this.pinyin = in.readString();
		this.fLetter = in.readString();
		this.activeFlg = in.readString();
		this.toUserId = in.readString();
		this.isTwoWayFlg = in.readString();
		this.isCheck = in.readByte() != 0;
		this.isFriend = in.readString();
	}

	@Generated(hash = 827763386)
	public Contacts(String userId, String contactName, String headPhoto,
					String phone, String email, String userName, String pinyin, String fLetter,
					String activeFlg, String toUserId, String isTwoWayFlg) {
		this.userId = userId;
		this.contactName = contactName;
		this.headPhoto = headPhoto;
		this.phone = phone;
		this.email = email;
		this.userName = userName;
		this.pinyin = pinyin;
		this.fLetter = fLetter;
		this.activeFlg = activeFlg;
		this.toUserId = toUserId;
		this.isTwoWayFlg = isTwoWayFlg;
	}

	public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
		public Contacts createFromParcel(Parcel source) {
			return new Contacts(source);
		}

		public Contacts[] newArray(int size) {
			return new Contacts[size];
		}
	};
}
