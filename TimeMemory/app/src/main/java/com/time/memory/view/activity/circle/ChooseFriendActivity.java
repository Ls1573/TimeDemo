package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.OnChooseCallBack;
import com.time.memory.entity.Contacts;
import com.time.memory.gui.MyGridView;
import com.time.memory.gui.SideLetterBar;
import com.time.memory.presenter.ChooseFriendPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.MainActivity;
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
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:选好友
 * @date 2016/9/12 19:05
 */
public class ChooseFriendActivity extends BaseActivity implements IChooseView, OnChooseCallBack, TextWatcher {
	private static final String TAG = "ChooseFriendActivity";

	@Bind(R.id.swipe_target)
	ListView swipe_target;
	@Bind(R.id.tv_side_letter_bar)
	SideLetterBar mLetterBar;//索引
	@Bind(R.id.tv_letter_overlay)
	TextView tv_letter_overlay;//提示字母
	@Bind(R.id.tv_main_right)
	TextView tv_main_right;//右标题
	@Bind(R.id.mgroup)
	MyGridView mgroup;//显示筛选头部
	@Bind(R.id.searchView)
	EditText searchView;//搜索
//	@Bind(R.id.view_line)
//	View view_line;//分割线

	private ChooseFriendsAdapter adapter;//朋友选择的
	private CheckFriendsAdapter cAdapter;//选择的
	private String title;//圈子名
	private boolean isCircle;
	private List<Contacts> mList;//好友信息
	private List<Contacts> fList;//筛选出的好友信息
	private List<Contacts> cList;//选中信息

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_choosefriend);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_choosefriend), getString(R.string.app_cancle), String.format(getString(R.string.choose_ok), 0), -1);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initData() {
		title = getIntent().getStringExtra("title");
		isCircle = getIntent().getBooleanExtra("isCircle", false);
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
		((ChooseFriendPresenter) mPresenter).getContactsFromDb();
		//数据回调监听
//		IMClientManager.getInstance(mContext).getTransDataListener().setImCallBack(this, ImConstant.IMCreateCircle);
	}

	@Override
	public BasePresenter initPresenter() {
		return new ChooseFriendPresenter();
	}

	@OnClick({R.id.tv_main_right, R.id.tv_main_left})
	public void onMyClcik(View view) {
		if (view.getId() == R.id.tv_main_left) {
			finish();
		} else if (view.getId() == R.id.tv_main_right) {
			//完成创建请求
			((ChooseFriendPresenter) mPresenter).reqCircle(getString(R.string.FSCREATEGROUP), MainApplication.getUserToken(), MainApplication.getUserId(), cList, title);
		}
	}

	@OnItemClick(R.id.mgroup)
	public void onItemClick(int position, View parent) {
		//点击
		cList.get(position).setIsCheck(false);
		adapter.notifyDataSetChanged();

		cList.remove(position);
		cAdapter.notifyDataSetChanged();

		//设置显示数
		int size = cList.size();
		tv_main_right.setText(String.format(getString(R.string.choose_ok), size));
		setGroupVisable(size);
	}

	@Override
	public void onChooseBack() {
		//选择回调
		cAdapter.notifyDataSetChanged();
		int size = cList.size();
		//设置显示数
		tv_main_right.setText(String.format(getString(R.string.choose_ok), size));
		setGroupVisable(size);
	}

	@Override
	public void setAdapter(List<Contacts> list) {
		if (swipe_target != null) {
			if (adapter == null) {
				this.mList = list;
				this.fList = new ArrayList<>();
				this.cList = new ArrayList<>();
				this.fList.addAll(mList);

				adapter = new ChooseFriendsAdapter(mContext, MainApplication.getUserId(), mList, cList);
				adapter.setOnChooseCallBack(this);
				cAdapter = new CheckFriendsAdapter(cList, mContext);
				swipe_target.setAdapter(adapter);
				mgroup.setAdapter(cAdapter);
			}
		}
	}

	/**
	 * 设置GridView显示
	 *
	 * @param size
	 */
	private void setGroupVisable(int size) {
		if (cList.size() == 0) {
			mgroup.setVisibility(View.GONE);
		} else {
			mgroup.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 筛选的List数据
	 *
	 * @param list
	 */
	@Override
	public void setFilterAdapter(ArrayList<Contacts> list) {
		mList.clear();
		mList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void closeActivity() {
		Intent intent = new Intent();
		if (isCircle) {
			intent.setClass(mContext, CirlceActivity.class);
		} else {
			intent.setClass(mContext, MainActivity.class);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();

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
		String keyWord = s.toString();
		((ChooseFriendPresenter) mPresenter).getContactsByWord(keyWord, fList);
	}
}
