package com.nrsc.security.core.validate.code;

import com.nrsc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Sun Chuan
 * @date: 2019/7/4 21:28
 */
@RestController
public class ValidateCodeController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    /**session操作工具类*/
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**图形验证码生成逻辑封装类*/
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    /**短信验证码生成逻辑封装类*/
    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    /**短信发送类*/
    @Autowired
    private SmsCodeSender smsCodeSender;

    /**
     * 生成图形验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.生成ImageCode对象
        ImageCode imageCode = (ImageCode) imageCodeGenerator.generate(new ServletWebRequest(request));
        //2.将ImageCode对象写入到session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, imageCode);
        //3.将验证码写回到浏览器
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }


    /**
     * 生成短信验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        //1.获取传过来的电话号码
        String mobile = ServletRequestUtils.getRequiredStringParameter(request, "mobile");
        //2.生成SmsCode对象
        ValidateCode smsCode = smsCodeGenerator.generate(new ServletWebRequest(request));
        //3.将ImageCode对象写入到session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY, smsCode);
        //4.将生成的验证码通过手机号发给用户
        smsCodeSender.send(mobile, smsCode.getCode());
    }
}
