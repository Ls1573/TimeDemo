package com.time.memory.presenter;

import com.time.memory.entity.Address;
import com.time.memory.model.AddresssController;
import com.time.memory.model.impl.IAddressController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IProvinceView;

import java.util.ArrayList;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:省份管理
 * @date 2016/12/1 11:55
 */
public class ProvincePresenter extends BasePresenter<IProvinceView> {
	private static final String TAG = "AddFriendPresenter";

	private IAddressController iAddressController;

	public ProvincePresenter() {
		iAddressController = new AddresssController();
	}


	/**
	 * 获取省份
	 */
	public void getProvinces() {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		ArrayList<Address> addresses = iAddressController.getProvinces();
		if (mView != null) {
			mView.showSuccess();
			mView.setAdapter(addresses);
		}
	}
}
