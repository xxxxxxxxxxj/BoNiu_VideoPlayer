package com.boniu.shipinbofangqi.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.boniu.shipinbofangqi.log.RingLog;

/**
 * Polling Tools
 *
 * @Author Ryan
 * @Create 2013-7-13 上午10:14:43
 */
public class PollingUtils {

    /**
     * @param context
     * @param seconds
     * @param cls
     * @param action
     * @param orderId
     */
    public static void startPollingService(Context context, int seconds, Class<?> cls, String action, String orderId) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.putExtra("orderId", orderId);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime();
        RingLog.e("triggerAtTime = "+triggerAtTime);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("TAG","1");
            manager.setWindow(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), seconds * 1000, pendingIntent);
        } else {
            Log.e("TAG","2");
            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), seconds * 1000, pendingIntent);
        }
    }

    /**
     * @param context
     * @param cls
     * @param action
     */
    public static void stopPollingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}
