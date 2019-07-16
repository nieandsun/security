package com.nrsc.security.browser.config;

import com.nrsc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.nrsc.security.core.properties.SecurityProperties;
import com.nrsc.security.core.validate.code.SmsCodeFilter;
import com.nrsc.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Autowired
    private UserDetailsService NRSCDetailsService;

    @Autowired
    //springboot会根据yml文件中的spring:datasource将数据源注入到spring容器
    //所以这里直接通过 @Autowired就可以拿到数据源
    private DataSource dataSource;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 第一次启动的时候自动建表（建议不用这句话，因为第二次启动会报错）
        // 建表语句可在JdbcTokenRepositoryImpl源码中找到
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(NRSCAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        //调用afterPropertiesSet，初始化urls
        validateCodeFilter.afterPropertiesSet();

        //直接复制的上面的代码，之后会进行优化
        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(NRSCAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        //调用afterPropertiesSet，初始化urls
        smsCodeFilter.afterPropertiesSet();


        //将图形验证码的校验逻辑放在用户名和密码校验逻辑之前
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/authentication/require")//登陆时进入的url-->相当于进入登陆页面
                .loginProcessingUrl("/authentication/form")//告诉spring-security点击登陆时访问的url为/authentication/form
                // ---->当spring-security接收到此url的请求后,会自动调用
                //com.nrsc.security.browser.action.NRSCDetailsService中的loadUserByUsername
                //.usernameParameter("user")-->与UsernamePasswordAuthenticationFilter中的usernameParameter对应,可修改其默认值
                //.passwordParameter("code")-->与UsernamePasswordAuthenticationFilter中的passwordParameter对应,可修改其默认值
                //进行登陆校验
                .successHandler(NRSCAuthenticationSuccessHandler)//指定使用NRSCAuthenticationSuccessHandler处理登陆成功后的行为
                .failureHandler(NRSCAuthenticationFailureHandler)//指定使用NNRSCAuthenticationFailureHandler处理登陆失败后的行为
                .and()

                //Remember相关配置
                .rememberMe()
                .tokenRepository(persistentTokenRepository())//指定使用的tokenRepository
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())//指定记住我的时间（秒）
                .userDetailsService(NRSCDetailsService)//指定进行登陆认证的UserDetailsService

                .and()
                .authorizeRequests()

                //不进认证的url
                .antMatchers("/code/*", "/authentication/require", securityProperties.getBrowser().getLoginPage())//指定不校验的url
                .permitAll()

                //除了不进行认证的url其他请求都需要认证
                .anyRequest()
                .authenticated()

                .and()
                .csrf().disable() //关闭csrf

                //将smsCodeAuthenticationSecurityConfig配置文件加到该配置文件里
                .apply(smsCodeAuthenticationSecurityConfig);
    }
}
