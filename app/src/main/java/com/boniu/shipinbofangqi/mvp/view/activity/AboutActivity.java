package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.util.QMUIPackageHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于页面
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_about_version)
    TextView tvAboutVersion;
    @BindView(R.id.srl_about)
    SmartRefreshLayout srlAbout;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvToolbarTitle.setText("关于我们");
        srlAbout.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvAboutVersion.setText("V" + QMUIPackageHelper.getAppVersion(mContext));
    }

    @Override
    protected void setView(Bundle savedInstanceState) {

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

    @OnClick({R.id.iv_toolbar_back, R.id.tv_about_yhxy, R.id.tv_about_yszc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_about_yhxy:
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.URL_KEY, "file:///android_asset/network.html");
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.tv_about_yszc:
                Bundle bundle1 = new Bundle();
                bundle1.putString(WebViewActivity.URL_KEY, "file:///android_asset/privacy.html");
                startActivity(WebViewActivity.class, bundle1);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
