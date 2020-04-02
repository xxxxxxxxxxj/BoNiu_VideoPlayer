package com.boniu.shipinbofangqi.app;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/4/12 11:22
 */
public class AppConfig {
    public static final String WX_SECRET = "4541b1896902768bafda8e717486e17a";
    public static final String UMENG_APPKEY = "5e56419f895cca15a600002f";
    public static final int CLEARACCOUNTID_CODE = 9990;//表示accountId的值已过期，客户端应调用/standard/account/getNewAccountId，通过登录时获取的token获取新的accountId加密字符串，确保账户的有效性
    public static final int EXIT_USER_CODE = 9991;//用户登录状态被注销状态码
    public static final String URL = "http://www.sayiyinxiang.com";
    public static final String SHAREIMG_URL = "http://img.sayiyinxiang.com/api/charging/imgs/15382200559974283912.jpg";
    public static final String XIEYI_URL = "https://m.dzztrip.cn/h5/product/app_bld/agreement/index.html";
    public static final int HTTP_TIMEOUT = 500;
    public static final String SERVER_KEY = "A16EF76FA2D6B5A1A743A489D9332D9A";
    public static final int SERVER_ERROR = 5201314;
    public static final String SERVER_ERROR_MSG = "服务器错误";
    public static final String QQ_ID = "";
    public static final String WX_ID = "wxf1749cddcb6224d1";
    public static final boolean isShowLog = false;
    public static final int environmental = 3;//1.test环境---2.demo环境---3.线上环境
    public static final int ALI_SDK_PAY_FLAG = 1000;
    public static final int REQUEST_CODE_CHOOSE = 23;
    public static final String KEYWORD = "boniu_player";
    public static final String DIRECTORY_CAPTURE = "boniuvideo_capture";//拍照存储图片的文件夹
    public static final String DIRECTORY_CROP = "boniuvideo_crop";//裁剪存储图片的文件夹
    public static final String DIRECTORY_LUBAN = "boniuvideo_luban";//鲁班压缩存储图片的文件夹
    public static final String DIRECTORY_DEVICEID = ".boniuvideo_deviceid";//设备唯一ID存储的文件夹(隐藏文件夹)
    public static final String FILENAME_DEVICEID = ".deviceid";//设备唯一ID存储的文件名(隐藏文件夹)
    public static final String DIRECTORY_GESTURES = ".boniuvideo_gesturespwd";//手势密码存储的文件夹(隐藏文件夹)
    public static final String FILENAME_GESTURES = ".gesturespwd";//手势密码存储的文件名(隐藏文件夹)
    public static final String DIRECTORY_APK = "base_apk";//下载的apk存储的文件夹
    public static final String DIRECTORY_VIDEO = "base_video";//拍摄视频存储的文件夹
    public static final String DIRECTORY_VIDEO_FRAME = "base_video_frame";//拍摄视频生成封面以及拍照存储的文件夹
}
