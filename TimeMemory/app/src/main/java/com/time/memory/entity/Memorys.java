package com.time.memory.entity;

import com.time.memory.gui.sticky.SectionAdapter;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆集
 * @date 2016/10/17 16:18
 */
public class Memorys extends SectionAdapter.Section {


	private String date;

	private String yearMouth;

	private String memoryId;

	private boolean isActived;

	private List<Memory> list;

	private Memory memory;

	private int position;

	private boolean isEnd;

	public boolean isHeader;

	public boolean isHeader() {
		return isHeader;
	}

	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}


	public Memorys() {
	}

	public Memorys(Memory memory, boolean isHeader, int start, int position,boolean islast) {
		this.memory = memory;
		this.isHeader = isHeader;
		this.start = start;
		this.position = position;
		setIsLast(islast);
	}

	public Memorys(String yearMouth, boolean isHeader, int start) {
		this.isHeader = isHeader;
		this.yearMouth = yearMouth;
		this.start = start;
	}

	public boolean isActived() {
		return isActived;
	}

	public void setIsActived(boolean isActived) {
		this.isActived = isActived;
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

	public List<Memory> getList() {
		return list;
	}

	public void setList(List<Memory> list) {
		this.list = list;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setIsEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
