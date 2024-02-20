package cn.sibetech.core.request;

import org.apache.commons.lang3.StringUtils;

/**
 * @author liwj
 * @date 2022/2/17 12:36
 */
public class NotPermissionException extends RuntimeException{
    public NotPermissionException(String permission) {
        super(permission);
    }

    public NotPermissionException(String[] permissions) {
        super(StringUtils.join(permissions, ","));
    }
}
