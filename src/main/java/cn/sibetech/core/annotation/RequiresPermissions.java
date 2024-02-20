package cn.sibetech.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证
 * @author liwj
 * @date 2022/2/17 10:37
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresPermissions {
    /**
     * 需要校验的权限
     */
    String[] value() default {};

    /**
     * 验证模式： AND | OR
     * @return
     */
    Logical logical() default Logical.AND;
}
