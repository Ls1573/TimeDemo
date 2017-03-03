package com.time.memory.view.activity.common;

import android.view.View;

import com.time.memory.R;
import com.time.memory.entity.MemoryGroup;
import com.time.memory.gui.MemoryTagView;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.3
 * @Description:测试tagView居中效果
 * @date 2017/1/5 9:53
 */
public class TestActivity extends BaseActivity {
	@Bind(R.id.add_tag)
	MemoryTagView tagView;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_test);
	}

	@Override
	public void initView() {
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public void initData() {
		List<MemoryGroup> tags = new ArrayList<>();
		tags.add(new MemoryGroup(1, "测试数的"));
		tags.add(new MemoryGroup(1, "哈哈据"));
		tags.add(new MemoryGroup(1, "哈哈据哈"));
		tags.add(new MemoryGroup(1, "哈哈测方式"));
		tags.add(new MemoryGroup(1, "哈哈"));
		tags.add(new MemoryGroup(1, "哈哈测到底的"));
		tagView.setTags(tags);
	}

	@OnClick(R.id.myClick)
	public void onClick(View view) {
		if (view.getId() == R.id.myClick) {
			List<MemoryGroup> tags = new ArrayList<>();
			tags.add(new MemoryGroup(1, "测试数的"));
			tags.add(new MemoryGroup(1, "哈哈据"));
			tags.add(new MemoryGroup(1, "哈哈据哈"));
			tags.add(new MemoryGroup(1, "哈哈测方式"));
			tags.add(new MemoryGroup(1, "哈哈"));
			tags.add(new MemoryGroup(1, "哈哈测到底的"));
			tags.add(new MemoryGroup(1, "哈哈测到底的爱说"));
			tagView.setTags(tags);
		}
	}
}
