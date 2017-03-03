package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:好友操作
 * @date 2016/10/14 13:46
 */
public interface IFriendController {

	void reqFriendInfo(String url, String userId, SimpleCallback call);

	void reqAddFriend(String url, String userId, SimpleCallback call);

	void reqFriends(String url, String userMobile, SimpleCallback callback);

	Object getContactsById(String userId, String comeFromId);
}
