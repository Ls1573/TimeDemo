package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Reminds;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MyUnreadMemory extends BaseHolder<Reminds> {
	private static final String TAG = "MyUnreadMemory";
	@Bind(R.id.user_img)
	ImageView userImg;//用户头像
	@Bind(R.id.user_name)
	TextView userName;//用户名
	@Bind(R.id.time)
	TextView time;//时间
	@Bind(R.id.user_content)
	TextView userContent;//内容

	private int mPosition;

	public MyUnreadMemory(View view) {
		super(view);
	}

	/**
	 * 设置数据
	 *
	 * @param mData
	 */


	@Override
	public void setData(final Reminds mData, final int positoin) {
		super.setData(mData, positoin);
		userName.setText(mData.getUserName());
		//切正方形图片
		if (mData.getUserHphoto() != null) {
			Picasso.with(mContext).load(mData.getUserHphoto()).centerCrop().resize(300, 300).error(R.drawable.friend_photo).placeholder(R.drawable.friend_photo).into(userImg);
		} else {
			userImg.setBackgroundResource(R.drawable.friend_photo);
		}
		time.setText(mData.getInsDateForShow());
		userContent.setText(mData.getTitle());
		//点击item
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(positoin, mData);
			}
		});

	}


}
