package com.nrsc.security.browser.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/6/18 19:32
 */
//@Component(value = "NRSCAuthenticationSuccessHandler")
public class NRSCAuthenticationSuccessHandler_bak implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Authentication封装了用户认证成功的信息
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException, ServletException {
        //设置返回内容的数据形式和编码格式
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //将户认证成功的信息以json数据的形式返回给前端
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
    }
}
