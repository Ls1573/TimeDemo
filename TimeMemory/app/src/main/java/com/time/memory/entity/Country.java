package com.time.memory.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Country implements Parcelable {

	public String country;

	public String code;

	public Country(Parcel in) {
		country = in.readString();
		code = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(country);
		parcel.writeString(code);
	}
}
