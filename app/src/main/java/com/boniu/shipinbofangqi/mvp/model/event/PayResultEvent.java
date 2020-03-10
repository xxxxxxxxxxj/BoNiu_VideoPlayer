package com.boniu.shipinbofangqi.mvp.model.event;

import com.boniu.shipinbofangqi.mvp.model.entity.PayResult;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-10 14:49
 */
public class PayResultEvent {
    private int code;
    private String msg;
    private PayResult payResult;

    @Override
    public String toString() {
        return "PayResultEvent{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", payResult=" + payResult +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PayResult getPayResult() {
        return payResult;
    }

    public void setPayResult(PayResult payResult) {
        this.payResult = payResult;
    }
}
