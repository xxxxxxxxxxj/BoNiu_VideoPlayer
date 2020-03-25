package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
import com.boniu.shipinbofangqi.util.GlideUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duyin.quickscan.baen.ScanResult;
import com.zhihu.matisse.internal.ui.widget.CheckView;

import java.util.List;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-22 16:24
 */
public class ChooseVideoAdapter extends BaseQuickAdapter<ScanResult, BaseViewHolder> {
    private int width;

    public ChooseVideoAdapter(int layoutResId, List<ScanResult> data, int width) {
        super(layoutResId, data);
        this.width = width;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScanResult item) {
        CheckView check_view = helper.getView(R.id.check_view);
        ImageView media_thumbnail = helper.getView(R.id.media_thumbnail);
        TextView video_duration = helper.getView(R.id.video_duration);
        if (item != null) {
            check_view.setChecked(item.isSelect());
            int videoDuration = CommonUtil.getLocalVideoDuration(item.getPath());
            String formatVideoDuration = FileSizeUtil.formatSeconds(videoDuration / 1000);
            StringUtil.setText(video_duration, formatVideoDuration, "", View.VISIBLE, View.GONE);
            GlideUtil.displayVideoCoverImg(mContext, item.getPath(), media_thumbnail);
        }
    }
}

