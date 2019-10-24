package com.nrsc.security.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author : Sun Chuan
 * @date : 2019/10/24 13:56
 * Description: TokenStore的实现类有5个，这里将RedisTokenStore注入spring容器
 */
@Configuration
public class TokenStoreConfig {

    /***
     * RedisTokenStore需要一个连接工厂，这里可以直接注入进来
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /***
     * 将RedisTokenStore注入到spring容器
     * @return
     */
    @Bean
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

}
