package com.time.memory.model.impl;


import com.time.memory.entity.PhotoInfo;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/12/14 15:34
 */
public interface IPhotoController {

	void savePhotos(List<PhotoInfo> photoInfos);

	void replasePhotos(List<PhotoInfo> photoInfos);

	void updatePhotoInfo(PhotoInfo photoInfo, boolean isActicted);

	void updatePhotoInfoClick(PhotoInfo photoInfo, boolean isClicked);

	void updatePhotoInfo(PhotoInfo photoInfo);

	void updatePhotoInfos(String date, String acticted);

	int getClickIndex(int position,int bucket_id);

	List<PhotoInfo> getPhotoInfos();

	List<PhotoInfo> getPhotoInfos(int bucketId);

	List<PhotoInfo> getPhotoInfosWithoutTitle(int bucketId);

	List<PhotoInfo> getActivedPhotoInfos();

	List<PhotoInfo> getPhotoInfosWithoutTitle();
}
