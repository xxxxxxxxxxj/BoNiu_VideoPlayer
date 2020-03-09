package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.ALiPayResult;
import com.boniu.shipinbofangqi.mvp.model.entity.PayChannel;
import com.boniu.shipinbofangqi.mvp.model.entity.PayInfo;
import com.boniu.shipinbofangqi.mvp.model.entity.PayResult;
import com.boniu.shipinbofangqi.mvp.model.entity.ProductInfo;
import com.boniu.shipinbofangqi.mvp.model.event.WXPayResultEvent;
import com.boniu.shipinbofangqi.mvp.presenter.MemberActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IMemberActivityView;
import com.boniu.shipinbofangqi.mvp.view.widget.popup.PayBottomPopup;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开通高级页面
 */
public class MemberActivity extends BaseActivity<MemberActivityPresenter> implements IMemberActivityView {

    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_member_validitytime)
    TextView tvMemberValiditytime;
    @BindView(R.id.tv_member_sub)
    TextView tv_member_sub;
    @BindView(R.id.srl_member)
    SmartRefreshLayout srlMember;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    private String appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, payStr;
    private String validityTime;
    private List<ProductInfo> productInfoList;
    private List<PayChannel> payChannelList;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConfig.ALI_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    ALiPayResult payResult = new ALiPayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        RingLog.e("支付成功");
                        RingToast.show("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        RingLog.e("支付失败");
                        RingToast.show("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Subscribe
    public void onWXPayResult(WXPayResultEvent event) {
        if (event != null) {

        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_member;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        srlMember.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.a2D2D2D));
        ivToolbarBack.setImageResource(R.mipmap.icon_title_close);
        tvToolbarTitle.setVisibility(View.GONE);
        ImmersionBar.with(this).statusBarColor(R.color.a2D2D2D).init();
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        boolean ISOPENENVIP = spUtil.getBoolean(Global.SP_KEY_ISOPENENVIP, false);
        if (ISOPENENVIP) {
            tv_member_sub.setText("立即续费");
            tvMemberValiditytime.setVisibility(View.VISIBLE);
            tvMemberValiditytime.setText(validityTime + "到期");
        } else {
            tv_member_sub.setText("立即支付");
            tvMemberValiditytime.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        validityTime = getIntent().getStringExtra("validityTime");
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {
        showLoadDialog();
        mPresenter.getProductList();
        mPresenter.getPayChannel();
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected MemberActivityPresenter createPresenter() {
        return new MemberActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_member_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_member_sub:
                new XPopup.Builder(mActivity)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(new PayBottomPopup(mActivity, mActivity, tipDialog, mHandler, appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, payStr, 18.0)/*.enableDrag(false)*/)
                        .show();
                break;
        }
    }

    @Override
    public void productListSuccess(List<ProductInfo> response) {
        RingLog.e("productListSuccess() response = " + response);
        hideLoadDialog();
        productInfoList = response;
    }

    @Override
    public void productListFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("productListFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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

    @Override
    public void payChannelFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("payChannelFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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

    @Override
    public void payChannelSuccess(List<PayChannel> response) {
        RingLog.e("payChannelSuccess() response = " + response);
        hideLoadDialog();
        payChannelList = response;
    }

    @Override
    public void orderCreateSuccess(String response) {
        RingLog.e("orderCreateSuccess() response = " + response);
        hideLoadDialog();
    }

    @Override
    public void orderCreateFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("orderCreateFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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

    @Override
    public void submitOrderFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("submitOrderFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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

    @Override
    public void submitOrderSuccess(PayInfo response) {
        RingLog.e("PayInfo() response = " + response);
        hideLoadDialog();
    }

    @Override
    public void queryPayOrderSuccess(PayResult response) {
        RingLog.e("queryPayOrderSuccess() response = " + response);
        hideLoadDialog();
    }

    @Override
    public void queryPayOrderFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("queryPayOrderFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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
