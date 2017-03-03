package com.time.memory.view.activity.mine;

import com.time.memory.R;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

/**
 * @author Qiu
 * @version V1.0
 * @Description:缓存
 * @date 2016/9/23 16:09
 */
public class CacheActivtiy extends BaseActivity implements IBaseView {
	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_cache);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_cache), R.drawable.image_back);
	}

	/***
	 * 进入下层计算图片缓存,db缓存,文件缓存,data/data/cache...
	 * 清除的时候，有选择的清除,主要是db
	 * 使用的Glide,到时取Glide的缓存文件大小
	 */
	@Override
	public void initData() {

	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}
}
