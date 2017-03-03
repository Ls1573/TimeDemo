package com.time.memory.entity;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:图片信息
 * @date 2016/9/14 9:53
 * ==============================
 */
public class Picture {

	/**
	 * 图片Id
	 */
	private String Id;
	/**
	 * 图片地址
	 */
	private String path;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


}
