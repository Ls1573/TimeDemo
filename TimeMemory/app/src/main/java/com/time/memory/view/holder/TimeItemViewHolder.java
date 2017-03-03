package com.time.memory.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Memorys;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.MemoryView;
import com.time.memory.gui.nineGridImage.NineGridImageView;
import com.time.memory.gui.nineGridImage.NineGridImageViewAdapter;
import com.time.memory.util.CPResourceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Qiu
 * @version V1.0
 * @Description:时间轴内容
 * @date 2016/11/9 16:13
 */
public class TimeItemViewHolder extends BaseHolder<Memorys> {

	@Bind(R.id.time_title_tv)
	TextView time_title_tv;//题头
	@Bind(R.id.time_imsg)
	NineGridImageView mNglContent;//9宫图
	@Bind(R.id.memory_pic_tv)
	TextView memory_pic_tv;//图片数
	@Bind(R.id.memory_fork_tv)
	TextView memory_fork_tv;//点赞数
	@Bind(R.id.memory_comment_tv)
	TextView memory_comment_tv;//评论数
	@Bind(R.id.memory_add_tv)
	TextView memory_add_tv;//追加数
	@Bind(R.id.add_tag)
	MemoryView tagView;

	@Bind(R.id.time_loc_tv)
	TextView time_loc_tv;//地址
	@Bind(R.id.time_unread_ll)
	LinearLayout time_unread_ll;//未读记忆
	@Bind(R.id.time_unreadnum_tv)
	TextView time_unreadnum_tv;//未读记忆数
	@Bind(R.id.foot_view_ll)
	LinearLayout foot_view_ll;//加载更多
	@Bind(R.id.foot_view_pre)
	ProgressBar footPre;
	@Bind(R.id.foot_view_tv)
	TextView foot_view_tv;

	private String imgPath;
	private String imageOss;

	private int getIndex(int length) {
		int index = length % 5 + 1;
		return CPResourceUtil.getDrawableId(mContext, "commbg" + index);
	}

	//上拉加载更多
	public static final int PULLUP_LOAD_MORE = 0;
	//正在加载中
	public static final int LOADING_MORE = 1;
	//没有更多数据了
	public static final int NO_MORE_DATA = 2;

	public TimeItemViewHolder(View itemView) {
		super(itemView);
		ButterKnife.bind(this, itemView);
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
		imageOss = mContext.getString(R.string.FSIMAGEOSS);
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

	@Override
	public void setData(final Memorys memory) {
		super.setData(memory);
		time_title_tv.setText(memory.getMemory().getTitle());//题头
		mNglContent.setImagesData(memory.getMemory().getPictureEntits());//9宫图片

		tagView.setTags(memory.getMemory().getMemoryGroups());

		memory_pic_tv.setText(memory.getMemory().getPhotoCount() + "");//图片数
		memory_fork_tv.setText(memory.getMemory().getPraiseCount() + "");//点赞数
		memory_comment_tv.setText(memory.getMemory().getCommentCount() + "");//评论数
		memory_add_tv.setText(memory.getMemory().getAddmemoryCount() + "");//追加数

		// 未读追加记忆数
		if (memory.getMemory().getUnReadMemoryCnt() != 0) {
			time_unread_ll.setVisibility(View.VISIBLE);
			time_unreadnum_tv.setText(String.valueOf(memory.getMemory().getUnReadMemoryCnt()));
		} else {
			time_unread_ll.setVisibility(View.GONE);
		}
		//地址
		if (!TextUtils.isEmpty(memory.getMemory().getLocal())) {
			time_loc_tv.setVisibility(View.VISIBLE);
			time_loc_tv.setText(memory.getMemory().getLocal());
		} else {
			time_loc_tv.setVisibility(View.GONE);
		}
		//加载更多
		if (memory.isLast()) {
			foot_view_ll.setVisibility(View.VISIBLE);
			switch (memory.getState()) {
				case PULLUP_LOAD_MORE:
					foot_view_tv.setText("上拉加载更多记忆");
					footPre.setVisibility(View.VISIBLE);
					break;
				case LOADING_MORE:
					foot_view_tv.setText("正在加载更多记忆");
					footPre.setVisibility(View.VISIBLE);
				case NO_MORE_DATA:
					foot_view_tv.setText("无更多记忆");
					footPre.setVisibility(View.GONE);
					break;
			}
		} else {
			foot_view_ll.setVisibility(View.GONE);
		}
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(memory.getMemory().getMemoryId());
			}
		});
	}
}
