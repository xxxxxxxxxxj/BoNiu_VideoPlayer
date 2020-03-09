package com.boniu.shipinbofangqi.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 20:47
 */
public class CancelAccountBean {
    private String applyTime;

    @Override
    public String toString() {
        return "CancelAccountBean{" +
                "applyTime='" + applyTime + '\'' +
                '}';
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }
}
