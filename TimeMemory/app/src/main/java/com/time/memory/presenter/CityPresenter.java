package com.time.memory.presenter;

import com.time.memory.entity.Address;
import com.time.memory.model.AddresssController;
import com.time.memory.model.impl.IAddressController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.ICityView;

import java.util.ArrayList;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:城市管理
 * @date 2016/12/1 11:56
 */
public class CityPresenter extends BasePresenter<ICityView> {
	private static final String TAG = "CityPresenter";

	private IAddressController iAddressController;

	public CityPresenter() {
		iAddressController = new AddresssController();
	}


	/**
	 * 获取城市
	 */
	public void getProvinces(String Id) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		ArrayList<Address> addresses = iAddressController.getCitys(Id);
		if (mView != null) {
			mView.showSuccess();
			mView.setAdapter(addresses);
		}
	}

}
