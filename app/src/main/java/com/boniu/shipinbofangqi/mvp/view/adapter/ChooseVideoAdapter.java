package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.util.GlideUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
public class ChooseVideoAdapter extends BaseQuickAdapter<BoNiuVideoInfo, BaseViewHolder> {
    private int width;

    public ChooseVideoAdapter(int layoutResId, List<BoNiuVideoInfo> data,int width) {
        super(layoutResId, data);
        this.width = width;
    }

    @Override
    protected void convert(BaseViewHolder helper, BoNiuVideoInfo item) {
        ImageView media_thumbnail = helper.getView(R.id.media_thumbnail);
        CheckView check_view = helper.getView(R.id.check_view);
        TextView video_duration = helper.getView(R.id.video_duration);
        RelativeLayout rl_itemchoosevideo_root = helper.getView(R.id.rl_itemchoosevideo_root);
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) rl_itemchoosevideo_root.getLayoutParams();
        params.width = width;
        params.height = width;
        rl_itemchoosevideo_root.setLayoutParams(params);
        if (item != null) {
            StringUtil.setText(video_duration, item.getBoniu_video_length(), "", View.VISIBLE, View.GONE);
            GlideUtil.displayVideoCoverImg(mContext, item.getBoniu_video_url(), media_thumbnail);
            check_view.setChecked(item.isBoniu_video_isselect());
        }
    }
}

