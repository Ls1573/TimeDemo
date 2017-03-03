package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:版本更新
 * @date 2016/11/28 11:50
 */
public class Upgrade extends BaseEntity {
	/**
	 * 版本号(1.0.0)
	 */
	private String android;
	/**
	 * 版本号(>1.0.0)
	 */
	private String androidVersion;
	/**
	 * 强制更新版本
	 */
	private String androidFlag;

	/**
	 * 下载链接
	 */
	private String android_download_link;

	/**
	 * 是否强制更新 (1:强制 )
	 */
	private String android_force_update;

	/**
	 * 更新版本描述
	 */
	private String android_update_message;

	private Upgrade result;

	public String getAndroid() {
		return android;
	}

	public void setAndroid(String android) {
		this.android = android;
	}

	public String getAndroid_download_link() {
		return android_download_link;
	}

	public void setAndroid_download_link(String android_download_link) {
		this.android_download_link = android_download_link;
	}

	public String getAndroid_force_update() {
		return android_force_update;
	}

	public void setAndroid_force_update(String android_force_update) {
		this.android_force_update = android_force_update;
	}

	public String getAndroid_update_message() {
		return android_update_message;
	}

	public void setAndroid_update_message(String android_update_message) {
		this.android_update_message = android_update_message;
	}

	public Upgrade getResult() {
		return result;
	}

	public void setResult(Upgrade result) {
		this.result = result;
	}

	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public String getAndroidFlag() {
		return androidFlag;
	}

	public void setAndroidFlag(String androidFlag) {
		this.androidFlag = androidFlag;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.android);
		dest.writeString(this.androidVersion);
		dest.writeString(this.androidFlag);
		dest.writeString(this.android_download_link);
		dest.writeString(this.android_force_update);
		dest.writeString(this.android_update_message);
		dest.writeParcelable(this.result, 0);
	}

	public Upgrade() {
	}

	protected Upgrade(Parcel in) {
		super(in);
		this.android = in.readString();
		this.androidVersion = in.readString();
		this.androidFlag = in.readString();
		this.android_download_link = in.readString();
		this.android_force_update = in.readString();
		this.android_update_message = in.readString();
		this.result = in.readParcelable(Upgrade.class.getClassLoader());
	}

	public static final Creator<Upgrade> CREATOR = new Creator<Upgrade>() {
		public Upgrade createFromParcel(Parcel source) {
			return new Upgrade(source);
		}

		public Upgrade[] newArray(int size) {
			return new Upgrade[size];
		}
	};
}
