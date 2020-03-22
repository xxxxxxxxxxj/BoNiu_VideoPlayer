package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.ChooseVideoAdapter;
import com.boniu.shipinbofangqi.mvp.view.widget.GridSpacingItemDecoration;
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
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_choosevideo)
    RecyclerView rvChoosevideo;
    @BindView(R.id.srl_choosevideo)
    SmartRefreshLayout srlChoosevideo;
    private List<String> videoList = new ArrayList<String>();
    private ChooseVideoAdapter chooseVideoAdapter;

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
    }

    @Override
    protected void initEvent() {
        chooseVideoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (videoList.size() > 0 && videoList.size() > position) {
                    EventBus.getDefault().post(new MatisseDataEvent(videoList.get(position)));
                    finish();
                }
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

    @OnClick({R.id.iv_toolbar_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
        }
    }

    private void getVideoFile(final List<String> list, File file) {// 获得视频文件
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
                        list.add(file.getAbsolutePath());
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
