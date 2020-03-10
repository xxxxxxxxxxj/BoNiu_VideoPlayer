package com.boniu.shipinbofangqi.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.boniu.shipinbofangqi.app.UrlConstants;
import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.mvp.model.entity.PayResult;
import com.boniu.shipinbofangqi.mvp.model.event.PayResultEvent;
import com.boniu.shipinbofangqi.util.CommonUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import org.greenrobot.eventbus.EventBus;

import okhttp3.RequestBody;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/8/8 17:13
 */
public class PayResultService extends Service {

    public static final String ACTION = "com.boniu.shipinbofangqi.services.PayResultService";
    private String orderId;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try {
            orderId = intent.getStringExtra("orderId");
            new PollingThread().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class PollingThread extends Thread {
        @Override
        public void run() {
            HttpParams params = UrlConstants.getParams(getApplicationContext());
            params.put("orderId", orderId);
            params.put("accountId", CommonUtil.getAccountId(getApplicationContext()));
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), params.toJSONString());
            EasyHttp.post(UrlConstants.QUERYPAYORDER)
                    .requestBody(requestBody)
                    .sign(true)
                    .execute(new SimpleCallBack<PayResult>() {
                        @Override
                        public void onError(ApiException e) {
                            RingLog.e("onError() e = " + e.toString());
                            PayResultEvent payResultEvent = new PayResultEvent();
                            payResultEvent.setCode(e.getCode());
                            payResultEvent.setMsg(e.getMessage());
                            EventBus.getDefault().post(payResultEvent);
                        }

                        @Override
                        public void onSuccess(PayResult response) {
                            RingLog.e("onSuccess() response = " + response.toString());
                            PayResultEvent payResultEvent = new PayResultEvent();
                            payResultEvent.setPayResult(response);
                            payResultEvent.setCode(0);
                            payResultEvent.setMsg("操作成功");
                            EventBus.getDefault().post(payResultEvent);
                        }
                    });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}