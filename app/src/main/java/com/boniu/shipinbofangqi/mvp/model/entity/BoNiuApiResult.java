package com.boniu.shipinbofangqi.mvp.model.entity;

import com.zhouyou.http.model.ApiResult;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 15:21
 */
public class BoNiuApiResult<T> extends ApiResult<T> {
    String errorCode;
    String errorMsg;
    String returnCode;
    T result;

    @Override
    public int getCode() {
        return Integer.parseInt(errorCode);
    }

    @Override
    public String getMsg() {
        return errorMsg;
    }


    @Override
    public T getData() {
        return result;
    }

    @Override
    public boolean isOk() {
        return Integer.parseInt(returnCode) == 200;//如果不是0表示成功，请重写isOk()方法。
    }

    @Override
    public String toString() {
        return "BoNiuApiResult{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", returnCode='" + returnCode + '\'' +
                ", result=" + result +
                '}';
    }
}
