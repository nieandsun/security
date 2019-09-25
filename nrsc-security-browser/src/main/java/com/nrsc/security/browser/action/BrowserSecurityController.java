package com.nrsc.security.browser.action;

import com.nrsc.security.browser.beans.SocialUserInfo;
import com.nrsc.security.core.enums.ResultEnum;
import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.core.properties.SecurityConstants;
import com.nrsc.security.core.utils.ResultVOUtil;
import com.nrsc.security.core.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/6/15 19:55
 */
@RestController
@Slf4j
public class BrowserSecurityController {
    //用户在访问我们的项目时,如果需要身份认证,spring-security会根据
    //我在SecurityConfig中loginPage的配置跳转到我自定义的url即/authentication/require,
    //但在这个跳转之前spring-security会将我们的请求缓存到RequestCache的session里,
    //通过该类可以从session中再将缓存的请求信息拿出来
    private RequestCache requestCache = new HttpSessionRequestCache();

    //spring-security提供的类,可以方便的进行重定向
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    //如果用户输入以.html结尾的url时,跳转到从配置文件yml或properties里拿出配置的登陆页面
    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResultVO requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //获取引发跳转的请求
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        //如果有这个引发跳转的请求的话,且该请求以.html结尾,将重定向到我们的登陆页/nrsc-login.html
        if (savedRequest != null) {
            //获取请求的url
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是:" + targetUrl);

            //如果请求url以.html结尾跳转到我们自己写的登录页----在前后端分离的项目里一般不会这样做
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                redirectStrategy.sendRedirect(request, response, nrscSecurityProperties.getBrowser().getLoginPage());
            }
        }
        //如果有引发跳转的请求且不以html结尾
        //或者如果没有引发跳转的请求----即直接访问authentication/require
        //返回一个未认证的状态码并引导用户进行登陆
        return ResultVOUtil.error(ResultEnum.UNAUTHORIZED.getCode(), ResultEnum.UNAUTHORIZED.getMessage());
    }

    /**
     * 获取第三方用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/social/user")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
        SocialUserInfo userInfo = new SocialUserInfo();
        //从session里取出封装了第三方信息QQ用户信息）的Connection对象
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        userInfo.setProviderId(connection.getKey().getProviderId());
        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
        userInfo.setNickName(connection.getDisplayName());
        userInfo.setHeadImg(connection.getImageUrl());
        return userInfo;
    }

    /***
     * session超时的时候会请求该方法
     * @return
     */
    @GetMapping("/session/invalid")
    public ResultVO sessionInvalid(){
        return ResultVOUtil.error(ResultEnum.SESSION_INVALID.getCode(), ResultEnum.SESSION_INVALID.getMessage());
    }


}
