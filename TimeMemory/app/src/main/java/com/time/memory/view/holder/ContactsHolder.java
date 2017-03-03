package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Contacts;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:联系人
 * @date 2016/9/24 15:19
 */
public class ContactsHolder extends BaseHolder<Contacts> {

	private static final String TAG = "ContactsHolder";
	@Bind(R.id.search_name_tv)
	TextView search_name_tv;//用户名

	@Bind(R.id.search_phone_iv)
	TextView search_phone_iv;//用户名

	@Bind(R.id.search_pic_iv)
	ImageView search_pic_iv;//用户头像


	public ContactsHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		//点击

	}

	@Override
	public void setData(Contacts mData,final int position) {
		super.setData(mData);
		search_name_tv.setText(mData.getContactName());
		search_phone_iv.setText(mData.getPhone());

		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);
			}
		});

	}
}

