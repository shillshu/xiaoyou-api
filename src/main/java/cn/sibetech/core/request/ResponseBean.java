package cn.sibetech.core.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author liwj
 * @date 2021/4/8 18:45
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBean<T> implements Serializable {
    public final static String ERR_MSG_OK="ok";
    public final static String ERR_CODE_OK="0";
    public final static String ERR_CODE_500="500";
    private String errcode;

    private String errmsg;

    private T data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseBean<T> okMsg(String msg){
        return restResult(null, ERR_CODE_OK, msg);
    }
    public static <T> ResponseBean<T> ok(T data) {
        return restResult(data, ERR_CODE_OK, ERR_MSG_OK);
    }
    public static <T> ResponseBean<T> ok(String msg, T data) {
        return restResult(data, ERR_CODE_OK, msg);
    }
    public static <T> ResponseBean<T> error(String msg) {
        return restResult(null, ERR_CODE_500, msg);
    }
    public static <T> ResponseBean<T> error(String code, String msg) {
        return restResult(null, code, msg);
    }
    public static <T> ResponseBean<T> error(String code, String msg, T data) {
        return restResult(data, code, msg);
    }
    private static <T> ResponseBean<T> restResult(T data, String code, String msg) {
        ResponseBean<T> apiResult = new ResponseBean<T>();
        apiResult.setErrcode(code);
        apiResult.setData(data);
        apiResult.setErrmsg(msg);
        return apiResult;
    }
}
