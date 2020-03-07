package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 帮助反馈界面
 */
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.srl_feedback)
    SmartRefreshLayout srlFeedback;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        srlFeedback.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("帮助与反馈");
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

    @OnClick({R.id.iv_toolbar_back, R.id.ll_feedback_jmwt, R.id.ll_feedback_gnwt, R.id.ll_feedback_nrwt, R.id.ll_feedback_qtwt, R.id.ll_feedback_cpjy, R.id.ll_feedback_lxkf, R.id.ll_feedback_zhzx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.ll_feedback_jmwt:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("name", "界面问题");
                startActivity(AddFeedBackActivity.class, bundle);
                break;
            case R.id.ll_feedback_gnwt:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 2);
                bundle1.putString("name", "功能问题");
                startActivity(AddFeedBackActivity.class, bundle1);
                break;
            case R.id.ll_feedback_nrwt:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("type", 3);
                bundle2.putString("name", "内容问题");
                startActivity(AddFeedBackActivity.class, bundle2);
                break;
            case R.id.ll_feedback_qtwt:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("type", 4);
                bundle3.putString("name", "其他问题");
                startActivity(AddFeedBackActivity.class, bundle3);
                break;
            case R.id.ll_feedback_cpjy:
                Bundle bundle4 = new Bundle();
                bundle4.putInt("type", 5);
                bundle4.putString("name", "产品建议");
                startActivity(AddFeedBackActivity.class, bundle4);
                break;
            case R.id.ll_feedback_lxkf:
                break;
            case R.id.ll_feedback_zhzx:
                break;
        }
    }
}
