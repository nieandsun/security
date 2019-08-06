package com.nrsc.security.core.properties;
import lombok.Data;

/**
 * @author : Sun Chuan
 * @date : 2019/7/6 16:51
 * Description: 验证码相关的封装类,之后还会有其他的验证码,所以将图片验证码，短信验证码等统一封装到该类里
 */
@Data
public class ValidateCodeProperties {
    /**封装图片验证吗相关的属性*/
    private ImageCodeProperties image = new ImageCodeProperties();
    /**封装短信验证吗相关的属性*/
    private SmsCodeProperties sms = new SmsCodeProperties();
}
