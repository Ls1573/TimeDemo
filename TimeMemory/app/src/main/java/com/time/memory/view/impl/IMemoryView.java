package com.time.memory.view.impl;

import com.time.memory.entity.Advert;
import com.time.memory.entity.MGroup;

import java.util.List;

/**
 * V-P
 */
public interface IMemoryView extends IBaseView {

	void reqCircle();

	void reqAdvert();

	void reqStartCirlce(int position);

	void setAdvert(Advert advert);

	void setAdapter(Advert advert, List<MGroup> MGroupList);

	void setAdapter(List<MGroup> MGroupList);
}
