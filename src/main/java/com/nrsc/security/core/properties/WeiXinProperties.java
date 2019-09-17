package com.nrsc.security.core.properties;

import com.nrsc.security.core.social.configutils.SocialProperties;
import lombok.Data;

/**
 * @author : Sun Chuan
 * @date : 2019/9/15 22:33
 * Description：
 */
@Data
public class WeiXinProperties extends SocialProperties {
    private String providerId = "weixin";
}
