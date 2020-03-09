package com.boniu.shipinbofangqi.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-27 23:00
 */
public class AccountInfoBean {
    private String applyCancelTime;
    private String mobile;
    private String type;
    private String vipExpireTime;

    @Override
    public String toString() {
        return "AccountInfoBean{" +
                "applyCancelTime='" + applyCancelTime + '\'' +
                ", mobile='" + mobile + '\'' +
                ", type='" + type + '\'' +
                ", vipExpireTime='" + vipExpireTime + '\'' +
                '}';
    }

    public String getApplyCancelTime() {
        return applyCancelTime;
    }

    public void setApplyCancelTime(String applyCancelTime) {
        this.applyCancelTime = applyCancelTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVipExpireTime() {
        return vipExpireTime;
    }

    public void setVipExpireTime(String vipExpireTime) {
        this.vipExpireTime = vipExpireTime;
    }
}
