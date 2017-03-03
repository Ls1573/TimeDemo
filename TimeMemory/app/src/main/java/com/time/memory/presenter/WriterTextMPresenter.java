package com.time.memory.presenter;

import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IWriterTextMView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:写记忆文字
 * @date 2016/10/11 14:49
 */
public class WriterTextMPresenter extends BasePresenter<IWriterTextMView> {

	// m层
	public WriterTextMPresenter() {
	}


	/**
	 * 日期转换
	 *
	 * @reurn
	 */
	public void convertDate(String date) {
		String[] curDates = date.split("-");
		int year = Integer.parseInt(curDates[0]);
		int month = Integer.parseInt(curDates[1]);
		int day = Integer.parseInt(curDates[2]);
		if (mView != null)
			mView.showDatePicker(year, month, day);
	}

	/**
	 * 日期截取
	 *
	 * @reurn
	 */
	public String subStringDate(String date) {
		return date.substring(0, 10);
	}

}
