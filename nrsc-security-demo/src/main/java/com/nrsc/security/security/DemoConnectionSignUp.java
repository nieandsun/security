package com.nrsc.security.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * @author : Sun Chuan
 * @date : 2019/9/14 23:29
 * Description：ConnectionSignUp类，需要注入到JdbcUsersConnectionRepository类里，
 * 以实现第三方登陆时免注册的逻辑功能
 */
@Component
public class DemoConnectionSignUp implements ConnectionSignUp {
    @Override
    public String execute(Connection<?> connection) {
        //真实项目中： TODO
        //这里其实应该向我们的用户业务表（user表）里插入一条数据
        //可以将插入user数据后的主键作为userId进行返回

        //这里为了简单起见，我就直接将connection对象中的displayName作为useId进行返回了
        return connection.getDisplayName();
    }
}
