package com.nrsc.security.browser.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/6/18 19:46
 */
@Component("NRSCAuthenticationFailureHandler")
public class NRSCAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     *   AuthenticationException里封装了用户登陆失败的错误信息
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e)
            throws IOException, ServletException {
        //修改状态码
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        //设置返回内容的数据形式和编码格式
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //将抓到的错误信息以json数据的形式进行返回
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(e));
    }
}
