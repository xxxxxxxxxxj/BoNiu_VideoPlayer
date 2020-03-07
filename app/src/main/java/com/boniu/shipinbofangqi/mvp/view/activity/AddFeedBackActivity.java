package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加反馈页面
 */
public class AddFeedBackActivity extends BaseActivity {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_addfeedback_name)
    TextView tvAddfeedbackName;
    @BindView(R.id.et_addfeedback_name)
    EditText etAddfeedbackName;
    @BindView(R.id.srl_addfeedback)
    SmartRefreshLayout srlAddfeedback;
    private String name;
    private int type;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_add_feed_back;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        srlAddfeedback.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("帮助与反馈");
        tvAddfeedbackName.setText(name);
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);
        name = getIntent().getStringExtra("name");
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

    @OnClick({R.id.iv_toolbar_back, R.id.tv_addfeedback_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_addfeedback_sub:
                break;
        }
    }
}
