package com.time.memory.view.holder;

import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.PraiseCallBack;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.MemoryPraise;
import com.time.memory.gui.MyGridView;
import com.time.memory.view.adapter.GridViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆点赞&&追加
 * @date 2016/10/28 12:07
 */
public class MemoryDForkHolder extends BaseHolder<MemoryInfo> implements PraiseCallBack {
	private static final String TAG = "MemoryDForkHolder";

	@Bind(R.id.memoryd_add_rl)
	RelativeLayout memoryd_add_rl;//点赞
	@Bind(R.id.memoryd_forks_tv)
	TextView memoryd_forks_tv;//点赞数
	@Bind(R.id.grid_view)
	MyGridView grid_view;//图片

	private GridViewAdapter adapter;

	public MemoryDForkHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		super.init();

	}

	@Override
	public void setData(MemoryInfo entity, int positoin) {
		super.setData(entity, positoin);
		List<MemoryPraise> praises = entity.getPraiserVos();
		adapter = new GridViewAdapter(mContext, praises);
		adapter.setPraiseCallBack(this);
		grid_view.setAdapter(adapter);
		int size = 0;
		if (praises != null && !praises.isEmpty())
			size = praises.size();
		//点赞数
		if (size != 0) {
			memoryd_forks_tv.setText(Html.fromHtml("此记忆有<font color=#E3CF9D>" + size + "</font>" + "人赞"));
		} else {
			memoryd_forks_tv.setText(mContext.getString(R.string.memory_priase_));
		}
	}

	@OnClick({R.id.memoryd_add_rl})
	public void onClick(View view) {
		if (view.getId() == R.id.memoryd_add_rl) {
			//追加记忆
			mHolderCallBack.onClick(7);
		}
	}

	@Override
	public void onPraiseCall(int position) {
		//点赞人回调
		mHolderCallBack.onClick(position, 2);
	}
}
