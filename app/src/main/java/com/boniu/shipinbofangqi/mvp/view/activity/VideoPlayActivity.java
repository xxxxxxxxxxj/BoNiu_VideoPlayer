package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.StringUtil;

/**
 * 视频播放界面
 */
public class VideoPlayActivity extends BaseActivity {
    //@BindView(R.id.video_player)
    //JzvdStd videoPlayer;
    private String video_name;
    private String video_url;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        /*videoPlayer.setUp(video_url, video_name);
        videoPlayer.startVideo();*/
    }

    @Override
    protected void setView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        String PLAYVIDEOTIME_TODAY = spUtil.getString(Global.SP_KEY_PLAYVIDEOTIME_TODAY, "");
        int PLAYVIDEONUM_TODAY = spUtil.getInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, 0);
        RingLog.e("PLAYVIDEOTIME_TODAY = " + PLAYVIDEOTIME_TODAY);
        RingLog.e("PLAYVIDEONUM_TODAY = " + PLAYVIDEONUM_TODAY);
        RingLog.e("CommonUtil.getCurrentDate() = " + CommonUtil.getCurrentDate());
        RingLog.e("initData");
        if (StringUtil.isNotEmpty(PLAYVIDEOTIME_TODAY)) {
            if (PLAYVIDEOTIME_TODAY.equals(CommonUtil.getCurrentDate())) {
                spUtil.saveInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, (PLAYVIDEONUM_TODAY + 1));
            } else {
                spUtil.saveString(Global.SP_KEY_PLAYVIDEOTIME_TODAY, CommonUtil.getCurrentDate());
                spUtil.saveInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, 1);
            }
        } else {
            spUtil.saveString(Global.SP_KEY_PLAYVIDEOTIME_TODAY, CommonUtil.getCurrentDate());
            spUtil.saveInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, 1);
        }
        video_url = getIntent().getStringExtra("video_url");
        video_name = getIntent().getStringExtra("video_name");
    }

    @Override
    protected void initEvent() {

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

    @Override
    protected void onPause() {
        super.onPause();
        /*JZUtils.clearSavedProgress(this, null);
        Jzvd.releaseAllVideos();*/
    }

    @Override
    public void onBackPressed() {
        /*if (Jzvd.backPress()) {
            return;
        }*/
        int PLAYVIDEONUM_TODAY = spUtil.getInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, 0);
        String PLAYVIDEOTIME_TODAY = spUtil.getString(Global.SP_KEY_PLAYVIDEOTIME_TODAY, "");
        RingLog.e("PLAYVIDEONUM_TODAY = " + PLAYVIDEONUM_TODAY);
        RingLog.e("PLAYVIDEOTIME_TODAY = " + PLAYVIDEOTIME_TODAY);
        if (PLAYVIDEONUM_TODAY >= 2) {
            boolean isBounced = false;
            String MARKETCLICKTIME = spUtil.getString(Global.SP_KEY_MARKETCLICKTIME, "");
            int MARKETCLICKTYPE = spUtil.getInt(Global.SP_KEY_MARKETCLICKTYPE, 0);
            String currentDate = CommonUtil.getCurrentDate();
            long timeDays = CommonUtil.getTimeDays(MARKETCLICKTIME, currentDate);
            RingLog.e("timeDays = " + timeDays);
            if (MARKETCLICKTYPE == 1) {//6个月每次
                if (CommonUtil.getTimeDays(MARKETCLICKTIME, currentDate) > 180) {
                    isBounced = true;
                }
            } else if (MARKETCLICKTYPE == 2) {//3个月每次
                if (CommonUtil.getTimeDays(MARKETCLICKTIME, currentDate) > 90) {
                    isBounced = true;
                }
            } else if (MARKETCLICKTYPE == 3) {//1个月每次
                if (CommonUtil.getTimeDays(MARKETCLICKTIME, currentDate) > 30) {
                    isBounced = true;
                }
            } else if (MARKETCLICKTYPE == 0) {//未弹过框
                isBounced = true;
            }
            if (isBounced) {
                //EventBus.getDefault().post(new MarketDialogEvent());
            }
        }
        super.onBackPressed();
    }
}
