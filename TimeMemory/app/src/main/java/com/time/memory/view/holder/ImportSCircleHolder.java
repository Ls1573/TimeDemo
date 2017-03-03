package com.time.memory.view.holder;

import android.view.View;

import com.time.memory.entity.MGroup;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:开启圈子
 * @date 2016/10/20 10:07
 */
public class ImportSCircleHolder extends BaseHolder<MGroup> {
	private static final String TAG = "ImportSCircleHolder";

//	@Bind(R.id.app_submit)
//	TextView app_submit;

	public ImportSCircleHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(MGroup entity, final int position) {
		super.setData(entity);
		//app_submit.setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick (View v){
//			mHolderCallBack.onClick(-1);
//		}
//	}
//	);
	}
}
