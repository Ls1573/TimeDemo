package com.time.memory.gui.gallery;


import com.time.memory.entity.PhotoInfo;

import java.util.Comparator;


/**
 *
 * @author @Qiu
 * @Description:按时间排序
 * @date 2016/9/28 9:33
 * @version V1.0
 *
 */
public class YMComparator implements Comparator<PhotoInfo> {

	@Override
	public int compare(PhotoInfo o1, PhotoInfo o2) {
		return o2.getDate().compareTo(o1.getDate());
	}

}
