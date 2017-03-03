package com.time.memory.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Praise;
import com.time.memory.gui.CircleImageTransformation;
import com.time.memory.gui.MyTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:片段点赞
 * @date 2016/10/27 13:52
 */
public class MemoryPraiseAdapter extends BaseAdapter {
	private static final String TAG = "MemoryPraiseAdapter";
	private Context context;
	private List<Praise> praises;
	private LayoutInflater inflater;
	private String imgWeb;

	public MemoryPraiseAdapter(Context context, List<Praise> praises) {
		this.context = context;
		this.praises = praises;
		this.inflater = LayoutInflater.from(context);
		this.imgWeb = context.getString(R.string.FSIMAGEPATH);
	}

	@Override
	public int getCount() {
		return praises != null && praises.size() > 0 ? praises.size() + 1 : 1;
	}

	@Override
	public Object getItem(int position) {
		if (position == 0)
			return null;
		else
			return praises.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_point_praise, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//最后一项
		if (position == praises.size()) {
			//点赞数
			if (getCount() == 1) {
				viewHolder.praiseTv.setVisibility(View.GONE);
				viewHolder.praiseIv.setVisibility(View.GONE);
				viewHolder.forkTv.setVisibility(View.GONE);
			} else {
				viewHolder.praiseTv.setText(getCount() - 1 + "人赞");
				viewHolder.praiseTv.setVisibility(View.VISIBLE);
				viewHolder.praiseIv.setVisibility(View.GONE);
				viewHolder.forkTv.setVisibility(View.GONE);
			}
		} else {
			//图片
			if (!TextUtils.isEmpty(praises.get(position).getUhp())) {
				if (praises.get(position).getUhp().contains("http")) {
					Picasso.with(context).load(praises.get(position).getUhp()).transform(new CircleImageTransformation()).into(viewHolder.praiseIv);
				} else
					Picasso.with(context).load(imgWeb + praises.get(position).getUhp()).transform(new CircleImageTransformation()).into(viewHolder.praiseIv);

				viewHolder.praiseIv.setVisibility(View.VISIBLE);
				viewHolder.forkTv.setVisibility(View.GONE);
			} else {
				viewHolder.forkTv.setText(praises.get(position).getSubName());
				viewHolder.praiseIv.setVisibility(View.GONE);
				viewHolder.forkTv.setVisibility(View.VISIBLE);
			}
			viewHolder.praiseTv.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		@Bind(R.id.memoryp_praise_iv)
		ImageView praiseIv;
		@Bind(R.id.memoryp_praise_tv)
		TextView praiseTv;
		@Bind(R.id.memoryd_fork_tv)
		MyTextView forkTv;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
