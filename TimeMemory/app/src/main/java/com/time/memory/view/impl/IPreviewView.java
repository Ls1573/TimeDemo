package com.time.memory.view.impl;

import com.time.memory.entity.TempMemory;
import com.time.memory.entity.WriterMemory;

import java.util.List;

/**
 * V-P
 */
public interface IPreviewView extends IBaseView {

	void setAdapter(List<WriterMemory> list);

	void setMemory(TempMemory memory);

}
