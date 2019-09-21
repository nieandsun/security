package com.nrsc.security.browser;

import com.nrsc.security.core.AbstractChannelSecurityConfig;
import com.nrsc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.core.properties.SecurityConstants;
import com.nrsc.security.core.social.SocialConfig;
import com.nrsc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService NRSCDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @see SocialConfig#nrscSocialSecurityConfig()
     */
    @Autowired
    private SpringSocialConfigurer nrscSocialSecurityConfig;

    /**
     * session失效策略
     */
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;
    /***
     * session并发策略
     */
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        applyPasswordAuthenticationConfig(http);
        http.apply(validateCodeSecurityConfig)
                .and()
                    //短信校验相关配置
                    .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                    //springsocial校验相关配置
                    .apply(nrscSocialSecurityConfig)
                .and()
                    //记住我相关配置
                    .rememberMe()
                        .tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(nrscSecurityProperties.getBrowser().getRememberMeSeconds())
                        .userDetailsService(NRSCDetailsService)
                .and()
                    //session相关的控制
                    .sessionManagement()
                        //指定session失效策略
                        .invalidSessionStrategy(invalidSessionStrategy)
                         //指定最大的session并发数量---即一个用户只能同时在一处登陆（腾讯视频的账号好像就只能同时允许2-3个手机同时登陆）
                        .maximumSessions(nrscSecurityProperties.getBrowser().getSession().getMaximumSessions())
                        //当超过指定的最大session并发数量时，阻止后面的登陆（感觉貌似很少会用到这种策略）
                        .maxSessionsPreventsLogin(nrscSecurityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                        //超过最大session并发数量时的策略
                        .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                    .authorizeRequests()
                    //配置不用进行认证校验的url
                    .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        nrscSecurityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        nrscSecurityProperties.getBrowser().getSignUpUrl(),
                        //session失效默认的跳转地址
                        nrscSecurityProperties.getBrowser().getSession().getSessionInvalidUrl(),
                        //获取第三方账号的用户信息的默认url
                        SecurityConstants.DEFAULT_GET_SOCIAL_USERINFO_URL,
                        "/user/register",
                        "/js/**"
                    )
                    .permitAll()
                    //指明除了上面不用认证的url外其他请求都需要认证校验
                    .anyRequest()
                    .authenticated()
                //关闭csrf
                .and()
                    .csrf().disable();
    }


}