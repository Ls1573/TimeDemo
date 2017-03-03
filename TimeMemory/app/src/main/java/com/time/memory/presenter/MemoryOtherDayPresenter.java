package com.time.memory.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.User;
import com.time.memory.model.ContactsController;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryDayController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryDetailController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.circle.AddFriendActivity;
import com.time.memory.view.activity.circle.FriendActivity;
import com.time.memory.view.impl.IMemoryOtherDayView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:他记忆(天)
 * @date 2016/9/14 11:13
 */
public class MemoryOtherDayPresenter extends BasePresenter<IMemoryOtherDayView> {
	private static final String TAG = "MemoryDayPresenter";

	private IMemoryDetailController iMemoryDayController;
	private ILoginController iLoginController;
	private IGroupController iGroupController;
	private IContactsController iContactsController;

	public MemoryOtherDayPresenter() {
		iMemoryDayController = new MemoryDayController();
		iLoginController = new LoginController();
		iGroupController = new GroupController();
		iContactsController = new ContactsController();
	}

	/**
	 * 获取用户
	 *
	 * @param userId
	 * @param toUserId
	 */
	public void getContact(String userId, String toUserId, String name, String headPic) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		Intent intent = new Intent();
		intent.putExtra("userId", userId);
		intent.putExtra("userName", name);
		intent.putExtra("hPic", headPic);

		if (userId.equals(toUserId)) {
			//自己
			intent.setClass(context, FriendActivity.class);
		} else {
			Contacts contacts = iContactsController.getContactFromDb(userId, toUserId);
			if (contacts != null) {
				//是好友
				intent.setClass(context, FriendActivity.class);
			} else {
				//不是好友
				intent.setClass(context, AddFriendActivity.class);
			}
		}
		if (mView != null) {
			mView.showSuccess();
			mView.startMyActivity(intent);
		}
	}


	/**
	 * 移除消息
	 *
	 * @param removeList
	 * @param list
	 */
	public void removeHotList(List<Integer> removeList, ArrayList<OtherMemory> list) {
		if (list == null) list = new ArrayList<>();
		for (Integer index : removeList) {
			if (list.size() > index) {
				list.remove((int) index);
				continue;
			} else {
				break;
			}
		}
		//他的圈子
		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		//总记忆数
		int totalCount = 0;
		if (!TextUtils.isEmpty(mGroup.getMemoryCnt())) {
			totalCount = Integer.parseInt(mGroup.getMemoryCnt());
		}
		//当前记忆数
		mGroup.setMemoryCnt(String.valueOf(totalCount - removeList.size() < 0 ? 0 : totalCount - removeList.size()));
		//更新
		iGroupController.upMGroup(mGroup);
		//刷新页面
		if (mView != null) {
			mView.refreshAdapter();
		}
	}

	/**
	 * 获取信息
	 *
	 * @param
	 * @reurn
	 */
	public void getMessage(int type, String userId, int curPage, int pageCount) {
		//他的
		String url = context.getString(R.string.FSOTHERMEMORYS);
		getMemorys(url, "1", "", "", curPage, pageCount);
	}

	/**
	 * 获取本地数据
	 */
	public void addMemoryFromTemp(TempMemory tempMemory) {
		if (tempMemory != null) {
			//不为空,赋值
			String imagePath = context.getString(R.string.FSIMAGEPATH);
			User user = iLoginController.getUser(MainApplication.getUserId());
			OtherMemory memory = new OtherMemory();
			memory.setMemoryId(tempMemory.getMemoryId());
			memory.setUserIdF(tempMemory.getUserId());
			memory.setPhotoCount(Integer.parseInt(tempMemory.getPiccount()));
			memory.setCommentCount(Integer.parseInt(tempMemory.getCommentcount()));
			memory.setAddmemoryCount(Integer.parseInt(tempMemory.getAddcount()));
			memory.setPraiseCount(Integer.parseInt(tempMemory.getForkcount()));
			memory.setTitle(tempMemory.getTitle());
			memory.setUserNameF(user.getUserName());
			memory.setMemoryDateForShow(tempMemory.getUpdateForshowDate());
			memory.setHeadPhotoF(user.getHeadPhoto());
			memory.setState("0");
			memory.setLocal(tempMemory.getLocal());

			ArrayList<PhotoInfo> pEntitys = new ArrayList<>();
			//不为空
			if (!TextUtils.isEmpty(tempMemory.getPhoto1())) {
				pEntitys.add(new PhotoInfo(tempMemory.getPhoto1()));
			}
			if (!TextUtils.isEmpty(tempMemory.getPhoto2())) {
				pEntitys.add(new PhotoInfo(tempMemory.getPhoto2()));
			}
			if (!TextUtils.isEmpty(tempMemory.getPhoto3())) {
				pEntitys.add(new PhotoInfo(tempMemory.getPhoto3()));
			}
			//设置图片
			memory.setPictureEntits(pEntitys);
			//设置头像地址
			if (mView != null) {
				mView.addAdapter(memory);
			}
		}
	}

	/**
	 * 获取记忆
	 *
	 * @return
	 */
	private void getMemorys(String url, String searchType, String lableId, String groupId, final int curPage, final int pageCount) {
		if (mView != null && curPage == 1)
			mView.showLoadingDialog();
		iMemoryDayController.getOtherMemoryAll(url, searchType, lableId, groupId, curPage, pageCount, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					OtherMemory entity = (OtherMemory) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.setAdapter(convert(entity.getMemoryList()));
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
								//不为空
								if (entity.getMemoryList().size() < pageCount)
									mView.showEmpty();
							} else {
								//没有数据
								mView.showEmpty();
							}
							mView.showSuccess();
						}
					} else {
						//失败
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(entity.getMessage());
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showFaild();
					mView.showShortToast(context.getString(R.string.net_error));
				}
			}
		});
	}

	/**
	 * 信息加工下
	 *
	 * @return
	 */
	public ArrayList<OtherMemory> convert(ArrayList<OtherMemory> memorys) {
		if (memorys == null) {
			memorys = new ArrayList<>();
		} else {
			for (OtherMemory entity : memorys) {
				List<PhotoInfo> pEntitys = new ArrayList<>();
				//不为空
				if (!TextUtils.isEmpty(entity.getPhoto1())) {
					pEntitys.add(new PhotoInfo(entity.getPhoto1()));
				}
				if (!TextUtils.isEmpty(entity.getPhoto2())) {
					pEntitys.add(new PhotoInfo(entity.getPhoto2()));
				}
				if (!TextUtils.isEmpty(entity.getPhoto3())) {
					pEntitys.add(new PhotoInfo(entity.getPhoto3()));
				}
				//设置图片
				entity.setPictureEntits(pEntitys);
			}
		}
		return memorys;
	}
}
