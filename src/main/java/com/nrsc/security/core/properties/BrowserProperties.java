package com.nrsc.security.core.properties;

import lombok.Data;

/**
 * @author : Sun Chuan
 * @date : 2019/6/20 22:13
 */
@Data
public class BrowserProperties {

    /**指定默认的注册页面*/
    private String signUpUrl = "/defaultPage/default-signUp.html";

    /**指定默认的登陆页面*/
    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    /**指定默认的处理成功与处理失败的方法*/
    private LoginType loginType = LoginType.JSON;

    /**记住我的时间3600秒即1小时*/
    private int rememberMeSeconds = 3600;
}
