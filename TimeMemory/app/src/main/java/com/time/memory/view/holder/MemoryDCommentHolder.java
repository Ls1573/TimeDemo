package com.time.memory.view.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.MemoryComment;
import com.time.memory.gui.CircleImageTransformation;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆片段详情回复
 * @date 2016/10/28 13:40
 */
public class MemoryDCommentHolder extends BaseHolder<MemoryComment> {
	private static final String TAG = "MemoryDCommentHolder";

	@Bind(R.id.memoryp_name_tv)
	TextView memoryp_name_tv;//名
	@Bind(R.id.memoryp_comments_tv)
	TextView memoryp_comments_tv;//评论条数
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
	@Bind(R.id.view_line)
	View view_line;//分割线

	private String imgWeb;
	private String imgOSS;

	public MemoryDCommentHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		super.init();
		imgWeb = mContext.getString(R.string.FSIMAGEPATH);
		imgOSS = mContext.getString(R.string.FSIMAGEOSS);
	}

	@Override
	public void setData(MemoryComment entity, final int positoin) {
		super.setData(entity, positoin);
		//第一条
		if (entity.isFirst()) {
			memoryp_comments_tv.setText(Html.fromHtml("评论<font color=#DEB54C>" + entity.getSum() + "</font>" + "条"));
			memoryp_comments_tv.setVisibility(View.VISIBLE);
		} else {
			memoryp_comments_tv.setVisibility(View.GONE);
		}
		//用户名
		memoryp_name_tv.setText(entity.getUnameC());
		//日期
		memoryp_date_tv.setText(entity.getInsdForShow());
		//内容
		if (TextUtils.isEmpty(entity.getuIdT()))
			//回复给记忆体
			memoryp_info_tv.setText(entity.getTt());
		else
			//回复给人的
			memoryp_info_tv.setText(Html.fromHtml("回复 <font color=#DEB54C> " + entity.getUnameT() + " </font>" + " : " + entity.getTt()));

		//图片
		if (!TextUtils.isEmpty(entity.getUhphotoC())) {
			Picasso.with(mContext).load(imgWeb + entity.getUhphotoC()).transform(new CircleImageTransformation()).error(R.drawable.login_photo).into(memoryp_head_iv);
		} else {
			memoryp_head_iv.setImageResource(R.drawable.login_photo);
		}
		//段落图片
		if (!TextUtils.isEmpty(entity.getP1())) {
			Picasso.with(mContext).load(imgWeb + entity.getP1() + imgOSS).into(memoryp_comment_iv);
			memoryp_comment_iv.setVisibility(View.VISIBLE);
		} else {
			memoryp_comment_iv.setVisibility(View.INVISIBLE);
		}

		//更多
		memoryp_more_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(positoin, 1, 4);
			}
		});


		//点击详情->回复
		memoryp_info_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(positoin, 2, 9);
			}
		});

		//片段图片->
		memoryp_comment_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(positoin, 3, 8);
			}
		});
		//头像
		memoryp_head_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(positoin, 1, 6);
			}
		});
	}
}
