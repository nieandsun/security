package com.nrsc.security.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : Sun Chuan
 * @date : 2019/9/29 17:43
 * Description: 弄一个配置类主要用来将一些公共的bean注入到spring容器
 */
@Configuration
public class SecurityCoreConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
