package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Message;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/9/12
 * ==============================
 */
public interface IMessageController {
	long selectMesages();

	List<Message> getMessages(String userId);

	Object getMessages(String userId, String msgId);

	long getUnReadedMessage(String userId);

	void deleteBykey(String Id);

	void reqMessage(String url, SimpleCallback callback);

	void setRead(String url, String userToken, SimpleCallback callback);

	void updateMessage(List<Message> list);

	void saveMesageList(List<Message> list);

	void reqAccept(String url, String userId, SimpleCallback callback);

	void reqThrough(String url, String userId, String groupId, SimpleCallback callback);

	void addMessage(Message msg);

	void upMessage(Message msg);
}
