package com.boniu.shipinbofangqi.fingerprintrecognition;

import android.util.Log;

import com.boniu.shipinbofangqi.app.AppConfig;

/**
 * Created by 77423 on 2016/11/7.
 */

public class FPLog {

    public static void log(String message) {
        if (AppConfig.isShowLog) {
            Log.i("FPLog", message);
        }
    }
}
