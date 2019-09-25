package com.nrsc.security.core.social.configutils;

/**
 * @author : Sun Chuan
 * @date : 2019/8/17 1:52
 * Description：从springboot1.5.6.RELEASE版本的org.springframework.boot.autoconfigure.social下拷贝的源码
 */
public class SocialProperties {

    /**
     * Application id.
     */
    private String appId;

    /**
     * Application secret.
     */
    private String appSecret;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
