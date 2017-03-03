package com.time.memory.view.impl;


import com.time.memory.entity.MGroup;
import com.time.memory.entity.UserGroup;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/9/13 16:49
 * ==============================
 */
public interface INoSeeView extends IBaseView{

		void setAdapter(List<MGroup> MGroupEntities,List<UserGroup> userEntities);

	    void nofiAdapter();
}
