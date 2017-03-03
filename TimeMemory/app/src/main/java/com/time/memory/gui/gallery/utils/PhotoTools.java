/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.time.memory.gui.gallery.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.time.memory.R;
import com.time.memory.entity.PhotoFolderInfo;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.gallery.GalleryFinal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Desction:手机图片获取
 * Date:16/10/10 下午4:26
 */
public class PhotoTools {

	/**
	 * 获取所有图片
	 *
	 * @param context
	 * @return
	 */
	public static List<PhotoFolderInfo> getAllPhotoFolder(Context context, List<PhotoInfo> selectPhotoMap) {
		List<PhotoFolderInfo> allFolderList = new ArrayList<>();
		final String[] projectionPhotos = {
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				MediaStore.Images.Media.DATA,
//				MediaStore.Images.Media.DATE_TAKEN,
//				MediaStore.Images.Media.ORIENTATION,
//				MediaStore.Images.Thumbnails.DATA,
//				MediaStore.Images.Media.DATE_ADDED,
				MediaStore.Images.Media.LATITUDE,
				MediaStore.Images.Media.LONGITUDE,
//				MediaStore.Images.Media.WIDTH,
//				MediaStore.Images.Media.HEIGHT,
				MediaStore.Images.Media.DATE_MODIFIED,

		};
		final ArrayList<PhotoFolderInfo> allPhotoFolderList = new ArrayList<>();
		HashMap<Integer, PhotoFolderInfo> bucketMap = new HashMap<>();
		Cursor cursor = null;
		//所有图片
		PhotoFolderInfo allPhotoFolderInfo = new PhotoFolderInfo();
		allPhotoFolderInfo.setFolderId(0);
		allPhotoFolderInfo.setFolderName(context.getResources().getString(R.string.all_photo));
		allPhotoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
		allPhotoFolderList.add(0, allPhotoFolderInfo);
		List<String> selectedList = GalleryFinal.getFunctionConfig().getSelectedList();
		try {
			cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI
					, projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
			if (cursor != null) {
				int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
				final int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
				while (cursor.moveToNext()) {
					int bucketId = cursor.getInt(bucketIdColumn);
					String bucketName = cursor.getString(bucketNameColumn);
					int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
					int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//					int imgWidth = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
//					int imgHeight = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
					//照片加入时间
//					int date_add = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
					//照片taken
//					int date_taken = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
					//照片MODIFIED
					int date_modified = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);
					//获取维度
					int date_latitude = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
					//获取经度
					int date_longitude = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
					//图片ID
					int imageId = cursor.getInt(imageIdColumn);
					//图片添加日期
//					long dateAdd = cursor.getLong(date_add);
//					long dateTaken = cursor.getLong(date_taken);
					long dateModified = cursor.getLong(date_modified);
//					int width = cursor.getInt(imgWidth);
//					int height = cursor.getInt(imgHeight);
					//地址
					String path = cursor.getString(dataColumn);
					//维度
					double latitude = cursor.getDouble(date_latitude);
					//经度
					double longitude = cursor.getDouble(date_longitude);
					//TODO 文件先未判断有没有&不知道会不会出问题
//					File file = new File(path);
//					if (file.exists() && file.length() > 0) {
					final PhotoInfo photoInfo = new PhotoInfo();
					photoInfo.setPhotoD(imageId);
					photoInfo.setPhotoPath(path);
					photoInfo.setBucketId(bucketId);
//					photoInfo.setDate(dateAdd);
					photoInfo.setDate(dateModified);
//					photoInfo.setDateModified(dateModified);
//					photoInfo.setDateTAKEN(dateTaken);
					photoInfo.setLatitude(latitude);
					photoInfo.setLongitude(longitude);
//					photoInfo.setWidth(width);
//					photoInfo.setHeight(height);
					if (allPhotoFolderInfo.getCoverPhoto() == null) {
						allPhotoFolderInfo.setCoverPhoto(photoInfo);
					}
					//添加到所有图片
					allPhotoFolderInfo.getPhotoList().add(photoInfo);
					//通过bucketId获取文件夹
					PhotoFolderInfo photoFolderInfo = bucketMap.get(bucketId);
					if (photoFolderInfo == null) {
						photoFolderInfo = new PhotoFolderInfo();
						photoFolderInfo.setPhotoList(new ArrayList<PhotoInfo>());
						photoFolderInfo.setFolderId(bucketId);
						photoFolderInfo.setFolderName(bucketName);
						photoFolderInfo.setCoverPhoto(photoInfo);
						bucketMap.put(bucketId, photoFolderInfo);
						allPhotoFolderList.add(photoFolderInfo);
					}
					photoFolderInfo.getPhotoList().add(photoInfo);
//					}
//					else {
//						CLog.e("PhotoTools", "文件不存在------------>:"path);
//					}
					if (selectedList != null && selectedList.size() > 0 && selectedList.contains(path)) {
						selectPhotoMap.add(photoInfo);
					}
				}
			}
		} catch (Exception ex) {
			ILogger.e(ex);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		allFolderList.addAll(allPhotoFolderList);
		if (selectedList != null) {
			selectedList.clear();
		}
		return allFolderList;
	}
}
