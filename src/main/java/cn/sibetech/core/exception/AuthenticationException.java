package cn.sibetech.core.exception;


import cn.sibetech.core.enums.OpenErrorCode;

public class AuthenticationException extends RuntimeException {
    private OpenErrorCode openErrorCode;

    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(OpenErrorCode openErrorCode) {
        super(openErrorCode.getDescription());
        this.openErrorCode = openErrorCode;
    }

    public OpenErrorCode getOpenErrorCode() {
        return openErrorCode;
    }

    public void setOpenErrorCode(OpenErrorCode openErrorCode) {
        this.openErrorCode = openErrorCode;
    }
}
