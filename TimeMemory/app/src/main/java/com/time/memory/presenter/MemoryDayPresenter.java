package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryGroup;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.User;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryDayController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryDetailController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IMemoryDayView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆(天)
 * @date 2016/9/14 11:13
 */
public class MemoryDayPresenter extends BasePresenter<IMemoryDayView> {
	private static final String TAG = "MemoryDayPresenter";

	private IMemoryDetailController iMemoryDayController;
	private ILoginController iLoginController;
	private IGroupController iGroupController;

	public MemoryDayPresenter() {
		iMemoryDayController = new MemoryDayController();
		iLoginController = new LoginController();
		iGroupController = new GroupController();
	}


	/**
	 * 获取信息
	 *
	 * @param
	 * @reurn
	 */
	public void getMessage(String userId, int curPage, int pageCount) {
		//我的
		String url = context.getString(R.string.FSMYMEMORYS);
		getMemorys(url, "1", "", "", curPage, pageCount);
	}

	/**
	 * 移除消息
	 *
	 * @param removeList
	 * @param list
	 */
	public void removeHotList(List<Integer> removeList, ArrayList<Memory> list) {
		if (list == null) list = new ArrayList<>();
		for (Integer index : removeList) {
			if (list.size() > index) {
				list.remove((int) index);
				continue;
			} else {
				break;
			}
		}
		//我的圈子
		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
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
	 * 添加数据
	 *
	 * @param tempMemory
	 */
	public void addMemoryFromTemp(TempMemory tempMemory) {
		if (tempMemory != null) {
			//不为空,赋值
			User user = iLoginController.getUser(MainApplication.getUserId());
			Memory memory = new Memory();
			memory.setMemoryId(tempMemory.getMemoryId());
			memory.setUserId(tempMemory.getUserId());
			memory.setPhotoCount(Integer.parseInt(tempMemory.getPiccount()));
			memory.setCommentCount(Integer.parseInt(tempMemory.getCommentcount()));
			memory.setAddmemoryCount(Integer.parseInt(tempMemory.getAddcount()));
			memory.setPraiseCount(Integer.parseInt(tempMemory.getForkcount()));
			memory.setTitle(tempMemory.getTitle());
			memory.setState("1");
			memory.setMemoryDateForShow(tempMemory.getUpdateForshowDate());
			memory.setLocal(tempMemory.getLocal());
			memory.setStateForShow(tempMemory.getMemoryOwner());
			ArrayList<String> groupList = new ArrayList<>();
			groupList.add(tempMemory.getMemoryOwner());
			memory.setGroupNameList(groupList);

			ArrayList<PhotoInfo> pEntitys = new ArrayList<>();
			//不为空
			if (!TextUtils.isEmpty(tempMemory.getPhoto1())) {
				pEntitys.add(new PhotoInfo(tempMemory.getPhoto1()));
				memory.setPhoto1(tempMemory.getPhoto1());
			}
			if (!TextUtils.isEmpty(tempMemory.getPhoto2())) {
				pEntitys.add(new PhotoInfo(tempMemory.getPhoto2()));
				memory.setPhoto2(tempMemory.getPhoto2());
			}
			if (!TextUtils.isEmpty(tempMemory.getPhoto3())) {
				pEntitys.add(new PhotoInfo(tempMemory.getPhoto3()));
				memory.setPhoto3(tempMemory.getPhoto3());
			}
			//设置图片
			memory.setPictureEntits(pEntitys);

			//Title
			ArrayList<MemoryGroup> memoryGroups = new ArrayList<>();
			memoryGroups.add(new MemoryGroup(TextUtils.isEmpty(tempMemory.getGroupId()) ? 0 : 1, tempMemory.getMemoryOwner()));
			memory.setMemoryGroups(memoryGroups);
			//设置头像地址
			memory.setNetPath(user.getHeadPhoto());
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
		iMemoryDayController.getMemoryAll(url, searchType, lableId, groupId, curPage, pageCount, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					Memory entity = (Memory) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.setAdapter(convert(entity.getMemoryList()));
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
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
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 信息加工下
	 *
	 * @return
	 */
	public ArrayList<Memory> convert(ArrayList<Memory> memorys) {
		if (memorys == null) {
			memorys = new ArrayList<>();
		} else {
			User user = iLoginController.getUser(MainApplication.getUserId());
			for (Memory entity : memorys) {
				ArrayList<PhotoInfo> pEntitys = new ArrayList<>();
				ArrayList<MemoryGroup> memoryGroups = new ArrayList<>();
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
				//设置头像地址
				entity.setNetPath(user.getHeadPhoto());
				//圈子名
				if (entity.getGroupNameList() == null || entity.getGroupNameList().isEmpty()) {
					memoryGroups.add(new MemoryGroup(0, "私密记忆"));
				} else {
					for (String ss : entity.getGroupNameList()) {
						memoryGroups.add(new MemoryGroup(1, ss));
					}
				}
				entity.setMemoryGroups(memoryGroups);
			}
		}
		return memorys;
	}
}
