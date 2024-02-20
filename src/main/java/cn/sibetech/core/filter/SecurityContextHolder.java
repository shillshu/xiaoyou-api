package cn.sibetech.core.filter;


import cn.sibetech.core.domain.ShiroUser;

/**
 * @author liwj
 * @date 2021/4/2 10:32
 */
public class SecurityContextHolder {
    private static ThreadLocal<ShiroUser> contextHolder = new ThreadLocal<ShiroUser>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static ShiroUser getCurrentUser(){
        return contextHolder.get();
    }

    public static void setCurrentUser(ShiroUser shiroUser) {
        contextHolder.set(shiroUser);
    }
}
