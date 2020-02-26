package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.BannerBean;
import com.boniu.shipinbofangqi.mvp.model.entity.PostBean;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 20:09
 */
public interface INewsFragView extends IBaseView {
    void getPostSuccess(List<PostBean> data);

    void getPostFail(int code, String msg);

    void getBannerSuccess(List<BannerBean> data);

    void getBannerFail(int code, String msg);
}
