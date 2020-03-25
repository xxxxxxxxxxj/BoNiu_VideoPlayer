package com.boniu.shipinbofangqi.mvp.view.adapter;

import com.boniu.shipinbofangqi.mvp.view.widget.VideoGrid;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.duyin.quickscan.baen.ScanResult;

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
    public ChooseVideoAdapter(int layoutResId, List<ScanResult> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScanResult item) {
        VideoGrid videoGrid = (VideoGrid) helper.itemView;
        videoGrid.bindMedia(item);
    }
}

