package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:点赞
 * @date 2016/10/27 11:56
 */
public class Praise extends BaseEntity {

	/**
	 * 点赞时间
	 */
	private String insdForShow;

	/**
	 * 点赞Id
	 */
	private String pid;

	/**
	 * 点赞人Id
	 */
	private String uid;

	/**
	 * 点赞人名字
	 */
	private String uname;

	/**
	 * 点赞人图片
	 */
	private String uhp;

	/**
	 * 是否是好友(0:true;1:false)
	 */
	private String isFriendFlg;

	public Praise(String uid, String uname, String uhp) {
		this.uid = uid;
		this.uname = uname;
		this.uhp = uhp;
	}


	/**
	 * 获取截取名
	 *
	 * @return
	 */
	public String getSubName() {
		String name = uname;
		if (name == null) {
			name = "#";
		} else if (uname.length() > 2) {
			name = uname.substring(uname.length() - 2);
		}
		return name;
	}


	public String getInsdForShow() {
		return insdForShow;
	}

	public void setInsdForShow(String insdForShow) {
		this.insdForShow = insdForShow;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUhp() {
		return uhp;
	}

	public void setUhp(String uhp) {
		this.uhp = uhp;
	}

	public String getIsFriendFlg() {
		return isFriendFlg;
	}

	public void setIsFriendFlg(String isFriendFlg) {
		this.isFriendFlg = isFriendFlg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.insdForShow);
		dest.writeString(this.pid);
		dest.writeString(this.uid);
		dest.writeString(this.uname);
		dest.writeString(this.uhp);
		dest.writeString(this.isFriendFlg);
	}

	protected Praise(Parcel in) {
		super(in);
		this.insdForShow = in.readString();
		this.pid = in.readString();
		this.uid = in.readString();
		this.uname = in.readString();
		this.uhp = in.readString();
		this.isFriendFlg = in.readString();
	}

	public static final Creator<Praise> CREATOR = new Creator<Praise>() {
		public Praise createFromParcel(Parcel source) {
			return new Praise(source);
		}

		public Praise[] newArray(int size) {
			return new Praise[size];
		}
	};
}
