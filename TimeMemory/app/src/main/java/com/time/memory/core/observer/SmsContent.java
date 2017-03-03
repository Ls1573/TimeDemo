package com.time.memory.core.observer;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

import com.time.memory.util.CLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:监听短信内容
 * @date 2016/11/14 8:07
 */
public class SmsContent extends ContentObserver {

	public static final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱的uri
	private static final String TAG = "SmsContent";
	private final String SMS_CONTENT = "时光记忆";// 拦截关键字
	private Activity activity = null;
	private String smsContent = "";
	private EditText verifyText = null;

	public SmsContent(Activity activity, Handler handler, EditText verifyText) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		try {
			Cursor cursor = null;// 光标
			// 读取收件箱中指定号码的短信
			cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX),
					new String[]{"address", "body", "person"}, null, null,
					"date desc");
			if (cursor != null) {// 如果短信为未读模式
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					// 拿到内容
					String smsbody = cursor.getString(cursor
							.getColumnIndex("body"));
					// 包含指定内容
					if (smsbody.indexOf(SMS_CONTENT) > 0) {
						// 6位
						String regEx = "[0-9]{6}";
						// 生成一个正则表达式
						Pattern p = Pattern.compile(regEx);
						// 创建匹配给定输入与此模式的匹配器。
						Matcher m = p.matcher(smsbody.toString());
						String res = "";// 结果
						if (m.find()) {// 如果匹配存在
							res = m.group().substring(0, 6);// 返回由以前匹配操作所匹配的输入子序列。(4位)
						}
						CLog.d(TAG, "res:" + res);
						smsContent = res.trim().toString();// 截取出验证码
						verifyText.setText(res);// EditText赋值
						cursor.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
