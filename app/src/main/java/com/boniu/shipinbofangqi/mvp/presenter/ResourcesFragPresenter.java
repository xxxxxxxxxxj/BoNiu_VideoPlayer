package com.boniu.shipinbofangqi.mvp.presenter;

import android.content.Context;

import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.iview.IResourcesFragView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-29 09:49
 */
public class ResourcesFragPresenter extends BasePresenter<IResourcesFragView> {
    public ResourcesFragPresenter(Context mContext, IResourcesFragView iResourcesFragView) {
        super(mContext, iResourcesFragView);
    }
}