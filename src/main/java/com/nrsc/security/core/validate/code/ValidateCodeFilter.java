package com.nrsc.security.core.validate.code;


import com.nrsc.security.core.properties.SecurityProperties;
import com.nrsc.security.core.validate.code.image.ImageCode;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created By: Sun Chuan
 * Created Date: 2019/7/4 22:05
 * Description：继承InitializingBean并实现其afterPropertiesSet的作用是，
 * 在当前bean的其他参数都组装完成后，调用afterPropertiesSet初始化下面的urls变量
 */
@Data
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private AuthenticationFailureHandler authenticationFailureHandler;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    //----------------------------下篇文章将研究一下InitializingBean接口----------------------------------
    /**
     * 用来存配置文件中指定的需要进行图形验证码校验的所有URL
     * <p>
     * 这里将所有的url弄成了本类的一个属性，其实还有其他方式，比如说我们只把SecurityProperties设置到本类中
     * 然后每次请求过来时，都做一次字符串切割和uri匹配
     * 但是相比之下这种方式是更好的
     */
    //----------------------------下篇文章将研究一下InitializingBean接口----------------------------------
    private Set<String> urls = new HashSet<>();

    /**
     * 拿到配置类
     */
    private SecurityProperties securityProperties;

    /**
     * spring提供的匹配uri的工具类，可匹配正则
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        //取出配置文件中的url，并用“，”进行分割
        String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrls(), ",");
        //将配置文件中配置的所有url加到urls属性中
        if (ArrayUtils.isNotEmpty(configUrls)) {
            for (String configUrl : configUrls) {
                urls.add(configUrl);
            }
        }
        //登陆肯定要进行图形验证码的校验，所以这里直接把登陆的uri放到urls里
        urls.add("/authentication/form");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        //只要请求的uri是配置的urls中的一个，则说明该请求需要进行图形验证码校验
        //因为uri中可能会包含参数，所以不能简单的用StringUtils.equalsIgnoreCase方法进行比较
        //spring提供的工具类AntPathMatcher可以对uri进行正则匹配，所以这里使用了该工具类
        for (String url : urls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                action = true;
                break;
            }
        }
        if (action) {
            try {
                //对输入的图形验证码进行校验
                validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                //认证失败调用之前写的认证失败的逻辑
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }

        }
        //走到这里说明已经校验成功,对请求放行
        filterChain.doFilter(request, response);
    }

    /**
     * 对输入的图形验证码的校验逻辑
     *
     * @param request
     * @throws ServletRequestBindingException
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        //1.从session中取出ImageCode对象
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,
                ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
        //2.获取请求中传过来的图形验证码字符串
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");

        //3.判断传过来的图形验证码字符串和session中存的是否相同
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        //4.走到这里说明校验已经通过,校验通过后该验证码就没啥用了,所以可以删除session中的ImageCode对象了
        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
    }
}
