package com.boniu.shipinbofangqi.mvp.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.LoginBean;
import com.boniu.shipinbofangqi.mvp.model.event.LoginEvent;
import com.boniu.shipinbofangqi.mvp.presenter.LoginActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.ILoginActivityView;
import com.boniu.shipinbofangqi.permission.PermissionListener;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.CountdownUtil;
import com.boniu.shipinbofangqi.util.GetDeviceId;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.QMUIDeviceHelper;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.gyf.immersionbar.ImmersionBar;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<LoginActivityPresenter> implements ILoginActivityView {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.et_login_mobile)
    EditText et_login_mobile;
    @BindView(R.id.et_login_yzm)
    EditText et_login_yzm;
    @BindView(R.id.tv_login_yzm)
    TextView tvLoginYzm;
    @BindView(R.id.tv_login_sub)
    TextView tvLoginSub;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.iv_toolbar_back)
    ImageView iv_toolbar_back;
    @BindView(R.id.srl_login)
    SmartRefreshLayout srlLogin;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Override
    protected void setView(Bundle savedInstanceState) {
        srlLogin.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.a2D2D2D));
        iv_toolbar_back.setImageResource(R.mipmap.icon_title_close);
        tvToolbarTitle.setVisibility(View.GONE);
        ImmersionBar.with(this).statusBarColor(R.color.a2D2D2D).init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {//界面加载完毕
            CommonUtil.showSoftInputFromWindow(mActivity, et_login_mobile);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        et_login_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (StringUtil.isNotEmpty(s.toString())) {
                    if (!CommonUtil.isChinaPhoneLegal(s.toString())) {
                        RingToast.show("请输入正确的手机号码");
                        et_login_mobile.setText("");
                        et_login_mobile.requestFocus();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
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

    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected LoginActivityPresenter createPresenter() {
        return new LoginActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_login_yzm, R.id.tv_login_sub, R.id.tv_login_yhxy, R.id.tv_login_yszc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_yhxy:
                Bundle bundle = new Bundle();
                bundle.putString(WebViewActivity.URL_KEY, "file:///android_asset/network.html");
                startActivity(WebViewActivity.class, bundle);
                break;
            case R.id.tv_login_yszc:
                Bundle bundle1 = new Bundle();
                bundle1.putString(WebViewActivity.URL_KEY, "file:///android_asset/privacy.html");
                startActivity(WebViewActivity.class, bundle1);
                break;
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_login_yzm:
                if (StringUtil.isEmpty(StringUtil.checkEditText(et_login_mobile))) {
                    RingToast.show("请输入电话号码");
                    return;
                }
                showLoadDialog();
                mPresenter.sendVerifyCode(StringUtil.checkEditText(et_login_mobile));
                break;
            case R.id.tv_login_sub:
                if (StringUtil.isEmpty(StringUtil.checkEditText(et_login_mobile))) {
                    RingToast.show("请输入电话号码");
                    return;
                }
                if (StringUtil.isEmpty(StringUtil.checkEditText(et_login_yzm))) {
                    RingToast.show("请输入验证码");
                    return;
                }
                showLoadDialog();
                mPresenter.login(StringUtil.checkEditText(et_login_mobile), StringUtil.checkEditText(et_login_yzm));
                break;
        }
    }

    @Override
    public void sendVerifyCodeSuccess(Boolean response) {
        hideLoadDialog();
        RingLog.e("sendVerifyCodeSuccess() response = " + response);
        if (response) {
            RingToast.show("验证码获取成功");
            CountdownUtil.getInstance().newTimer(60000, 1000, new CountdownUtil.ICountDown() {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvLoginYzm.setEnabled(false);
                    tvLoginYzm.setText((millisUntilFinished / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    tvLoginYzm.setEnabled(true);
                    tvLoginYzm.setText("重新获取");
                }
            }, "LOGIN_TIMER");
            et_login_yzm.requestFocus();
        }
    }

    @Override
    public void sendVerifyCodeFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("sendVerifyCodeFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
        RingToast.show("验证码获取失败，请重试");
        if (errorCode == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            RingToast.show(errorMsg);
        }
    }

    @Override
    public void loginSuccess(LoginBean response) {
        hideLoadDialog();
        RingLog.e("loginSuccess() response = " + response);
        if (response != null) {
            RingToast.show("登录成功");
            spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, true);
            spUtil.saveString(Global.SP_KEY_CELLPHONE, StringUtil.checkEditText(et_login_mobile));
            spUtil.saveString(Global.SP_KEY_ACCOUNTIUD, response.getAccountId());
            spUtil.saveString(Global.SP_KEY_TOKEN, response.getToken());
            EventBus.getDefault().post(new LoginEvent());
            finish();
        }
    }

    @Override
    public void loginFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("sendVerifyCodeFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
        RingToast.show("登录失败，请重试");
        if (errorCode == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            RingToast.show(errorMsg);
        }
        et_login_yzm.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CountdownUtil.getInstance().cancel("LOGIN_TIMER");
    }
}
