package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AppInfoBean;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IMainActivityView;
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
 * @date zhoujunxia on 2019-10-14 10:15
 */
public class MainActivityPresenter extends BasePresenter<IMainActivityView> {
    public MainActivityPresenter(Context mContext, IMainActivityView iMainActivityView) {
        super(mContext, iMainActivityView);
    }

    /**
     * 获取最新版本
     */
    public void checkVersion() {
        HttpParams params = new UrlConstants().getParams(mContext);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.CHECKVERSION)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<AppInfoBean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.checkVersionFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(AppInfoBean response) {
                        mIView.checkVersionSuccess(response);
                    }
                });
    }
}
