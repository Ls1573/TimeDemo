package com.time.memory.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryPointVo;
import com.time.memory.entity.ReleaseMemory;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.WriterMemory;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.model.AliUploadController;
import com.time.memory.model.MemoryController;
import com.time.memory.model.impl.IAliUploadController;
import com.time.memory.model.impl.IMemoryController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.AttPathUtils;
import com.time.memory.util.BitmapUtils;
import com.time.memory.util.CLog;
import com.time.memory.util.DateUtil;
import com.time.memory.view.impl.IPreviewView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:预览记忆
 * @date 2016/10/11 11:48
 */
public class PreViewPresenter extends BasePresenter<IPreviewView> {
	private static final String TAG = "PreViewPresenter";

	private IAliUploadController iAliUploadController;
	private IMemoryController iMemoryController;

	protected ExecutorService threadPool;    // 线程池

	private int totalCount;//总张数
	private int successCount;//成功张数
	private ReleaseMemory releaseMemory;//要发布的记忆体
	private List<MemoryPointVo> memoryPointVo;//记忆片段
	private List<String> memoryPhotos;
	private List<WriterMemory> mList;
	private int state;


	// m层
	public PreViewPresenter() {
		iMemoryController = new MemoryController();
		iAliUploadController = new AliUploadController(context);
		// 启动线程池
		threadPool = ExecutorManager.getInstance();

		releaseMemory = new ReleaseMemory();
		memoryPhotos = new ArrayList<>();
		mList = new ArrayList<>();
	}

	@Override
	public void detachView() {
		if (handler != null)
			handler.removeCallbacksAndMessages(null);
		super.detachView();
	}

	/**
	 * 筛选数据
	 *
	 * @param list
	 */
	public void filterMemorys(List<WriterMemory> list) {
		if (mView != null)
			mView.showLoadingDialog();
		List<WriterMemory> mList = new ArrayList<>();
		//创建记忆片段
		for (int k = 0; k < list.size(); k++) {
			WriterMemory writerEntity = list.get(k);
			//计算图片中数量
			int size = writerEntity.getPictureEntits().size();
			//描述
			String desc = writerEntity.getDesc();

			if (size == 0 && TextUtils.isEmpty(desc)) {
				continue;
			}
			mList.add(writerEntity);
		}
		if (mView != null) {
			mView.showSuccess();
			mView.setAdapter(mList);
		}
	}


	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int opr = msg.what;
			Bundle bundle = msg.getData();
			if (opr == 0) {
				String path = bundle.getString("path");
				//第几段
				int position = bundle.getInt("position");
				//第几张图片
				int pindex = bundle.getInt("pindex");
				//发送
				upLoadHeader(path, position, pindex);
				CLog.e(TAG, "path:" + path + "  position:" + position + "   " + pindex);
			} else if (opr == 1) {
				//成功
				//拿到的网络的图片
				String netPath = bundle.getString("netPath");
				//第几段
				int position = bundle.getInt("position");
				//第几张图片
				int pindex = bundle.getInt("pindex");
				createList(netPath, position, pindex);
			} else {
				//失败
				if (mView != null) {
					mView.showShortToast("请求异常,请稍后再试");
					mView.showFaild();
				}
			}
		}
	};

	/**
	 * 成功计入
	 *
	 * @param path
	 * @param position
	 * @param index
	 */
	private void createList(String path, int position, int index) {
		successCount++;
		MemoryPointVo pointVo = memoryPointVo.get(position);
		switch (index) {
			case 0:
				pointVo.setPhoto1(path);
				break;
			case 1:
				pointVo.setPhoto2(path);
				break;
			case 2:
				pointVo.setPhoto3(path);
				break;
			case 3:
				pointVo.setPhoto4(path);
				break;
			case 4:
				pointVo.setPhoto5(path);
				break;
			case 5:
				pointVo.setPhoto6(path);
				break;
		}
		memoryPhotos.add(path);
		if (successCount == totalCount) {
			//全部传完了-可以传给后台
			reqMemory();
		} else {
			//继续上传
			++index;
			compressImage(position, index);
		}
	}

	/**
	 * 上传到服务器
	 */
	private void reqMemory() {
		iMemoryController.reqMemoryUpload(context.getString(R.string.FSWRITER), releaseMemory, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showShortToast(context.getString(R.string.net_error));
						mView.showFaild();
					}
				} else {
					Memory entity = (Memory) data;
					if (entity.getStatus() == 0) {
						//发布成功
						if (mView != null) {
							//保存到临时
							mView.setMemory(saveMemoryLoc(entity.getMemoryId(), entity.getMemoryIdSource()));
							mView.showSuccess();
							mView.showShortToast("发布成功");
						}
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(entity.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 * 上传图片到OSS(图片压缩 1张)
	 */
	public void upLoadHeader(String path, final int position, final int index) {
		final String objectKey = "memory/" + DateUtil.getDate() + "/" + UUID.randomUUID().toString().replace("-", "") + ".jpg";//记忆
		iAliUploadController.upLoad(path, objectKey, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			@Override
			public void onSuccess(PutObjectRequest request, PutObjectResult result) {
				//成功
				Message msg = handler.obtainMessage();
				Bundle bundle = msg.getData();
				msg.what = 1;
				bundle.putString("netPath", objectKey);
				bundle.putInt("position", position);
				bundle.putInt("pindex", index);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}

			@Override
			public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
				// 请求异常
				handler.sendMessage(handler.obtainMessage(-1));
			}
		});
	}

	/**
	 * 压缩图片
	 *
	 * @param path
	 * @param position-第几个片段
	 * @param index-第几张图片
	 */
	public void compressImageFromFile(final String path, final int position, final int index) {
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				Bitmap result = BitmapUtils.compressImageFromFile(path);
				// 放到缓存里
				File cachePath = AttPathUtils.getInternalDir();
				File file = new File(cachePath, UUID.randomUUID().toString().replace("-", "") + ".jpg");
				// 保存到本地缓存
				boolean compressBmpToFile = BitmapUtils.compressBmpToFile(result, file.getAbsolutePath().toString());
				//压缩成功
				if (compressBmpToFile) {
					Message msg = handler.obtainMessage();
					Bundle bundle = msg.getData();
					msg.what = 0;
					bundle.putString("path", file.getAbsolutePath().toString());
					bundle.putInt("position", position);
					bundle.putInt("pindex", index);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			}
		});
	}

	/**
	 * 上传记忆
	 */
	public void upLoadMemory(List<WriterMemory> mList, WriterStyleMemory styleMemory, int state, String Id) {
		if (mList == null || mList.isEmpty()) {
			if (mView != null)
				mView.showShortToast("请选择一个记忆");
			return;
		}
		successCount = 0;
		totalCount = 0;
		this.state = state;
		this.mList.clear();
		this.mList.addAll(mList);

		memoryPointVo = new ArrayList<>();
		mView.showLoadingDialog();

		//创建记忆片段
		for (int k = 0; k < mList.size(); k++) {
			WriterMemory writerEntity = mList.get(k);
			//计算图片中数量
			int size = writerEntity.getPictureEntits().size();
			//增加片段
			memoryPointVo.add(new MemoryPointVo(String.valueOf(k), writerEntity.getDesc(), String.valueOf(size), writerEntity.getDate(), writerEntity.getAddress()));
			//计算总数量
			totalCount += size;
		}
		styleMemory.setState(String.valueOf(state));
		if (state == 2)
			styleMemory.setGroupId(Id);
		//加入头信息
		releaseMemory.setMemoryVo(styleMemory);
		//加入片段信息
		releaseMemory.setMemoryPointVo(memoryPointVo);

		//没有图片的情况-->直接上传
		if (totalCount == 0) {
			reqMemory();
		} else {
			compressImage(0, 0);
		}
		CLog.e(TAG, "totalCount:" + totalCount);
	}


	/**
	 * 压缩图片(单张)
	 */
	private void compressImage(int position, int index) {
		if (position == mList.size()) return;
		//当前片段下没有图片||数量超出了->重置
		if (mList.get(position).getPictureEntits() == null || mList.get(position).getPictureEntits().isEmpty() || mList.get(position).getPictureEntits().size() == index) {
			++position;
			index = 0;
		}
		//每个片段里的图片
		compressImageFromFile(mList.get(position).getPictureEntits().get(index).getPhotoPath(), position, index);
	}

	/**
	 * 保存到临时
	 */
	private TempMemory saveMemoryLoc(String memoryId, String memoryIdSource) {
		TempMemory tempMemory = new TempMemory();
		//用户Id
		tempMemory.setUserId(releaseMemory.getMemoryVo().getUserId());
		tempMemory.setAddcount("0");
		tempMemory.setGroupId(releaseMemory.getMemoryVo().getGroupId());
		tempMemory.setCommentcount("0");
		tempMemory.setForkcount("0");
		tempMemory.setPiccount(String.valueOf(memoryPhotos.size()));
		tempMemory.setTitle(releaseMemory.getMemoryVo().getTitle());
		tempMemory.setMemoryId(memoryId);
		tempMemory.setMemoryIdSource(memoryIdSource);
		tempMemory.setType(releaseMemory.getMemoryVo().getState());
		tempMemory.setUpdateForshowDate(releaseMemory.getMemoryVo().getMemoryDate());
		tempMemory.setLocal(releaseMemory.getMemoryPointVo().get(0).getLocal());
		if (memoryPhotos.size() > 0)
			tempMemory.setPhoto1(memoryPhotos.get(0));
		if (memoryPhotos.size() > 1)
			tempMemory.setPhoto2(memoryPhotos.get(1));
		if (memoryPhotos.size() > 2)
			tempMemory.setPhoto3(memoryPhotos.get(2));
		//清空数据
//		iMemoryTempController.deleteAll();
		//保存
//		iMemoryTempController.saveMemory(tempMemory);
		return tempMemory;
	}

}
