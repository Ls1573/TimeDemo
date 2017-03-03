package com.time.memory.entity;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆详情
 * @date 2016/10/25 19:10
 */
public class MemoryInfo extends BaseEntity {

	private Memory memory;

	/**
	 * 片段
	 */
	private ArrayList<MemoryEdit> memoryPointVos;

	/**
	 * 我的片段(自己写的)
	 */
	private ArrayList<MemoryEdit> mPointInnerVos;
	/**
	 * 我的片段(追加的)
	 */
	private ArrayList<MemoryEdit> mPointOuterVos;


	/**
	 * 点赞
	 */
	private List<Praise> praiseVos;

	/**
	 * 评论
	 */
	private List<Comment> commentVos;

	/**
	 * 点赞
	 */
	private List<MemoryPraise> praiserVos;

	/**
	 * 评论
	 */
	private List<MemoryComment> commentorVos;
	/**
	 * 准备状态
	 */
	private boolean isRight;

	/**
	 * 点赞数
	 */
	private int praise;


	public void setMemoryInfo(MemoryInfo memoryInfo) {
		setMemory(memoryInfo.getMemory());
		setCommentorVos(memoryInfo.getCommentorVos());
//		setCommentVos(memoryInfo.getCommentVos());
		setMemoryPointVos(memoryInfo.getMemoryPointVos());
//		setPraiseVos(memoryInfo.getPraiseVos());
		setPraiserVos(memoryInfo.getPraiserVos());
		setIsRight(true);
		setPraise(memoryInfo.getPraise());

	}

	public void setNewMemoryInfo(MemoryInfo memoryInfo) {
		if (commentorVos == null) commentorVos = new ArrayList<>();
		commentorVos.clear();
		commentorVos.addAll(memoryInfo.getCommentorVos());
		if (memoryPointVos == null) memoryPointVos = new ArrayList<>();
		memoryPointVos.clear();
		memoryPointVos.addAll(memoryInfo.getMemoryPointVos());
		if (praiserVos == null) praiserVos = new ArrayList<>();
		praiserVos.clear();
		praiserVos.addAll(memoryInfo.getPraiserVos());

		setIsRight(true);
		setPraise(memoryInfo.getPraise());
		getMemory().setTitle(memoryInfo.getMemory().getTitle());
		getMemory().setMemoryDate(memoryInfo.getMemory().getMemoryDate());
		getMemory().setLabelName(memoryInfo.getMemory().getLabelName());
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public ArrayList<MemoryEdit> getMemoryPointVos() {
		return memoryPointVos;
	}

	public void setMemoryPointVos(ArrayList<MemoryEdit> memoryPointVos) {
		this.memoryPointVos = memoryPointVos;
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

	public List<MemoryComment> getCommentorVos() {
		return commentorVos;
	}

	public void setCommentorVos(List<MemoryComment> commentorVos) {
		this.commentorVos = commentorVos;
	}

	public List<MemoryPraise> getPraiserVos() {
		return praiserVos;
	}

	public void setPraiserVos(List<MemoryPraise> praiserVos) {
		this.praiserVos = praiserVos;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setIsRight(boolean isRight) {
		this.isRight = isRight;
	}

	public ArrayList<MemoryEdit> getmPointInnerVos() {
		return mPointInnerVos;
	}

	public void setmPointInnerVos(ArrayList<MemoryEdit> mPointInnerVos) {
		this.mPointInnerVos = mPointInnerVos;
	}

	public ArrayList<MemoryEdit> getmPointOuterVos() {
		return mPointOuterVos;
	}

	public void setmPointOuterVos(ArrayList<MemoryEdit> mPointOuterVos) {
		this.mPointOuterVos = mPointOuterVos;
	}

	public int getPraise() {
		return praise;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeParcelable(this.memory, 0);
		dest.writeTypedList(memoryPointVos);
		dest.writeTypedList(mPointInnerVos);
		dest.writeTypedList(mPointOuterVos);
		dest.writeTypedList(praiseVos);
		dest.writeTypedList(commentVos);
		dest.writeTypedList(praiserVos);
		dest.writeTypedList(commentorVos);
		dest.writeByte(isRight ? (byte) 1 : (byte) 0);
		dest.writeInt(this.praise);
	}

	public MemoryInfo() {
	}

	protected MemoryInfo(Parcel in) {
		super(in);
		this.memory = in.readParcelable(Memory.class.getClassLoader());
		this.memoryPointVos = in.createTypedArrayList(MemoryEdit.CREATOR);
		this.mPointInnerVos = in.createTypedArrayList(MemoryEdit.CREATOR);
		this.mPointOuterVos = in.createTypedArrayList(MemoryEdit.CREATOR);
		this.praiseVos = in.createTypedArrayList(Praise.CREATOR);
		this.commentVos = in.createTypedArrayList(Comment.CREATOR);
		this.praiserVos = in.createTypedArrayList(MemoryPraise.CREATOR);
		this.commentorVos = in.createTypedArrayList(MemoryComment.CREATOR);
		this.isRight = in.readByte() != 0;
		this.praise = in.readInt();
	}

	public static final Creator<MemoryInfo> CREATOR = new Creator<MemoryInfo>() {
		public MemoryInfo createFromParcel(Parcel source) {
			return new MemoryInfo(source);
		}

		public MemoryInfo[] newArray(int size) {
			return new MemoryInfo[size];
		}
	};
}
