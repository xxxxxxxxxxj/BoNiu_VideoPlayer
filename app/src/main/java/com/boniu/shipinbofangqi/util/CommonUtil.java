package com.boniu.shipinbofangqi.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.app.AppConfig;
import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.OrderCreateBean;
import com.boniu.shipinbofangqi.mvp.view.activity.LoginActivity;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.RequestBody;

/**
 * author：   zp
 * date：     2015/8/19 0019 17:45
 * <p/>       公共类,主要用于一些常用的方法
 * modify by  ljy
 */
public class CommonUtil {
    /**
     * 拨打电话
     *
     * @param context
     * @param phone
     */
    public static void cellPhone(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }


    //查看大图
    public static void photoView(Activity context, ImageView imageView, RecyclerView recyclerView, int position, List<Object> imgList) {
        new XPopup.Builder(context).asImageViewer(imageView, position, imgList, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                popupView.updateSrcView((ImageView) recyclerView.getChildAt(position));
            }
        }, new XPopupImageLoader() {
            @Override
            public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
                //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
                Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round).override(Target.SIZE_ORIGINAL)).placeholder(R.mipmap.ic_image_load).error(R.mipmap.ic_image_load).into(imageView);
            }

            @Override
            public File getImageFile(@NonNull Context context, @NonNull Object uri) {
                try {
                    return Glide.with(context).downloadOnly().load(uri).submit().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).show();
    }

    public static List<File> pathToFile(List<String> pathList) {
        List<File> fileList = new ArrayList<File>();
        for (int i = 0; i < pathList.size(); i++) {
            fileList.add(new File(pathList.get(i)));
        }
        return fileList;
    }

    public static List<String> fileToPath(List<File> fileList) {
        List<String> pathList = new ArrayList<String>();
        for (int i = 0; i < fileList.size(); i++) {
            pathList.add(fileList.get(i).getAbsolutePath());
        }
        return pathList;
    }

    /**
     * 判断当前日期是否在给定的两个日期之间
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     */
    public static boolean compareDateState(String beginDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c.setTimeInMillis(System.currentTimeMillis());
            c1.setTime(df.parse(beginDate));
            c2.setTime(df.parse(endDate));
        } catch (java.text.ParseException e) {
            return false;
        }
        int resultBegin = c.compareTo(c1);
        int resultEnd = c.compareTo(c2);
        return resultBegin > 0 && resultEnd < 0;
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static Uri getUri(Context mContext, File file) {
        Uri mUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二：Android 7.0及以上获取文件 Uri
            mUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", file);
        } else {
            //步骤三：获取文件Uri
            mUri = Uri.fromFile(file);
        }
        return mUri;
    }

    /**
     * get Local video duration
     *
     * @return
     */
    public static int getLocalVideoDuration(String videoPath) {
        int duration;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            duration = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
            RingLog.e("获取视频时长", "获取失败!");
            return 0;
        }
        return duration;
    }

    /**
     * get Local video duration
     *
     * @return
     */
    public static Bitmap getThumbnail(Context context, long origId) {
        return MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(),
                origId, MediaStore.Video.Thumbnails.MINI_KIND, null);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        String time = date.toLocaleString();
        RingLog.e("md", "时间time为： " + time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sim = dateFormat.format(date);
        RingLog.e("md", "时间sim为： " + sim);
        return sim;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDate() {
        Date date = new Date();
        String time = date.toLocaleString();
        RingLog.e("md", "时间time为： " + time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sim = dateFormat.format(date);
        RingLog.e("md", "时间sim为： " + sim);
        return sim;
    }

    public static long getTimeDays(String startDateStr, String endDateStr) {
        long betweenDate = 0;
        try {
            //设置转换的日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(startDateStr);
            //结束时间
            Date endDate = sdf.parse(endDateStr);
            //得到相差的天数 betweenDate
            betweenDate = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return betweenDate;
    }

    // 跳转到应用市场评价
    public static void goMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id="
                    + context.getPackageName());
            Intent intentwx = new Intent(Intent.ACTION_VIEW, uri);
            intentwx.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentwx);
        } catch (Exception e) {
            RingToast.show("您没有安装应用市场");
        }
    }

    public static boolean isLogin(Context mContext) {
        return SharedPreferenceUtil.getInstance(mContext).getBoolean(Global.SP_KEY_ISLOGIN, false);
    }

    public static String getCellPhone(Context mContext) {
        return SharedPreferenceUtil.getInstance(mContext).getString(Global.SP_KEY_CELLPHONE, "");
    }

    public static String getToken(Context mContext) {
        return SharedPreferenceUtil.getInstance(mContext).getString(Global.SP_KEY_TOKEN, "");
    }

    public static String getAccountId(Context mContext) {
        return SharedPreferenceUtil.getInstance(mContext).getString(Global.SP_KEY_ACCOUNTIUD, "");
    }

    public static void getNewAccountId(Context mContext) {
        HttpParams params = new UrlConstants().getParams(mContext);
        if (StringUtil.isNotEmpty(CommonUtil.getAccountId(mContext))) {
            params.put("accountId", CommonUtil.getAccountId(mContext));
        }
        if (StringUtil.isNotEmpty(CommonUtil.getToken(mContext))) {
            params.put("token", CommonUtil.getToken(mContext));
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
        EasyHttp.post(UrlConstants.GETNEWACCOUNTID)
                .requestBody(requestBody)
                .sign(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        RingLog.e("onError() e = " + e.toString());
                    }

                    @Override
                    public void onSuccess(String response) {
                        if (StringUtil.isNotEmpty(response)) {
                            Gson gson = new Gson();
                            OrderCreateBean orderCreateBean = gson.fromJson(response, OrderCreateBean.class);
                            if (orderCreateBean != null) {
                                int errorCode = 0;
                                if (orderCreateBean.getErrorCode().contains("-")) {
                                    errorCode = Integer.parseInt(orderCreateBean.getErrorCode().split("-")[2]);
                                }
                                if (errorCode == 0) {
                                    SharedPreferenceUtil.getInstance(mContext).saveString(Global.SP_KEY_ACCOUNTIUD, orderCreateBean.getResult());
                                } else if (errorCode == AppConfig.EXIT_USER_CODE) {
                                    SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_ISLOGIN);
                                    SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_CELLPHONE);
                                    SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_ACCOUNTIUD);
                                    SharedPreferenceUtil.getInstance(mContext).removeData(Global.SP_KEY_TOKEN);
                                    RingToast.show("您已在其他设备登录");
                                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                                } else if (errorCode == AppConfig.CLEARACCOUNTID_CODE) {
                                    CommonUtil.getNewAccountId(mContext);
                                } else {
                                    int netWorkState = CommonUtil.getNetWorkState(mContext);
                                    if (netWorkState == CommonUtil.NETWORK_NONE) {
                                        RingToast.show("无网络连接");
                                    } else {
                                        RingToast.show(orderCreateBean.getErrorMsg());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void copy(Context mContext, String content) {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
        RingToast.show("复制成功");
    }

    public static boolean isQQInstall(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                //通过遍历应用所有包名进行判断
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void goToQQ(Context mContext, String qq) {
        if (isQQInstall(mContext)) {
            final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        } else {
            RingToast.show("请安装QQ");
        }
    }

    public static void goBrowser(Context mContext, String linkUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(linkUrl);
        intent.setData(uri);
        mContext.startActivity(intent);
    }

    //手机号判断 true为通过验证
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        if (str == null) {
            return false;
        }
        if (str.length() != 11) {
            return false;
        }
        String regExp = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
