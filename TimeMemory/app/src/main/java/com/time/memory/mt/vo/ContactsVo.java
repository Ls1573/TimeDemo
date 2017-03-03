package com.time.memory.mt.vo;

import android.os.Parcel;

import com.time.memory.entity.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 临时联系人
 */
@Entity(nameInDb = "contactsvo")
public class ContactsVo extends BaseEntity {

	/**
	 * 用户名称
	 */
	@Property(nameInDb = "userName")
	private String userName;

	/**
	 * 手机号码
	 */
	@Property(nameInDb = "userMobile")
	private String userMobile;

	@Transient
	private String userToken;

	@Transient
	private ArrayList<ContactsVo> contactsVoList;


	public ArrayList<ContactsVo> getContactsVoList() {
		return contactsVoList;
	}

	public void setContactsVoList(ArrayList<ContactsVo> contactsVoList) {
		this.contactsVoList = contactsVoList;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userName);
		dest.writeString(this.userMobile);
		dest.writeString(this.userToken);
		dest.writeTypedList(contactsVoList);
	}

	public ContactsVo() {
	}

	protected ContactsVo(Parcel in) {
		super(in);
		this.userName = in.readString();
		this.userMobile = in.readString();
		this.userToken = in.readString();
		this.contactsVoList = in.createTypedArrayList(ContactsVo.CREATOR);
	}

	@Generated(hash = 769363353)
	public ContactsVo(String userName, String userMobile) {
		this.userName = userName;
		this.userMobile = userMobile;
	}

	public static final Creator<ContactsVo> CREATOR = new Creator<ContactsVo>() {
		public ContactsVo createFromParcel(Parcel source) {
			return new ContactsVo(source);
		}

		public ContactsVo[] newArray(int size) {
			return new ContactsVo[size];
		}
	};
}