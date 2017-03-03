package com.time.memory.view.impl;

import com.time.memory.entity.MGroup;

/**
 * V-P
 */
public interface IScanView extends IBaseView {

	void startGroup(MGroup MGroup);

	void sacnError();

	void startFriend(String userId,String uName);

	void repleyFriend(String userId,String uName,String hPic);
}
