package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注销账号页面
 */
public class CancelAccountActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_cancelaccount_zh)
    TextView tvCancelaccountZh;
    @BindView(R.id.tv_cancelaccount_time)
    TextView tvCancelaccountTime;
    @BindView(R.id.srl_cancelaccount)
    SmartRefreshLayout srlCancelaccount;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_cancel_account;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        srlCancelaccount.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("账号注销");
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        tvCancelaccountZh.setText("注销账号：" + CommonUtil.getCellPhone(mContext));
        tvCancelaccountTime.setText("注销申请时间：" + spUtil.getString(Global.SP_KEY_CANCELTIME,""));
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

    @OnClick({R.id.iv_toolbar_back, R.id.tv_cancelaccount_gb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_cancelaccount_gb:
                finish();
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
