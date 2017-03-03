package com.time.memory.view.activity.mine;

import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.DataStatistics;
import com.time.memory.presenter.DataStatisticsPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IDataStatisticsView;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/11/7.
 */
public class DataStatisticsActivity extends BaseActivity implements IDataStatisticsView{
    /**
     * 我创建的圈子数
     */
    @Bind(R.id.i_create_tv)
    TextView iCreate;
    /**
     * 我加入别人创建的圈子数
     */
    @Bind(R.id.i_add_tv)
    TextView iAdd;
    /**
     * 我的好友数
     */
    @Bind(R.id.my_friend_tv)
    TextView myFriend;
    /**
     * 我发布的记忆数
     */
    @Bind(R.id.i_release_tv)
    TextView iRelease;
    /**
     * 我补充别人的记忆数
     */
    @Bind(R.id.i_add_other_tv)
    TextView iAddOther;
    /**
     * 别人补充我的记忆数
     */
    @Bind(R.id.other_supplement_tv)
    TextView otherSupplement;
    private String url;
    @Override
    public void onCreateMyView() {
        setContentView(R.layout.activity_data_statistics);
    }

    @Override
    public void initView() {
        initTopBarForLeft(getString(R.string.data_statistics), R.drawable.image_back);

    }

    @Override
    public void initData() {
        url = getString(R.string.FSDATASTATISTICS);
        ((DataStatisticsPresenter)mPresenter).getData(url);
    }
    @Override
    public BasePresenter initPresenter() {
        return new DataStatisticsPresenter();
    }

    @Override
    public void setDataStatistics(DataStatistics dataStatistics) {
        iCreate.setText(dataStatistics.getCreateGroupCount());
        iAdd.setText(dataStatistics.getJoinGroupCount());
        myFriend.setText(dataStatistics.getFriendCount());
        iRelease.setText(dataStatistics.getCreateMemoryCount());
        iAddOther.setText(dataStatistics.getAddMemoryPointCount1());
        otherSupplement.setText(dataStatistics.getAddMemoryPointCount2());
    }
}

