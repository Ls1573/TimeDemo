package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryComment;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.MemoryPraise;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.User;
import com.time.memory.model.CircleController;
import com.time.memory.model.CommentController;
import com.time.memory.model.ForkController;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.MemoryDayController;
import com.time.memory.model.MemoryInfoController;
import com.time.memory.model.ReportController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.ICommentController;
import com.time.memory.model.impl.IForkController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMemoryDetailController;
import com.time.memory.model.impl.IMemoryInfoController;
import com.time.memory.model.impl.IReportController;
import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.mt.vo.CommentVo;
import com.time.memory.mt.vo.MemoryDelVo;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DateUtil;
import com.time.memory.view.impl.IMemoryDetailMView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
public class MemoryInfosMPresenter extends BasePresenter<IMemoryDetailMView> {
	private static final String TAG = "MemoryPresenter";
	// m层
	private ICircleController iCircleController;
	private ILoginController iLoginController;
	private IMemoryDetailController iMemoryDayController;
	private IMemoryInfoController iMemoryInfoController;
	private IReportController iReportController;
	private IForkController iForkController;
	private ICommentController iCommentController;
	private IGroupController iGroupController;

	public MemoryInfosMPresenter() {
		iMemoryDayController = new MemoryDayController();
		iCircleController = new CircleController();
		iLoginController = new LoginController();
		iReportController = new ReportController();
		iMemoryInfoController = new MemoryInfoController();
		iForkController = new ForkController();
		iCommentController = new CommentController();
		iGroupController = new GroupController();
	}

	/**
	 * 追加记忆
	 */
	public void addMemoryPoints(TempMemory tempMemory, MemoryInfo memoryInfo, int position) {
		if (tempMemory != null) {
			String imgPath = context.getString(R.string.FSIMAGEPATH);
			//不为空,赋值
			User user = iLoginController.getUser(MainApplication.getUserId());
			MemoryEdit memory = new MemoryEdit();

			memory.setAddFlag("0");//是补充的
			memory.setInsDate(tempMemory.getInsDateForShow());//创建时间
			memory.setMpDateForShow(tempMemory.getUpdateForshowDate());//记忆时间
			memory.setMemoryId(tempMemory.getMemoryId());//记忆Id
			memory.setMemoryPointId(tempMemory.getMemoryPointId());//记忆片段Id
			memory.setMemoryPointIdSource(tempMemory.getMemoryPointId());//记忆片段Id源Id
			memory.setMemorySrcId(tempMemory.getMemoryPointId());//记忆片段Id源Id

			memory.setMpId(tempMemory.getMemoryPointId());//记忆片段Id
			memory.setUserId(tempMemory.getUserId());//补充人Id
			memory.setUname(tempMemory.getUserName());////补充人name
			memory.setPhotoCount(tempMemory.getPiccount());//图片数
			memory.setLocal(tempMemory.getLocal());//地址
			memory.setDetail(tempMemory.getDesc());//描述
			memory.setpCnt("0");//点赞数
			memory.setcCnt("0");//评论数
			memory.setMpFlag("1");//没有点赞
			//图片集
			ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
			if (!TextUtils.isEmpty(tempMemory.getPhoto1()))
				photoInfos.add(new PhotoInfo(imgPath + tempMemory.getPhoto1()));
			if (!TextUtils.isEmpty(tempMemory.getPhoto2()))
				photoInfos.add(new PhotoInfo(imgPath + tempMemory.getPhoto2()));
			if (!TextUtils.isEmpty(tempMemory.getPhoto3()))
				photoInfos.add(new PhotoInfo(imgPath + tempMemory.getPhoto3()));
			if (!TextUtils.isEmpty(tempMemory.getPhoto4()))
				photoInfos.add(new PhotoInfo(imgPath + tempMemory.getPhoto4()));
			if (!TextUtils.isEmpty(tempMemory.getPhoto5()))
				photoInfos.add(new PhotoInfo(imgPath + tempMemory.getPhoto5()));
			if (!TextUtils.isEmpty(tempMemory.getPhoto6()))
				photoInfos.add(new PhotoInfo(imgPath + tempMemory.getPhoto6()));
			//图片数据
			memory.setPhotoInfos(photoInfos);

			//增加一个片段
			if (memoryInfo.getMemoryPointVos() == null) {
				ArrayList<MemoryEdit> memoryEdits = new ArrayList<>();
				memoryInfo.getMemoryPointVos().addAll(memoryEdits);
			}
			//设置是否被分发
			if (!memoryInfo.getMemoryPointVos().isEmpty()) {
				memory.setDeliverCnt(memoryInfo.getMemoryPointVos().get(0).getDeliverCnt());
			}
			//加入
			memoryInfo.getMemoryPointVos().add(memory);
			//转换(补充记忆)
			convertMemoryPoints(memoryInfo.getMemoryPointVos(), memoryInfo);
			//转换(图片信息)
			convertPhotoInfo(photoInfos.size(), memoryInfo, tempMemory);
			//设置图片
			if (mView != null) {
				mView.refreshMemory(position);
			}
		}
	}

	/**
	 * 刷新评论数据
	 *
	 * @param memoryComments
	 */
	public void refreComment(List<MemoryComment> memoryComments) {
		if (memoryComments.size() > 1)
			memoryComments.get(1).setIsFirst(false);
		memoryComments.get(0).setIsFirst(true);
		memoryComments.get(0).setSum(memoryComments.size());
	}

	/**
	 * 刷新移除后评论数据
	 */
	public void refreComment(List<MemoryInfo> memoryInfos, int memoryTag, int position, String pointId, String mCMemoryId, String mpSrcId) {
		List<MemoryComment> memoryComments = memoryInfos.get(memoryTag).getCommentorVos();
		memoryComments.get(0).setIsFirst(false);
		memoryComments.get(memoryComments.size() - 1).setIsLast(false);
		memoryComments.remove(position);
		if (memoryComments.size() > 0) {
			memoryComments.get(0).setIsFirst(true);
			memoryComments.get(0).setSum(memoryComments.size());
			memoryComments.get(memoryComments.size() - 1).setIsLast(true);
		}
		//评论主体
		if (TextUtils.isEmpty(mCMemoryId)) {
			return;
		}
		if (pointId.equals(mCMemoryId)) {
			return;
		}

		ArrayList<MemoryEdit> memoryEdits = memoryInfos.get(memoryTag).getMemoryPointVos();
		int size = memoryEdits.size();
		for (int i = 0; i < size; i++) {
			if (TextUtils.isEmpty(mpSrcId))
				mpSrcId = pointId;
			if (mpSrcId.equals(memoryEdits.get(i).getMpId())) {
				String count = memoryEdits.get(i).getcCnt();
				if (TextUtils.isEmpty(count)) {
					count = "0";
				} else if (count.equals("0")) {

				} else {
					int c = Integer.parseInt(count);
					c -= 1;
					count = String.valueOf(c < 0 ? 0 : c);
				}
				memoryEdits.get(i).setcCnt(count);
				break;
			}
		}

	}


	/**
	 * 发布评论
	 *
	 * @param userId              记忆发布人ID
	 * @param memoryId            记忆ID
	 * @param memoryPointId       记忆片断ID
	 * @param groupId             群ID
	 * @param commentUserId       评论发布人ID
	 * @param commentToUserId     评论接收人ID
	 * @param commentTitle        评论内容
	 * @param memoryIdSource      记忆Id的源Id
	 * @param memoryPointIdSource 记忆片段Id的源Id
	 * @param source              1:我的记忆;2:其他书中
	 */
	public void sendCommentMsg(String url, String token, String userId, String memoryId, String memoryPointId, String groupId,
							   final String commentUserId, String commentToUserId, String commentTitle, String headPic, String userName, String touserName,
							   boolean isToUser, String memoryIdSource, String memoryPointIdSource, String source) {
		//回复内容
		if (TextUtils.isEmpty(commentTitle)) {
			//评论为空
			if (mView != null) {
				mView.showShortToast("请输入评论内容");
			}
			return;
		}
		//回复给用户
		if (isToUser) {
		} else {
			//回复主体
			commentToUserId = "";
		}
		//评论主体
		CommentVo commentVo = new CommentVo();
		commentVo.setUserId(userId);
		commentVo.setMemoryId(memoryId);
		commentVo.setMemoryPointId(memoryPointId);
		commentVo.setGroupId(groupId);
		commentVo.setCommentUserId(commentUserId);
		commentVo.setCommentUserName(userName);
		commentVo.setCommentUserHead(headPic);
		commentVo.setCommentToUserId(commentToUserId);
		commentVo.setCommentToUserName(touserName);
		commentVo.setCommentTitle(commentTitle);
		commentVo.setMemoryIdSource(memoryIdSource);
		commentVo.setMemoryPointIdSource(memoryPointIdSource);
		commentVo.setSource(source);

		//新的评论数据
		final MemoryComment memoryComment = createComment(commentVo);
		if (mView != null)
			mView.showLoadingDialog();
		iCommentController.reqComment(url, token, commentVo, new SimpleCallback() {
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
					MemoryComment entity = (MemoryComment) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							memoryComment.setcId(entity.getUuid());
							memoryComment.setuIdC(commentUserId);
							mView.refreshComment(memoryComment);
							mView.showSuccess();
							mView.sendCommentSuccess(true);
						}
					} else {
						//失败
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
	 * 删除评论
	 *
	 * @param uuid           评论UUID
	 * @param memoryId       记忆ID
	 * @param groupId        圈子ID 我的记忆和TA的记忆，请传空串
	 * @param commentUserId  评论发布人Id
	 * @param userId         当前用户(炒作删除评论用户的ID)
	 * @param source         1:我的记忆;2:书的记忆
	 * @param memoryIdSource 记忆Id的源Id
	 * @param mPonitId       评论记忆片段Id
	 * @param mCMemoryId     评论记忆Id
	 */
	public void removeComment(String url, String token, String uuid, String memoryId, String groupId, final int position, final int memoryTag,
							  String commentUserId, String userId, String source, String memoryIdSource,
							  final String mPonitId, final String mCMemoryId, final String mpSrcId
	) {
		if (mView != null)
			mView.showLoadingDialog();
		//构建
		final CX02RespVo msgRequest = new CX02RespVo();
		msgRequest.setType(BusynessType.CX02.getIndex());
		msgRequest.setGroupId(TextUtils.isEmpty(groupId) ? "" : groupId);
		msgRequest.setMemoryId(memoryId);
		msgRequest.setUuid(uuid);
		msgRequest.setUserToken(token);
		msgRequest.setCommentUserId(commentUserId);
		msgRequest.setUserId(userId);
		msgRequest.setSource(source);
		msgRequest.setMemoryIdSource(memoryIdSource);

		iCommentController.reqRemoveComment(url, msgRequest, new SimpleCallback() {
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
					}
				} else {
					MemoryComment entity = (MemoryComment) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.removeComment(position, memoryTag, mPonitId, mCMemoryId, mpSrcId);
							mView.showSuccess();
						}
					} else {
						//失败
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
	 * 获取评论图片对应的片段位置
	 */
	public void getCommentPosition(final int position, final MemoryInfo memoryInfo) {
		//获得片段Id
		String pointId = memoryInfo.getCommentorVos().get(position).getpId();
		ArrayList<MemoryEdit> memoryEdits = memoryInfo.getMemoryPointVos();
		int index = 0;

		for (int i = 0; i < memoryEdits.size(); i++) {
			if (pointId.equals(memoryEdits.get(i).getMpId())) {
				index = i;
				break;
			}
		}

		if (mView != null) {
			mView.startPoint(index, memoryInfo);
		}
	}


	/**
	 * 获取图片集合
	 *
	 * @param memoryEdits-段落集合
	 * @param position         -第几个段子
	 * @param index            -段落下第几个图片
	 * @return
	 */
	public void getPhotos(ArrayList<MemoryEdit> memoryEdits, int position, int index) {
		ArrayList<PhotoInfo> photoInfos = new ArrayList<>();
		//所有图片集合
//		for (MemoryEdit memoryEdit : memoryEdits) {
//			photoInfos.addAll(memoryEdit.getPhotoInfos());
//		}

		//遍历所有片段
		int size = memoryEdits.size();
		for (int i = 0; i < size; i++) {
			int pSize = memoryEdits.get(i).getPhotoInfos().size();
			for (int j = 0; j < pSize; j++) {
				//每个片段下的图片
				//设置下标
				memoryEdits.get(i).getPhotoInfos().get(j).setCurPointIndex(j + 1);
				//设置对应片段
				memoryEdits.get(i).getPhotoInfos().get(j).setCurPoint(String.valueOf(i + 1));
				//设置图片每个片段下的图片总数
				memoryEdits.get(i).getPhotoInfos().get(j).setTotalCount(pSize);

			}
			//加入图片集
			photoInfos.addAll(memoryEdits.get(i).getPhotoInfos());
		}

		//当前图片位置
		int curIndex = index;

		for (int i = 0; i < position; i++) {
			if (memoryEdits.get(i).getPhotoInfos() != null)
				curIndex += memoryEdits.get(i).getPhotoInfos().size();
		}
		if (mView != null) {
			mView.onPhotoPreivew(photoInfos, curIndex);
		}
	}

	/**
	 * 获取信息
	 *
	 * @param
	 * @reurn
	 */
	public void getMemoryInfos(String url, final String memoryId, String memorySrcId, String type, final int state, final int position, final boolean isNew, final int praiseCount, final MemoryInfo memoryInfo) {
		if (mView != null)
			mView.showLoadingDialog();

		iMemoryInfoController.reqMemoryInfos(url, memoryId, TextUtils.isEmpty(memorySrcId) ? memoryId : memorySrcId, type, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					MemoryInfo entity = (MemoryInfo) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							//统一数据
							convertMemoryPoints(entity);
							//转换数据(图片)
							convertMemorys(entity.getMemoryPointVos(), memoryId, state);
							//转换数据(评论)
							convertConmment(entity.getCommentorVos());
							//赋值点赞数
							convertPraise(entity, praiseCount, memoryInfo);
							//排序
							if (state == 2)
								Collections.sort(entity.getMemoryPointVos(), new ConstactComparator());
							//转换(补充记忆)
							convertMemoryPoints(entity.getMemoryPointVos(), entity);
							//装换图片数据
							mView.setMyInfoAdapter(entity, position, isNew);
							mView.showSuccess();
						}
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(entity.getMessage());
							//TODO 删除
							mView.removeMemory(position, state);
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 记忆点赞请求
	 *
	 * @param memoryUserId  记忆发布人ID
	 * @param praiseUserId  点赞人ID
	 * @param memoryId      记忆ID
	 * @param memoryPointId 记忆片断ID
	 * @param groupId       圈子ID
	 * @param delFlg        点赞/取消
	 */
	public void reqFork(String url, String token, String memoryUserId, String praiseUserId, String memoryId, String memoryPointId, String groupId, final String delFlg, final int memoryTag, String memoryIdSource, String memoryPointIdSource) {
		if (mView != null)
			mView.showLoadingDialog();
		final String flag = delFlg.equals("0") ? "1" : "0";

		iForkController.reqFork(url, token, memoryUserId, praiseUserId, memoryId, memoryPointId, groupId, flag, memoryIdSource, memoryPointIdSource, new SimpleCallback() {
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
					MemoryPraise entity = (MemoryPraise) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.showSuccess();
							mView.sendForkSuccess(flag, true, memoryTag);
						}
					} else {
						//失败
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
	 * 片段点赞请求
	 *
	 * @param memoryUserId        记忆发布人ID
	 * @param praiseUserId        点赞人ID
	 * @param memoryId            记忆ID
	 * @param memoryPointId       记忆片断ID
	 * @param groupId             圈子ID
	 * @param delFlg              点赞/取消
	 * @param memoryIdSource      记忆ID的源Id
	 * @param memoryPointIdSource 记忆片段ID的源Id
	 */
	public void reqPointFork(String url, String token, String memoryUserId, String praiseUserId, String memoryId, String memoryPointId, String groupId, String delFlg, final int position, final int memoryTag, String memoryIdSource, String memoryPointIdSource) {
		final String flag = delFlg.equals("0") ? "1" : "0";
		if (mView != null) {
			mView.showLoadingDialog();
		}
		//构建
		iForkController.reqFork(url, token, memoryUserId, praiseUserId, memoryId, memoryPointId, groupId, flag, memoryIdSource, memoryPointIdSource, new SimpleCallback() {
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
					MemoryPraise entity = (MemoryPraise) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.showSuccess();
							mView.sendPointForkSuccess(flag, true, position, memoryTag);
						}
					} else {
						//失败
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
	 * 设置添加移除联系人
	 */
	public void setForker(User user, String flag, MemoryInfo memoryinfo, Memory memory, int memoryTag) {
		//根据flag状态区分
		if (flag.equals("0")) {
			//当前是点赞
			isFork(user, memoryinfo.getPraiserVos(), memoryTag);
		} else {
			//取消点赞
			unFork(user, memoryinfo.getMemoryPointVos(), memoryinfo.getPraiserVos(), memory, memoryTag);
		}
	}


	/**
	 * 当前是点赞
	 *
	 * @param user
	 * @param praiseVos
	 */
	private void isFork(User user, List<MemoryPraise> praiseVos, int memoryTag) {
		String userId = user.getUserId();
		boolean isHas = false;
		for (int i = 0; i < praiseVos.size(); i++) {
			if (praiseVos.get(i).getpUsrId().equals(userId)) {
				isHas = true;
				break;
			}
		}
		//存在数据不刷新
		if (isHas) {
		} else {
			//没有就添加
			praiseVos.add(new MemoryPraise(userId, user.getUserName(), user.getHeadPhoto()));
		}
		if (mView != null) {
			mView.refreshPraiseAdapter(memoryTag);
		}
	}

	/**
	 * 当前是取消点赞
	 *
	 * @param user
	 * @param praiseVos
	 * @param memory
	 */
	private void unFork(User user, List<MemoryEdit> praiseVos, List<MemoryPraise> praisess, Memory memory, int memoryTag) {
		int count = 0;
		int index = -1;
		String usreId = MainApplication.getUserId();
		//记忆详情
		if (memory.getMpFlag().equals("0")) {
			count++;
		}
		for (MemoryEdit memoryEdit : praiseVos) {
			if (memoryEdit.getMpFlag().equals("0")) {
				count++;
			}
		}
		//如果详情被点击
		if (count == 0) {
			//删除
			for (int i = 0; i < praisess.size(); i++) {
				if (praisess.get(i).getpUsrId().equals(usreId)) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				praisess.remove(index);
			}
		}
		if (mView != null) {
			mView.refreshPraiseAdapter(memoryTag);
		}
	}


	/**
	 * 举报评论用户
	 *
	 * @param url
	 */
	public void reqReport(String url, String memoryId, String memoryPointId, String commentId, String complainType, String complainDetailType) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iReportController.reqReport(url, null, memoryId, memoryPointId, commentId, complainType, complainDetailType, new SimpleCallback() {
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
						//成功
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("举报成功");
						}
					} else {
						//失败
						if (mView != null) {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
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
	public void removeMemory(String url, String token, final String userId, String memoryId, String memoryPointId, String addUserId,
							 final String groupId, final int position, final int state, final String type,
							 String mpFlg, String source, String memoryIdSource, String memoryPonitIdSource

	) {
		//构建
		MemoryDelVo msgRequest = new MemoryDelVo();
		msgRequest.setUserId(userId);
		msgRequest.setMemoryId(memoryId);
		msgRequest.setMemoryPointId(memoryPointId);
		msgRequest.setGroupId(groupId);

		msgRequest.setMpFlg(mpFlg);
		msgRequest.setSource(source);
		msgRequest.setMemoryIdSource(memoryIdSource);
		msgRequest.setMemoryPointId(memoryPonitIdSource);

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
//							upGroupInfo(userId, groupId, state, type);
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

	/**
	 * 更新圈子数据
	 */
	public void upGroupInfo(String userId, String groupId, int state, String type) {
		MGroup mGroup = null;
		//我的  0：公开，1：私密 2：圈子
		MGroup uGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		if (state == 1) {
			//仅是我的->但可能是发布在他||圈子中
			if (type.equals("0")) {
				//他的
				mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
			} else if (type.equals("2")) {
				//群的
				mGroup = (MGroup) iGroupController.getGroupByKey(groupId, MainApplication.getUserId());
			}
		} else if (state == 0) {
			//还有他的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		} else {
			//还有群
			mGroup = (MGroup) iGroupController.getGroupByKey(groupId, MainApplication.getUserId());
		}

		//(他的||群的)总记忆数
		if (mGroup != null) {
			int totalCount = 0;
			if (!TextUtils.isEmpty(mGroup.getMemoryCnt())) {
				totalCount = Integer.parseInt(mGroup.getMemoryCnt());
			}
			//(他的||群的)数据--1
			mGroup.setMemoryCnt(String.valueOf(totalCount >= 1 ? --totalCount : 0));
			mGroup.setTitle("暂无新记忆");
			//更新
			iGroupController.upMGroup(mGroup);
		}
		//(我的)总记忆数
		int totalMyCount = 0;
		if (!TextUtils.isEmpty(uGroup.getMemoryCnt())) {
			totalMyCount = Integer.parseInt(uGroup.getMemoryCnt());
		}
		//(我的)数据--1
		uGroup.setMemoryCnt(String.valueOf(totalMyCount >= 1 ? --totalMyCount : 0));
		uGroup.setTitle("暂无新记忆");
		//更新
		iGroupController.upMGroup(uGroup);
	}

	/**
	 * 获取用户
	 *
	 * @param key
	 */
	public User getUser(String key) {
		return iLoginController.getUser(key);
	}

	public void getMessage(int state, String type, String lableId, String groupId, int curPage, int pageCount) {
		if (state == 1) {
			//我的
			getMemorys(context.getString(R.string.FSMYMEMORYS), type, lableId, "", curPage, pageCount);
		} else if (state == 0) {
			//他的
			getOtherMemorys(context.getString(R.string.FSOTHERMEMORYS), type, lableId, "", curPage, pageCount);
		} else {
			//群的
			getGroupMemorys(context.getString(R.string.FSGROUPMEMORYS), type, lableId, groupId, curPage, pageCount);
		}
	}

	/**
	 * 获取我的记忆
	 *
	 * @return
	 */
	private void getMemorys(String url, String searchType, String lableId, String groupId, int curPage, final int pageCount) {
//		if (mView != null)
//			mView.showLoadingDialog();
		iMemoryDayController.getMemoryAll(url, searchType, lableId, groupId, curPage, pageCount, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					Memory entity = (Memory) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
								//不为空
								mView.setMyAdapter(convertPhotoCount(entity.getMemoryList()));
								if (entity.getMemoryList().size() < pageCount) {
									mView.showFaild();
								}
							} else {
								mView.showShortToast("加载完成");
								mView.showFaild();
							}
						}
					} else {
						//失败
						if (mView != null) {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 获取TA的记忆
	 *
	 * @return
	 */
	private void getOtherMemorys(String url, String searchType, String lableId, String groupId, int curPage, final int pageCount) {
		if (mView != null)
			mView.showLoadingDialog();
		iMemoryDayController.getOtherMemoryAll(url, searchType, lableId, groupId, curPage, pageCount, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					OtherMemory entity = (OtherMemory) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
								//不为空
								mView.setOtherAdapter(convertOPhotoCount(entity.getMemoryList()));
								if (entity.getMemoryList().size() < pageCount) {
									mView.showFaild();
								} else {
									mView.showSuccess();
								}
							} else {
								mView.showShortToast("加载完成");
								mView.showFaild();
							}
						}
					} else {
						//失败
						if (mView != null) {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 获取群的记忆
	 *
	 * @return
	 */
	private void getGroupMemorys(String url, String searchType, String lableId, String groupId, int curPage, final int pageCount) {
		if (mView != null)
			mView.showLoadingDialog();
		iMemoryDayController.getGroupMemoryAll(url, searchType, lableId, groupId, curPage, pageCount, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					GroupMemory entity = (GroupMemory) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							if (entity.getMemoryList() != null && !entity.getMemoryList().isEmpty()) {
								//不为空
								mView.setGroupAdapter(convertGPhotoCount(entity.getMemoryList()));
								if (entity.getMemoryList().size() < pageCount) {
									mView.showFaild();
								} else {
									mView.showSuccess();
								}
							} else {
								mView.showShortToast("加载完成");
								mView.showFaild();
							}
						}
					} else {
						//失败
						if (mView != null) {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 切换记忆片段及追加片段
	 * {@link #addMemoryPoints}
	 */
	public void convertMemoryPoints(ArrayList<MemoryEdit> memoryEdits, MemoryInfo entity) {
		int index = -1;
		for (int i = 0; i < memoryEdits.size(); i++) {
			if (TextUtils.isEmpty(memoryEdits.get(i).getAddFlag())) {
				break;
			}
			//是补充的记忆
			if (memoryEdits.get(i).getAddFlag().equals("0")) {
				index = i;
				break;
			}
		}
		if (index != -1) {
			//追加记忆数
			int addMemorySize = memoryEdits.size() - index;
			memoryEdits.get(index).setSumAdd(String.valueOf(addMemorySize));
			entity.getMemory().setAddmemoryCount(addMemorySize);
		}
	}


	/**
	 * 整理图片信息
	 * {@link #addMemoryPoints}
	 */
	public void convertPhotoInfo(int total, MemoryInfo memoryInfo, TempMemory tempMemory) {
		Memory entity = memoryInfo.getMemory();
		//图片数
		int cCount = entity.getPhotoTotalCount();
		entity.setPhotoTotalCount(cCount + total);

		//图片信息
		if (TextUtils.isEmpty(entity.getPhoto1())) {
			//第一张图就没有
			entity.setPhoto1(tempMemory.getPhoto1());
			entity.setPhoto2(tempMemory.getPhoto2());
			entity.setPhoto3(tempMemory.getPhoto3());
		} else if (TextUtils.isEmpty(entity.getPhoto2())) {
			//第二张图没有
			entity.setPhoto2(tempMemory.getPhoto1());
			entity.setPhoto3(tempMemory.getPhoto2());
		} else if (TextUtils.isEmpty(entity.getPhoto3())) {
			//第三张图没有
			entity.setPhoto3(tempMemory.getPhoto1());
		}
	}


	/**
	 * 转换图片数(我的)
	 *
	 * @param memoryList
	 * @return
	 */
	public ArrayList<Memory> convertPhotoCount(ArrayList<Memory> memoryList) {
		if (memoryList == null) memoryList = new ArrayList<>();
		for (Memory entity : memoryList) {
			entity.setPhotoTotalCount(entity.getPhotoCount());
		}
		return memoryList;
	}

	/**
	 * 转换形态(我的)
	 *
	 * @param memoryList
	 * @return
	 */
	public void convertIsLoad(ArrayList<Memory> memoryList) {
		if (memoryList == null) memoryList = new ArrayList<>();
		for (Memory entity : memoryList) {
			entity.setIsLoaded(false);
		}
	}

	/**
	 * 转换形态(他的)
	 *
	 * @param memoryList
	 * @return
	 */
	public void convertOIsLoad(ArrayList<OtherMemory> memoryList) {
		if (memoryList == null) memoryList = new ArrayList<>();
		for (OtherMemory entity : memoryList) {
			entity.setIsLoaded(false);
		}
	}

	/**
	 * 转换图片数(他的)
	 *
	 * @param memoryList
	 * @return
	 */
	public ArrayList<OtherMemory> convertOPhotoCount(ArrayList<OtherMemory> memoryList) {
		if (memoryList == null) memoryList = new ArrayList<>();
		for (OtherMemory entity : memoryList) {
			entity.setPhotoTotalCount(entity.getPhotoCount());
		}
		return memoryList;
	}

	/**
	 * 转换形态(群的)
	 *
	 * @param memoryList
	 * @return
	 */
	public void convertGIsLoad(ArrayList<GroupMemory> memoryList) {
		if (memoryList == null) memoryList = new ArrayList<>();
		for (GroupMemory entity : memoryList) {
			entity.setIsLoaded(false);
		}
	}

	/**
	 * 转换图片数(群的)
	 *
	 * @param memoryList
	 * @return
	 */
	public ArrayList<GroupMemory> convertGPhotoCount(ArrayList<GroupMemory> memoryList) {
		if (memoryList == null) memoryList = new ArrayList<>();
		for (GroupMemory entity : memoryList) {
			entity.setPhotoTotalCount(entity.getPhotoCount());
		}
		return memoryList;
	}

	/**
	 * 信息加工下
	 *
	 * @return
	 */
	private List<Memory> convert(List<Memory> memorys) {
		String imagePath = context.getString(R.string.FSIMAGEPATH);
		User user = iLoginController.getUser(MainApplication.getUserId());
		for (Memory entity : memorys) {
			List<PhotoInfo> pEntitys = new ArrayList<>();
			//不为空
			if (!TextUtils.isEmpty(entity.getPhoto1())) {
				pEntitys.add(new PhotoInfo(imagePath + entity.getPhoto1()));
			}
			if (!TextUtils.isEmpty(entity.getPhoto2())) {
				pEntitys.add(new PhotoInfo(imagePath + entity.getPhoto2()));
			}
			if (!TextUtils.isEmpty(entity.getPhoto3())) {
				pEntitys.add(new PhotoInfo(imagePath + entity.getPhoto3()));
			}
			//设置图片
			entity.setPictureEntits(pEntitys);
			//设置头像地址
			entity.setNetPath(imagePath + user.getHeadPhoto());
		}
		return memorys;
	}


	/**
	 * 排序
	 */
	class ConstactComparator implements Comparator<MemoryEdit> {
		@Override
		public int compare(MemoryEdit lhs, MemoryEdit rhs) {
			String a = lhs.getAddFlag();
			String b = rhs.getAddFlag();
			return b.compareTo(a);
		}
	}


	/**
	 * 赋值
	 *
	 * @param memoryComments
	 */
	public void convertConmment(List<MemoryComment> memoryComments) {
		if (memoryComments != null && !memoryComments.isEmpty()) {
			memoryComments.get(0).setIsFirst(true);
			memoryComments.get(0).setSum(memoryComments.size());
			memoryComments.get(memoryComments.size() - 1).setIsLast(true);
			convertDate(memoryComments);
		}
	}

	/**
	 * 赋值点赞人数
	 */
	public void convertPraise(MemoryInfo entity, int praiseCount, MemoryInfo memoryInfo) {
		if (praiseCount != -1)
			entity.setPraise(praiseCount);
		else {
			//当前数
			int praise = memoryInfo.getPraise();
			int count = 0;
			//记录与自己相关的旧数据
			ArrayList<MemoryEdit> poList = memoryInfo.getMemoryPointVos();
			if (poList != null) {
				for (MemoryEdit edit : poList) {
					if (edit.getMpFlag().equals("0"))
						++count;
				}
			}
			if (memoryInfo.getMemory().getMpFlag().equals("0"))
				++count;
			//当前与自己无关的数
			count = praise - count;
			//新数据
			ArrayList<MemoryEdit> cList = entity.getMemoryPointVos();
			if (cList != null) {
				for (MemoryEdit edit : cList) {
					if (edit.getMpFlag().equals("0"))
						++count;
				}
			}
			if (entity.getMemory().getMpFlag().equals("0"))
				++count;

			entity.setPraise(count);
		}

	}

	/**
	 * 因为我的详情数据改变,现统一数据风格
	 * 1:本体记忆;0:追加记忆
	 */
	private void convertMemoryPoints(MemoryInfo entity) {
		if (entity.getMemoryPointVos() != null) {
			return;
		} else {
			ArrayList<MemoryEdit> list = new ArrayList<>();
			if (entity.getmPointInnerVos() != null) {
				for (MemoryEdit mm : entity.getmPointInnerVos()) {
					mm.setAddFlag("1");
				}
				list.addAll(entity.getmPointInnerVos());
			}
			if (entity.getmPointOuterVos() != null) {
				for (MemoryEdit mm : entity.getmPointOuterVos()) {
					mm.setAddFlag("0");
				}
				list.addAll(entity.getmPointOuterVos());
			}
			entity.setMemoryPointVos(list);
		}
	}

	/**
	 * 转换获取Photos对象
	 *
	 * @param memoryPointVos
	 */
	private void convertMemorys(ArrayList<MemoryEdit> memoryPointVos, String memoryId, int state) {
		ArrayList<PhotoInfo> pList = null;
		String imUrl = context.getString(R.string.FSIMAGEPATH);
		for (MemoryEdit entity : memoryPointVos) {
			pList = new ArrayList<>();
			if (!TextUtils.isEmpty(entity.getP1())) {
				pList.add(new PhotoInfo(imUrl + entity.getP1()));
			}
			if (!TextUtils.isEmpty(entity.getP2())) {
				pList.add(new PhotoInfo(imUrl + entity.getP2()));
			}
			if (!TextUtils.isEmpty(entity.getP3())) {
				pList.add(new PhotoInfo(imUrl + entity.getP3()));
			}
			if (!TextUtils.isEmpty(entity.getP4())) {
				pList.add(new PhotoInfo(imUrl + entity.getP4()));
			}
			if (!TextUtils.isEmpty(entity.getP5())) {
				pList.add(new PhotoInfo(imUrl + entity.getP5()));
			}
			if (!TextUtils.isEmpty(entity.getP6())) {
				pList.add(new PhotoInfo(imUrl + entity.getP6()));
			}
			entity.setPhotoInfos(pList);
			if (state == 1)
				entity.setNewDate(entity.getMpDateForShow());
			else if (state == 2)
				entity.setNewDate(entity.getInsdForShow());
			//TODO
			entity.setMemoryPointId(entity.getMpId());
			entity.setMemoryId(memoryId);
			entity.setUserId(entity.getuId());
		}
		if (!memoryPointVos.isEmpty()) {
			memoryPointVos.get(0).setIsFirst(true);
			memoryPointVos.get(memoryPointVos.size() - 1).setIsLast(true);
		}
	}

	/**
	 * 日期转换
	 */
	private void convertDate(List<MemoryComment> memoryComments) {
		for (MemoryComment comment : memoryComments) {
			comment.setInsdForShow(getDate(comment.getInsdForShow()));
		}
	}

	/**
	 * 日期转换
	 *
	 * @param inDate
	 * @return
	 */
	private String getDate(String inDate) {
		Date parse = null;
		try {
			parse = new SimpleDateFormat("yyyy-MM-dd").parse(inDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SimpleDateFormat("yyyy.MM.dd").format(parse);
	}


	/**
	 * 生成评论数据
	 *
	 * @param commentVo
	 * @return
	 */
	private MemoryComment createComment(CommentVo commentVo) {
		MemoryComment nComment = new MemoryComment();
		nComment.setInsdForShow(DateUtil.getCurrentDotDate());
		nComment.setTt(commentVo.getCommentTitle());
		//被评论人
		nComment.setCommentToUserId(commentVo.getCommentToUserId());
		nComment.setCommentToUserName(commentVo.getCommentToUserName());
		nComment.setuIdT(commentVo.getCommentToUserId());
		nComment.setUnameT(commentVo.getCommentToUserName());
		//评论人信息
		nComment.setuIdC(commentVo.getUserId());
		nComment.setUnameC(commentVo.getCommentUserName());
		nComment.setUhphotoC(commentVo.getCommentUserHead());
		nComment.setMemoryIdSource(commentVo.getMemoryIdSource());
		nComment.setMemoryPointIdSource(commentVo.getMemoryPointIdSource());
		nComment.setSource(commentVo.getSource());
		nComment.setpId(commentVo.getMemoryPointId());
		nComment.setgId(commentVo.getGroupId());
		nComment.setmId(commentVo.getMemoryId());
		return nComment;
	}

}

