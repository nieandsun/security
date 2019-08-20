package com.nrsc.security.core.social.qq;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author : Sun Chuan
 * @date : 2019/8/19 22:48
 * Description：继承springsocialSocial为我们提供的SocialAuthenticationFilter的配置类SpringSocialConfigurer
 * 并重写其后置处理方法，让其默认拦截包含我们配置字符串的请求
 */
public class NrscSpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    public NrscSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    /**
     * T 其实就是指的SocialAuthenticationFilter类型
     *
     * @param object
     * @param <T>
     * @return
     */
    @Override
    protected <T> T postProcess(T object) {
        //在父类处理完SocialAuthenticationFilter之后的基础上，将其默认拦截url改成我们配置的值
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        return (T) filter;
    }
}
