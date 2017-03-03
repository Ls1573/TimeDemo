package com.time.memory.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.entity.Fork;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:点赞
 * @date 2016/9/20 11:18
 */
public class CommentForkAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder viewHolder;
	private List<Fork> list;

	public CommentForkAdapter(List<Fork> list) {
		this.mContext = MainApplication.getContext();
		this.mInflater = LayoutInflater.from(mContext);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
			convertView = mInflater.inflate(R.layout.item_comment_fork_pic, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//TODO 测试数据
		String url = "http://img0.imgtn.bdimg.com/it/u=3468736256,2070128096&fm=21&gp=0.jpg";
//		GlideUtil.glideWithAllUrl(url, viewHolder.forkPic);
		return convertView;
	}

	class ViewHolder {
		@Bind(R.id.comment_fork_pic_iv)
		ImageView forkPic;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
