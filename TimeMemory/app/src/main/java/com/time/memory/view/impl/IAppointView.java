package com.time.memory.view.impl;

import com.time.memory.entity.Appoint;

import java.util.List;

/**
 * V-P
 */
public interface IAppointView extends IBaseView {
	void setAdapter(List<Appoint> groupList);
}
