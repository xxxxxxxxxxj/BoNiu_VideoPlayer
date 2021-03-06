package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.boniu.shipinbofangqi.util.StringUtil;
import com.kongzue.dialog.v3.CustomDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

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
                CommonUtil.goToQQ(mActivity, "1404556846");
                break;
            case R.id.ll_feedback_zhzx:
                String CANCELTIME = spUtil.getString(Global.SP_KEY_CANCELTIME, "");
                if (StringUtil.isNotEmpty(CANCELTIME)) {
                    startActivity(CancelAccountActivity.class);
                } else {
                    CustomDialog.build(mActivity, R.layout.layout_cancelaccount_dialog, new CustomDialog.OnBindView() {
                        @Override
                        public void onBind(final CustomDialog dialog, View v) {
                            ImageView iv_cancelaccountdialog_close = v.findViewById(R.id.iv_cancelaccountdialog_close);
                            TextView tv_cancelaccountdialog_zx = v.findViewById(R.id.tv_cancelaccountdialog_zx);
                            TextView tv_cancelaccountdialog_cancel = v.findViewById(R.id.tv_cancelaccountdialog_cancel);
                            iv_cancelaccountdialog_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.doDismiss();
                                }
                            });
                            tv_cancelaccountdialog_zx.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showLoadDialog();
                                    mPresenter.cancelAccount();
                                    dialog.doDismiss();
                                }
                            });
                            tv_cancelaccountdialog_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.doDismiss();
                                }
                            });
                        }
                    }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false).show();
                }
                break;
        }
    }

    @Override
    public void cancelAccountSuccess(CancelAccountBean response) {
        RingLog.e("cancelAccountSuccess() response = " + response);
        hideLoadDialog();
        if (response != null) {
            if (StringUtil.isNotEmpty(response.getApplyTime())) {
                spUtil.saveString(Global.SP_KEY_CANCELTIME, response.getApplyTime());
            }
            startActivity(CancelAccountActivity.class);
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
            RingToast.show("您已在其他设备登录");
            startActivity(LoginActivity.class);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            int netWorkState = CommonUtil.getNetWorkState(mContext);
            if (netWorkState == CommonUtil.NETWORK_NONE) {
                RingToast.show("无网络连接");
            } else {
                RingToast.show(errorMsg);
            }
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
