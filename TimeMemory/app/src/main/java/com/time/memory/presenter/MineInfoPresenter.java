package com.time.memory.presenter;

import android.graphics.Bitmap;
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
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.User;
import com.time.memory.model.AliUploadController;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.IAliUploadController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.AttPathUtils;
import com.time.memory.util.BitmapUtils;
import com.time.memory.util.DateUtil;
import com.time.memory.view.impl.IMineInfoView;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:个人信息
 * @date 2016-9-5下午1:14:57
 * ==============================
 */
public class MineInfoPresenter extends BasePresenter<IMineInfoView> {
	private static final String TAG = "MineInfoPresenter";
	// m层
	private ILoginController iLoginController;
	private IAliUploadController iAliUploadController;
	private IGroupController iGroupController;
	// 线程池
	protected ExecutorService threadPool;

	public MineInfoPresenter() {
		iLoginController = new LoginController();
		iGroupController = new GroupController();
		iAliUploadController = new AliUploadController(context);
		// 启动线程池
		threadPool = ExecutorManager.getInstance();
	}

	@Override
	public void detachView() {
		super.detachView();
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int opr = msg.what;
			if (opr == 0) {
				//发送
				upLoadHeader(msg.obj.toString());
			} else if (opr == 1) {
				//成功
				if (mView != null) {
					if (mView != null)
						mView.upLoadMsg(msg.obj.toString());
				}
			} else if (opr == -1) {
				//失败
				if (mView != null) {
					mView.showShortToast("请求异常,请稍后再试");
					mView.showFaild();
				}
			}
		}
	};

	// 压缩图片
	public void compressImageFromFile(final String path) {
		if (TextUtils.isEmpty(path)) return;
		mView.showLoadingDialog();
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				String imgPath = path.substring(path.indexOf("file:///") + "file:///".length());
				Bitmap result = BitmapUtils.compressImageFromFile(imgPath);
				// 放到缓存里
				File cachePath = AttPathUtils.getInternalDir();
				File file = new File(imgPath);
				// 保存到本地缓存
				boolean compressBmpToFile = BitmapUtils.compressBmpToFile(result, file.getAbsolutePath().toString());
				//压缩成功
				if (compressBmpToFile) {
					handler.sendMessage(handler.obtainMessage(0, file.getAbsolutePath().toString()));
				}
			}
		});
	}

	/**
	 * 上传图片(图片压缩 1张)
	 */
	public void upLoadHeader(String path) {
		final String objectKey = "header/" + DateUtil.getDate() + "/" + UUID.randomUUID().toString().replace("-", "") + ".jpg";//头像
		iAliUploadController.upLoad(path, objectKey, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			@Override
			public void onSuccess(PutObjectRequest request, PutObjectResult result) {
				//成功
				handler.sendMessage(handler.obtainMessage(1, objectKey));
			}

			@Override
			public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
				// 请求异常
				handler.sendMessage(handler.obtainMessage(-1));
			}
		});
	}

	/**
	 * 获取用户信息
	 */
	public void getUser(String userId) {
		User user = iLoginController.getUser(userId);
		if (mView != null) {
			mView.setUserInfos(user);
		}
	}

	/**
	 * 上传信息
	 *
	 * @param url
	 * @param pic
	 * @param nickName
	 * @param email
	 * @param company
	 * @param sex
	 * @param user
	 */
	public void uploadMsg(final String url, final String pic, final String nickName, final String email, final String company, final String sex, final PhotoInfo photoInfo, final User user) {
		String userGender = "";
		String imgPath = null;
		if (!TextUtils.isEmpty(sex))
			userGender = sex.equals("男") ? "1" : "2";

		if (!TextUtils.isEmpty(pic)) {
				String imgWeb = context.getString(R.string.FSIMAGEPATH);
			if (pic.contains(imgWeb))
				imgPath = pic.substring(imgWeb.length());
			else
				imgPath = pic;
			user.setHeadPhoto(imgPath);
			user.setLocpath(photoInfo.getPhotoPath());
		}
		iLoginController.upDateUser(url, imgPath, nickName, email, company, userGender, user.getProvinceId(), user.getCityId(), user.getSign(), user.getCompanyIntroduce(), new SimpleCallback() {
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
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						if (mView != null) {
							//保存
							iLoginController.updateUser(convertUser(nickName, email, company, sex, user));
//							mView.showShortToast("修改成功");
							upDateGroupsPic(pic);
							mView.showSuccess();
						}
					} else {
						if (mView != null) {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					}
				}
			}
		});
	}

	/**
	 * 更新圈子图片
	 */
	private void upDateGroupsPic(String pic) {
		if (TextUtils.isEmpty(pic)) return;
		String imgPath;
		if (!TextUtils.isEmpty(pic)) {
			String imgWeb = context.getString(R.string.FSIMAGEPATH);
			if (pic.contains(imgWeb))
				imgPath = pic.substring(imgWeb.length());
			else
				imgPath = pic;
			MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
			if (mGroup != null) {
				mGroup.setHeadPhoto1(imgPath);
				iGroupController.upMGroup(mGroup);
			}
		}
	}

	/**
	 * 上传图片
	 */
	public void uploadImage(PhotoInfo photoInfo) {
		//上传图片
		if (photoInfo != null)
			compressImageFromFile(photoInfo.getPhotoPath());
		else {
			if (mView != null)
				mView.upLoadMsg("");
		}
	}

	/**
	 * 换装
	 *
	 * @return
	 */
	private User convertUser(String nickName, String email, String company, String sex, User user) {
		if (!TextUtils.isEmpty(nickName))
			user.setUserName(nickName);
		if (!TextUtils.isEmpty(email))
			user.setEmail(email);
		if (!TextUtils.isEmpty(company))
			user.setCompany(company);
		if (!TextUtils.isEmpty(sex))
			user.setUserGender(sex);
		return user;
	}

}
