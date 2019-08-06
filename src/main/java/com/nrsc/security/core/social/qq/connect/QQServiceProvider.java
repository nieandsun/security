package com.nrsc.security.core.social.qq.connect;

import com.nrsc.security.core.social.qq.api.QQ;
import com.nrsc.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @author : Sun Chuan
 * @date : 2019/8/5 23:47
 * Description：利用利用Api和Oauth2Operation对象组装ServiceProvider对象
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 对应于OAuth2协议第一步,即将用户导向QQ认证授权页面的url
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 对应于Oauth2协议中拿着授权码获取Access Token这一步的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * 必须要有一个构造,否则会报错
     *
     * @param appId
     * @param appSecret
     */
    public QQServiceProvider(String appId, String appSecret) {
        /**
         * 先利用springsocial提供的OAuth2Operation实现类-->OAuth2Template走OAuth2协议的步骤(但有一点问题)
         */
        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }
}
