package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuVideoInfo;
import com.boniu.shipinbofangqi.mvp.model.event.RefreshVideoEvent;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.VideoAdapter;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuVideoDao;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoListActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.rv_videolist)
    RecyclerView rvVideolist;
    @BindView(R.id.srl_videolist)
    SmartRefreshLayout srlVideolist;
    private String boniu_folder_name;
    private int boniu_folder_id;
    private BoNiuVideoDao boNiuVideoDao;
    private List<BoNiuVideoInfo> videoList = new ArrayList<BoNiuVideoInfo>();
    private VideoAdapter videoAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(RefreshVideoEvent event) {
        if (event != null) {
            setData();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        srlVideolist.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText(boniu_folder_name);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        rvVideolist.setLayoutManager(new LinearLayoutManager(mActivity));
        videoAdapter = new VideoAdapter(R.layout.item_videofrag_video, videoList);
        rvVideolist.setAdapter(videoAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        boniu_folder_id = getIntent().getIntExtra("boniu_folder_id", 0);
        boniu_folder_name = getIntent().getStringExtra("boniu_folder_name");
        boNiuVideoDao = new BoNiuVideoDao(mActivity);
    }

    @Override
    protected void initEvent() {
        videoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BoNiuVideoInfo boNiuVideoInfo = videoList.get(position);
                switch (view.getId()) {
                    case R.id.ll_item_videofrag_video_root://播放
                        break;
                    case R.id.iv_item_videofrag_video_operation:
                        BottomMenu.show(mActivity, new String[]{"重命名", "移动", "移出", "删除"}, new OnMenuItemClickListener() {
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
                                                        EventBus.getDefault().post(new RefreshVideoEvent());
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
                                    boNiuVideoDao.updateVideoFolder(boniu_video_id, 0);
                                    setData();
                                    RingToast.show("视频移出成功");
                                    EventBus.getDefault().post(new RefreshVideoEvent());
                                } else if (index == 3) {
                                    MessageDialog.show(mActivity, "删除视频", "确定删除视频吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                        @Override
                                        public boolean onClick(BaseDialog baseDialog, View v) {
                                            boNiuVideoDao.deleteById(boniu_video_id);
                                            setData();
                                            RingToast.show("视频删除成功");
                                            EventBus.getDefault().post(new RefreshVideoEvent());
                                            return false;
                                        }
                                    });
                                }
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {
        setData();
    }

    private void setData() {
        videoList.clear();
        videoList.addAll(boNiuVideoDao.getAllByFolderId(boniu_folder_id));
        videoAdapter.notifyDataSetChanged();
        if (videoList.size() <= 0) {
            videoAdapter.setEmptyView(setEmptyViewBase(2, "这里是空的哦", null));
        }
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
        }
    }
}
