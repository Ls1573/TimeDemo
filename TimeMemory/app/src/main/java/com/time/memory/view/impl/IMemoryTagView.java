package com.time.memory.view.impl;

import com.time.memory.entity.Lable;
import com.time.memory.entity.Memory;
import com.time.memory.entity.Memorys;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IMemoryTagView extends IBaseView {

	void setTags(List<Lable> tags);

	void setAdapter(List<Memorys> list, ArrayList<Memory> memories);

	void showTags(boolean isSuccess);
}
