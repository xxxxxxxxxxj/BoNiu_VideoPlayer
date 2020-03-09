package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.LoginBean;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 15:16
 */
public interface ILoginActivityView extends IBaseView {
    void sendVerifyCodeSuccess(Boolean response);

    void sendVerifyCodeFail(int errorCode, String errorMsg);

    void loginSuccess(LoginBean response);

    void loginFail(int code, String message);
}
