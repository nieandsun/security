package com.nrsc.security.core.validate.code.image;

import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.core.validate.code.ValidateCodeGenerator;
import lombok.Data;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/6 17:25
 * Description:
 *          (1)请求中可以传入高度和宽度
 *          (2)配置文件中可以配置高度、宽度、验证码的位数和过期时间
 */
@Data
public class ImageCodeGenerator implements ValidateCodeGenerator {

    public NrscSecurityProperties nrscSecurityProperties;

    @Override
    public ImageCode generate(ServletWebRequest request) {
        //ServletRequestUtils工具的作用是
        //如果可以获取到请求中name为"width"的变量,将其转为int类型并作为返回值
        //如果获取不到请求中name为"width"的变量,则将第三个参数进行返回即下面的securityProperties.getCode().getImage().getWidth()
        int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width",
                nrscSecurityProperties.getCode().getImage().getWidth());

        int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height",
                nrscSecurityProperties.getCode().getImage().getHeight());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        for (int i = 0; i < nrscSecurityProperties.getCode().getImage().getLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand += rand;
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand, nrscSecurityProperties.getCode().getImage().getExpireIn());
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
