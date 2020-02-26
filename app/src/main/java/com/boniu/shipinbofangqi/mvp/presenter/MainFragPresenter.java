package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IMainFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:07
 */
public class MainFragPresenter extends BasePresenter<IMainFragView> {
    public MainFragPresenter(Context mContext, IMainFragView iMainFragView) {
        super(mContext, iMainFragView);
    }
}
