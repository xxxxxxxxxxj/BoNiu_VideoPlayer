package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-27 22:35
 */
public interface IFlashActivityView extends IBaseView {
    void getAccountInfoSuccess(AccountInfoBean accountInfoBean);

    void getAccountInfoFail(int status, String desc);
}
