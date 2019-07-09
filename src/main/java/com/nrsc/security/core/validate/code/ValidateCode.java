package com.nrsc.security.core.validate.code;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: Sun Chuan
 * @date: 2019/7/9 12:01
 * Description:
 */
@Data
public class ValidateCode {

    /**随机验证码*/
    private String code;
    /**本地当前时间*/
    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
