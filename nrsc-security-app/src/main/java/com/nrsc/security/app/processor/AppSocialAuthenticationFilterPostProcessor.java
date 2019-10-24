package com.nrsc.security.app.processor;

import com.nrsc.security.core.social.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author : Sun Chuan
 * @date : 2019/10/22 0:26
 * Description：设置app下springsocial走的成功处理器
 */
@Component
public class AppSocialAuthenticationFilterPostProcessor implements SocialAuthenticationFilterPostProcessor {
    //认证成功后返回token的成功处理器
    @Autowired
    private AuthenticationSuccessHandler NRSCAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
        socialAuthenticationFilter.setAuthenticationSuccessHandler(NRSCAuthenticationSuccessHandler);
    }
}
