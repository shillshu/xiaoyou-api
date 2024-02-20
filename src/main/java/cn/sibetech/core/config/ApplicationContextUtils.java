package cn.sibetech.core.config;

import org.springframework.context.ApplicationContext;

public class ApplicationContextUtils {
    public static ApplicationContext applicationContext;

    /**
     * 通过名称获取bean
     */
    public static Object get(String name) {
        return applicationContext.getBean(name);
    }


    /**
     * 通过类型获取bean
     */
    public static <T> T get(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 判断某个bean是不是存在
     */
    public static boolean has(String name) {
        return applicationContext.containsBean(name);
    }

    public static String getActiveProfile(){
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }
}
