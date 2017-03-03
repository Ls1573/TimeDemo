package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Advert;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.GroupAdDto;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.MemoryDto;
import com.time.memory.entity.NewContacts;
import com.time.memory.entity.UnReadMemoryNum;
import com.time.memory.entity.User;
import com.time.memory.model.AdvertController;
import com.time.memory.model.CircleController;
import com.time.memory.model.ContactsController;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryController;
import com.time.memory.model.impl.IAdvertController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryController;
import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.SA01ReqVo;
import com.time.memory.mt.nio.message.response.SA10ReqVo;
import com.time.memory.mt.nio.message.response.SA20RespVo;
import com.time.memory.mt.nio.message.response.SG01RespVo;
import com.time.memory.mt.nio.message.response.SG03RespVo;
import com.time.memory.mt.nio.message.response.SG04RespVo;
import com.time.memory.mt.nio.message.response.SW01RespVo;
import com.time.memory.mt.nio.message.response.SW02RespVo;
import com.time.memory.mt.vo.MemoryDelVo;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DateUtil;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IMemoryView;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:记忆
 * @date 2016-9-6下午15:20:15
 * ==============================
 */
public class MemoryPresenter extends BasePresenter<IMemoryView> {
	private static final String TAG = "MemoryPresenter";
	// m层
	private ICircleController iCircleController;
	private ILoginController iLoginController;
	private IGroupController iGroupController;
	private IAdvertController iAdvertController;
	private IMemoryController iMemoryController;
	private IContactsController iContactsController;

	public MemoryPresenter() {
		iCircleController = new CircleController();
		iLoginController = new LoginController();
		iGroupController = new GroupController();
		iAdvertController = new AdvertController();
		iMemoryController = new MemoryController();
		iContactsController = new ContactsController();
	}

	/**
	 * 获取未读信息数
	 *
	 * @param url
	 */
	public void getUnReadNum(String url, final List<MGroup> mMGroupList) {
		if (mMGroupList == null || mMGroupList.isEmpty()) return;
		iMemoryController.reqMemoryUnRead(url, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
				}
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
				} else {
					UnReadMemoryNum entity = (UnReadMemoryNum) data;
					if (entity.getStatus() == 0) {
						//成功
						saveTotalConut(entity.getTotalPCnt());
						saveContacts(entity.getUfrieds());
						saveGroups(mMGroupList, entity);
					} else {
						//异常->不处理
					}
				}
			}
		});
	}


	/**
	 * 保存所有未评论数据
	 */
	private void saveTotalConut(String total) {
		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		if (mGroup != null) {
			mGroup.setTotalPCnt(total);
			iCircleController.upCirlce(mGroup);
		}
	}

	/**
	 * 保存新增好友
	 */
	private void saveContacts(List<NewContacts> newContactses) {
		if (newContactses == null || newContactses.isEmpty()) return;
		Contacts contacts;
		List<Contacts> contactsVos = new ArrayList<>();
		for (NewContacts entity : newContactses) {
			contacts = new Contacts();
			contacts.setHeadPhoto(entity.getFhp());
			contacts.setPhone(entity.getFmobile());
			contacts.setUserId(entity.getFid());
			contacts.setContactName(entity.getFname());
			contacts.setActiveFlg("0");
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
			contacts.setToUserId(entity.getUid());
			contacts.setIsTwoWayFlg(entity.getIsTwoWayFlg());
			contactsVos.add(contacts);
		}
		iContactsController.saveContacts(contactsVos);
	}


	/**
	 * 保存数据
	 *
	 * @param mMGroupList
	 * @param entity
	 */
	private void saveGroups(List<MGroup> mMGroupList, UnReadMemoryNum entity) {
		//我的-补充
		mMGroupList.get(0).setUnReadMPointAddCnt(entity.getUnReadMPointAddCntM());
		//他的-补充&新增
		mMGroupList.get(1).setUnReadMPointAddCnt(entity.getUnReadMPointAddCntF());
		mMGroupList.get(1).setUnReadMemoryCnt(entity.getUnReadMemoryCntF());

		//遍历群
		MGroup mGroup;
		UnReadMemoryNum unReadMemoryNum;
		int size = mMGroupList.size();
		int count = entity.getUnReadGroups().size();
		for (int i = 1; i < size; i++) {
			//自己的群
			mGroup = mMGroupList.get(i);
			mGroup.setUnReadMPointAddCnt("0");
			mGroup.setUnReadMemoryCnt("0");
			for (int j = 0; j < count; j++) {
				//获取的数据
				unReadMemoryNum = entity.getUnReadGroups().get(j);
				if (mGroup.getGroupId().equals(unReadMemoryNum.getGroupId())) {
					//Id相同
					mGroup.setUnReadMPointAddCnt(unReadMemoryNum.getUnReadMPointAddCntG());
					mGroup.setUnReadMemoryCnt(unReadMemoryNum.getUnReadMemoryCntG());
					break;
				} else {
					continue;
				}
			}
		}
		iCircleController.saveCircle(mMGroupList);
		getCircle(MainApplication.getUserId());
	}


	/**
	 * 修改圈子名
	 *
	 * @param respVo
	 */
	public void upGroup(String userId, SG04RespVo respVo) {
		if (respVo.getCode() != 0) return;
		MGroup mGroup = iCircleController.getGroup(respVo.getGroupId());
		if (mGroup != null) {
			mGroup.setGroupName(respVo.getGroupName());
			iCircleController.upCirlce(mGroup);
		}
		//刷新
		getCircle(userId);
	}


	/**
	 * 被移出圈子
	 *
	 * @param userId
	 * @param respVo
	 */
	public void removeGroup(String userId, SG03RespVo respVo) {
		if (respVo.getCode() != 0) return;
		iCircleController.delCirlceById(respVo.getGroupId());
		//刷新
		getCircle(userId);
	}


	/**
	 * 创建圈子
	 */
	public void addMGroup(String userId, SG01RespVo respVo) {
		int code = respVo.getCode();
		if (code != 0) return;
		MGroup entity = new MGroup();
		User user = iLoginController.getUser(userId);
		entity.setGroupName(respVo.getGroupName());
		entity.setUserId(userId);
		entity.setGroupId(respVo.getGroupId());
		entity.setGroupCount(String.valueOf(respVo.getGroupCount()));
		entity.setAdminUserId(respVo.getAdminUserId());
		entity.setMemoryCnt(String.valueOf(respVo.getMemoryCnt()));
		entity.setFreeze(respVo.getFreeze());
		entity.setActiveFlg("1");
		entity.setType(2);
		entity.setUserName(respVo.getAdminUserName());
		entity.setAdminUserName(respVo.getAdminUserName());
		entity.setTitle("暂无记忆");
		entity.setUpdateDateForShow(respVo.getUpdateDateForShow());
		entity.setComeFrom(userId);
		//设置头像
		List<String> picList = respVo.getHeadPhotos();
		if (picList != null && !picList.isEmpty()) {
			if (picList.size() == 1) {
				entity.setHeadPhoto1(picList.get(0));
			}
			if (picList.size() == 2) {
				entity.setHeadPhoto2(picList.get(1));
			}
			if (picList.size() == 3) {
				entity.setHeadPhoto3(picList.get(2));
			}
			if (picList.size() == 4) {
				entity.setHeadPhoto4(picList.get(3));
			}
		}
		//保存
		iCircleController.saveCircle(entity);
		//刷新
		getCircle(userId);
	}

	/**
	 * 删除记忆
	 *
	 * @param userId        记忆发布人
	 * @param memoryId      记忆ID
	 * @param memoryPointId 记忆片断ID
	 * @param addUserId     记忆片断补充人ID
	 * @param groupId       圈子ID
	 */
	public void removeMemory(String userId, String memoryId, String memoryPointId, String addUserId, String groupId) {
		//构建
		MemoryDelVo msgRequest = new MemoryDelVo();
		msgRequest.setUserId(userId);
		msgRequest.setMemoryId(memoryId);
		msgRequest.setMemoryPointId(memoryPointId);
		msgRequest.setGroupId(groupId);

		MemoryDelVo memoryDelVo = new MemoryDelVo();
		memoryDelVo.setMemoryDelVo(msgRequest);
		memoryDelVo.setType(BusynessType.CX01.getIndex());

//		iMinaController.sendMsg(context, memoryDelVo, true, new SimpleCallback() {
//			@Override
//			public void onCallback(Object data) {
//				int code = (int) data;
//				if (code == 0) {
//					CLog.e(TAG, "数据已成功发出！");
//					if (mView != null) {
//					}
//				} else {
//					CLog.e(TAG, "数据发送失败。错误码是：" + code + "！");
//				}
//			}
//
//			@Override
//			public void onNoNetCallback() {
//				//无网络状态
//			}
//		});
	}

	/**
	 * 未读消息-评论
	 *
	 * @param reqVo
	 */
	public void unReadComment(SA10ReqVo reqVo, String userId) {
		//我的
		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		if (mGroup != null) {
			int count = Integer.parseInt(mGroup.getTotalPCnt());
			mGroup.setTotalPCnt(String.valueOf(++count));
			iCircleController.upCirlce(mGroup);
			getCirclesFromDb(userId);
		}
	}

	/**
	 * 未读消息-点赞
	 *
	 * @param reqVo
	 */
	public void unReadMsg(SA01ReqVo reqVo, String userId) {
		//我的
		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		if (mGroup != null) {
			int count = Integer.parseInt(mGroup.getTotalPCnt());
			mGroup.setTotalPCnt(String.valueOf(++count));
			iCircleController.upCirlce(mGroup);
			getCirclesFromDb(userId);
		}
	}


	/**
	 * 更新圈子数据
	 */
	public void upGroupInfo(SW01RespVo reqVo, String userId) {
		String type = reqVo.getMessageType();
		MGroup mGroup = null;
		if ("1".equals(type)) {
			//我的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		} else if ("2".equals(type)) {
			//他的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		} else {
			//群
			mGroup = (MGroup) iGroupController.getGroupByKey(reqVo.getGroupId(), MainApplication.getUserId());
		}

		//总记忆数
		int totalCount = 0;
		if (!TextUtils.isEmpty(mGroup.getMemoryCnt())) {
			totalCount = Integer.parseInt(mGroup.getMemoryCnt());
		}
		//总新增记忆数
		int totalAddCount = 0;
		if (!TextUtils.isEmpty(mGroup.getUnReadMemoryCnt())) {
			totalAddCount = Integer.parseInt(mGroup.getUnReadMemoryCnt());
		}

		//记忆总数
		mGroup.setMemoryCnt(String.valueOf(++totalCount));

		//数据+1
		if (!MainApplication.getUserId().equals(reqVo.getMuid())) {
			//不是我才会有新增
			if (!"1".equals(type))
				mGroup.setUnReadMemoryCnt(String.valueOf(++totalAddCount));
		}

		mGroup.setTitle(reqVo.getMessageDetail());
		mGroup.setUpdateDateForShow(reqVo.getDateYmd());

		//更新
		iGroupController.upMGroup(mGroup);
		getCirclesFromDb(userId);
	}

	/**
	 * 更新圈子(转发)
	 */
	public void upForward(SW02RespVo reqVo, String userId) {
		String type = reqVo.getMessageType();
		MGroup mGroup = null;
		if ("1".equals(type)) {
			//我的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		} else if ("2".equals(type)) {
			//他的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		} else {
			//群
			mGroup = (MGroup) iGroupController.getGroupByKey(reqVo.getGroupId(), MainApplication.getUserId());
		}

		if (TextUtils.isEmpty(reqVo.getMuid())) return;

		//未读记忆数(新增)
		int totalCount = 0;
		if (!TextUtils.isEmpty(mGroup.getUnReadMemoryCnt())) {
			totalCount = Integer.parseInt(mGroup.getUnReadMemoryCnt());
		}
		//我自己不加新增数
		if (!reqVo.getMuid().equals(userId)) {
			//数据+1
			mGroup.setUnReadMemoryCnt(String.valueOf(++totalCount));
		}

		//记忆总数据
		int sumCount = 0;
		if (!TextUtils.isEmpty(mGroup.getMemoryCnt())) {
			sumCount = Integer.parseInt(mGroup.getMemoryCnt());
		}
		//记忆总数+1
		mGroup.setMemoryCnt(String.valueOf(++sumCount));

		//设置内容
		mGroup.setTitle(reqVo.getMessageDetail());
		//设置更新时间
		mGroup.setUpdateDateForShow(reqVo.getDateYmd());

		//更新
		iGroupController.upMGroup(mGroup);
		getCirclesFromDb(userId);
	}


	/**
	 * 更新圈子数据
	 */

	public void upGroupAddMemory(SA20RespVo reqVo, String userId) {
		String type = reqVo.getMessageType();
		MGroup mGroup = null;
		if ("1".equals(type)) {
			//我的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		} else {
			//群
			mGroup = (MGroup) iGroupController.getGroupByKey(reqVo.getGroupId(), MainApplication.getUserId());
		}
		//计算总数据
		int count = 0;
		if (!TextUtils.isEmpty(mGroup.getUnReadMPointAddCnt())) {
			count = Integer.parseInt(mGroup.getUnReadMPointAddCnt());
		}
		//不是自己发布的--> 数据+1
//		if (!TextUtils.isEmpty(reqVo.getMuid()))
//			if (!reqVo.getMuid().equals(MainApplication.getUserId()))
		mGroup.setUnReadMPointAddCnt(String.valueOf(++count));

		//更新
		iGroupController.upMGroup(mGroup);
		getCirclesFromDb(userId);
	}


	/**
	 * 获取用户
	 *
	 * @param key
	 */
	public User getUser(String key) {
		return iLoginController.getUser(key);
	}

	/**
	 * 获取db中的圈子
	 */
	public void getCircle(String userId) {
		User user = iLoginController.getUser(userId);
		if (user.getImportgroup() == 0) {
			//没有导入过,从http请求
			if (mView != null) {
				mView.reqCircle();
			}
		} else {
			//导入过,从db请求
			getCirclesFromDb(userId);
		}
	}

	/**
	 * Db查询
	 *
	 * @param userId
	 */
	public void getCirclesFromDb(String userId) {
		//圈子
		List<MGroup> mGroupList = iCircleController.getGroups(userId);
		if (mGroupList != null && !mGroupList.isEmpty()) {
			convertGroup(mGroupList);
			if (mView != null) {
				mView.showSuccess();
				mView.setAdapter(mGroupList);
			}
		}
	}

	/**
	 * 激活圈子
	 *
	 * @param url
	 */
	public void reqStartCircle(String url, final String userToken, final String userId, final String groupId, final int position) {
		List<String> groupIdList = new ArrayList<>();
		groupIdList.add(groupId);
		GroupAdDto dto = new GroupAdDto();
		dto.setUserToken(userToken);
		dto.setGroupIdList(groupIdList);
		mView.showLoadingDialog();

		//请求圈子列表数据
		iCircleController.reqCircle(url, dto, new SimpleCallback() {
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
					MGroup entity = (MGroup) data;
					if (entity.getStatus() == 0) {
						//存在数据,进行下步操作
						MGroup mGroup = iCircleController.getGroup(groupId);
						mGroup.setActiveFlg("0");
						iCircleController.upCirlce(mGroup);
						if (mView != null) {
//							mView.reqStartCirlce(position);
							getCircle(userId);
							mView.showSuccess();
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
	 * 获取本地的数据
	 */
	public void getGrouptFromDb(final String userId) {
		User user = iLoginController.getUser(userId);
		String pUrl = user.getHeadPhoto();
		//广告图
		List<Advert> advertList = iAdvertController.getAdvertFromDb();
		Advert advert;
		if (advertList == null || advertList.isEmpty()) {
			//没有广告->创建个假的
			advert = new Advert();
			advert.setLinkUrl(context.getString(R.string.FSMANAGER));
		} else {
			advert = advertList.get(0);
		}
		//圈子
		List<MGroup> mGroupList = iCircleController.getGroups(userId);
		if (mGroupList == null || mGroupList.isEmpty()) {
			//为空->创建去
			MemoryDto memoryDto = new MemoryDto();
			memoryDto.setTitleM("暂无记忆");
			memoryDto.setTitleF("暂无记忆");
			memoryDto.setUpdateDateMForShow(DateUtil.getCurrentDateLine());
			memoryDto.setUpdateDateFForShow(DateUtil.getCurrentDateLine());
			memoryDto.setMemoryCntM(0);
			memoryDto.setMemoryCntF(0);
			memoryDto.setUnReadCntF(0);
			memoryDto.setUnReadCntM(0);
			memoryDto.setUnReadMemoryCntF(0);
			memoryDto.setUnReadMPointAddCntF(0);
			memoryDto.setTotalPCnt(0);
			memoryDto.setFriendCntF(0);
			//我的头像
			memoryDto.setHeadPhotoM(pUrl);
			//他的头像
			if (!TextUtils.isEmpty(pUrl)) {
				List<String> uPics = new ArrayList<>();
				uPics.add(pUrl);
				memoryDto.setHeadPhotosF(uPics);
			} else {
				memoryDto.setHeadPhotosF(null);
			}
			//装换
			List<MGroup> mGroups = addGroup(memoryDto, userId);
			//保存
			iCircleController.saveCircle(mGroups);
			mGroupList.clear();
			mGroupList.addAll(mGroups);
		} else {
			//有数据
		}
		//展示去-->请求网络数据
		if (mView != null) {
			mView.setAdapter(advert, mGroupList);
			mView.reqAdvert();
		}
	}

	/**
	 * 获取广告数据
	 */
	public void reqAdvert(String url) {
		iAdvertController.reqAdvert(url, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
				}
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
				} else {
					Advert entity = (Advert) data;
					if (entity.getStatus() == 0) {
						if (entity.getAdvertizes() != null && !entity.getAdvertizes().isEmpty()) {
							if (mView != null) {
								iAdvertController.saveAdvert(entity.getAdvertizes().get(0));
								mView.setAdvert(entity.getAdvertizes().get(0));
							}
						}
					}
				}
			}
		});
	}


	/**
	 * 获取网络上的圈子数据
	 *
	 * @param url
	 */
	public void reqCircleInfos(String url, final String userId) {
		//请求圈子列表数据
		iCircleController.reqCircle(url, userId, new SimpleCallback() {
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
					MemoryDto respVo = (MemoryDto) data;
					if (respVo.getStatus() == 0) {
						//成功
						//存在数据,进行下步操作
						iCircleController.removeAll();
						iCircleController.saveCircle(addGroup(respVo, userId));
						iLoginController.updateUser(userId, "importgroup", "1");
						getCirclesFromDb(userId);
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(respVo.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 * 圈子图片转换
	 *
	 * @param mGroupList
	 */
	private void convertGroup(List<MGroup> mGroupList) {
		List<String> headPhotos = null;
		for (MGroup mGroup : mGroupList) {
			headPhotos = new ArrayList<>();
			if (mGroup.getType() == 0) {
				//我的
				if (!TextUtils.isEmpty(mGroup.getHeadPhoto1())) {
					headPhotos.add(mGroup.getHeadPhoto1());
				} else {
					headPhotos.add("");
				}
			} else {
				if (!TextUtils.isEmpty(mGroup.getHeadPhoto1())) {
					headPhotos.add(mGroup.getHeadPhoto1());
				}
				if (!TextUtils.isEmpty(mGroup.getHeadPhoto2())) {
					headPhotos.add(mGroup.getHeadPhoto2());
				}
				if (!TextUtils.isEmpty(mGroup.getHeadPhoto3())) {
					headPhotos.add(mGroup.getHeadPhoto3());
				}
				if (!TextUtils.isEmpty(mGroup.getHeadPhoto4())) {
					headPhotos.add(mGroup.getHeadPhoto4());
				}

				if (headPhotos.size() != 4) {
					int size = 4 - headPhotos.size();
					for (int i = 0; i < size; i++) {
						headPhotos.add("");
					}
				}
			}
			mGroup.setHeadPhotos(headPhotos);
		}
	}


	/**
	 * 数据转换
	 *
	 * @param entity
	 */
	private List<MGroup> addGroup(MemoryDto entity, String userId) {
		List<MGroup> mGroups = entity.getGroups();
		if (mGroups == null) mGroups = new ArrayList<>();
		for (MGroup group : mGroups) {
			group.setType(2);
			group.setComeFrom(userId);
			//头像
			if (group.getHeadPhotos() != null) {
				if (group.getHeadPhotos().size() > 0)
					group.setHeadPhoto1(group.getHeadPhotos().get(0));
				if (group.getHeadPhotos().size() > 1)
					group.setHeadPhoto2(group.getHeadPhotos().get(1));
				if (group.getHeadPhotos().size() > 2)
					group.setHeadPhoto3(group.getHeadPhotos().get(2));
				if (group.getHeadPhotos().size() > 3)
					group.setHeadPhoto4(group.getHeadPhotos().get(3));
			}
		}
		//我的记忆
		MGroup myMGroup = new MGroup();
		myMGroup.setGroupName("我的记忆");
		myMGroup.setGroupId(userId);
		myMGroup.setTitle(entity.getTitleM());
		myMGroup.setUpdateDateForShow(entity.getUpdateDateMForShow());
		myMGroup.setActiveFlg("0");
		myMGroup.setMemoryCnt(entity.getMemoryCntM() + "");
		myMGroup.setUnReadMPointAddCnt(entity.getUnReadCntM() + "");
		myMGroup.setGroupCount("0");
		myMGroup.setType(0);
		myMGroup.setTotalPCnt(String.valueOf(entity.getTotalPCnt()));
		myMGroup.setComeFrom(userId);
		myMGroup.setHeadPhoto1(entity.getHeadPhotoM());//头像

		//他的记忆
//		MGroup otherMGroup = new MGroup();
//		otherMGroup.setGroupName("TA的记忆");
//		otherMGroup.setTitle(entity.getTitleF());
//		otherMGroup.setGroupId(userId + "a");
//		otherMGroup.setUpdateDateForShow(entity.getUpdateDateFForShow());
//		otherMGroup.setMemoryCnt(entity.getMemoryCntF() + "");
//		otherMGroup.setUnReadCnt(entity.getUnReadCntF() + "");
//		otherMGroup.setGroupCount(entity.getFriendCntF() + "");
//		otherMGroup.setUnReadMemoryCnt(entity.getUnReadMemoryCntF() + "");
//		otherMGroup.setUnReadMPointAddCnt(entity.getUnReadMPointAddCntF() + "");
//		otherMGroup.setType(1);
//		otherMGroup.setActiveFlg("0");
//		otherMGroup.setComeFrom(userId);
//
//
//		//头像
//		if (entity.getHeadPhotosF() != null) {
//			if (entity.getHeadPhotosF().size() > 0)
//				otherMGroup.setHeadPhoto1(entity.getHeadPhotosF().get(0));
//			if (entity.getHeadPhotosF().size() > 1)
//				otherMGroup.setHeadPhoto2(entity.getHeadPhotosF().get(1));
//			if (entity.getHeadPhotosF().size() > 2)
//				otherMGroup.setHeadPhoto3(entity.getHeadPhotosF().get(2));
//			if (entity.getHeadPhotosF().size() > 3)
//				otherMGroup.setHeadPhoto4(entity.getHeadPhotosF().get(3));
//		}

		//创建新编辑部
		MGroup newAddMgroup = new MGroup();
		newAddMgroup.setGroupName("");
		newAddMgroup.setTitle("创建新编辑部");
		newAddMgroup.setGroupId(userId + "b");
		newAddMgroup.setType(3);
		newAddMgroup.setActiveFlg("0");
		newAddMgroup.setComeFrom(userId);

		mGroups.add(0, myMGroup);
//		mGroups.add(1, otherMGroup);
		mGroups.add(newAddMgroup);
		return mGroups;
	}
}

