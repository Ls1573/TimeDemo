package com.time.memory.entity;

import com.time.memory.gui.sticky.SectionAdapter;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:他的记忆集
 * @date 2016/10/17 16:18
 */
public class OtherMemorys extends SectionAdapter.Section {
	private String date;

	private String yearMouth;

	private String memoryId;

	private boolean isActived;

	private int position;

	private boolean isEnd;

	public boolean isHeader;

	private OtherMemory memory;

	private List<OtherMemory> list;

	public OtherMemorys() {
	}

	public OtherMemorys(OtherMemory memory, boolean isHeader, int start, int position) {
		this.memory = memory;
		this.isHeader = isHeader;
		this.start = start;
		this.position = position;
	}

	public OtherMemorys(String yearMouth, boolean isHeader, int start) {
		this.isHeader = isHeader;
		this.yearMouth = yearMouth;
		this.start = start;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getYearMouth() {
		return yearMouth;
	}

	public void setYearMouth(String yearMouth) {
		this.yearMouth = yearMouth;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public boolean isActived() {
		return isActived;
	}

	public void setIsActived(boolean isActived) {
		this.isActived = isActived;
	}

	public List<OtherMemory> getList() {
		return list;
	}

	public void setList(List<OtherMemory> list) {
		this.list = list;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	public OtherMemory getMemory() {
		return memory;
	}

	public void setMemory(OtherMemory memory) {
		this.memory = memory;
	}
}
