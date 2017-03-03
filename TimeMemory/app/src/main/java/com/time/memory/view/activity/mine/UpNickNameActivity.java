package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.User;
import com.time.memory.presenter.MineInfoPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;
import com.time.memory.view.impl.IMineInfoView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/9.
 */
public class UpNickNameActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.mine_nick_name_et)
    EditText mine_nick_name_et;
    @Override
    public void onCreateMyView() {
        setContentView(R.layout.activity_up_nickname);
    }

    @Override
    public void initView() {
        initTopBarForBoth(getString(R.string.mine_name), R.drawable.image_back, getString(R.string.app_save), -1);
    }

    @Override
    public void initData() {
        String nickName = getIntent().getStringExtra("nickName");
        mine_nick_name_et.setText(TextUtils.isEmpty(nickName) ? "" : nickName);
    }
    @OnClick(R.id.tv_main_right)
    public void onClick(View view) {
        if (view.getId() == R.id.app_cancle) {
            //取消
            setResultBack(Activity.RESULT_CANCELED, "");
        }
        if (view.getId() == R.id.tv_main_right) {
            //返回
            setResultBack(ReqConstant.NICK_NAME, mine_nick_name_et.getText().toString());

        }
    }

    @Override
    public void onBackPressed() {
        setResultBack(Activity.RESULT_CANCELED, "");
    }
    @Override
    public BasePresenter initPresenter() {
        return new MineInfoPresenter();
    }
    /**
     * 返回
     *
     * @param nickName
     */
    private void setResultBack(int resultCode, String nickName) {
        Intent intent = new Intent();
        intent.putExtra("nickName", nickName);
        setResult(resultCode, intent);
        finish();
    }

}
