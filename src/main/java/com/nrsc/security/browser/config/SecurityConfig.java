package com.nrsc.security.browser.config;

import com.nrsc.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationSuccessHandler NRSCAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler NRSCAuthenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/authentication/require")//登陆时进入的url-->相当于进入登陆页面
                .loginProcessingUrl("/nrsc/signIn")//告诉spring-security点击登陆时访问的url为/nrsc/signIn
                                            // ---->当spring-security接收到此url的请求后,会自动调用
                                            //com.nrsc.security.browser.action.NRSCDetailsService中的loadUserByUsername
                                            //进行登陆校验

                .successHandler(NRSCAuthenticationSuccessHandler)//指定使用NRSCAuthenticationSuccessHandler处理登陆成功后的行为
                .failureHandler(NRSCAuthenticationFailureHandler)//指定使用NNRSCAuthenticationFailureHandler处理登陆失败后的行为
                .and()
                .authorizeRequests()
                .antMatchers("/authentication/require", securityProperties.getBrowser().getLoginPage())//指定不校验的url
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable(); //关闭csrf
    }
}
