package com.time.memory.presenter;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.DataStatistics;
import com.time.memory.model.MyDataStatisticsController;
import com.time.memory.model.impl.DataStatisticsController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IDataStatisticsView;

/**
 * Created by Administrator on 2016/11/7.
 */
public class DataStatisticsPresenter extends BasePresenter<IDataStatisticsView> {
    private static final String TAG = "DataStatisticsPresenter";
    private DataStatisticsController dataStatisticsController;

    public DataStatisticsPresenter(){
        dataStatisticsController = new MyDataStatisticsController();
    }
    public void getData(String url){
        dataStatisticsController.getDataStatistics(url, new SimpleCallback() {
            @Override
            public void onNoNetCallback() {
                if (mView != null) {
                    mView.showShortToast(context.getString(R.string.net_no_connection));
                    mView.showFaild();
                }
            }

            @Override
            public void onCallback(Object data) {
                if (data == null) {
                    if (mView != null) {
                        mView.showFaild();
                        mView.showShortToast(context.getString(R.string.net_error));
                    }
                }else {
                    DataStatistics dataStatistics = (DataStatistics) data;
                    if (dataStatistics.getStatus() == 0){
                        if (mView != null){
                            mView.setDataStatistics(dataStatistics);
                        }else {
                            //失败
                            if (mView != null) {
                                mView.showFaild();
                                mView.showShortToast(dataStatistics.getMessage());
                            }
                        }
                    }
                }
            }
        });
    }
}
