package com.boniu.shipinbofangqi.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.EnvUtils;
import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.ALiPayResult;
import com.boniu.shipinbofangqi.mvp.model.entity.OrderCreateBean;
import com.boniu.shipinbofangqi.mvp.model.entity.PayChannel;
import com.boniu.shipinbofangqi.mvp.model.entity.PayInfo;
import com.boniu.shipinbofangqi.mvp.model.entity.PayResult;
import com.boniu.shipinbofangqi.mvp.model.entity.ProductInfo;
import com.boniu.shipinbofangqi.mvp.model.event.PayEvent;
import com.boniu.shipinbofangqi.mvp.model.event.PayResultEvent;
import com.boniu.shipinbofangqi.mvp.model.event.WXPayResultEvent;
import com.boniu.shipinbofangqi.mvp.presenter.MemberActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.MemberProductAdapter;
import com.boniu.shipinbofangqi.mvp.view.iview.IMemberActivityView;
import com.boniu.shipinbofangqi.mvp.view.widget.popup.PayBottomPopup;
import com.boniu.shipinbofangqi.services.PayResultService;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.PayUtils;
import com.boniu.shipinbofangqi.util.PollingUtils;
import com.boniu.shipinbofangqi.util.SharedPreferenceUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.mm.opensdk.modelbase.BaseResp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
    @BindView(R.id.tv_member_sub)
    TextView tv_member_sub;
    @BindView(R.id.srl_member)
    SmartRefreshLayout srlMember;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.rv_member_product)
    RecyclerView rv_member_product;
    private List<ProductInfo> productInfoList = new ArrayList<ProductInfo>();
    private List<PayChannel> payChannelList = new ArrayList<PayChannel>();
    private double price;
    private PayBottomPopup payBottomPopup;
    private String productId;
    private String orderId;
    private String payChannel;
    private int pollingNum;//轮询次数
    private MemberProductAdapter memberProductAdapter;

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
                        pollingNum = 1;
                        showLoadDialog();
                        mPresenter.queryPayOrder(orderId);
                    } else if (TextUtils.equals(resultStatus, "8000")) {//支付结果确认中
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        pollingNum = 1;
                        showLoadDialog();
                        mPresenter.queryPayOrder(orderId);
                    } else {//支付失败
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
            BaseResp resp = event.getResp();
            if (resp != null) {
                Log.e("TAG", "resp.errCode = " + resp.errCode);
                Log.e("TAG", "resp.errStr = " + resp.errStr);
                if (resp.errCode == 0) {
                    pollingNum = 1;
                    showLoadDialog();
                    mPresenter.queryPayOrder(orderId);
                } else {
                    if (StringUtil.isNotEmpty(resp.errStr)) {
                        RingToast.show(resp.errStr);
                    } else {
                        RingToast.show("支付失败");
                    }
                }
            }
        }
    }

    @Subscribe
    public void onPayResult(PayResultEvent event) {
        if (event != null) {
            if (event.getCode() == 0) {
                if (pollingNum >= 3) {
                    RingToast.show("支付失败,请联系客服");
                    hideLoadDialog();
                    PollingUtils.stopPollingService(mActivity, PayResultService.class, PayResultService.ACTION);
                } else {
                    PayResult payResult = event.getPayResult();
                    if (payResult != null) {
                        String resultCode = payResult.getResultCode();
                        if (StringUtil.isNotEmpty(resultCode)) {
                            if (resultCode.equals("RETRY")) {
                                pollingNum = pollingNum + 1;
                                PollingUtils.startPollingService(mActivity, 1, PayResultService.class, PayResultService.ACTION, orderId);
                            } else if (resultCode.equals("SUCCESS")) {
                                EventBus.getDefault().post(new PayEvent());
                                RingToast.show("支付成功");
                                hideLoadDialog();
                                PollingUtils.stopPollingService(mActivity, PayResultService.class, PayResultService.ACTION);
                                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, true);
                                finish();
                            } else if (resultCode.equals("FAIL")) {
                                RingToast.show(payResult.getResultMsg());
                                hideLoadDialog();
                                PollingUtils.stopPollingService(mActivity, PayResultService.class, PayResultService.ACTION);
                            }
                        }
                    } else {
                        pollingNum = pollingNum + 1;
                        PollingUtils.startPollingService(mActivity, 1, PayResultService.class, PayResultService.ACTION, orderId);
                    }
                }
            } else if (event.getCode() == AppConfig.EXIT_USER_CODE) {
                hideLoadDialog();
                PollingUtils.stopPollingService(mActivity, PayResultService.class, PayResultService.ACTION);
                SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_ISLOGIN);
                SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_CELLPHONE);
                SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_ACCOUNTIUD);
                SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_TOKEN);
                RingToast.show("您已在其他设备登录");
                startActivity(new Intent(mContext, LoginActivity.class));
            } else if (event.getCode() == AppConfig.CLEARACCOUNTID_CODE) {
                hideLoadDialog();
                PollingUtils.stopPollingService(mActivity, PayResultService.class, PayResultService.ACTION);
                CommonUtil.getNewAccountId(mContext);
            } else {
                hideLoadDialog();
                PollingUtils.stopPollingService(mActivity, PayResultService.class, PayResultService.ACTION);
                int netWorkState = CommonUtil.getNetWorkState(mContext);
                if (netWorkState == CommonUtil.NETWORK_NONE) {
                    RingToast.show("无网络连接");
                } else {
                    RingToast.show(event.getMsg());
                }
            }
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
        rv_member_product.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_member_product.setLayoutManager(linearLayoutManager);
        memberProductAdapter = new MemberProductAdapter(R.layout.item_member_product, productInfoList);
        rv_member_product.setAdapter(memberProductAdapter);
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        boolean ISOPENENVIP = spUtil.getBoolean(Global.SP_KEY_ISOPENENVIP, false);
        if (ISOPENENVIP) {
            tv_member_sub.setText("立即续费");
        } else {
            tv_member_sub.setText("立即支付");
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
    }

    @Override
    protected void initEvent() {
        memberProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < productInfoList.size(); i++) {
                    productInfoList.get(i).setSelect(false);
                }
                productInfoList.get(position).setSelect(true);
                price = productInfoList.get(position).getDiscountPrice();
                productId = productInfoList.get(position).getProductId();
                memberProductAdapter.notifyDataSetChanged();
            }
        });
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
        setSwipeBack(false);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_member_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_member_sub:
                if (StringUtil.isEmpty(productId)) {
                    RingToast.show("请先选择产品");
                    return;
                }
                if (payBottomPopup == null) {
                    payBottomPopup = new PayBottomPopup(mActivity);
                }
                payBottomPopup.setPrice(price);
                payBottomPopup.setPayChannel(payChannelList);
                payBottomPopup.setOnPayInfoListener(new PayBottomPopup.OnPayInfoListener() {
                    @Override
                    public void OnPayInfo() {
                        if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) > 0) {
                            showLoadDialog();
                            mPresenter.orderCreate(productId);
                        } else {
                            RingToast.show("请先选择支付方式");
                        }
                    }
                });
                new XPopup.Builder(mActivity)
                        .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                        .asCustom(payBottomPopup)
                        .show();
                break;
        }
    }

    @Override
    public void productListSuccess(List<ProductInfo> response) {
        RingLog.e("productListSuccess() response = " + response);
        hideLoadDialog();
        productInfoList.clear();
        if (response != null && response.size() > 0) {
            productInfoList.addAll(response);
            for (int i = 0; i < productInfoList.size(); i++) {
                productInfoList.get(i).setSelect(false);
                if (i == 0) {
                    productInfoList.get(i).setDesc("最基础");
                } else if (i == 1) {
                    productInfoList.get(i).setDesc("最优惠");
                } else if (i == 2) {
                    productInfoList.get(i).setDesc("最值得");
                }
            }
            for (int i = 0; i < productInfoList.size(); i++) {
                if (i == 1) {
                    productInfoList.get(i).setSelect(true);
                    price = productInfoList.get(i).getDiscountPrice();
                    productId = productInfoList.get(i).getProductId();
                    break;
                }
            }
        }
        memberProductAdapter.notifyDataSetChanged();
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
    public void payChannelFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("payChannelFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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
    public void payChannelSuccess(List<PayChannel> response) {
        RingLog.e("payChannelSuccess() response = " + response);
        hideLoadDialog();
        if (response != null && response.size() > 0) {
            payChannelList.addAll(response);
        }
    }

    @Override
    public void orderCreateSuccess(String response) {
        RingLog.e("orderCreateSuccess() response = " + response);
        hideLoadDialog();
        if (StringUtil.isNotEmpty(response)) {
            Gson gson = new Gson();
            OrderCreateBean orderCreateBean = gson.fromJson(response, OrderCreateBean.class);
            if (orderCreateBean != null) {
                if (orderCreateBean.getErrorCode().equals("0")) {
                    orderId = orderCreateBean.getResult();
                    if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 1) {//微信支付
                        payChannel = "WECHAT_PAY";
                    } else if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 2) {//支付宝支付
                        payChannel = "ALIPAY";
                    }
                    showLoadDialog();
                    mPresenter.submitOrder(orderId, payChannel);
                } else {
                    int code = 0;
                    if (orderCreateBean.getErrorCode().contains("-")) {
                        code = Integer.parseInt(orderCreateBean.getErrorCode().split("-")[2]);
                    }
                    if (code == AppConfig.EXIT_USER_CODE) {
                        spUtil.removeData(Global.SP_KEY_ISLOGIN);
                        spUtil.removeData(Global.SP_KEY_CELLPHONE);
                        spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
                        spUtil.removeData(Global.SP_KEY_TOKEN);
                        RingToast.show("您已在其他设备登录");
                        startActivity(LoginActivity.class);
                    } else if (code == AppConfig.CLEARACCOUNTID_CODE) {
                        CommonUtil.getNewAccountId(mActivity);
                    } else {
                        int netWorkState = CommonUtil.getNetWorkState(mContext);
                        if (netWorkState == CommonUtil.NETWORK_NONE) {
                            RingToast.show("无网络连接");
                        } else {
                            RingToast.show(orderCreateBean.getErrorMsg());
                        }
                    }
                }
            }
        }
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
    public void submitOrderFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("submitOrderFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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
    public void submitOrderSuccess(PayInfo response) {
        RingLog.e("PayInfo() response = " + response);
        hideLoadDialog();
        try {
            if (response != null) {
                if ((StringUtil.isNotEmpty(response.getErrorCode()) && response.getErrorCode().equals("FAIL")) || (StringUtil.isNotEmpty(response.getErrorCode()) && response.getResultCode().equals("FAIL"))) {
                    RingToast.show(response.getResultMsg());
                } else {
                    if (response.getPayInfo() != null) {
                        if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 1) {
                            String[] split = response.getPayInfo().split(",");
                            response.setPackageValue(split[0].split(":")[1].replace("\"", ""));
                            response.setAppId(split[1].split(":")[1].replace("\"", ""));
                            response.setSign(split[2].split(":")[1].replace("\"", ""));
                            response.setPartnerId(split[3].split(":")[1].replace("\"", ""));
                            response.setPrepayId(split[4].split(":")[1].replace("\"", ""));
                            response.setNonceStr(split[5].split(":")[1].replace("\"", ""));
                            response.setTimeStamp(split[6].split(":")[1].replace("}", "").replace("\"", ""));
                            RingLog.e("PayInfo() response = " + response);
                            PayUtils.weChatPayment(mActivity, response.getAppId(), response.getPartnerId(), response.getPrepayId(), response.getPackageValue(), response.getNonceStr(), response.getTimeStamp(), response.getSign(), tipDialog);
                        } else if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 2) {
                            PayUtils.payByAliPay(mActivity, response.getPayInfo(), mHandler);
                        }
                    }
                }
            }
        } catch (Exception e) {
            RingLog.e("submitOrderSuccess() Exception = " + e.toString());
        }
    }

    @Override
    public void queryPayOrderFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("submitOrderFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
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
    public void queryPayOrderSuccess(PayResult response) {
        if (pollingNum >= 3) {
            RingToast.show("支付失败,请联系客服");
            hideLoadDialog();
        } else {
            if (response != null) {
                String resultCode = response.getResultCode();
                if (StringUtil.isNotEmpty(resultCode)) {
                    if (resultCode.equals("RETRY")) {
                        pollingNum = pollingNum + 1;
                        mPresenter.queryPayOrder(orderId);
                    } else if (resultCode.equals("SUCCESS")) {
                        EventBus.getDefault().post(new PayEvent());
                        RingToast.show("支付成功");
                        hideLoadDialog();
                        spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, true);
                        finish();
                    } else if (resultCode.equals("FAIL")) {
                        RingToast.show(response.getResultMsg());
                        hideLoadDialog();
                    }
                }
            } else {
                pollingNum = pollingNum + 1;
                mPresenter.queryPayOrder(orderId);
            }
        }
    }
}
