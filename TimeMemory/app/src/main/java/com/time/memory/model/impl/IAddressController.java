package com.time.memory.model.impl;


import com.time.memory.entity.Address;

import java.util.ArrayList;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:地址管理
 * @date 2016/12/1 11:10
 */
public interface IAddressController {

	ArrayList<Address> getProvinces();

	ArrayList<Address> getCitys(String provinceId);

}
