package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.OtherMemorys;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryDayController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryDetailController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IMemoryOtherTimeView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆时间轴
 * @date 2016/10/17 16:26
 */
public class MemoryOtherTimePresenter extends BasePresenter<IMemoryOtherTimeView> {
	private static final String TAG = "MemoryTimePresenter";
	private IMemoryDetailController iMemoryDayController;
	private ILoginController iLoginController;


	public MemoryOtherTimePresenter() {
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
	public int getPosition(String memoryId, ArrayList<OtherMemory> memories) {
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
	public void getMessage(int curPage, int pageCount, String lableId, String groupId, int lastSize) {
		String url = context.getString(R.string.FSOTHERMEMORYS);
		getOtherOtherMemorys(url, "2", "", "", curPage, pageCount, lastSize);
	}

	/**
	 * 获取记忆
	 *
	 * @return
	 */
	private void getOtherOtherMemorys(String url, String searchType, String lableId, String groupId, final int curPage, final int pageCount, final int lastSize) {
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
							mView.setAdapter(convert(entity.getMemoryList(), lastSize), entity.getMemoryList());
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
								//不为空
								if (entity.getMemoryList().size() < pageCount)
									mView.showEmpty();
							} else {
								//没有数据
								mView.showEmpty();
							}
						}
						mView.showSuccess();
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
	public List<OtherMemorys> convert(List<OtherMemory> OtherMemorys, final int lastSize) {
		if (OtherMemorys == null) OtherMemorys = new ArrayList<>();
		List<OtherMemorys> OtherMemoryses = new ArrayList<>();

		List<OtherMemory> mList = null;
		OtherMemorys writerEntity = null;
		int endPosition = 0;
		//循环遍历
		for (int i = 0; i < OtherMemorys.size(); i++) {
			OtherMemory curMemory = OtherMemorys.get(i);
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
				writerEntity = new OtherMemorys();
				//头
				curMemory.setIsHeader(false);
				mList.add(curMemory);
				writerEntity.setList(mList);
				writerEntity.setPosition(i);
				writerEntity.setDate(curMemory.getMemoryDateForShow().substring(8, 10));
				writerEntity.setYearMouth(curMemory.getMemoryDateForShow().substring(0, 7));
				writerEntity.setStart(0 + lastSize);
				writerEntity.setEnd(endPosition + lastSize);
				OtherMemoryses.add(writerEntity);
				continue;
			} else {
				//上一个
				OtherMemory oMemory = OtherMemorys.get(i - 1);
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
					writerEntity = new OtherMemorys();
					mList.add(curMemory);
					writerEntity.setList(mList);
					writerEntity.setDate(curMemory.getMemoryDateForShow().substring(8, 10));
					writerEntity.setYearMouth(curMemory.getMemoryDateForShow().substring(0, 7));
					++endPosition;
					writerEntity.setStart(endPosition + lastSize);
					++endPosition;
					writerEntity.setPosition(i);
					writerEntity.setEnd(endPosition + lastSize);
					OtherMemoryses.add(writerEntity);
					continue;
				}
			}
		}
		return OtherMemoryses;
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
