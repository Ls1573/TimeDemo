package com.time.memory.view.holder;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Advert;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:广告
 * @date 2016/11/14 10:44
 */
public class AdvertHolder extends BaseHolder<Advert> {
	private static final String TAG = "AdvertHolder";

	@Bind(R.id.memoryd_advert_iv)
	ImageView memoryd_advert_iv;

	private String imgPath;

	public AdvertHolder(View view) {
		super(view);
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(final Advert entity, final int position) {
		super.setData(entity);
		if (!TextUtils.isEmpty(entity.getImgPath())) {
			//有数据
			Picasso.with(mContext).load(imgPath + entity.getImgPath()).config(Bitmap.Config.RGB_565).into(memoryd_advert_iv);
		} else {
			//无数据
			memoryd_advert_iv.setImageResource(R.drawable.commbg6);
		}

		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(entity.getImgPath())) {
					mHolderCallBack.onClick(-1, entity.getLinkUrl());
				}
			}
		});
	}
}
