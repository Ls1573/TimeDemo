package com.time.memory.view.impl;

import com.time.memory.entity.Address;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IProvinceView extends IBaseView {

	void setAdapter(ArrayList<Address> addresses);
}
