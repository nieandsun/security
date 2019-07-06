package com.nrsc.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/6 17:24
 * Description: 以后还会有别的验证码生成逻辑，故将其统一定义出来
 */
public interface ValidateCodeGenerator {
    ImageCode generate(ServletWebRequest request);
}
