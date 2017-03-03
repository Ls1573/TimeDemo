package com.time.memory.model.impl;

import com.time.memory.entity.Picture;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:添加记忆
 * @date 2016/9/22 8:29
 */
public interface IAddMemoryController {

	List<Picture> getAppoint();

	List<Picture> getNoSee();

	List<String> getTags();

}
