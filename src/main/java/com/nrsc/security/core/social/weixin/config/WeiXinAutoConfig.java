package com.nrsc.security.core.social.weixin.config;

import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.core.properties.WeiXinProperties;
import com.nrsc.security.core.social.configutils.SocialAutoConfigurerAdapter;
import com.nrsc.security.core.social.weixin.connect.WeiXinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @author : Sun Chuan
 * @date : 2019/9/15 22:56
 * Description：将微信连接工厂注入到spring容器
 */
@Configuration
@ConditionalOnProperty(prefix = "nrsc.security.social.weixin", name = "app-id")
public class WeiXinAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeiXinProperties weiXinProperties = nrscSecurityProperties.getSocial().getWeixin();
        return new WeiXinConnectionFactory(weiXinProperties.getProviderId(), weiXinProperties.getAppId(),
                weiXinProperties.getAppSecret());
    }

}
