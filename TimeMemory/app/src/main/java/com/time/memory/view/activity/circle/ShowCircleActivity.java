package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.ClearEditText;
import com.time.memory.gui.SideLetterBar;
import com.time.memory.presenter.ShowCirclePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.ShowCircleAdapter;
import com.time.memory.view.impl.IShowCircleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnItemClick;


/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:圈子成员列表
 * @date 2016/9/13 14:02
 * ==============================
 */
public class ShowCircleActivity extends BaseActivity implements IShowCircleView, TextWatcher {

	private static final String TAG = "ShowCircleActivity";

	@Bind(R.id.swipe_target)
	ListView swipe_target;
	@Bind(R.id.tv_side_letter_bar)
	SideLetterBar mLetterBar;//索引
	@Bind(R.id.tv_letter_overlay)
	TextView tv_letter_overlay;//提示字母
	@Bind(R.id.circle_search_et)
	ClearEditText searchEt;

	private ShowCircleAdapter adapter;
	private MGroup group;

	private ArrayList<GroupContacts> mList;//成员列表
	private ArrayList<GroupContacts> fList;//筛选出的成员列表

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_showcircle);
	}

	@Override
	public void initView() {
		group = getIntent().getParcelableExtra("group");
		mList = getIntent().getParcelableArrayListExtra("groupContacts");
		initTopBarForLeft(group.getGroupName(), R.drawable.image_back);
	}

	@Override
	public void initData() {
		fList = new ArrayList<>();
		searchEt.addTextChangedListener(this);

		mLetterBar.setOverlay(tv_letter_overlay);
		mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
			@Override
			public void onLetterChanged(String letter) {
				int position = adapter.getLetterPosition(letter);
				swipe_target.setSelection(position);
			}
		});
		((ShowCirclePresenter) mPresenter).convertAdapter(mList);
	}

	@Override
	public BasePresenter initPresenter() {
		return new ShowCirclePresenter();
	}

	@Override
	public void setAdapter(ArrayList<GroupContacts> list) {
		if (swipe_target != null) {
			if (adapter == null) {
				fList.addAll(list);
				adapter = new ShowCircleAdapter(mContext, list);
				swipe_target.setAdapter(adapter);
			}
		}
	}

	@Override
	public void setFilterAdapter(ArrayList<GroupContacts> list) {
		mList.clear();
		mList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	@OnItemClick(R.id.swipe_target)
	public void onItemClick(int position, View parent) {
		watchOpr(position);
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
		((ShowCirclePresenter) mPresenter).getContactsByWord(keyWord, fList);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		String userName = intent.getStringExtra("userName");
		int position = intent.getIntExtra("position", -1);
		if (position != -1) {
			mList.get(position).setUserName(userName);
			adapter.notifyDataSetChanged();
		}
		intent.removeExtra("position");
		intent.removeExtra("userName");
	}

	/**
	 * 观察模式操作
	 *
	 * @param position
	 */
	private void watchOpr(int position) {
		GroupContacts contacts = mList.get(position);
		Intent intent = new Intent();
		if (contacts.getUserId().equals(MainApplication.getUserId())) {
			//是自己
			intent.setClass(mContext, FriendActivity.class);
		}
		//判断是不是好友
		else if (contacts.getIsFriend().equals("1")) {
			//==1->是好友
			intent.setClass(mContext, FriendActivity.class);
		} else {
			//不是好友去到加好友页面
			intent.setClass(mContext, AddFriendActivity.class);
		}
		intent.putExtra("userId", contacts.getUserId());
		intent.putExtra("userName", contacts.getUserName());
		intent.putExtra("hPic", contacts.getHeadPhoto());
		intent.putExtra("position", position);
		startAnimActivity(intent);
	}

	/**
	 * 隐藏
	 */
	private void hideAddTag() {
		searchEt.setText("");
		KeyBoardUtils.HideKeyboard(searchEt);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
	}
}
