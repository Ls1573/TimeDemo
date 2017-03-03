package com.time.memory.core.callback;

public interface AdapterCallback<T> extends Callback {

	void onDataCallBack(T data, int position);

	void onDataCallBack(T data, int position, int index);

	void onDataCallBack(T data, int position, int index, int tag);

	void onLongCallBack(T data, int position);

}