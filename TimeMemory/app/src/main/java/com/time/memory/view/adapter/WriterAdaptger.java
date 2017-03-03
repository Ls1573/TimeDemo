package com.time.memory.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Picture;
import com.time.memory.entity.WriterMemory;
import com.time.memory.gui.sixGridImage.SixGridImageView;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.util.CLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Qiu
 * @version V1.0
 * @Description:写记忆(无缓存处理)
 * @date 2016/10/10 9:13
 */
public class WriterAdaptger extends BaseAdapter {
	private static final String TAG = "WriterAdaptger";
	private List<WriterMemory> list;
	private Context context;
	private LayoutInflater inflater;
	private WriterMHolder holder;

	public WriterAdaptger(Context context, List<WriterMemory> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_writermemory, null, false);
		holder = new WriterMHolder(convertView);
		holder.setData(list.get(position));
		return convertView;
	}


	class WriterMHolder {
		@Bind(R.id.writer_grid)
		SixGridImageView writer_grid;

		public WriterMHolder(View view) {
			ButterKnife.bind(this, view);
			writer_grid.setAdapter(mAdapter);
		}

		public void setData(WriterMemory entity) {
			CLog.e(TAG, "entity.getPictureEntits():" + entity.getPictureEntits().size());
			writer_grid.setImagesData(entity.getPictureEntits());
		}

		SixGridImageViewAdapter<Picture> mAdapter = new SixGridImageViewAdapter<Picture>() {
			@Override
			protected void onDisplayImage(Context context, ImageView imageView, Picture entity) {
				Picasso.with(context).load(entity.getPath()).config(Bitmap.Config.RGB_565).into(imageView);
			}

			@Override
			protected void onDeleteClick(int position) {
				//删除点击
			}

			@Override
			protected void onItemImageClick(int position) {
				CLog.e(TAG, "position:" + position);

			}

			@Override
			protected void onAddClick(int position) {

			}
		};
	}
}
