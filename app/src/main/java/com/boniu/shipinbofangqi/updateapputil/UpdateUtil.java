package com.boniu.shipinbofangqi.updateapputil;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.boniu.shipinbofangqi.util.Global;
import com.boniu.shipinbofangqi.util.SharedPreferenceUtil;
import com.kongzue.dialog.v3.CustomDialog;

import java.io.File;


public class UpdateUtil {
    public static final int UPDATEFORDIALOG = 1;
    public static final int UPDATEFORNOTIFICATION = 2;

    public static void showForceUpgradeDialog(final Context context, String msg, final String path,
                                              final String version) {
        InstallDialog mDialog = new InstallDialog.Builder(context)
                .setTitle(version)
                .setType(InstallDialog.DIALOGTYPE_ALERT).setMessage(msg)
                .setCancelable(false).setOKStr("立即升级")
                .positiveListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        updateApk(context, path, version, UPDATEFORNOTIFICATION, 1);
                    }
                }).build();
        mDialog.show();
    }

    public static void showUpgradeDialog(final Context context, String msg, final String path, final String version) {
        InstallDialog mDialog = new InstallDialog.Builder(context)
                .setTitle(version)
                .setType(InstallDialog.DIALOGTYPE_CONFIRM).setMessage(msg)
                .setCancelStr("残忍拒绝").setOKStr("立即升级")
                .positiveListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        updateApk(context, path, version, UPDATEFORNOTIFICATION, 0);
                    }
                }).build();
        mDialog.show();
    }

    public static void showUpgradeDialog(final AppCompatActivity mActivity, String title, String msg, final boolean forceUp, String linkUrl) {
        CustomDialog.build(mActivity, R.layout.layout_upgrade_dialog, new CustomDialog.OnBindView() {
            @Override
            public void onBind(final CustomDialog dialog, View v) {
                ImageView iv_upgradedialog_close = v.findViewById(R.id.iv_upgradedialog_close);
                TextView tv_upgradedialog_title = v.findViewById(R.id.tv_upgradedialog_title);
                TextView tv_upgradedialog_content = v.findViewById(R.id.tv_upgradedialog_content);
                TextView tv_upgradedialog_sub = v.findViewById(R.id.tv_upgradedialog_sub);
                if (forceUp) {//强制升级
                    iv_upgradedialog_close.setVisibility(View.INVISIBLE);
                } else {//非强制升级
                    iv_upgradedialog_close.setVisibility(View.VISIBLE);
                    iv_upgradedialog_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferenceUtil.getInstance(mActivity).saveBoolean(Global.SP_KEY_ISCLOSEUPGRADEDIALOG, true);
                            dialog.doDismiss();
                        }
                    });
                }
                tv_upgradedialog_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtil.goBrowser(mActivity, linkUrl);
                        dialog.doDismiss();
                    }
                });
                tv_upgradedialog_title.setText(title);
                tv_upgradedialog_content.setText(msg);
            }
        }).setAlign(CustomDialog.ALIGN.DEFAULT).setCancelable(false).show();
    }

    /**
     * 下载apk,两种模式
     *
     * @param context
     * @param updateType
     */
    public static void updateApk(final Context context, final String apkPath, final String serverVersionName, int updateType, final int isUpgrade) {
        switch (updateType) {
            case UPDATEFORDIALOG:
                //BgUpdate.updateForDialog(context, url, filePath);
                break;
            case UPDATEFORNOTIFICATION:
                //BgUpdate.updateForNotification(context, url, filePath);
                if (isWifiConnected(context)) {
                    DownloadAppUtils.download(context, apkPath, serverVersionName, isUpgrade);
                } else {
                    new ConfirmDialog(context, new Callback() {
                        @Override
                        public void callback(int position) {
                            DownloadAppUtils.download(context, apkPath, serverVersionName, isUpgrade);
                        }
                    }).setContent("目前手机不是WiFi状态\n确认是否继续下载更新？").show();
                }
                break;
        }
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 比较版本号
     *
     * @param serviceVersion
     * @param localVersion
     * @return
     */
    public static boolean compareVersion(String serviceVersion,
                                         String localVersion) {
        Log.d("TAG", "serviceVersion:" + serviceVersion + " localVersion:"
                + localVersion);
        boolean result = false;
        serviceVersion = serviceVersion.replace(".", "");
        localVersion = localVersion.replace(".", "");
        int flagLength = 0;
        int versionLength = serviceVersion.length() > localVersion.length() ? localVersion
                .length() : serviceVersion.length();
        for (int i = 0; i < versionLength; i++) {
            if (Integer.parseInt(serviceVersion.charAt(i) + "") > Integer
                    .parseInt(localVersion.charAt(i) + "")) {
                result = true;
                break;
            } else if (Integer.parseInt(serviceVersion.charAt(i) + "") < Integer
                    .parseInt(localVersion.charAt(i) + "")) {
                break;
            } else {
                flagLength = i;
            }
        }
        if (!result
                && flagLength + 1 == versionLength
                && serviceVersion.length() > localVersion.length()
                && 0 < Integer.parseInt(serviceVersion.charAt(versionLength)
                + "")) {
            result = true;
        }
        return result;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkFile
     */
    public static void installAPK(Context context, File apkFile) {
        Log.e("TAG", "apkFile = " + apkFile.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = "application/vnd.android.package-archive";
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }
}
