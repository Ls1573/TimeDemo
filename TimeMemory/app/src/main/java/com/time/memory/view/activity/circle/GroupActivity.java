package com.time.memory.view.activity.circle;

import android.Manifest;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.MemoryMoreSheet;
import com.time.memory.presenter.GroupShowPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DevUtils;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.ShareUtil;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.LockActivity;
import com.time.memory.view.activity.common.QrCodeActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.adapter.GroupShowAdapter;
import com.time.memory.view.holder.GroupHolder;
import com.time.memory.view.impl.IGroupShowView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子页面
 * @date 2016/11/7 19:59
 */
public class GroupActivity extends BaseActivity implements IGroupShowView, AdapterCallback, EasyPermissions.PermissionCallbacks {

	//	@Bind(R.id.grid_view)
//	MyGridView grid_view;
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	@Bind(R.id.group_sl)
	ScrollView group_sl;
	@Bind(R.id.group_more_ll)
	LinearLayout groupMoreLl;//更多
	@Bind(R.id.group_name_tv)
	TextView groupNameTv;//群名
	@Bind(R.id.tv_main_title)
	TextView tv_main_title;//title
	@Bind(R.id.group_admin_tv)
	TextView groupAdminTv;//主编名
	@Bind(R.id.group_pwd_tv)
	TextView group_pwd_tv;//编辑部密码提示
	@Bind(R.id.group_memorys_tv)
	TextView groupMemorysTv;//记忆数
	@Bind(R.id.group_addmemorys_tv)
	TextView groupAddmemorysTv;//追加记忆数

	private MGroup group;
	private GroupShowAdapter groupShowAdapter;
	private BaseRecyclerAdapter adapter;
	private List<GroupContacts> mGroupList;//群成员
	private ArrayList<GroupContacts> groupList;//群成员
	private String url;//分享网址
	private String imaUrl;//分享的图片网址
	private ShareUtil shareUtil;//分享工具类
	private int imgUrlInt;
	private int sharedPosition;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_group);
	}

	@Override
	public void initView() {
		group = getIntent().getParcelableExtra("group");
		initTopBarForLeft(group.getGroupName(), R.drawable.image_back);
	}

	@Override
	public BasePresenter initPresenter() {
		return new GroupShowPresenter();
	}

	@Override
	public void initData() {
		shareUtil = new ShareUtil();
		url = getString(R.string.FSSHAREURL) + "/mt-nio/webPage/groupInfo.htm?groupId=";
		this.mGroupList = new ArrayList<>();
		this.groupList = new ArrayList<>();
		setCircle();

		swipe_target.setLayoutManager(new GridLayoutManager(mContext, 5));

		//请求数据
		((GroupShowPresenter) mPresenter).reqCirlcePerple(getString(R.string.FSGETGROUPPEOPLES), group.getGroupId(), group.getAdminUserId());
	}

	@Override
	public void setAdapter(List<GroupContacts> groupLists, ArrayList<GroupContacts> groups, int memoryCount, int addMemoryCount) {
		if (!isFinishing()) {
			setMemoryCount(memoryCount, addMemoryCount);

			this.mGroupList.clear();
			this.groupList.clear();

			this.mGroupList.addAll(groupLists);
			this.groupList.addAll(groups);

			if (adapter == null) {
				adapter = new BaseRecyclerAdapter(mGroupList, R.layout.item_choosefriend_, GroupHolder.class);
				swipe_target.setAdapter(adapter);
				adapter.setCallBack(this);
			} else {
				adapter.notifyDataSetChanged();
			}

			if (mGroupList.size() == 15)
				groupMoreLl.setVisibility(View.VISIBLE);
			else
				groupMoreLl.setVisibility(View.GONE);
			group_sl.setVisibility(View.VISIBLE);
			//设置分享圈子的图片  当第一个没有的时候设置成默认的logo图片
			if (!TextUtils.isEmpty(groupLists.get(0).getHeadPhoto())) {
				imaUrl = getString(R.string.FSIMAGEPATH) + groupLists.get(0).getHeadPhoto() + getString(R.string.FSIMAGEOSS);
			}
		}

	}

	@Override
	public void exitCircle() {
		//TODO 退出圈子(数据的操作)
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@OnClick({R.id.group_name_ll, R.id.group_admin_ll, R.id.group_ercode_ll, R.id.group_copyId_ll, R.id.app_exit, R.id.app_submit, R.id.group_more_ll, R.id.app_cancle, R.id.group_pwd_ll})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_cancle:
				//返回
				setMyResult();
				break;
			case R.id.group_name_ll:
				//编辑部名
				updateCircle();
				break;
			case R.id.group_admin_ll:
				//主编
				break;
			case R.id.group_ercode_ll:
				//二维码
				showQrCode();
				break;
			case R.id.group_copyId_ll:
				//复制群Id
				copyGroupId();
				break;
			case R.id.app_exit:
				//退出
				exitCirlce();
				break;
			case R.id.app_submit:
				//发送邀请链接
				showMoreDialog();
				break;
			case R.id.group_more_ll:
				//更多成员
				morePeople();
				break;
			case R.id.group_pwd_ll:
				//编辑部密码
				Intent intent = new Intent(mContext, LockActivity.class);
				intent.putExtra("mGroup", group);
				intent.putExtra("isAddPwd", true);
				startActivityForResult(intent, ReqConstant.REQUEST_LOCK_GROUP);
				break;
			default:
				break;
		}
	}

	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		Intent intent = new Intent();
		if (group.getAdminUserId().equals(MainApplication.getUserId())) {
			//是管理员
			if (position == mGroupList.size() - 1) {
				//最后一位-人
				deletePeople(intent);
			} else if (position == mGroupList.size() - 2) {
				//最后两位+人
				addPeople(intent);
			} else {
				//查看个人资料
				watchOpr(position);
			}
		} else {
			//不是管理员
			if (position == mGroupList.size() - 1) {
				//最后一位+人
				addPeople(intent);
			} else {
				//查看个人资料
				watchOpr(position);
			}
		}
	}

	@Override
	public void onBackPressed() {
		setMyResult();
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
	 * 观察模式操作
	 *
	 * @param position
	 */
	private void watchOpr(int position) {
		GroupContacts contacts = mGroupList.get(position);
		Intent intent = new Intent();
		//判断是不是自己
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
		startAnimActivity(intent);
	}

	/**
	 * 添加好友进圈子
	 */
	private void addPeople(Intent intent) {
		intent.setClass(mContext, NewlyFriendActivity.class);
		intent.putParcelableArrayListExtra("groups", groupList);
		intent.putExtra("groupId", group.getGroupId());
		startActivityForResult(intent, ReqConstant.ADDPEOPLE);
	}

	/**
	 * 从圈子移除
	 */
	private void deletePeople(Intent intent) {
		intent.setClass(mContext, DeleteCircleActivity.class);
		intent.putExtra("group", group);
		intent.putExtra("groupContacts", groupList);
		startActivityForResult(intent, ReqConstant.REQUEST_CODE_GROUP);
	}

	/**
	 * 查看更多成员
	 */
	private void morePeople() {
		Intent intent = new Intent(mContext, ShowCircleActivity.class);
		intent.putExtra("group", group);
		intent.putExtra("groupContacts", groupList);
		startActivityForResult(intent, ReqConstant.REQUEST_CODE_GROUP);
	}

	/**
	 * 设置圈子信息
	 */
	private void setCircle() {
		groupNameTv.setText(group.getGroupName());
		groupAdminTv.setText(group.getAdminUserName());
		group_pwd_tv.setHint(TextUtils.isEmpty(group.getGroupPw()) ? getString(R.string.group_pwd_set) : getString(R.string.group_pwd_readey));
	}

	/**
	 * 设置记忆数
	 */
	private void setMemoryCount(int memoryCount, int addMemoryCount) {
		groupMemorysTv.setText(String.valueOf(memoryCount));//记忆数
		groupAddmemorysTv.setText(String.valueOf(addMemoryCount));//追加记忆数
	}

	/**
	 * 修改圈子名
	 */
	private void updateCircle() {
		//不是管理员不可以修改
		if (!group.getAdminUserId().equals(MainApplication.getUserId())) return;
		//改名
		Intent intent = new Intent(mContext, UpdateCircleActivity.class);
		intent.putExtra("group", group);
		startActivityForResult(intent, ReqConstant.UPCIRCLE);
	}

	/**
	 * 显示QrCode
	 */
	private void showQrCode() {
		Intent intent = new Intent(mContext, QrCodeActivity.class);
		intent.putExtra("qrcode", "sgjy_group_" + group.getGroupNum());
		intent.putExtra("sign", getString(R.string.qrcode_group));
		startAnimActivity(intent);
	}

	/**
	 * 返回
	 */
	private void setMyResult() {
		Intent intent = new Intent();
		intent.putExtra("group", group);
		setResult(ReqConstant.REQUEST_CODE_WATGROUP, intent);
		finish();
	}

	/**
	 * 复制群num到剪切板
	 */
	private void copyGroupId() {
		ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(group.getGroupNum());
		showShortToast(getString(R.string.clipboard_copyId));
	}

	/**
	 * 退出圈子
	 */
	private void exitCirlce() {
		DialogUtils.request(GroupActivity.this, "确定退出" + group.getGroupName() + "吗", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isSure = (boolean) data;
				if (isSure)
					((GroupShowPresenter) mPresenter).reqExitCircle(getString(R.string.FSEXITGROUP), group.getGroupId(), group.getAdminUserId(), mGroupList);
			}
		});
	}

	/**
	 * 更多的弹窗
	 */
	private void showMoreDialog() {
		MemoryMoreSheet.createBuilder(GroupActivity.this, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				isDelete(false).
				isShared(true).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setListener(new MemoryMoreSheet.onMemoryMoreListener() {
					@Override
					public void onSubmit(int postion) {
						if (postion == 4) {
							if (imaUrl != null) {
								shareUtil.sinoShareQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦!", "快去看看吧！", imaUrl, url + group.getGroupId());
								return;
							} else {
								shareUtil.sinoShareIntQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦!", "快去看看吧！", R.drawable.headpic, url + group.getGroupId());
							}
						} else if (postion == 3) {
							sharedPosition = 3;
							requestWRITEPermission();
						} else if (postion == 2) {
							sharedPosition = 2;
							requestWRITEPermission();
						} else if (postion == 1) {
							if (imaUrl != null) {
								shareUtil.friendsCircleShareQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦！", "快去看看吧！", imaUrl, url + group.getGroupId());
								return;
							} else {
								shareUtil.friendsCircleShareIntQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦！", "快去看看吧！", R.drawable.headpic, url + group.getGroupId());
							}
						} else if (postion == 0) {
							if (imaUrl != null) {
								shareUtil.wXShareQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦！", "快去看看吧！", imaUrl, url + group.getGroupId());
								return;
							} else {
								shareUtil.wXShareIntQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦！", "快去看看吧！", R.drawable.headpic, url + group.getGroupId());
							}
						}
					}

					@Override
					public void onCancle() {
					}
				}).show();
	}

	/**
	 * 文件读取
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_WRITE)
	protected void requestWRITEPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			if (sharedPosition == 2) {
				if (imaUrl != null) {
					shareUtil.qqFriendShareQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦!", "快去看看吧！", imaUrl, url + group.getGroupId());
					return;
				} else {
					shareUtil.qqFriendShareIntQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦！", "快去看看吧！", R.drawable.headpic, url + group.getGroupId());
				}
			} else {
				if (imaUrl != null) {
					shareUtil.qqSpaceShareQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦!", "快去看看吧！", imaUrl, url + group.getGroupId());
					return;
				} else {
					shareUtil.qqSpaceShareIntQ(GroupActivity.this, "这是«" + group.getGroupName() + "»的记忆之书，或许你的记忆在里面哦！", "快去看看吧！", R.drawable.headpic, url + group.getGroupId());
				}
			}

		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_external),
					ReqConstant.REQUEST_CODE_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (requestCode == resultCode) {
			if (resultCode == ReqConstant.UPCIRCLE) {
				if (data == null) return;
				//改名
				String name = data.getStringExtra("title");
				groupNameTv.setText(name);
				tv_main_title.setText(name);
				group.setGroupName(name);
			} else if (resultCode == ReqConstant.ADDPEOPLE) {
				//圈子成员加人-成功
				((GroupShowPresenter) mPresenter).reqCirlcePerple(getString(R.string.FSGETGROUPPEOPLES), group.getGroupId(), group.getAdminUserId());
			} else if (resultCode == ReqConstant.REQUEST_CODE_GROUP) {
				//圈子成员删除成员-成功
				((GroupShowPresenter) mPresenter).reqCirlcePerple(getString(R.string.FSGETGROUPPEOPLES), group.getGroupId(), group.getAdminUserId());
			} else if (requestCode == ReqConstant.REQUEST_CODE_WRITE) {
				//更改权限返回
				requestWRITEPermission();
			} else if (requestCode == ReqConstant.REQUEST_LOCK_GROUP) {
				if (data == null) return;
				//更改密码返回
				String groupPw = data.getStringExtra("groupPw");
				group.setGroupPw(groupPw);
				setCircle();
			} else {
				UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
			}
		}
	}
}
