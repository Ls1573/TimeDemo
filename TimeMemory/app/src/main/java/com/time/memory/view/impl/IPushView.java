package com.time.memory.view.impl;

import com.time.memory.entity.MyPush;

/**
 * V-P
 */
public interface IPushView extends IBaseView {

	void setMessage(MyPush push);

	void updatePush(int position);

}
