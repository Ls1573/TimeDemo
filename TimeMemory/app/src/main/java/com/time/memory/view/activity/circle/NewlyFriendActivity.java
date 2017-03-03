package com.time.memory.view.activity.circle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.OnChooseCallBack;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.GroupContacts;
import com.time.memory.gui.MyGridView;
import com.time.memory.gui.SideLetterBar;
import com.time.memory.presenter.NewlyFriendPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.CheckFriendsAdapter;
import com.time.memory.view.adapter.ChooseFriendsAdapter;
import com.time.memory.view.impl.IChooseView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:追加好友
 * @date 2016/10/20 16:33
 */
public class NewlyFriendActivity extends BaseActivity implements IChooseView, OnChooseCallBack, TextWatcher {
	private static final String TAG = "ChooseFriendActivity";

	@Bind(R.id.swipe_target)
	ListView swipe_target;
	@Bind(R.id.tv_side_letter_bar)
	SideLetterBar mLetterBar;//索引
	@Bind(R.id.tv_letter_overlay)
	TextView tv_letter_overlay;//提示字母
	@Bind(R.id.mgroup)
	MyGridView mgroup;//显示筛选头部
	@Bind(R.id.searchView)
	EditText searchView;//搜索

	private ChooseFriendsAdapter adapter;//朋友选择的
	private CheckFriendsAdapter cAdapter;//选择的
	private List<Contacts> mList;//好友信息
	private List<Contacts> cList;//选中信息
	private ArrayList<Contacts> fList;//筛选出的成员列表
	private ArrayList<GroupContacts> groups;
	private String groupId;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_choosefriend);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_choosefriend), R.drawable.image_back, getString(R.string.app_ok), -1);
	}

	@Override
	public void initData() {
		fList = new ArrayList<>();
		groups = getIntent().getParcelableArrayListExtra("groups");
		groupId = getIntent().getStringExtra("groupId");
		mLetterBar.setOverlay(tv_letter_overlay);
		mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
			@Override
			public void onLetterChanged(String letter) {
				int position = adapter.getLetterPosition(letter);
				swipe_target.setSelection(position);
			}
		});
		searchView.addTextChangedListener(this);
		//从数据库取数据
		((NewlyFriendPresenter) mPresenter).getContactsFromDb(groups, MainApplication.getUserId());
	}

	@Override
	public BasePresenter initPresenter() {
		return new NewlyFriendPresenter();
	}


	@OnClick(R.id.tv_main_right)
	public void onMyClcik(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.tv_main_right) {
			//追加好友请求
			((NewlyFriendPresenter) mPresenter).reqAddCirclepeoloe(getString(R.string.FSADDPEOPLEINC), MainApplication.getUserToken(), groupId, cList);
		}
	}

	@OnItemClick(R.id.mgroup)
	public void onItemClick(int position, View parent) {
		//点击
		cList.get(position).setIsCheck(false);
		adapter.notifyDataSetChanged();

		cList.remove(position);
		cAdapter.notifyDataSetChanged();
	}

	@Override
	public void onChooseBack() {
		//选择回调
		cAdapter.notifyDataSetChanged();
	}

	@Override
	public void setAdapter(List<Contacts> list) {
		if (swipe_target != null) {
			this.mList = list;
			this.fList.addAll(mList);
			this.cList = new ArrayList<>();
			adapter = new ChooseFriendsAdapter(mContext, MainApplication.getUserId(), mList, cList);
			adapter.setOnChooseCallBack(this);
			cAdapter = new CheckFriendsAdapter(cList, mContext);
			swipe_target.setAdapter(adapter);
			mgroup.setAdapter(cAdapter);
		}
	}

	@Override
	public void setFilterAdapter(ArrayList<Contacts> list) {
		mList.clear();
		mList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void closeActivity() {
		finish();
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();
		setResult(ReqConstant.ADDPEOPLE);
		finish();

	}

	@Override
	public void showFaild() {
		//数据发送失败
		hideLoadingDialog();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		//输入监听
		String keyWord = s.toString();
		((NewlyFriendPresenter) mPresenter).getContactsByWord(keyWord, fList);
	}
}
