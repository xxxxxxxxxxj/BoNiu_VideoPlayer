package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.entity.PayChannel;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-10 20:37
 */
public class PayWayAdapter extends BaseQuickAdapter<PayChannel, BaseViewHolder> {
    public PayWayAdapter(int layoutResId, List<PayChannel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayChannel item) {
        ImageView iv_paypop_payway = helper.getView(R.id.iv_paypop_payway);
        ImageView iv_paypop_select = helper.getView(R.id.iv_paypop_select);
        TextView tv_paypop_payway = helper.getView(R.id.tv_paypop_payway);
        View vw_paypop_payway = helper.getView(R.id.vw_paypop_payway);
        if (helper.getLayoutPosition() == mData.size() - 1) {
            vw_paypop_payway.setVisibility(View.GONE);
        } else {
            vw_paypop_payway.setVisibility(View.VISIBLE);
        }
        if (item != null) {
            StringUtil.setText(tv_paypop_payway, item.getDesc(), "", View.VISIBLE, View.VISIBLE);
            if (StringUtil.isNotEmpty(item.getType())) {
                if (item.getType().equals("WECHAT_PAY")) {
                    iv_paypop_payway.setImageResource(R.mipmap.icon_wx);
                } else if (item.getType().equals("ALIPAY")) {
                    iv_paypop_payway.setImageResource(R.mipmap.icon_zfb);
                }
            }
            if (item.isSelect()) {
                iv_paypop_select.setImageResource(R.mipmap.icon_select);
            } else {
                iv_paypop_select.setImageResource(R.mipmap.icon_unselect);
            }
        }
    }
}
