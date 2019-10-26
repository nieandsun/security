package com.nrsc.security.core.properties;

import lombok.Data;

/**
 * @author : Sun Chuan
 * @date : 2019/10/25 22:41
 * Description：
 */
@Data
public class Oauth2ServerProperties {
    /***
     *  jwt的密签 --- 发出去的令牌需要它签名，验证令牌时也用它来验
     *  ★一定要保护好，别人知道了就可以拿着它来签发你的jwt令牌了
     */
    private String jwtSigningKey = "nrsc";

    /***
     * 指定使用哪个TokenStore
     */
    private String tokenStore;
}
