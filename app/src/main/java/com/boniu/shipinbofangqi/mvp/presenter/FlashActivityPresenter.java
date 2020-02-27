package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IFlashActivityView;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

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
        EasyHttp.post(UrlConstants.GETACCOUNTINFO)
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
