package com.boniu.shipinbofangqi.app;

import android.content.Context;

import com.boniu.shipinbofangqi.log.RingLog;
import com.boniu.shipinbofangqi.util.ChannelUtil;
import com.boniu.shipinbofangqi.util.GetDeviceId;
import com.boniu.shipinbofangqi.util.QMUIPackageHelper;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/4/11 11:30
 */
public class UrlConstants {
    /**
     * 1.获取登录账户的相关详细信息
     */
    public static final String GETACCOUNTINFO = "standard/account/getAccountInfo";
    /**
     * 2.获取验证码
     */
    public static final String SENDVERIFYCODE = "/standard/account/sendVerifyCode";
    /**
     * 3.登录
     */
    public static final String LOGIN = "/standard/account/login";
    /**
     * 4.设备或用户登录之后产生token值,当accountId失效之后，需调用此接口获取最新的accountId的值
     */
    public static final String GETNEWACCOUNTID = "/standard/account/getNewAccountId";
    /**
     * 5.获取APP基础信息
     */
    public static final String CHECKVERSION = "/standard/common/base";
    /**
     * 6.用户信息反馈接口
     */
    public static final String ADDFEEDBACK = "/standard/common/addFeedback";
    /**
     * 7.注销账号
     */
    public static final String CANCELACCOUNT = "/standard/account/cancelAccount";
    /**
     * 8.退出当前已登录账户
     */
    public static final String LOGOUT = "/standard/account/logout";
    /**
     * 23.最新帖子列表
     */
    public static final String NEWEST_POINT = "article/info/new";
    /**
     * 24.热门帖子列表
     */
    public static final String HOT_POINT = "article/info/hot";
    /**
     * 25.问题车帖子列表
     */
    public static final String PROBLEM_CAR_POINT = "article/info/problem";
    /**
     * 67.检查升级
     */
    public static final String CHECK_VERSION = "util/version/check";
    /**
     * 72.获取瀑布流title
     */
    public static final String GET_ENCYCLOPEDIAS_TITLE = "encyclopedia/queryEncyclopediaClassification";
    /**
     * 72.获取瀑布流内容
     */
    public static final String GET_ENCYCLOPEDIAS = "encyclopedia/queryEncyclopediaList";
    public static final String ADVERTISEMENT = "";

    private static int getEnvironmental() {
        return AppConfig.environmental;//1.test环境---2.demo环境---3.线上环境
    }

    /**
     * 获取带端口的IP地址
     *
     * @return
     */
    public static String getServiceBaseUrl() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "https://test99.rhinox.cn/";
                break;
            case 2://demo环境
                url = "https://demo.dzztrip.cn/api/";
                break;
            case 3://线上环境
                url = "https://service.dzztrip.cn/api/";
                break;
            default:
                break;
        }
        return url;
    }

    /**
     * 获取带端口的IP地址
     *
     * @return
     */
    public static String getServiceBaseUrl1() {
        String url = "";
        switch (getEnvironmental()) {
            case 1://test环境
                url = "https://test99.rhinox.cn/";
                break;
            case 2://demo环境
                url = "https://demo.cwjia.cn/pet-api/";
                break;
            case 3://线上环境
                url = "https://api.ichongwujia.com/";
                break;
            default:
                break;
        }
        return url;
    }

    public static HttpHeaders getHeaders(Context mContext) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.HEAD_KEY_CONTENT_TYPE, "application/json");
        headers.put("uuid", GetDeviceId.readDeviceID(mContext));
        headers.put("appName", "SHIPINBOFANGQI_BONIU");
        headers.put("brand", android.os.Build.BRAND);
        headers.put("channel", ChannelUtil.getChannel(mContext));
        headers.put("deviceModel", android.os.Build.MODEL);
        headers.put("deviceType", "Android");
        headers.put("version", QMUIPackageHelper.getAppVersion(mContext));
        headers.put("phoneSystemVersion", "Android "
                + android.os.Build.VERSION.RELEASE);
        headers.put("petTimeStamp", String.valueOf(System.currentTimeMillis()));
        RingLog.e("headers = " + headers.toString());
        RingLog.e("toJSONString = " + headers.toJSONString());
        return headers;
    }

    public static HttpHeaders getHeadersNouuid(Context mContext) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.HEAD_KEY_CONTENT_TYPE, "application/json");
        headers.put("appName", "SHIPINBOFANGQI_BONIU");
        headers.put("brand", android.os.Build.BRAND);
        headers.put("channel", ChannelUtil.getChannel(mContext));
        headers.put("deviceModel", android.os.Build.MODEL);
        headers.put("deviceType", "Android");
        headers.put("version", QMUIPackageHelper.getAppVersion(mContext));
        headers.put("phoneSystemVersion", "Android "
                + android.os.Build.VERSION.RELEASE);
        headers.put("petTimeStamp", String.valueOf(System.currentTimeMillis()));
        RingLog.e("headers = " + headers.toString());
        RingLog.e("toJSONString = " + headers.toJSONString());
        return headers;
    }

    public static HttpParams getParams(Context mContext) {
        HttpParams params = new HttpParams();
        params.put("uuid", GetDeviceId.readDeviceID(mContext));
        params.put("appName", "SHIPINBOFANGQI_BONIU");
        params.put("brand", android.os.Build.BRAND);
        params.put("channel", ChannelUtil.getChannel(mContext));
        params.put("deviceModel", android.os.Build.MODEL);
        params.put("deviceType", "Android");
        params.put("version", QMUIPackageHelper.getAppVersion(mContext));
        RingLog.e("params = " + params.toString());
        return params;
    }
}
