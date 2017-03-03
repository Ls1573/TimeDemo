package com.time.memory.core.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import com.time.memory.R;

/**
 * @author Qiu
 * @version V1.0
 * @Description:计时器
 * @date 2016/10/8 14:27
 */
public class TimeCount extends CountDownTimer {
	private TextView view;
	private Context context;

	// 总的时间millisInFuture，一个是countDownInterval
	public TimeCount(long millisInFuture, long countDownInterval, TextView view, Context context) {
		super(millisInFuture, countDownInterval);
		this.view = view;
		this.context = context;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		view.setClickable(false);// 设置不能点击
		view.setText("(" + millisUntilFinished / 1000 + "s)后重发");// 设置倒计时时间
		// 设置按钮为灰色，这时是不能点击的
		Spannable span = new SpannableString(view.getText().toString());
		// 字体颜色是淡色的
		view.setTextColor(context.getResources().getColor(R.color.browen_B3));
		// mBtnVerify.setBackgroundResource(android.R.color.transparent);
		view.setText(span);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onFinish() {
//		view.setText("重新获取\n验证码");
		view.setText("重新获取");
		view.setEnabled(true);
		view.setClickable(true);
		// 字体颜色
		view.setTextColor(context.getResources().getColor(R.color.browen));
		// mBtnVerify.setBackgroundColor(android.R.color.transparent);
	}
}
