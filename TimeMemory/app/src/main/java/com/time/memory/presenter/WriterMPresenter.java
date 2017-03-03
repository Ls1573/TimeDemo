package com.time.memory.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.User;
import com.time.memory.entity.WriterMemory;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.NetUtils;
import com.time.memory.view.impl.IWtieterMView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:写记忆
 * @date 2016-10-3上午10:22:33
 * ==============================
 */
public class WriterMPresenter extends BasePresenter<IWtieterMView> {
	private static final String TAG = "WriterMPresenter";

	private IGroupController iGroupController;
	private ILoginController iLoginController;
	private ExecutorService executorManager;

	// m层
	public WriterMPresenter() {
		iGroupController = new GroupController();
		iLoginController = new LoginController();
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
	 * 获取最大图片数
	 *
	 * @param mList
	 */
	public void getMaxSize(ArrayList<WriterMemory> mList) {
		if (mList == null) mList = new ArrayList<>();
		int total = 0;
		for (WriterMemory memory : mList) {
			if (memory.getPictureEntits() != null)
				total += memory.getPictureEntits().size();
		}
		total = 299 - total;
		if (mView != null) {
			mView.setMaxSize(total);
		}
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
	public void delete(final int position, final int index, List<WriterMemory> list) {
		WriterMemory memory = list.get(position);
		if (memory.getPictureEntits().size() == 1) {
			//提示是否删除掉左右一张图片
			reqPremission(position, index, memory, list);
		} else {
			//正常删除
			deleteMemoryPic(position, index, memory);
		}
	}

	/**
	 * 删除片段
	 */
	private void deleteMemoryPoint(final int position, List<WriterMemory> list) {
		list.remove(position);
		int sumNum = list.size();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setNum((i + 1) + "/" + sumNum);
		}
		mView.refreshItemAdapter(position);
	}

	/**
	 * 删除图片
	 */
	private void deleteMemoryPic(final int position, final int index, WriterMemory memory) {
		//正常删除
		memory.getPictureEntits().remove(index);
		mView.refreshItemAdapter(position);
	}

	/**
	 * 请求询问
	 *
	 * @param position
	 * @param index
	 * @param memory
	 */
	private void reqPremission(final int position, final int index, final WriterMemory memory, final List<WriterMemory> list) {
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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				WriterStyleMemory entity = msg.getData().getParcelable("writerStyleMemory");
				ArrayList<WriterMemory> writerMemories = msg.getData().getParcelableArrayList("writerMemories");
				setMessage(writerMemories, entity);
			} else if (msg.what == 2) {
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
	 * 回调给UI
	 *
	 * @param writerMemories
	 * @param writerStyleMemory
	 */
	private void setMessage(ArrayList<WriterMemory> writerMemories, WriterStyleMemory writerStyleMemory) {
		if (mView != null) {
			mView.setAdapter(writerMemories, writerStyleMemory);
			mView.showSuccess();
		}
	}

	/**
	 * 图片转换
	 *
	 * @param photoList
	 */
	public void getMessage(final ArrayList<PhotoInfo> photoList, final int state, final String Id) {
		if (mView != null)
			mView.showLoadingDialog();
		executorManager.submit(new Runnable() {
			@Override
			public void run() {
				try {
					// 分组
					ArrayList<WriterMemory> writerMemories = convertPhotos(photoList);
					//获取位置
					getAddress(writerMemories);
					//改变下起始位置
					writerMemories.get(0).setIsFirst(true);
					//设置最后一个
					writerMemories.get(writerMemories.size() - 1).setIsLast(true);
					//获取信息头
					WriterStyleMemory writerStyleMemory = getWriterStyleMemory(state, Id);
					//放到Handler里
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.getData().putParcelable("writerStyleMemory", writerStyleMemory);
					msg.getData().putParcelableArrayList("writerMemories", writerMemories);
					handler.sendMessage(msg);
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
	private void getAddress(ArrayList<WriterMemory> writerMemories) {
		if (!NetUtils.isNetworkAvailable(context)) return;
		//构造 GeocodeSearch 对象
		GeocodeSearch geocoderSearch = new GeocodeSearch(context);
		//需查询对象
		RegeocodeQuery query;
		for (WriterMemory memory : writerMemories) {
			//有经纬度
			if (memory.getLatitude() != 0d && memory.getLongitude() != 0d) {
				// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
				query = new RegeocodeQuery(new LatLonPoint(memory.getLatitude(), memory.getLongitude()), 200, GeocodeSearch.AMAP);
				try {
					RegeocodeAddress address = geocoderSearch.getFromLocation(query);
					String addressName = address.getCity() + address.getDistrict();
					//CLog.e(TAG, "addressName:" + addressName);
					if (!TextUtils.isEmpty(addressName))
						memory.setAddress(addressName);
				} catch (AMapException e) {
					memory.setAddress("");
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 获取信息头
	 *
	 * @return
	 */
	private WriterStyleMemory getWriterStyleMemory(int state, String Id) {
		User user = iLoginController.getUser(MainApplication.getUserId());
		CLog.e(TAG, "user:" + user.toString());
		WriterStyleMemory writerStyleMemory = new WriterStyleMemory();
		writerStyleMemory.setUsername(user.getUserName());
		if (state == 0) {
			//公开
			writerStyleMemory.setSign("朋友");
		} else if (state == 1) {
			//私密
			writerStyleMemory.setSign("自己");
		} else {
			//群的
			MGroup mGroup = iGroupController.getGroupByKey(Id);
			if (mGroup != null) {
				writerStyleMemory.setSign(mGroup.getGroupName());
			} else {
				writerStyleMemory.setSign(context.getString(R.string.memory_groupsign));
			}
		}
		return writerStyleMemory;
	}

	/**
	 * 编辑地址信息
	 */
	public void addAddress(String address, int positon, ArrayList<WriterMemory> mList) {
		mList.get(positon).setAddress(address);
		if (mView != null) {
			mView.refreshItemAdapter(positon);
		}
	}

	/**
	 * 编辑目标
	 */
	public void addTarget(MGroup mGroup, int nState, WriterStyleMemory writerStyleMemory) {
		if (mGroup == null) return;
		int state = 1;
		String groupId = "";
		String sign = mGroup.getGroupName();
		//(0:我的;1:他的;2:圈子的)
		if (mGroup.getType() == 0) {
			state = 1;
			sign = "自己";
		} else if (mGroup.getType() == 1) {
			state = 0;
			sign = "朋友";
		} else {
			state = 2;
			groupId = mGroup.getGroupId();
		}
		//判断Lable标签变化
		if (nState == 0 && state == 1) {
			//不变
		} else if (nState == 1 && state == 0) {
			//不变
		} else {
			//变换
			writerStyleMemory.setLabelId("");
			writerStyleMemory.setLabelName("");
			writerStyleMemory.setGroupId("");
			writerStyleMemory.setGroupLabelName("");
		}
		writerStyleMemory.setSign(sign);//头
		if (mView != null) {
			mView.setMessage(state, groupId, sign);
		}
	}

	/**
	 * 新增信息
	 *
	 * @param mList
	 * @param photoList
	 * @param position
	 */
	public void addMessage(final ArrayList<WriterMemory> mList, final ArrayList<PhotoInfo> photoList, final int position) {
		if (mView != null)
			mView.showLoadingDialog();
		executorManager.submit(new Runnable() {
			@Override
			public void run() {
				try {
					ArrayList<WriterMemory> writerMemories = convertPhotos(photoList);
					//获取位置
					getAddress(writerMemories);
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
					//放到Handler里
					handler.sendEmptyMessage(2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 新增图片
	 *
	 * @param mList
	 * @param photoList
	 * @param position
	 */

	public void addPic(ArrayList<WriterMemory> mList, ArrayList<PhotoInfo> photoList, int position) {
		//TODO
		mList.get(position).getPictureEntits().clear();
		mList.get(position).getPictureEntits().addAll(photoList);
		// TODO
		if (mView != null) {
			mView.refreshItemAdapter(position);
		}
	}

	/**
	 * 换装
	 */
	private ArrayList<WriterMemory> convertPhotos(ArrayList<PhotoInfo> photoList) {
		ArrayList<WriterMemory> writerMemories = new ArrayList<>();
		WriterMemory writerEntity;
		List<PhotoInfo> pList = null;
		//分组排序
		for (int i = 0; i < photoList.size(); i++) {
			PhotoInfo cPhoto = photoList.get(i);
			if (i == 0) {
				pList = new ArrayList<>();
				writerEntity = new WriterMemory();
				pList.add(cPhoto);
				writerEntity.setPictureEntits(pList);
				writerEntity.setDate(cPhoto.getDate());
				writerEntity.setLatitude(cPhoto.getLatitude());
				writerEntity.setLongitude(cPhoto.getLongitude());
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
					writerEntity = new WriterMemory();
					pList.add(cPhoto);
					writerEntity.setPictureEntits(pList);
					writerEntity.setDate(cPhoto.getDate());
					writerEntity.setLatitude(cPhoto.getLatitude());
					writerEntity.setLongitude(cPhoto.getLongitude());
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
