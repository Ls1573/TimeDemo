package com.time.memory.view.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
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
import com.time.memory.util.CPResourceUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆详情片段
 * @date 2016/10/28 9:39
 */
public class MemoryDPointHolder extends BaseHolder<MemoryEdit> {
	private static final String TAG = "MemoryDPointHolder";
	@Bind(R.id.memoryd_addtag_tv)
	TextView memoryd_addtag_tv;//补充记忆数
	@Bind(R.id.writer_grid)
	SixGridImageView writer_grid;//图片
	@Bind(R.id.memoryd_date_tv)
	TextView memoryd_date_tv;//日期
	@Bind(R.id.memoryd_address_tv)
	TextView memoryd_address_tv;//地址
	@Bind(R.id.memoryd_praise_tv)
	TextView memoryd_praise_tv;//点赞
	@Bind(R.id.memoryd_comment_tv)
	TextView memoryd_comment_tv;//评论
	@Bind(R.id.memoryp_desc_tv)
	TextView memoryp_desc_tv;//详情
	@Bind(R.id.memoryd_name_tv)
	TextView memoryd_name_tv;//补充人
	@Bind(R.id.memoryd_memorydate_tv)
	TextView memoryd_memorydate_tv;//补充时间

	private int mPosition;
	private Drawable memory_heart;
	private Drawable memory_unheart;
	private String imUrlOSS;
	private String addUser;

	private int size = 1;


	private int getIndex(int length) {
		int index = length % 5 + 1;
		return CPResourceUtil.getDrawableId(mContext, "commbg" + index);
	}


	public MemoryDPointHolder(View view) {
		super(view);
		memory_heart = mContext.getResources().getDrawable(R.drawable.heart_ok);
		memory_heart.setBounds(0, 0, memory_heart.getMinimumWidth(), memory_heart.getMinimumHeight());

		memory_unheart = mContext.getResources().getDrawable(R.drawable.memory_heart);
		memory_unheart.setBounds(0, 0, memory_unheart.getMinimumWidth(), memory_unheart.getMinimumHeight());

		imUrlOSS = mContext.getString(R.string.FSIMAGEOSSDETAIL);
		addUser = mContext.getString(R.string.memory_adduser);
	}

	@Override
	public void init() {
		super.init();
		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
				String url = entity.getPhotoPath() + imUrlOSS;
				int index = getIndex(Math.abs(url.hashCode()));
				//网络图
				if (size == 1) {
					Picasso.with(context).load(url).centerCrop().resize(800, 600).error(index).placeholder(index).into(imageView);
				} else {
					Picasso.with(context).load(url).centerCrop().resize(400, 400).error(index).placeholder(index).into(imageView);
				}
			}

			@Override
			protected void onDeleteClick(int position) {
			}

			@Override
			protected void onItemImageClick(int position) {
				CLog.e(TAG, "position:" + position);
				mHolderCallBack.onClick(mPosition, position, 5);
			}

			@Override
			protected void onAddClick(int position) {
			}
		};
		writer_grid.setAdapter(mAdapter);
	}

	@Override
	public void setData(MemoryEdit entity, int positoin) {
		super.setData(entity, positoin);
		this.mPosition = positoin;
		size = entity.getPhotoInfos().size();
		//6宫图
		writer_grid.setImagesData(entity.getPhotoInfos());
		//记忆日期
		memoryd_date_tv.setText(entity.getMpDateForShow());

		//补充记忆 0-->补充记忆
		if (!TextUtils.isEmpty(entity.getAddFlag()) && entity.getAddFlag().equals("0")) {
			//补充日期
			memoryd_memorydate_tv.setText(entity.getInsdForShow());
			//补充人+圈子名
			if (TextUtils.isEmpty(entity.getgName())) {
				memoryd_name_tv.setText(Html.fromHtml("by<font color=#DEB54C> " + entity.getUname() + " </font>"));
			} else {
				memoryd_name_tv.setText(Html.fromHtml("by<font color=#DEB54C> «" + entity.getgName() + "» " + entity.getUname() + " </font>"));
			}

			memoryd_memorydate_tv.setVisibility(View.VISIBLE);
			memoryd_name_tv.setVisibility(View.VISIBLE);
		} else {
			memoryd_memorydate_tv.setVisibility(View.GONE);
			memoryd_name_tv.setVisibility(View.GONE);
		}

		//地址
		if (!TextUtils.isEmpty(entity.getLocal())) {
			memoryd_address_tv.setText(entity.getLocal());
			memoryd_address_tv.setVisibility(View.VISIBLE);
		} else
			memoryd_address_tv.setVisibility(View.GONE);
		//描述
		if (TextUtils.isEmpty(entity.getDetail()))
			memoryp_desc_tv.setVisibility(View.GONE);
		else {
			memoryp_desc_tv.setText(entity.getDetail());
			memoryp_desc_tv.setVisibility(View.VISIBLE);
		}
		//点赞数
		memoryd_praise_tv.setText(entity.getpCnt());
		//评论数
		memoryd_comment_tv.setText(entity.getcCnt());
		if (entity.getMpFlag().equals("0"))
			memoryd_praise_tv.setCompoundDrawables(memory_heart, null, null, null);
		else
			memoryd_praise_tv.setCompoundDrawables(memory_unheart, null, null, null);

		//补充记忆数
		if (!TextUtils.isEmpty(entity.getSumAdd())) {
			//是补充数据
			memoryd_addtag_tv.setVisibility(View.VISIBLE);
			memoryd_addtag_tv.setText(String.format(mContext.getString(R.string.memory_add_), entity.getSumAdd()));
		} else {
			memoryd_addtag_tv.setVisibility(View.GONE);
		}
	}

	@OnClick({R.id.memoryd_praise_tv, R.id.memoryd_comment_tv})
	public void onClick(View view) {
		if (view.getId() == R.id.memoryd_praise_tv) {
			//点赞
			mHolderCallBack.onClick(mPosition, 0);
		} else {
			//评论
			mHolderCallBack.onClick(mPosition, 1);
		}
	}
}
