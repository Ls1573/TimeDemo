package com.time.memory.view.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.PhotoFolderInfo;
import com.time.memory.util.CPResourceUtil;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:文件夹
 * @date 2016/11/30 15:48
 */
public class PFolderHolder extends BaseHolder<PhotoFolderInfo> {

	@Bind(R.id.photo_iv)
	ImageView photo_iv;//封面
	@Bind(R.id.photo_folder_tv)
	TextView photo_folder_tv;//文件夹名
	@Bind(R.id.photo_foldernum_tv)
	TextView photo_foldernum_tv;//图片数

	private int position;

	public PFolderHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);
			}
		});
	}

	private int getIndex() {
		int index = (int) (Math.random() * 5) + 1;
		return CPResourceUtil.getDrawableId(mContext, "commbg" + index);
	}

	@Override
	public void setData(PhotoFolderInfo mEntity, int position) {
		super.setData(mEntity);
		this.position = position;
		//默认图
		Picasso.with(mContext).load("file://" + mEntity.getPhotoThumbl()).config(Bitmap.Config.RGB_565).resize(300, 300).placeholder(getIndex()).centerCrop().into(photo_iv);
		//文件夹名
		photo_folder_tv.setText(mEntity.getFolderName());
		//图片数
		photo_foldernum_tv.setText(String.format(mContext.getString(R.string.photo_num), String.valueOf(mEntity.getPhotoSize())));
	}

}
