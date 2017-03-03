package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Advert;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/14 9:57
 */
public interface IAdvertController {
	void reqAdvert(String url, SimpleCallback callback);

	List<Advert> getAdvertFromDb();

	void saveAdvert(Advert advert);
}
