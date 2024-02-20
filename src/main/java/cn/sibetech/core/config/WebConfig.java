package cn.sibetech.core.config;

import cn.sibetech.core.filter.AppFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liwj
 * @date 2021/7/6 15:31
 */
@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean appFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new AppFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}
