package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.CancelAccountBean;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 20:45
 */
public interface IFeedBackActivityView  extends IBaseView {
    void cancelAccountSuccess(CancelAccountBean response);

    void cancelAccountFail(int errorCode, String errorMsg);
}
