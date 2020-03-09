package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.AppInfoBean;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 10:16
 */
public interface IMainActivityView extends IBaseView {
    void checkVersionSuccess(AppInfoBean response);

    void checkVersionFail(int status, String desc);
}
