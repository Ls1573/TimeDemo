package com.time.memory.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0 ==============================
 * @Description:我的记忆填充
 * @date 2016-9-9下午2:56:18
 */
public class FragmentAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragmentList;

	public FragmentAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
		super(fm);
		this.mFragmentList = mFragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}

}
