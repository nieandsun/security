package com.nrsc.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * "大盒子"类-----------------用于统一管理项目中所有由yml或properties文件传入的变量值
 * @author : Sun Chuan
 * @date : 2019/6/20 22:13
 */
@Component //将此类注入到spring容器中
@Data //不用写get set方法了
@ConfigurationProperties(prefix = "nrsc.security") //指定以nrsc.security开头的配置会射入到该类中
public class NrscSecurityProperties {
    /**封装浏览器相关的属性*/
    private BrowserProperties browser = new BrowserProperties();
    /**验证码相关的属性---可能包含图形验证码，短信验证码等，所以对其进行了又一次封装*/
    private ValidateCodeProperties code = new ValidateCodeProperties();

    private NrscSocialProperties social = new NrscSocialProperties();
}
