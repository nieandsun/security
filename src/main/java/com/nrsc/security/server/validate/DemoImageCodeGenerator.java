package com.nrsc.security.server.validate;

import com.nrsc.security.core.validate.code.ImageCode;
import com.nrsc.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/6 19:03
 */
//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("更高级的图形验证码生成代码");
        return null;
    }
}
