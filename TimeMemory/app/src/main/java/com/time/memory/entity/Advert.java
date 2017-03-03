package com.time.memory.entity;

import android.os.Parcel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:广告
 * @date 2016/11/5 13:50
 */
@Entity(nameInDb = "advert")
public class Advert extends BaseEntity {

	@Property(nameInDb = "id")
	private Long id;

	@Property(nameInDb = "advertizeExplain")
	private String advertizeExplain; //广告说明

	@Property(nameInDb = "showType")
	private String showType;//广告类型（1：滚动 2：固定）

	@Property(nameInDb = "sort")
	private Byte sort; //排列序

	@Property(nameInDb = "imgPath")
	private String imgPath;//图片  OSS url

	@Property(nameInDb = "backgroundImgPath")
	private String backgroundImgPath; // 背景图片

	@Property(nameInDb = "linkUrl")
	private String linkUrl;//超链接

	@Transient
	private ArrayList<Advert> advertizes;

	public void setAdvert(Advert advert) {
		imgPath = advert.getImgPath();
		linkUrl = advert.getLinkUrl();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAdvertizeExplain() {
		return advertizeExplain;
	}

	public void setAdvertizeExplain(String advertizeExplain) {
		this.advertizeExplain = advertizeExplain;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public Byte getSort() {
		return sort;
	}

	public void setSort(Byte sort) {
		this.sort = sort;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getBackgroundImgPath() {
		return backgroundImgPath;
	}

	public void setBackgroundImgPath(String backgroundImgPath) {
		this.backgroundImgPath = backgroundImgPath;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public ArrayList<Advert> getAdvertizes() {
		return advertizes;
	}

	public void setAdvertizes(ArrayList<Advert> advertizes) {
		this.advertizes = advertizes;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeValue(this.id);
		dest.writeString(this.advertizeExplain);
		dest.writeString(this.showType);
		dest.writeValue(this.sort);
		dest.writeString(this.imgPath);
		dest.writeString(this.backgroundImgPath);
		dest.writeString(this.linkUrl);
		dest.writeTypedList(advertizes);
	}

	public Advert() {
	}

	protected Advert(Parcel in) {
		super(in);
		this.id = (Long) in.readValue(Long.class.getClassLoader());
		this.advertizeExplain = in.readString();
		this.showType = in.readString();
		this.sort = (Byte) in.readValue(Byte.class.getClassLoader());
		this.imgPath = in.readString();
		this.backgroundImgPath = in.readString();
		this.linkUrl = in.readString();
		this.advertizes = in.createTypedArrayList(Advert.CREATOR);
	}

	@Generated(hash = 535521404)
	public Advert(Long id, String advertizeExplain, String showType, Byte sort,
			String imgPath, String backgroundImgPath, String linkUrl) {
		this.id = id;
		this.advertizeExplain = advertizeExplain;
		this.showType = showType;
		this.sort = sort;
		this.imgPath = imgPath;
		this.backgroundImgPath = backgroundImgPath;
		this.linkUrl = linkUrl;
	}

	public static final Creator<Advert> CREATOR = new Creator<Advert>() {
		public Advert createFromParcel(Parcel source) {
			return new Advert(source);
		}

		public Advert[] newArray(int size) {
			return new Advert[size];
		}
	};
}
