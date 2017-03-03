package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:新增联系人
 * @date 2016/12/2 13:37
 */
public class NewContacts extends BaseEntity {
	private String uid; //登录用户ID
	private String fid; //好友的ID
	private String fname;    //好友的名字
	private String fhp;        //好友的头像
	private String fmobile;    //好友的电话
	private String isTwoWayFlg;//双向好友标志位


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFhp() {
		return fhp;
	}

	public void setFhp(String fhp) {
		this.fhp = fhp;
	}

	public String getFmobile() {
		return fmobile;
	}

	public void setFmobile(String fmobile) {
		this.fmobile = fmobile;
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
		dest.writeString(this.uid);
		dest.writeString(this.fid);
		dest.writeString(this.fname);
		dest.writeString(this.fhp);
		dest.writeString(this.fmobile);
		dest.writeString(this.isTwoWayFlg);
	}

	public NewContacts() {
	}

	protected NewContacts(Parcel in) {
		super(in);
		this.uid = in.readString();
		this.fid = in.readString();
		this.fname = in.readString();
		this.fhp = in.readString();
		this.fmobile = in.readString();
		this.isTwoWayFlg = in.readString();
	}

	public static final Creator<NewContacts> CREATOR = new Creator<NewContacts>() {
		public NewContacts createFromParcel(Parcel source) {
			return new NewContacts(source);
		}

		public NewContacts[] newArray(int size) {
			return new NewContacts[size];
		}
	};
}
