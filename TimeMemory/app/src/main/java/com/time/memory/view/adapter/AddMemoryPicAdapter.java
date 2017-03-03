package com.time.memory.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Picture;
import com.time.memory.gui.MyImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:添加记忆图片
 * @date 2016/9/22 9:38
 */
public class AddMemoryPicAdapter extends BaseAdapter {

	private static final String TAG = "AddMemoryPicAdapter";
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private List<Picture> list;


	public AddMemoryPicAdapter(Context context, List<Picture> list) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size() < 9 ? list.size() + 1 : 9;
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_addmemory_pic, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//<9的情况，会有一张占位图
		if (list.size() < 9) {
			if (position < list.size())
				//TODO 测试
				Picasso.with(mContext).load(list.get(position).getPath()).config(Bitmap.Config.RGB_565).into(viewHolder.addmemoryPic);
			else
				viewHolder.addmemoryPic.setImageResource(R.drawable.add_picture);
		} else {
			Picasso.with(mContext).load(list.get(position).getPath()).config(Bitmap.Config.RGB_565).into(viewHolder.addmemoryPic);
		}
		return convertView;
	}

	class ViewHolder {
		@Bind(R.id.addmemory_pic_iv)
		MyImageView addmemoryPic;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
