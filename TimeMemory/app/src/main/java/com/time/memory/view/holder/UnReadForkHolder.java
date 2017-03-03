package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.UnForkDto;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:未读点赞评论
 * @date 2016/11/24 16:14
 */
public class UnReadForkHolder extends BaseHolder<UnForkDto> {
	private static final String TAG = "MyUnreadMemory";
	@Bind(R.id.user_img)
	ImageView userImg;//用户头像
	@Bind(R.id.user_name)
	TextView userName;//用户名
	@Bind(R.id.time)
	TextView time;//时间
	@Bind(R.id.user_content)
	TextView userContent;//内容
	@Bind(R.id.fork_title_tv)
	TextView titleTv;//Title

	private String imgPath;
	private String imgOSS;
	private int mPosition;

	public UnReadForkHolder(View view) {
		super(view);
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(mPosition);
			}
		});
	}

	@Override
	public void init() {
		super.init();
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
		imgOSS = mContext.getString(R.string.FSIMAGEOSS);
	}

	@Override
	public void setData(final UnForkDto mData, final int positoin) {
		super.setData(mData, positoin);
		this.mPosition = positoin;
		if (mData.getHphoto() != null) {
			Picasso.with(mContext).load(imgPath + mData.getHphoto() + imgOSS).centerCrop().resize(400, 400).error(R.drawable.friend_photo).placeholder(R.drawable.friend_photo).into(userImg);
		} else {
			userImg.setBackgroundResource(R.drawable.friend_photo);
		}
		//用户
		userName.setText(mData.getUname());
		//时间
		time.setText(mData.getInsdForShow());

		//Title flag(评论：1) (点赞：2)
		String content = null;
		if (mData.getFlag().equals("1")) {
			//评论数据
			userContent.setVisibility(View.VISIBLE);
			titleTv.setText("评论了\"" + mData.getMtitle() + "\"");
			userContent.setText(mData.getTitle());
		} else {
			//点赞
			userContent.setVisibility(View.GONE);
			titleTv.setText("点赞了\"" + mData.getMtitle() + "\"");
		}
	}
}
