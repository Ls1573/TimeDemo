package com.time.memory.view.activity.circle;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Lable;
import com.time.memory.gui.TagView;
import com.time.memory.presenter.GroupTagPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IAddTagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:群标签管理
 * @date 2016/10/24 19:26
 */
public class GroupTagActivity extends BaseActivity implements IAddTagView, TagView.OnTagClickListener, TextView.OnEditorActionListener {

	private static final String TAG = "GroupTagActivity";
	private static final int TRANSLATE_DURATION = 200;
	@Bind(R.id.tv_main_right)
	TextView tvRight;//右标题
	@Bind(R.id.app_submit)
	TextView app_submit;//提交
	@Bind(R.id.add_tag)
	TagView tagView;
	@Bind(R.id.tag_et)
	EditText tagEt;//输入框
	@Bind(R.id.tag_input_rl)
	RelativeLayout tag_input_rl;//标签外框
	@Bind(R.id.tv_main_title)
	TextView tvMain;//标签管理


	private boolean isEdit;//编辑|发布
	private List<Lable> mTags;//标签
	private String Id;//ID
	private String tag;//输入的tag
	private boolean isRetuen;//返回

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_addtag);
	}

	@Override
	public void initView() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new GroupTagPresenter();
	}

	@Override
	public void initData() {
		//分用户和圈子
		Id = getIntent().getStringExtra("Id");
		isRetuen = getIntent().getBooleanExtra("isRetuen", true);

		tagView.setOnTagClickListener(this);
		tagEt.setOnEditorActionListener(this);
		tvRight.setEnabled(false);

		if (isRetuen) {
			tvMain.setText(getString(R.string.app_addtag));
		} else {
			tvMain.setText(getString(R.string.app_tagmanager));
		}

		((GroupTagPresenter) mPresenter).getGroupTags(Id);
	}


	@OnClick({R.id.app_cancle, R.id.tv_main_right, R.id.app_submit})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_cancle:
				hideAddTag();
				finish();
				break;
			case R.id.tv_main_right:
				//编辑
				isEdit();
				break;
			case R.id.app_submit:
				//创建
				showAddTag();
				break;
		}
	}

	@Override
	public void setTags(List<Lable> tags) {
		this.mTags = tags;
		tagView.setTags(mTags, false);
		tvRight.setEnabled(true);
	}

	@Override
	public void showAddTagView(boolean isShow) {
		//显示追加按钮
		app_submit.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	@Override
	public void removeTagSuccess(int position) {
		//删除
		mTags.remove(position);
	}

	/**
	 * 编辑&发布切换
	 */
	private void isEdit() {
		isEdit = !isEdit;
		tagView.setVisable(isEdit);
		tvRight.setText(!isEdit ? getString(R.string.app_eddit) : getString(R.string.app_cancle));
		if (isEdit) hideAddTag();
	}

	@Override
	public void onTagClick(int position) {
		//点击了标签
		CLog.e(TAG, "onTagClick:" + position);
		if (position == -1) return;
	}

	@Override
	public void onIvClick(Integer position) {
		//点击了删除
		tagView.removeView(position);
	}

	/**
	 * TagView删除回调
	 *
	 * @param index
	 */
	@Override
	public void onRemoveIndex(int index) {
		CLog.e(TAG, "index:" + index);
		((GroupTagPresenter) mPresenter).removeTag(getString(R.string.FSREMOVEGROUPTAG), mTags.get(index).getLabelId(), index);
	}

	/**
	 * 添加tagView
	 */
	@Override
	public void addTagView(Lable lable) {
		if (mTags == null)
			mTags = new ArrayList<>();
		this.mTags.add(lable);
		hideAddTag();
		tagView.addView(lable, isEdit);
	}


	@Override
	public void addTag(Lable lable) {
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			//回复
			String tag = tagEt.getText().toString();
			((GroupTagPresenter) mPresenter).reqAddTag(getString(R.string.FSREADDGROUPTAG), Id, tag, mTags);
		}
		return true;
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		tvRight.setEnabled(true);
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	/**
	 * 新增标签
	 */
	private void showAddTag() {
		tag_input_rl.setVisibility(View.VISIBLE);
		tag_input_rl.startAnimation(createTranslationInAnimation());
		KeyBoardUtils.ShowKeyboard(tagEt);
	}

	/**
	 * 退出
	 */
	private void hideAddTag() {
		tagEt.setText("");
		KeyBoardUtils.HideKeyboard(tagEt);
		tag_input_rl.setVisibility(View.GONE);
//		tag_input_rl.startAnimation(createTranslationOutAnimation());
	}

	// 创建进入动画
	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}


	// 创建退出位移动画
	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}
}
