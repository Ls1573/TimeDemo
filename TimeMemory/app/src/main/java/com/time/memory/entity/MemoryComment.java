package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:评论
 * @date 2016/9/20 11:01
 */
public class MemoryComment extends BaseEntity {

	/**
	 * 评论Id
	 */
	private String cId;

	private String gId;

	/**
	 * 评论片段Id
	 */
	private String pId;

	/**
	 * 评论 Id
	 */
	private String uuid;

	/**
	 * 评论日期
	 */
	private String insdForShow;
	/**
	 * 记忆Id
	 */
	private String mId;
	/**
	 * 记忆源Id
	 */
	private String mpSrcId;
	/**
	 * 评论人Id
	 */
	private String uIdC;
	/**
	 * 记忆所有人Id
	 */
	private String uIdM;
	/**
	 * 评论内容
	 */
	private String tt;

	/**
	 * 评论人name
	 */
	private String unameC;


	/**
	 * 评论接受人name
	 */
	private String unameT;

	/**
	 * 被回复用户内容
	 */
	private String uIdT;
	/**
	 * 图片
	 */
	private String p1;

	/**
	 * 第一条
	 */
	private boolean isFirst;

	/**
	 * 最后一条
	 */
	private boolean isLast;

	/**
	 * 总数量
	 */
	private int sum;

	/**
	 * 评论人头像
	 */
	private String uhphotoC;

	/**
	 * 评论接收人ID
	 */
	private String commentToUserId;

	/**
	 * 评论接收人name
	 */
	private String commentToUserName;
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
	 * 是否是好友(0:true;1:false)
	 */
	private String isFriendFlg;

	public String getCommentToUserId() {
		return commentToUserId;
	}

	public void setCommentToUserId(String commentToUserId) {
		this.commentToUserId = commentToUserId;
	}

	public String getCommentToUserName() {
		return commentToUserName;
	}

	public void setCommentToUserName(String commentToUserName) {
		this.commentToUserName = commentToUserName;
	}

	public String getCid() {
		return cId;
	}

	public void setCid(String cid) {
		this.cId = cid;
	}

	public String getInsdForShow() {
		return insdForShow;
	}

	public void setInsdForShow(String insdForShow) {
		this.insdForShow = insdForShow;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getuIdC() {
		return uIdC;
	}

	public void setuIdC(String uIdC) {
		this.uIdC = uIdC;
	}

	public String getuIdM() {
		return uIdM;
	}

	public void setuIdM(String uIdM) {
		this.uIdM = uIdM;
	}

	public String getTt() {
		return tt;
	}

	public void setTt(String tt) {
		this.tt = tt;
	}

	public String getUnameC() {
		return unameC;
	}

	public void setUnameC(String unameC) {
		this.unameC = unameC;
	}

	public String getuIdT() {
		return uIdT;
	}

	public void setuIdT(String uIdT) {
		this.uIdT = uIdT;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public String getUhphotoC() {
		return uhphotoC;
	}

	public void setUhphotoC(String uhphotoC) {
		this.uhphotoC = uhphotoC;
	}

	public String getcId() {
		return cId;
	}


	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getUnameT() {
		return unameT;
	}

	public void setUnameT(String unameT) {
		this.unameT = unameT;
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

	public String getMpSrcId() {
		return mpSrcId;
	}

	public void setMpSrcId(String mpSrcId) {
		this.mpSrcId = mpSrcId;
	}

	public String getgId() {
		return gId;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.cId);
		dest.writeString(this.gId);
		dest.writeString(this.pId);
		dest.writeString(this.uuid);
		dest.writeString(this.insdForShow);
		dest.writeString(this.mId);
		dest.writeString(this.mpSrcId);
		dest.writeString(this.uIdC);
		dest.writeString(this.uIdM);
		dest.writeString(this.tt);
		dest.writeString(this.unameC);
		dest.writeString(this.unameT);
		dest.writeString(this.uIdT);
		dest.writeString(this.p1);
		dest.writeByte(isFirst ? (byte) 1 : (byte) 0);
		dest.writeByte(isLast ? (byte) 1 : (byte) 0);
		dest.writeInt(this.sum);
		dest.writeString(this.uhphotoC);
		dest.writeString(this.commentToUserId);
		dest.writeString(this.commentToUserName);
		dest.writeString(this.memoryIdSource);
		dest.writeString(this.memoryPointIdSource);
		dest.writeString(this.source);
		dest.writeString(this.isFriendFlg);
	}

	public MemoryComment() {
	}

	protected MemoryComment(Parcel in) {
		super(in);
		this.cId = in.readString();
		this.gId = in.readString();
		this.pId = in.readString();
		this.uuid = in.readString();
		this.insdForShow = in.readString();
		this.mId = in.readString();
		this.mpSrcId = in.readString();
		this.uIdC = in.readString();
		this.uIdM = in.readString();
		this.tt = in.readString();
		this.unameC = in.readString();
		this.unameT = in.readString();
		this.uIdT = in.readString();
		this.p1 = in.readString();
		this.isFirst = in.readByte() != 0;
		this.isLast = in.readByte() != 0;
		this.sum = in.readInt();
		this.uhphotoC = in.readString();
		this.commentToUserId = in.readString();
		this.commentToUserName = in.readString();
		this.memoryIdSource = in.readString();
		this.memoryPointIdSource = in.readString();
		this.source = in.readString();
		this.isFriendFlg = in.readString();
	}

	public static final Creator<MemoryComment> CREATOR = new Creator<MemoryComment>() {
		public MemoryComment createFromParcel(Parcel source) {
			return new MemoryComment(source);
		}

		public MemoryComment[] newArray(int size) {
			return new MemoryComment[size];
		}
	};
}
