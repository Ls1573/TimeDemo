package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:点赞
 * @date 2016/10/27 11:56
 */
public class MemoryPraise extends BaseEntity {

	/**
	 * 点赞人头像
	 */
	private String hphoto;

	/**
	 * 点赞记忆Id
	 */
	private String mId;

	/**
	 * 点赞人ID
	 */
	private String pUsrId;

	/**
	 * 点赞Id
	 */
	private String praiserUuid;

	/**
	 * 点赞人name
	 */
	private String uname;

	/**
	 * 是不是好友flag(1:false;0:true	)
	 */
	private String isFriendFlg;

	public MemoryPraise(String uid, String uname, String uhp) {
		this.pUsrId = uid;
		this.uname = uname;
		this.hphoto = uhp;
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

	public String getHphoto() {
		return hphoto;
	}

	public void setHphoto(String hphoto) {
		this.hphoto = hphoto;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getpUsrId() {
		return pUsrId;
	}

	public void setpUsrId(String pUsrId) {
		this.pUsrId = pUsrId;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPraiserUuid() {
		return praiserUuid;
	}

	public void setPraiserUuid(String praiserUuid) {
		this.praiserUuid = praiserUuid;
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
		dest.writeString(this.hphoto);
		dest.writeString(this.mId);
		dest.writeString(this.pUsrId);
		dest.writeString(this.praiserUuid);
		dest.writeString(this.uname);
		dest.writeString(this.isFriendFlg);
	}

	protected MemoryPraise(Parcel in) {
		super(in);
		this.hphoto = in.readString();
		this.mId = in.readString();
		this.pUsrId = in.readString();
		this.praiserUuid = in.readString();
		this.uname = in.readString();
		this.isFriendFlg = in.readString();
	}

	public static final Creator<MemoryPraise> CREATOR = new Creator<MemoryPraise>() {
		@Override
		public MemoryPraise createFromParcel(Parcel source) {
			return new MemoryPraise(source);
		}

		@Override
		public MemoryPraise[] newArray(int size) {
			return new MemoryPraise[size];
		}
	};
}
