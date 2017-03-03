package com.time.memory.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.circle.ChooseFriendActivity;
import com.time.memory.view.impl.ICreateView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:新建圈子
 * @date 2016/10/12 11:35
 */
public class CreateCirclePresenter extends BasePresenter<ICreateView> {
	private static final String TAG = "MessagePresenter";

	public CreateCirclePresenter() {
	}

	/**
	 * 选择好友
	 */
	public void choseFriend(String title,boolean isCircle) {
		if (mView == null) return;
		if (TextUtils.isEmpty(title)) {
			mView.showShortToast("请输入圈子名称");
			return;
		}
		if (title.length() < 2) {
			mView.showShortToast("名称长度在2~10个字符,请重新输入");
			return;
		}
		Intent intent = new Intent(context, ChooseFriendActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("isCircle", isCircle);
		mView.success(intent);
	}

}
