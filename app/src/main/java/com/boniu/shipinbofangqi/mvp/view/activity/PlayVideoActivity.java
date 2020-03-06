package com.boniu.shipinbofangqi.mvp.view.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.presenter.base.BasePresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;

/**
 * 视频播放界面
 */
public class PlayVideoActivity extends BaseActivity {
    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;
    private String video_name;
    private String video_url;
    OrientationUtils orientationUtils;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_play_video;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        RingLog.e("video_url = " + video_url);
        RingLog.e("video_name = " + video_name);
        videoPlayer.setUp(video_url, true, video_name);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        /*imageView.setImageResource(R.mipmap.xxx1);
        videoPlayer.setThumbImageView(imageView);*/
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }

    @Override
    protected void setView(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        RingLog.e("initData");
        String PLAYVIDEOTIME_TODAY = spUtil.getString(Global.SP_KEY_PLAYVIDEOTIME_TODAY, "");
        int PLAYVIDEONUM_TODAY = spUtil.getInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, 0);
        if (StringUtil.isNotEmpty(PLAYVIDEOTIME_TODAY)) {
            if (PLAYVIDEOTIME_TODAY.equals(CommonUtil.getCurrentDate())) {
                spUtil.saveInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, PLAYVIDEONUM_TODAY++);
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
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        int PLAYVIDEONUM_TODAY = spUtil.getInt(Global.SP_KEY_PLAYVIDEONUM_TODAY, 0);
        if (PLAYVIDEONUM_TODAY >= 2) {
            boolean isBounced = false;
            String MARKETCLICKTIME = spUtil.getString(Global.SP_KEY_MARKETCLICKTIME, "");
            int MARKETCLICKTYPE = spUtil.getInt(Global.SP_KEY_MARKETCLICKTYPE, 0);
            String currentDate = CommonUtil.getCurrentDate();
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
                MessageDialog.show(mActivity, "Wo～用的怎么样？如果还行，请为我点个赞！", "", "喜欢，好评打赏", "继续逛逛", "不喜欢，去吐槽")
                        .setButtonOrientation(LinearLayout.VERTICAL).setCancelable(false).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        spUtil.getInt(Global.SP_KEY_MARKETCLICKTYPE, 1);
                        spUtil.getString(Global.SP_KEY_MARKETCLICKTIME, CommonUtil.getCurrentDate());
                        CommonUtil.goMarket(mContext);
                        return false;
                    }
                }).setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        spUtil.getInt(Global.SP_KEY_MARKETCLICKTYPE, 2);
                        spUtil.getString(Global.SP_KEY_MARKETCLICKTIME, CommonUtil.getCurrentDate());
                        return false;
                    }
                }).setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        spUtil.getInt(Global.SP_KEY_MARKETCLICKTYPE, 3);
                        spUtil.getString(Global.SP_KEY_MARKETCLICKTIME, CommonUtil.getCurrentDate());
                        startActivity(FeedBackActivity.class);
                        return false;
                    }
                });
            }
        }
        super.onBackPressed();
    }
}
