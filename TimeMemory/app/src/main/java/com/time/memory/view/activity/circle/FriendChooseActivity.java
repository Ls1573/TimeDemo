package com.time.memory.view.activity.circle;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.GroupContacts;
import com.time.memory.gui.ClearEditText;
import com.time.memory.presenter.FriendChoosePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.util.ShareUtil;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.SearchUserHolder;
import com.time.memory.view.impl.IFriendChooseView;
import com.zbar.lib.CaptureActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.3
 * @Description:添加好友方式选择
 * @date 2016/12/29 11:02
 */
public class FriendChooseActivity extends BaseActivity implements IFriendChooseView, EasyPermissions.PermissionCallbacks, TextWatcher, TextView.OnEditorActionListener {

	@Bind(R.id.friend_search_et)
	ClearEditText searchEt;

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	@Bind(R.id.friend_choose_ll)
	LinearLayout chooseLl;

	private ShareUtil shareUtil;//分享工具类
	private BaseRecyclerAdapter adapter;
	private List<GroupContacts> users;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_friendchoose);
	}

	@Override
	protected void onDestroy() {
		if (users != null) {
			users.clear();
			users = null;
		}
		shareUtil = null;
		super.onDestroy();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_friend_choose), R.drawable.image_back);
	}

	@Override
	public void initData() {
		shareUtil = new ShareUtil();
		searchEt.addTextChangedListener(this);
		searchEt.setOnEditorActionListener(this);

		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
	}

	@Override
	public BasePresenter initPresenter() {
		return new FriendChoosePresenter();
	}


	@OnClick({R.id.friend_scan_ll, R.id.friend_contacts_ll, R.id.friend_wechat_ll, R.id.friend_circle_ll, R.id.app_cancle})
	public void onClick(View view) {
		int Id = view.getId();
		switch (Id) {
			case R.id.app_cancle:
				KeyBoardUtils.HideKeyboard(searchEt);
				finish();
				break;
			case R.id.friend_scan_ll:
				requestTakePermission();
				break;
			case R.id.friend_contacts_ll:
				startAnimActivity(ContactsActivity.class);
				break;
			case R.id.friend_wechat_ll:
				shareUtil.wXShareInt(FriendChooseActivity.this, getString(R.string.friend_wechat), getString(R.string.app_name), getString(R.string.friend_wechatf), R.drawable.headpic, getString(R.string.FSDOWNLOAD));
				break;
			case R.id.friend_circle_ll:
				shareUtil.friendsCircleShareInt(FriendChooseActivity.this, getString(R.string.friend_wechat), getString(R.string.app_name), getString(R.string.friend_wechatc), R.drawable.headpic, getString(R.string.FSDOWNLOAD));
				break;
		}
	}

	@Override
	public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			//搜索
			((FriendChoosePresenter) mPresenter).searchFriend(getString(R.string.FSSEARCHUSER), searchEt.getText().toString().trim());
		}
		return true;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	@Override
	public void afterTextChanged(Editable editable) {
		if (TextUtils.isEmpty(editable.toString())) {
			if (users != null && adapter != null) {
				users.clear();
				adapter.notifyDataSetChanged();
			}
			swipeTarget.setVisibility(View.GONE);
			chooseLl.setVisibility(View.VISIBLE);
		} else {
			swipeTarget.setVisibility(View.VISIBLE);
			chooseLl.setVisibility(View.GONE);
		}
	}

	@Override
	public void setAdapter(List<GroupContacts> list) {
		if (swipeTarget != null) {
			if (adapter == null) {
				this.users = list;
				adapter = new BaseRecyclerAdapter(users, R.layout.item_user, SearchUserHolder.class);
				adapter.setCallBack(this);
				swipeTarget.setAdapter(adapter);
			} else {
				users.clear();
				users.addAll(list);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		GroupContacts contacts = users.get(position);
		Intent intent = new Intent();
		//判断是不是自己
		if (contacts.getUserId().equals(MainApplication.getUserId())) {
			//是自己
			intent.setClass(mContext, FriendActivity.class);
		}
		//判断是不是好友
		else if (contacts.getIsFriend().equals("0")) {
			//==0->是好友
			intent.setClass(mContext, FriendActivity.class);
		} else {
			//不是好友去到加好友页面
			intent.setClass(mContext, AddFriendActivity.class);
		}
		intent.putExtra("userId", contacts.getUserId());
		intent.putExtra("userName", contacts.getUserName());
		intent.putExtra("hPic", contacts.getHeadPhoto());
		startAnimActivity(intent);
	}

	/**
	 * 拍照获取权限
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_CAMERA)
	protected void requestTakePermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
			startAnimActivity(CaptureActivity.class);
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_takephoto),
					ReqConstant.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝,再次申请
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (requestCode == ReqConstant.REQUEST_CODE_CAMERA) {
			requestTakePermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
