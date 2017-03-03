package com.time.memory.view.activity.circle;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.OnContactsCallBack;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.Contacts;
import com.time.memory.gui.ClearEditText;
import com.time.memory.gui.SideLetterBar;
import com.time.memory.presenter.ContactsPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.adapter.ContactsAdapter;
import com.time.memory.view.holder.CircleSearchHolder;
import com.time.memory.view.impl.IContactsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.3
 * @Description:手机联系人信息
 * @date 2016/12/30 11:08
 */
public class ContactsActivity extends BaseActivity implements IContactsView, OnContactsCallBack, SideLetterBar.OnLetterChangedListener, TextWatcher, AdapterCallback, TextView.OnEditorActionListener, EasyPermissions.PermissionCallbacks {
	private String SMS_SEND_ACTIOIN = "SMS_SEND_ACTIOIN";
	private String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";
	@Bind(R.id.swipe_target_rl)
	RelativeLayout swipe_target_rl;
	@Bind(R.id.swipe_view)
	RecyclerView swipe_view;
	@Bind(R.id.circle_search_et)
	ClearEditText searchEt;
	@Bind(R.id.swipe_target)
	ListView swipe_target;
	@Bind(R.id.tv_side_letter_bar)
	SideLetterBar mLetterBar;//索引
	@Bind(R.id.tv_letter_overlay)
	TextView tv_letter_overlay;//提示字母

	private ContactsAdapter constantsAdapter;
	private BaseRecyclerAdapter recyclerAdapter;
	private List<Contacts> mList;
	private List<Contacts> mRecyclerList;

	private String phoneNumber;
	private String message;


	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_contacts);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_contacts), R.drawable.image_back);
	}

	@Override
	public BasePresenter initPresenter() {
		return new ContactsPresenter();
	}

	@Override
	public void initData() {
		swipe_view.setLayoutManager(new LinearLayoutManager(mContext));
		mLetterBar.setOverlay(tv_letter_overlay);
		mLetterBar.setOnLetterChangedListener(this);
		searchEt.addTextChangedListener(this);
		searchEt.setOnEditorActionListener(this);
		//获取用户信息
		((ContactsPresenter) mPresenter).getContactsFromDb(MainApplication.getUserId());

	}

	@OnClick({R.id.app_cancle})
	public void onClick(View view) {
		int Id = view.getId();
		switch (Id) {
			case R.id.app_cancle:
				KeyBoardUtils.HideKeyboard(searchEt);
				finish();
				break;
		}
	}


	@Override
	public void setAdapter(List<Contacts> list) {
		// 通讯录(ListView)
		if (swipe_target != null) {
			if (constantsAdapter == null) {
				this.mList = list;
				constantsAdapter = new ContactsAdapter(mContext, mList);
				constantsAdapter.setOnContactsCallBack(this);
				swipe_target.setAdapter(constantsAdapter);
			} else {
				mList.clear();
				mList.addAll(list);
				constantsAdapter.notifityAdapter();
			}
		}
	}

	@Override
	public void setRecyerAdapter(List<Contacts> list) {
		// 通讯录(RecyclerView)
		if (swipe_view != null) {
			if (recyclerAdapter == null) {
				this.mRecyclerList = list;
				recyclerAdapter = new BaseRecyclerAdapter(mRecyclerList, R.layout.item_contacts_circle, CircleSearchHolder.class);
				recyclerAdapter.setCallBack(this);
				swipe_view.setAdapter(recyclerAdapter);
			} else {
				mRecyclerList.clear();
				mRecyclerList.addAll(list);
				recyclerAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void reqFLetter(ArrayList<String> fLetters) {
		mLetterBar.setLetters(fLetters);
	}

	@Override
	public void onLetterChanged(String letter) {
		if (constantsAdapter != null) {
			int position = constantsAdapter.getLetterPosition(letter);
			swipe_target.setSelection(position);
		}
	}

	/**
	 * 添加用户
	 *
	 * @param userId
	 */
	@Override
	public void onAddUser(String userId) {
		//申请成为好友
		((ContactsPresenter) mPresenter).reqAddFriend(getString(R.string.FSREQAPPLY), userId);
	}

	/**
	 * 邀请用户
	 */
	@Override
	public void onApplyUser(int position) {
		//TODO 测试发送
		phoneNumber = mList.get(position).getPhone();
		message = getString(R.string.apply_message);
		requestSMSPermission();
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		//输入监听
		String keyWord = s.toString();
		getContacts(keyWord);

	}

	/**
	 * 帅选数据
	 *
	 * @param keyWord
	 */
	private void getContacts(String keyWord) {
		if (!TextUtils.isEmpty(keyWord)) {
			//查找数据
			((ContactsPresenter) mPresenter).getConctacts(keyWord, MainApplication.getUserId());
			swipe_target_rl.setVisibility(View.GONE);
			swipe_view.setVisibility(View.VISIBLE);
		} else {
			//为空->显示列表
			swipe_target_rl.setVisibility(View.VISIBLE);
			swipe_view.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			//搜索
		}
		return true;
	}

	@Override
	public void onCallback(Object data) {
		int index = (int) data;
		Intent intent = new Intent(mContext, FriendActivity.class);
		intent.putExtra("userId", mRecyclerList.get(index).getUserId());
		intent.putExtra("userName", mRecyclerList.get(index).getContactName());
		startAnimActivity(intent);
		KeyBoardUtils.HideKeyboard(searchEt);
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	private void send(String number, String message) {
		//处理返回的发送状态
		try {
			String SENT = "sms_sent";
			String DELIVERED = "sms_delivered";
			PendingIntent sentPI = PendingIntent.getActivity(this, 0, new Intent(SENT), 0);
			PendingIntent deliveredPI = PendingIntent.getActivity(this, 0, new Intent(DELIVERED), 0);

			SmsManager smsm = SmsManager.getDefault();
			smsm.sendTextMessage(number, null, message, sentPI, deliveredPI);
		} catch (Exception e) {
			e.printStackTrace();
		}
		showShortToast(mContext.getString(R.string.apply_success));
	}

	/**
	 * 拍照发短信
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_SMS)
	protected void requestSMSPermission() {
		//TODO
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.SEND_SMS)) {
			send(phoneNumber, message);
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_sms),
					ReqConstant.REQUEST_CODE_SMS, Manifest.permission.SEND_SMS);
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
		if (requestCode == ReqConstant.REQUEST_CODE_SMS) {
			requestSMSPermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
}
