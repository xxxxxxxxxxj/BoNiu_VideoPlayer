package com.boniu.shipinbofangqi.mvp.model.event;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-19 00:24
 */
public class GestureSuccessEvent {
    private int type;

    public GestureSuccessEvent() {
    }

    public GestureSuccessEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
