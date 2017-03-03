package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Qiu
 * @version V1.0
 * @Description:临时记忆表数据
 * @date 2016/11/2 11:21
 */
@Entity(nameInDb = "tempmemory")
public class TempMemory extends BaseEntity {

	/**
	 * 记忆Id
	 */
	@Id
	@Property(nameInDb = "memoryId")
	private String memoryId;

	@Transient
	private String memoryIdSource;

	/**
	 * 用户Id
	 */
	@Property(nameInDb = "userId")
	private String userId;
	/**
	 * 更新时间
	 */
	@Property(nameInDb = "updateForshowDate")
	private String updateForshowDate;
	/**
	 * 用户名
	 */
	@Property(nameInDb = "userName")
	private String userName;
	/**
	 * title
	 */
	@Property(nameInDb = "title")
	private String title;
	/**
	 * 图片数
	 */
	@Property(nameInDb = "piccount")
	private String piccount;
	/**
	 * 点赞数
	 */
	@Property(nameInDb = "forkcount")
	private String forkcount;
	/**
	 * 评论数
	 */
	@Property(nameInDb = "commentcount")
	private String commentcount;
	/**
	 * 追加数
	 */
	@Property(nameInDb = "addcount")
	private String addcount;
	/**
	 * 图片1
	 */
	@Property(nameInDb = "photo1")
	private String photo1;
	/**
	 * 图片2
	 */
	@Property(nameInDb = "photo2")
	private String photo2;
	/**
	 * 图片3
	 */
	@Property(nameInDb = "photo3")
	private String photo3;
	/**
	 * 图片4
	 */
	@Transient
	private String photo4;
	/**
	 * 图片5
	 */
	@Transient
	private String photo5;
	/**
	 * 图片6
	 */
	@Transient
	private String photo6;
	/**
	 * 描述
	 */
	@Transient
	private String desc;
	/**
	 * 创建日期
	 */
	@Transient
	private String insDateForShow;
	/**
	 * 地址
	 */
	@Transient
	private String local;
	/**
	 * 片段Id
	 */
	@Transient
	private String memoryPointId;
	/**
	 * 类别(1:我的；0:他的；2：群的)
	 */
	@Property(nameInDb = "type")
	private String type;
	/**
	 * 记忆拥有者
	 */
	@Transient
	private String memoryOwner;
	/**
	 * 群Id
	 */
	@Property(nameInDb = "groupId")
	private String groupId;


	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUpdateForshowDate() {
		return updateForshowDate;
	}

	public void setUpdateForshowDate(String updateForshowDate) {
		this.updateForshowDate = updateForshowDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPiccount() {
		return piccount;
	}

	public void setPiccount(String piccount) {
		this.piccount = piccount;
	}

	public String getForkcount() {
		return forkcount;
	}

	public void setForkcount(String forkcount) {
		this.forkcount = forkcount;
	}

	public String getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	public String getAddcount() {
		return addcount;
	}

	public void setAddcount(String addcount) {
		this.addcount = addcount;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

	public String getPhoto4() {
		return photo4;
	}

	public void setPhoto4(String photo4) {
		this.photo4 = photo4;
	}

	public String getPhoto5() {
		return photo5;
	}

	public void setPhoto5(String photo5) {
		this.photo5 = photo5;
	}

	public String getPhoto6() {
		return photo6;
	}

	public void setPhoto6(String photo6) {
		this.photo6 = photo6;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getInsDateForShow() {
		return insDateForShow;
	}

	public void setInsDateForShow(String insDateForShow) {
		this.insDateForShow = insDateForShow;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getMemoryPointId() {
		return memoryPointId;
	}

	public void setMemoryPointId(String memoryPointId) {
		this.memoryPointId = memoryPointId;
	}

	public String getMemoryOwner() {
		return memoryOwner;
	}

	public void setMemoryOwner(String memoryOwner) {
		this.memoryOwner = memoryOwner;
	}

	public String getMemoryIdSource() {
		return memoryIdSource;
	}

	public void setMemoryIdSource(String memoryIdSource) {
		this.memoryIdSource = memoryIdSource;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.memoryId);
		dest.writeString(this.memoryIdSource);
		dest.writeString(this.userId);
		dest.writeString(this.updateForshowDate);
		dest.writeString(this.userName);
		dest.writeString(this.title);
		dest.writeString(this.piccount);
		dest.writeString(this.forkcount);
		dest.writeString(this.commentcount);
		dest.writeString(this.addcount);
		dest.writeString(this.photo1);
		dest.writeString(this.photo2);
		dest.writeString(this.photo3);
		dest.writeString(this.photo4);
		dest.writeString(this.photo5);
		dest.writeString(this.photo6);
		dest.writeString(this.desc);
		dest.writeString(this.insDateForShow);
		dest.writeString(this.local);
		dest.writeString(this.memoryPointId);
		dest.writeString(this.type);
		dest.writeString(this.memoryOwner);
		dest.writeString(this.groupId);
	}

	public TempMemory() {
	}

	protected TempMemory(Parcel in) {
		super(in);
		this.memoryId = in.readString();
		this.memoryIdSource = in.readString();
		this.userId = in.readString();
		this.updateForshowDate = in.readString();
		this.userName = in.readString();
		this.title = in.readString();
		this.piccount = in.readString();
		this.forkcount = in.readString();
		this.commentcount = in.readString();
		this.addcount = in.readString();
		this.photo1 = in.readString();
		this.photo2 = in.readString();
		this.photo3 = in.readString();
		this.photo4 = in.readString();
		this.photo5 = in.readString();
		this.photo6 = in.readString();
		this.desc = in.readString();
		this.insDateForShow = in.readString();
		this.local = in.readString();
		this.memoryPointId = in.readString();
		this.type = in.readString();
		this.memoryOwner = in.readString();
		this.groupId = in.readString();
	}

	@Generated(hash = 1038472530)
	public TempMemory(String memoryId, String userId, String updateForshowDate,
			String userName, String title, String piccount, String forkcount,
			String commentcount, String addcount, String photo1, String photo2,
			String photo3, String type, String groupId) {
		this.memoryId = memoryId;
		this.userId = userId;
		this.updateForshowDate = updateForshowDate;
		this.userName = userName;
		this.title = title;
		this.piccount = piccount;
		this.forkcount = forkcount;
		this.commentcount = commentcount;
		this.addcount = addcount;
		this.photo1 = photo1;
		this.photo2 = photo2;
		this.photo3 = photo3;
		this.type = type;
		this.groupId = groupId;
	}

	public static final Creator<TempMemory> CREATOR = new Creator<TempMemory>() {
		public TempMemory createFromParcel(Parcel source) {
			return new TempMemory(source);
		}

		public TempMemory[] newArray(int size) {
			return new TempMemory[size];
		}
	};
}
