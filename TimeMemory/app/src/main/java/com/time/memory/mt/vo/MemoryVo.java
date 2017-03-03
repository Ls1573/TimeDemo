package com.time.memory.mt.vo;

import java.io.Serializable;
import java.util.Date;

public class MemoryVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String memoryId;

	private String title;

	private String photo1;

	private String photo2;

	private String photo3;

	private String labelId;

	private String labelName;

	private String groupId;

	private String groupLabelId;

	private Date memoryDate;

	private String remindWho;

	private String noseeWho;

	private String state;

	private int pageIndex;

	private int pageSize;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoto1() {
		return photo1;
	}

	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}

	public String getPhoto2() {
		return photo2;
	}

	public void setPhoto2(String photo2) {
		this.photo2 = photo2;
	}

	public String getPhoto3() {
		return photo3;
	}

	public void setPhoto3(String photo3) {
		this.photo3 = photo3;
	}

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupLabelId() {
		return groupLabelId;
	}

	public void setGroupLabelId(String groupLabelId) {
		this.groupLabelId = groupLabelId;
	}

	public Date getMemoryDate() {
		return memoryDate;
	}

	public void setMemoryDate(Date memoryDate) {
		this.memoryDate = memoryDate;
	}

	public String getRemindWho() {
		return remindWho;
	}

	public void setRemindWho(String remindWho) {
		this.remindWho = remindWho;
	}

	public String getNoseeWho() {
		return noseeWho;
	}

	public void setNoseeWho(String noseeWho) {
		this.noseeWho = noseeWho;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}