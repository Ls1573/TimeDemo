package com.time.memory.view.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Message;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:待处理事项
 * @date 2016/10/15 16:13
 */
public class MessageDealHolder extends BaseHolder<Message> {

	private static final String TAG = "MessageDealHolder";
	@Bind(R.id.message_pic_iv)
	ImageView message_pic_iv;//图片
	@Bind(R.id.message_title_tv)
	TextView message_title_tv;//题头
	@Bind(R.id.message_new_tv)
	TextView message_new_tv;//内容
	@Bind(R.id.message_through_tv)
	TextView message_through_tv;//通过
	@Bind(R.id.message_accept_tv)
	TextView message_accept_tv;//接受
	@Bind(R.id.message_refuse_tv)
	TextView message_refuse_tv;//拒绝
	@Bind(R.id.message_hasaccept_tv)
	TextView message_hasaccept_tv;//已接受

	private String imgWeb;//头像拼接
	private int mPosition;
	private Message mEntity;

	public MessageDealHolder(View view) {
		super(view);
		imgWeb = mContext.getString(R.string.FSIMAGEPATH);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(Message entity, int position) {
		super.setData(entity);
		this.mPosition = position;
		this.mEntity = entity;
		//有图片
		if (!TextUtils.isEmpty(entity.getSendUserPhoto())) {
			Picasso.with(mContext).load(imgWeb + entity.getSendUserPhoto()).resize(300, 300).centerCrop().placeholder(R.drawable.notice).error(R.drawable.notice).into(message_pic_iv);
		}
		//题头
		message_title_tv.setText(entity.getSendUserName());
		//内容
		if (!TextUtils.isEmpty(entity.getMsgType())) {
			message_hasaccept_tv.setVisibility(View.VISIBLE);
			message_through_tv.setVisibility(View.GONE);
			message_accept_tv.setVisibility(View.GONE);
			message_refuse_tv.setVisibility(View.GONE);
		} else {
			String type = entity.getMessageType();
			String info;
			if (type.equals("90")) {
				//好友申请
				info = "向你提出好友申请";
				message_new_tv.setText(info);
				message_through_tv.setVisibility(View.GONE);
				message_accept_tv.setVisibility(View.VISIBLE);
			} else if (type.equals("91")) {
				//圈子申请
				String msg[] = entity.getMessageDetail().split("&spt;");
				message_new_tv.setText(Html.fromHtml("TA申请加入<font color=#2C2C2C>" + msg[1] + "</font>"));
				message_through_tv.setVisibility(View.VISIBLE);
				message_accept_tv.setVisibility(View.GONE);
			}
		}
	}

	@OnClick({R.id.message_through_tv, R.id.message_accept_tv, R.id.message_refuse_tv, R.id.message_pic_iv})
	public void onClick(View view) {
		if (view.getId() == R.id.message_refuse_tv) {
			//拒绝
			mHolderCallBack.onClick(mPosition, -1);
		} else if (view.getId() == R.id.message_accept_tv) {
			//接受
			mHolderCallBack.onClick(mPosition, 1);
		} else if (view.getId() == R.id.message_through_tv) {
			//通过
			mHolderCallBack.onClick(mPosition, 2);
		} else if (view.getId() == R.id.message_pic_iv) {
			//头像
			if (mEntity.getMessageType().equals("90"))
				mHolderCallBack.onClick(mPosition, 3);
		}
	}
}
