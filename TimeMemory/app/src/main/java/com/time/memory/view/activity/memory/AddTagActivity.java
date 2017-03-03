package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.Lable;
import com.time.memory.gui.TagView;
import com.time.memory.presenter.AddTagPresenter;
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
 * @author Qiu
 * @version V1.0
 * @Description:选记忆签
 * @date 2016/9/26 9:22
 */
public class AddTagActivity extends BaseActivity implements IAddTagView, TagView.OnTagClickListener, TextView.OnEditorActionListener {

	private static final String TAG = "AddTagActivity";
	private static final int TRANSLATE_DURATION = 200;
	@Bind(R.id.tv_main_right)
	TextView tvRight;//右标题
	@Bind(R.id.app_submit)
	TextView app_submit;//提交
	@Bind(R.id.add_tag)
	TagView tagView;
	@Bind(R.id.add_tag_tv)
	TextView add_tag_tv;
	@Bind(R.id.tv_main_title)
	TextView tv_main_title;
	@Bind(R.id.tag_et)
	EditText tagEt;//输入框
	@Bind(R.id.tag_input_rl)
	RelativeLayout tag_input_rl;//标签外框


	private boolean isEdit;//编辑|发布
	private List<Lable> mTags;//标签
	private String Id;//ID
	private boolean ismemory;//记忆
	private boolean isRetuen;//返回
	private int type;//类别(1:个人;2:圈子)

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_addtag);
	}

	@Override
	public void initView() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new AddTagPresenter();
	}

	@Override
	public void initData() {
		//分用户和圈子
		Id = getIntent().getStringExtra("Id");
		type = getIntent().getIntExtra("state", 0);
		ismemory = getIntent().getBooleanExtra("ismemory", false);
		isRetuen = getIntent().getBooleanExtra("isRetuen", true);

		tagView.setOnTagClickListener(this);
		tagEt.setOnEditorActionListener(this);
		tvRight.setEnabled(false);

		if (!ismemory)
			tv_main_title.setText(getString(R.string.app_addtag));
		else
			tv_main_title.setText(getString(R.string.app_tagmanager));

		((AddTagPresenter) mPresenter).getTags(Id, type);
	}

	@OnClick({R.id.app_cancle, R.id.tv_main_right, R.id.app_submit})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_cancle:
				setMyResult(ismemory ? ReqConstant.ADDTAG : RESULT_CANCELED, null);
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
	}

	@Override
	public void showAddTagView(boolean isShow) {
		//显示追加按钮
		app_submit.setVisibility(isShow ? View.VISIBLE : View.GONE);
		add_tag_tv.setVisibility(isShow ? View.GONE : View.VISIBLE);
	}

	@Override
	public void removeTagSuccess(int position) {
		//删除
		mTags.remove(position);
	}

	@Override
	public void addTagView(Lable tag) {
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
		//返回结果
		if (isRetuen)
			setMyResult(ReqConstant.ADDTAG, mTags.get(position));
	}

	@Override
	public void onIvClick(Integer position) {
		//点击了删除
		CLog.e(TAG, "position:" + position);
		tagView.removeView(position);
	}

	/**
	 * TAGView删除回调
	 *
	 * @param index
	 */
	@Override
	public void onRemoveIndex(int index) {
		CLog.e(TAG, "index:" + index + "   " + mTags.get(index).getLabelName() + "    " + mTags.get(index).getLabelId() + "  mTags:" + mTags.size());
		((AddTagPresenter) mPresenter).removeTag(Id, type, mTags.get(index).getLabelId(), index);
	}

	@Override
	public void addTag(Lable lable) {
		if (mTags == null)
			mTags = new ArrayList<>();
		this.mTags.add(lable);
		hideAddTag();
		tagView.addView(lable, isEdit);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			//发送评论-回复记忆体
			String tag = tagEt.getText().toString();
			((AddTagPresenter) mPresenter).addTag(tag, Id, type, mTags);
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

	@Override
	public void onBackPressed() {
		setMyResult(ismemory ? ReqConstant.ADDTAG : RESULT_CANCELED, null);
		super.onBackPressed();
	}

	/**
	 * 设置我的返回结果
	 */
	private void setMyResult(int resultCode, Lable msg) {
		hideAddTag();
		Intent intent = new Intent();
		intent.putExtra("lable", msg);
		setResult(resultCode, intent);
		finish();
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
