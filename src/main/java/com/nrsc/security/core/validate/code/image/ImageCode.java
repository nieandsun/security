package com.nrsc.security.core.validate.code.image;

import com.nrsc.security.core.validate.code.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/4 21:32
 */
@Data
public class ImageCode extends ValidateCode {
    /**图片*/
    private BufferedImage image;


    /**
     * 构造-----expireIn 超时时间（单位秒）
     * @param image
     * @param code
     * @param expireIn
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

}
