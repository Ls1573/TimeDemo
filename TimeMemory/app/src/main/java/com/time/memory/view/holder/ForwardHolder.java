package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MGroup;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:转一转
 * @date 2016/11/4 16:56
 */
public class ForwardHolder extends BaseHolder<MGroup> {
	private static final String TAG = "ForwardHolder";
	@Bind(R.id.circle_constant_tv)
	TextView circle_constant_tv;//联系人
	@Bind(R.id.circle_check)
	ImageView circle_check;//复选

	private int mPosition;

	public ForwardHolder(View view) {
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
	}

	@Override
	public void setData(MGroup entity, final int position) {
		super.setData(entity);
		this.mPosition = position;
		circle_constant_tv.setText(entity.getGroupName());
		circle_check.setSelected(entity.isCheck());
	}
}
