package com.time.memory.entity;

import android.os.Parcel;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:群激活
 * @date 2016/10/19 18:10
 */
public class GroupAdDto extends BaseEntity {

	private String userToken;
	private List<String> groupIdList;

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public List<String> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<String> groupIdList) {
		this.groupIdList = groupIdList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.userToken);
		dest.writeStringList(this.groupIdList);
	}

	public GroupAdDto() {
	}

	protected GroupAdDto(Parcel in) {
		super(in);
		this.userToken = in.readString();
		this.groupIdList = in.createStringArrayList();
	}

	public static final Creator<GroupAdDto> CREATOR = new Creator<GroupAdDto>() {
		public GroupAdDto createFromParcel(Parcel source) {
			return new GroupAdDto(source);
		}

		public GroupAdDto[] newArray(int size) {
			return new GroupAdDto[size];
		}
	};
}
