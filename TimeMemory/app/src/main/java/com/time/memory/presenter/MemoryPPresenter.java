package com.time.memory.presenter;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryInfoController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryInfoController;
import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.vo.MemoryDelVo;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IMemoryPView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆片段
 * @date 2016/10/27 10:04
 */
public class MemoryPPresenter extends BasePresenter<IMemoryPView> {
	private static final String TAG = "MemoryPPresenter";

	private ILoginController iLoginController;
	private IMemoryInfoController iMemoryInfoController;

	// m层
	public MemoryPPresenter() {
		iLoginController = new LoginController();
		iMemoryInfoController = new MemoryInfoController();
	}

	/**
	 * 获取用户
	 *
	 * @param key
	 */
	public void getUser(String key) {
		User user = iLoginController.getUser(key);
		if (mView != null) {
			mView.setUser(user);
		}
	}

	/**
	 * 删除记忆
	 *
	 * @param userId              记忆发布人
	 * @param memoryId            记忆ID
	 * @param memoryPointId       记忆片断ID
	 * @param addUserId           记忆片断补充人ID
	 * @param groupId             圈子ID
	 * @param mpFlg               本体/补充区分 0:本体片段 1:补充片段
	 * @param source              1:我的记忆;2:其他书的
	 * @param memoryIdSource      记忆Id的源Id
	 * @param memoryPonitIdSource 记忆片段Id的源Id
	 */
	public void removeMemory(String url, String token, String userId, String memoryId, String memoryPointId, String addUserId, String groupId, final int position, final int state, boolean isLast,
							 String mpFlg, String source, String memoryIdSource, String memoryPonitIdSource
	) {
		//构建
		MemoryDelVo msgRequest = new MemoryDelVo();
		msgRequest.setUserId(userId);
		msgRequest.setMemoryId(memoryId);

		msgRequest.setMpFlg(mpFlg);
		msgRequest.setSource(source);
		msgRequest.setMemoryIdSource(memoryIdSource);
		msgRequest.setMemoryPonitIdSource(memoryPonitIdSource);

		if (!isLast)
			msgRequest.setMemoryPointId(memoryPointId);
		else
			msgRequest.setMemoryPointId("");
		msgRequest.setGroupId(groupId);

		MemoryDelVo memoryDelVo = new MemoryDelVo();
		memoryDelVo.setMemoryDelVo(msgRequest);
		memoryDelVo.setType(BusynessType.CX01.getIndex());

		if (mView != null)
			mView.showLoadingDialog();
		iMemoryInfoController.reqRemoveMemory(url, token, msgRequest, new SimpleCallback() {
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
						// 成功
						if (mView != null) {
							mView.showSuccess();
							mView.removeMemory(position, state);
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

}
