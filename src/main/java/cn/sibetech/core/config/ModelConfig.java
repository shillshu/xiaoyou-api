package cn.sibetech.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liwj
 * @date 2020/12/14
 */
@Configuration
public class ModelConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
