package com.boniu.shipinbofangqi.mvp.view.widget.popup;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.widget.dialog.QMUITipDialog;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.PayUtils;
import com.boniu.shipinbofangqi.util.SharedPreferenceUtil;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-08 01:04
 */
public class PayBottomPopup extends BottomPopupView {
    private ImageView iv_paypop_close;
    private TextView tv_paypop_price;
    private RelativeLayout rl_paypop_wx;
    private ImageView iv_paypop_wxselect;
    private RelativeLayout rl_paypop_zfb;
    private ImageView iv_paypop_zfbselect;
    private TextView tv_paypop_sub;
    private int type;
    private SharedPreferenceUtil spUtil;
    private double price;
    private String appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, payStr;
    /**
     * 加载提示框
     */
    private QMUITipDialog tipDialog;
    private Handler mHandler;
    private BaseActivity mActivity;

    public PayBottomPopup(@NonNull Context context, BaseActivity mActivity, QMUITipDialog tipDialog, Handler mHandler, String appId
            , String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign, String payStr, double price) {
        super(context);
        this.mActivity = mActivity;
        this.tipDialog = tipDialog;
        this.mHandler = mHandler;
        this.appId = appId;
        this.partnerId = partnerId;
        this.prepayId = prepayId;
        this.packageValue = packageValue;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.sign = sign;
        this.payStr = payStr;
        this.price = price;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pay_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        spUtil = SharedPreferenceUtil.getInstance(mActivity);
        type = spUtil.getInt(Global.SP_KEY_PAYWAY, 0);
        iv_paypop_close = findViewById(R.id.iv_paypop_close);
        tv_paypop_price = findViewById(R.id.tv_paypop_price);
        rl_paypop_wx = findViewById(R.id.rl_paypop_wx);
        iv_paypop_wxselect = findViewById(R.id.iv_paypop_wxselect);
        rl_paypop_zfb = findViewById(R.id.rl_paypop_zfb);
        iv_paypop_zfbselect = findViewById(R.id.iv_paypop_zfbselect);
        tv_paypop_sub = findViewById(R.id.tv_paypop_sub);
        tv_paypop_price.setText("支付金额：¥" + price);
        if (type == 1) {//微信支付
            iv_paypop_wxselect.setImageResource(R.mipmap.icon_select);
            iv_paypop_zfbselect.setImageResource(R.mipmap.icon_unselect);
        } else if (type == 1) {//支付宝支付
            iv_paypop_wxselect.setImageResource(R.mipmap.icon_unselect);
            iv_paypop_zfbselect.setImageResource(R.mipmap.icon_select);
        }
        iv_paypop_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rl_paypop_wx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                spUtil.saveInt(Global.SP_KEY_PAYWAY, 1);
                iv_paypop_wxselect.setImageResource(R.mipmap.icon_select);
                iv_paypop_zfbselect.setImageResource(R.mipmap.icon_unselect);
            }
        });
        rl_paypop_zfb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                spUtil.saveInt(Global.SP_KEY_PAYWAY, 2);
                iv_paypop_wxselect.setImageResource(R.mipmap.icon_unselect);
                iv_paypop_zfbselect.setImageResource(R.mipmap.icon_select);
            }
        });
        tv_paypop_sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 1) {
                    PayUtils.weChatPayment(mActivity, appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, tipDialog);
                } else if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 2) {
                    PayUtils.payByAliPay(mActivity, payStr, mHandler);
                } else {
                    RingToast.show("请先选择支付方式");
                }
            }
        });
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(mActivity) * .85f);
    }
}