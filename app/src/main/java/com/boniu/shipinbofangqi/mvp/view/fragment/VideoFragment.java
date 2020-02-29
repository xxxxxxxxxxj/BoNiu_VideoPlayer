package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuFolderInfo;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.presenter.VideoFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.adapter.FolderAdapter;
import com.boniu.shipinbofangqi.mvp.view.adapter.VideoAdapter;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IVideoFragView;
import com.boniu.shipinbofangqi.mvp.view.widget.NoScollFullLinearLayoutManager;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuFolderDao;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuVideoDao;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.InputDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-02-29 09:48
 */
public class VideoFragment extends BaseFragment<VideoFragPresenter> implements IVideoFragView {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.iv_toolbar_right)
    ImageView ivToolbarRight;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.ll_fragvideo_input)
    LinearLayout llFragvideoInput;
    @BindView(R.id.rv_fragvideo_video)
    RecyclerView rvFragvideoVideo;
    @BindView(R.id.rv_fragvideo_folder)
    RecyclerView rvFragvideoFolder;
    @BindView(R.id.srl_fragvideo)
    SmartRefreshLayout srlFragvideo;
    private BoNiuVideoDao boNiuVideoDao;
    private BoNiuFolderDao boNiuFolderDao;
    private List<BoNiuVideoInfo> videoList = new ArrayList<BoNiuVideoInfo>();
    private List<BoNiuFolderInfo> folderList = new ArrayList<BoNiuFolderInfo>();
    private VideoAdapter videoAdapter;
    private FolderAdapter folderAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(MatisseDataEvent event) {
        if (event != null) {
            List<String> videoUrls = event.getStrings();
            List<Uri> uris = event.getUris();
            RingLog.e("uris = " + uris.toString());
            RingLog.e("videoUrls = " + videoUrls.toString());
            if (videoUrls != null && videoUrls.size() > 0) {
                for (int i = 0; i < videoUrls.size(); i++) {
                    String videoUrl = videoUrls.get(i);
                    RingLog.e("videoUrl = " + videoUrl);
                    if (StringUtil.isNotEmpty(videoUrl)) {
                        if (!boNiuVideoDao.isExists(videoUrl)) {
                            BoNiuVideoInfo boNiuVideoInfo = new BoNiuVideoInfo();
                            String videoName = videoUrl.substring(videoUrl.lastIndexOf("/") + 1, videoUrl.length());
                            long size = FileSizeUtil.getFileOrFilesSize(videoUrls.get(i));
                            String formatSize = FileSizeUtil.formatFileSize(size, false);
                            int videoDuration = CommonUtil.getLocalVideoDuration(videoUrls.get(i));
                            String currentTime = CommonUtil.getCurrentTime();
                            String formatVideoDuration = FileSizeUtil.formatSeconds(videoDuration / 1000);
                            RingLog.e("videoName = " + videoName);
                            RingLog.e("size = " + size);
                            RingLog.e("formatSize = " + formatSize);
                            RingLog.e("videoDuration = " + videoDuration);
                            RingLog.e("formatVideoDuration = " + formatVideoDuration);
                            RingLog.e("currentTime = " + currentTime);
                            boNiuVideoInfo.setBoniu_video_url(videoUrl);
                            boNiuVideoInfo.setBoniu_video_memory(size);
                            boNiuVideoInfo.setBoniu_video_formatmemory(formatSize);
                            boNiuVideoInfo.setBoniu_video_length(formatVideoDuration);
                            boNiuVideoInfo.setBoniu_video_name(videoName);
                            boNiuVideoInfo.setBoniu_video_createtime(currentTime);
                            boNiuVideoDao.add(boNiuVideoInfo);
                        }
                    }
                }
                setData();
            }
        }
    }

    @Override
    protected VideoFragPresenter createPresenter() {
        return new VideoFragPresenter(mActivity, this);
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        srlFragvideo.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("视频");
        ivToolbarBack.setVisibility(View.GONE);
        ivToolbarRight.setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        rvFragvideoVideo.setHasFixedSize(true);
        rvFragvideoVideo.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager = new
                NoScollFullLinearLayoutManager(mActivity);
        noScollFullLinearLayoutManager.setScrollEnabled(false);
        rvFragvideoVideo.setLayoutManager(noScollFullLinearLayoutManager);
        videoAdapter = new VideoAdapter(R.layout.item_videofrag_video, videoList);
        rvFragvideoVideo.setAdapter(videoAdapter);

        rvFragvideoFolder.setHasFixedSize(true);
        rvFragvideoFolder.setNestedScrollingEnabled(false);
        NoScollFullLinearLayoutManager noScollFullLinearLayoutManager1 = new
                NoScollFullLinearLayoutManager(mActivity);
        noScollFullLinearLayoutManager1.setScrollEnabled(false);
        rvFragvideoFolder.setLayoutManager(noScollFullLinearLayoutManager1);
        folderAdapter = new FolderAdapter(R.layout.item_videofrag_video, folderList);
        rvFragvideoFolder.setAdapter(folderAdapter);
    }

    @Override
    protected void initData() {
        boNiuVideoDao = new BoNiuVideoDao(mActivity);
        boNiuFolderDao = new BoNiuFolderDao(mActivity);
        setData();
    }

    private void setData() {
        videoList.clear();
        videoList.addAll(boNiuVideoDao.getAll());
        folderList.clear();
        folderList.addAll(boNiuFolderDao.getAll());
        if (videoList != null && videoList.size() > 0) {
            videoAdapter.notifyDataSetChanged();
        }
        if (folderList != null && folderList.size() > 0) {
            folderAdapter.notifyDataSetChanged();
        }
        if (videoList.size() <= 0 && folderList.size() <= 0) {
            llFragvideoInput.setVisibility(View.VISIBLE);
        } else {
            llFragvideoInput.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {
        videoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BoNiuVideoInfo boNiuVideoInfo = videoList.get(position);
                switch (view.getId()) {
                    case R.id.ll_item_videofrag_video_root:
                        break;
                    case R.id.iv_item_videofrag_video_operation:
                        break;
                }
            }
        });
        folderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BoNiuFolderInfo boNiuFolderInfo = folderList.get(position);
                switch (view.getId()) {
                    case R.id.ll_item_videofrag_video_root:
                        break;
                    case R.id.iv_item_videofrag_video_operation:
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.iv_toolbar_right, R.id.ll_fragvideo_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_right:
                DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                DialogSettings.theme = DialogSettings.THEME.LIGHT;
                DialogSettings.tipTheme = DialogSettings.THEME.DARK;
                InputDialog.build(mActivity)
                        .setTitle("新建文件夹").setMessage("请输入文件夹名称")
                        .setOkButton("确定", new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                if (StringUtil.isNotEmpty(inputStr)) {
                                    if (!boNiuFolderDao.isExists(inputStr)) {
                                        boNiuFolderDao.add(new BoNiuFolderInfo(inputStr, CommonUtil.getCurrentTime()));
                                        setData();
                                        RingToast.show("文件夹创建成功");
                                        return false;
                                    } else {
                                        RingToast.show("文件夹已存在");
                                        return true;
                                    }
                                } else {
                                    RingToast.show("请输入文件夹名称");
                                    return true;
                                }
                            }
                        })
                        .setCancelButton("取消")
                        .setHintText("请输入文件夹名称")
                        .setCancelable(false)
                        .show();
                break;
            case R.id.ll_fragvideo_input:
                getVideo(9);
                break;
        }
    }
}
