package com.boniu.shipinbofangqi.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.LoginBean;
import com.boniu.shipinbofangqi.mvp.model.event.LoginEvent;
import com.boniu.shipinbofangqi.mvp.presenter.LoginActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.iview.ILoginActivityView;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity<LoginActivityPresenter> implements ILoginActivityView {
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tiet_login_mobile)
    TextInputEditText tietLoginMobile;
    @BindView(R.id.til_login_mobile)
    TextInputLayout tilLoginMobile;
    @BindView(R.id.tiet_login_yzm)
    TextInputEditText tietLoginYzm;
    @BindView(R.id.til_login_yzm)
    TextInputLayout tilLoginYzm;
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
        CommonUtil.showSoftInputFromWindow(mActivity, tietLoginMobile);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

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
    protected LoginActivityPresenter createPresenter() {
        return new LoginActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_toolbar_back, R.id.tv_login_yzm, R.id.tv_login_sub})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.tv_login_yzm:
                if (StringUtil.isEmpty(StringUtil.checkEditText(tilLoginMobile.getEditText()))) {
                    RingToast.show("请输入电话号码");
                    return;
                }
                showLoadDialog();
                mPresenter.sendVerifyCode(StringUtil.checkEditText(tilLoginMobile.getEditText()));
                break;
            case R.id.tv_login_sub:
                if (StringUtil.isEmpty(StringUtil.checkEditText(tilLoginMobile.getEditText()))) {
                    RingToast.show("请输入电话号码");
                    return;
                }
                if (StringUtil.isEmpty(StringUtil.checkEditText(tilLoginYzm.getEditText()))) {
                    RingToast.show("请输入验证码");
                    return;
                }
                showLoadDialog();
                mPresenter.login(StringUtil.checkEditText(tilLoginMobile.getEditText()), StringUtil.checkEditText(tilLoginYzm.getEditText()));
                break;
        }
    }

    @Override
    public void sendVerifyCodeSuccess(Boolean response) {
        hideLoadDialog();
        RingLog.e("sendVerifyCodeSuccess() response = " + response);
        RingToast.show("验证码获取成功");
        tietLoginYzm.requestFocus();
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
        }
    }

    @Override
    public void loginSuccess(LoginBean response) {
        hideLoadDialog();
        RingLog.e("loginSuccess() response = " + response);
        RingToast.show("登录成功");
        EventBus.getDefault().post(new LoginEvent());
        spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, true);
        spUtil.saveString(Global.SP_KEY_CELLPHONE, StringUtil.checkEditText(tilLoginMobile.getEditText()));
        spUtil.saveString(Global.SP_KEY_ACCOUNTIUD, response.getAccountId());
        spUtil.saveString(Global.SP_KEY_TOKEN, response.getToken());
        finish();
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
        }
    }
}
