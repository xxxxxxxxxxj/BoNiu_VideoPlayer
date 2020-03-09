package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.LoginBean;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.ILoginActivityView;
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
 * @date zhoujunxia on 2020-03-09 15:30
 */
public class LoginActivityPresenter extends BasePresenter<ILoginActivityView> {
    public LoginActivityPresenter(Context mContext, ILoginActivityView iLoginActivityView) {
        super(mContext, iLoginActivityView);
    }

    /**
     * 获取验证码
     */
    public void sendVerifyCode(Context mContext, String mobile) {
        HttpParams params = new HttpParams();
        params.put("appName", "SHIPINBOFANGQI_BONIU");
        params.put("mobile", mobile);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.SENDVERIFYCODE)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<Boolean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.sendVerifyCodeFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean response) {
                        mIView.sendVerifyCodeSuccess(response);
                    }
                });
    }

    /**
     * 登录
     *
     * @param mContext
     * @param mobile
     * @param verifyCode
     */
    public void login(Context mContext, String mobile, String verifyCode) {
        HttpParams params = new UrlConstants().getParams(mContext);
        params.put("mobile", mobile);
        params.put("verifyCode", verifyCode);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.LOGIN)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<LoginBean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.loginFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(LoginBean response) {
                        mIView.loginSuccess(response);
                    }
                });
    }
}
