package com.nrsc.security.core.social.qq.connect;

import com.nrsc.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author : Sun Chuan
 * @date : 2019/8/7 20:29
 * Description: 组装ConnectionFactory对象---》ConnectionFactory对象由ServiceProvider和Adapter对象组成
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    /**
     * 除了ServiceProvider和Adapter对象还需要一个providerId---》提供商的唯一标识
     *
     * @param providerId
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
