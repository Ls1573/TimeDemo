package com.time.memory.entity;

import android.os.Parcel;

/**
 * @author Qiu
 * @version V1.0
 * @Description:下拉脚
 * @date 2016/10/22 16:14
 */
public class Footer extends BaseEntity {
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeInt(this.type);
	}

	public Footer() {
		this.type = 0;
	}

	protected Footer(Parcel in) {
		super(in);
		this.type = in.readInt();
	}

	public static final Creator<Footer> CREATOR = new Creator<Footer>() {
		@Override
		public Footer createFromParcel(Parcel source) {
			return new Footer(source);
		}

		@Override
		public Footer[] newArray(int size) {
			return new Footer[size];
		}
	};
}
