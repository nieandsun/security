package com.nrsc.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/4 21:28
 */
@RestController
public class ValidateCodeController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";
    //session操作工具类
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    //验证码生成逻辑封装类
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    /**
     * 生成图形验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.生成ImageCode对象
        ImageCode imageCode = imageCodeGenerator.generate(new ServletWebRequest(request));
        //2.将ImageCode对象写入到session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        //3.将验证码写回到浏览器
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }
}
