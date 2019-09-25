package com.nrsc.security.core.social;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrsc.security.core.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : Sun Chuan
 * @date : 2019/9/17 23:41
 * Description： 由于微信，QQ，微博等解绑和绑定都想用这个视图，但providerId不可能一样
 * 所以这里不能直接用@Component注解将其写死
 */
public class NrscConnectView extends AbstractView {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        //如果map（Model对象）里有Connection对象，则表示绑定，否则表示解绑
        if (map.get("connections") == null) {
            response.getWriter().write(JSON.toJSONString(ResultVOUtil.success("解绑成功")));
        } else {
            response.getWriter().write(JSON.toJSONString(ResultVOUtil.success("绑定成功")));
        }
    }
}
