package com.time.memory.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.callback.CommentCallBack;
import com.time.memory.entity.Comment;
import com.time.memory.gui.CircleImageTransformation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:片段评论
 * @date 2016/10/27 13:44
 */
public class MemoryCommentAdapter extends BaseAdapter {

	private Context context;
	private List<Comment> comments;
	private String imgWeb;
	private CommentCallBack commentCallBack;

	public MemoryCommentAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this.comments = comments;
		this.imgWeb = context.getString(R.string.FSIMAGEPATH);
	}

	public void setCommentCallBack(CommentCallBack commentCallBack) {
		this.commentCallBack = commentCallBack;
	}

	@Override
	public int getCount() {
		return comments != null && comments.size() > 0 ? comments.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return comments != null && comments.size() > 0 ? comments.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_point_comment, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Comment comment = comments.get(position);
		viewHolder.memoryp_name_tv.setText(comment.getU1name());
		viewHolder.memoryp_date_tv.setText(comment.getInsdForShow());
		//回复给记忆体
		if (TextUtils.isEmpty(comment.getU2name()))
			viewHolder.memoryp_info_tv.setText(comment.getCt());
		else
			//回复给人的
			viewHolder.memoryp_info_tv.setText(Html.fromHtml("回复 <font color=#DEB54C> " + comment.getU2name() + " </font>" + " : " + comment.getCt()));
		//图片
		if (!TextUtils.isEmpty(comment.getU1hp())) {
			if (comment.getU1hp().contains("http")) {
				Picasso.with(context).load(comment.getU1hp()).transform(new CircleImageTransformation()).into(viewHolder.memoryp_head_iv);
			} else
				Picasso.with(context).load(imgWeb + comment.getU1hp()).transform(new CircleImageTransformation()).into(viewHolder.memoryp_head_iv);
		} else {
			viewHolder.memoryp_head_iv.setImageResource(R.drawable.login_photo);
		}

		//更多
		viewHolder.memoryp_more_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				commentCallBack.onMoreClick(position);
			}
		});
		//点击详情->回复
		viewHolder.memoryp_info_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				commentCallBack.onCommentClick(position);
			}
		});
		//头像
		viewHolder.memoryp_head_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				commentCallBack.onUserCall(position);
			}
		});
		return convertView;
	}

	class ViewHolder {
		@Bind(R.id.memoryp_name_tv)
		TextView memoryp_name_tv;//名
		@Bind(R.id.memoryp_info_tv)
		TextView memoryp_info_tv;//详情
		@Bind(R.id.memoryp_date_tv)
		TextView memoryp_date_tv;//日期
		@Bind(R.id.memoryp_head_iv)
		ImageView memoryp_head_iv;//头像
		@Bind(R.id.memoryp_comment_iv)
		ImageView memoryp_comment_iv;//额外图
		@Bind(R.id.memoryp_more_iv)
		ImageView memoryp_more_iv;//更多信息

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
