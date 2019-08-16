package com.nrsc.security.core.properties;

import com.nrsc.security.core.social.configutils.SocialProperties;
import lombok.Data;

/**
 * @author : Sun Chuan
 * @date : 2019/8/7 20:43
 * Description:
 */
@Data
public class QQProperties extends SocialProperties {
    private String providerId = "qq";
}
