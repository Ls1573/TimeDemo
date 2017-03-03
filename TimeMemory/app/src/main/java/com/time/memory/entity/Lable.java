package com.time.memory.entity;

import android.os.Parcel;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:标签
 * @date 2016/10/24 8:32
 */
public class Lable extends BaseEntity {
	private String labelId;
	private String labelName;

	private List<Lable> labelVoList;

	private List<Lable> groupLabelVoList;

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public List<Lable> getLabelVoList() {
		return labelVoList;
	}

	public void setLabelVoList(List<Lable> labelVoList) {
		this.labelVoList = labelVoList;
	}

	public List<Lable> getGroupLabelVoList() {
		return groupLabelVoList;
	}

	public void setGroupLabelVoList(List<Lable> groupLabelVoList) {
		this.groupLabelVoList = groupLabelVoList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(this.labelId);
		dest.writeString(this.labelName);
		dest.writeTypedList(labelVoList);
		dest.writeTypedList(groupLabelVoList);
	}

	public Lable() {
	}

	public Lable(String labelId, String labelName) {
		this.labelId = labelId;
		this.labelName = labelName;
	}

	protected Lable(Parcel in) {
		super(in);
		this.labelId = in.readString();
		this.labelName = in.readString();
		this.labelVoList = in.createTypedArrayList(Lable.CREATOR);
		this.groupLabelVoList = in.createTypedArrayList(Lable.CREATOR);
	}

	public static final Creator<Lable> CREATOR = new Creator<Lable>() {
		public Lable createFromParcel(Parcel source) {
			return new Lable(source);
		}

		public Lable[] newArray(int size) {
			return new Lable[size];
		}
	};
}
