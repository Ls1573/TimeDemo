package com.time.memory.model.impl;

import com.time.memory.entity.MGroup;
import com.time.memory.entity.UserGroup;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:不给谁看
 * @date 2016/9/13 16:454
 * ==============================
 */
public interface INoSeeController {
	List<MGroup> getGroupEntities();//群

	List<UserGroup> getUserEntities();//用户
}
