package com.time.memory.core.callback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/9/19 13:27
 */
public interface MemoryDayCallback {
	void setActive(int newActiveViewPosition);

	void deactivate(int position);
}
