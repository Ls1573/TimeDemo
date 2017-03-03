package com.time.memory.view.fragment.circle;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.callback.ICircleCallBack;
import com.time.memory.core.callback.IMCircleCallBack;
import com.time.memory.core.callback.OnConstactCallBack;
import com.time.memory.core.constant.ImConstant;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.im.IMClientManager;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.AppSettingsDialog;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.Contacts;
import com.time.memory.gui.ClearEditText;
import com.time.memory.gui.SideLetterBar;
import com.time.memory.mt.nio.message.response.SG02RespVo;
import com.time.memory.presenter.CirclePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.circle.CirlceActivity;
import com.time.memory.view.activity.circle.FriendActivity;
import com.time.memory.view.activity.circle.FriendChooseActivity;
import com.time.memory.view.activity.memory.TagManagerActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.adapter.ConstantsAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.holder.CircleSearchHolder;
import com.time.memory.view.impl.ICircleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.3
 * @Description:圈子
 * @date 2016-9-9下午4:50:42
 * ==============================
 */
public class CircleFragment extends BaseFragment implements ICircleView, OnConstactCallBack, SideLetterBar.OnLetterChangedListener, TextWatcher, IMCircleCallBack, AdapterCallback, EasyPermissions.PermissionCallbacks {
	private static final String TAG = "CircleFragment";

	private static final int PERMISSIONS_CODE_CONSTACTS = 101;//联系人
	private static final int RC_SETTINGS_SCREEN = 102;//去设置页面了

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

	private ConstantsAdapter constantsAdapter;
	private BaseRecyclerAdapter recyclerAdapter;

	private List<Contacts> mList;
	private List<Contacts> mRecyclerList;

	private boolean isLoad = false;
	private boolean isFirst = true;

	private ICircleCallBack iCircleCallBack;

	public void setiCircleCallBack(ICircleCallBack iCircleCallBack) {
		this.iCircleCallBack = iCircleCallBack;
	}

	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container,
							   Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_circle, null);
		return view;
	}

	@Override
	public void initView(View view) {
		initTopBarForRight(view, getString(R.string.main_circle), "", R.drawable.addfriend);
	}

	@Override
	public void initData() {
		swipe_view.setLayoutManager(new LinearLayoutManager(mContext));
		mLetterBar.setOverlay(tv_letter_overlay);
		mLetterBar.setOnLetterChangedListener(this);
		searchEt.addTextChangedListener(this);
		//设置udp监听
		IMClientManager.getInstance(mContext.getApplicationContext()).getTransDataListener().setImCallBack(this, ImConstant.IMCIRCLE);
	}

	@Override
	public BasePresenter initPresenter() {
		return new CirclePresenter();
	}

	@Override
	public void onResume() {
		super.onResume();
		//获取用户信息
		((CirclePresenter) mPresenter).getContactsFromDb(MainApplication.getUserId());
	}

	@Override
	public void onDestroy() {
		IMClientManager.getInstance(mContext.getApplicationContext()).getTransDataListener().removeCallback(ImConstant.IMCIRCLE);
		super.onDestroy();
	}

	@Override
	public void onLetterChanged(String letter) {
		if (constantsAdapter != null) {
			int position = constantsAdapter.getLetterPosition(letter);
			swipe_target.setSelection(position);
		}
	}

	@Override
	public void onConstactsCallBack(int position, String constactsId) {
		//选择联系人
		Intent intent = new Intent(mContext, FriendActivity.class);
		intent.putExtra("userId", mList.get(position).getUserId());
		intent.putExtra("userName", mList.get(position).getContactName());
		startAnimActivity(intent);
	}

	@Override
	public void onLongConstactsCallBack(int position, String constactsId) {
		//选择联系人->长按删除
		CLog.e(TAG, "position:" + position);
		delDialog(position);
	}

	@Override
	public void onCreateCircle() {
		//圈子列表
		startAnimActivity(CirlceActivity.class);
	}

	@Override
	public void onContastsUp() {
		//同步通讯录
		requestConstacts();
	}

	/**
	 * 管理记忆签
	 */
	@Override
	public void onTags() {
		startAnimActivity(TagManagerActivity.class);
	}

	@Override
	public void onCirlceSort() {
	}


	@OnClick({R.id.iv_main_right})
	public void onClick(View view) {
		if (view.getId() == R.id.iv_main_right) {
			//新好友
			Intent intent = new Intent(mContext, FriendChooseActivity.class);
			startActivityForResult(intent, ReqConstant.REQUEST_CODE_NEWFRIEND);
		}
	}

	@Override
	public void setAdapter(List<Contacts> list) {
		// 通讯录(ListView)
		if (swipe_target != null) {
			isLoad = true;
			isFirst = false;
			if (constantsAdapter == null) {
				this.mList = list;
				constantsAdapter = new ConstantsAdapter(mContext, mList);
				swipe_target.setAdapter(constantsAdapter);
				constantsAdapter.setOnConstactCallBack(this);
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
	public void reqContacts() {
		//请求联系人
		((CirclePresenter) mPresenter).reqContacts(getString(R.string.FSREQFriends), MainApplication.getUserToken(), MainApplication.getUserId());
	}

	/**
	 * 设置首字母
	 *
	 * @param fLetters
	 */
	@Override
	public void reqFLetter(ArrayList<String> fLetters) {
		mLetterBar.setLetters(fLetters);
	}

	/**
	 * 移除好友
	 *
	 * @param position
	 */
	@Override
	public void removeFriend(int position) {
		constantsAdapter.remove(position);
	}

	/**
	 * 重新获取数据
	 */
	@Override
	public void refreshContacts() {
		((CirclePresenter) mPresenter).getContactsFromDb(MainApplication.getUserId());
	}


	/**
	 * 刷新本地数据
	 */
	@Override
	public void nofityMemoryNum(String num) {
		if (iCircleCallBack != null)
			iCircleCallBack.onNotiftyNum(num);
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
			((CirclePresenter) mPresenter).getConctacts(keyWord, MainApplication.getUserId());
			swipe_target_rl.setVisibility(View.GONE);
			swipe_view.setVisibility(View.VISIBLE);
		} else {
			//为空->显示列表
			swipe_target_rl.setVisibility(View.VISIBLE);
			swipe_view.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDataCallBack(Object data, int position) {
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
	}

	@Override
	public void onCallback(Object data) {
		int index = (int) data;
		Intent intent = new Intent(mContext, FriendActivity.class);
		intent.putExtra("userId", mRecyclerList.get(index).getUserId());
		intent.putExtra("userName", mRecyclerList.get(index).getContactName());
		startAnimActivity(intent);
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	/**
	 * UDP - 新好友
	 *
	 * @param respVo
	 */
	@Override
	public void onContacts(SG02RespVo respVo) {
		((CirclePresenter) mPresenter).addContats(MainApplication.getUserId(), respVo);
	}


	/**
	 * 删除用户
	 *
	 * @param position
	 */
	private void delDialog(final int position) {
		DialogUtils.request(getActivity(), "确定删除此用户么?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isDel = (boolean) data;
				if (isDel) {
					//TODO 删除  FSDELETEFRIEND
					((CirclePresenter) mPresenter).reqRemoveFriend(getString(R.string.FSDELETEFRIEND), mList.get(position).getUserId(), position);
				}
			}
		});
	}

	/**
	 * 同步联系人
	 */
	@AfterPermissionGranted(PERMISSIONS_CODE_CONSTACTS)
	private void requestConstacts() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_CONTACTS)) {
			//有权限
			((CirclePresenter) mPresenter).upConstacts(MainApplication.getUserId());
		} else {
			// 请求一个权限
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tips_constacts),
					PERMISSIONS_CODE_CONSTACTS, Manifest.permission.READ_CONTACTS);
		}
	}

	/**
	 * 再次请求权限
	 */
	private void requestPermissionAgain() {
		//检查,权限被禁止
		if (EasyPermissions.permissionPermanentlyDenied(this, Manifest.permission.READ_CONTACTS)) {
			new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
					.setTitle(getString(R.string.title_settings_dialog))
					.setPositiveButton(getString(R.string.setting))
					.setNegativeButton(getString(R.string.app_cancle), new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									CLog.e(TAG, "取消了---------->");
								}
							}
					)
					.setRequestCode(RC_SETTINGS_SCREEN)
					.build()
					.show();
		} else {
			//没有被禁止-询问
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tips_constacts),
					PERMISSIONS_CODE_CONSTACTS, Manifest.permission.READ_CONTACTS);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (resultCode == RC_SETTINGS_SCREEN) {
			requestConstacts();
		}
	}
}
