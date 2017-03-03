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
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.MemoryPointVo;
import com.time.memory.entity.MemorySuppory;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.SupporyMemory;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.User;
import com.time.memory.entity.WriterMemory;
import com.time.memory.model.AliUploadController;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryController;
import com.time.memory.model.impl.IAliUploadController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.AttPathUtils;
import com.time.memory.util.BitmapUtils;
import com.time.memory.util.CLog;
import com.time.memory.util.DateUtil;
import com.time.memory.view.impl.ISupporyPView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:追加记忆
 * @date 2016/10/25 15:49
 */
public class SupporyPPresenter extends BasePresenter<ISupporyPView> {
	private static final String TAG = "PreViewPresenter";

	private IAliUploadController iAliUploadController;
	private IMemoryController iMemoryController;
	private ILoginController iLoginController;
	protected ExecutorService threadPool;    // 线程池

	private int totalCount;//总张数
	private int successCount;//成功张数
	private SupporyMemory supporyMemory;//要发布的记忆体
	private MemoryPointVo memoryPointVo;
	private List<PhotoInfo> photoInfos;

	// m层
	public SupporyPPresenter() {
		threadPool = ExecutorManager.getInstance();
		iMemoryController = new MemoryController();
		iAliUploadController = new AliUploadController(context);
		iLoginController = new LoginController();


		//记忆体
		supporyMemory = new SupporyMemory();
		memoryPointVo = new MemoryPointVo();
		photoInfos = new ArrayList<>();
		supporyMemory.setMemoryPointVo(memoryPointVo);
	}

	@Override
	public void detachView() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		super.detachView();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int opr = msg.what;
			Bundle bundle = msg.getData();
			if (opr == 0) {
				String path = bundle.getString("path");
				//第几张图片
				int pindex = bundle.getInt("pindex");
				//发送
				upLoadHeader(path, pindex);
				CLog.e(TAG, "path:" + path + "   " + pindex);
			} else if (opr == 1) {
				//成功
				//拿到的网络的图片
				String netPath = bundle.getString("netPath");
				//第几张图片
				int pindex = bundle.getInt("pindex");
				createList(netPath, pindex);
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
	 * @param index
	 */
	private void createList(String path, int index) {
		successCount++;
		switch (index) {
			case 0:
				memoryPointVo.setPhoto1(path);
				break;
			case 1:
				memoryPointVo.setPhoto2(path);
				break;
			case 2:
				memoryPointVo.setPhoto3(path);
				break;
			case 3:
				memoryPointVo.setPhoto4(path);
				break;
			case 4:
				memoryPointVo.setPhoto5(path);
				break;
			case 5:
				memoryPointVo.setPhoto6(path);
				break;
		}
		if (successCount == totalCount) {
			//全部传完了-可以传给后台
			reqMemory();
		} else {
			//继续上传
			++index;
			compressImage(index);
		}
	}

	/**
	 * 上传到服务器
	 */
	private void reqMemory() {
		iMemoryController.reqMemoryUpload(context.getString(R.string.FSSUPPORT), supporyMemory, new SimpleCallback() {
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
					MemorySuppory entity = (MemorySuppory) data;
					if (entity.getStatus() == 0) {
						//发布成功
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("发布成功");
							mView.setMemory(saveMemoryLoc(entity));
						} else {
							mView.showFaild();
							mView.showShortToast(entity.getMessage());
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
	 * 保存到临时
	 */
	private TempMemory saveMemoryLoc(MemorySuppory entity) {
		User user = iLoginController.getUser(MainApplication.getUserId());
		TempMemory tempMemory = new TempMemory();
		//用户Id
		tempMemory.setUserId(user.getUserId());//Id
		tempMemory.setUserName(user.getUserName());//Name
		tempMemory.setAddcount("0");
		tempMemory.setLocal(memoryPointVo.getLocal());//地址
		tempMemory.setGroupId(memoryPointVo.getGroupId());//群Id
		tempMemory.setCommentcount("0");
		tempMemory.setForkcount("0");
		tempMemory.setPiccount(String.valueOf(photoInfos.size()));
		tempMemory.setDesc(memoryPointVo.getDetail());
		tempMemory.setInsDateForShow(DateUtil.getCurrentDateLine());
		tempMemory.setMemoryId(memoryPointVo.getMemoryId());//记忆Id
		tempMemory.setMemoryPointId(entity.getUuid());//记忆片段Id
		tempMemory.setUpdateForshowDate(memoryPointVo.getMemoryPointDate());
		//图片集
		if (!TextUtils.isEmpty(memoryPointVo.getPhoto1()))
			tempMemory.setPhoto1(memoryPointVo.getPhoto1());
		if (!TextUtils.isEmpty(memoryPointVo.getPhoto2()))
			tempMemory.setPhoto2(memoryPointVo.getPhoto2());
		if (!TextUtils.isEmpty(memoryPointVo.getPhoto3()))
			tempMemory.setPhoto3(memoryPointVo.getPhoto3());
		if (!TextUtils.isEmpty(memoryPointVo.getPhoto4()))
			tempMemory.setPhoto4(memoryPointVo.getPhoto4());
		if (!TextUtils.isEmpty(memoryPointVo.getPhoto5()))
			tempMemory.setPhoto5(memoryPointVo.getPhoto5());
		if (!TextUtils.isEmpty(memoryPointVo.getPhoto6()))
			tempMemory.setPhoto6(memoryPointVo.getPhoto6());
		return tempMemory;
	}

	/**
	 * 上传图片到OSS(图片压缩 1张)
	 */
	public void upLoadHeader(String path, final int index) {
		final String objectKey = "memory/" + DateUtil.getDate() + "/" + UUID.randomUUID().toString().replace("-", "") + ".jpg";//记忆
		iAliUploadController.upLoad(path, objectKey, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			@Override
			public void onSuccess(PutObjectRequest request, PutObjectResult result) {
				//成功
				Message msg = handler.obtainMessage();
				Bundle bundle = msg.getData();
				msg.what = 1;
				bundle.putString("netPath", objectKey);
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

	// 压缩图片
	public void compressImageFromFile(final String path, final int index) {
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
	public void upLoadMemory(WriterMemory writerEntity, int state, String Id, String memoryId, String memorySourceId, String userId, String addUserId) {
		successCount = 0;
		totalCount = 0;
		photoInfos.clear();
		photoInfos.addAll(writerEntity.getPictureEntits());
		mView.showLoadingDialog();
		//计算图片中数量
		totalCount = photoInfos.size();
		//如果是群->传入群Id
		if (state == 2)
			memoryPointVo.setGroupId(Id);

		memoryPointVo.setMemoryPointDate(writerEntity.getDate());
		memoryPointVo.setLocal(writerEntity.getAddress());
		memoryPointVo.setDetail(writerEntity.getDesc());
		memoryPointVo.setPhotoCount(String.valueOf(totalCount));
		memoryPointVo.setMemoryId(memoryId);
		memoryPointVo.setUserId(userId);
		memoryPointVo.setAddUserId(addUserId);

		memoryPointVo.setSource(state == 2 ? "2" : "1");
		memoryPointVo.setMemoryIdSource(memorySourceId);


		supporyMemory.setState(String.valueOf(state));

		CLog.e(TAG, "totalCount:" + totalCount);
		if (totalCount == 0) {
			//没有图片
			reqMemory();
		} else {
			//压缩
			compressImage(0);
		}
	}

	/**
	 * 压缩图片(单张)
	 */
	private void compressImage(int position) {
		if (position == totalCount) return;
		compressImageFromFile(photoInfos.get(position).getPhotoPath(), position);
	}
}
