package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;

import com.time.memory.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:写记忆-页脚
 * @date 2016-10-6下午3:09:16
 * ==============================
 */
public class WriterFooterHolder extends BaseHolder<Object> {
	@Bind(R.id.writer_more_iv)
	ImageView writer_more_iv;//添加

	private int mPosition;

	public WriterFooterHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(Object entity, int position) {
		super.setData(entity);
		this.mPosition = position;
	}

	@OnClick({R.id.writer_more_iv})
	public void onClick(View view) {
		if (view.getId() == R.id.writer_more_iv) {
			//尾部追加
			mHolderCallBack.onClick(mPosition, -1, 7);
		}
	}
}
