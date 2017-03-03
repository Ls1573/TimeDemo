package com.time.memory.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.model.GroupController;
import com.time.memory.model.MemoryInfoController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.IMemoryInfoController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.NetUtils;
import com.time.memory.view.impl.IEditMView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑记忆
 * @date 2016/10/25 17:49
 */
public class EditMPresenter extends BasePresenter<IEditMView> {
	private static final String TAG = "WriterMPresenter";

	private IGroupController iGroupController;
	private IMemoryInfoController iMemoryInfoController;
	private ExecutorService executorManager;

	// m层
	public EditMPresenter() {
		iGroupController = new GroupController();
		iMemoryInfoController = new MemoryInfoController();
		executorManager = ExecutorManager.getInstance();
	}

	@Override
	public void detachView() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		super.detachView();
	}

	/**
	 * 处理数据
	 */
	public void setMemoryInfo(MemoryInfo memoryInfo) {
		ArrayList<MemoryEdit> list = filterPoint(memoryInfo.getMemoryPointVos());
		WriterStyleMemory writerStyleMemory = convertStyle(memoryInfo.getMemory());
		convertDate(list);
		if (mView != null) {
			mView.setAdapter(list, writerStyleMemory);
		}
	}

	/**
	 * 筛选出不是追加记忆片段
	 */
	private ArrayList<MemoryEdit> filterPoint(ArrayList<MemoryEdit> memoryEdits) {
		ArrayList<MemoryEdit> list = new ArrayList<>();
		for (MemoryEdit memoryEdit : memoryEdits) {
			if (memoryEdit.getAddFlag().equals("1"))
				list.add(memoryEdit);
		}
		return list;
	}

	/**
	 * 日期转换
	 *
	 * @param list
	 */
	private void convertDate(ArrayList<MemoryEdit> list) {
		if (list == null) list = new ArrayList<>();
		for (MemoryEdit entity : list) {
			entity.setNewDate(entity.getMpDateForShow());
		}
	}

	/**
	 * 转换头数据
	 *
	 * @return
	 */
	public WriterStyleMemory convertStyle(Memory memory) {
		WriterStyleMemory writerStyleMemory = new WriterStyleMemory();
		writerStyleMemory.setTitle(memory.getTitle());
		writerStyleMemory.setUserId(memory.getUserId());
		writerStyleMemory.setLabelName(memory.getLabelName());
		writerStyleMemory.setMemoryId(memory.getMemoryId());
		writerStyleMemory.setMemoryDate(memory.getMemoryDateForShow());
		writerStyleMemory.setUsername(memory.getUserName());
		return writerStyleMemory;
	}


	/**
	 * 日期转换
	 *
	 * @reurn
	 */
	public void convertDate(String date, int position) {
		String[] curDates = date.split("-");
		int year = Integer.parseInt(curDates[0]);
		int month = Integer.parseInt(curDates[1]);
		int day = Integer.parseInt(curDates[2]);
		if (mView != null)
			mView.showDatePicker(year, month, day, position);
	}

	/**
	 * 日期转换
	 *
	 * @reurn
	 */
	public void convertItemDate(String date, int position) {
		String[] curDates = date.split("\\.");
		int year = Integer.parseInt(curDates[0]);
		int month = Integer.parseInt(curDates[1]);
		int day = Integer.parseInt(curDates[2]);
		if (mView != null)
			mView.showDatePicker(year, month, day, position);
	}

	/**
	 * 日期截取
	 *
	 * @reurn
	 */
	public String subStringDate(String date) {
		return date.substring(0, 10);
	}

	/**
	 * 删除项
	 *
	 * @param position
	 * @param index
	 */
	public void delete(final int position, final int index, List<MemoryEdit> list) {
		MemoryEdit memory = list.get(position);
		if (memory.getPhotoInfos().size() == 1) {
			//提示是否删除掉左右一张图片
			//TODO
			reqPremission(position, index, memory, list);
		} else {
			//正常删除
			deleteMemoryPic(position, index, memory);
		}
	}

	/**
	 * 删除图片
	 */
	private void deleteMemoryPic(final int position, final int index, MemoryEdit memory) {
		//正常删除
		memory.getPhotoInfos().remove(index);
		mView.refreshItemAdapter(position);
	}

	/**
	 * 删除片段
	 */
	private void deleteMemoryPoint(final int position, List<MemoryEdit> list) {
		list.remove(position);
		int sumNum = list.size();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setNum((i + 1) + "/" + sumNum);
		}
		mView.removeItemAdapter(position);
	}

	/**
	 * 请求询问
	 *
	 * @param position
	 * @param index
	 * @param memory
	 */
	private void reqPremission(final int position, final int index, final MemoryEdit memory, final List<MemoryEdit> list) {
		DialogUtils.request(context, "删除图片还是记忆?", "记忆", "图片", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isTrue = (boolean) data;
				if (!isTrue) {
					//正常删除图片
					deleteMemoryPic(position, index, memory);
				} else {
					//删除记忆
					deleteMemoryPoint(position, list);
				}
			}
		});
	}

	/**
	 * 获取信息头
	 *
	 * @return
	 */
	private WriterStyleMemory getWriterStyleMemory(int state, String Id) {
		WriterStyleMemory writerStyleMemory = new WriterStyleMemory();
		if (state == 0) {
			//公开
			writerStyleMemory.setSign(String.format(context.getString(R.string.memory_sign), context.getString(R.string.memory_sign)));
		} else if (state == 1) {
			//私密
			writerStyleMemory.setSign(String.format(context.getString(R.string.memory_sign), context.getString(R.string.memory_sign)));
		} else {
			//群的
			MGroup mGroup = iGroupController.getGroupByKey(Id);
			if (mGroup != null) {
				writerStyleMemory.setSign(String.format(context.getString(R.string.memory_sign), mGroup.getGroupName()));
			} else {
				writerStyleMemory.setSign(String.format(context.getString(R.string.memory_sign), context.getString(R.string.memory_groupsign)));
			}
		}
		return writerStyleMemory;
	}

	/**
	 * 编辑地址信息
	 */
	public void addAddress(String address, int positon, ArrayList<MemoryEdit> mList) {
		if (TextUtils.isEmpty(address)) {
			//地址为空
		} else {
			mList.get(positon).setLocal(address);
			if (mView != null) {
				mView.refreshItemAdapter(positon);
			}
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				addMessage();
			}
		}
	};

	/**
	 * 追加
	 */
	private void addMessage() {
		// TODO
		if (mView != null) {
			mView.addAdapter(true);
			mView.showSuccess();
		}
	}

	/**
	 * 新增信息
	 *
	 * @param mList
	 * @param photoList
	 * @param position
	 */
	public void addMessage(final ArrayList<MemoryEdit> mList, final ArrayList<PhotoInfo> photoList, final int position, final String memoryId) {
		if (mView != null)
			mView.showLoadingDialog();
		executorManager.submit(new Runnable() {
			@Override
			public void run() {
				try {
					ArrayList<MemoryEdit> writerMemories = convertPhotos(photoList, memoryId);
					//获取位置
					getAddress(writerMemories);
					// 分组
					if (!mList.isEmpty()) {
						//TODO 分组
						mList.get(0).setIsFirst(false);
						mList.get(mList.size() - 1).setIsLast(false);
					}
					mList.addAll(position, writerMemories);
					mList.get(0).setIsFirst(true);
					mList.get(mList.size() - 1).setIsLast(true);

					int sumNum = mList.size();
					for (int i = 0; i < mList.size(); i++) {
						mList.get(i).setNum((i + 1) + "/" + sumNum);
					}
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取地址
	 *
	 * @param writerMemories
	 */
	private void getAddress(ArrayList<MemoryEdit> writerMemories) {
		if (!NetUtils.isNetworkAvailable(context)) return;
		//构造 GeocodeSearch 对象
		GeocodeSearch geocoderSearch = new GeocodeSearch(context);
		//需查询对象
		RegeocodeQuery query;
		for (MemoryEdit memory : writerMemories) {
			//有经纬度
			if (memory.getLatitude() != 0d && memory.getLongitude() != 0d) {
				// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
				query = new RegeocodeQuery(new LatLonPoint(memory.getLatitude(), memory.getLongitude()), 200, GeocodeSearch.AMAP);
				try {
					RegeocodeAddress address = geocoderSearch.getFromLocation(query);
					String addressName = address.getCity() + address.getDistrict();
					//CLog.e(TAG, "addressName:" + addressName);
					if (!TextUtils.isEmpty(addressName))
						memory.setLocal(addressName);
				} catch (AMapException e) {
					memory.setLocal("");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取最大张数
	 *
	 * @param mList
	 */
	public void getMaxSize(List<PhotoInfo> mList) {
		int total = 0;
		for (PhotoInfo entity : mList) {
			if (entity.getPhotoPath().contains("http")) {
				total++;
			}
		}
		if (mView != null) {
			mView.checkPhoto(6 - total);
		}
	}

	/**
	 * 新增图片
	 *
	 * @param mList
	 * @param photoList
	 * @param position
	 */
	public void addPic(ArrayList<MemoryEdit> mList, ArrayList<PhotoInfo> photoList, int position) {
		//TODO
		ArrayList<PhotoInfo> newList = new ArrayList<>();
		//取差集数据
		int size = mList.get(position).getPhotoInfos().size();
		if (size == 0) {
			for (PhotoInfo pEntity : photoList) {
				newList.add(pEntity);
			}
		} else {
			for (PhotoInfo pEntity : photoList) {
				for (int i = 0; i < size; i++) {
					PhotoInfo eEntity = mList.get(position).getPhotoInfos().get(i);
					if (!eEntity.getPhotoPath().equals(pEntity.getPhotoPath())) {
						if (i == size - 1) {
							//最后一个
							newList.add(pEntity);
						} else {
							continue;
						}
					} else {
						break;
					}
				}
			}
		}

		mList.get(position).getPhotoInfos().addAll(newList);
		// TODO
		if (mView != null) {
			mView.refreshItemAdapter(position);
		}
	}

	/**
	 * 装换
	 */
	private ArrayList<MemoryEdit> convertPhotos(ArrayList<PhotoInfo> photoList, String memoryId) {
		ArrayList<MemoryEdit> writerMemories = new ArrayList<>();
		MemoryEdit writerEntity;
		ArrayList<PhotoInfo> pList = null;
		//分组排序
		for (int i = 0; i < photoList.size(); i++) {
			PhotoInfo cPhoto = photoList.get(i);
			if (i == 0) {
				pList = new ArrayList<>();
				writerEntity = new MemoryEdit();
				pList.add(cPhoto);
				writerEntity.setPhotoInfos(pList);
				writerEntity.setDate(cPhoto.getDate());
				writerEntity.setMemoryId(memoryId);
				writerEntity.setMemoryPointId("");
				writerMemories.add(writerEntity);
				continue;
			} else {
				//上一个
				PhotoInfo oPhoto = photoList.get(i - 1);
				//日期相等加到一起去
				if (oPhoto.getDate().equals(cPhoto.getDate()) && pList.size() < 6) {
					pList.add(cPhoto);
					continue;
				} else {
					pList = new ArrayList<>();
					writerEntity = new MemoryEdit();
					pList.add(cPhoto);
					writerEntity.setPhotoInfos(pList);
					writerEntity.setDate(cPhoto.getDate());
					writerEntity.setMemoryId(memoryId);
					writerEntity.setMemoryPointId("");
					writerMemories.add(writerEntity);
					continue;
				}
			}
		}
		int sumNum = writerMemories.size();
		for (int i = 0; i < writerMemories.size(); i++) {
			writerMemories.get(i).setNum((i + 1) + "/" + sumNum);
		}
		return writerMemories;
	}
}
