package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Lable;
import com.time.memory.entity.MGroup;
import com.time.memory.model.AddTagController;
import com.time.memory.model.GroupController;
import com.time.memory.model.impl.IAddTagController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IAddTagView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:添加标签
 * @date 2016/9/26 10:35
 */
public class AddTagPresenter extends BasePresenter<IAddTagView> {
	private static final String TAG = "AddTagPresenter";
	// m层
	private IAddTagController addTagController;
	private IGroupController iGroupController;

	public AddTagPresenter() {
		addTagController = new AddTagController();
		iGroupController = new GroupController();
	}

	/**
	 * 创建标签
	 *
	 * @param msg
	 * @param type
	 */
	public void addTag(String msg, String groupId, int type, List<Lable> mTags) {
		if (mTags == null) mTags = new ArrayList<>();
		if (TextUtils.isEmpty(msg)) {
			if (mView != null) {
				mView.showShortToast("请输入标签");
				return;
			}
		}
		for (Lable lable : mTags) {
			if (lable.getLabelName().equals(msg)) {
				mView.showShortToast("已有标签");
				return;
			}
		}
		if (type == 1 || type == 0) {
			//我的,他的
			reqAddUserTag(msg);
		} else if (type == 2) {
			//群的
			reqAddGroupTag(msg, groupId);
		}
	}

	/**
	 * 根据类别去取不同的数据
	 *
	 * @param Id
	 * @param type
	 */
	public void getTags(String Id, int type) {
		if (type == 0) {
			//他的
			getUserTags(Id, type);
		} else if (type == 1) {
			//我的
			getUserTags(Id, type);
		} else if (type == 2) {
			//群的
			getGroupTags(Id);
		}
	}

	/**
	 * 删除TAG
	 */
	public void removeTag(String Id, int type, String labelId, int position) {
		String url = null;
		if (type == 1) {
			//我的
			url = context.getString(R.string.FSREMOVEMYTAG);
		} else if (type == 2) {
			//群的
			url = context.getString(R.string.FSREMOVEGROUPTAG);
		}
		removeTag(url, labelId, position);
	}

	/**
	 * 删除标签
	 */
	private void removeTag(String url, String labeId, final int position) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		addTagController.removeTag(url, labeId, new SimpleCallback() {
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
	 * 创建我的标签
	 */
	private void reqAddUserTag(final String title) {
		if (mView != null)
			mView.showLoadingDialog();
		addTagController.reqAddUserTag(context.getString(R.string.FSADDUSERTAG), title, new SimpleCallback() {
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
						entity.setLabelName(title);
						if (mView != null) {
							mView.addTag(entity);
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
	 * 创建群的标签
	 */
	private void reqAddGroupTag(final String title, String groupId) {
		if (mView != null)
			mView.showLoadingDialog();
		addTagController.reqAddTag(context.getString(R.string.FSREADDGROUPTAG), title, groupId, new SimpleCallback() {
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
						entity.setLabelName(title);
						if (mView != null) {
							mView.addTag(entity);
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
	 * 获取用户的Tags
	 */
	private void getUserTags(final String Id, final int type) {
		if (mView != null)
			mView.showLoadingDialog();
		addTagController.getTags(context.getString(R.string.FSUSERTAGS), new SimpleCallback() {
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
//						iGroupController.getGroupByKey(Id);
						//验证
						verfityId(true);
						if (mView != null) {
							mView.setTags(entity.getLabelVoList());
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
	 * 获取群的Tags
	 */
	private void getGroupTags(final String Id) {
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
						//验证
						checkAdmin(Id);
						//成功
						if (mView != null) {
							mView.setTags(entity.getGroupLabelVoList());
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
	 * 检验Group管理员
	 *
	 * @param Id
	 */
	private void checkAdmin(String Id) {
		CLog.e(TAG, "MainApplication.getUserId():" + MainApplication.getUserId());
		MGroup group = iGroupController.getGroupByKey(Id);
		if (group != null) {
			if (!TextUtils.isEmpty(group.getAdminUserId())) {
				if (group.getAdminUserId().equals(MainApplication.getUserId())) {
					verfityId(true);
				} else {
					verfityId(false);
				}
			} else {
				verfityId(false);
			}
		} else {
			verfityId(false);
		}
	}

	/**
	 * 验证权限
	 */
	private void verfityId(boolean isShow) {
		if (mView != null)
			mView.showAddTagView(isShow);
	}
}
