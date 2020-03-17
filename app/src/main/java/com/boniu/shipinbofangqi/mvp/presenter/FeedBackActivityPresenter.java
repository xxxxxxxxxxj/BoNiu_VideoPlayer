package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.CancelAccountBean;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IFeedBackActivityView;
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
 * @date zhoujunxia on 2020-03-09 20:46
 */
public class FeedBackActivityPresenter extends BasePresenter<IFeedBackActivityView> {
    public FeedBackActivityPresenter(Context mContext, IFeedBackActivityView iFeedBackActivityView) {
        super(mContext, iFeedBackActivityView);
    }

    /**
     * 账号注销
     */
    public void cancelAccount() {
        HttpParams params = UrlConstants.getParams(mContext);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.CANCELACCOUNT)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<CancelAccountBean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.cancelAccountFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(CancelAccountBean response) {
                        mIView.cancelAccountSuccess(response);
                    }
                });
    }
}
