package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Lable;
import com.time.memory.model.AddTagController;
import com.time.memory.model.impl.IAddTagController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IAddTagView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:群标签管理
 * @date 2016/10/24 19:27
 */
public class GroupTagPresenter extends BasePresenter<IAddTagView> {
	// m层
	private IAddTagController addTagController;

	public GroupTagPresenter() {
		addTagController = new AddTagController();
	}

	/**
	 * 创建群标签
	 *
	 * @param url
	 * @param title
	 */
	public void reqAddTag(String url, String groupId, final String title, List<Lable> mTags) {
		if (mTags == null) mTags = new ArrayList<>();
		if (TextUtils.isEmpty(title)) {
			if (mView != null) {
				mView.showShortToast("请输入标签");
				return;
			}
		}
		for (Lable lable : mTags) {
			if (lable.getLabelName().equals(title)) {
				mView.showShortToast("已有标签");
				return;
			}
		}
		if (mView != null) {
			mView.showLoadingDialog();
		}
		addTagController.reqAddTag(url, title, groupId, new SimpleCallback() {
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
					Lable entity = (Lable) data;
					entity.setLabelName(title);
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.showSuccess();
							mView.addTagView(entity);
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
		});
	}

	/**
	 * 删除TAG
	 */
	public void removeTag(String url, String labelId, final int position) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		addTagController.removeTag(url, labelId, new SimpleCallback() {
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
						//成功
						if (mView != null) {
							mView.removeTagSuccess(position);
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
		});
	}

	/**
	 * 获取群的Tags
	 */
	public void getGroupTags(final String Id) {
		if (mView != null)
			mView.showLoadingDialog();
		addTagController.getGroupTags(context.getString(R.string.FSGROUPTAGS), Id, new SimpleCallback() {
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
					Lable entity = (Lable) data;
					if (entity.getStatus() == 0) {
						if (entity.getGroupLabelVoList() != null && !entity.getGroupLabelVoList().isEmpty()) {
							//成功
							if (mView != null) {
								mView.setTags(entity.getGroupLabelVoList());
							}
						}
						if (mView != null) {
							mView.showSuccess();
							mView.showAddTagView(true);
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
