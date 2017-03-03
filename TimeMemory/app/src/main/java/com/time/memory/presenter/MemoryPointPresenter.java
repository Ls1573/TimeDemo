package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Comment;
import com.time.memory.entity.MemoryComment;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.MemoryPraise;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.Praise;
import com.time.memory.model.CommentController;
import com.time.memory.model.ForkController;
import com.time.memory.model.MemoryPointController;
import com.time.memory.model.MinaController;
import com.time.memory.model.ReportController;
import com.time.memory.model.impl.ICommentController;
import com.time.memory.model.impl.IForkController;
import com.time.memory.model.impl.IMemoryPointController;
import com.time.memory.model.impl.IMinaController;
import com.time.memory.model.impl.IReportController;
import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.mt.vo.CommentVo;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DateUtil;
import com.time.memory.view.impl.IMemoryPointView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆片段
 * @date 2016/10/27 8:47
 */
public class MemoryPointPresenter extends BasePresenter<IMemoryPointView> {
	private static final String TAG = "MemoryPointPresenter";

	private IMemoryPointController iMemoryPointController;
	private IReportController iReportController;
	private IMinaController iMinaController;
	private IForkController iForkController;
	private ICommentController iCommentController;

	// m层
	public MemoryPointPresenter() {
		iMemoryPointController = new MemoryPointController();
		iReportController = new ReportController();
		iMinaController = new MinaController();
		iForkController = new ForkController();
		iCommentController = new CommentController();
	}

	/**
	 * 设置当前位置
	 *
	 * @param photoInfos
	 * @param curPoint
	 */
	public void convertList(ArrayList<PhotoInfo> photoInfos, int curPoint, int curIndex) {
		if (photoInfos == null) photoInfos = new ArrayList<>();
		int size = photoInfos.size();
		for (int i = 0; i < size; i++) {
			photoInfos.get(i).setCurPoint(String.valueOf(curPoint + 1));
			photoInfos.get(i).setCurPointIndex(i + 1);
		}
		if (mView != null) {
			mView.startActivity(photoInfos, curIndex);
		}
	}

	/**
	 * 设置添加移除联系人
	 */
	public void setForker(String userId, String flag, MemoryEdit memoryEdit, String userName, String uHp) {
		if (memoryEdit.getPraiseVos() == null) {
			memoryEdit.setPraiseVos(new ArrayList<Praise>());
		}
		boolean isHas = false;
		int index = -1;
		int size = memoryEdit.getPraiseVos().size();
		for (int i = 0; i < size; i++) {
			//当赞人Id==当前用户id
			if (memoryEdit.getPraiseVos().get(i).getUid().equals(userId)) {
				isHas = true;
				index = i;
				break;
			}
		}
		//存在数据移除
		if (isHas) {
			memoryEdit.getPraiseVos().remove(index);
		} else {
			//没有就添加
			memoryEdit.getPraiseVos().add(new Praise(userId, userName, uHp));
		}
		if (mView != null) {
			mView.refreshPraise();
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
	public void sendCommentMsg(String url, String token, String userId, String memoryId, String memoryPointId, String groupId, String commentUserId, String commentToUserId, String commentTitle, String headPic, String userName, String touserName, boolean isToUser, String memoryIdSource, String memoryPointIdSource, String source) {
		//回复内容
		if (TextUtils.isEmpty(commentTitle)) {
			//评论为空
			if (mView != null) {
				mView.showFaild();
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
		final Comment memoryComment = createComment(commentVo);
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
							memoryComment.setCid(entity.getUuid());
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
	 * 刷新移除后评论数据
	 *
	 * @param memoryComments
	 */
	public void refreComment(List<MemoryComment> memoryComments, int position) {
		memoryComments.get(0).setIsFirst(false);
		memoryComments.get(memoryComments.size() - 1).setIsLast(false);
		memoryComments.remove(position);
		if (memoryComments.size() > 0) {
			memoryComments.get(0).setIsFirst(true);
			memoryComments.get(0).setSum(memoryComments.size());
			memoryComments.get(memoryComments.size() - 1).setIsLast(true);
		}
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
	 */
	public void removeComment(String url, String token, String uuid, String memoryId, String groupId, final int position,
							  String commentUserId, String userId, String source, String memoryIdSource) {
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
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					MemoryComment entity = (MemoryComment) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.removeComment(position);
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
	 * 点赞请求
	 *
	 * @param memoryUserId  记忆发布人ID
	 * @param praiseUserId  点赞人ID
	 * @param memoryId      记忆ID
	 * @param memoryPointId 记忆片断ID
	 * @param groupId       圈子ID
	 * @param delFlg        点赞/取消
	 */
	public void reqFork(String url, String token, String memoryUserId, String praiseUserId, String memoryId, String memoryPointId, String groupId, String delFlg, String memoryIdSource, String memoryPointIdSource) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		final String flag = delFlg.equals("0") ? "1" : "0";
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
							mView.sendForkSuccess(flag, true);
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
	 * 获取记忆片段详情
	 */
	public void getMemoryInfos(final String memoryId, String type) {
		String url = context.getString(R.string.FSMRMORYPOINT);
		if (!type.equals("CD90M"))
			url = context.getString(R.string.FSMRMORYPOINTD);
		iMemoryPointController.reqMemoryInfos(url, memoryId, type, new SimpleCallback() {
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
				} else {
					MemoryEdit entity = (MemoryEdit) data;
					if (entity.getStatus() == 0) {
						convertDate(entity);
						//成功
						if (mView != null) {
							mView.setMemoryInfo(entity);
						}
					}
				}
			}
		});
	}


	/**
	 * 生成评论数据
	 *
	 * @param commentVo
	 * @return
	 */
	private Comment createComment(CommentVo commentVo) {
		Comment nComment = new Comment();
		nComment.setInsdForShow(DateUtil.getCurrentDotDate());
		nComment.setCt(commentVo.getCommentTitle());
		nComment.setU2id(commentVo.getCommentToUserId());
		nComment.setU2name(commentVo.getCommentToUserName());
		nComment.setU1id(commentVo.getUserId());
		nComment.setU1name(commentVo.getCommentUserName());
		nComment.setU1hp(commentVo.getCommentUserHead());
		nComment.setMemoryIdSource(commentVo.getMemoryIdSource());
		nComment.setMemoryPointIdSource(commentVo.getMemoryPointIdSource());
		nComment.setSource(commentVo.getSource());
		return nComment;
	}


	/**
	 * 日期转换
	 *
	 * @param entity
	 */
	private void convertDate(MemoryEdit entity) {
		for (Comment comment : entity.getCommentVos()) {
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
}
