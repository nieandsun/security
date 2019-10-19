package com.nrsc.security.app;

import com.nrsc.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.core.properties.SecurityConstants;
import com.nrsc.security.core.social.SocialConfig;
import com.nrsc.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author : Sun Chuan
 * @date : 2019/10/15 10:57
 * Description: 资源服务器
 */
@Configuration
@EnableResourceServer
public class NrscResourcesServerConfig  extends ResourceServerConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler NRSCAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler NRSCAuthenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    /**
     * @see SocialConfig#nrscSocialSecurityConfig()
     */
    @Autowired
    private SpringSocialConfigurer nrscSocialSecurityConfig;


    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(NRSCAuthenticationSuccessHandler)
                .failureHandler(NRSCAuthenticationFailureHandler);

        //验证码有一些问题：因为验证码会放到session里，校验也需要从session里取，
        // 但是在token认证的情景下是不需session参与的
        // 所以这里先注释掉，下篇文章再解决
        http//.apply(validateCodeSecurityConfig)
                //	.and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(nrscSocialSecurityConfig)
                .and()
                .authorizeRequests()
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        nrscSecurityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX+"/*",
                        nrscSecurityProperties.getBrowser().getSignUpUrl(),
                        nrscSecurityProperties.getBrowser().getSession().getSessionInvalidUrl(),
                        nrscSecurityProperties.getBrowser().getSignOutUrl(),
                        "/user/regist")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
