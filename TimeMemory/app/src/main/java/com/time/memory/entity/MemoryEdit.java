package com.time.memory.entity;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆片段详情信息
 * @date 2016/10/21 15:49
 */
public class MemoryEdit extends MemoryPointVo {

	/**
	 * 第一项
	 */
	private boolean isFirst;

	/**
	 * 最后一项
	 */
	private boolean isLast;
	/**
	 * 是否是追加的
	 */
	private String addFlag;

	/**
	 * 总追击记忆数
	 */
	private String sumAdd;

	/**
	 * 当前数量指示
	 */
	private String num;

	/**
	 * 哪个圈子追加的
	 */
	private String gName;

	private double latitude;//维度
	private double longitude;//经度

	private int deliverCnt;//如果大于1 被派发过，如果小于等于1 没有被派发过

	/**
	 * 图片集
	 */
	private ArrayList<PhotoInfo> photoInfos;


	/**
	 * 点赞
	 */
	private List<Praise> praiseVos;

	/**
	 * 评论
	 */
	private List<Comment> commentVos;

	/**
	 * 图片信息
	 *
	 * @return
	 */
	public ArrayList<PhotoInfo> getPhotoInfos() {
		return photoInfos;
	}

	public void setPhotoInfos(ArrayList<PhotoInfo> photoInfos) {
		this.photoInfos = photoInfos;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public List<Praise> getPraiseVos() {
		return praiseVos;
	}

	public void setPraiseVos(List<Praise> praiseVos) {
		this.praiseVos = praiseVos;
	}

	public List<Comment> getCommentVos() {
		return commentVos;
	}

	public void setCommentVos(List<Comment> commentVos) {
		this.commentVos = commentVos;
	}

	public String getAddFlag() {
		return addFlag;
	}

	public String getSumAdd() {
		return sumAdd;
	}

	public void setSumAdd(String sumAdd) {
		this.sumAdd = sumAdd;
	}

	public void setAddFlag(String addFlag) {
		this.addFlag = addFlag;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getDeliverCnt() {
		return deliverCnt;
	}

	public void setDeliverCnt(int deliverCnt) {
		this.deliverCnt = deliverCnt;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}


	@Override
	public String toString() {
		return "MemoryEdit{" +
				"deliverCnt=" + deliverCnt +
				", isFirst=" + isFirst +
				", isLast=" + isLast +
				", addFlag='" + addFlag + '\'' +
				", sumAdd='" + sumAdd + '\'' +
				", num='" + num + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", mpSrcId=" + getMemorySrcId() +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeByte(isFirst ? (byte) 1 : (byte) 0);
		dest.writeByte(isLast ? (byte) 1 : (byte) 0);
		dest.writeString(this.addFlag);
		dest.writeString(this.sumAdd);
		dest.writeString(this.num);
		dest.writeString(this.gName);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeInt(this.deliverCnt);
		dest.writeTypedList(photoInfos);
		dest.writeTypedList(praiseVos);
		dest.writeTypedList(commentVos);
	}

	public MemoryEdit() {
	}

	protected MemoryEdit(Parcel in) {
		super(in);
		this.isFirst = in.readByte() != 0;
		this.isLast = in.readByte() != 0;
		this.addFlag = in.readString();
		this.sumAdd = in.readString();
		this.num = in.readString();
		this.gName = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.deliverCnt = in.readInt();
		this.photoInfos = in.createTypedArrayList(PhotoInfo.CREATOR);
		this.praiseVos = in.createTypedArrayList(Praise.CREATOR);
		this.commentVos = in.createTypedArrayList(Comment.CREATOR);
	}

	public static final Creator<MemoryEdit> CREATOR = new Creator<MemoryEdit>() {
		public MemoryEdit createFromParcel(Parcel source) {
			return new MemoryEdit(source);
		}

		public MemoryEdit[] newArray(int size) {
			return new MemoryEdit[size];
		}
	};
}
