package com.time.memory.core.constant;


import com.time.memory.entity.PhotoInfo;

import java.util.ArrayList;

/**
 * @author Qiu
 * @version V1.0
 * @Description:图片
 * @date 2016/12/16 10:51
 */
public class PhotoConstant {

	private static ArrayList<PhotoInfo> mCurPhotoList;

	public static ArrayList<PhotoInfo> getmCurPhotoList() {
		return mCurPhotoList;
	}

	public static void setmCurPhotoList(ArrayList<PhotoInfo> mCurPhotoList) {
		if (PhotoConstant.mCurPhotoList != null) {
			PhotoConstant.mCurPhotoList.clear();
			PhotoConstant.mCurPhotoList.addAll(mCurPhotoList);
		} else {
			PhotoConstant.mCurPhotoList = mCurPhotoList;
		}
	}
}
