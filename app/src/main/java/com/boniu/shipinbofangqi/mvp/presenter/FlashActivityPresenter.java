package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IFlashActivityView;
import com.boniu.shipinbofangqi.util.CommonUtil;
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
 * @date zhoujunxia on 2020-02-27 22:39
 */
public class FlashActivityPresenter extends BasePresenter<IFlashActivityView> {
    public FlashActivityPresenter(Context mContext, IFlashActivityView iFlashActivityView) {
        super(mContext, iFlashActivityView);
    }

    /**
     * 获取登录账户的相关详细信息
     */
    public void getAccountInfo() {
        HttpParams params = new UrlConstants().getParams(mContext);
        params.put("accountId", CommonUtil.getAccountId(mContext));
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
}
