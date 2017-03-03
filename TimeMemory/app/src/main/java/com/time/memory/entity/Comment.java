package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:评论
 * @date 2016/9/20 11:01
 */
public class Comment extends BaseEntity {

	/**
	 * 评论Id
	 */
	private String cid;

	/**
	 * 评论内容
	 */
	private String ct;

	/**
	 * 评论日期
	 */
	private String insdForShow;

	/**
	 * 评论用户Id
	 */
	private String u1id;

	/**
	 * 评论用户name
	 */
	private String u1name;
	/**
	 * 评论用户图片
	 */
	private String u1hp;

	/**
	 * 被回复用户Id
	 */
	private String u2id;

	/**
	 * 被回复用户名
	 */
	private String u2name;
	/**
	 * 记忆Id的源Id
	 */
	private String memoryIdSource;

	/**
	 * 记忆片段Id的源Id
	 */
	private String memoryPointIdSource;
	/**
	 * 1:我的记忆;2:其他书中
	 */
	private String source;

	/**
	 * 是不是好友(0:true;1:false)
	 */
	private String isFriendFlg;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getInsdForShow() {
		return insdForShow;
	}

	public void setInsdForShow(String insdForShow) {
		this.insdForShow = insdForShow;
	}

	public String getU1id() {
		return u1id;
	}

	public void setU1id(String u1id) {
		this.u1id = u1id;
	}

	public String getU1name() {
		return u1name;
	}

	public void setU1name(String u1name) {
		this.u1name = u1name;
	}

	public String getU1hp() {
		return u1hp;
	}

	public void setU1hp(String u1hp) {
		this.u1hp = u1hp;
	}

	public String getU2id() {
		return u2id;
	}

	public void setU2id(String u2id) {
		this.u2id = u2id;
	}

	public String getU2name() {
		return u2name;
	}

	public void setU2name(String u2name) {
		this.u2name = u2name;
	}

	public String getIsFriendFlg() {
		return isFriendFlg;
	}

	public void setIsFriendFlg(String isFriendFlg) {
		this.isFriendFlg = isFriendFlg;
	}

	public String getMemoryIdSource() {
		return memoryIdSource;
	}

	public void setMemoryIdSource(String memoryIdSource) {
		this.memoryIdSource = memoryIdSource;
	}

	public String getMemoryPointIdSource() {
		return memoryPointIdSource;
	}

	public void setMemoryPointIdSource(String memoryPointIdSource) {
		this.memoryPointIdSource = memoryPointIdSource;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.cid);
		dest.writeString(this.ct);
		dest.writeString(this.insdForShow);
		dest.writeString(this.u1id);
		dest.writeString(this.u1name);
		dest.writeString(this.u1hp);
		dest.writeString(this.u2id);
		dest.writeString(this.u2name);
		dest.writeString(this.memoryIdSource);
		dest.writeString(this.memoryPointIdSource);
		dest.writeString(this.source);
		dest.writeString(this.isFriendFlg);
	}

	public Comment() {
	}

	protected Comment(Parcel in) {
		super(in);
		this.cid = in.readString();
		this.ct = in.readString();
		this.insdForShow = in.readString();
		this.u1id = in.readString();
		this.u1name = in.readString();
		this.u1hp = in.readString();
		this.u2id = in.readString();
		this.u2name = in.readString();
		this.memoryIdSource = in.readString();
		this.memoryPointIdSource = in.readString();
		this.source = in.readString();
		this.isFriendFlg = in.readString();
	}

	public static final Creator<Comment> CREATOR = new Creator<Comment>() {
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}

		public Comment[] newArray(int size) {
			return new Comment[size];
		}
	};
}
