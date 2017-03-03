package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.presenter.MineInfoPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/9.
 */
public class UpCompanyActivity extends BaseActivity implements IBaseView {
    @Bind(R.id.mine_company_name_et)
    EditText mine_company_name_et;
    @Override
    public void onCreateMyView() {
        setContentView(R.layout.activity_company);
    }

    @Override
    public void initView() {
        initTopBarForBoth(getString(R.string.app_address), R.drawable.image_back, getString(R.string.app_save), -1);
    }

    @Override
    public void initData() {
        String company = getIntent().getStringExtra("company");
        mine_company_name_et.setText(TextUtils.isEmpty(company) ? "" : company);
    }
    @OnClick(R.id.tv_main_right)
    public void onClick(View view) {
        if (view.getId() == R.id.app_cancle) {
            //取消
            setResultBack(Activity.RESULT_CANCELED, "");
        }
        if (view.getId() == R.id.tv_main_right) {
            //返回
            setResultBack(ReqConstant.COMPANY, mine_company_name_et.getText().toString());
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
     * @param company
     */
    private void setResultBack(int resultCode, String company) {
        Intent intent = new Intent();
        intent.putExtra("company", company);
        setResult(resultCode, intent);
        finish();
    }
}
