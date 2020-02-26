package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IMyFragView;

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
}