package com.time.memory.view.activity.memory;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryComment;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.MemoryPraise;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.User;
import com.time.memory.gui.ActionSheet;
import com.time.memory.gui.MemoryMoreSheet;
import com.time.memory.gui.PagingScrollHelper;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.presenter.MemoryInfosMPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.util.ShareUtil;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.circle.AddFriendActivity;
import com.time.memory.view.activity.circle.FriendActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.BaseHolder;
import com.time.memory.view.holder.MemoryOwnHolder;
import com.time.memory.view.impl.IMemoryDetailMView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆详情
 * @date 2016/9/14 16:21
 */
public class MemoryDetailActivtiy extends BaseActivity implements IMemoryDetailMView, AdapterCallback, EasyPermissions.PermissionCallbacks, PagingScrollHelper.onPageChangeListener, TextView.OnEditorActionListener {

	private static final String TAG = "MemoryDetailsActivtiy";

	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	@Bind(R.id.memoryp_send_rl)
	RelativeLayout memoryp_send_rl;

	@Bind(R.id.memoryp_send_et)
	EditText sendEt;//输入内容
	@Bind(R.id.memoryp_fork_iv)
	ImageView memoryp_fork_iv;//点赞
	@Bind(R.id.memoryp_shared_iv)
	ImageView sharedIv;//分享
	@Bind(R.id.memoryp_add_tv)
	ImageView addIv;//追加
	@Bind(R.id.tv_main_title)
	TextView tvMain;//主标题

	private PagingScrollHelper scrollHelper;//滑动帮助类

	private int fork;//点赞
	private int unFork;//未点赞

	private String groupId;//群Id
	private int mPosition;//第几条
	private int sharedPosition;//分享位置
	private int pageCount;//每页条数
	private int curPage;//第几页
	private int type;//热门&时间&标签
	private int state;//权限
	private boolean isSignle;
	private boolean isOwen;
	private String title;//title
	private String lableId;//标签Id
	private boolean isCanLoad = true;//能否继续加载
	private boolean isToUser;//回复给用户
	private String commentGroupId;//评论群Id
	private String commentmId;//评论记忆Id
	private String pointId;//评论片段Id
	private String toUserId;//被回复用户Id
	private String toUserName;//被回复用户name
	private User user;//当前用户
	private LinearLayoutManager linearLayoutManager;//布局管理

	private ArrayList<Memory> memories;//记忆集合(我的)->只是限定有几个页面的
	private ArrayList<OtherMemory> list;//记忆集合(他的)->只是限定有几个页面的
	private ArrayList<GroupMemory> glist;//记忆内容(群的的)->只是限定有几个页面的
	private List<MemoryInfo> memoryInfos;//记忆信息集合->实际填充的内容
	private BaseRecyclerAdapter myAdapter;
	private String url;//分享Url
	private List<Integer> removeList;//移除掉的记忆下标
	private ShareUtil shareUtil;//分享工具类
	private String contents;
	private String imgOss;
	private String imgPath;


	@Override
	protected void onDestroy() {
		System.gc();
		swipe_target = null;
		linearLayoutManager = null;
		memories = null;
		list = null;
		glist = null;
		memoryInfos = null;
		shareUtil = null;
		super.onDestroy();
	}

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_memory_info);
	}

	@Override
	public void initView() {
		//获取用户个人信息数据
		user = ((MemoryInfosMPresenter) mPresenter).getUser(MainApplication.getUserId());

		state = getIntent().getIntExtra("state", 1);
		mPosition = getIntent().getIntExtra("position", 0);
		groupId = getIntent().getStringExtra("groupId");
		type = getIntent().getIntExtra("type", 0);
		title = getIntent().getStringExtra("title");
		lableId = getIntent().getStringExtra("lableId");
		pageCount = getIntent().getIntExtra("pageCount", 5);
		isSignle = getIntent().getBooleanExtra("isSliding", false);
		isOwen = getIntent().getBooleanExtra("isOwen", false);
		initTopBarForBoth(title, R.drawable.image_close, "", R.drawable.image_more);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryInfosMPresenter();
	}

	@Override
	public void initData() {
		imgOss = getString(R.string.FSIMAGEPATH);
		imgPath = getString(R.string.FSIMAGEOSS);
		url = getString(R.string.FSSHAREURL) + "/mt-nio/webPage/memoryInfo.htm?memoryId=";
		//点赞图片
		fork = R.drawable.connent_hearted;
		unFork = R.drawable.connent_heart;
		sendEt.setOnEditorActionListener(this);
		memoryInfos = new ArrayList<>();
		linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
		scrollHelper = new PagingScrollHelper();
		scrollHelper.setUpRecycleView(swipe_target);
		scrollHelper.setOnPageChangeListener(this);
		scrollHelper.updateLayoutManger();
		swipe_target.setLayoutManager(linearLayoutManager);

		((DefaultItemAnimator) swipe_target.getItemAnimator()).setSupportsChangeAnimations(false);

		getState();
		if (mPosition != 0 && !isSignle) {
			int width = getWindowManager().getDefaultDisplay().getWidth();
			scrollHelper.setOffsetX(mPosition, width);
			linearLayoutManager.scrollToPosition(mPosition);
		}
		//计算页数
		curPage = mPosition / pageCount + 1;

		//添加软键盘弹出隐藏监听
		addOnSoftKeyBoardVisibleListener();
		shareUtil = new ShareUtil();
	}

	@OnClick({R.id.app_cancle, R.id.iv_main_right, R.id.memoryp_fork_iv, R.id.memoryp_add_tv, R.id.memoryp_shared_iv})
	public void onClick(View view) {
		if (view.getId() == R.id.app_cancle) {
			//关闭(ok)
			setMyResylt();
		} else if (view.getId() == R.id.iv_main_right) {
			//更多(ok)
			showMoreDialog(false);
		} else if (view.getId() == R.id.memoryp_fork_iv) {
			//点赞(ok)
			onDataCallBack(10, -1, 10, mPosition);
		} else if (view.getId() == R.id.memoryp_add_tv) {
			//追加记忆(ok)
			onDataCallBack(7, -1, 7, mPosition);
		} else if (view.getId() == R.id.memoryp_shared_iv) {
			//分享(ok)
			onDataCallBack(11, -1, 11, mPosition);
		}
	}

	@Override
	public void onBackPressed() {
		setMyResylt();
	}

	/**
	 * 数据分发
	 */
	private void getState() {
		if (state == 1) {
			//我的
			getMyMemorys();
		} else if (state == 0) {
			//他的
			getOtherMemorys();
		} else {
			//群的
			getGroupMemorys();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//设置singTask的intent
		setIntent(intent);
		TempMemory memory = getIntent().getParcelableExtra("tempMemory");
		int editMemory = getIntent().getIntExtra("editMemory", 0);
		if (memory != null) {
			//追加
			((MemoryInfosMPresenter) mPresenter).addMemoryPoints(memory, memoryInfos.get(mPosition), mPosition);
		} else if (editMemory == 1) {
			//TODO 编辑记忆
//			if (state == 1) {
//				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSMYMEMORYINFO), memoryInfos.get(mPosition).getMemory().getMemoryId(), memoryInfos.get(mPosition).getMemory().getMemorySrcId(), "CD01", state, mPosition, true, memoryInfos.get(mPosition).getPraise(), memoryInfos.get(mPosition));
//			} else {
//				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSGROUPMEMORYINFO), glist.get(mPosition).getMemoryId(), null, "CD03", state, mPosition, true, glist.get(mPosition).getPraiseCount(), memoryInfos.get(mPosition));
//			}
			if (state == 1)
				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSMYMEMORYINFO), memories.get(mPosition).getMemoryId(), memories.get(mPosition).getMemorySrcId(), "CD01", state, mPosition, false, -1, memoryInfos.get(mPosition));
			else
				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSGROUPMEMORYINFO), glist.get(mPosition).getMemoryId(), null, "CD03", state, mPosition, false, -1, memoryInfos.get(mPosition));

		}
	}

	/**
	 * 我的
	 *
	 * @param memList
	 */
	@Override
	public void setMyAdapter(List<Memory> memList) {
		if (swipe_target != null) {
			int size = memList.size();
			for (int i = 0; i < size; i++) {
				memoryInfos.add(new MemoryInfo());
			}
			if (myAdapter == null) {
				myAdapter = new BaseRecyclerAdapter(memoryInfos, R.layout.fragment_memory_detail, MemoryOwnHolder.class);
				myAdapter.setCallBack(this);
				swipe_target.setAdapter(myAdapter);
			} else {
				memories.addAll(memList);
				myAdapter.notifyDataSetChanged();
			}
			if (size != pageCount) isCanLoad = false;
		}
	}

	/**
	 * 他的
	 */
	@Override
	public void setOtherAdapter(ArrayList<OtherMemory> memList) {
		if (swipe_target != null) {
			if (memoryInfos == null) this.memoryInfos = new ArrayList<>();
			int size = memList.size();
			for (int i = 0; i < size; i++) {
				memoryInfos.add(new MemoryInfo());
			}
			if (myAdapter == null) {
				myAdapter = new BaseRecyclerAdapter(memoryInfos, R.layout.fragment_memory_detail, MemoryOwnHolder.class);
				myAdapter.setCallBack(this);
				swipe_target.setAdapter(myAdapter);
			} else {
				list.addAll(memList);
				myAdapter.notifyDataSetChanged();
			}
			if (size != pageCount) isCanLoad = false;
		}
	}

	/**
	 * 群的
	 *
	 * @param memList
	 */
	@Override
	public void setGroupAdapter(ArrayList<GroupMemory> memList) {
		if (swipe_target != null) {
			if (memoryInfos == null) this.memoryInfos = new ArrayList<>();
			int size = memList.size();
			for (int i = 0; i < size; i++) {
				memoryInfos.add(new MemoryInfo());
			}
			if (myAdapter == null) {
				myAdapter = new BaseRecyclerAdapter(memoryInfos, R.layout.fragment_memory_detail, MemoryOwnHolder.class);
				myAdapter.setCallBack(this);
				swipe_target.setAdapter(myAdapter);
			} else {
				glist.addAll(memList);
				myAdapter.notifyDataSetChanged();
			}
			if (size != pageCount) isCanLoad = false;
		}
	}

	@Override
	public void setMyInfoAdapter(MemoryInfo memoryInfo, int position, boolean isNew) {
		//赋值
		if (isNew) {
			memoryInfos.get(position).setMemoryInfo(memoryInfo);
			//已经加载
			if (state == 1) {
				memories.get(position).setIsLoaded(true);
			} else if (state == 0)
				list.get(position).setIsLoaded(true);
			else {
				glist.get(position).setIsLoaded(true);
			}
			myAdapter.notifyItemChanged(position);
		} else {
			memoryInfos.get(position).setNewMemoryInfo(memoryInfo);
			refreshAllAdapter(mPosition);
		}
		//更新
		upMemors(position);
		//设置点赞指示
		memoryp_fork_iv.setImageResource(memoryInfo.getMemory().getMpFlag().equals("1") ? unFork : fork);
		//设置输入框显示
		memoryp_send_rl.setVisibility(View.VISIBLE);
	}

	@Override
	public void removeMemory(int position, int state) {
		if (state == 1) {
			memories.remove(position);
		} else if (state == 0) {
			list.remove(position);
		} else {
			glist.remove(position);
		}
		//移除记忆代表集合
		if (removeList == null) removeList = new ArrayList<>();
		removeList.add(position);
		memoryInfos.remove(position);
		if (memoryInfos.size() == 0) {
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return;
		}
		myAdapter.notifyItemRemoved(position);
		myAdapter.notifyItemRangeChanged(position, memoryInfos.size() - position);
		if (position != memoryInfos.size()) {
			onPageChange(position);
		} else {
			scrollHelper.setDelOffsetX();
			onPageChange(position - 1);
		}
	}


	@Override
	public void onPageChange(int index) {
		if (isFinishing()) return;
		OnDispatchTouch();
		//滑动取值
		this.mPosition = index;
		getMemorysInfo();
	}

	/**
	 * 添加Group
	 *
	 * @param groupName
	 */
	public void addGroup(String groupName) {
		if (memoryInfos.get(mPosition).getMemory().getGroupNameList() == null)
			memoryInfos.get(mPosition).getMemory().setGroupNameList(new ArrayList<String>());
		if (memories.get(mPosition).getGroupNameList() == null)
			memories.get(mPosition).setGroupNameList(new ArrayList<String>());
		if (TextUtils.isEmpty(groupName)) {
			memoryInfos.get(mPosition).getMemory().getGroupNameList().clear();
			memories.get(mPosition).getGroupNameList().clear();
		} else {
			memoryInfos.get(mPosition).getMemory().getGroupNameList().add(groupName);
			if (memories.get(mPosition).getGroupNameList().contains("私密记忆")) {
				memories.get(mPosition).getGroupNameList().remove("私密记忆");
			}
			memories.get(mPosition).getGroupNameList().add(groupName);
		}
		refreshAllAdapter(mPosition);
	}

	/**
	 * 事件模拟
	 */
	private void OnDispatchTouch() {
		long time = SystemClock.uptimeMillis();
		swipe_target.dispatchTouchEvent(MotionEvent.obtain(time, time, MotionEvent.ACTION_CANCEL, 0, 0, 0));//强制停止
		swipe_target.dispatchTouchEvent(MotionEvent.obtain(time + 10, time + 10, MotionEvent.ACTION_DOWN, 100, 100, 0));//模拟按下
	}

	/**
	 * 监听软键盘状态
	 */
	public void addOnSoftKeyBoardVisibleListener() {
		final View decorView = getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						decorView.getWindowVisibleDisplayFrame(rect);
						int displayHight = rect.bottom - rect.top;
						int hight = decorView.getHeight();
						boolean visible = (double) displayHight / hight < 0.8;
						int visable = View.VISIBLE;
						//TODO
						if (visible) {
							visable = View.GONE;
						} else {
							visable = View.VISIBLE;
						}
						sharedIv.setVisibility(visable);
						addIv.setVisibility(visable);
						memoryp_fork_iv.setVisibility(visable);
					}
				}
		);
	}

	/**
	 * 获取数据信息
	 */
	private void getMemorysInfo() {
		if (state == 1) {
			if (!memories.get(mPosition).isLoaded()) {
				//我的
				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSMYMEMORYINFO), memories.get(mPosition).getMemoryId(), memories.get(mPosition).getMemorySrcId(), "CD01", state, mPosition, true, memories.get(mPosition).getPraiseCount(), memoryInfos.get(mPosition));
				memories.get(mPosition).setUnReadMemoryCnt(0);
				memoryp_send_rl.setVisibility(View.GONE);
			} else {
				setmemoryPSend();
			}
			if (mPosition + 1 == memories.size() && isCanLoad) {
				//页数自加
				curPage++;
				//最后一页了->请求数据
				((MemoryInfosMPresenter) mPresenter).getMessage(state, String.valueOf(type), lableId, groupId, curPage, pageCount);
			}
		} else if (state == 0) {
			//他的
			if (!list.get(mPosition).isLoaded()) {
				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSOTHERMEMORYINFO), list.get(mPosition).getMemoryId(), null, "CD02", state, mPosition, true, -1, memoryInfos.get(mPosition));
				list.get(mPosition).setUnReadMemoryCnt(0);
				list.get(mPosition).setUnReadMPointAddCnt(0);
				memoryp_send_rl.setVisibility(View.GONE);
			} else {
				setmemoryPSend();
			}
			if (mPosition + 1 == list.size() && isCanLoad) {
				curPage++;
				((MemoryInfosMPresenter) mPresenter).getMessage(state, String.valueOf(type), lableId, groupId, curPage, pageCount);
			}
			//标题
			setTitle();
		} else {
			//群的.
			if (!glist.get(mPosition).isLoaded()) {
				((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSGROUPMEMORYINFO), glist.get(mPosition).getMemoryId(), null, "CD03", state, mPosition, true, glist.get(mPosition).getPraiseCount(), memoryInfos.get(mPosition));
				glist.get(mPosition).setUnReadMPointAddCnt(0);
				glist.get(mPosition).setUnReadMPointAddCnt(0);
				memoryp_send_rl.setVisibility(View.GONE);
			} else {
				setmemoryPSend();
			}
			if (mPosition + 1 == glist.size() && isCanLoad) {
				curPage++;
				((MemoryInfosMPresenter) mPresenter).getMessage(state, String.valueOf(type), lableId, groupId, curPage, pageCount);
			}
		}
	}

	/**
	 * 设置数据库显示
	 */
	private void setmemoryPSend() {
		if (memoryInfos.get(mPosition).isRight()) {
			//设置输入框显示
			memoryp_send_rl.setVisibility(View.VISIBLE);
			//设置点赞指示
			memoryp_fork_iv.setImageResource(memoryInfos.get(mPosition).getMemory().getMpFlag().equals("1") ? unFork : fork);
		} else {
			//设置输入框显示
			memoryp_send_rl.setVisibility(View.GONE);
		}
	}


	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
		isCanLoad = false;
		CLog.e(TAG, "++++++++++++++++++++++++++ isCanLoad:" + isCanLoad);
	}


	/**
	 * 刷新评论数据
	 *
	 * @param memoryTag
	 */
	@Override
	public void refreshPraiseAdapter(int memoryTag) {
//		myAdapter.notifyItemChanged(memoryInfos.get(memoryTag).getMemoryPointVos().size() + 1);
		refreshAllAdapter(memoryTag);
		//更新数据
		upMemors(memoryTag);
	}

	/**
	 * 记忆片段点赞
	 *
	 * @param flag
	 * @param isSuccess
	 */
	@Override
	public void sendPointForkSuccess(String flag, boolean isSuccess, int posion, int memoryTag) {
		//成功进行处理;失败不管
		if (isSuccess) {
			MemoryInfo memoryInfo = memoryInfos.get(memoryTag);
			int count = Integer.parseInt(memoryInfo.getMemoryPointVos().get(posion).getpCnt());
			int praise = memoryInfo.getPraise();
			if (flag.equals("0")) {
				//点赞人数
				++count;
				//点赞数(隐藏字段)
				++praise;
			} else {
				--count;
				--praise;
			}
			memoryInfo.setPraise(praise);
			memoryInfo.getMemoryPointVos().get(posion).setMpFlag(flag);
			memoryInfo.getMemoryPointVos().get(posion).setpCnt(String.valueOf(count < 0 ? 0 : count));
			refreshAllAdapter(posion);
			((MemoryInfosMPresenter) mPresenter).setForker(user, flag, memoryInfo, memoryInfo.getMemory(), memoryTag);
		}
	}

	/**
	 * 记忆点赞
	 */
	@Override
	public void sendForkSuccess(String flag, boolean isSuccess, int memoryTag) {
		//成功进行处理;失败不管
		if (isSuccess) {
			//刷新下
			memoryInfos.get(memoryTag).getMemory().setMpFlag(flag);
			memoryp_fork_iv.setImageResource(flag.equals("1") ? unFork : fork);
			int praise = memoryInfos.get(memoryTag).getPraise();
			if (flag.equals("1")) {
				memoryInfos.get(memoryTag).setPraise(--praise);
			} else {
				memoryInfos.get(memoryTag).setPraise(++praise);
			}
			((MemoryInfosMPresenter) mPresenter).setForker(user, flag, memoryInfos.get(memoryTag), memoryInfos.get(memoryTag).getMemory(), memoryTag);
		}
	}

	/**
	 * 我的
	 */
	private void getMyMemorys() {
		memories = getIntent().getParcelableArrayListExtra("memorys");
		//添加Fragment数据
		((MemoryInfosMPresenter) mPresenter).convertIsLoad(memories);
		setMyAdapter(((MemoryInfosMPresenter) mPresenter).convertPhotoCount(memories));
		((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSMYMEMORYINFO), memories.get(mPosition).getMemoryId(), memories.get(mPosition).getMemorySrcId(), "CD01", state, mPosition, true, memories.get(mPosition).getPraiseCount(), memoryInfos.get(mPosition));
	}

	/**
	 * 他的
	 */
	private void getOtherMemorys() {
		list = getIntent().getParcelableArrayListExtra("otherList");
		//添加Fragment数据
		((MemoryInfosMPresenter) mPresenter).convertOIsLoad(list);
		setOtherAdapter(((MemoryInfosMPresenter) mPresenter).convertOPhotoCount(list));
		((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSOTHERMEMORYINFO), list.get(mPosition).getMemoryId(), null, "CD02", state, mPosition, true, -1, null);
	}

	/**
	 * 群的
	 */
	private void getGroupMemorys() {
		glist = getIntent().getParcelableArrayListExtra("groupList");
		//添加Fragment数据
		((MemoryInfosMPresenter) mPresenter).convertGIsLoad(glist);
		setGroupAdapter(((MemoryInfosMPresenter) mPresenter).convertGPhotoCount(glist));
		((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSGROUPMEMORYINFO), glist.get(mPosition).getMemoryId(), glist.get(mPosition).getMemorySrcId(), "CD03", state, mPosition, true, glist.get(mPosition).getPraiseCount(), memoryInfos.get(mPosition));
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			//发送评论-回复记忆体
			String msg = sendEt.getText().toString().trim();
			if (!isToUser) {
				commentGroupId = groupId;
				pointId = memoryInfos.get(mPosition).getMemory().getMemoryId();
				commentmId = memoryInfos.get(mPosition).getMemory().getMemoryId();
			}
			((MemoryInfosMPresenter) mPresenter).sendCommentMsg(getString(R.string.FSCOMMENT), MainApplication.getUserToken(), memoryInfos.get(mPosition).getMemory().getUserId(),
					commentmId, pointId,
					commentGroupId, MainApplication.getUserId(), toUserId, msg, user.getHeadPhoto(),
					user.getUserName(), toUserName, isToUser, memoryInfos.get(mPosition).getMemory().getMemoryIdSource(), "", String.valueOf(state));
		}
		return true;
	}

	/**
	 * 单击回调
	 *
	 * @param data      操作指示符
	 * @param position  哪个片段下标位置
	 * @param index     下标
	 * @param memoryTag 哪个记忆的下标
	 *                  </n>
	 *                  </n>
	 *                  0:点赞
	 *                  1:片段
	 *                  2:点赞人
	 *                  4:评论的更多
	 *                  5:观看图片
	 *                  6:评论人头像
	 *                  7:追加记忆
	 *                  8:片段图片->跳转到片段
	 *                  9:点击详情->回复
	 *                  10:记忆点赞
	 */
	@Override
	public void onDataCallBack(Object data, int position, int index, int memoryTag) {
		int opr = (int) data;
		CLog.e(TAG, "opr:" + opr + "  pos:" + position + "  index:" + index + "  tag:" + memoryTag);
		switch (opr) {
			case 0:
				//片段点赞(OK)
				((MemoryInfosMPresenter) mPresenter).reqPointFork(getString(R.string.FSFORK), MainApplication.getUserToken(), memoryInfos.get(memoryTag).getMemory().getUserId(), MainApplication.getUserId(), memoryInfos.get(memoryTag).getMemory().getMemoryId(), memoryInfos.get(memoryTag).getMemoryPointVos().get(position).getMpId(), state == 1 ? memoryInfos.get(memoryTag).getMemoryPointVos().get(position).getGroupId() : groupId, memoryInfos.get(memoryTag).getMemoryPointVos().get(position).getMpFlag(), position, memoryTag, memoryInfos.get(memoryTag).getMemory().getMemoryIdSource(), memoryInfos.get(memoryTag).getMemoryPointVos().get(position).getMemorySrcId());
				break;
			case 1:
				//片段(OK)
				startPoint(position, memoryInfos.get(memoryTag));
				break;
			case 2:
				//点赞人头像(OK)
				onPraise(position, memoryTag);
				break;
			case 4:
				//评论的更多(OK)
				oprComment(index, position, memoryTag);
				break;
			case 5:
				//观看图片(OK)
				((MemoryInfosMPresenter) mPresenter).getPhotos(memoryInfos.get(memoryTag).getMemoryPointVos(), position, index);
				break;
			case 6:
				//评论人头像(OK)
				onComment(position, memoryTag);
				break;
			case 7:
				//追加记忆(ok)
				GalleryFinal.openGalleryMuti(ReqConstant.REQUEST_CODE_GALLERY, initSupporyPhoto(6, false, true, true, state, groupId, memoryInfos.get(memoryTag).getMemory().getMemoryId(), memoryInfos.get(memoryTag).getMemory().getMemoryIdSource(), memoryInfos.get(memoryTag).getMemory().getUserId(), new ArrayList<PhotoInfo>()), null);
				break;
			case 8:
				//片段图片->跳转到片段(OK)
				((MemoryInfosMPresenter) mPresenter).getCommentPosition(position, memoryInfos.get(memoryTag));
				break;
			case 9:
				//点击详情->回复(OK)
				oprComment(index, position, memoryTag);
				break;
			case 10:
				//记忆主体点赞(OK)
				((MemoryInfosMPresenter) mPresenter).reqFork(getString(R.string.FSFORK), MainApplication.getUserToken(), memoryInfos.get(memoryTag).getMemory().getUserId(), MainApplication.getUserId(), memoryInfos.get(memoryTag).getMemory().getMemoryId(), memoryInfos.get(memoryTag).getMemory().getMemoryId(), groupId, memoryInfos.get(memoryTag).getMemory().getMpFlag(), memoryTag, memoryInfos.get(memoryTag).getMemory().getMemoryIdSource(), "");
				break;
			case 11:
				//分享更多(ok)
				showMoreDialog(true);
				break;
			default:
				CLog.e(TAG, "!---待排查---------->");
				break;
		}
	}

	/**
	 * 设置记忆标题(他的)
	 * {@link #getMemorysInfo}
	 */
	private void setTitle() {
		tvMain.setText(list.get(mPosition).getUserNameF() + "的记忆");
	}

	/**
	 * 操作更新
	 * {@link #setMyInfoAdapter}
	 * {@link #refreshPraiseAdapter}
	 * {@link #refreshComment}
	 * {@link #removeComment}
	 * {@link #refreshMemory}
	 *
	 * @param position
	 */
	private void upMemors(int position) {
		MemoryInfo memoryInfo = memoryInfos.get(position);
		//点赞数
		int praiseSize = memoryInfo.getPraise();
		//评论数
		int commentSize = memoryInfo.getCommentorVos().size();
		//追加记忆数
		int addMemoryCount = memoryInfo.getMemory().getAddmemoryCount();
		//p1
		String p1 = memoryInfo.getMemory().getPhoto1();
		//p2
		String p2 = memoryInfo.getMemory().getPhoto2();
		//p3
		String p3 = memoryInfo.getMemory().getPhoto3();
		//图片数
		int photoCount = memoryInfo.getMemory().getPhotoTotalCount();
		if (state == 1) {
			Memory memory = memories.get(position);
			memory.setPraiseCount(praiseSize);
			memory.setCommentCount(commentSize);
			memory.setAddmemoryCount(addMemoryCount);
			memory.setUnReadMPointAddCnt(0);
			memory.setPhotoCount(photoCount);
			memory.setPhoto1(p1);
			memory.setPhoto2(p2);
			memory.setPhoto3(p3);
		} else if (state == 0) {
			OtherMemory oMemory = list.get(position);
			oMemory.setPraiseCount(praiseSize);
			oMemory.setCommentCount(commentSize);
			oMemory.setAddmemoryCount(addMemoryCount);
			oMemory.setPhotoCount(photoCount);
			oMemory.setPhoto1(p1);
			oMemory.setPhoto2(p2);
			oMemory.setPhoto3(p3);
		} else {
			GroupMemory groupMemory = glist.get(position);
			groupMemory.setPraiseCount(praiseSize);
			groupMemory.setCommentCount(commentSize);
			groupMemory.setAddmemoryCount(addMemoryCount);
			groupMemory.setUnReadMPointAddCnt(0);
			groupMemory.setPhotoCount(photoCount);
			groupMemory.setPhoto1(p1);
			groupMemory.setPhoto2(p2);
			groupMemory.setPhoto3(p3);
		}
	}

	/**
	 * 评论操作
	 *
	 * @param index
	 */
	private void oprComment(int index, int position, int memoryTag) {
		if (index == 1) {
			//点击更多->弹窗
			if (memoryInfos.get(memoryTag).getMemory().getUserId().equals(MainApplication.getUserId())) {
				//自己->管理员
				if (memoryInfos.get(memoryTag).getCommentorVos().get(position).getuIdC().equals(MainApplication.getUserId())) {
					//评论发布人Id==当前用户Id-->删除
					showCommentsDialog(memoryTag, position, -1, "删除");
				} else {
					//评论发布人Id!=当前用户Id-->删除||举报
					showCommentsDialog(memoryTag, position, 0, "删除", "举报");
				}
			} else {
				if (memoryInfos.get(memoryTag).getCommentorVos().get(position).getuIdC().equals(MainApplication.getUserId())) {
					//评论人Id==记忆所有人Id
					showCommentsDialog(memoryTag, position, -1, "删除");
				} else {
					showCommentsDialog(memoryTag, position, 1, "举报");
				}
			}
		} else if (index == 2) {
			//点击内容->回复用户
			isToUser = true;
			if (state == 1) {
				commentGroupId = memoryInfos.get(memoryTag).getCommentorVos().get(position).getgId();
			} else {
				commentGroupId = groupId;
			}

			commentmId = memoryInfos.get(memoryTag).getCommentorVos().get(position).getmId();
			pointId = memoryInfos.get(memoryTag).getCommentorVos().get(position).getpId();
			if (TextUtils.isEmpty(pointId)) {
				pointId = memoryInfos.get(mPosition).getMemory().getMemoryId();
			}
			toUserId = memoryInfos.get(memoryTag).getCommentorVos().get(position).getuIdC();
			toUserName = memoryInfos.get(memoryTag).getCommentorVos().get(position).getUnameC();
			sendEt.setHint("回复 : " + toUserName);
			KeyBoardUtils.ShowKeyboard(sendEt);
		}
	}

	/**
	 * 点赞
	 *
	 * @param position
	 */
	private void onPraise(int position, int memoryTag) {
		Intent intent = new Intent();
		MemoryPraise praise = memoryInfos.get(memoryTag).getPraiserVos().get(position);
		if (praise.getpUsrId().equals(MainApplication.getUserId())) {
			//点赞人==当前用户
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
		intent.putExtra("userId", praise.getpUsrId());
		intent.putExtra("userName", praise.getUname());
		intent.putExtra("hPic", praise.getHphoto());
		startAnimActivity(intent);
	}

	/**
	 * 评论人跳转
	 *
	 * @param position
	 */

	private void onComment(int position, int memoryTag) {
		Intent intent = new Intent();
		MemoryComment comment = memoryInfos.get(memoryTag).getCommentorVos().get(position);
		if (comment.getuIdC().equals(MainApplication.getUserId())) {
			//评论人==当前用户
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
		intent.putExtra("userId", comment.getuIdC());
		intent.putExtra("userName", comment.getUnameC());
		intent.putExtra("hPic", comment.getUhphotoC());
		startAnimActivity(intent);
	}

	/**
	 * 预览图片
	 */
	@Override
	public void onPhotoPreivew(ArrayList<PhotoInfo> photoInfos, int index) {
		Intent intent = new Intent(mContext, PhotoManagerActivity.class);
		intent.putExtra("onlyWatch", true);
		intent.putExtra("curClick", index);
		intent.putParcelableArrayListExtra(PhotoManagerActivity.PHOTO_LIST, photoInfos);
		startAnimActivity(intent);
	}


	/**
	 * 跳转到片段
	 *
	 * @param position
	 */
	@Override
	public void startPoint(int position, MemoryInfo memoryInfo) {
		Intent intent = new Intent(mContext, MemoryPointActivity.class);
		intent.putExtra("memoryInfo", memoryInfo);
		intent.putExtra("curPosition", position);
		intent.putExtra("groupId", groupId);
		intent.putExtra("title", title);
		intent.putExtra("state", state);
		intent.putExtra("groupName", title);
		startActivityForResult(intent, ReqConstant.REQUEST_CODE_POINT);
	}


	/**
	 * 移除评论数据
	 *
	 * @param position
	 */
	@Override
	public void removeComment(int position, int memoryTag, String mPonitId, String mCMemoryId, String mpSrcId) {
		((MemoryInfosMPresenter) mPresenter).refreComment(memoryInfos, memoryTag, position, mPonitId, mCMemoryId, mpSrcId);
		refreshAllAdapter(position);
		//刷新
		upMemors(memoryTag);
	}

	/**
	 * 刷新
	 *
	 * @param position
	 */
	@Override
	public void refreshMemory(int position) {
		refreshAllAdapter(position);
		upMemors(position);
	}

	/**
	 * 添加评论数据
	 *
	 * @param comment
	 */
	@Override
	public void refreshComment(MemoryComment comment) {
		//正序排列
		memoryInfos.get(mPosition).getCommentorVos().add(comment);
		((MemoryInfosMPresenter) mPresenter).refreComment(memoryInfos.get(mPosition).getCommentorVos());
		refreshAdapter(2 + memoryInfos.get(mPosition).getMemoryPointVos().size(), memoryInfos.get(mPosition).getCommentorVos().size());
		//更新数据
		upMemors(mPosition);
	}

	/**
	 * 发送评论成功标志
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
	 * 设置结果
	 */
	public void setMyResylt() {
		KeyBoardUtils.HideKeyboard(sendEt);
		Intent intent = new Intent();
//		intent.putExtra("removeList", (Serializable) removeList);
		intent.putExtra("mlist", memories);
		intent.putExtra("glist", glist);
		intent.putExtra("list", list);
		intent.putExtra("curPage", ++curPage);//当前页自增
		intent.putExtra("isCanLoad", isCanLoad);//是否可以继续加载
		setResult(ReqConstant.REQUEST_CODE_DETAILS, intent);
		finish();
	}


	/**
	 * 刷新Adapter(增加)
	 */
	private void refreshAdapter(int posion) {
		View view = swipe_target.getChildAt(mPosition);
		if (view != null) {
			BaseHolder viewHolder = (BaseHolder) swipe_target.getChildViewHolder(view);
			viewHolder.notifyItem(posion);
		}
	}

	/**
	 * 刷新Adapter(增加)
	 */
	private void refreshAdapter(int posion, int count) {
		View view = swipe_target.getChildAt(mPosition);
		if (view != null) {
			BaseHolder viewHolder = (BaseHolder) swipe_target.getChildViewHolder(view);
			viewHolder.notifyAddItem(posion, count);
		}
	}

	/**
	 * 刷新Adapter(All)
	 */
	private void refreshAllAdapter(int posion) {
		if (swipe_target.getChildCount() != 0) {
			View view = swipe_target.getChildAt(0);
			if (view != null) {
				BaseHolder viewHolder = (BaseHolder) swipe_target.getChildViewHolder(view);
				viewHolder.notifyAllItem(posion);
			}
		}
	}

	/**
	 * 更多的弹窗
	 */
	private void showMoreDialog(boolean isShared) {
		boolean isEdit = false;
		if (state == 1) {
			//我的
			isEdit = true;
		} else if (state == 0) {
			//他的
			if (MainApplication.getUserId().equals(list.get(mPosition).getUserIdF())) {
				isEdit = true;
			}
		} else {
			//群的
			if (MainApplication.getUserId().equals(glist.get(mPosition).getUserIdG())) {
				isEdit = true;
			}
		}
		if (!TextUtils.isEmpty(memoryInfos.get(mPosition).getMemoryPointVos().get(0).getDetail())) {
			contents = memoryInfos.get(mPosition).getMemoryPointVos().get(0).getDetail();
		} else {
			contents = "快去看看吧！";
		}
		//我的记忆
		MemoryMoreSheet.createBuilder(MemoryDetailActivtiy.this, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				isEdit(isEdit).
				isShared(isShared).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setListener(new MemoryMoreSheet.onMemoryMoreListener() {
					@Override
					public void onSubmit(int postion) {
						if (postion == 8) {
							//转发
							Intent intent = new Intent(mContext, ForwardActivity.class);
							intent.putExtra("memoryId", getgMemoryIdSource());
							intent.putExtra("state", state);
							intent.putExtra("groupId", getGroupId());
							intent.putExtra("isowen", getgMemoryOwen());
							intent.putExtra("type", getType());
							startActivityForResult(intent, ReqConstant.REQUEST_CODE_FORWARD);
						} else if (postion == 7) {
							//删除记忆
							deleteMemory();
						} else if (postion == 6) {
							//举报
							reportUser(-1, false);
						} else if (postion == 5) {
							//编辑记忆
							Intent intent = new Intent(mContext, EditMActivity.class);
							intent.putExtra("memoryInfo", memoryInfos.get(mPosition));
//							intent.putExtra("state", state);
//							intent.putExtra("Id", groupId);
							startActivityForResult(intent, ReqConstant.REQUEST_CODE_EDIT);
						} else {
							sharedPosition = postion;
							requestWriterPermission();
						}
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
		final String p1Image = memoryInfos.get(mPosition).getMemoryPointVos().get(0).getP1();
		CLog.e(TAG, "imgOss + p1Image + imgPath:" + (imgOss + p1Image + imgPath));
		if (postion == 4) {
			if (state == 1) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.sinoShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + memories.get(mPosition).getMemoryId());
				} else {
					shareUtil.sinoShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + memories.get(mPosition).getMemoryId());
				}
			} else if (state == 0) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.sinoShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + list.get(mPosition).getMemoryId());
				} else {
					shareUtil.sinoShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + list.get(mPosition).getMemoryId());
				}
			} else if (state == 2) {
				// 我刚在《高三九班》写了一篇记忆《认真钓鱼的笑笑》
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.sinoShare(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + glist.get(mPosition).getMemoryId());
				} else {
					shareUtil.sinoShareInt(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + glist.get(mPosition).getMemoryId());
				}
			}
		} else if (postion == 3) {
			if (state == 1) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.qqSpaceShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + memories.get(mPosition).getMemoryId());
				} else {
					shareUtil.qqSpaceShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + memories.get(mPosition).getMemoryId());
				}
			} else if (state == 0) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.qqSpaceShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + list.get(mPosition).getMemoryId());
				} else {
					shareUtil.qqSpaceShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + list.get(mPosition).getMemoryId());
				}
			} else if (state == 2) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.qqSpaceShare(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + glist.get(mPosition).getMemoryId());
				} else {
					shareUtil.qqSpaceShareInt(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + glist.get(mPosition).getMemoryId());
				}
			}
		} else if (postion == 2) {
			if (state == 1) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.qqFriendShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + memories.get(mPosition).getMemoryId());
				} else {
					shareUtil.qqFriendShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + memories.get(mPosition).getMemoryId());
				}
			} else if (state == 0) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.qqFriendShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + list.get(mPosition).getMemoryId());
				} else {
					shareUtil.qqFriendShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + list.get(mPosition).getMemoryId());
				}
			} else if (state == 2) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.qqFriendShare(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + glist.get(mPosition).getMemoryId());
				} else {
					shareUtil.qqFriendShareInt(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + glist.get(mPosition).getMemoryId());
				}
			}
		} else if (postion == 1) {
			if (state == 1) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.friendsCircleShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + memories.get(mPosition).getMemoryId());
				} else {
					shareUtil.friendsCircleShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + memories.get(mPosition).getMemoryId());
				}
			} else if (state == 0) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.friendsCircleShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + list.get(mPosition).getMemoryId());
				} else {
					shareUtil.friendsCircleShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + list.get(mPosition).getMemoryId());
				}
			} else if (state == 2) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.friendsCircleShare(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + glist.get(mPosition).getMemoryId());
				} else {
					shareUtil.friendsCircleShareInt(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + glist.get(mPosition).getMemoryId());
				}
			}
		} else if (postion == 0) {
			if (state == 1) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.wXShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + memories.get(mPosition).getMemoryId());
				} else {
					shareUtil.wXShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + memories.get(mPosition).getMemoryId());
				}
			} else if (state == 0) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.wXShare(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + list.get(mPosition).getMemoryId());
				} else {
					shareUtil.wXShareInt(MemoryDetailActivtiy.this, "我刚写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + list.get(mPosition).getMemoryId());
				}
			} else if (state == 2) {
				if (!TextUtils.isEmpty(p1Image)) {
					shareUtil.wXShare(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, imgOss + p1Image + imgPath, url + glist.get(mPosition).getMemoryId());
				} else {
					shareUtil.wXShareInt(MemoryDetailActivtiy.this, "我刚在«" + title + "»写了一篇记忆", memoryInfos.get(mPosition).getMemory().getTitle(), contents, R.drawable.headpic, url + glist.get(mPosition).getMemoryId());
				}
			}
		}
	}

	/**
	 * 评论弹窗
	 *
	 * @param position
	 * @param title
	 */
	private void showCommentsDialog(final int memoryTag, final int position, final int opr, String... title) {
		ActionSheet.createBuilder(MemoryDetailActivtiy.this, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				setOtherButtonTitles(title).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setCancelButtonTitle("取消").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						if (opr == -1) {
							//删除
							((MemoryInfosMPresenter) mPresenter).removeComment(getString(R.string.FSREMOVECOMMENT), MainApplication.getUserToken(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getcId(), memoryInfos.get(memoryTag).getMemory().getMemoryId(), groupId, position, memoryTag, memoryInfos.get(memoryTag).getCommentorVos().get(position).getuIdC(), MainApplication.getUserId(), String.valueOf(state), memoryInfos.get(memoryTag).getMemory().getMemoryIdSource(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getpId(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getmId(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getMpSrcId());
						} else if (opr == 1) {
							//举报
							reportUser(position, true);
						} else {
							if (index == 0) {
								//删除
								((MemoryInfosMPresenter) mPresenter).removeComment(getString(R.string.FSREMOVECOMMENT), MainApplication.getUserToken(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getcId(), memoryInfos.get(memoryTag).getMemory().getMemoryId(), groupId, position, memoryTag, memoryInfos.get(memoryTag).getCommentorVos().get(position).getuIdC(), MainApplication.getUserId(), String.valueOf(state), memoryInfos.get(memoryTag).getMemory().getMemoryIdSource(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getpId(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getmId(), memoryInfos.get(memoryTag).getCommentorVos().get(position).getMpSrcId());
							} else {
								//举报
								reportUser(position, true);
							}
						}
					}
				}).show();
	}


	/**
	 * 举报
	 *
	 * @param position  评论位置
	 * @param isComment 对评论操作
	 */
	private void reportUser(final int position, final boolean isComment) {
		ActionSheet.createBuilder(MemoryDetailActivtiy.this, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setCancelButtonTitle("取消").
				setOtherButtonTitles("色情/暴力信息", "广告信息", "钓鱼/欺诈信息", "诽谤造谣信息").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						((MemoryInfosMPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), getgMemoryId(), "", "", "2", String.valueOf(index + 1));
					}
				}).show();
	}

	/**
	 * 获取记忆Id
	 *
	 * @return
	 */
	private String getgMemoryId() {
		String memoryId = null;
		memoryId = memoryInfos.get(mPosition).getMemory().getMemoryIdSource();
		if (state == 1) {
		} else if (state == 0) {
			memoryId = list.get(mPosition).getMemoryId();
		} else {
			//群的
			memoryId = glist.get(mPosition).getMemoryId();
		}
		return memoryId;
	}

	/**
	 * 获取记忆Id源Id
	 *
	 * @return
	 */
	private String getgMemoryIdSource() {
		return memoryInfos.get(mPosition).getMemory().getMemoryIdSource();
	}

	/**
	 * 获取是私密还是包含圈子
	 *
	 * @return
	 */
	private boolean getgMemoryOwen() {
		boolean isOwen = false;
		if (state == 1) {
			if (memoryInfos.get(mPosition).getMemory().getGroupNameList() == null || memoryInfos.get(mPosition).getMemory().getGroupNameList().isEmpty())
				isOwen = true;
		}
		return isOwen;
	}


	/**
	 * 转发赛选
	 *
	 * @return
	 */
	private String getType() {
		String type = null;
		//我的记忆
		if (state == 1)
			type = memories.get(mPosition).getState();
		else if (state == 0)
			type = list.get(mPosition).getState();
		else {
			//群
			type = glist.get(mPosition).getState();
		}
		return type;
	}


	/**
	 * 转发赛选
	 *
	 * @return
	 */
	private String getGroupId() {
		String mGroupId;
		if (state == 1) {
			//我的记忆
			mGroupId = "";
		} else if (state == 0) {
			//type
			mGroupId = "";
		} else {
			//群的记忆
			mGroupId = groupId;
		}
		return mGroupId;
	}

	/**
	 * 删除记忆
	 */
	private void deleteMemory() {
		String userId = MainApplication.getUserId();
		String memoryId = null;
		String type = null;
		if (state == 1) {
			//我的记忆
			memoryId = memories.get(mPosition).getMemoryId();
			type = memories.get(mPosition).getState();
			groupId = memories.get(mPosition).getGroupId();
		} else if (state == 0) {
			//他的
			memoryId = list.get(mPosition).getMemoryId();
		} else {
			//群的
			memoryId = glist.get(mPosition).getMemoryId();
		}
		deleteMemroyDialog(userId, memoryId, mPosition, type);
	}

	/**
	 * 删除记忆
	 */
	private void deleteMemroyDialog(final String userId, final String memoryId, final int mPosition, final String type) {
		DialogUtils.request(MemoryDetailActivtiy.this, "确定要删除此记忆吗?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isSure = (boolean) data;
				if (isSure) {
					//确定删除
					((MemoryInfosMPresenter) mPresenter).removeMemory(getString(R.string.FSDELETEMEMORY), MainApplication.getUserToken(), userId, memoryId, "", "", groupId, mPosition, state, type, "", String.valueOf(state), memoryInfos.get(mPosition).getMemory().getMemoryIdSource(), "");
				}
			}
		});
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		if (requestCode == ReqConstant.REQUEST_CODE_POINT) {
			//片段返回
			if (data != null) {
				MemoryInfo memoryInfo = data.getParcelableExtra("memoryInfo");
				if (memoryInfo == null) {
					removeMemory(mPosition, state);
					return;
				}
				if (state == 1)
					((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSMYMEMORYINFO), memories.get(mPosition).getMemoryId(), memories.get(mPosition).getMemorySrcId(), "CD01", state, mPosition, false, -1, memoryInfos.get(mPosition));
				else if (state == 0)
					((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSOTHERMEMORYINFO), list.get(mPosition).getMemoryId(), null, "CD02", state, mPosition, false, -1, null);
				else
					((MemoryInfosMPresenter) mPresenter).getMemoryInfos(getString(R.string.FSGROUPMEMORYINFO), glist.get(mPosition).getMemoryId(), null, "CD03", state, mPosition, false, -1, memoryInfos.get(mPosition));
			}
		} else if (requestCode == ReqConstant.REQUEST_CODE_FORWARD) {
			//转发返回
			if (data != null) {
				boolean isRemove = data.getBooleanExtra("isRemove", true);
				String groupName = data.getStringExtra("groupName");
				if (isRemove)
					removeMemory(mPosition, state);
				else {
					if (state == 1)
						addGroup(groupName);
				}
			}
		} else if (requestCode == ReqConstant.REQUEST_CODE_WRITE) {
			//更改权限返回
			requestWriterPermission();
		} else {
			UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
		}
	}

}