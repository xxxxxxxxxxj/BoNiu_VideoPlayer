package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IAddFeedBackActivityView;
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
 * @date zhoujunxia on 2020-03-09 20:05
 */
public class AddFeedBackActivityPresenter extends BasePresenter<IAddFeedBackActivityView> {
    public AddFeedBackActivityPresenter(Context mContext, IAddFeedBackActivityView iAddFeedBackActivityView) {
        super(mContext, iAddFeedBackActivityView);
    }

    /**
     * 提交反馈
     */
    public void addFeedBack(String type, String content) {
        HttpParams params = UrlConstants.getParams(mContext);
        params.put("type", type);
        params.put("content", content);
        params.put("accountId", CommonUtil.getAccountId(mContext));
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.ADDFEEDBACK)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<Boolean>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                        mIView.addFeedBackFail(e.getCode(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean response) {
                        mIView.addFeedBackSuccess(response);
                    }
                });
    }
}
