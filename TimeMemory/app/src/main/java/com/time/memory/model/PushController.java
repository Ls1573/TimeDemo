package com.time.memory.model;

import com.time.memory.MainApplication;
import com.time.memory.entity.MyPush;
import com.time.memory.entity.MyPushDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IPushController;

/**
 * @author Qiu
 * @version V1.0
 * @Description:推送设置
 * @date 2016/11/1 9:13
 */
public class PushController extends BaseController implements IPushController {

	private MyPushDao pushDao;

	public PushController() {
		pushDao = MainApplication.getDaoSession().getMyPushDao();
	}

	/**
	 * 保存推送设置
	 *
	 * @param push
	 */
	@Override
	public void savePush(MyPush push) {
		pushDao.insertOrReplace(push);
	}

	/**
	 * 更新设置
	 *
	 * @param myPush
	 */
	@Override
	public void updatePush(MyPush myPush) {
		pushDao.updateInTx(myPush);
	}

	/**
	 * 获取推送设置
	 *
	 * @param key
	 * @return
	 */
	@Override
	public MyPush getPushByKey(String key) {
		return pushDao.load(key);
	}
}
