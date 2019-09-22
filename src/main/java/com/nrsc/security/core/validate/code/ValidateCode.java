package com.nrsc.security.core.validate.code;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: Sun Chuan
 * @date: 2019/7/9 12:01
 * Description: 存放到redis里的对象必须是序列化的，所以这里要实现Serializable接口
 */
@Data
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = -5882129757498488074L;
    /**随机验证码*/
    private String code;
    /**本地当前时间*/
    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime){
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
