package com.time.memory.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.time.memory.R;

/**
 * 自定义进度条
 *
 * @author Qiu
 */
public class CustomClipLoading extends ProgressDialog {
	private AnimationDrawable mAnimation;
	// 上下文
	private Context mContext;
	//
	private ImageView mImageView;
	// animationlist的 id
	private int mResid;

	/**
	 * @param context 上下文对象
	 * @param id      动画id
	 */
	public CustomClipLoading(Context context, int id) {
		super(context, R.style.dialog);
		this.mContext = context;
		this.mResid = id;
		// 设置在窗口的边界之外时，该对话框是否被取消(true-取消)
		setCanceledOnTouchOutside(false);
	}

	public CustomClipLoading(Context context) {
		super(context);
	}

	// 设置在窗口的边界之外时，该对话框是否被取消(true-取消)
	public void setOnOutSide(boolean state) {
		setCanceledOnTouchOutside(state);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化页面
		initView();
		// 初始化数据
		initData();
	}

	// 初始化数据
	private void initData() {
		// 设置图片资源
		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 使Runnable被添加到消息队列。运行在UI主线程中
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});
	}

	// 初始化dialog显示内容
	private void initView() {
		// 显示界面
		setContentView(R.layout.view_progress_dialog);
		// 显示的图片
		mImageView = (ImageView) findViewById(R.id.loadingIv);
	}

}
