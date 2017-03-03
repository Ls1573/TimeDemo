package com.time.memory.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Memory;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.MemoryView;
import com.time.memory.gui.nineGridImage.NineGridImageView;
import com.time.memory.gui.nineGridImage.NineGridImageViewAdapter;
import com.time.memory.util.CPResourceUtil;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆(天)
 * @date 2016/9/14 10:05
 */
public class MemoryDayHolder extends BaseHolder<Memory> {

	private static final String TAG = "MemoryDayHolder";
	@Bind(R.id.time_date_tv)
	TextView time_date_tv;//时间日期
	@Bind(R.id.time_loc_tv)
	TextView time_loc_tv;//地址
	@Bind(R.id.time_title_tv)
	TextView time_title_tv;//题头
	@Bind(R.id.add_tag)
	MemoryView tagView;
	@Bind(R.id.time_imsg)
	NineGridImageView mNglContent;//9宫图
	@Bind(R.id.time_unread_ll)
	LinearLayout time_unread_ll;//未读记忆
	@Bind(R.id.time_unreadnum_tv)
	TextView time_unreadnum_tv;//未读记忆数
	@Bind(R.id.memory_pic_tv)
	TextView memory_pic_tv;//图片数
	@Bind(R.id.memory_fork_tv)
	TextView memory_fork_tv;//点赞数
	@Bind(R.id.memory_comment_tv)
	TextView memory_comment_tv;//评论数
	@Bind(R.id.memory_add_tv)
	TextView memory_add_tv;//追加数

	private int mPosition;
	private String imgPath;
	private String imageOss;

	public MemoryDayHolder(View view) {
		super(view);
	}

	private int getIndex(int length) {
		int index = length % 5 + 1;
		return CPResourceUtil.getDrawableId(mContext, "commbg" + index);
	}

	public void init() {
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
		imageOss = mContext.getString(R.string.FSMYIMAGEOSS);
		NineGridImageViewAdapter<PhotoInfo> mAdapter = new NineGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, ImageView imageView, PhotoInfo entity) {
				String url = imgPath + entity.getPhotoPath() + imageOss;
				int index = getIndex(Math.abs(url.hashCode()));
				Picasso.with(context).load(url).centerCrop().resize(300, 300).error(index).placeholder(index).into(imageView);
			}
		};
		mNglContent.setAdapter(mAdapter);
	}

	/**
	 * 设置数据
	 *
	 * @param entity
	 */
	@Override
	public void setData(final Memory entity, final int positoin) {
		this.setData(mData);
		this.mPosition = positoin;
		if (entity.getPictureEntits() == null || entity.getPictureEntits().isEmpty()) {
			mNglContent.setVisibility(View.GONE);
		} else {
			mNglContent.setVisibility(View.VISIBLE);
			mNglContent.setImagesData(entity.getPictureEntits());
		}

		// 未读追加记忆数
		if (entity.getUnReadMPointAddCnt() != 0) {
			time_unread_ll.setVisibility(View.VISIBLE);
			time_unreadnum_tv.setText(String.valueOf(entity.getUnReadMPointAddCnt()));
		} else {
			time_unread_ll.setVisibility(View.GONE);
		}
		tagView.setTags(entity.getMemoryGroups());

		//地址
		if (!TextUtils.isEmpty(entity.getLocal())) {
			time_loc_tv.setVisibility(View.VISIBLE);
			time_loc_tv.setText(entity.getLocal());
		} else {
			time_loc_tv.setVisibility(View.GONE);
		}
		//题头
		time_title_tv.setText(entity.getTitle());
		//图片数
		memory_pic_tv.setText(entity.getPhotoCount() + "");
		//点赞
		memory_fork_tv.setText(entity.getPraiseCount() + "");
		//评论
		memory_comment_tv.setText(entity.getCommentCount() + "");
		//追加记忆
		memory_add_tv.setText(entity.getAddmemoryCount() + "");
		//日期
		time_date_tv.setText(entity.getMemoryDateForShow());

		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//全部已读
				entity.setUnReadMemoryCnt(0);
				mHolderCallBack.onClick(mPosition);
			}
		});
	}


}
