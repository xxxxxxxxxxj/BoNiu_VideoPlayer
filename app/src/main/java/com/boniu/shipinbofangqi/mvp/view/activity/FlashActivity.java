package com.boniu.shipinbofangqi.mvp.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.fingerprintrecognition.FingerprintCore;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.presenter.FlashActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IFlashActivityView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.CountdownUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.JumpToUtil;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
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
        DialogSettings.init();
        boolean isUseBlur = DialogSettings.checkRenderscriptSupport(this);
        Log.e("TAG", "isUseBlur = " + isUseBlur);
        DialogSettings.DEBUGMODE = true;
        DialogSettings.isUseBlur = isUseBlur;
        DialogSettings.autoShowInputKeyboard = true;
        //DialogSettings.backgroundColor = Color.BLUE;
        //DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
        //DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
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
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
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
                    fingerNum = 0;
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
        startAgainfingerDialog = MessageDialog.show(mActivity, "", "", "取消")
                .setButtonOrientation(LinearLayout.VERTICAL)
                .setCustomView(R.layout.layout_startagainfinger_dialog, new MessageDialog.OnBindView() {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {
                    }
                }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        mFingerprintCore.cancelAuthenticate();
                        return false;
                    }
                }).setCancelable(false);
    }

    private void showFingerFailDialog() {
        fingerFailDialog = MessageDialog.show(mActivity, "", "", "取消")
                .setCustomView(R.layout.layout_fingerfail_dialog, new MessageDialog.OnBindView() {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {
                    }
                }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        mFingerprintCore.cancelAuthenticate();
                        return false;
                    }
                }).setCancelable(false);
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
    public void getAccountInfoSuccess(AccountInfoBean response) {
        RingLog.e("getAccountInfoSuccess() response = " + response);
        hideLoadDialog();
        spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, true);
        spUtil.saveString(Global.SP_KEY_CELLPHONE, response.getMobile());
        if (response != null) {
            if (StringUtil.isNotEmpty(response.getType()) && response.getType().equals("VIP")) {
                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, true);
            } else if (StringUtil.isNotEmpty(response.getType()) && response.getType().equals("NORMAL")) {
                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, false);
            }
        }
        setJumpLogic();
    }

    @Override
    public void getAccountInfoFail(int status, String desc) {
        hideLoadDialog();
        RingLog.e("getAccountInfoFail() status = " + status + "---desc = " + desc);
        if (status == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
        } else if (status == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        }
        setJumpLogic();
    }

    private void setJumpLogic() {
        boolean ISAGREEPRIVACY = spUtil.getBoolean(Global.SP_KEY_ISAGREEPRIVACY, false);
        //判断是否同意隐私政策
        if (ISAGREEPRIVACY) {
            //判断是否登录
            if (CommonUtil.isLogin(mActivity)) {
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
                final String linkWord1 = "     您好，在您使用本应用前，请您认真阅读并了解";
                final String linkWord2 = "《用户协议》";
                final String linkWord3 = "和";
                final String linkWord4 = "《隐私政策》";
                final String linkWord5 = "。点击“同意”即表示已阅读并同意全部条款。";
                String word = linkWord1 + linkWord2 + linkWord3 + linkWord4 + linkWord5;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(word);
                int index2 = word.indexOf(linkWord2);
                int index4 = word.indexOf(linkWord4);
                spannableStringBuilder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebViewActivity.URL_KEY, "file:///android_asset/network.html");
                        startActivity(WebViewActivity.class, bundle);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(ContextCompat.getColor(mActivity, R.color.a1b91ff));       //设置文件颜色
                    }
                }, index2, index2 + linkWord2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Bundle bundle = new Bundle();
                        bundle.putString(WebViewActivity.URL_KEY, "file:///android_asset/privacy.html");
                        startActivity(WebViewActivity.class, bundle);
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(ContextCompat.getColor(mActivity, R.color.a1b91ff));       //设置文件颜色
                    }
                }, index4, index4 + linkWord4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_privacydialog_msg.setText(spannableStringBuilder);
                tv_privacydialog_msg.setMovementMethod(LinkMovementMethod.getInstance());

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
