package com.time.memory.view.activity.circle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.callback.OnFriendCallBack;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.ClearEditText;
import com.time.memory.gui.SideLetterBar;
import com.time.memory.presenter.DelCirclePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.DeleteCircleAdapter;
import com.time.memory.view.impl.IDelCircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:圈子成员列表
 * @date 2016/9/13 14:02
 * ==============================
 */
public class DeleteCircleActivity extends BaseActivity implements IDelCircleView, OnFriendCallBack, TextWatcher {

	private static final String TAG = "ShowCircleActivity";

	@Bind(R.id.swipe_target)
	ListView swipe_target;
	@Bind(R.id.common_title_ll)
	RelativeLayout common_title_ll;
	@Bind(R.id.tv_side_letter_bar)
	SideLetterBar mLetterBar;//索引
	@Bind(R.id.tv_letter_overlay)
	TextView tv_letter_overlay;//提示字母
	@Bind(R.id.tv_main_title)
	TextView tv_main_title;//题头
	@Bind(R.id.circle_search_et)
	ClearEditText searchEt;

	private DeleteCircleAdapter adapter;

	private MGroup group;
	private boolean isDelete = false;//删除用户


	private ArrayList<GroupContacts> mList;//成员列表
	private ArrayList<GroupContacts> fList;//筛选出的成员列表
	private List<GroupContacts> cList;//选中信息


	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_deletecircle);
	}

	@Override
	public void initView() {
		group = getIntent().getParcelableExtra("group");
		initTopBarForBoth(group.getGroupName(), R.drawable.image_back, getString(R.string.app_delete), -1);
	}

	@Override
	public void initData() {
		mList = new ArrayList<>();
		cList = new ArrayList<>();
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
		//请求数据
		((DelCirclePresenter) mPresenter).reqCirlcePerple(getString(R.string.FSGETGROUPPEOPLES), group.getGroupId());
	}

	@Override
	public BasePresenter initPresenter() {
		return new DelCirclePresenter();
	}

	@OnClick({R.id.tv_main_right})
	public void onClick(View view) {
		hideAddTag();
		if (view.getId() == R.id.tv_main_right) {
			//移除
			showDialog();
		} else if (view.getId() == R.id.app_cancle) {
			setMyResult();
		}
	}

	@Override
	public void onChooseBack(int position) {
		//点击回调
		CLog.e(TAG, "position:" + position);
		deleteOpr(position);
	}

	@Override
	public void setAdapter(ArrayList<GroupContacts> list) {
		if (swipe_target != null) {
			cList.clear();
			mList.clear();
			fList.clear();

			mList.addAll(list);
			fList.addAll(mList);

			if (adapter == null) {
				adapter = new DeleteCircleAdapter(mContext, mList);
				adapter.setOnFriendCallBack(this);
				swipe_target.setAdapter(adapter);
			} else {
				adapter.notifityAdapter();
			}
			CLog.e(TAG, "mList:" + mList.size());
		}
	}

	@Override
	public void setFilterAdapter(ArrayList<GroupContacts> list) {
		mList.clear();
		mList.addAll(list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void removeUses() {
		//移除用户
		for (GroupContacts entity : cList) {
			mList.remove(entity);
			fList.remove(entity);
		}
		isDelete = true;
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

	@Override
	public void onBackPressed() {
		setMyResult();
	}

	/**
	 * 删除模式操作
	 */
	private void deleteOpr(int position) {
		GroupContacts entity = mList.get(position);
		if (entity.getUserId().equals(MainApplication.getUserId())) {
			//当前管理员,不操作
			showShortToast("你是管理员,不可以删除自己");
			return;
		}
		if (cList.contains(entity)) {
			//已有就拿掉
			entity.setIsCheck(false);
			cList.remove(entity);
		} else {
			//没有就加入
			entity.setIsCheck(true);
			cList.add(entity);
		}
		adapter.notifyDataSetChanged();
		CLog.e(TAG, "cList:" + cList.size());
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
		((DelCirclePresenter) mPresenter).getContactsByWord(keyWord, fList);
	}

	/**
	 * 隐藏
	 */
	private void hideAddTag() {
		searchEt.setText("");
		KeyBoardUtils.HideKeyboard(searchEt);
	}

	/**
	 * 返回
	 */
	public void setMyResult() {
		//成功
		if (isDelete)
			setResult(ReqConstant.REQUEST_CODE_GROUP);
		finish();
	}

	private void showDialog() {
		DialogUtils.request(DeleteCircleActivity.this, "是否确定删除?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isTrue = (boolean) data;
				if (isTrue)
					((DelCirclePresenter) mPresenter).deleteUsers(getString(R.string.FSREQDEUSER), group.getGroupId(), cList);
			}
		});
	}

}
