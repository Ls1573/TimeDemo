package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MGroup;

import butterknife.Bind;


/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:指定圈子
 * @date 2016/9/13 11:46
 * ==============================
 */
public class ImportCircleHolder extends BaseHolder<MGroup> {
	private static final String TAG = "AppointHolder";
	@Bind(R.id.memory_sign_iv)
	ImageView memory_sign_iv;//复选框
	@Bind(R.id.memory_desc_tv)
	TextView memory_desc_tv;//描述

	private int mPosition;

	public ImportCircleHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(mPosition);
			}
		});
	}

	@Override
	public void setData(MGroup entity, final int position) {
		super.setData(entity);
		mPosition = position;
		memory_desc_tv.setText(entity.getGroupName());
		boolean isActivect = false;
		if (entity.getActiveFlg().equals("0")) {
			//未激活
			isActivect = false;
		} else {
			//已激活
			isActivect = true;
		}
		//激活状态
		memory_sign_iv.setSelected(isActivect);

	}
}
