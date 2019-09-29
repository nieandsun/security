package com.nrsc.security.app.authentication;

import com.alibaba.fastjson.JSON;
import com.nrsc.security.core.properties.LoginType;
import com.nrsc.security.core.properties.NrscSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/6/18 19:32
 */
@Slf4j
@Component(value = "NRSCAuthenticationSuccessHandler")
public class NRSCAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    @Autowired
//    private ObjectMapper objectMapper;

    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;
    /**
     * Authentication封装了用户认证成功的信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException, ServletException {

        log.info("登陆成功");

        //如果设置了登陆成功返回json,则按如下方式进行返回
        if (LoginType.JSON.equals(nrscSecurityProperties.getBrowser().getLoginType())) {
            //设置返回内容的数据形式和编码格式
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            //将户认证成功的信息以json数据的形式返回给前端
            httpServletResponse.getWriter().write(JSON.toJSONString(authentication));
        } else {
            //如果登陆成功后不想返回json,可以按照spring-security默认方式进行处理(登录成功后会跳转到引发登录的请求上)
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }
    }
}
