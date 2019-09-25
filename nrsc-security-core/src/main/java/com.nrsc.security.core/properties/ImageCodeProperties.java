package com.nrsc.security.core.properties;

import lombok.Data;

/**
 * @author: Sun Chuan
 * @date: 2019/7/6 16:50
 */
@Data
public class ImageCodeProperties extends SmsCodeProperties {

    /**验证码的宽度*/
    private int width = 67;

    /**验证码的高度*/
    private int height = 23;

    /**保证初始化ImageCodeProperties时length的默认值为4*/
    public ImageCodeProperties() {
        setLength(4);
    }
}
