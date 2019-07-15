package com.nrsc.security.core.validate.code;

import com.nrsc.security.core.properties.SecurityProperties;
import com.nrsc.security.core.validate.code.image.ImageCodeGenerator;
import com.nrsc.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.nrsc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/6 17:29
 */
@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    //@ConditionalOnMissingBean注解后面可以是name也可以是Type（XXX.class）
    //如果为name表示当spring容器里没有该名字的类时，被@ConditionalOnMissingBean注解的bean才可以注册到spring容器
    //如果为type表示当spring容器里没有该类型的类时，被@ConditionalOnMissingBean注解的bean才可以注册到spring容器

    //假如业务觉得我们提供的代码生成器太low，想换一个高大上的，比较好的做法不是随意改我们提供的默认代码
    //而是自己实现一套逻辑覆盖掉我们的逻辑------》实际开发中这叫“增量开发”

    //记录一个问题---------------------------------------之后会慢慢去研究----------------------------------------
    /***
     * 其实我还一直有一个疑惑----》
     *   比如说进行认证成功的代码开发时，只要我们实现AuthenticationSuccessHandler接口，当认证成功后就会走我们定义的
     *   逻辑----》这是为什么？？？
     *   肯定不是使用了@ConditionalOnMissingBean，因为使用该注解的话，spring-security默认实现了该接口的bean就不会
     *   注入到spring容器中，但我们的逻辑却是  如果配置文件里传的为REDIRECT的话，就会调用spring-security默认的认证
     *   成功逻辑---------》
     */
    //记录一个问题---------------------------------------之后会慢慢去研究------------------------------------------
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }


    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}
