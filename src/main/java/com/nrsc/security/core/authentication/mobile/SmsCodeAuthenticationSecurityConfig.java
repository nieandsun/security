package com.nrsc.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author: Sun Chuan
 * @date: 2019/7/15 19:53
 * Description: 将smsCodeAuthenticationProvider 和 smsCodeAuthenticationFilter 放到spring-security过滤器链
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationSuccessHandler NRSCAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler NRSCAuthenticationFailureHandler;

    @Autowired
    private UserDetailsService NRSCDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        //可以查看AbstractAuthenticationProcessingFilter源码,AuthenticationManager需要进行set进来
        //UsernamePasswordAuthenticationFilter之所以没有进行set,是因为配置文件里默认给UsernamePasswordAuthenticationFilter进行了set
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        //可以查看AbstractAuthenticationProcessingFilter源码,该Filter给了一个默认的成功处理器和失败处理器
        //为了使用我们自己写的,这里也需要重新set一下
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(NRSCAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(NRSCAuthenticationFailureHandler);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        //注意这里直接将NRSCDetailsService给set进去了,实际业务中要在NRSCDetailsService里判断我们的数据库有没有该电话号码
        smsCodeAuthenticationProvider.setUserDetailsService(NRSCDetailsService);

        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
