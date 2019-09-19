package com.nrsc.security.core.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrsc.security.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : Sun Chuan
 * @date : 2019/9/17 23:22
 * Description： 获取当前用户所有第三方账号的绑定状态的视图
 */
@Component("connect/status")
@Slf4j
public class NrscConnectionStatusView extends AbstractView {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        //取出所有的Connection对象
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) map.get("connectionMap");
        //取出所有的providerId
        Set<String> providerIds = (Set<String>) map.get("providerIds");
        log.info("providerIds:{}",providerIds);

        //封装当前用户的第三方账号绑定状态 key为providerId ， value为true或false
        Map<String, Boolean> result = new HashMap<>();
        for (String key : connections.keySet()) {
            result.put(key, CollectionUtils.isNotEmpty(connections.get(key)));
        }
        //将当前用户的第三方账号绑定状态返回给浏览器
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(ResultVOUtil.success(result)));
    }
}
