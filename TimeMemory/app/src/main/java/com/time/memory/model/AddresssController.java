package com.time.memory.model;

import android.nfc.Tag;

import com.time.memory.MainApplication;
import com.time.memory.entity.Address;
import com.time.memory.entity.AddressDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IAddressController;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/12/1 11:11
 */
public class AddresssController extends BaseController implements IAddressController {
	private AddressDao addressDao;

	public AddresssController() {
		addressDao = MainApplication.getDaoSession().getAddressDao();
	}

	@Override
	public ArrayList<Address> getProvinces() {
		QueryBuilder qb = addressDao.queryBuilder();
		qb.where(AddressDao.Properties.Area_level.eq("1"));
		qb.orderAsc(AddressDao.Properties.Orby);
		return (ArrayList<Address>) qb.list();
	}

	@Override
	public ArrayList<Address> getCitys(String provinceId) {
		QueryBuilder qb = addressDao.queryBuilder();
		qb.where(AddressDao.Properties.Area_level.eq("2"), AddressDao.Properties.Parent_id.eq(provinceId));
		return (ArrayList<Address>) qb.list();
	}
}
