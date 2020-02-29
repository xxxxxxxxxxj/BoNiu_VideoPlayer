package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.util.GlideUtil;
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
 * @date zhoujunxia on 2020-02-29 13:52
 */
public class VideoAdapter extends BaseQuickAdapter<BoNiuVideoInfo, BaseViewHolder> {

    public VideoAdapter(int layoutResId, List<BoNiuVideoInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BoNiuVideoInfo item) {
        ImageView iv_item_videofrag_video_img = helper.getView(R.id.iv_item_videofrag_video_img);
        TextView tv_item_videofrag_video_time = helper.getView(R.id.tv_item_videofrag_video_time);
        TextView tv_item_videofrag_video_memory = helper.getView(R.id.tv_item_videofrag_video_memory);
        TextView tv_item_videofrag_video_length = helper.getView(R.id.tv_item_videofrag_video_length);
        TextView tv_item_videofrag_video_name = helper.getView(R.id.tv_item_videofrag_video_name);
        if (item != null) {
            GlideUtil.displayVideoCoverImg(mContext, item.getBoniu_video_url(), iv_item_videofrag_video_img);
            StringUtil.setText(tv_item_videofrag_video_time, item.getBoniu_video_createtime(), "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_videofrag_video_memory, item.getBoniu_video_formatmemory(), "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_videofrag_video_length, item.getBoniu_video_length(), "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_videofrag_video_name, item.getBoniu_video_name(), "", View.VISIBLE, View.VISIBLE);
            helper.addOnClickListener(R.id.ll_item_videofrag_video_root).addOnClickListener(R.id.iv_item_videofrag_video_operation);
        }
    }
}
