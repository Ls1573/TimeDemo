package com.time.memory.view.impl;

import com.time.memory.entity.MemoryEdit;

import java.util.List;

/**
 * V-P
 */
public interface IEditPreviewView extends IBaseView {

	void setAdapter(List<MemoryEdit> list);

	void upSuccess();
}
