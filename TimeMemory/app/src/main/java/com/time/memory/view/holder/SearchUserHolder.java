package com.time.memory.view.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.GroupContacts;
import com.time.memory.gui.CircleImageTransformation;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.3
 * @Description:搜索好友
 * @date 2017/1/3 14:10
 */
public class SearchUserHolder extends BaseHolder<GroupContacts> {
	@Bind(R.id.searchuserp_tv)
	TextView searchPhoneTv;//手机号
	@Bind(R.id.searchuser_tv)
	TextView searchUserTv;//用户名
	@Bind(R.id.searchuser_iv)
	ImageView searchuserIv;//头像

	private String imgWeb;
	private String imgOss;

	public SearchUserHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		imgWeb = mContext.getString(R.string.FSIMAGEPATH);
		imgOss = mContext.getString(R.string.FSIMAGEOSS);
	}

	@Override
	public void setData(GroupContacts entity, final int position) {
		super.setData(entity);
		//名
		searchUserTv.setText(entity.getUserName());
		//手机号
		searchPhoneTv.setText(entity.getUserMobile());
		//头像
		if (!TextUtils.isEmpty(entity.getHeadPhoto()))
			Picasso.with(mContext).load(imgWeb + entity.getHeadPhoto() + imgOss).transform(new CircleImageTransformation()).error(R.drawable.login_photo).placeholder(R.drawable.login_photo).into(searchuserIv);

		//点击回调
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);
			}
		});
	}
}
