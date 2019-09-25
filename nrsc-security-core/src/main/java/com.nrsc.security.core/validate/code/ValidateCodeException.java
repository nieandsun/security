package com.nrsc.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/4 22:15
 * Description:继承spring-security异常类的基类
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = -75098325592950112L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
