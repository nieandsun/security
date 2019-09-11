package com.nrsc.security.core.social;

import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.core.social.jdbc.NrscJdbcUsersConnectionRepository;
import com.nrsc.security.core.social.qq.NrscSpringSocialConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @author : Sun Chuan
 * @date : 2019/8/7 20:57
 * Description:
 * UsersConnectionRepository的实现类，用来拿着Connection对象查找UserConnection表中是否与之相对应的userId
 * userId就是我们系统中的唯一标识，这个应该由各个系统自己根据业务去指定，比如说你系统里的username是唯一的，
 * 则这个useId就可以是username
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        /**
         * 第二个参数的作用：根据条件查找该用哪个ConnectionFactory来构建Connection对象
         * 第三个参数的作用: 对插入到userconnection表中的数据进行加密和解密
         */
        NrscJdbcUsersConnectionRepository repository = new NrscJdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        //设置userconnection表的前缀
        repository.setTablePrefix("nrsc_");
        return repository;
    }

    /**
     * 通过apply()该实例，可以将SocialAuthenticationFilter加入到spring-security的过滤器链
     */
    @Bean
    public SpringSocialConfigurer nrscSocialSecurityConfig() {
        // 默认配置类，进行组件的组装
        // 包括了过滤器SocialAuthenticationFilter 添加到security过滤链中
        String filterProcessesUrl = nrscSecurityProperties.getSocial().getFilterProcessesUrl();
        NrscSpringSocialConfigurer configurer = new NrscSpringSocialConfigurer(filterProcessesUrl);

        //指定SpringSocial/SpringSecurity跳向注册页面时的url为我们配置的url
        configurer.signupUrl(nrscSecurityProperties.getBrowser().getSignUpUrl());
        return configurer;
    }

    /**
     * ProviderSignInUtils有两个作用：
     * （1）从session里获取封装了第三方用户信息的Connection对象
     * （2）将注册的用户信息与第三方用户信息（QQ信息）建立关系并将该关系插入到userconnection表里
     * <p>
     * 需要的两个参数：
     * （1）ConnectionFactoryLocator 可以直接注册进来，用来定位ConnectionFactory
     * （2）UsersConnectionRepository----》getUsersConnectionRepository(connectionFactoryLocator)
     * 即我们配置的用来处理userconnection表的bean
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }
}
