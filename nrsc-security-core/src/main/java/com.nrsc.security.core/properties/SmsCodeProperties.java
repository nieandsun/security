package com.nrsc.security.core.properties;

import lombok.Data;

/**
 * @author: Sun Chuan
 * @date: 2019/7/9 12:34
 * Description:
 */
@Data
public class SmsCodeProperties {

    /**验证码的位数*/
    private int length = 6;

    /**验证码过期时间*/
    private int expireIn = 60;

    /**需要进行图形验证码校验的所有URL，需以逗号分隔开*/
    private String urls;
}
