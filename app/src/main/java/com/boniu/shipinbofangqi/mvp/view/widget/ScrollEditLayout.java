package com.boniu.shipinbofangqi.mvp.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-17 20:41
 */
public class ScrollEditLayout extends ScrollView {

    public ScrollEditLayout(Context context) {
        super(context);
    }

    public ScrollEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollEditLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}