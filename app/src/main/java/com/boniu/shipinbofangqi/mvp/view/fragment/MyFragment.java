package com.boniu.shipinbofangqi.mvp.view.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AccountInfoBean;
import com.boniu.shipinbofangqi.mvp.model.event.GestureSuccessEvent;
import com.boniu.shipinbofangqi.mvp.model.event.LoginEvent;
import com.boniu.shipinbofangqi.mvp.model.event.PayEvent;
import com.boniu.shipinbofangqi.mvp.presenter.MyFragPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.AboutActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.FeedBackActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.LoginActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.MemberActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.SetGesturesActivity;
import com.boniu.shipinbofangqi.mvp.view.activity.StartGesturesActivity;
import com.boniu.shipinbofangqi.mvp.view.fragment.base.BaseFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IMyFragView;
import com.boniu.shipinbofangqi.permission.PermissionListener;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.GetGestures;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.QMUIDeviceHelper;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.ll_fragmy_folder)
    LinearLayout ll_fragmy_folder;
    @BindView(R.id.srl_fragmy)
    SmartRefreshLayout srlFragMy;
    @BindView(R.id.tv_fragmy_login)
    TextView tvFragmyLogin;
    @BindView(R.id.tv_fragmy_senior_state)
    TextView tvFragmySeniorState;
    @BindView(R.id.sh_fragmy_folder)
    ImageView shFragmyFolder;
    @BindView(R.id.tv_fragmy_loginout)
    TextView tv_fragmy_loginout;
    private String validityTime;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(GestureSuccessEvent event) {
        if (event != null && event.getType() == 3) {
            shFragmyFolder.setImageResource(R.mipmap.icon_switch_close);
            spUtil.saveBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(LoginEvent event) {
        if (event != null) {
            loadData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(PayEvent event) {
        if (event != null) {
            loadData();
        }
    }

    private void setData() {
        if (CommonUtil.isLogin(mActivity)) {
            tv_fragmy_loginout.setVisibility(View.VISIBLE);
            boolean ISOPENENVIP = spUtil.getBoolean(Global.SP_KEY_ISOPENENVIP, false);
            if (ISOPENENVIP) {
                tvFragmySeniorState.setText(validityTime + "到期");
            } else {
                tvFragmySeniorState.setText("未开通");
            }
            tvFragmyLogin.setText(CommonUtil.getCellPhone(mActivity));
        } else {
            tv_fragmy_loginout.setVisibility(View.GONE);
            tvFragmySeniorState.setText("未开通");
            tvFragmyLogin.setText("未登录");
        }
    }

    @Override
    protected MyFragPresenter createPresenter() {
        return new MyFragPresenter(mActivity, this);
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
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
        setData();
        srlFragMy.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        tvToolbarTitle.setText("我的");
        ivToolbarBack.setVisibility(View.GONE);
        requestEachCombined(new PermissionListener() {
            @Override
            public void onGranted(String permissionName) {
                String pwd = GetGestures.readGestures(mActivity);
                if (StringUtil.isNotEmpty(pwd)) {
                    //判断是否开启加密文件夹
                    boolean ISOPENENCRYPTEDFOLDER = spUtil.getBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
                    if (ISOPENENCRYPTEDFOLDER) {
                        shFragmyFolder.setImageResource(R.mipmap.icon_switch_open);
                    } else {
                        shFragmyFolder.setImageResource(R.mipmap.icon_switch_close);
                        spUtil.saveBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
                    }
                } else {
                    shFragmyFolder.setImageResource(R.mipmap.icon_switch_close);
                    spUtil.saveBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
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
                        return false;
                    }
                });
            }
        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {
        showLoadDialog();
        mPresenter.getAccountInfo();
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

    @OnClick({R.id.rl_fragmy_login, R.id.ll_fragmy_senior, R.id.ll_fragmy_feedback, R.id.ll_fragmy_about,
            R.id.sh_fragmy_folder, R.id.tv_fragmy_loginout, R.id.ll_fragmy_gestures})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_fragmy_gestures:
                if (CommonUtil.isLogin(mActivity)) {
                    requestEachCombined(new PermissionListener() {
                        @Override
                        public void onGranted(String permissionName) {
                            String pwd = GetGestures.readGestures(mActivity);
                            if (StringUtil.isNotEmpty(pwd)) {
                                startActivity(StartGesturesActivity.class);
                            } else {
                                startActivity(SetGesturesActivity.class);
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
                                    return false;
                                }
                            });
                        }
                    }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_fragmy_loginout:
                MessageDialog.show(mActivity, "退出登录", "确定退出登录吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        showLoadDialog();
                        mPresenter.logout();
                        return false;
                    }
                });
                break;
            case R.id.rl_fragmy_login:
                if (!CommonUtil.isLogin(mActivity)) {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_fragmy_senior:
                if (CommonUtil.isLogin(mActivity)) {
                    startActivity(MemberActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_fragmy_feedback:
                if (CommonUtil.isLogin(mActivity)) {
                    startActivity(FeedBackActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_fragmy_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.sh_fragmy_folder:
                if (CommonUtil.isLogin(mActivity)) {
                    boolean ISOPENENCRYPTEDFOLDER = spUtil.getBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, false);
                    if (ISOPENENCRYPTEDFOLDER) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 3);
                        startActivity(StartGesturesActivity.class, bundle);
                    } else {
                        //判断是否开通高级版
                        boolean ISOPENENVIP = spUtil.getBoolean(Global.SP_KEY_ISOPENENVIP, false);
                        if (ISOPENENVIP) {
                            requestEachCombined(new PermissionListener() {
                                @Override
                                public void onGranted(String permissionName) {
                                    String pwd = GetGestures.readGestures(mActivity);
                                    if (StringUtil.isNotEmpty(pwd)) {
                                        shFragmyFolder.setImageResource(R.mipmap.icon_switch_open);
                                        spUtil.saveBoolean(Global.SP_KEY_ISOPENENCRYPTEDFOLDER, true);
                                    } else {
                                        RingToast.show("请先设置手势密码");
                                        startActivity(SetGesturesActivity.class);
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
                                            return false;
                                        }
                                    });
                                }
                            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
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
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    @Override
    public void getAccountInfoSuccess(AccountInfoBean response) {
        hideLoadDialog();
        RingLog.e("getAccountInfoSuccess() response = " + response);
        if (response != null) {
            spUtil.saveBoolean(Global.SP_KEY_ISLOGIN, true);
            spUtil.saveString(Global.SP_KEY_CELLPHONE, response.getMobile());
            if (StringUtil.isNotEmpty(response.getApplyCancelTime())) {
                spUtil.saveString(Global.SP_KEY_CANCELTIME, response.getApplyCancelTime());
            }
            if (StringUtil.isNotEmpty(response.getType()) && response.getType().equals("VIP")) {
                validityTime = response.getVipExpireTime();
                spUtil.saveString(Global.SP_KEY_VALIDITYTIME, validityTime);
                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, true);
            } else if (StringUtil.isNotEmpty(response.getType()) && response.getType().equals("NORMAL")) {
                spUtil.saveBoolean(Global.SP_KEY_ISOPENENVIP, false);
            }
        }
        setData();
    }

    @Override
    public void getAccountInfoFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("getAccountInfoFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
        if (errorCode == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
            RingToast.show("您已在其他设备登录");
            startActivity(LoginActivity.class);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            int netWorkState = CommonUtil.getNetWorkState(mActivity);
            if (netWorkState == CommonUtil.NETWORK_NONE) {
                RingToast.show("无网络连接");
            } else {
                //RingToast.show(errorMsg);
            }
        }
        setData();
    }

    @Override
    public void logoutFail(int errorCode, String errorMsg) {
        hideLoadDialog();
        RingLog.e("logoutFail() errorCode = " + errorCode + "---errorMsg = " + errorMsg);
        if (errorCode == AppConfig.EXIT_USER_CODE) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
        } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
            CommonUtil.getNewAccountId(mActivity);
        } else {
            int netWorkState = CommonUtil.getNetWorkState(mActivity);
            if (netWorkState == CommonUtil.NETWORK_NONE) {
                RingToast.show("无网络连接");
            } else {
                RingToast.show(errorMsg);
            }
        }
    }

    @Override
    public void logoutSuccess(Boolean response) {
        hideLoadDialog();
        RingLog.e("logoutSuccess() response = " + response);
        if (response) {
            spUtil.removeData(Global.SP_KEY_ISLOGIN);
            spUtil.removeData(Global.SP_KEY_CELLPHONE);
            spUtil.removeData(Global.SP_KEY_ACCOUNTIUD);
            spUtil.removeData(Global.SP_KEY_TOKEN);
            setData();
        }
    }
}
