package com.nrsc.security.core.social.qq.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * @author : Sun Chuan
 * @date : 2019/8/5 14:52
 * Description: 通过查看QQ互联官网可知，获取用户信息接口需要三个参数access_token,appid和openid
 */

/**
 * 继承AbstractOAuth2ApiBinding,该类提供了一些帮我们快速获取服务提供商信息的方法和属性
 * 里面包含accessToken属性
 * 注意accessToken在这里是一个类变量,但是每个用户每次第三方登陆都应该获取一个新的值,
 * 因此QQImpl应该是一个多例的,不能用@Component及其相关的注解
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
    //private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 获取openid的url, opendid与QQ号一一对应
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 获取用户信息的url
     * 实际url中还包括参数access_token,但我们不需要管,因为AbstractOAuth2ApiBinding会帮我们加上
     */
    private static final String URL_GET_USERINFO =
            "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    /**
     * 本项目(就是我们的项目)如想用QQ作为第三方登陆,需在QQ官方进行注册,
     * 注册后QQ会利用两个东西来认定是我们的项目
     * 一个是appId--------------相当于本项目在QQ的用户名
     * 另一个是appSecret--------相当于本项目在QQ的密码
     */
    private String appId;
    /**
     * 用户的id与QQ号一一对应,可以通过请求获得
     */
    private String openId;

    /**
     * @param accessToken 走完OAuth2协议的5步后获得
     * @param appId       配置在我们的配置文件里
     */
    public QQImpl(String accessToken, String appId) {

        //在利用restTemplate发起请求时默认将access_token作为请求参数
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        String url = String.format(URL_GET_OPENID, accessToken);

        //至于下面的请求为什么还要传入accessToken,或者是不是可以不传,我会回过头来再来探索
        String result = getRestTemplate().getForObject(url, String.class);
        log.info(" 调用OpenAPI接口,获取到的数据为:{}", result);

        /**
         * openid的返回参数结构为:
         * callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
         * 所以可以按照如下的方式获取到openid
         */
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    /**
     * @return
     */
    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USERINFO, appId, openId);
        //不用管access_token,父类AbstractOAuth2ApiBinding会自动将其挂上作为请求参数
        String result = getRestTemplate().getForObject(url, String.class);
        log.info("调用获取用户信息接口,获得的数据为:{}", result);

        QQUserInfo userInfo = null;
        try {
            //objectMapper解析时需要字段一样，不然就会报错，所以这里用了FastJson
            //userInfo = objectMapper.readValue(result, QQUserInfo.class);
            userInfo = JSON.parseObject(result,QQUserInfo.class);
            userInfo.setOpenId(openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
