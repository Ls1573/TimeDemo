package com.time.memory.core.callback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:Holder -> Adapter回调接口
 * @date 2016/9/24 16:19
 */
public interface IHolderCallBack {

	void onClick(int position);

	void onLongClick(int position);

	void onClick(Object obj);

	void onClick(int position, Object obj);

	void onClick(int position, int index, Object obj);

	void onClick(int position, int index, Object obj,int tag);



}
