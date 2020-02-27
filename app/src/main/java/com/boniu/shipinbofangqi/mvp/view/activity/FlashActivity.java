package com.boniu.shipinbofangqi.mvp.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.fingerprintrecognition.FingerprintCore;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.presenter.FlashActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IFlashActivityView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CountdownUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.JumpToUtil;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动页
 */
public class FlashActivity extends BaseActivity<FlashActivityPresenter> implements IFlashActivityView {
    @BindView(R.id.tv_flash_shouquan)
    TextView tv_flash_shouquan;
    @BindView(R.id.btn_flash_skip)
    Button btnFlashSkip;
    private FingerprintCore mFingerprintCore;
    private CustomDialog startfingerDialog;
    private MessageDialog startAgainfingerDialog;
    private MessageDialog fingerFailDialog;
    private int fingerNum;
    private ImageView iv_startagainfingerdialog;
    private TextView tv_startagainfingerdialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_flash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            RingLog.e("url:" + uri);

            // scheme部分
            String scheme = uri.getScheme();
            RingLog.e("scheme:" + scheme);

            // host部分
            String host = uri.getHost();
            RingLog.e("host:" + host);

            // port部分
            int port = uri.getPort();
            RingLog.e("port:" + port);

            // 访问路劲
            String path = uri.getPath();
            RingLog.e("path:" + path);

            List<String> pathSegments = uri.getPathSegments();

            // Query部分
            String query = uri.getQuery();
            RingLog.e("query:" + query);

            //获取指定参数值
            String point = uri.getQueryParameter("point");
            RingLog.e("point:" + point);
            String backup = uri.getQueryParameter("backup");
            RingLog.e("backup:" + backup);
            JumpToUtil.jumpTo(mActivity, Integer.parseInt(point), backup);
        }
        btnFlashSkip.setVisibility(View.GONE);/*
        CountdownUtil.getInstance().newTimer(3000, 1000, new CountdownUtil.ICountDown() {
            @Override
            public void onTick(long millisUntilFinished) {
                btnFlashSkip.setText("跳过  " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                startActivity(MainActivity.class, true);
            }
        }, "FLASH_TIMER");*/
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {
        showLoadDialog();
        mPresenter.getAccountInfo();
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected FlashActivityPresenter createPresenter() {
        return new FlashActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAllowFullScreen(true);
        super.onCreate(savedInstanceState);
        setSwipeBack(false);
    }

    @OnClick({R.id.btn_flash_skip, R.id.rl_flash_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_flash_skip:
                startActivity(MainActivity.class, true);
                break;
            case R.id.rl_flash_root:
                //判断是否开启指纹识别
                boolean ISOPENFINGER = spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false);
                if (ISOPENFINGER) {
                    //开启指纹识别
                    if (!mFingerprintCore.isAuthenticating()) {
                        mFingerprintCore.startAuthenticate();
                    }
                    startFingerDialog();
                }
                break;
        }
    }

    private void startFingerDialog() {
        startfingerDialog = CustomDialog.build(mActivity, R.layout.layout_startfinger_dialog, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
            }
        }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false);
        startfingerDialog.show();
    }

    private void startAgainFingerDialog() {
        startAgainfingerDialog = MessageDialog.show(mActivity, "", "", "再次尝试指纹识别", "取消")
                .setCustomView(R.layout.layout_startagainfinger_dialog, new MessageDialog.OnBindView() {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {
                        iv_startagainfingerdialog = v.findViewById(R.id.iv_startagainfingerdialog);
                        tv_startagainfingerdialog = v.findViewById(R.id.tv_startagainfingerdialog);
                    }
                }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        iv_startagainfingerdialog.setImageResource(R.mipmap.icon_zhiwen);
                        tv_startagainfingerdialog.setText("指纹识别");
                        //开启指纹识别
                        if (!mFingerprintCore.isAuthenticating()) {
                            mFingerprintCore.startAuthenticate();
                        }
                        return true;
                    }
                });
    }

    private void showFingerFailDialog() {
        fingerFailDialog = MessageDialog.show(mActivity, "", "", "", "取消")
                .setCustomView(R.layout.layout_fingerfail_dialog, new MessageDialog.OnBindView() {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {
                    }
                });
    }

    private FingerprintCore.IFingerprintResultListener mResultListener = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            if (startfingerDialog != null) {
                startfingerDialog.doDismiss();
            }
            startfingerDialog = null;
            if (startAgainfingerDialog != null) {
                startAgainfingerDialog.doDismiss();
            }
            startAgainfingerDialog = null;
            if (fingerFailDialog != null) {
                fingerFailDialog.doDismiss();
            }
            fingerFailDialog = null;
            mFingerprintCore.cancelAuthenticate();
            RingToast.show("验证成功");
            //直接进入首页
            startActivity(MainActivity.class, true);
        }

        @Override
        public void onAuthenticateFailed(int helpId) {
            mFingerprintCore.cancelAuthenticate();
            Log.e("TAG", "onAuthenticateFailed");
            fingerNum++;
            if (startfingerDialog != null) {
                startfingerDialog.doDismiss();
            }
            startfingerDialog = null;
            if (startAgainfingerDialog != null) {
                startAgainfingerDialog.doDismiss();
            }
            startAgainfingerDialog = null;
            if (fingerFailDialog != null) {
                fingerFailDialog.doDismiss();
            }
            fingerFailDialog = null;
            if (fingerNum == 1) {//第一次验证失败
                startAgainFingerDialog();
            } else if (fingerNum == 2) {//第二次验证失败
                showFingerFailDialog();
            }
        }

        @Override
        public void onAuthenticateError(int errMsgId) {
            Log.e("TAG", "onAuthenticateError");
            mFingerprintCore.cancelAuthenticate();
        }

        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFingerprintCore.onDestroy();
        CountdownUtil.getInstance().cancel("FLASH_TIMER");
    }

    @Override
    public void getAccountInfoSuccess(AccountInfoBean accountInfoBean) {
        RingLog.e("getAccountInfoSuccess() accountInfoBean = " + accountInfoBean);
        hideLoadDialog();
        spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, true);
        setJumpLogic();
    }

    @Override
    public void getAccountInfoFail(int status, String desc) {
        hideLoadDialog();
        RingLog.e("checkVersionFail() status = " + status + "---desc = " + desc);
        if (status == AppConfig.EXIT_USER_CODE) {
            spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, false);
        }
        setJumpLogic();
    }

    private void setJumpLogic() {
        boolean ISAGREEPRIVACY = spUtil.getBoolean(Global.SP_KEY_ISAGREEPRIVACY, false);
        //判断是否同意隐私政策
        if (ISAGREEPRIVACY) {
            boolean ISLOGIN = spUtil.getBoolean(Global.SP_KEY_ISLOGIN, false);
            //判断是否登录
            if (ISLOGIN) {
                //判断是否开启指纹识别
                boolean ISOPENFINGER = spUtil.getBoolean(Global.SP_KEY_ISOPENFINGER, false);
                if (ISOPENFINGER) {
                    tv_flash_shouquan.setVisibility(View.VISIBLE);
                    //开启指纹识别
                    if (!mFingerprintCore.isAuthenticating()) {
                        mFingerprintCore.startAuthenticate();
                    }
                    startFingerDialog();
                } else {
                    //直接进入首页
                    startActivity(MainActivity.class, true);
                }
            } else {
                //判断是否跳转登录
                boolean ISJUMPLOGIN = spUtil.getBoolean(Global.SP_KEY_ISJUMPLOGIN, false);
                if (ISJUMPLOGIN) {
                    //直接进入首页
                    startActivity(MainActivity.class, true);
                } else {
                    spUtil.saveBoolean(Global.SP_KEY_ISJUMPLOGIN, true);
                    //先进入首页
                    startActivity(MainActivity.class);
                    //再进入登录页
                    startActivity(LoginActivity.class, true);
                }
            }
        } else {
            //弹出隐私协议提示框
            setPrivacyDialog();
        }
    }

    private void setPrivacyDialog() {
        CustomDialog.build(mActivity, R.layout.layout_privacy_dialog, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                TextView tv_privacydialog_msg = v.findViewById(R.id.tv_privacydialog_msg);
                TextView tv_privacydialog_yes = v.findViewById(R.id.tv_privacydialog_yes);
                TextView tv_privacydialog_no = v.findViewById(R.id.tv_privacydialog_no);

                tv_privacydialog_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                        spUtil.saveBoolean(Global.SP_KEY_ISAGREEPRIVACY, true);
                        spUtil.saveBoolean(Global.SP_KEY_ISJUMPLOGIN, true);
                        //先进入首页
                        startActivity(MainActivity.class);
                        //再进入登录页
                        startActivity(LoginActivity.class, true);
                    }
                });
                tv_privacydialog_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                        setExitAppDialog();
                    }
                });
            }
        }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false).show();
    }

    private void setExitAppDialog() {
        CustomDialog.build(mActivity, R.layout.layout_exitapp_dialog, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                TextView tv_exitappdialog_yes = v.findViewById(R.id.tv_exitappdialog_yes);
                TextView tv_exitappdialog_no = v.findViewById(R.id.tv_exitappdialog_no);

                tv_exitappdialog_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                        setPrivacyDialog();
                    }
                });
                tv_exitappdialog_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.doDismiss();
                        activityListManager.exitApp();
                    }
                });
            }
        }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false).show();
    }
}
