package com.time.memory.entity;

import android.os.Parcel;
import android.text.TextUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:写记忆
 * @date 2016/10/21 16:06
 */
public class WriterMemory extends BaseEntity {
	/**
	 * 图片集
	 */
	private List<PhotoInfo> pictureEntits;

	/**
	 * 描述
	 */
	private String desc;


	/**
	 * 第一项
	 */
	private boolean isFirst;


	/**
	 * 最后一项
	 */
	private boolean isLast;

	/**
	 * 时间
	 */
	private String date;

	/**
	 * 地址
	 */
	private String address;

	private double latitude;//维度
	private double longitude;//经度

	/**
	 * 当前数量指示
	 */
	private String num;


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		if (TextUtils.isEmpty(date)) {
			this.date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
			return;
		}
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy年MM月dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.date = new SimpleDateFormat("yyyy.MM.dd").format(parse);
	}

	public void setNewDate(String date) {
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.date = new SimpleDateFormat("yyyy.MM.dd").format(parse);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
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

	public void setPictureEntits(List<PhotoInfo> pictureEntits) {
		this.pictureEntits = pictureEntits;
	}

	public List<PhotoInfo> getPictureEntits() {
		return this.pictureEntits;
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


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedList(this.pictureEntits);
		dest.writeString(this.desc);
		dest.writeByte(this.isFirst ? (byte) 1 : (byte) 0);
		dest.writeByte(this.isLast ? (byte) 1 : (byte) 0);
		dest.writeString(this.date);
		dest.writeString(this.address);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeString(this.num);
	}

	public WriterMemory() {
	}

	protected WriterMemory(Parcel in) {
		super(in);
		this.pictureEntits = in.createTypedArrayList(PhotoInfo.CREATOR);
		this.desc = in.readString();
		this.isFirst = in.readByte() != 0;
		this.isLast = in.readByte() != 0;
		this.date = in.readString();
		this.address = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.num = in.readString();
	}

	public static final Creator<WriterMemory> CREATOR = new Creator<WriterMemory>() {
		@Override
		public WriterMemory createFromParcel(Parcel source) {
			return new WriterMemory(source);
		}

		@Override
		public WriterMemory[] newArray(int size) {
			return new WriterMemory[size];
		}
	};
}
