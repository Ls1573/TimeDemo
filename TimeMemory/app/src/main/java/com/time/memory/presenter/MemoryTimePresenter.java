package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryGroup;
import com.time.memory.entity.Memorys;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryDayController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryDetailController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IMemoryTimeView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆时间轴
 * @date 2016/10/17 16:26
 */
public class MemoryTimePresenter extends BasePresenter<IMemoryTimeView> {
	private static final String TAG = "MemoryTimePresenter";
	private IMemoryDetailController iMemoryDayController;
	private ILoginController iLoginController;


	public MemoryTimePresenter() {
		iMemoryDayController = new MemoryDayController();
		iLoginController = new LoginController();
	}

	/**
	 * 获取位置下标
	 *
	 * @param memoryId
	 * @param memories
	 * @return
	 */
	public int getPosition(String memoryId, ArrayList<Memory> memories) {
		int index = 0;
		for (int i = 0; i < memories.size(); i++) {
			if (memories.get(i).getMemoryId().equals(memoryId)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 获取信息
	 *
	 * @param
	 * @reurn
	 */
	public void getMessage(int curPage, int pageCount, int lastSize) {
		//我的
		CLog.e(TAG, "lastSize:" + lastSize);
		String url = context.getString(R.string.FSMYMEMORYS);
		getMyMemorys(url, "2", "", "", curPage, pageCount, lastSize);
	}

	/**
	 * 获取我的记忆
	 *
	 * @return
	 */
	private void getMyMemorys(String url, String searchType, String lableId, String groupId, final int curPage, final int pageCount, final int lastSize) {
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
							mView.setAdapter(convert(entity.getMemoryList(), lastSize), entity.getMemoryList());
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
								if (entity.getMemoryList().size() < pageCount)
									mView.showEmpty();
							} else {
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
	 * 获header对应的下标
	 *
	 * @return
	 */
	private List<Integer> convertH(List<Memorys> memoryses) {
		List<Integer> list = new ArrayList<>();
		for (Memorys memorys : memoryses) {
			list.add(memorys.getStart());
		}
		return list;
	}

	/**
	 * 信息加工下
	 *
	 * @return
	 */
	public List<Memorys> convert(List<Memory> memorys, int lastSize) {
		if (memorys == null) memorys = new ArrayList<>();
		List<Memorys> memoryses = new ArrayList<>();

		List<Memory> mList = null;
		Memorys writerEntity = null;
		int endPosition = 0;
		int size = memorys.size();
		//循环遍历
		for (int i = 0; i < size; i++) {
			ArrayList<MemoryGroup> memoryGroups = new ArrayList<>();
			Memory curMemory = memorys.get(i);
			//圈子名
			if (curMemory.getGroupNameList() == null || curMemory.getGroupNameList().isEmpty()) {
				memoryGroups.add(new MemoryGroup(0, "私密记忆"));
			} else {
				for (String ss : curMemory.getGroupNameList()) {
					memoryGroups.add(new MemoryGroup(1, ss));
				}
			}
			curMemory.setMemoryGroups(memoryGroups);

			//图片集
			List<PhotoInfo> pEntitys = new ArrayList<>();
			//不为空
			if (!TextUtils.isEmpty(curMemory.getPhoto1())) {
				pEntitys.add(new PhotoInfo(curMemory.getPhoto1()));
			}
			if (!TextUtils.isEmpty(curMemory.getPhoto2())) {
				pEntitys.add(new PhotoInfo(curMemory.getPhoto2()));
			}
			if (!TextUtils.isEmpty(curMemory.getPhoto3())) {
				pEntitys.add(new PhotoInfo(curMemory.getPhoto3()));
			}
			//设置图片
			curMemory.setPictureEntits(pEntitys);
			//分组排序
			if (i == 0) {
				mList = new ArrayList<>();
				++endPosition;
				writerEntity = new Memorys();
				//头
				curMemory.setIsHeader(false);
				mList.add(curMemory);
				writerEntity.setList(mList);
				writerEntity.setPosition(i);
				writerEntity.setDate(curMemory.getMemoryDateForShow().substring(8, 10));
				writerEntity.setYearMouth(curMemory.getMemoryDateForShow().substring(0, 7));
				writerEntity.setStart(0 + lastSize);
				writerEntity.setEnd(endPosition + lastSize);
				memoryses.add(writerEntity);
				continue;
			} else {
				//上一个
				Memory oMemory = memorys.get(i - 1);
				//日期相等加到一起去
				if (oMemory.getMemoryDateForShow().equals(curMemory.getMemoryDateForShow())) {
					++endPosition;
					writerEntity.setEnd(endPosition + lastSize);
					writerEntity.setPosition(i);
					curMemory.setIsHeader(false);
					curMemory.setPosition(i);
					mList.add(curMemory);
					continue;
				} else {
					mList = new ArrayList<>();
					writerEntity = new Memorys();
					mList.add(curMemory);
					writerEntity.setList(mList);
					writerEntity.setDate(curMemory.getMemoryDateForShow().substring(8, 10));
					writerEntity.setYearMouth(curMemory.getMemoryDateForShow().substring(0, 7));
					++endPosition;
					writerEntity.setStart(endPosition + lastSize);
					++endPosition;
					writerEntity.setPosition(i);
					writerEntity.setEnd(endPosition + lastSize);
					memoryses.add(writerEntity);
					continue;
				}
			}
		}
		return memoryses;
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
			}
		}
		return memorys;
	}
}
