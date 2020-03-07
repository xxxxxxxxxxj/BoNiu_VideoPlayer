package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.iv_toolbar_back)
    ImageView iv_toolbar_back;
    @BindView(R.id.srl_login)
    SmartRefreshLayout srlLogin;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        srlLogin.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.a2D2D2D));
        iv_toolbar_back.setImageResource(R.mipmap.icon_title_close);
        tvToolbarTitle.setVisibility(View.GONE);
        ImmersionBar.with(this).statusBarColor(R.color.a2D2D2D).init();
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
