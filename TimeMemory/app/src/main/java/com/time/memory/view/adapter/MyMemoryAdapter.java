package com.time.memory.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆详情翻页
 * @date 2016/9/18 8:53
 */
public class MyMemoryAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = "MyMemoryAdapter";
	private List<Fragment> fragments;

	public MyMemoryAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}