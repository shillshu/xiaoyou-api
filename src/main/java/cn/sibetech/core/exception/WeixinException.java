package cn.sibetech.core.exception;

public class WeixinException extends RuntimeException{

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    private int errcode;

    public WeixinException() {
    }

    public WeixinException(String message) {
        super(message);
    }

    public WeixinException(int errcode, String message) {
        super(message);
        this.errcode = errcode;
    }
}
