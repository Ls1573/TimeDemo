package com.time.memory.view.holder;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Contacts;
import com.time.memory.gui.CircleImageView;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子
 * @date 2016/10/26 18:02
 */
public class CircleSearchHolder extends BaseHolder<Contacts> {
	@Bind(R.id.circle_pic_iv)
	CircleImageView circle_pic_iv;//头像
	@Bind(R.id.circle_constant_tv)
	TextView circle_constant_tv;//姓名

	private int actictedColor;
	private int unActictedColor;
	private String imgWeb;
	private int mPosition;

	public CircleSearchHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		actictedColor = mContext.getResources().getColor(R.color.yellow_d9);
		unActictedColor = mContext.getResources().getColor(R.color.common_font_black);
		imgWeb = mContext.getString(R.string.FSIMAGEPATH);
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(mPosition);
			}
		});
	}

	@Override
	public void setData(Contacts entity, int position) {
		super.setData(entity);
		this.mPosition = position;
		circle_constant_tv.setText(entity.getContactName());
		String pic = entity.getHeadPhoto();
		if (!TextUtils.isEmpty(pic))
			Picasso.with(mContext).load(imgWeb + pic).config(Bitmap.Config.RGB_565).placeholder(R.drawable.circie_mine).error(R.drawable.circie_mine).into(circle_pic_iv);
		if (entity.getActiveFlg().equals("0")) {
			//已激活
			circle_constant_tv.setTextColor(actictedColor);
		} else {
			circle_constant_tv.setTextColor(unActictedColor);
		}
	}
}
