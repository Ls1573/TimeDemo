package com.time.memory.view.impl;

import com.time.memory.entity.Memory;
import com.time.memory.entity.Memorys;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/28 16:16
 */
public interface IMemoryTimeView extends IBaseView{
	void setAdapter(List<Memorys> list, ArrayList<Memory> memories);
}
