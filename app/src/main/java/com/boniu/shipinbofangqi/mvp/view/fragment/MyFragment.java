package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.fingerprintrecognition.FingerprintCore;
import com.boniu.shipinbofangqi.fingerprintrecognition.FingerprintUtil;
import com.boniu.shipinbofangqi.mvp.presenter.MyFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.AboutActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.FeedBackActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.LoginActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.MemberActivity;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IMyFragView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.Global;
import com.kongzue.dialog.v3.CustomDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:09
 */
public class MyFragment extends BaseFragment<MyFragPresenter> implements IMyFragView {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.sh_fragmy)
    ImageView sh_fragmy;
    @BindView(R.id.ll_fragmy_folder)
    LinearLayout ll_fragmy_folder;
    @BindView(R.id.ll_fragmy_finger)
    LinearLayout ll_fragmy_finger;
    @BindView(R.id.srl_fragmy)
    SmartRefreshLayout srlFragMy;
    @BindView(R.id.tv_fragmy_login)
    TextView tvFragmyLogin;
    @BindView(R.id.tv_fragmy_senior_state)
    TextView tvFragmySeniorState;
    @BindView(R.id.sh_fragmy_folder)
    ImageView shFragmyFolder;
    private FingerprintCore mFingerprintCore;

    @Override
    protected MyFragPresenter createPresenter() {
        return new MyFragPresenter(mActivity, this);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        srlFragMy.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("我的");
        ivToolbarBack.setVisibility(View.GONE);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));

        mFingerprintCore = new FingerprintCore(mActivity);
        if (mFingerprintCore.isSupport()) {
            ll_fragmy_folder.setVisibility(View.VISIBLE);
            ll_fragmy_finger.setVisibility(View.VISIBLE);
            //判断设备是否录入指纹
            boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
            //判断是否开启指纹支付
            boolean isFinger = spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false);
            if (isFinger && hasEnrolledFingerprints) {
                sh_fragmy.setImageResource(R.mipmap.icon_switch_open);
            } else {
                sh_fragmy.setImageResource(R.mipmap.icon_switch_close);
                spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, false);
                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, false);
            }
        } else {
            ll_fragmy_finger.setVisibility(View.GONE);
            ll_fragmy_folder.setVisibility(View.GONE);
            spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, false);
            spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, false);
        }
        //判断是否开启加密文件夹
        boolean ISOPENENCRYPTEDFOLDER = spUtil.getBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
        if (ISOPENENCRYPTEDFOLDER) {
            shFragmyFolder.setImageResource(R.mipmap.icon_switch_open);
        } else {
            shFragmyFolder.setImageResource(R.mipmap.icon_switch_close);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }

    // Fragment页面onResume函数重载
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MyFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    // Fragment页面onResume函数重载
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MyFragment");
    }

    @OnClick({R.id.rl_fragmy_login, R.id.ll_fragmy_senior, R.id.ll_fragmy_feedback, R.id.ll_fragmy_about, R.id.sh_fragmy, R.id.sh_fragmy_folder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_fragmy_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.ll_fragmy_senior:
                startActivity(MemberActivity.class);
                break;
            case R.id.ll_fragmy_feedback:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.ll_fragmy_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.sh_fragmy:
                boolean isFinger = spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false);
                if (isFinger) {
                    sh_fragmy.setImageResource(R.mipmap.icon_switch_close);
                    spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, false);
                } else {
                    //判断设备是否录入指纹
                    boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                    if (hasEnrolledFingerprints) {
                        sh_fragmy.setImageResource(R.mipmap.icon_switch_open);
                        spUtil.saveBoolean(Global.SP_KEY_ISOPENFINGER, true);
                    } else {
                        RingToast.show("您还没有录制指纹，请录入！");
                        FingerprintUtil.openFingerPrintSettingPage(mActivity);
                    }
                }
                break;
            case R.id.sh_fragmy_folder:
                boolean ISOPENENCRYPTEDFOLDER = spUtil.getBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
                if (ISOPENENCRYPTEDFOLDER) {
                    shFragmyFolder.setImageResource(R.mipmap.icon_switch_close);
                    spUtil.saveBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
                } else {
                    //判断是否开通高级版
                    boolean ISOPENENVIP = spUtil.getBoolean(Global.SP_KEY_ISOPENENVIP, false);
                    if (ISOPENENVIP) {
                        //再判断是否录入指纹
                        boolean hasEnrolledFingerprints = mFingerprintCore.isHasEnrolledFingerprints();
                        if (hasEnrolledFingerprints) {
                            //再判断是否开启指纹识别
                            boolean isFinger1 = spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false);
                            if (isFinger1) {
                                shFragmyFolder.setImageResource(R.mipmap.icon_switch_open);
                                spUtil.saveBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, true);
                            } else {
                                RingToast.show("请先开启指纹识别！");
                            }
                        } else {
                            RingToast.show("您还没有录制指纹，请录入！");
                            FingerprintUtil.openFingerPrintSettingPage(mActivity);
                        }
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
                }
                break;
        }
    }
}
