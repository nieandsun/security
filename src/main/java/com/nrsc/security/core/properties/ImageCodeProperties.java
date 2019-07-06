package com.nrsc.security.core.properties;

import lombok.Data;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/6 16:50
 */
@Data
public class ImageCodeProperties {
    /**验证码的宽度*/
    private int width = 67;
    /**验证码的高度*/
    private int height = 23;
    /**验证码的位数*/
    private int length = 4;
    /**验证码过期时间*/
    private int expireIn = 60;

    /**需要进行图形验证码校验的所有URL，需以逗号分隔开*/
    private String urls;
}
