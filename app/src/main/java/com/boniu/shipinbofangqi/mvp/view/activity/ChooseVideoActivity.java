package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.adapter.ChooseVideoAdapter;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.duyin.quickscan.QuickScanManager;
import com.duyin.quickscan.baen.ScanResult;
import com.umeng.analytics.MobclickAgent;
import com.zhihu.matisse.internal.ui.widget.MediaGridInset;

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
    private List<ScanResult> videoList = new ArrayList<ScanResult>();
    private ArrayList<String> selectVideoList = new ArrayList<String>();
    private ChooseVideoAdapter chooseVideoAdapter;
    private int maxSelectable;
    private int selectable;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_choose_video;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //getVideoFile(videoList, Environment.getExternalStorageDirectory());
        tvToolbarTitle.setText("选择视频");
        rvChoosevideo.setHasFixedSize(true);//避免每次绘制Item时，不再重新计算Item高度。
        rvChoosevideo.setItemViewCacheSize(20);
        rvChoosevideo.setDrawingCacheEnabled(true);
        rvChoosevideo.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvChoosevideo.setLayoutManager(new GridLayoutManager(mContext, 3));
        chooseVideoAdapter = new ChooseVideoAdapter(R.layout.item_choosevideo, videoList);
        chooseVideoAdapter.setHasStableIds(true);
        int spacing = getResources().getDimensionPixelSize(com.zhihu.matisse.R.dimen.media_grid_spacing);
        rvChoosevideo.addItemDecoration(new MediaGridInset(3, spacing, false));
        rvChoosevideo.setAdapter(chooseVideoAdapter);
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        QuickScanManager.getQuickScanManager().Init(this).getAllResult("", new QuickScanManager.OnResultListener() {
            @Override
            public void ScanSuccess(List<ScanResult> lists) {
                videoList.clear();
                for (ScanResult scanResult : lists) {
                    String name = scanResult.getName();
                    int i = name.lastIndexOf('.');
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
                            videoList.add(scanResult);
                        }
                    }
                }
                RingLog.e("videoList.size() = " + videoList.size());
                chooseVideoAdapter.notifyDataSetChanged();
            }

            @Override
            public void ScanError(String msg) {
                RingToast.show(msg);
            }
        });
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
                    ScanResult scanResult = videoList.get(position);
                    if (scanResult.isSelect()) {
                        selectable = selectable - 1;
                        scanResult.setSelect(false);
                    } else {
                        if (selectable >= maxSelectable) {
                            RingToast.show("最多只能选择" + maxSelectable + "个视频");
                        } else {
                            selectable = selectable + 1;
                            scanResult.setSelect(true);
                        }
                    }
                    chooseVideoAdapter.notifyItemChanged(position);
                    if (selectable > 0) {
                        tvToolbarOther.setVisibility(View.VISIBLE);
                        tvToolbarOther.setText("使用(" + selectable + ")");
                    } else {
                        tvToolbarOther.setVisibility(View.GONE);
                    }
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

    @OnClick({R.id.iv_toolbar_back, R.id.tv_toolbar_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_toolbar_other:
                selectVideoList.clear();
                for (int i = 0; i < videoList.size(); i++) {
                    if (selectVideoList.size() == selectable) {
                        break;
                    } else {
                        if (videoList.get(i).isSelect()) {
                            selectVideoList.add(videoList.get(i).getPath());
                        }
                    }
                }
                /*Intent intent = new Intent();
                intent.putStringArrayListExtra("selectVideoList", selectVideoList);
                setResult(RESULT_OK, intent);*/
                EventBus.getDefault().post(new MatisseDataEvent(null, selectVideoList));
                finish();
                break;
        }
    }

    private void getVideoFile(final List<ScanResult> list, File file) {// 获得视频文件
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
                        list.add(new ScanResult(file.getAbsolutePath()));
                        return true;
                    }
                } else if (file.isDirectory()) {
                    getVideoFile(list, file);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
