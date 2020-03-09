package com.boniu.shipinbofangqi.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 17:08
 */
public class LoginBean {
    private String accountId;
    private String token;

    @Override
    public String toString() {
        return "LoginBean{" +
                "accountId='" + accountId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
