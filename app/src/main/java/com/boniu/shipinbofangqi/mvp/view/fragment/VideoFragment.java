package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuFolderInfo;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.mvp.model.event.GestureSuccessEvent;
import com.boniu.shipinbofangqi.mvp.model.event.LoginEvent;
import com.boniu.shipinbofangqi.mvp.model.event.MarketDialogEvent;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.model.event.RefreshVideoEvent;
import com.boniu.shipinbofangqi.mvp.presenter.VideoFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.FeedBackActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.FolderListActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.LoginActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.MemberActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.PlayVideoActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.StartGesturesActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.VideoListActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.FolderAdapter;
import com.boniu.shipinbofangqi.mvp.view.adapter.VideoAdapter;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IVideoFragView;
import com.boniu.shipinbofangqi.mvp.view.widget.NoScollFullLinearLayoutManager;
import com.boniu.shipinbofangqi.permission.PermissionListener;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuFolderDao;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuVideoDao;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.QMUIDeviceHelper;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

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
    private BoNiuFolderInfo boNiuFolderInfo;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(GestureSuccessEvent event) {
        if (event != null && event.getType() == 2 && boNiuFolderInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("boniu_folder_id", boNiuFolderInfo.getBoniu_folder_id());
            bundle.putString("boniu_folder_name", boNiuFolderInfo.getBoniu_folder_name());
            startActivity(VideoListActivity.class, bundle);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(RefreshVideoEvent event) {
        if (event != null) {
            setData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(LoginEvent event) {
        if (event != null) {
            setData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(MarketDialogEvent event) {
        if (event != null) {
            CustomDialog.build(mActivity, R.layout.layout_market_dialog, new CustomDialog.OnBindView() {
                @Override
                public void onBind(final CustomDialog dialog, View v) {
                    TextView tv_marketdialog_one = v.findViewById(R.id.tv_marketdialog_one);
                    TextView tv_marketdialog_two = v.findViewById(R.id.tv_marketdialog_two);
                    TextView tv_marketdialog_three = v.findViewById(R.id.tv_marketdialog_three);
                    tv_marketdialog_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            spUtil.saveInt(Global.SP_KEY_MARKETCLICKTYPE, 1);
                            spUtil.saveString(Global.SP_KEY_MARKETCLICKTIME, CommonUtil.getCurrentDate());
                            CommonUtil.goMarket(mActivity);
                            dialog.doDismiss();
                        }
                    });
                    tv_marketdialog_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            spUtil.saveInt(Global.SP_KEY_MARKETCLICKTYPE, 2);
                            spUtil.saveString(Global.SP_KEY_MARKETCLICKTIME, CommonUtil.getCurrentDate());
                            dialog.doDismiss();
                        }
                    });
                    tv_marketdialog_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            spUtil.saveInt(Global.SP_KEY_MARKETCLICKTYPE, 3);
                            spUtil.saveString(Global.SP_KEY_MARKETCLICKTIME, CommonUtil.getCurrentDate());
                            startActivity(FeedBackActivity.class);
                            dialog.doDismiss();
                        }
                    });
                }
            }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(MatisseDataEvent event) {
        if (event != null) {
            List<String> videoUrls = event.getStrings();
            if (videoUrls != null && videoUrls.size() > 0) {
                for (int i = 0; i < videoUrls.size(); i++) {
                    String videoUrl = videoUrls.get(i);
                    RingLog.e("videoUrl = " + videoUrl);
                    if (StringUtil.isNotEmpty(videoUrl)) {
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
                        boNiuVideoInfo.setBoniu_video_account(CommonUtil.getCellPhone(mActivity));
                        boNiuVideoDao.add(boNiuVideoInfo);
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
        if (!boNiuFolderDao.isExists()) {
            boNiuFolderDao.add(new BoNiuFolderInfo("默认文件夹", CommonUtil.getCurrentTime(), 1, CommonUtil.getCellPhone(mActivity)));
        }
        setData();
    }

    private void setData() {
        videoList.clear();
        videoList.addAll(boNiuVideoDao.getAll());
        folderList.clear();
        folderList.addAll(boNiuFolderDao.getAll());
        videoAdapter.notifyDataSetChanged();
        folderAdapter.notifyDataSetChanged();
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
                    case R.id.ll_item_videofrag_video_root://播放视频
                        if (CommonUtil.isLogin(mActivity)) {
                            requestEachCombined(new PermissionListener() {
                                @Override
                                public void onGranted(String permissionName) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("video_url", boNiuVideoInfo.getBoniu_video_url());
                                    bundle.putString("video_name", boNiuVideoInfo.getBoniu_video_name());
                                    startActivity(PlayVideoActivity.class, bundle);
                                }

                                @Override
                                public void onDenied(String permissionName) {
                                    showToast("没获取到sd卡权限，无法播放本地视频哦");
                                }

                                @Override
                                public void onDeniedWithNeverAsk(String permissionName) {
                                    MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                        @Override
                                        public boolean onClick(BaseDialog baseDialog, View v) {
                                            QMUIDeviceHelper.goToPermissionManager(mActivity);
                                            return true;
                                        }
                                    });
                                }
                            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                        } else {
                            startActivity(LoginActivity.class);
                        }
                        break;
                    case R.id.iv_item_videofrag_video_operation:
                        if (CommonUtil.isLogin(mActivity)) {
                            BottomMenu.show(mActivity, new String[]{"重命名", "移动", "删除"}, new OnMenuItemClickListener() {
                                @Override
                                public void onClick(String text, int index) {
                                    int boniu_video_id = boNiuVideoInfo.getBoniu_video_id();
                                    String boniu_video_name = boNiuVideoInfo.getBoniu_video_name();
                                    if (index == 0) {
                                        String[] split = boniu_video_name.split("\\.");
                                        RingLog.e("boniu_video_name = " + boniu_video_name);
                                        RingLog.e("split = " + split.length);
                                        RingLog.e("split = " + split.toString());
                                        InputDialog.build(mActivity)
                                                .setTitle("重命名").setMessage("")
                                                .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                                    @Override
                                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                                        if (StringUtil.isNotEmpty(inputStr)) {
                                                            boNiuVideoDao.updateVideoName(boniu_video_id, inputStr + "." + split[1]);
                                                            setData();
                                                            RingToast.show("视频名称修改成功");
                                                            return false;
                                                        } else {
                                                            RingToast.show("请输入视频名称");
                                                            return true;
                                                        }
                                                    }
                                                })
                                                .setCancelButton("取消")
                                                .setHintText("请输入视频名称")
                                                .setInputText(split[0])
                                                .setCancelable(false)
                                                .show();
                                    } else if (index == 1) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("boniu_video_id", boniu_video_id);
                                        startActivity(FolderListActivity.class, bundle);
                                    } else if (index == 2) {
                                        MessageDialog.show(mActivity, "删除视频", "确定删除视频吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                boNiuVideoDao.deleteById(boniu_video_id);
                                                setData();
                                                RingToast.show("视频删除成功");
                                                return false;
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            startActivity(LoginActivity.class);
                        }
                        break;
                }
            }
        });
        folderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                boNiuFolderInfo = folderList.get(position);
                switch (view.getId()) {
                    case R.id.ll_item_videofrag_video_root:
                        if (CommonUtil.isLogin(mActivity)) {
                            //判断是否开启加密文件夹
                            boolean ISOPENENCRYPTEDFOLDER = spUtil.getBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
                            if (ISOPENENCRYPTEDFOLDER) {
                                requestEachCombined(new PermissionListener() {
                                    @Override
                                    public void onGranted(String permissionName) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("type", 2);
                                        startActivity(StartGesturesActivity.class, bundle);
                                    }

                                    @Override
                                    public void onDenied(String permissionName) {
                                        showToast("请打开存储权限");
                                    }

                                    @Override
                                    public void onDeniedWithNeverAsk(String permissionName) {
                                        MessageDialog.show(mActivity, "请打开存储权限", "确定要打开存储权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                                return false;
                                            }
                                        });
                                    }
                                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt("boniu_folder_id", boNiuFolderInfo.getBoniu_folder_id());
                                bundle.putString("boniu_folder_name", boNiuFolderInfo.getBoniu_folder_name());
                                startActivity(VideoListActivity.class, bundle);
                            }
                        } else {
                            startActivity(LoginActivity.class);
                        }
                        break;
                    case R.id.iv_item_videofrag_video_operation:
                        if (CommonUtil.isLogin(mActivity)) {
                            BottomMenu.show(mActivity, new String[]{"重命名", "删除"}, new OnMenuItemClickListener() {
                                @Override
                                public void onClick(String text, int index) {
                                    int boniu_folder_id = boNiuFolderInfo.getBoniu_folder_id();
                                    if (index == 0) {
                                        String boniu_folder_name = boNiuFolderInfo.getBoniu_folder_name();
                                        InputDialog.build(mActivity)
                                                .setTitle("重命名").setMessage("")
                                                .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                                    @Override
                                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                                        if (StringUtil.isNotEmpty(inputStr)) {
                                                            boNiuFolderDao.updateFolderName(boniu_folder_id, inputStr);
                                                            setData();
                                                            RingToast.show("文件夹名称修改成功");
                                                            return false;
                                                        } else {
                                                            RingToast.show("请输入文件夹名称");
                                                            return true;
                                                        }
                                                    }
                                                })
                                                .setCancelButton("取消")
                                                .setHintText("请输入文件夹名称")
                                                .setInputText(boniu_folder_name)
                                                .setCancelable(false)
                                                .show();
                                    } else if (index == 1) {
                                        MessageDialog.show(mActivity, "删除文件夹", "确定删除文件夹吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                            @Override
                                            public boolean onClick(BaseDialog baseDialog, View v) {
                                                boNiuFolderDao.deleteById(boniu_folder_id);
                                                boNiuVideoDao.deleteByFolderId(boniu_folder_id);
                                                setData();
                                                RingToast.show("文件夹删除成功");
                                                return false;
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            startActivity(LoginActivity.class);
                        }
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
            case R.id.iv_toolbar_right://判断是否开通高级版
                if (CommonUtil.isLogin(mActivity)) {
                    boolean ISOPENENVIP = spUtil.getBoolean(Global.SP_KEY_ISOPENENVIP, false);
                    if (ISOPENENVIP) {
                        InputDialog.build(mActivity)
                                .setTitle("新建文件夹").setMessage("")
                                .setOkButton("确定", new OnInputDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                        if (StringUtil.isNotEmpty(inputStr)) {
                                            if (!boNiuFolderDao.isExists(inputStr)) {
                                                boNiuFolderDao.add(new BoNiuFolderInfo(inputStr, CommonUtil.getCurrentTime(), CommonUtil.getCellPhone(mActivity)));
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
                    } else {
                        //弹出高级版开通弹窗
                        CustomDialog.build(mActivity, R.layout.layout_openvip_dialog, new CustomDialog.OnBindView() {
                            @Override
                            public void onBind(final CustomDialog dialog, View v) {
                                ImageView iv_openvipdialog_close = v.findViewById(R.id.iv_openvipdialog_close);
                                TextView tv_openvipdialog_open = v.findViewById(R.id.tv_openvipdialog_open);
                                iv_openvipdialog_close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.doDismiss();
                                    }
                                });
                                tv_openvipdialog_open.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(MemberActivity.class);
                                        dialog.doDismiss();
                                    }
                                });
                            }
                        }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false).show();
                    }
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_fragvideo_input:
                if (CommonUtil.isLogin(mActivity)) {
                    chooseVideo(9);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    // Fragment页面onResume函数重载
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideoFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    // Fragment页面onResume函数重载
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VideoFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
