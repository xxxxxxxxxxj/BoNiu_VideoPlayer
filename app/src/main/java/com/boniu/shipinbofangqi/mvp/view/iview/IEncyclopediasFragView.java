package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.Encyclopedias;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 18:47
 */
public interface IEncyclopediasFragView extends IBaseView {
    void getEncyclopediasFail(int code, String message);

    void getEncyclopediasSuccess(List<Encyclopedias> response);
}
