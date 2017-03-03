package com.time.memory.view.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.GroupContacts;
import com.time.memory.gui.CircleImageTransformation;
import com.time.memory.gui.MyImageView;
import com.time.memory.gui.MyTextView;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子成员
 * @date 2016/12/13 12:03
 */
public class GroupHolder extends BaseHolder<GroupContacts> {
	private static final String TAG = "GroupHolder";

	@Bind(R.id.circle_constant_tv)
	MyTextView constantTv;//好友名
	@Bind(R.id.circle_constantname_tv)
	TextView constantnameTv;//用户名
	@Bind(R.id.circle_constant_iv)
	MyImageView circle_constant_iv;//好友头像

	private String imgOss;
	private int position;

	public GroupHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		this.imgOss = mContext.getString(R.string.FSIMAGEPATH);
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);
			}
		});
	}

	@Override
	public void setData(GroupContacts groupContacts, int position) {
		super.setData(groupContacts);
		this.position = position;
		//添加
		if (groupContacts.getState() == 1) {
			circle_constant_iv.setVisibility(View.VISIBLE);
			circle_constant_iv.setBackgroundResource(R.drawable.groupadd);
			constantTv.setVisibility(View.GONE);
			constantnameTv.setVisibility(View.GONE);
		} else if (groupContacts.getState() == 2) {
			//删除
			circle_constant_iv.setVisibility(View.VISIBLE);
			circle_constant_iv.setBackgroundResource(R.drawable.groupdel);
			constantTv.setVisibility(View.GONE);
			constantnameTv.setVisibility(View.GONE);
		} else {
			//用户名
			constantnameTv.setVisibility(View.VISIBLE);
			constantnameTv.setText(groupContacts.getUserName());

			//头像为空
			if (TextUtils.isEmpty(groupContacts.getHeadPhoto())) {
				//设置用户名
				constantTv.setText(groupContacts.getSubName());
				constantTv.setVisibility(View.VISIBLE);
				circle_constant_iv.setVisibility(View.GONE);
			} else {
				circle_constant_iv.setVisibility(View.VISIBLE);
				constantTv.setVisibility(View.GONE);
				Picasso.with(mContext).load(imgOss + groupContacts.getHeadPhoto()).transform(new CircleImageTransformation()).error(R.drawable.login_photo).placeholder(R.drawable.login_photo).into(circle_constant_iv);
			}
		}
	}
}
