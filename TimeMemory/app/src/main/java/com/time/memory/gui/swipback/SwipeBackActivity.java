package com.time.memory.gui.swipback;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.time.memory.R;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:滑动退出
 * @date 2016/11/29 17:08
 */
public class SwipeBackActivity extends BaseActivity implements SwipeBackLayout.SwipeBackListener {

	private SwipeBackLayout swipeBackLayout;
	private ImageView ivShadow;


	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(getContainer());
		View view = LayoutInflater.from(this).inflate(layoutResID, null);
		swipeBackLayout.addView(view);
		setDragEdge(SwipeBackLayout.DragEdge.LEFT);
	}

	private View getContainer() {
		RelativeLayout container = new RelativeLayout(this);
		swipeBackLayout = new SwipeBackLayout(this);
		swipeBackLayout.setOnSwipeBackListener(this);
		ivShadow = new ImageView(this);
		ivShadow.setBackgroundColor(getResources().getColor(R.color.black_p50));
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		container.addView(ivShadow, params);
		container.addView(swipeBackLayout);
		return container;
	}

	public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
		swipeBackLayout.setDragEdge(dragEdge);
	}

	public SwipeBackLayout getSwipeBackLayout() {
		return swipeBackLayout;
	}

	public void setSwipeEnable(boolean isEnable){
		swipeBackLayout.setEnablePullToBack(isEnable);
	}


	@Override
	public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
		ivShadow.setAlpha(1 - fractionScreen);
	}


	@Override
	public void onCreateMyView() {
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}


}
