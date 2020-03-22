package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.BoNiuFolderInfo;
import com.boniu.shipinbofangqi.mvp.model.event.RefreshVideoEvent;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.FolderAdapter;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuFolderDao;
import com.boniu.shipinbofangqi.sqllite.dao.BoNiuVideoDao;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.FileSizeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择文件夹界面
 */
public class FolderListActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_choosefolder)
    RecyclerView rvChoosefolder;
    @BindView(R.id.srl_choosefolder)
    SmartRefreshLayout srlChoosefolder;
    private BoNiuFolderDao boNiuFolderDao;
    private List<BoNiuFolderInfo> folderList = new ArrayList<BoNiuFolderInfo>();
    private FolderAdapter folderAdapter;
    private int boniu_video_id;
    private int boniu_folder_id;
    private BoNiuVideoDao boNiuVideoDao;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_choose_folder;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        srlChoosefolder.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("文件夹列表");
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        rvChoosefolder.setLayoutManager(new LinearLayoutManager(mActivity));
        folderAdapter = new FolderAdapter(R.layout.item_videofrag_video, folderList, 1);
        rvChoosefolder.setAdapter(folderAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        boniu_video_id = getIntent().getIntExtra("boniu_video_id", 0);
        boniu_folder_id = getIntent().getIntExtra("boniu_folder_id", 0);
        RingLog.e("boniu_video_id = " + boniu_video_id);
        RingLog.e("boniu_folder_id = " + boniu_folder_id);
        boNiuVideoDao = new BoNiuVideoDao(mActivity);
        boNiuFolderDao = new BoNiuFolderDao(mActivity);
    }

    @Override
    protected void initEvent() {
        folderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BoNiuFolderInfo boNiuFolderInfo = folderList.get(position);
                switch (view.getId()) {
                    case R.id.ll_item_videofrag_video_root:
                        if (CommonUtil.isLogin(mActivity)) {
                            boNiuVideoDao.updateVideoFolder(boniu_video_id, boNiuFolderInfo.getBoniu_folder_id());
                            double sizeByFolderId = boNiuVideoDao.getSizeByFolderId(boNiuFolderInfo.getBoniu_folder_id());
                            String formatSize = FileSizeUtil.formatFileSize((long) sizeByFolderId, false);
                            if (formatSize.equals("0.00M")) {
                                sizeByFolderId = 0.00;
                            }
                            boNiuFolderDao.updateFolderSize(boNiuFolderInfo.getBoniu_folder_id(), sizeByFolderId, formatSize);

                            double sizeByFolderId1 = boNiuVideoDao.getSizeByFolderId(boniu_folder_id);
                            String formatSize1 = FileSizeUtil.formatFileSize((long) sizeByFolderId1, false);
                            if (formatSize1.equals("0.00M")) {
                                sizeByFolderId1 = 0.00;
                            }
                            boNiuFolderDao.updateFolderSize(boniu_folder_id, sizeByFolderId1, formatSize1);
                            EventBus.getDefault().post(new RefreshVideoEvent());
                            finish();
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
        folderList.clear();
        folderList.addAll(boNiuFolderDao.getAllByFolderId(boniu_folder_id));
        folderAdapter.notifyDataSetChanged();
        if (folderList.size() <= 0) {
            folderAdapter.setEmptyView(setEmptyViewBase(2, "您还没有创建文件夹", null));
        }
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

    @OnClick({R.id.iv_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
        }
    }
}
