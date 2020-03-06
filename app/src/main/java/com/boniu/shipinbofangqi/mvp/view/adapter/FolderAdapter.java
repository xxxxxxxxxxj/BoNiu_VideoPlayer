package com.boniu.shipinbofangqi.mvp.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuFolderInfo;
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
 * @date zhoujunxia on 2020-02-29 13:53
 */
public class FolderAdapter extends BaseQuickAdapter<BoNiuFolderInfo, BaseViewHolder> {
    private int type;

    public FolderAdapter(int layoutResId, List<BoNiuFolderInfo> data) {
        super(layoutResId, data);
    }

    public FolderAdapter(int layoutResId, List<BoNiuFolderInfo> data,int type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, BoNiuFolderInfo item) {
        ImageView iv_item_videofrag_video_img = helper.getView(R.id.iv_item_videofrag_video_img);
        TextView tv_item_videofrag_video_time = helper.getView(R.id.tv_item_videofrag_video_time);
        TextView tv_item_videofrag_video_memory = helper.getView(R.id.tv_item_videofrag_video_memory);
        TextView tv_item_videofrag_video_length = helper.getView(R.id.tv_item_videofrag_video_length);
        TextView tv_item_videofrag_video_name = helper.getView(R.id.tv_item_videofrag_video_name);
        ImageView iv_item_videofrag_video_operation = helper.getView(R.id.iv_item_videofrag_video_operation);
        if (item != null) {
            if(type == 0){
                iv_item_videofrag_video_operation.setVisibility(View.VISIBLE);
            }else if(type == 1){
                iv_item_videofrag_video_operation.setVisibility(View.GONE);
            }
            tv_item_videofrag_video_length.setVisibility(View.GONE);
            iv_item_videofrag_video_img.setImageResource(R.mipmap.icon_folder);
            StringUtil.setText(tv_item_videofrag_video_memory, item.getBoniu_folder_formatmemory(), "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_videofrag_video_time, item.getBoniu_folder_createtime(), "", View.VISIBLE, View.VISIBLE);
            StringUtil.setText(tv_item_videofrag_video_name, item.getBoniu_folder_name(), "", View.VISIBLE, View.VISIBLE);
            helper.addOnClickListener(R.id.ll_item_videofrag_video_root).addOnClickListener(R.id.iv_item_videofrag_video_operation);
        }
    }
}
