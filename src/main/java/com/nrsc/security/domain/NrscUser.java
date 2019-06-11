package com.nrsc.security.domain;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 该类就是实际生产中我们自己的user类了,它对应于我们数据库中的用户信息表
 */
@Data
public class NrscUser extends User {


    /**
     * 实现父类的构造方法
     * @param username
     * @param password
     * @param authorities
     */
    public NrscUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    /**
     * 实现父类构造方法
     * @param username
     * @param password
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public NrscUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    /**
     * 电话
     */
    private String mobile;

    /**
     * email
     */
    private String email;

}
