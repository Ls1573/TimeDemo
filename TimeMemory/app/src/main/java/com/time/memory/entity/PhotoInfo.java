/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.time.memory.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Desction:图片信息
 * Date:16/8/30 上午11:23
 */
@Entity
public class PhotoInfo implements Parcelable {

	@Id(autoincrement = true)
	private Long pId;
	private int photoD;
	private int bucketId;
	private String photoPath;
	private int width;
	private int height;
	private String date;

	private double latitude;//维度
	private double longitude;//经度
	private boolean isTitle;//是否是提示头
	private boolean isActicted;//是否被选中
	private boolean isAll;//全选

	@Transient
	private String curPoint;//当前对应的记忆片段
	@Transient
	private int curPointIndex;//当前对应的记忆片段-下标
	@Transient
	private int totalCount;//总图片数

	private int start;
	private int end;
	private int allNum;
	private boolean isClicked;//被选中

	private String paserTimeToYM(long time) {
		System.setProperty("user.timezone", "Asia/Shanghai");
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		return format.format(new Date(time * 1000L));
	}

	public void setDate(long date) {
		this.date = paserTimeToYM(date);
	}

	public PhotoInfo() {
	}


	public PhotoInfo(String photoPath) {
		this.photoPath = photoPath;
	}

	public PhotoInfo(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public PhotoInfo(boolean isTitle, String date) {
		this.isTitle = isTitle;
		this.date = date;
	}

	public PhotoInfo(boolean isTitle, String date, int start) {
		this.isTitle = isTitle;
		this.start = start;
		this.date = date;
	}

	public PhotoInfo(boolean isTitle, String date, int start, boolean isAll) {
		this.isTitle = isTitle;
		this.start = start;
		this.date = date;
		this.isAll = isAll;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public int getPhotoD() {
		return photoD;
	}

	public void setPhotoD(int photoD) {
		this.photoD = photoD;
	}

	public int getBucketId() {
		return bucketId;
	}

	public void setBucketId(int bucketId) {
		this.bucketId = bucketId;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public boolean isTitle() {
		return isTitle;
	}

	public void setIsTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public boolean isActicted() {
		return isActicted;
	}

	public void setIsActicted(boolean isActicted) {
		this.isActicted = isActicted;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setIsAll(boolean isAll) {
		this.isAll = isAll;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setIsClicked(boolean isClicked) {
		this.isClicked = isClicked;
	}


	public Long getPId() {
		return this.pId;
	}

	public void setPId(Long pId) {
		this.pId = pId;
	}

	public boolean getIsTitle() {
		return this.isTitle;
	}

	public boolean getIsActicted() {
		return this.isActicted;
	}

	public boolean getIsAll() {
		return this.isAll;
	}

	public boolean getIsClicked() {
		return this.isClicked;
	}

	public String getCurPoint() {
		return curPoint;
	}

	public void setCurPoint(String curPoint) {
		this.curPoint = curPoint;
	}

	public int getCurPointIndex() {
		return curPointIndex;
	}

	public void setCurPointIndex(int curPointIndex) {
		this.curPointIndex = curPointIndex;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.pId);
		dest.writeInt(this.photoD);
		dest.writeInt(this.bucketId);
		dest.writeString(this.photoPath);
		dest.writeInt(this.width);
		dest.writeInt(this.height);
		dest.writeString(this.date);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeByte(isTitle ? (byte) 1 : (byte) 0);
		dest.writeByte(isActicted ? (byte) 1 : (byte) 0);
		dest.writeByte(isAll ? (byte) 1 : (byte) 0);
		dest.writeString(this.curPoint);
		dest.writeInt(this.curPointIndex);
		dest.writeInt(this.totalCount);
		dest.writeInt(this.start);
		dest.writeInt(this.end);
		dest.writeInt(this.allNum);
		dest.writeByte(isClicked ? (byte) 1 : (byte) 0);
	}

	protected PhotoInfo(Parcel in) {
		this.pId = (Long) in.readValue(Long.class.getClassLoader());
		this.photoD = in.readInt();
		this.bucketId = in.readInt();
		this.photoPath = in.readString();
		this.width = in.readInt();
		this.height = in.readInt();
		this.date = in.readString();
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.isTitle = in.readByte() != 0;
		this.isActicted = in.readByte() != 0;
		this.isAll = in.readByte() != 0;
		this.curPoint = in.readString();
		this.curPointIndex = in.readInt();
		this.totalCount = in.readInt();
		this.start = in.readInt();
		this.end = in.readInt();
		this.allNum = in.readInt();
		this.isClicked = in.readByte() != 0;
	}

	@Generated(hash = 1603700414)
	public PhotoInfo(Long pId, int photoD, int bucketId, String photoPath,
					 int width, int height, String date, double latitude, double longitude,
					 boolean isTitle, boolean isActicted, boolean isAll, int start, int end,
					 int allNum, boolean isClicked) {
		this.pId = pId;
		this.photoD = photoD;
		this.bucketId = bucketId;
		this.photoPath = photoPath;
		this.width = width;
		this.height = height;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.isTitle = isTitle;
		this.isActicted = isActicted;
		this.isAll = isAll;
		this.start = start;
		this.end = end;
		this.allNum = allNum;
		this.isClicked = isClicked;
	}

	public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
		public PhotoInfo createFromParcel(Parcel source) {
			return new PhotoInfo(source);
		}

		public PhotoInfo[] newArray(int size) {
			return new PhotoInfo[size];
		}
	};
}

