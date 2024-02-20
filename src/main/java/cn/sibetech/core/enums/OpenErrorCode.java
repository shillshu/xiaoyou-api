package cn.sibetech.core.enums;

public enum OpenErrorCode {
    Missing_App_Id(10001, "缺少appId参数"),
    Invalid_App_Id(20001, "不合法的appId参数"),
    Missing_Signature(10005, "缺少签名参数"),
    Invalid_Signature(20005, "无效签名"),
    Missing_Timestamp(10006, "缺少时间戳参数"),
    Invalid_Argument(20000, "不合法的参数"),

    Invalid_JWT_Token(10001, "Invalid token"),
    Missing_body(10002, "miss body"),
    UnknownAccount(2001, "账号不存在"),
    IncorrectCredentials(2002, "账号或密码错误"),
    LockedAccount(2003, "帐号被锁定"),
    WeakPassword(2003, "您的密码强度较弱，请立即更改"),
    SystemError(2000, "系统异常"),
    ValidCodeError(2005, "验证码错误"),
    ValidImgError(2006, "验证不通过"),
    InvalidCode(2004, "验证码已失效"),

    UNAUTHORIZED(401, "认证失败"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "请求的资源不存在"),
    MISS_PERMISSION(405, "API接口无权限调用"),
    TIME_OUT(408, "请求超时"),
    SERVER_ERROR(500, "服务器内部错误");

    private int code;

    private String description;

    OpenErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }


    public String getDescription() {
        return description;
    }

}
