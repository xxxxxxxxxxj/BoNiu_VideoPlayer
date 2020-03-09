package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.CancelAccountBean;
import com.boniu.shipinbofangqi.mvp.presenter.FeedBackActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IFeedBackActivityView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 帮助反馈界面
 */
public class FeedBackActivity extends BaseActivity<FeedBackActivityPresenter> implements IFeedBackActivityView {
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
        tvToolbarTitle.setText("账号注销");
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
    protected FeedBackActivityPresenter createPresenter() {
        return new FeedBackActivityPresenter(this, this);
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
                bundle.putString("type", "UI");
                bundle.putString("name", "界面问题");
                startActivity(AddFeedBackActivity.class, bundle);
                break;
            case R.id.ll_feedback_gnwt:
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "FUNCTION");
                bundle1.putString("name", "功能问题");
                startActivity(AddFeedBackActivity.class, bundle1);
                break;
            case R.id.ll_feedback_nrwt:
                Bundle bundle2 = new Bundle();
                bundle2.putString("type", "CONTENT");
                bundle2.putString("name", "内容问题");
                startActivity(AddFeedBackActivity.class, bundle2);
                break;
            case R.id.ll_feedback_qtwt:
                Bundle bundle3 = new Bundle();
                bundle3.putString("type", "OTHER");
                bundle3.putString("name", "其他问题");
                startActivity(AddFeedBackActivity.class, bundle3);
                break;
            case R.id.ll_feedback_cpjy:
                Bundle bundle4 = new Bundle();
                bundle4.putString("type", "PROD_SUGGEST");
                bundle4.putString("name", "产品建议");
                startActivity(AddFeedBackActivity.class, bundle4);
                break;
            case R.id.ll_feedback_lxkf:
                CommonUtil.copy(mActivity, "1404556846");
                break;
            case R.id.ll_feedback_zhzx:
                boolean ISCANCEL = spUtil.getBoolean(Global.SP_KEY_ISCANCEL, false);
                if (ISCANCEL) {
                    RingToast.show("账号已注销");
                } else {
                    MessageDialog.show(mActivity, "注销账号", "确定注销账号吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v) {
                            showLoadDialog();
                            mPresenter.cancelAccount();
                            return false;
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void cancelAccountSuccess(CancelAccountBean response) {
        RingLog.e("cancelAccountSuccess() response = " + response);
        hideLoadDialog();
        spUtil.saveBoolean(Global.SP_KEY_ISCANCEL, true);
        if (response != null) {
            Bundle bundle = new Bundle();
            bundle.putString("applyTime", response.getApplyTime());
            startActivity(CancelAccountActivity.class, bundle);
        }
    }

    @Override
    public void cancelAccountFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("cancelAccountFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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
