package com.time.memory.presenter;

import com.time.memory.entity.Appoint;
import com.time.memory.model.impl.IAppointController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IAppointView;

import java.util.List;


/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:指定圈子
 * @date 2016/9/13 11:38
 * ==============================
 */
public class AppointPresenter extends BasePresenter<IAppointView> {
	// m层
	IAppointController iAppointController;


	/**
	 * 获取信息
	 *
	 * @param
	 * @reurn
	 */
	public void getMessage() {
		List<Appoint> groupList = iAppointController.getList();
		// TODO
		if (mView != null) {
			mView.setAdapter(groupList);
		}
	}
}
