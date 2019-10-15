package com.nrsc.security.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author : Sun Chuan
 * @date : 2019/10/12 23:39
 * Description：
 */
@Configuration
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //security5+ 认证默认为表单了也就是http.formLogin()
        http.httpBasic();
    }
}
