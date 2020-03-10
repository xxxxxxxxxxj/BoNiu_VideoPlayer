package com.boniu.shipinbofangqi.mvp.view.widget.popup;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.entity.PayChannel;
import com.boniu.shipinbofangqi.mvp.view.adapter.PayWayAdapter;
import com.boniu.shipinbofangqi.mvp.view.widget.NoScollFullLinearLayoutManager;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.SharedPreferenceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;

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
    private TextView tv_paypop_sub;
    private SharedPreferenceUtil spUtil;
    private double price;
    public OnPayInfoListener onPayInfoListener = null;
    private List<PayChannel> payChannelList;
    private RecyclerView rv_paypop_payway;
    private PayWayAdapter payWayAdapter;

    public void setPayChannel(List<PayChannel> payChannelList) {
        this.payChannelList = payChannelList;
    }

    public interface OnPayInfoListener {
        public void OnPayInfo();
    }

    public void setOnPayInfoListener(OnPayInfoListener onPayInfoListener) {
        this.onPayInfoListener = onPayInfoListener;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PayBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pay_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        spUtil = SharedPreferenceUtil.getInstance(getContext());
        rv_paypop_payway = findViewById(R.id.rv_paypop_payway);
        iv_paypop_close = findViewById(R.id.iv_paypop_close);
        tv_paypop_price = findViewById(R.id.tv_paypop_price);
        tv_paypop_sub = findViewById(R.id.tv_paypop_sub);
        tv_paypop_price.setText("支付金额：¥" + price);
        rv_paypop_payway.setHasFixedSize(true);
        rv_paypop_payway.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new
                NoScollFullLinearLayoutManager(getContext());
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rv_paypop_payway.setLayoutManager(noScollFullLinearLayoutManager);
        payWayAdapter = new PayWayAdapter(R.layout.item_payway, payChannelList);
        rv_paypop_payway.setAdapter(payWayAdapter);
        if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 1) {//微信支付
            if (payChannelList.size() > 0) {
                for (int i = 0; i < payChannelList.size(); i++) {
                    payChannelList.get(i).setSelect(false);
                }
                for (int i = 0; i < payChannelList.size(); i++) {
                    if (payChannelList.get(i).getType().equals("WECHAT_PAY")) {
                        payChannelList.get(i).setSelect(true);
                        break;
                    }
                }
            }
        } else if (spUtil.getInt(Global.SP_KEY_PAYWAY, 0) == 2) {//支付宝支付
            if (payChannelList.size() > 0) {
                for (int i = 0; i < payChannelList.size(); i++) {
                    payChannelList.get(i).setSelect(false);
                }
                for (int i = 0; i < payChannelList.size(); i++) {
                    if (payChannelList.get(i).getType().equals("ALIPAY")) {
                        payChannelList.get(i).setSelect(true);
                        break;
                    }
                }
            }
        }
        iv_paypop_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        payWayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < payChannelList.size(); i++) {
                    payChannelList.get(i).setSelect(false);
                }
                payChannelList.get(position).setSelect(true);
                if (payChannelList.get(position).getType().equals("WECHAT_PAY")) {
                    spUtil.saveInt(Global.SP_KEY_PAYWAY, 1);
                } else if (payChannelList.get(position).getType().equals("ALIPAY")) {
                    spUtil.saveInt(Global.SP_KEY_PAYWAY, 2);
                }
                payWayAdapter.notifyDataSetChanged();
            }
        });
        tv_paypop_sub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onPayInfoListener != null) {
                    onPayInfoListener.OnPayInfo();
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
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .85f);
    }
}