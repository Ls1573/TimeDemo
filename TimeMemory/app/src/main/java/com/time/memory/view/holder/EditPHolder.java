package com.time.memory.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.sixGridImage.SixGridImageView;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.util.CLog;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑预览
 * @date 2016/10/26 8:57
 */
public class EditPHolder extends BaseHolder<MemoryEdit> {
	private static final String TAG = "WriterMHolder";
	@Bind(R.id.writer_grid)
	SixGridImageView writer_grid;

	@Bind(R.id.writer_details_tv)
	TextView writerContent;//内容
	@Bind(R.id.writer_date_tv)
	TextView writerDate;//日期
	@Bind(R.id.writer_address_tv)
	TextView writer_address_tv;//地址

	private int size = 1;

	public EditPHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
				if (entity.getPhotoPath().contains("http")) {
					//网络图
					Picasso.with(mContext).load(entity.getPhotoPath()).resize(800, 600).centerCrop().into(imageView);
				} else {
					//本地
					if (size == 1) {
						Picasso.with(mContext).load("file://" + entity.getPhotoPath()).resize(800, 600).centerCrop().into(imageView);
					} else {
						Picasso.with(mContext).load("file://" + entity.getPhotoPath()).resize(400, 400).centerCrop().into(imageView);
					}
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
	public void setData(MemoryEdit entity) {
		super.setData(entity);
		this.size = entity.getPhotoInfos().size();
		//设置数据
		writer_grid.setImagesData(entity.getPhotoInfos());
		//设置时间
		writerDate.setText(entity.getMemoryPointDate());
		//设置内容
		if (TextUtils.isEmpty(entity.getDetail())) {
			writerContent.setVisibility(View.GONE);
		} else {
			writerContent.setVisibility(View.VISIBLE);
			writerContent.setText(entity.getDetail());
		}
		//地址
		if (!TextUtils.isEmpty(entity.getLocal())) {
			writer_address_tv.setText(entity.getLocal());
			writer_address_tv.setVisibility(View.VISIBLE);
		} else {
			writer_address_tv.setVisibility(View.GONE);
		}
	}
}
