package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 20:05
 */
public interface IAddFeedBackActivityView extends IBaseView {
    void addFeedBackSuccess(Boolean response);

    void addFeedBackFail(int errorCode, String errorMsg);
}
