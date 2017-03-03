package com.time.memory.view.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.time.memory.R;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:选择城市
 * @date 2016-9-6上午11:12:54
 * ==============================
 */
public class PickCountryHolder extends BaseHolder<Object> {
	@Bind(R.id.tv_pick_count)
	public TextView tv_pick_count;
	@Bind(R.id.tv_game)
	public TextView tv_game;

	public PickCountryHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		super.init();
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
	}

	@Override
	public void setData(Object mData) {
		super.setData(mData);
		tv_game.setText("game_Name");
		tv_pick_count.setText("play_Count");
	}

}
