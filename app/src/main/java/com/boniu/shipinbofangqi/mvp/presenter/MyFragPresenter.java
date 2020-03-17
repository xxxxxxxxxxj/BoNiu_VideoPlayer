package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IMyFragView;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import okhttp3.RequestBody;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:13
 */
public class MyFragPresenter extends BasePresenter<IMyFragView> {
    public MyFragPresenter(Context mContext, IMyFragView iMyFragView) {
        super(mContext, iMyFragView);
    }

    /**
     * 获取账户信息
     */
    public void getAccountInfo() {
        HttpParams params = new UrlConstants().getParams(mContext);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.GETACCOUNTINFO)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<AccountInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.getAccountInfoFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(AccountInfoBean response) {
                        mIView.getAccountInfoSuccess(response);
                    }
                });
    }

    /**
     * 退出当前已登录账户
     */
    public void logout() {
        HttpParams params = new UrlConstants().getParams(mContext);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.LOGOUT)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<Boolean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.logoutFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean response) {
                        mIView.logoutSuccess(response);
                    }
                });
    }
}