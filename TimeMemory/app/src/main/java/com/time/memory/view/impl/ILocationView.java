package com.time.memory.view.impl;

import com.time.memory.entity.Location;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/15 15:16
 */
public interface ILocationView extends IBaseView {

	void setAdapter(List<Location> locations);
}
