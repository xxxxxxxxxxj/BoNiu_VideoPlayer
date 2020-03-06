package com.boniu.shipinbofangqi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.boniu.shipinbofangqi.R;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.toast.RingToast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
}
