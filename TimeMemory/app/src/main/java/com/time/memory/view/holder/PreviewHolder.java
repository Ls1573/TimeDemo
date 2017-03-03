package com.time.memory.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.WriterMemory;
import com.time.memory.gui.sixGridImage.SixGridImageView;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.util.CLog;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:预览
 * @date 2016/10/11 11:43
 */
public class PreviewHolder extends BaseHolder<WriterMemory> {
	private static final String TAG = "WriterMHolder";
	@Bind(R.id.writer_grid)
	SixGridImageView writer_grid;

	@Bind(R.id.writer_details_tv)
	TextView writerContent;//内容
	@Bind(R.id.writer_date_tv)
	TextView writerDate;//日期
	@Bind(R.id.writer_address_tv)
	TextView writer_address_tv;//地址

	public PreviewHolder(View view) {
		super(view);
	}

	private int size = 1;

	@Override
	public void init() {
		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
//				GlideUtil.glideWithTarget(context, entity.getPath(), imageView);
				if (size == 1) {
					Picasso.with(context).load("file://" + entity.getPhotoPath()).resize(800, 600).centerCrop().into(imageView);
				} else {
					Picasso.with(context).load("file://" + entity.getPhotoPath()).resize(400, 400).centerCrop().into(imageView);
				}
			}

			@Override
			protected void onDeleteClick(int position) {
			}

			@Override
			protected void onItemImageClick(int position) {
				CLog.e(TAG, "position:" + position);
			}

			@Override
			protected void onAddClick(int position) {
			}
		};
		writer_grid.setAdapter(mAdapter);
	}

	@Override
	public void setData(WriterMemory entity) {
		super.setData(entity);
		//获取张数
		size = entity.getPictureEntits().size();
		//设置数据
		writer_grid.setImagesData(entity.getPictureEntits());
		//设置时间
		writerDate.setText(entity.getDate());
		//设置内容
		if (TextUtils.isEmpty(entity.getDesc())) {
			writerContent.setVisibility(View.GONE);
		} else {
			writerContent.setVisibility(View.VISIBLE);
			writerContent.setText(entity.getDesc());
		}
		//地址
		if (!TextUtils.isEmpty(entity.getAddress())) {
			writer_address_tv.setText(entity.getAddress());
			writer_address_tv.setVisibility(View.VISIBLE);
		} else {
			writer_address_tv.setVisibility(View.GONE);
		}
	}
}
