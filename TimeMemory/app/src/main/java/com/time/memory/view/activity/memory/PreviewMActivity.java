package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.R;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.WriterMemory;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.presenter.PreViewPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerHeaderAdapter;
import com.time.memory.view.holder.PreviewHolder;
import com.time.memory.view.holder.PriviewStyleHolder;
import com.time.memory.view.impl.IPreviewView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆预览
 * @date 2016/10/11 11:39
 */
public class PreviewMActivity extends BaseActivity implements IPreviewView {
	private static final String TAG = "PreviewMActivity";
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;

	private List<WriterMemory> mList;//数据集
	private WriterStyleMemory styleMemory;//主题头
	private BaseRecyclerHeaderAdapter adapter;
	private int state;//上传给谁(旧的)
	private int nState;//上传给谁(当前的)
	private String Id;//
	private String name;//对方的名
	private String className;//类名

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

		List<WriterMemory> list = getIntent().getParcelableArrayListExtra("writers");
		styleMemory = getIntent().getParcelableExtra("style");
		state = getIntent().getIntExtra("state", 1);
		nState = getIntent().getIntExtra("nState", 1);
		Id = getIntent().getStringExtra("Id");
		name = getIntent().getStringExtra("name");
		className = getIntent().getStringExtra("className");

		CLog.e(TAG, "name:" + name + "  className:" + className);
		//筛选数据
		((PreViewPresenter) mPresenter).filterMemorys(list);
	}

	@Override
	public BasePresenter initPresenter() {
		return new PreViewPresenter();
	}

	@Override
	public void setAdapter(List<WriterMemory> list) {
		if (adapter == null) {
			this.mList = list;
			adapter = new BaseRecyclerHeaderAdapter(mList, R.layout.item_previcememory, PreviewHolder.class,
					styleMemory, R.layout.item_previewstyle, PriviewStyleHolder.class);
			adapter.setHeaderVisable(true);
			swipe_target.setAdapter(adapter);
		}
	}

	/**
	 * 上传成功,分发
	 *
	 * @param memory
	 */
	@Override
	public void setMemory(TempMemory memory) {
		try {
			Class clas;
			if (!TextUtils.isEmpty(className)) {
				clas = Class.forName(className);
			} else {
				if (state == 1) {
					//我的
					clas = MyMemoryActivtiy.class;
				} else if (state == 0) {
					//他的
					clas = MemoryOtherActivity.class;
				} else {
					//群的
					clas = MemoryGroupActivity.class;
				}
			}
			Intent Intent = new Intent(mContext, clas);
			if (nState == 1) {
				name = "私密记忆";
			}
			memory.setMemoryOwner(name);

			//同一个
			if (state == nState)
				Intent.putExtra("tempMemory", memory);
			else if (state == 1)
				//原来的是从我的记忆进入-->可以返回的
				Intent.putExtra("tempMemory", memory);
			Intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startAnimActivity(Intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finish();
	}

	@OnClick(R.id.tv_main_right)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.tv_main_right) {
			//下一步(发送)
			((PreViewPresenter) mPresenter).upLoadMemory(mList, styleMemory, nState, Id);
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}
}
