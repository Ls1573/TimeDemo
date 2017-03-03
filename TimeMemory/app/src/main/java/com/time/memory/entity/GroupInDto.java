package com.time.memory.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:圈子删除操作
 * @date 2016/10/18 17:54
 */
public class GroupInDto implements Parcelable {

	private String userToken;
	private String groupId;
	private List<String> userIdList;

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.userToken);
		dest.writeString(this.groupId);
		dest.writeStringList(this.userIdList);
	}

	public GroupInDto() {
	}

	protected GroupInDto(Parcel in) {
		this.userToken = in.readString();
		this.groupId = in.readString();
		this.userIdList = in.createStringArrayList();
	}

	public static final Parcelable.Creator<GroupInDto> CREATOR = new Parcelable.Creator<GroupInDto>() {
		@Override
		public GroupInDto createFromParcel(Parcel source) {
			return new GroupInDto(source);
		}

		@Override
		public GroupInDto[] newArray(int size) {
			return new GroupInDto[size];
		}
	};
}
