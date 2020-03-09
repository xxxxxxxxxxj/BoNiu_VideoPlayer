package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:12
 */
public interface IMyFragView extends IBaseView {
    void getAccountInfoSuccess(AccountInfoBean response);

    void getAccountInfoFail(int code, String message);

    void logoutFail(int code, String message);

    void logoutSuccess(Boolean response);
}
