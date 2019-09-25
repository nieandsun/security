package com.nrsc.security.browser.beans;

import lombok.Data;

/**
 * @author : Sun Chuan
 * @date : 2019/9/11 23:03
 * Description：展示给前端的第三方用户信息
 */
@Data
public class SocialUserInfo {
    /**提供商唯一标识*/
    private String providerId;

    /***用户在提供商的唯一标识（其实就是openId）*/
    private String providerUserId;

    /**用户在提供商的昵称*/
    private String nickName;

    /**用户在提供商的头像*/
    private String headImg;
}
