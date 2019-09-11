package com.nrsc.security.server.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component("NRSCDetailsService")
@Slf4j
public class NRSCDetailsService implements UserDetailsService , SocialUserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        //点击页面的登陆时,你实际需要做的事为
//        //1.拿着用户名去数据库里查询其密码
//        //2.将拿到的用户名和密码封装到User里进行返回
//
//        log.info("用户名为:" + username);
//        //假设下面的密码是根据用户名获得的
//        String password = passwordEncoder.encode("123456");
//        log.info("password:" + password);
//        //return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
////        return new User(username, password, true,true,true,false,
////                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//        return new NrscUser(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("表单登录用户名:" + username);
        return buildUser(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        log.info("社交登录用户Id:" + userId);
        return buildUser(userId);
    }

    private SocialUserDetails buildUser(String userId) {
        // 根据用户名查找用户信息
        //根据查找到的用户信息判断用户是否被冻结
        String password = passwordEncoder.encode("123456");
        log.info("数据库密码是:"+password);
        return new SocialUser(userId, password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}