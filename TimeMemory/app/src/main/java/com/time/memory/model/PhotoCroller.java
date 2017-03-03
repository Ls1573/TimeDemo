package com.time.memory.model;

import android.database.Cursor;

import com.time.memory.MainApplication;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.PhotoInfoDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IPhotoController;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:图片操作
 * @date 2016/12/14 15:35
 */
public class PhotoCroller extends BaseController implements IPhotoController {
	private PhotoInfoDao photoInfoDao;

	public PhotoCroller() {
		photoInfoDao = MainApplication.getDaoSession().getPhotoInfoDao();
	}

	/**
	 * 保存所有数据
	 *
	 * @param photoInfos
	 */
	@Override
	public void savePhotos(List<PhotoInfo> photoInfos) {
		photoInfoDao.deleteAll();
		photoInfoDao.insertInTx(photoInfos);
	}

	/**
	 * 更新部分数据
	 *
	 * @param photoInfos
	 */
	@Override
	public void replasePhotos(List<PhotoInfo> photoInfos) {
		photoInfoDao.updateInTx(photoInfos);
	}

	/**
	 * 更新一条数据
	 *
	 * @param photoInfo
	 */
	@Override
	public void updatePhotoInfo(PhotoInfo photoInfo, boolean isActicted) {
		photoInfo.setIsActicted(isActicted);
		photoInfoDao.update(photoInfo);
	}

	/**
	 * 更新一条数据
	 *
	 * @param photoInfo
	 */
	@Override
	public void updatePhotoInfoClick(PhotoInfo photoInfo, boolean isClicked) {
		photoInfo.setIsClicked(isClicked);
		photoInfoDao.update(photoInfo);
	}

	@Override
	public void updatePhotoInfo(PhotoInfo photoInfo) {
		photoInfoDao.update(photoInfo);
	}

	/**
	 * sql更新多条数据
	 *
	 * @param date
	 * @param acticted
	 */
	@Override
	public void updatePhotoInfos(String date, String acticted) {
//		String sql = "update PHOTO_INFO set IS_ACTICTED= ? where  DATE= ? and IS_TITLE=0";
		String sql = "update PHOTO_INFO set IS_ACTICTED=? where _id in(select _id from PHOTO_INFO where date=? and IS_TITLE=0 limit 299)";
		photoInfoDao.getDatabase().execSQL(sql, new String[]{acticted, date});
//		photoInfoDao.notify();
	}

	/**
	 * 获取点击位置
	 *
	 * @return
	 */
	@Override
	public int getClickIndex(int position, int bucket_id) {
		int index = 0;
		Cursor cursor = null;
		if (bucket_id != 0)
			cursor = photoInfoDao.getDatabase().rawQuery("select count(1) from (select * from PHOTO_INFO where bucket_id=?  limit ?)a where a.IS_TITLE=0", new String[]{String.valueOf(bucket_id), String.valueOf(position)});
		else
			cursor = photoInfoDao.getDatabase().rawQuery("select count(1) from (select * from PHOTO_INFO  limit ?)a where a.IS_TITLE=0", new String[]{String.valueOf(position)});

		if (cursor != null) {
			if (cursor.moveToNext()) {
				index = cursor.getInt(0);
			}
			cursor.close();
		}

		return index;
	}

	/**
	 * 所有数据
	 *
	 * @return
	 */
	@Override
	public List<PhotoInfo> getPhotoInfos() {
		return photoInfoDao.loadAll();
	}

	/**
	 * 获取文件夹下的图片及頭部
	 *
	 * @param bucketId
	 * @return
	 */
	@Override
	public List<PhotoInfo> getPhotoInfos(int bucketId) {
		QueryBuilder qb = photoInfoDao.queryBuilder();
		qb.where(PhotoInfoDao.Properties.BucketId.eq(bucketId));
		return qb.list();
	}

	/**
	 * 获取文件夹下的图片無頭部
	 *
	 * @param bucketId
	 * @return
	 */
	@Override
	public List<PhotoInfo> getPhotoInfosWithoutTitle(int bucketId) {
		QueryBuilder qb = photoInfoDao.queryBuilder();
		qb.where(PhotoInfoDao.Properties.IsTitle.eq(0), PhotoInfoDao.Properties.BucketId.eq(bucketId));
		return qb.list();
	}


	/**
	 * 获取被激活的图片
	 *
	 * @return
	 */
	@Override
	public List<PhotoInfo> getActivedPhotoInfos() {
		QueryBuilder qb = photoInfoDao.queryBuilder();
		qb.where(PhotoInfoDao.Properties.IsTitle.eq(0), PhotoInfoDao.Properties.IsActicted.eq(1));
		return qb.list();
	}

	/**
	 * 无Title数据
	 *
	 * @return
	 */
	@Override
	public List<PhotoInfo> getPhotoInfosWithoutTitle() {
		QueryBuilder qb = photoInfoDao.queryBuilder();
		qb.where(PhotoInfoDao.Properties.IsTitle.eq(0));
		return qb.list();
	}
}
