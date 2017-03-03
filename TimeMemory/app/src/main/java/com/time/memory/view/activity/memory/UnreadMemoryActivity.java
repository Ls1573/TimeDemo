package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.Memory;
import com.time.memory.entity.Reminds;
import com.time.memory.presenter.MyUnreadMemoryPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.MyUnreadMemory;
import com.time.memory.view.impl.IUnreadMemory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 未读&&补充记忆
 */
public class UnreadMemoryActivity extends BaseActivity implements IUnreadMemory, AdapterCallback {
	public static final String TAG = "UnreadMemoryActivity";

	@Bind(R.id.swipe_target)
	RecyclerView unreadMemory;

	private String groupId;
	private int position;

	private BaseRecyclerAdapter adapter;
	private List<Reminds> mList;//未读内容

	private boolean isCanLoad = true;
	private int curPage = 1;//当前页
	private int pageCount = 5;//每页显示数

	private String type;//判断是新增的还是补充的 标识
	private String tage;//为了确定调用那个接口的 标识（取消掉未读信息）

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_unread_memory);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MyUnreadMemoryPresenter();
	}

	@Override
	public void initView() {
		type = getIntent().getStringExtra("type");
		groupId = getIntent().getStringExtra("groupId");
		position = getIntent().getIntExtra("position", 0);

		if (TextUtils.isEmpty(type)) finish();
		//设置标题
		if (type.equals("0")) {
			initTopBarForLeft("未读补充记忆", R.drawable.image_back);
		} else if (type.equals("1")) {
			initTopBarForLeft("未读记忆", R.drawable.image_back);
		}

		initLoading();
		fl_loading.withEmptyIco(R.drawable.commentemp)
				.withLoadedEmptyText("")
				.withBtnEmptyEnnable(false);

	}

	@Override
	public void initData() {
		mList = new ArrayList<>();
		unreadMemory.setLayoutManager(new LinearLayoutManager(mContext));
		//去闪屏
		((DefaultItemAnimator) unreadMemory.getItemAnimator()).setSupportsChangeAnimations(false);
		//刷新列表
		if (type.equals("1")) {
			//新增
			getAddMemorys();
		} else {
			//补充
			getNewMemorys();
		}
	}

	/**
	 * 获取新增记忆
	 */
	private void getAddMemorys() {
		if (position > 0) {
			tage = "group";
			((MyUnreadMemoryPresenter) mPresenter).getCircleUnread(getString(R.string.FSUNREADCIRCLE), "CF05", groupId);
		}
	}

	/**
	 * 获取补充记忆
	 */
	private void getNewMemorys() {
		if (position == 0) {
			tage = "mine";
			((MyUnreadMemoryPresenter) mPresenter).getMyUnread(getString(R.string.FSUNREAD), "CF03");
		} else {
			tage = "group";
			((MyUnreadMemoryPresenter) mPresenter).getCircleUnread(getString(R.string.FSSUPPLEMENTGROUP), "CF07", groupId);
		}
	}

	@Override
	public void setAdapter(ArrayList<Reminds> list) {
		if (unreadMemory != null) {
			if (adapter == null) {
				this.mList = list;
				adapter = new BaseRecyclerAdapter(mList, R.layout.item_unread_memory, MyUnreadMemory.class);
				adapter.setCallBack(this);
				unreadMemory.setAdapter(adapter);
			} else {
				this.mList.clear();
				adapter.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void refreshAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	@Override
	public void closeActivity() {
		finish();
	}

	@Override
	public void showSuccess() {
		super.showSuccess();
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		super.showFaild();
		hideLoadingDialog();
	}

	@Override
	public void showEmpty() {
		hideLoadingDialog();
		//结果为空
		isCanLoad = false;
		mList.clear();
		if (adapter != null)
			adapter.notifyDataSetChanged();
		unreadMemory.setVisibility(View.GONE);
		super.showEmpty();
	}

	@Override
	public void onDataCallBack(Object data, int index) {
		Reminds reminds = (Reminds) data;
		String msg = "";
		if (type.equals("1")) {
			//新增
			if (tage.equals("other")) {
				msg = "CF04";
				((MyUnreadMemoryPresenter) mPresenter).upGroupNewInfo("2", "", MainApplication.getUserId(), -1);
			} else if (tage.equals("group")) {
				msg = "CF05";
				((MyUnreadMemoryPresenter) mPresenter).upGroupNewInfo("3", groupId, MainApplication.getUserId(), -1);
			}
		} else {
			//补充
			if (tage.equals("mine")) {
				msg = "CF03";
				((MyUnreadMemoryPresenter) mPresenter).upGroupInfo("1", "", MainApplication.getUserId(), -1);
			} else if (tage.equals("other")) {
				msg = "CF06";
				((MyUnreadMemoryPresenter) mPresenter).upGroupInfo("2", "", MainApplication.getUserId(), -1);
			} else if (tage.equals("group")) {
				msg = "CF07";
				((MyUnreadMemoryPresenter) mPresenter).upGroupInfo("3", groupId, MainApplication.getUserId(), -1);
			}
		}
		Intent intent = new Intent(this, MemoryDetailActivtiy.class);
		if (this.position == 0) {
			ArrayList<Memory> memories = new ArrayList<>();
			memories.add(new Memory(reminds.getMemoryId(), reminds.getMemorySrcId(), reminds.getUserId(), reminds.getTitle()));
			intent.putExtra("groupId", "");
			intent.putExtra("memorys", memories);
			intent.putExtra("state", 1);
			intent.putExtra("type", 1);
			intent.putExtra("title", "我的记忆");
		} else {
			ArrayList<GroupMemory> groupList = new ArrayList<>();
			groupList.add(new GroupMemory(reminds.getMemoryId(), reminds.getMemorySrcId(), reminds.getUserId(), reminds.getTitle()));
			intent.putExtra("groupList", groupList);
			intent.putExtra("groupId", groupId);
			intent.putExtra("state", 2);
			intent.putExtra("type", 1);
			intent.putExtra("title", reminds.getTitle());
		}
		intent.putExtra("isSignle", true);
		intent.putExtra("pageCount", 5);
		intent.putExtra("position", 0);
		startActivity(intent);
		//移除
		((MyUnreadMemoryPresenter) mPresenter).removeList(index, reminds, mList, msg, groupId);
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
	}

	@Override
	public void onCallback(Object data) {
	}
}
