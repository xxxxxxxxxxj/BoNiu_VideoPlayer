package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
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
 * @date zhoujunxia on 2020-03-22 16:24
 */
public class ChooseVideoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int width;

    public ChooseVideoAdapter(int layoutResId, List<String> data, int width) {
        super(layoutResId, data);
        this.width = width;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView media_thumbnail = helper.getView(R.id.media_thumbnail);
        TextView video_duration = helper.getView(R.id.video_duration);
        RelativeLayout rl_itemchoosevideo_root = helper.getView(R.id.rl_itemchoosevideo_root);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) rl_itemchoosevideo_root.getLayoutParams();
        params.width = width;
        params.height = width;
        rl_itemchoosevideo_root.setLayoutParams(params);
        if (item != null) {
            int videoDuration = CommonUtil.getLocalVideoDuration(item);
            String formatVideoDuration = FileSizeUtil.formatSeconds(videoDuration / 1000);
            StringUtil.setText(video_duration, formatVideoDuration, "", View.VISIBLE, View.GONE);
            GlideUtil.displayVideoCoverImg(mContext, item, media_thumbnail);
        }
    }
}
