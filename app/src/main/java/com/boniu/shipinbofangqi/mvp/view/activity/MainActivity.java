package com.boniu.shipinbofangqi.mvp.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.AppInfoBean;
import com.boniu.shipinbofangqi.mvp.model.entity.TabEntity;
import com.boniu.shipinbofangqi.mvp.model.event.CaptureEvent;
import com.boniu.shipinbofangqi.mvp.model.event.MatisseDataEvent;
import com.boniu.shipinbofangqi.mvp.model.event.PayEvent;
import com.boniu.shipinbofangqi.mvp.presenter.MainActivityPresenter;
import com.boniu.shipinbofangqi.mvp.view.activity.base.BaseActivity;
import com.boniu.shipinbofangqi.mvp.view.fragment.MyFragment;
import com.boniu.shipinbofangqi.mvp.view.fragment.ResourcesFragment;
import com.boniu.shipinbofangqi.mvp.view.fragment.VideoFragment;
import com.boniu.shipinbofangqi.mvp.view.iview.IMainActivityView;
import com.boniu.shipinbofangqi.permission.PermissionListener;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.boniu.shipinbofangqi.updateapputil.Callback;
import com.boniu.shipinbofangqi.updateapputil.ConfirmDialog;
import com.boniu.shipinbofangqi.updateapputil.DownloadAppUtils;
import com.boniu.shipinbofangqi.updateapputil.DownloadProgressDialog;
import com.boniu.shipinbofangqi.updateapputil.UpdateAppEvent;
import com.boniu.shipinbofangqi.updateapputil.UpdateUtil;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.GetDeviceId;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.PathUtils;
import com.boniu.shipinbofangqi.util.QMUIDeviceHelper;
import com.boniu.shipinbofangqi.util.StringUtil;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhouyou.http.EasyHttp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 */
public class MainActivity extends BaseActivity<MainActivityPresenter> implements IMainActivityView {
    @BindView(R.id.ctl_main)
    CommonTabLayout ctlMain;
    private DownloadProgressDialog progressDialog;
    private boolean isShow;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private String[] mTitles = {"视频", "资源", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_normal, R.mipmap.tab_shop_normal,
            R.mipmap.tab_petcircle_normal};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_passed, R.mipmap.tab_shop_passed,
            R.mipmap.tab_petcircle_passed};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int currentIndex;
    private long exitTime;
    private List<String> pathList = new ArrayList<String>();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(PayEvent event) {
        if (event != null) {
            setFragMentIndex(2);
        }
    }/*

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(MatisseDataEvent event) {
        if (event != null) {
            setFragMentIndex(0);
        }
    }*/

    @Override
    protected void setView(Bundle savedInstanceState) {
        mFragments.add(new VideoFragment());
        mFragments.add(new ResourcesFragment());
        mFragments.add(new MyFragment());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        ctlMain.setTabData(mTabEntities, this, R.id.fl_main, mFragments);
        ctlMain.setCurrentTab(currentIndex);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
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
        showLoadDialog();
        mPresenter.checkVersion();
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBack(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(UpdateAppEvent event) {
        if (event != null) {
            if (event.getState() == UpdateAppEvent.DOWNLOADING) {
                long soFarBytes = event.getSoFarBytes();
                long totalBytes = event.getTotalBytes();
                if (event.getIsUpgrade() == 0 && isShow) {

                } else {
                    RingLog.e("下载中...soFarBytes = " + soFarBytes + "---totalBytes = " + totalBytes);
                    if (progressDialog != null && progressDialog.isShowing()) {

                    } else {
                        progressDialog = new DownloadProgressDialog(this);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setTitle("下载提示");
                        progressDialog.setMessage("当前下载进度:");
                        progressDialog.setIndeterminate(false);
                        if (event.getIsUpgrade() == 1) {
                            progressDialog.setCancelable(false);
                            progressDialog.setCanceledOnTouchOutside(false);
                        } else {
                            progressDialog.setCancelable(true);
                            progressDialog.setCanceledOnTouchOutside(true);
                        }
                        progressDialog.show();
                    }
                    progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            RingLog.e("onDismiss");
                            isShow = true;
                        }
                    });
                }
                if (progressDialog != null) {
                    progressDialog.setMax((int) totalBytes);
                    progressDialog.setProgress((int) soFarBytes);
                }
                isShow = true;
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_COMPLETE) {
                UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            UpdateUtil.installAPK(mContext, new File(DownloadAppUtils.downloadUpdateApkFilePath));
                        }
                    }, false).setContent("下载完成\n确认是否安装？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            } else if (event.getState() == UpdateAppEvent.DOWNLOAD_FAIL) {
                if (event.getIsUpgrade() == 1) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(false)
                            .setCancleBtnVisible(View.GONE).setDialogCanceledOnTouchOutside(false).show();
                } else {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.retry();
                        }
                    }, true).setContent("下载失败\n确认是否重试？").setDialogCancelable(true)
                            .setCancleBtnVisible(View.VISIBLE).setDialogCanceledOnTouchOutside(true).show();
                }
            }
        }
    }

    @Override
    public void checkVersionSuccess(AppInfoBean response) {
        RingLog.e("checkVersionSuccess() response = " + response);
        hideLoadDialog();
        if (response != null) {
            AppInfoBean.VersionInfoVo versionInfoVo = response.getVersionInfoVo();
            if (versionInfoVo != null) {
                boolean ISCLOSEUPGRADEDIALOG = spUtil.getBoolean(Global.SP_KEY_ISCLOSEUPGRADEDIALOG, false);
                String UPGRADETIME = spUtil.getString(Global.SP_KEY_UPGRADETIME, "");
                if (ISCLOSEUPGRADEDIALOG) {
                    if (StringUtil.isNotEmpty(UPGRADETIME)) {
                        if (CommonUtil.getTimeDays(UPGRADETIME, CommonUtil.getCurrentDate()) > 7) {
                            setUpgradeDialog(versionInfoVo);
                        }
                    } else {
                        setUpgradeDialog(versionInfoVo);
                    }
                } else {
                    setUpgradeDialog(versionInfoVo);
                }
            }
        }
    }

    private void setUpgradeDialog(AppInfoBean.VersionInfoVo versionInfoVo) {
        spUtil.saveString(Global.SP_KEY_UPGRADETIME, CommonUtil.getCurrentDate());
        // 强制升级
        UpdateUtil.showUpgradeDialog(mActivity, versionInfoVo.getTitle(), versionInfoVo.getContent(),
                versionInfoVo.isForceUp(), versionInfoVo.getLinkUrl());
    }

    @Override
    public void checkVersionFail(int status, String desc) {
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
            int netWorkState = CommonUtil.getNetWorkState(mContext);
            if (netWorkState == CommonUtil.NETWORK_NONE) {
                RingToast.show("无网络连接");
            } else {
                RingToast.show(desc);
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            onDestroy();
            this.finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RingLog.e("requestCode = " + requestCode);
        RingLog.e("resultCode = " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSEVIDEO://选择视频返回
                    ArrayList<String> selectVideoList = getIntent().getStringArrayListExtra("selectVideoList");
                    EventBus.getDefault().post(new MatisseDataEvent(null, selectVideoList));
                    break;
                case REQUEST_CODE_CHOOSE://Matisse选择照片返回
                    List<Uri> uris = Matisse.obtainResult(data);
                    List<String> strings = Matisse.obtainPathResult(data);
                    EventBus.getDefault().post(new MatisseDataEvent(uris, strings));
                    break;
                case REQUEST_CODE_PREVIEW://选择相册返回码
                    //启动裁剪
                    Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startUCrop(selectedUri, REQUEST_CODE_UCROP, 1, 1);
                    } else {
                        RingToast.show(R.string.toast_cannot_retrieve_selected_image);
                    }
                    break;
                case REQUEST_CODE_CAPTURE://拍照返回码
                    EventBus.getDefault().post(new CaptureEvent());
                    break;
                case REQUEST_CODE_UCROP://UCrop裁剪返回码
                    Uri resultUri = UCrop.getOutput(data);
                    RingLog.e("resultUri = " + resultUri);
                    if (resultUri != null) {
                        String path = PathUtils.getPath(mActivity, resultUri);
                        pathList.clear();
                        pathList.add(path);
                        RingLog.e("resultUri = " + resultUri.toString());
                        RingLog.e("path = " + path);
                        EventBus.getDefault().post(new MatisseDataEvent(null, pathList));
                    } else {
                        RingToast.show(R.string.toast_cannot_retrieve_cropped_image);
                    }
                    break;
            }
        } else if (resultCode == CameraActivity.RESULTCODE_VIDEO) {//拍摄视频返回
            int flag = data.getIntExtra("flag", 0);
            String path = data.getStringExtra("path");
            ArrayList<String> strings1 = new ArrayList<>();
            strings1.add(path);
            EventBus.getDefault().post(new MatisseDataEvent(null, strings1));
        }
    }

    public void setFragMentIndex(int index) {
        this.currentIndex = index;
        ctlMain.setCurrentTab(currentIndex);
    }
}
