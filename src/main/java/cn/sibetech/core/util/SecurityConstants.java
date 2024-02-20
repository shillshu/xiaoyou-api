package cn.sibetech.core.util;

/**
 * @author liwj
 * @date 2022/2/23 15:49
 */
public class SecurityConstants {
    /**
     * 用户标识
     */
    public static final String USER_KEY = "user_key";
    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";


    public static final String IMGVERIFY_PREFIX = "ImgVerify-";
    public static final String IMGVERIFY_RESULT_PREFIX = "ImgVerifyResult-";
    public static final String IMGVERIFY_RESULT_SUCCESS = "success";
    public static final String USER_CEPING_PREFIX = "cp";

    public final static String TOKEN_PREFIX = "xg:token:";
    /**
     * token有效期，默认30（分钟）
     */
    public final static long EXPIRE_TIME = 30;

    public final static long MILLIS_SECOND = 1000;

    public final static long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    /**
     * token刷新时间，默认20(分钟)
     */
    public final static long REFRESH_TIME = 20 * MILLIS_MINUTE;

    public static final String AUTHORIZATION = "Authorization";
    public static final String HEADER = "header";
    public static final String TOKEN = "token";
}
