package com.time.memory.view.fragment.memory;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.CommentCallBack;
import com.time.memory.core.callback.IMPointCommentCallBack;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.Comment;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.Praise;
import com.time.memory.gui.ActionSheet;
import com.time.memory.gui.MemoryMoreSheet;
import com.time.memory.gui.MyGridView;
import com.time.memory.gui.MyListView;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.gui.gallery.PhotoPreviewActivity;
import com.time.memory.gui.sixGridImage.SixGridImageView;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.mt.nio.message.response.CA10RespVo;
import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.presenter.MemoryPointPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.CPResourceUtil;
import com.time.memory.util.DevUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.util.ShareUtil;
import com.time.memory.view.activity.circle.AddFriendActivity;
import com.time.memory.view.activity.circle.FriendActivity;
import com.time.memory.view.activity.memory.PhotoManagerActivity;
import com.time.memory.view.adapter.MemoryCommentAdapter;
import com.time.memory.view.adapter.MemoryPraiseAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.impl.IMemoryPointView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:片段详情
 * @date 2016/10/27 8:37
 */
public class MemoryPointlFragment extends BaseFragment implements IMemoryPointView, EasyPermissions.PermissionCallbacks, CommentCallBack, TextView.OnEditorActionListener, IMPointCommentCallBack {

	private static final String TAG = "MemoryDetailsFragment";

	@Bind(R.id.writer_grid)
	SixGridImageView writer_grid;//6宫图
	@Bind(R.id.swipe_target)
	MyListView swipeTarget;//评论详情
	@Bind(R.id.grid_view)
	MyGridView myGridView;//点赞人图片
	@Bind(R.id.memoryp_auther_tv)
	TextView autherTv;//作者
	@Bind(R.id.memoryp_date_tv)
	TextView dateTv;//日期
	@Bind(R.id.memoryp_desc_tv)
	TextView descTv;//描述
	@Bind(R.id.memoryp_comment_tv)
	TextView commentNumTv;//评论条数
	@Bind(R.id.memoryp_priase_tv)
	TextView priaseTv;//无点赞数据
	@Bind(R.id.memoryp_nocomment_iv)
	ImageView memoryNoIv;//无评论数据
	@Bind(R.id.memoryp_nocomment_tv)
	TextView memoryNoTv;//无评论数据
	@Bind(R.id.memoryp_fork_iv)
	ImageView forkIv;//是否点赞
	@Bind(R.id.memoryp_send_et)
	EditText sendEt;//输入内容

	private MemoryPraiseAdapter praiseAdapter;//点赞
	private MemoryCommentAdapter commentAdapter;//评论

	private MemoryEdit memoryEdit;//记忆片段
	private String userId;//所有人
	private String memoryId;//记忆ID
	private String memorySrcId;//记忆ID源Id
	private String groupId;//群Id
	private String groupName;//群Name
	private String headPic;//头像地址
	private String userName;//用户名
	private int state;
	private int fork;//点赞
	private int unFork;//未点赞
	private int curPoint;//当前是第几个片段
	private int sharedPosition;//分享位置
	private boolean isToUser;//回复给用户
	private String toUserId;//被回复用户Id
	private String toUserName;//被回复用户name
	private String title;
	private String url;//分享网址
	private ShareUtil shareUtil;//分享工具类
	private String contents;
	private String imUrlOSS;
	private String imUrPath;


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Bundle bundle = getArguments();
		userId = bundle.getString("userId");
		memoryId = bundle.getString("memoryId");
		memorySrcId = bundle.getString("memorySrcId");
		groupId = bundle.getString("groupId");
		groupName = bundle.getString("groupName");
		headPic = bundle.getString("headPic");
		userName = bundle.getString("userName");
		state = bundle.getInt("state");
		title = bundle.getString("title");
		memoryEdit = bundle.getParcelable("memoryPoint");
		curPoint = bundle.getInt("curPoint", 1);
		groupId = state == 1 ? memoryEdit.getGroupId() : groupId;
	}

	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memory_pointl, null);
		return view;
	}

	@Override
	public void initView(View view) {
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryPointPresenter();
	}

	@Override
	public void initData() {
		shareUtil = new ShareUtil();
		url = getString(R.string.FSSHAREURL) + "/mt-nio/webPage/memoryInfo.htm?memoryId=" + memoryId;
		imUrlOSS = mContext.getString(R.string.FSIMAGEOSSDETAIL);
		imUrPath = mContext.getString(R.string.FSIMAGEOSS);
		fork = R.drawable.pointhearted;
		unFork = R.drawable.pointheart;
		sendEt.setOnEditorActionListener(this);
		//6宫图初始化
		initImageAdapter();
		//设置数据
		setPointMessage();
		//获取片段
		((MemoryPointPresenter) mPresenter).getMemoryInfos(memoryEdit.getMemoryPointId(), state > 1 ? "CD90G" : "CD90M");
	}

	@Override
	public void onDestroy() {
		commentAdapter = null;
		praiseAdapter = null;
		super.onDestroy();
	}

	private int getIndex(int length) {
		int index = length % 5 + 1;
		return CPResourceUtil.getDrawableId(mContext, "commbg" + index);
	}

	/**
	 * 6宫图加载
	 */
	private void initImageAdapter() {
		final int size = memoryEdit.getPhotoInfos().size();
		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
				String url = entity.getPhotoPath() + imUrlOSS;
				int index = getIndex(Math.abs(url.hashCode()));
				if (size == 1) {
					Picasso.with(context).load(url).centerCrop().resize(800, 600).error(index).placeholder(index).into(imageView);
				} else {
					Picasso.with(context).load(url).centerCrop().resize(400, 400).error(index).placeholder(index).into(imageView);
				}
			}

			@Override
			protected void onDeleteClick(int position) {
			}

			@Override
			protected void onItemImageClick(int position) {
				CLog.e(TAG, "position:" + position);
				((MemoryPointPresenter) mPresenter).convertList(memoryEdit.getPhotoInfos(), curPoint, position);
			}

			@Override
			protected void onAddClick(int position) {
			}
		};
		writer_grid.setAdapter(mAdapter);
	}


	@OnClick({R.id.memoryp_fork_iv, R.id.memoryp_add_iv, R.id.memoryp_shared_iv})
	public void onClick(View view) {
		super.onMyClick(view);
		switch (view.getId()) {
			case R.id.app_cancle:
				getActivity().finish();
				break;
			case R.id.memoryp_fork_iv:
				//点赞
				((MemoryPointPresenter) mPresenter).reqFork(getString(R.string.FSFORK), MainApplication.getUserToken(), userId, MainApplication.getUserId(), memoryId, memoryEdit.getMpId(), groupId, memoryEdit.getMpFlag(), memorySrcId, memoryEdit.getMemorySrcId());
				break;
			case R.id.memoryp_add_iv:
				//追加记忆
				GalleryFinal.openGalleryMuti(ReqConstant.REQUEST_CODE_GALLERY, initSupporyPhoto(6, false, true, true, state, groupId, memoryId, memorySrcId, userId, new ArrayList<PhotoInfo>()), null);
				break;
			case R.id.memoryp_shared_iv:
				//分享
				showMoreDialog();
				break;
		}
	}


	@OnItemClick(R.id.grid_view)
	public void onItemClick(View view, int position) {
		//点赞
		onPraise(position);
	}

	/**
	 * 查看图片
	 *
	 * @param photoInfos
	 * @param curIndex
	 */
	@Override
	public void startActivity(ArrayList<PhotoInfo> photoInfos, int curIndex) {
		Intent intent = new Intent(mContext, PhotoManagerActivity.class);
		intent.putExtra("onlyWatch", true);
		intent.putExtra("curClick", curIndex);
		intent.putParcelableArrayListExtra(PhotoPreviewActivity.PHOTO_LIST, photoInfos);
		startAnimActivity(intent);
	}

	/**
	 * 点赞
	 *
	 * @param position
	 */
	private void onPraise(int position) {
		Intent intent = new Intent();
		Praise praise = memoryEdit.getPraiseVos().get(position);
		if (praise.getUid().equals(MainApplication.getUserId())) {
			//点赞人==当前用户
			//intent.setClass(mContext, FriendActivity.class);
			return;
		} else {
			if (praise.getIsFriendFlg().equals("0")) {
				//是好友
				intent.setClass(mContext, FriendActivity.class);
			} else {
				//不是好友
				intent.setClass(mContext, AddFriendActivity.class);
			}
		}
		intent.putExtra("userId", praise.getUid());
		intent.putExtra("userName", praise.getUname());
		intent.putExtra("hPic", praise.getUhp());
		startAnimActivity(intent);
	}

	/**
	 * 评论
	 *
	 * @param position
	 */
	private void onComment(int position) {
		Intent intent = new Intent();
		Comment comment = memoryEdit.getCommentVos().get(position);
		if (comment.getU1id().equals(MainApplication.getUserId())) {
			//点赞人==当前用户
			//intent.setClass(mContext, FriendActivity.class);
			return;
		} else {
			if (comment.getIsFriendFlg().equals("0")) {
				//是好友
				intent.setClass(mContext, FriendActivity.class);
			} else {
				//不是好友
				intent.setClass(mContext, AddFriendActivity.class);
			}
		}
		intent.putExtra("userId", comment.getU1id());
		intent.putExtra("userName", comment.getU1name());
		intent.putExtra("hPic", comment.getU1hp());
		startAnimActivity(intent);
	}

	/**
	 * 评论点击(更多)
	 *
	 * @param position
	 */
	@Override
	public void onMoreClick(int position) {
		if (userId.equals(MainApplication.getUserId())) {
			//自己->管理员
			if (memoryEdit.getCommentVos().get(position).getU1id().equals(MainApplication.getUserId())) {
				//评论发布人Id==当前用户Id-->删除
				showCommentsDialog(position, -1, "删除");
			} else {
				//评论发布人Id!=当前用户Id-->删除||举报
				showCommentsDialog(position, 0, "删除", "举报");
			}
		} else {
			if (memoryEdit.getCommentVos().get(position).getU1id().equals(MainApplication.getUserId())) {
				//评论人Id==记忆所有人Id
				showCommentsDialog(position, -1, "删除");
			} else {
				showCommentsDialog(position, 1, "举报");
			}
		}
	}

	/**
	 * 评论点击(回复用户)
	 *
	 * @param position
	 */
	@Override
	public void onCommentClick(int position) {
		//回复别人
		isToUser = true;
		toUserId = memoryEdit.getCommentVos().get(position).getU1id();
		toUserName = memoryEdit.getCommentVos().get(position).getU1name();
		sendEt.setHint("回复 : " + toUserName);
		KeyBoardUtils.ShowKeyboard(sendEt);
	}

	/**
	 * 点击头像
	 *
	 * @param position
	 */
	@Override
	public void onUserCall(int position) {
		onComment(position);
	}

	/**
	 * EditText响应
	 *
	 * @param v
	 * @param actionId
	 * @param event
	 * @return
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND && isGoOn()) {
			//发送评论-回复记忆体
			String msg = sendEt.getText().toString().trim();
			((MemoryPointPresenter) mPresenter).sendCommentMsg(getString(R.string.FSCOMMENT), MainApplication.getUserToken(), userId, memoryId, memoryEdit.getMpId(), groupId, MainApplication.getUserId(), toUserId, msg, headPic, userName, toUserName, isToUser, memorySrcId, memoryEdit.getMemorySrcId(), String.valueOf(state));
		}
		return true;
	}

	/**
	 * 获取段落详情数据
	 *
	 * @param entity
	 */
	@Override
	public void setMemoryInfo(MemoryEdit entity) {
		if (!TextUtils.isEmpty(entity.getDetail())) {
			contents = entity.getDetail();
		} else {
			contents = "快去看看吧！";
		}
		memoryEdit.setCommentVos(entity.getCommentVos());
		memoryEdit.setPraiseVos(entity.getPraiseVos());
		setPraiseAdapter();
		setCommentAdapter();
	}


	/**
	 * 发送评论成功标志
	 *
	 * @param isSuccess
	 */
	@Override
	public void sendCommentSuccess(boolean isSuccess) {
		//清空数据
		sendEt.setHint("评论");
		sendEt.setText("");
		isToUser = false;
		toUserId = "";
		toUserName = "";
		KeyBoardUtils.HideKeyboard(sendEt);
	}

	/**
	 * 点赞成功标志
	 *
	 * @param flag
	 * @param isSuccess
	 */
	@Override
	public void sendForkSuccess(String flag, boolean isSuccess) {
		//成功进行处理;失败不管
		if (isSuccess) {
			memoryEdit.setMpFlag(flag);
			forkIv.setImageResource(flag.equals("0") ? fork : unFork);
			((MemoryPointPresenter) mPresenter).setForker(MainApplication.getUserId(), flag, memoryEdit, userName, headPic);
		}
	}

	/**
	 * 刷新评论数据
	 *
	 * @param comment
	 */
	@Override
	public void refreshComment(Comment comment) {
		//正序排列
		if (memoryEdit.getCommentVos() == null) {
			memoryEdit.setCommentVos(new ArrayList<Comment>());
		}
		memoryEdit.getCommentVos().add(comment);
		if (commentAdapter == null) {
			commentAdapter = new MemoryCommentAdapter(mContext, memoryEdit.getCommentVos());
			commentAdapter.setCommentCallBack(this);
			swipeTarget.setAdapter(commentAdapter);
			setCommentessage();
		} else {
			commentAdapter.notifyDataSetChanged();
		}

		if (memoryEdit.getCommentVos() != null && !memoryEdit.getCommentVos().isEmpty()) {
			commentNumTv.setText(String.format(getString(R.string.comment_sign), memoryEdit.getCommentVos().size()));
			memoryNoIv.setVisibility(View.GONE);
			memoryNoTv.setVisibility(View.GONE);
			swipeTarget.setVisibility(View.VISIBLE);
		} else {
			commentNumTv.setText(getString(R.string.memory_comment));
			memoryNoIv.setVisibility(View.VISIBLE);
			memoryNoTv.setVisibility(View.VISIBLE);
			swipeTarget.setVisibility(View.GONE);
		}
	}

	/**
	 * 移除评论数据
	 *
	 * @param position
	 */
	@Override
	public void removeComment(int position) {
		memoryEdit.getCommentVos().remove(position);
		commentAdapter.notifyDataSetChanged();
		if (memoryEdit.getCommentVos() != null && !memoryEdit.getCommentVos().isEmpty()) {
			commentNumTv.setText(String.format(getString(R.string.comment_sign), memoryEdit.getCommentVos().size()));
			memoryNoIv.setVisibility(View.GONE);
			memoryNoTv.setVisibility(View.GONE);
			swipeTarget.setVisibility(View.VISIBLE);
		} else {
			commentNumTv.setText(getString(R.string.memory_comment));
			memoryNoIv.setVisibility(View.VISIBLE);
			memoryNoTv.setVisibility(View.VISIBLE);
			swipeTarget.setVisibility(View.GONE);
		}
	}


	/**
	 * 刷新点赞数据
	 */
	@Override
	public void refreshPraise() {
		if (praiseAdapter == null) {
			praiseAdapter = new MemoryPraiseAdapter(mContext, memoryEdit.getPraiseVos());
			myGridView.setAdapter(praiseAdapter);
		} else {
			praiseAdapter.notifyDataSetChanged();
		}
		if (memoryEdit.getPraiseVos().isEmpty()) {
			myGridView.setVisibility(View.GONE);
			priaseTv.setVisibility(View.VISIBLE);
		} else {
			myGridView.setVisibility(View.VISIBLE);
			priaseTv.setVisibility(View.GONE);
		}
	}


	/**
	 * UDP 评论回调
	 */
	@Override
	public void onPointComment(CA10RespVo ca10RespVo) {
	}

	/**
	 * UDP 删除评论回调
	 *
	 * @param cx02ReqVo
	 */
	@Override
	public void onRemoveComment(CX02RespVo cx02ReqVo) {
	}


	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	/**
	 * 设置点赞数据
	 */
	private void setPraiseAdapter() {
		if (myGridView != null) {
			praiseAdapter = new MemoryPraiseAdapter(mContext, memoryEdit.getPraiseVos());
			myGridView.setAdapter(praiseAdapter);
			if (memoryEdit.getPraiseVos().isEmpty()) {
				myGridView.setVisibility(View.GONE);
				priaseTv.setVisibility(View.VISIBLE);
			} else {
				myGridView.setVisibility(View.VISIBLE);
				priaseTv.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置评论数据
	 */
	private void setCommentAdapter() {
		if (swipeTarget != null) {
			commentAdapter = new MemoryCommentAdapter(mContext, memoryEdit.getCommentVos());
			commentAdapter.setCommentCallBack(this);
			swipeTarget.setAdapter(commentAdapter);
			setCommentessage();
		}
	}

	/**
	 * 设置信息
	 */
	private void setPointMessage() {
		writer_grid.setImagesData(memoryEdit.getPhotoInfos());//图片
		autherTv.setText(memoryEdit.getUname());//作者
		dateTv.setText(memoryEdit.getMemoryPointDate());//日期
		descTv.setText(memoryEdit.getDetail());//描述
		forkIv.setImageResource(memoryEdit.getMpFlag().equals("0") ? fork : unFork);//点赞
		setCommentessage();
	}

	/**
	 * 设置评论信息
	 */
	private void setCommentessage() {
		if (memoryEdit.getCommentVos() != null && !memoryEdit.getCommentVos().isEmpty()) {
			commentNumTv.setText(String.format(getString(R.string.comment_sign), memoryEdit.getCommentVos().size()));
			memoryNoIv.setVisibility(View.GONE);
			memoryNoTv.setVisibility(View.GONE);
			swipeTarget.setVisibility(View.VISIBLE);
		} else {
			commentNumTv.setText(getString(R.string.memory_comment));
			memoryNoIv.setVisibility(View.VISIBLE);
			memoryNoTv.setVisibility(View.VISIBLE);
			swipeTarget.setVisibility(View.GONE);
		}
	}

	/**
	 * 评论弹窗
	 *
	 * @param position
	 * @param title
	 */
	private void showCommentsDialog(final int position, final int opr, String... title) {
		ActionSheet.createBuilder(getActivity(), getChildFragmentManager()).
				setCancelableOnTouchOutside(true).
				setOtherButtonTitles(title).
				setTitlePadding(DevUtils.getTitleHeight(getActivity())).
				setBottomPadding(DevUtils.getBottomStatusHeight(getActivity())).
				setCancelButtonTitle("取消").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						if (opr == -1) {
							//TODO 删除
							((MemoryPointPresenter) mPresenter).removeComment(getString(R.string.FSREMOVECOMMENT), MainApplication.getUserToken(), memoryEdit.getCommentVos().get(position).getCid(), memoryId, groupId, position, memoryEdit.getCommentVos().get(position).getU1id(), MainApplication.getUserId(), String.valueOf(state), memorySrcId);
						} else if (opr == 1) {
							//举报
							reportUser(position, true);
						} else {
							if (index == 0) {
								//删除
								((MemoryPointPresenter) mPresenter).removeComment(getString(R.string.FSREMOVECOMMENT), MainApplication.getUserToken(), memoryEdit.getCommentVos().get(position).getCid(), memoryId, groupId, position, memoryEdit.getCommentVos().get(position).getU1id(), MainApplication.getUserId(), String.valueOf(state), memorySrcId);
							} else {
								//举报
								reportUser(position, true);
							}
						}
					}
				}).show();
	}

	/**
	 * 更多的弹窗
	 */
	private void showMoreDialog() {
		MemoryMoreSheet.createBuilder(getActivity(), getChildFragmentManager()).
				setCancelableOnTouchOutside(true).
				isDelete(false).
				isShared(true).
				setTitlePadding(DevUtils.getTitleHeight(getActivity())).
				setBottomPadding(DevUtils.getBottomStatusHeight(getActivity())).
				setListener(new MemoryMoreSheet.onMemoryMoreListener() {
					@Override
					public void onSubmit(int postion) {
						// 我刚在《高三九班》写了一篇记忆《认真钓鱼的笑笑》
						sharedPosition = postion;
						requestWriterPermission();
					}

					@Override
					public void onCancle() {
					}
				}).show();
	}

	/**
	 * 分享
	 *
	 * @param postion
	 */
	private void shared(int postion) {
		String content = "";
		if (state == 2) {
			content = "我刚在«" + groupName + "»写了一篇记忆";
		} else {
			content = "我刚写了一篇记忆";
		}
		if (postion == 4) {
			if (memoryEdit.getPhotoInfos().size() != 0) {
				shareUtil.sinoShare(getActivity(), content, title, contents, memoryEdit.getPhotoInfos().get(0).getPhotoPath() + imUrPath, url);
			} else {
				shareUtil.sinoShareInt(getActivity(), content, title, contents, R.drawable.headpic, url);
			}
		} else if (postion == 3) {
			if (memoryEdit.getPhotoInfos().size() != 0) {
				shareUtil.qqSpaceShare(getActivity(), content, title, contents, memoryEdit.getPhotoInfos().get(0).getPhotoPath() + imUrPath, url);
			} else {
				shareUtil.qqSpaceShareInt(getActivity(), content, title, contents, R.drawable.headpic, url);
			}
		} else if (postion == 2) {
			if (memoryEdit.getPhotoInfos().size() != 0) {
				shareUtil.qqFriendShare(getActivity(), content, title, contents, memoryEdit.getPhotoInfos().get(0).getPhotoPath() + imUrPath, url);
			} else {
				shareUtil.qqFriendShareInt(getActivity(), content, title, contents, R.drawable.headpic, url);
			}
		} else if (postion == 1) {
			if (memoryEdit.getPhotoInfos().size() != 0) {
				shareUtil.friendsCircleShare(getActivity(), content, title, contents, memoryEdit.getPhotoInfos().get(0).getPhotoPath() + imUrPath, url);
			} else {
				shareUtil.friendsCircleShareInt(getActivity(), content, title, contents, R.drawable.headpic, url);
			}
		} else if (postion == 0) {
			if (memoryEdit.getPhotoInfos().size() != 0) {
				shareUtil.wXShare(getActivity(), content, title, contents, memoryEdit.getPhotoInfos().get(0).getPhotoPath() + imUrPath, url);
			} else {
				shareUtil.wXShareInt(getActivity(), content, title, contents, R.drawable.headpic, url);
			}
		}
	}


	/**
	 * 举报
	 *
	 * @param position  评论位置
	 * @param isComment 对评论操作
	 */
	private void reportUser(final int position, final boolean isComment) {
		ActionSheet.createBuilder(getActivity(), getChildFragmentManager()).
				setCancelableOnTouchOutside(true).
				setCancelButtonTitle("取消").
				setTitlePadding(DevUtils.getTitleHeight(getActivity())).
				setBottomPadding(DevUtils.getBottomStatusHeight(getActivity())).
				setOtherButtonTitles("色情/暴力信息", "广告信息", "钓鱼/欺诈信息", "诽谤造谣信息").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						if (isComment)
							((MemoryPointPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), memoryId, memoryEdit.getMpId(), memoryEdit.getCommentVos().get(position).getU1id(), "2", String.valueOf(index + 1));
						else
							((MemoryPointPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), memoryId, memoryEdit.getMpId(), "", "2", String.valueOf(index + 1));
					}
				}).show();
	}

	/**
	 * 读写文件
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_WRITE)
	protected void requestWriterPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			shared(sharedPosition);
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_writer),
					ReqConstant.REQUEST_CODE_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝,再次申请
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ReqConstant.REQUEST_CODE_WRITE) {
			//更改权限返回
			requestWriterPermission();
		} else {
			UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
		}
	}

}
