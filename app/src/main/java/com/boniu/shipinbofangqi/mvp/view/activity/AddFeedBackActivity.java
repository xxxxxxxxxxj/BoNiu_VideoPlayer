package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.presenter.AddFeedBackActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IAddFeedBackActivityView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加反馈页面
 */
public class AddFeedBackActivity extends BaseActivity<AddFeedBackActivityPresenter> implements IAddFeedBackActivityView {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_addfeedback_name)
    TextView tvAddfeedbackName;
    @BindView(R.id.et_addfeedback_name)
    EditText etAddfeedbackName;
    @BindView(R.id.srl_addfeedback)
    SmartRefreshLayout srlAddfeedback;
    private String name;
    private String type;

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
        CommonUtil.showSoftInputFromWindow(mActivity, etAddfeedbackName);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
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
    protected AddFeedBackActivityPresenter createPresenter() {
        return new AddFeedBackActivityPresenter(this, this);
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
                if (StringUtil.isEmpty(StringUtil.checkEditText(etAddfeedbackName))) {
                    RingToast.show("请输入内容");
                    return;
                }
                showLoadDialog();
                mPresenter.addFeedBack(type, StringUtil.checkEditText(etAddfeedbackName));
                break;
        }
    }

    @Override
    public void addFeedBackSuccess(Boolean response) {
        RingLog.e("addFeedBackSuccess() response = " + response);
        hideLoadDialog();
        if (response) {
            RingToast.show("提交成功");
            etAddfeedbackName.setText("");
        }
    }

    @Override
    public void addFeedBackFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("addFeedBackFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
        if (errorCode == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
            startActivity(LoginActivity.class);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        }
    }
}
