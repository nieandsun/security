package com.nrsc.security.controller;


import com.nrsc.security.app.utils.AppSignUpUtils;
import com.nrsc.security.core.properties.NrscSecurityProperties;
import com.nrsc.security.domain.NrscUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private AppSignUpUtils appSignUpUtils;

    @Autowired
    private NrscSecurityProperties nrscSecurityProperties;

    @GetMapping("/hello")
    public String sayHello() {
        return "hello spring-security";
    }

    @GetMapping("/me1")
    public Object getCurrentUser1() {
        //方式1，直接从SecurityContextHolder中拿到Authentication对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @GetMapping("/me2")
    public Object getCurrentUser2(Authentication authentication, HttpServletRequest request) throws UnsupportedEncodingException {
        //方式2---方式1的简写版
        //从请求头中获取到JWT
        String token = StringUtils.substringAfter(request.getHeader("Authorization"), "bearer ");
        //对JWT进行解析,注意由于JWT生成时编码格式用的UTF-8（看源码可以追踪到）
        //但Jwts工具用到的默认编码格式不是，所以要设置其编码格式为UTF-8
        Claims claims = Jwts.parser()
                .setSigningKey(nrscSecurityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();
        //取出扩展信息，并打印
        String company = (String) claims.get("company");

        System.err.println(company);
        return authentication;
    }

    @GetMapping("/me3")
    public Object getCurrentUser3(@AuthenticationPrincipal UserDetails user) {
        //方式3，只获取User对象
        return user;
    }

    /***
     * JWT 情况下获取的principal是一个字符串
     * @param user
     * @return
     */
    @GetMapping("/me4")
    public Object getCurrentUser4(@AuthenticationPrincipal String user) {
        //方式3，只获取User对象
        return user;
    }

    @PostMapping("/register")
    public void register(NrscUser user, HttpServletRequest request) {
        //注册用户相关逻辑-----》即向用户表里插入一条用户数据-----》这里不写了

        //不管是注册用户还是绑定用户，都会拿到一个用户唯一标识，如用户名。
        String userId = user.getUsername();
        //将用户userId和第三方用户信息建立关系并将其插入到userconnection表
        //providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));

        //使用我们自己的utils将用户userId和第三方用户信息建立关系、将该关系插入到userconnection表
        //和删掉redis中保存的deviceId信息
        appSignUpUtils.doPostSignUp(new ServletWebRequest(request), userId);
    }
}
