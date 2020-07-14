package com.boniu.shipinbofangqi.mvp.view.activity;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.model.event.GestureSuccessEvent;
import com.boniu.shipinbofangqi.mvp.presenter.FlashActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.IFlashActivityView;
import com.boniu.shipinbofangqi.permission.PermissionListener;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.CountdownUtil;
import com.boniu.shipinbofangqi.util.GetDeviceId;
import com.boniu.shipinbofangqi.util.GetGestures;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.JumpToUtil;
import com.boniu.shipinbofangqi.util.QMUIDeviceHelper;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.umeng.analytics.MobclickAgent;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private String password;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(GestureSuccessEvent event) {
        if (event != null && event.getType() == 1) {
            startActivity(MainActivity.class, true);
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_flash;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DialogSettings.init();
            boolean isUseBlur = DialogSettings.checkRenderscriptSupport(this);
            RingLog.e("TAG", "isUseBlur = " + isUseBlur);
            DialogSettings.DEBUGMODE = true;
            DialogSettings.isUseBlur = isUseBlur;
            DialogSettings.autoShowInputKeyboard = true;
            //DialogSettings.backgroundColor = Color.BLUE;
            //DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
            //DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
            DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
            DialogSettings.theme = DialogSettings.THEME.LIGHT;
        } else {
            RingLog.e("不支持");
        }
    }

    @Override
    protected void initEvent() {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                password = GetGestures.readGestures(mActivity);
                if (StringUtil.isEmpty(GetDeviceId.readDeviceID(mContext))) {
                    GetDeviceId.saveDeviceID(mContext);
                    EasyHttp.getInstance().addCommonHeaders(UrlConstants.getHeaders(mActivity));//设置全局公共头
                }
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
                        return true;
                    }
                });
            }
        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    @Override
    protected void loadData() {
        showLoadDialog();
        mPresenter.getAccountInfo();
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected FlashActivityPresenter createPresenter() {
        return new FlashActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {/*
        setAllowFullScreen(true);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }*/
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
                //判断是否开启手势密码
                if (CommonUtil.isLogin(mActivity) && StringUtil.isNotEmpty(password)) {
                    setGesture();
                }
                break;
        }
    }

    private void setGesture() {
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CountdownUtil.getInstance().cancel("FLASH_TIMER");
    }

    @Override
    public void getAccountInfoSuccess(AccountInfoBean response) {
        RingLog.e("getAccountInfoSuccess() response = " + response);
        hideLoadDialog();
        if (response != null) {
            spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, true);
            spUtil.saveString(Global.SP_KEY_CELLPHONE, response.getMobile());
            if (StringUtil.isNotEmpty(response.getApplyCancelTime())) {
                spUtil.saveString(Global.SP_KEY_CANCELTIME, response.getApplyCancelTime());
            }
            if (StringUtil.isNotEmpty(response.getType()) && response.getType().equals("VIP")) {
                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, true);
                spUtil.saveString(Global.SP_KEY_VALIDITYTIME, response.getVipExpireTime());
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
            RingToast.show("您已在其他设备登录");
            startActivity(LoginActivity.class);
        } else if (status == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            int netWorkState = CommonUtil.getNetWorkState(mActivity);
            if (netWorkState == CommonUtil.NETWORK_NONE) {
                RingToast.show("无网络连接");
            } else {
                //RingToast.show(errorMsg);
            }
        }
        setJumpLogic();
    }

    private void setJumpLogic() {
        boolean ISAGREEPRIVACY = spUtil.getBoolean(Global.SP_KEY_ISAGREEPRIVACY, false);
        //判断是否同意隐私政策
        if (ISAGREEPRIVACY) {
            //判断是否登录
            if (CommonUtil.isLogin(mActivity)) {
                //判断是否开启手势密码
                if (StringUtil.isNotEmpty(password)) {
                    tv_flash_shouquan.setVisibility(View.VISIBLE);
                    setGesture();
                } else {
                    //直接进入首页
                    startActivity(MainActivity.class, true);
                }
            } else {
                //判断是否跳转登录
                boolean ISJUMPLOGIN = spUtil.getBoolean(Global.SP_KEY_ISJUMPLOGIN, false);
                if (ISJUMPLOGIN) {
                    //判断是否开启手势密码
                    /*if (StringUtil.isNotEmpty(password)) {
                        tv_flash_shouquan.setVisibility(View.VISIBLE);
                        setGesture();
                    } else {*/
                    //直接进入首页
                    startActivity(MainActivity.class, true);
                    //}
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
