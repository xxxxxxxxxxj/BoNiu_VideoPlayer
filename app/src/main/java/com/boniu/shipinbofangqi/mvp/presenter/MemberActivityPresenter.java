package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.PayChannel;
import com.boniu.shipinbofangqi.mvp.model.entity.PayInfo;
import com.boniu.shipinbofangqi.mvp.model.entity.PayResult;
import com.boniu.shipinbofangqi.mvp.model.entity.ProductInfo;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IMemberActivityView;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.util.List;

import okhttp3.RequestBody;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 22:02
 */
public class MemberActivityPresenter extends BasePresenter<IMemberActivityView> {
    public MemberActivityPresenter(Context mContext, IMemberActivityView iMemberActivityView) {
        super(mContext, iMemberActivityView);
    }

    /**
     * 获取产品列表
     */
    public void getProductList() {
        HttpParams params = UrlConstants.getParams(mContext);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.PRODUCTLIST)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<List<ProductInfo>>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.productListFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<ProductInfo> response) {
                        mIView.productListSuccess(response);
                    }
                });
    }

    /**
     * 获取支付方式
     */
    public void getPayChannel() {
        HttpParams params = UrlConstants.getParams(mContext);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.PAYCHANNEL)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<List<PayChannel>>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.payChannelFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(List<PayChannel> response) {
                        mIView.payChannelSuccess(response);
                    }
                });
    }

    /**
     * 创建订单
     */
    public void orderCreate(String productId) {
        HttpParams params = UrlConstants.getParams(mContext);
        params.put("productId", productId);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.ORDERCREATE)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.orderCreateFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        mIView.orderCreateSuccess(response);
                    }
                });
    }

    /**
     * 下单
     */
    public void submitOrder(String orderId, String payChannel) {
        HttpParams params = new HttpParams();
        params.put("orderId", orderId);
        params.put("payChannel", payChannel);
        params.put("appName", "SHIPINBOFANGQI_BONIU");
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.SUBMITORDER)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<PayInfo>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.submitOrderFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(PayInfo response) {
                        mIView.submitOrderSuccess(response);
                    }
                });
    }

    /**
     * 查询支付结果
     */
    public void queryPayOrder(String orderId) {
        HttpParams params = UrlConstants.getParams(mContext);
        params.put("orderId", orderId);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.QUERYPAYORDER)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<PayResult>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.queryPayOrderFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(PayResult response) {
                        mIView.queryPayOrderSuccess(response);
                    }
                });
    }
}
