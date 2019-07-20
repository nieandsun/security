package com.nrsc.security.core.validate.code;

import com.nrsc.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author: Sun Chuan
 * @date: 2019/7/4 21:28
 */
@RestController
public class ValidateCodeController {

    /**
     * 收集系统中所有的 {@link ValidateCodeProcessor} 接口的实现,并将其存到Map中。
     */
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     *
     * @param request
     * @param response
     * @param type
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
        /**
         * ServletWebRequest是一个包装类，
         * 如果参数为new ServletWebRequest(request, response)
         * 可以直接用ServletWebRequest request 去接，
         * 省去了写response对象的麻烦，如果想要再获取response对象，只需要用request.getResponse()方法就可以
         */
        validateCodeProcessors.get(type + "CodeProcessor").create(new ServletWebRequest(request, response));
    }

}
