package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.ChooseVideoAdapter;
import com.boniu.shipinbofangqi.mvp.view.widget.GridSpacingItemDecoration;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
import com.boniu.shipinbofangqi.util.QMUIDisplayHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择视频界面
 */
public class ChooseVideoActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_other)
    TextView tvToolbarOther;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_choosevideo)
    RecyclerView rvChoosevideo;
    @BindView(R.id.srl_choosevideo)
    SmartRefreshLayout srlChoosevideo;
    private List<BoNiuVideoInfo> videoList = new ArrayList<BoNiuVideoInfo>();
    private List<BoNiuVideoInfo> tempVideoList = new ArrayList<BoNiuVideoInfo>();
    private ChooseVideoAdapter chooseVideoAdapter;
    private int maxSelectable;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_choose_video;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        getVideoFile(videoList, Environment.getExternalStorageDirectory());
        tvToolbarTitle.setText("选择视频");
        srlChoosevideo.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        rvChoosevideo.setLayoutManager(new GridLayoutManager(mContext, 3));
        int screenWidth = QMUIDisplayHelper.getScreenWidth(mContext);
        int width = (screenWidth - QMUIDisplayHelper.dp2px(mContext, 5) * 2) / 3;
        chooseVideoAdapter = new ChooseVideoAdapter(R.layout.item_choosevideo, videoList, width);
        rvChoosevideo.addItemDecoration(new GridSpacingItemDecoration(3, QMUIDisplayHelper.dp2px(mContext, 5), QMUIDisplayHelper.dp2px(mContext, 5), false));
        rvChoosevideo.setAdapter(chooseVideoAdapter);
    }

    @Override
    protected void setView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        maxSelectable = getIntent().getIntExtra("maxSelectable", 0);
    }

    @Override
    protected void initEvent() {
        chooseVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (videoList.size() > 0 && videoList.size() > position) {
                    tempVideoList.clear();
                    if (videoList.get(position).isBoniu_video_isselect()) {
                        videoList.get(position).setBoniu_video_isselect(false);
                    } else {
                        int tempMaxSelectable = 0;
                        for (int i = 0; i < videoList.size(); i++) {
                            if (videoList.get(position).isBoniu_video_isselect()) {
                                tempMaxSelectable = tempMaxSelectable + 1;
                            }
                        }
                        if (tempMaxSelectable >= maxSelectable) {
                            RingToast.show("最多选择9个视频");
                        } else {
                            videoList.get(position).setBoniu_video_isselect(true);
                        }
                    }
                    int tempMaxSelectable = 0;
                    for (int i = 0; i < videoList.size(); i++) {
                        if (videoList.get(position).isBoniu_video_isselect()) {
                            tempVideoList.add(videoList.get(position));
                            tempMaxSelectable = tempMaxSelectable + 1;
                        }
                    }
                    if (tempMaxSelectable > 0) {
                        tvToolbarOther.setVisibility(View.VISIBLE);
                        tvToolbarOther.setText("使用(" + tempMaxSelectable + ")");
                    } else {
                        tvToolbarOther.setVisibility(View.GONE);
                    }
                }
                chooseVideoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_toolbar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_toolbar_other:
                EventBus.getDefault().post(new MatisseDataEvent(tempVideoList));
                finish();
                break;
        }
    }

    private void getVideoFile(final List<BoNiuVideoInfo> list, File file) {// 获得视频文件
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // sdCard找到视频名称
                String name = file.getName();
                int i = name.indexOf('.');
                if (i != -1) {
                    name = name.substring(i);
                    if (name.equalsIgnoreCase(".mp4")
                            || name.equalsIgnoreCase(".3gp")
                            || name.equalsIgnoreCase(".wmv")
                            || name.equalsIgnoreCase(".ts")
                            || name.equalsIgnoreCase(".rmvb")
                            || name.equalsIgnoreCase(".mov")
                            || name.equalsIgnoreCase(".m4v")
                            || name.equalsIgnoreCase(".avi")
                            || name.equalsIgnoreCase(".m3u8")
                            || name.equalsIgnoreCase(".3gpp")
                            || name.equalsIgnoreCase(".3gpp2")
                            || name.equalsIgnoreCase(".mkv")
                            || name.equalsIgnoreCase(".flv")
                            || name.equalsIgnoreCase(".divx")
                            || name.equalsIgnoreCase(".f4v")
                            || name.equalsIgnoreCase(".rm")
                            || name.equalsIgnoreCase(".asf")
                            || name.equalsIgnoreCase(".ram")
                            || name.equalsIgnoreCase(".mpg")
                            || name.equalsIgnoreCase(".v8")
                            || name.equalsIgnoreCase(".swf")
                            || name.equalsIgnoreCase(".m2v")
                            || name.equalsIgnoreCase(".asx")
                            || name.equalsIgnoreCase(".ra")
                            || name.equalsIgnoreCase(".ndivx")
                            || name.equalsIgnoreCase(".xvid")) {
                        BoNiuVideoInfo boNiuVideoInfo = new BoNiuVideoInfo();
                        long size = FileSizeUtil.getFileOrFilesSize(file.getAbsolutePath());
                        String formatSize = FileSizeUtil.formatFileSize(size, false);
                        int videoDuration = CommonUtil.getLocalVideoDuration(file.getAbsolutePath());
                        String currentTime = CommonUtil.getCurrentTime();
                        String formatVideoDuration = FileSizeUtil.formatSeconds(videoDuration / 1000);
                        RingLog.e("name = " + name);
                        RingLog.e("size = " + size);
                        RingLog.e("formatSize = " + formatSize);
                        RingLog.e("videoDuration = " + videoDuration);
                        RingLog.e("formatVideoDuration = " + formatVideoDuration);
                        RingLog.e("currentTime = " + currentTime);
                        boNiuVideoInfo.setBoniu_video_url(file.getAbsolutePath());
                        boNiuVideoInfo.setBoniu_video_memory(size);
                        boNiuVideoInfo.setBoniu_video_formatmemory(formatSize);
                        boNiuVideoInfo.setBoniu_video_length(formatVideoDuration);
                        boNiuVideoInfo.setBoniu_video_name(name);
                        boNiuVideoInfo.setBoniu_video_createtime(currentTime);
                        boNiuVideoInfo.setBoniu_video_isselect(false);
                        list.add(boNiuVideoInfo);
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });
    }
}
