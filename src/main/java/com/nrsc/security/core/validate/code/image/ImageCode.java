package com.nrsc.security.core.validate.code.image;

import com.nrsc.security.core.validate.code.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/4 21:32
 * ImageCode对象没有实现Serializable 接口，
 * 且属性 --- BufferedImage对象，也没有实现Serializable 接口，因此将该对象存到redis里时会报出序列化错误。
 *
 * 经过分析可知，我们只需要把验证码和过期时间放到redis里就行了，因为图形验证码验证的是验证码，不是图形
 * 所以没必要把BufferedImage对象存到session和redis里。
 * 父类实现了Serializable 就当子类也实现了，所以可以只在其父类实现就好了
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
