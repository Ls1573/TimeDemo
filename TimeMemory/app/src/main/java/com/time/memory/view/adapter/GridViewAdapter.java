package com.time.memory.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.callback.PraiseCallBack;
import com.time.memory.entity.MemoryPraise;
import com.time.memory.gui.CircleImageTransformation;
import com.time.memory.gui.MyTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:点赞图
 * @date 2016/10/28 12:14
 */
public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private List<MemoryPraise> MemoryPraises;
	private String imgPath;
	private PraiseCallBack praiseCallBack;

	public void setPraiseCallBack(PraiseCallBack praiseCallBack) {
		this.praiseCallBack = praiseCallBack;
	}

	public GridViewAdapter(Context context, List<MemoryPraise> MemoryPraises) {
		this.context = context;
		this.MemoryPraises = MemoryPraises;
		this.imgPath = context.getString(R.string.FSIMAGEPATH);
	}

	@Override
	public int getCount() {
		return MemoryPraises != null && MemoryPraises.size() > 0 ? MemoryPraises.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return MemoryPraises.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_memory_forkp, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String pic = MemoryPraises.get(position).getHphoto();
		String uname = MemoryPraises.get(position).getSubName();
		//设置图片
		if (!TextUtils.isEmpty(pic)) {
			Picasso.with(parent.getContext()).load(imgPath + pic).transform(new CircleImageTransformation()).error(R.drawable.login_photo).into(viewHolder.memoryd_fork_iv);
			viewHolder.memoryd_fork_tv.setVisibility(View.GONE);
			viewHolder.memoryd_fork_iv.setVisibility(View.VISIBLE);
		} else {
			//设置姓名
			viewHolder.memoryd_fork_tv.setText(uname);
			viewHolder.memoryd_fork_tv.setVisibility(View.VISIBLE);
			viewHolder.memoryd_fork_iv.setVisibility(View.GONE);
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				praiseCallBack.onPraiseCall(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		@Bind(R.id.memoryd_fork_iv)
		ImageView memoryd_fork_iv;
		@Bind(R.id.memoryd_fork_tv)
		MyTextView memoryd_fork_tv;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
