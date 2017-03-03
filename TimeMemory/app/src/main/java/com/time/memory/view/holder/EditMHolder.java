package com.time.memory.view.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.WriterEditText;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.gui.sixGridImage.SixGridImageWriterView;
import com.time.memory.util.CLog;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑记忆
 * @date 2016/10/25 19:28
 */
public class EditMHolder extends BaseHolder<MemoryEdit> implements TextWatcher {
	private static final String TAG = "WriterMHolder";
	@Bind(R.id.writer_grid)
	SixGridImageWriterView writer_grid;

	@Bind(R.id.writer_add_iv)
	ImageView writer_add_iv;//添加
	@Bind(R.id.writer_delete_iv)
	ImageView writer_delete_iv;//添加
	//	@Bind(R.id.writer_more_iv)
//	ImageView writer_more_iv;//添加
	@Bind(R.id.writer_num_tv)
	TextView writer_num_tv;//数量指示
	@Bind(R.id.writer_date_tv)
	TextView writer_date_tv;//时间
	@Bind(R.id.writer_loc_tv)
	TextView writer_loc_tv;//地址

	@Bind(R.id.writer_et)
	WriterEditText writer_et;

	private int mPosition;
	private int size = 1;
	private MemoryEdit mEntity;

	public EditMHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		writer_et.addTextChangedListener(this);

		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
				//显示
				if (entity.getPhotoPath().contains("http")) {
					//网络图
					Picasso.with(context).load(entity.getPhotoPath()).config(Bitmap.Config.RGB_565).placeholder(R.drawable.commbg4).error(R.drawable.commbg4).into(imageView);
				} else {
					//本地显示
					if (size == 1) {
						Picasso.with(context).load("file://" + entity.getPhotoPath()).config(Bitmap.Config.RGB_565).resize(500, 400).centerCrop().into(imageView);
					} else {
						Picasso.with(context).load("file://" + entity.getPhotoPath()).config(Bitmap.Config.RGB_565).resize(300, 300).centerCrop().into(imageView);
					}
				}
			}

			@Override
			protected void onDeleteClick(int position) {
//				CLog.e(TAG, "onDeleteClick:" + position + "  mPosition:" + mPosition);
				//删除图片
				mHolderCallBack.onClick(mPosition, position, 1);
			}

			@Override
			protected void onItemImageClick(int position) {
				CLog.e(TAG, "onItemImageClick:" + position + "  mPosition:" + mPosition);
				//点击图片
				mHolderCallBack.onClick(mPosition, position, 2);
			}

			@Override
			protected void onAddClick(int position) {
				//添加
				CLog.e(TAG, "onAddClick:" + position + "  mPosition:" + mPosition);
				mHolderCallBack.onClick(mPosition, position, 3);
			}
		};
		writer_grid.setAdapter(mAdapter);

	}

	@Override
	public void setData(MemoryEdit mEntity, int position) {
		super.setData(mEntity);
		this.mEntity = mEntity;
		this.mPosition = position;
		this.size = mEntity.getPhotoInfos().size();
		//设置数据
		writer_grid.setImagesData(mEntity.getPhotoInfos());
		//设置显示头
//		writer_add_iv.setVisibility(mEntity.isFirst() ? View.GONE : View.VISIBLE);
		//设置显示尾
//		writer_more_iv.setVisibility(mEntity.isLast() ? View.VISIBLE : View.GONE);
		//文字
		writer_et.setText(mEntity.getDetail());
		//数量指示
		writer_num_tv.setText(mEntity.getNum());
		//时间显示
		writer_date_tv.setText(mEntity.getMemoryPointDate());
		//地址
		writer_loc_tv.setText(mEntity.getLocal());

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		//文字变化
		mEntity.setDetail(s.toString().trim());
	}

	@OnClick({R.id.writer_date_tv, R.id.writer_loc_tv, R.id.writer_add_iv, R.id.writer_delete_iv})
	public void onClick(View view) {
		if (view.getId() == R.id.writer_date_tv) {
			//日期
			mHolderCallBack.onClick(mPosition, -1, 4);
		} else if (view.getId() == R.id.writer_loc_tv) {
			//地址
			mHolderCallBack.onClick(mPosition, -1, 5);
		} else if (view.getId() == R.id.writer_add_iv) {
			//头部追加
			mHolderCallBack.onClick(mPosition, -1, 6);
		} else if (view.getId() == R.id.writer_delete_iv) {
			//头部删除
			mHolderCallBack.onClick(mPosition, -1, 8);
		}
//		else if (view.getId() == R.id.writer_more_iv) {
//			//尾部追加
//			mHolderCallBack.onClick(mPosition, -1, 7);
//		}
	}

}
