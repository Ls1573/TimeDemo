package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.time.memory.R;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.presenter.EditPPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerHeaderAdapter;
import com.time.memory.view.holder.EditPHolder;
import com.time.memory.view.holder.PriviewStyleHolder;
import com.time.memory.view.impl.IEditPreviewView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑预览
 * @date 2016/10/26 8:55
 */
public class EditPMActivity extends BaseActivity implements IEditPreviewView {
	private static final String TAG = "PreviewMActivity";
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;

	private List<MemoryEdit> mList;//数据集
	private WriterStyleMemory styleMemory;//主题头
	private BaseRecyclerHeaderAdapter adapter;
	private int state;//上传给谁
	private String Id;//对方Id

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_writermemory);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_preview), R.drawable.image_back, getString(R.string.app_release), -1);
	}

	@Override
	public void initData() {
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		List<MemoryEdit> list = getIntent().getParcelableArrayListExtra("writers");
		styleMemory = getIntent().getParcelableExtra("style");
		state = getIntent().getIntExtra("state", 1);
		Id = getIntent().getStringExtra("Id");

		//筛选数据
		((EditPPresenter) mPresenter).filterMemorys(list);
	}

	@Override
	public BasePresenter initPresenter() {
		return new EditPPresenter();
	}

	@Override
	public void setAdapter(List<MemoryEdit> list) {
		if (adapter == null) {
			this.mList = list;
			adapter = new BaseRecyclerHeaderAdapter(mList, R.layout.item_previcememory, EditPHolder.class,
					styleMemory, R.layout.item_previewstyle, PriviewStyleHolder.class);
			adapter.setHeaderVisable(true);
			swipe_target.setAdapter(adapter);
		}
	}

	@Override
	public void upSuccess() {
		Intent Intent = new Intent(mContext, MemoryDetailActivtiy.class);
		Intent.putExtra("editMemory", 1);
		Intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startAnimActivity(Intent);
		finish();
	}

	@OnClick(R.id.tv_main_right)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.tv_main_right) {
			//下一步(发送)
			((EditPPresenter) mPresenter).upLoadMemory(mList, styleMemory, state, Id);
		}
	}

	@Override
	public void showSuccess() {
		//发布成功
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}
}
