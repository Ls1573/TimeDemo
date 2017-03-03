package com.time.memory.view.holder;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.WriterStyleMemory;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:写记忆
 * @date 2016-10-6下午3:09:16
 * ==============================
 */
public class WriterStyleHolder extends BaseHolder<WriterStyleMemory> implements TextWatcher {

	@Bind(R.id.writer_date)
	TextView writer_date;//日期
	@Bind(R.id.writer_sign_tv)
	TextView writer_sign_tv;//提示语
	@Bind(R.id.writer_tag_tv)
	TextView writer_tag_tv;//贴记忆签
	@Bind(R.id.writer_style_et)
	EditText writer_style_et;//主题

	private String top;
	private String end;

	private WriterStyleMemory mWriterStyleMemory;

	public WriterStyleHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		writer_style_et.addTextChangedListener(this);
		top = "当前记忆发布之后,仅 ";
		end = " 可见";
	}

	@Override
	public void setData(WriterStyleMemory entity) {
		super.setData(entity);
		mWriterStyleMemory = entity;
		writer_date.setText(entity.getMemoryDate());
		writer_style_et.setText(entity.getTitle());
		writer_tag_tv.setText(entity.getLabelName());

		if (TextUtils.isEmpty(entity.getSign())) {
			writer_sign_tv.setVisibility(View.GONE);
		} else {
			writer_sign_tv.setVisibility(View.VISIBLE);
			setOnHref(top, entity.getSign(), end);
		}
	}

	@OnClick({R.id.writer_date, R.id.writer_tag_tv})
	public void onClick(View view) {
		if (view.getId() == R.id.writer_date) {
			//创建日期
			mHolderCallBack.onClick(-1, 1);
		} else if (view.getId() == R.id.writer_tag_tv) {
			//贴记忆签
			mHolderCallBack.onClick(-1, 2);
		}
	}

	/**
	 * 设置超链接
	 *
	 * @param top-头部信息
	 * @param uName-被回复人
	 * @param end-尾部信息
	 */
	private void setOnHref(String top, String uName, String end) {
		SpannableString spanttt = new SpannableString(uName);
		ClickableSpan clickttt = new MClickableSpan();

		spanttt.setSpan(clickttt, 0, uName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		writer_sign_tv.setText(top);
		writer_sign_tv.append(spanttt);
		writer_sign_tv.append(end);
		writer_sign_tv.setMovementMethod(LinkMovementMethod.getInstance());

		//设置高亮背景,注释掉后会有默认选中背景色
		writer_sign_tv.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
	}

	/**
	 * 超链接点击
	 */
	class MClickableSpan extends ClickableSpan {
		public MClickableSpan() {
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			//链接字体颜色-黄
			ds.setColor(mContext.getResources().getColor(R.color.yellow_DE));
		}

		@Override
		public void onClick(View widget) {
			//点击了链接
			mHolderCallBack.onClick(-1, 3);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		//文字变化
		mWriterStyleMemory.setTitle(s.toString().trim());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}


}
