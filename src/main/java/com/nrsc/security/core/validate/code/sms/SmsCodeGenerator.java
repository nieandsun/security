package com.nrsc.security.core.validate.code.sms;

import com.nrsc.security.core.properties.SecurityProperties;
import com.nrsc.security.core.validate.code.ValidateCode;
import com.nrsc.security.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: Sun Chuan
 * @date: 2019/7/9 12:29
 * @Description: 注意验证码+ 超时时间可以进行配置
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
