package com.boniu.shipinbofangqi.mvp.view.iview;

import com.boniu.shipinbofangqi.mvp.model.entity.PayChannel;
import com.boniu.shipinbofangqi.mvp.model.entity.PayInfo;
import com.boniu.shipinbofangqi.mvp.model.entity.PayResult;
import com.boniu.shipinbofangqi.mvp.model.entity.ProductInfo;
import com.boniu.shipinbofangqi.mvp.view.iview.base.IBaseView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 22:01
 */
public interface IMemberActivityView  extends IBaseView {
    void productListSuccess(List<ProductInfo> response);

    void productListFail(int errorCode, String errorMsg);

    void payChannelFail(int code, String message);

    void payChannelSuccess(List<PayChannel> response);

    void orderCreateSuccess(String response);

    void orderCreateFail(int code, String message);

    void submitOrderFail(int code, String message);

    void submitOrderSuccess(PayInfo response);

    void queryPayOrderSuccess(PayResult response);

    void queryPayOrderFail(int code, String message);
}
