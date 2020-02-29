package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tiet_login_mobile)
    TextInputEditText tietLoginMobile;
    @BindView(R.id.til_login_mobile)
    TextInputLayout tilLoginMobile;
    @BindView(R.id.tiet_login_yzm)
    TextInputEditText tietLoginYzm;
    @BindView(R.id.til_login_yzm)
    TextInputLayout tilLoginYzm;
    @BindView(R.id.tv_login_yzm)
    TextView tvLoginYzm;
    @BindView(R.id.tv_login_sub)
    TextView tvLoginSub;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        tvToolbarTitle.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_login_yzm, R.id.tv_login_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_login_yzm:
                break;
            case R.id.tv_login_sub:
                break;
        }
    }
}
