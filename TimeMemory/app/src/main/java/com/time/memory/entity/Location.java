package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:定位
 * @date 2016/11/15 14:29
 */
public class Location extends BaseEntity {

	//类型(1:定位完成;2:停止定位;3开始定位)
	private int state;
	//经    度
	private double longitude;
	//维度
	private double latitude;
	//定位类型
	private int locationtype;
	//城市
	private String city;
	//地址
	private String address;
	//兴趣点
	private String poiName;
	//城市码
	private String cityCode;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getLocationtype() {
		return locationtype;
	}

	public void setLocationtype(int locationtype) {
		this.locationtype = locationtype;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(this.state);
		dest.writeDouble(this.longitude);
		dest.writeDouble(this.latitude);
		dest.writeInt(this.locationtype);
		dest.writeString(this.city);
		dest.writeString(this.address);
		dest.writeString(this.poiName);
		dest.writeString(this.cityCode);
	}

	public Location() {
	}

	protected Location(Parcel in) {
		super(in);
		this.state = in.readInt();
		this.longitude = in.readDouble();
		this.latitude = in.readDouble();
		this.locationtype = in.readInt();
		this.city = in.readString();
		this.address = in.readString();
		this.poiName = in.readString();
		this.cityCode = in.readString();
	}

	public static final Creator<Location> CREATOR = new Creator<Location>() {
		public Location createFromParcel(Parcel source) {
			return new Location(source);
		}

		public Location[] newArray(int size) {
			return new Location[size];
		}
	};
}
