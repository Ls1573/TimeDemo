package com.time.memory.view.holder;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.Message;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:消息条目
 * @date 2016/9/12 10:37
 * ==============================
 */
public class MessageHolder extends BaseHolder<Message> {

	private static final String TAG = "MessageHolder";
	@Bind(R.id.message_rl)
	RelativeLayout message_rl;//外圈
	@Bind(R.id.message_pic_iv)
	ImageView message_pic_iv;//  信息图片
	@Bind(R.id.message_new_iv)
	ImageView message_new_iv;// 未读消息提示
	@Bind(R.id.message_new_tv)
	TextView message_new_tv;//新消息内容
	@Bind(R.id.message_title_tv)
	TextView message_title_tv;//新消息头
	@Bind(R.id.message_date_tv)
	TextView message_date_tv;//新消息日期
	@Bind(R.id.message_num_tv)
	TextView message_num_tv;//新消息数量

	private String imgPath;
	private String imgOSS;

	public MessageHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
		imgOSS = mContext.getString(R.string.FSIMAGEOSS);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void setData(Message entity, final int position) {
		super.setData(entity);
		if (entity.getMessageType().equals("unMessage")) {
			//未读的提示头
			message_pic_iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unread_pic));
			message_rl.setBackgroundColor(mContext.getResources().getColor(R.color.grey_F6));
			//消息提示头
			message_title_tv.setText(mContext.getString(R.string.message_unRead_tv));
			//内容
			//您有%1$s个待处理事项
			message_new_tv.setText(Html.fromHtml("您有<font color=#E34D4C>" + entity.getMessageDetail() + "</font>个待处理事项"));
		} else {
			message_rl.setBackground(mContext.getResources().getDrawable(R.drawable.mine_ll));
			//日期
			message_date_tv.setText(entity.getInsDate());
			//消息提示头
			message_title_tv.setText(entity.getSendUserName());
			//内容
			String type = entity.getMessageType();
			String info = null;
			if (type.equals("01")) {
				//添加好友
				info = entity.getMessageDetail() + "已经通过了你的好友申请，我们是好友啦！";
			} else if (type.equals("02")) {
				// 加入圈子
				info = "邀请您加入编辑部【" + entity.getMessageDetail() + "】,快去看看吧！";
			} else if (type.equals("03")) {
				info = "您已经被编辑部【" + entity.getMessageDetail() + "】移除";
			} else if (type.equals("04")) {
				try {
					String user[] = entity.getMessageDetail().split("&spt;");
					info = user[0] + "已加入编辑部【" + "user[1]" + "】";
				} catch (Exception e) {
					info = entity.getMessageDetail() + "已加入圈子";
				}

			}
			message_new_tv.setText(info);
			//图片
			if (!TextUtils.isEmpty(entity.getSendUserPhoto())) {
				Picasso.with(mContext).load(imgPath + entity.getSendUserPhoto() + imgOSS).config(Bitmap.Config.RGB_565).placeholder(R.drawable.notice).error(R.drawable.notice).into(message_pic_iv);
			} else {
				message_pic_iv.setImageResource(R.drawable.notice);
			}
			//单击
			mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mHolderCallBack.onClick(position);
				}
			});

			//联系人->长点击
			mView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					mHolderCallBack.onLongClick(position);
					return true;
				}
			});


		}
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);
			}
		});
	}
}
