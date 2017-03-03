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
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.MemoryPointVo;
import com.time.memory.entity.ReleaseMemory;
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
import com.time.memory.view.impl.IEditPreviewView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:预览编辑
 * @date 2016/10/26 8:56
 */
public class EditPPresenter extends BasePresenter<IEditPreviewView> {
	private static final String TAG = "PreViewPresenter";
	private IAliUploadController iAliUploadController;
	private IMemoryController iMemoryController;
	protected ExecutorService threadPool;    // 线程池


	private int totalCount;//总张数
	private int successCount;//成功张数
	private ReleaseMemory releaseMemory;//要发布的记忆体
	private List<MemoryPointVo> memoryPointVo;//记忆片段
	private List<MemoryEdit> mList;
	private int urlLength;

	// m层
	public EditPPresenter() {
		iMemoryController = new MemoryController();
		iAliUploadController = new AliUploadController(context);
		// 启动线程池
		threadPool = ExecutorManager.getInstance();

		mList = new ArrayList<>();
		releaseMemory = new ReleaseMemory();
	}

	/**
	 * 筛选数据
	 *
	 * @param list
	 */
	public void filterMemorys(List<MemoryEdit> list) {
		if (mView != null)
			mView.showLoadingDialog();
		List<MemoryEdit> mList = new ArrayList<>();
		//创建记忆片段
		for (int k = 0; k < list.size(); k++) {
			MemoryEdit writerEntity = list.get(k);
			//计算图片中数量
			int size = writerEntity.getPhotoInfos().size();
			//描述
			String desc = writerEntity.getDetail();

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
		iMemoryController.reqMemoryUpload(context.getString(R.string.FSEDIT), releaseMemory, new SimpleCallback() {
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
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						//发布成功
						if (mView != null) {
							mView.showSuccess();
							mView.upSuccess();
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

	// 压缩图片
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
	public void upLoadMemory(List<MemoryEdit> mList, WriterStyleMemory styleMemory, int state, String Id) {
		if (mList == null || mList.isEmpty()) {
			if (mView != null)
				mView.showShortToast("请添加一个记忆");
			return;
		}
		successCount = 0;
		totalCount = 0;
		this.mList.clear();
		this.mList.addAll(mList);
		this.urlLength = context.getString(R.string.FSIMAGEPATH).length();
		memoryPointVo = new ArrayList<>();
		mView.showLoadingDialog();

		for (int k = 0; k < mList.size(); k++) {
			MemoryEdit writerEntity = mList.get(k);
			//计算图片中数量
			int size = writerEntity.getPhotoInfos().size();
			//增加片段
			memoryPointVo.add(new MemoryPointVo(String.valueOf(k), writerEntity.getDetail(), String.valueOf(size), writerEntity.getMemoryPointDate(), writerEntity.getLocal(), writerEntity.getMemoryId(), writerEntity.getMemoryPointId(), writerEntity.getUserId()));
			//计算总数量
			totalCount += size;
		}
		//设置权限
		styleMemory.setState(String.valueOf(state));
		//设置群Id
		if (state == 2)
			styleMemory.setGroupId(Id);

		//设置参数
		releaseMemory.setMemoryPointVo(memoryPointVo);
		releaseMemory.setMemoryVo(styleMemory);

		//没有图片的情况-->直接上传
		if (totalCount == 0) {
			reqMemory();
		} else {
			compressImage(0, 0);
		}
	}

	/**
	 * 压缩图片(单张)
	 */
	private void compressImage(int position, int index) {
		if (position == mList.size()) return;
		//当前片段下没有图片||数量超出了->重置
		if (mList.get(position).getPhotoInfos() == null || mList.get(position).getPhotoInfos().isEmpty() || mList.get(position).getPhotoInfos().size() == index) {
			++position;
			index = 0;
		}
		MemoryEdit memoryEdit = mList.get(position);
		String url = memoryEdit.getPhotoInfos().get(index).getPhotoPath();
		if (!url.contains("http")) {
			//不是Url的去压缩上传
			compressImageFromFile(url, position, index);
		} else {
			//如果地址是url->赋值
			createList(url.substring(urlLength), position, index);
		}
	}

}
