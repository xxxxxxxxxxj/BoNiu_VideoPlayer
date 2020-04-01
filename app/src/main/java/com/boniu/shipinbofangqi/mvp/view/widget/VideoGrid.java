package com.boniu.shipinbofangqi.mvp.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
import com.boniu.shipinbofangqi.util.GlideUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.duyin.quickscan.baen.ScanResult;
import com.zhihu.matisse.internal.ui.widget.CheckView;
import com.zhihu.matisse.internal.ui.widget.SquareFrameLayout;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-25 21:27
 */
public class VideoGrid extends SquareFrameLayout {
    private ImageView mThumbnail;
    private CheckView mCheckView;
    private ImageView mGifTag;
    private TextView mVideoDuration;

    public VideoGrid(Context context) {
        super(context);
        init(context);
    }

    public VideoGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.media_grid_content, this, true);
        mThumbnail = (ImageView) findViewById(R.id.media_thumbnail);
        mCheckView = (CheckView) findViewById(R.id.check_view);
        mGifTag = (ImageView) findViewById(R.id.gif);
        mVideoDuration = (TextView) findViewById(R.id.video_duration);
    }

    public void bindMedia(ScanResult item) {
        mCheckView.setChecked(item.isSelect());
        int videoDuration = CommonUtil.getLocalVideoDuration(item.getPath());
        String formatVideoDuration = FileSizeUtil.formatSeconds(videoDuration / 1000);
        RingLog.e("item.getDuration() = " + item.getDuration() + "---videoDuration = " + videoDuration + "---item.getSize() = " + item.getSize());
        StringUtil.setText(mVideoDuration, formatVideoDuration, "", View.VISIBLE, View.GONE);
        GlideUtil.displayVideoCoverImg(getContext(), item.getPath(), mThumbnail);
    }
}
