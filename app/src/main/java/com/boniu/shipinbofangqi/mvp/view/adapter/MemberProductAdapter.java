package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.entity.ProductInfo;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.SharedPreferenceUtil;
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
 * @date zhoujunxia on 2020-03-10 19:49
 */
public class MemberProductAdapter extends BaseQuickAdapter<ProductInfo, BaseViewHolder> {
    public MemberProductAdapter(int layoutResId, List<ProductInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductInfo item) {
        LinearLayout ll_item_memberproduct_unselect = helper.getView(R.id.ll_item_memberproduct_unselect);
        TextView tv_item_memberproduct_desc_unselect = helper.getView(R.id.tv_item_memberproduct_desc_unselect);
        TextView tv_item_memberproduct_days_unselect = helper.getView(R.id.tv_item_memberproduct_days_unselect);
        TextView tv_item_memberproduct_price_unselect = helper.getView(R.id.tv_item_memberproduct_price_unselect);
        RelativeLayout rl_item_memberproduct_select = helper.getView(R.id.rl_item_memberproduct_select);
        TextView tv_item_memberproduct_desc = helper.getView(R.id.tv_item_memberproduct_desc);
        TextView tv_item_memberproduct_days = helper.getView(R.id.tv_item_memberproduct_days);
        TextView tv_item_memberproduct_price = helper.getView(R.id.tv_item_memberproduct_price);
        TextView tv_item_memberproduct_validitytime = helper.getView(R.id.tv_item_memberproduct_validitytime);
        if (item != null) {
            if (item.isSelect()) {
                ll_item_memberproduct_unselect.setVisibility(View.GONE);
                rl_item_memberproduct_select.setVisibility(View.VISIBLE);
                if (StringUtil.isNotEmpty(SharedPreferenceUtil.getInstance(mContext).getString(Global.SP_KEY_VALIDITYTIME, ""))) {
                    tv_item_memberproduct_validitytime.setVisibility(View.VISIBLE);
                    tv_item_memberproduct_validitytime.setText(SharedPreferenceUtil.getInstance(mContext).getString(Global.SP_KEY_VALIDITYTIME, "") + "到期");
                } else {
                    tv_item_memberproduct_validitytime.setVisibility(View.GONE);
                }
                StringUtil.setText(tv_item_memberproduct_desc, item.getDesc(), "", View.VISIBLE, View.VISIBLE);
                StringUtil.setText(tv_item_memberproduct_days, item.getProductName(), "", View.VISIBLE, View.VISIBLE);
                StringUtil.setText(tv_item_memberproduct_price, "¥" + item.getDiscountPrice(), "", View.VISIBLE, View.VISIBLE);
            } else {
                ll_item_memberproduct_unselect.setVisibility(View.VISIBLE);
                rl_item_memberproduct_select.setVisibility(View.GONE);
                StringUtil.setText(tv_item_memberproduct_desc_unselect, item.getDesc(), "", View.VISIBLE, View.VISIBLE);
                StringUtil.setText(tv_item_memberproduct_days_unselect, item.getProductName(), "", View.VISIBLE, View.VISIBLE);
                StringUtil.setText(tv_item_memberproduct_price_unselect, "¥" + item.getDiscountPrice(), "", View.VISIBLE, View.VISIBLE);
            }
        }
    }
}
