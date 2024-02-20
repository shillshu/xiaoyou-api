package cn.sibetech.core.aspect;

import cn.sibetech.core.annotation.Logical;
import cn.sibetech.core.annotation.RequiresPermissions;
import cn.sibetech.core.filter.SecurityContextHolder;
import cn.sibetech.core.request.NotPermissionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author liwj
 * @date 2022/2/17 10:44
 */
@Aspect
@Component
public class AuthorizeAspect {
    public AuthorizeAspect(){}
    /**
     * 定义AOP签名 (切入所有使用鉴权注解的方法)
     */
    public static final String POINTCUT_SIGN = " @annotation(cn.sibetech.core.annotation.RequiresPermissions) || " +
            "@within(cn.sibetech.core.annotation.RequiresPermissions)";

    /**
     * 声明AOP签名
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut() {
    }
    /**
     * 环绕切入
     *
     * @param joinPoint 切面对象
     * @return 底层方法执行后的返回值
     * @throws Throwable 底层方法抛出的异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 注解鉴权
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        checkAnnotation(signature.getMethod());
        try {
            // 执行原有逻辑
            Object obj = joinPoint.proceed();
            return obj;
        }
        catch (Throwable e) {
            throw e;
        }
    }
    /**
     * 对一个Method对象进行注解检查
     */
    public void checkAnnotation(Method method) {
        // 校验 @RequiresPermissions 注解
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);

        if (requiresPermissions == null) {
            requiresPermissions = AnnotationUtils.findAnnotation(method.getDeclaringClass(), RequiresPermissions.class);
        }
        checkRequiresPermissions(requiresPermissions);
    }

    private void checkRequiresPermissions(RequiresPermissions requiresPermissions){
        if (requiresPermissions != null) {
            List<String> userPerms = SecurityContextHolder.getCurrentUser().getPermissions();
            if (requiresPermissions.logical() == Logical.AND) {
                for(String perm: requiresPermissions.value()) {
                    if(!hasPerm(userPerms, perm)) {
                        throw new NotPermissionException(requiresPermissions.value());
                    }
                }
            }
            else {
                for(String perm: requiresPermissions.value()) {
                    if(hasPerm(userPerms, perm)) {
                        return;
                    }
                }
                if(requiresPermissions.value().length>0) {
                    throw new NotPermissionException(requiresPermissions.value());
                }
            }
        }
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPerm(List<String> authorities, String permission) {
        return authorities.stream().anyMatch(x -> x.equals(permission));
    }
}
