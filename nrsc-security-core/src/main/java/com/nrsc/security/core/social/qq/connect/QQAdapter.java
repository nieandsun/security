package com.nrsc.security.core.social.qq.connect;

import com.nrsc.security.core.social.qq.api.QQ;
import com.nrsc.security.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @author : Sun Chuan
 * @date : 2019/8/7 20:21
 * Description:
 */
public class QQAdapter implements ApiAdapter<QQ> {
    /**
     * 用来测试当前服务是否是通的，这里假设服务一直是通的
     *
     * @param api
     * @return
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     *
     * 将我们从QQ获取的用户信息设置成Connection对象相对应的字段信息
     * 记住：connection对象的字段是固定的
     * @param api
     * @param values
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo userInfo = api.getUserInfo();

        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        values.setProfileUrl(null);//个人主页url，QQ没有，这里设null
        values.setProviderUserId(userInfo.getOpenId());
    }

    /**
     * 同上面的方法一样也是通过api拿到一个标准的用户信息---》之后会讲
     * @param api
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    /***
     * 微博等更新主页信息---这里不用管
     * @param api
     * @param message
     */
    @Override
    public void updateStatus(QQ api, String message) {
        //do nothing
    }
}
